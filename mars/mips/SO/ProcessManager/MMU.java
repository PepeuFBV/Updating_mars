package mars.mips.SO.ProcessManager;

import java.util.LinkedHashMap;
import java.util.Map;
import mars.mips.hardware.AddressErrorException;

/**
 * Memory Management Unit
 */
public abstract class MMU {
  /**
   * Represents a block of instructions in the virtual memory.
   * Also known as a virtual table entry.
   */
  public static final Map<ProcessControlBlock, VirtualTableEntry[]> pageTable = new LinkedHashMap<>();

  public static void addInstruction(int addressInstructions, int numPage, int displacement) {
    ProcessControlBlock process = ProcessTable.getExecutionProcess();
    VirtualTableEntry[] pageTable = MMU.pageTable.get(process);

    if (pageTable.length < MemoryManager.maxNumPages) {
      if (pageTable[numPage] == null) {
        VirtualTableEntry newPage = new VirtualTableEntry();
        newPage.addInstruction(addressInstructions, displacement);
        newPage.setPresent(true);
        pageTable[numPage] = newPage;
        MMU.pageTable.put(process, pageTable);
      } else {
        pageTable[numPage].addInstruction(addressInstructions, displacement);
        MMU.pageTable.put(process, pageTable);
      }
    } else {
      // ALGORITMO DE SUBSTITUIÇÃO DE PÁGINA.
      process.setPageFaults(process.getPageFaults() + 1);
    }
  }

  public static void verifyInstruction(int address) throws AddressErrorException {
    // Verify if the instruction is in the page table
    // If not, call the page fault handler
    ProcessControlBlock process = ProcessTable.getExecutionProcess();
    VirtualTableEntry[] pageTable = MMU.pageTable.get(process);

    int mascara = 0b00000011;
    int index = (address / (MemoryManager.pageSize * 4)) & mascara;
    int displacement = (address / 4) % MemoryManager.pageSize;

    if (pageTable[index] == null) {

      process.setMisses(process.getMisses() + 1);
      addInstruction(address, index, displacement);

    } else {
      if (pageTable[index].getInstructions()[displacement] == address && pageTable[index].isPresent()) {
        process.setHits(process.getHits() + 1);
        pageTable[index].setReferencedPage(true);
        return;
      }

      process.setMisses(process.getMisses() + 1);
      addInstruction(address, index, displacement);
    }
  }
}