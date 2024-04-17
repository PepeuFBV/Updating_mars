package mars.mips.SO.ProcessManager;

import java.util.LinkedHashMap;
import java.util.Map;

import mars.mips.hardware.AddressErrorException;
import mars.simulator.Simulator;

/**
 * Memory Management Unit
 */
public abstract class MMU {
  /**
   * Represents a block of instructions in the virtual memory.
   * Also known as a virtual table entry.
   */
  public static final Map<ProcessControlBlock, VirtualTableEntry[]> pageTable = new LinkedHashMap<>();

  public boolean addPage(int[] addressInstructions) {
    ProcessControlBlock process = ProcessTable.getExecutionProcess();

    // Check if there is a running process
    if (process == null) {
      return false; // If not, cannot add the page
    }

    VirtualTableEntry[] tableEntries = pageTable.get(process);

    // If the process doesn't have any associated blocks yet, create a new block
    // array
    if (tableEntries == null) {
      tableEntries = new VirtualTableEntry[1];
      tableEntries[0] = new VirtualTableEntry(addressInstructions);
      pageTable.put(process, tableEntries);
      return true;
    }

    // If the process already has associated blocks and there are already 16 blocks,
    // return false
    if (tableEntries.length >= 16) {
      return false;
    }

    // If the process already has associated blocks and there are less than 16
    // blocks, add the new block
    VirtualTableEntry[] newBlocks = new VirtualTableEntry[tableEntries.length + 1];
    System.arraycopy(tableEntries, 0, newBlocks, 0, tableEntries.length);
    newBlocks[tableEntries.length] = new VirtualTableEntry(addressInstructions);
    pageTable.put(process, newBlocks);
    return true;
  }

  public static boolean addInstruction(int addressInstructions, int numPage, int displacement) {
    ProcessControlBlock process = ProcessTable.getExecutionProcess();
    VirtualTableEntry[] pageTable = MMU.pageTable.get(process);

    if (pageTable.length < MemoryManager.maxNumPages){
      
    } else {
      //  ALGORITMO DE SUBSTITUIÇÃO DE PÁGINA.
    }

    return false;
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

      if (address < process.getUpperLimit() || address > process.getLowerLimit()) {
        String errorMessage = "\nThe address limits of the running process, "
            + "which has an upper limit: " + process.getUpperLimit() + " \nand lower limit: "
            + process.getLowerLimit() + " are outside the access area."
            + "\nAccess attempt address: " + address + " ";
        throw new AddressErrorException(errorMessage, Simulator.EXCEPTION, address);
      }

      if (pageTable[index].getInstructions()[displacement] == address) {
        process.setHits(process.getHits() + 1);
        pageTable[index].setReferencedPage(true);
        return;
      }

      process.setMisses(process.getMisses() + 1);
      addInstruction(address, index, displacement);
    }
  }

  // private static void pageFaultHandler(ProcessControlBlock process, int
  // address, int pageNumber) {
  // process.setPageFaults(process.getPageFaults() + 1);

  // VirtualTableEntry[] pageTable = MMU.pageTable.get(process);
  // int[] instructions = new int[MemoryManager.pageSize];
  // }
}