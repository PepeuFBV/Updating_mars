package mars.mips.SO.ProcessManager;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

import mars.mips.hardware.AddressErrorException;

/**
 * Memory Management Unit
 */
public abstract class MMU {
<<<<<<< Updated upstream
  /**
   * Represents a block of instructions in the virtual memory.
   * Also known as a virtual table entry.
   */
  public static final Map<ProcessControlBlock, VirtualTableEntry[]> pageTable = new LinkedHashMap<>();
  public static Map<ProcessControlBlock, Queue<VirtualTableEntry>> lastPage = new LinkedHashMap<>();
=======
>>>>>>> Stashed changes

    /**
     * Represents a block of instructions in the virtual memory. Also known as a
     * virtual table entry.
     */
    public static final Map<ProcessControlBlock, VirtualTableEntry[]> pageTable = new LinkedHashMap<>(
            Collections.singletonMap(ProcessTable.getExecutionProcess(), new VirtualTableEntry[MemoryManager.maxNumPages])
    );

<<<<<<< Updated upstream
    if (pageTable.length < MemoryManager.maxNumPages) {
      if (pageTable[numPage] == null) {
        VirtualTableEntry newPage = new VirtualTableEntry();
        newPage.addInstruction(addressInstructions, displacement);
        newPage.setPresent(true);
        pageTable[numPage] = newPage;
        MMU.pageTable.put(process, pageTable);
        MMU.lastPage.get(process).add(newPage);
      } else {
        pageTable[numPage].addInstruction(addressInstructions, displacement);
        MMU.pageTable.put(process, pageTable);
      }
    } else {
      // ALGORITMO DE SUBSTITUIÇÃO DE PÁGINA.
      process.setPageFaults(process.getPageFaults() + 1);
=======
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
            System.out.println("Entrando aqui?");
        }
>>>>>>> Stashed changes
    }

<<<<<<< Updated upstream
  public static void deletProcess(ProcessControlBlock process) {
    MMU.pageTable.remove(process);
  }

  public static void verifyInstruction(int address) throws AddressErrorException {
    // Verify if the instruction is in the page table
    // If not, call the page fault handler
    ProcessControlBlock process = ProcessTable.getExecutionProcess();
    VirtualTableEntry[] pageTable = MMU.pageTable.get(process);
    
    int index = ((address / (MemoryManager.pageSize * 4)) % MemoryManager.maxNumPages);
    int displacement = (address / 4) % MemoryManager.pageSize;
=======
    public static void verifyInstruction(int address) throws AddressErrorException {
        // Verify if the instruction is in the page table
        // If not, call the page fault handler
        ProcessControlBlock process = ProcessTable.getExecutionProcess();
        VirtualTableEntry[] virtualTableProcess = MMU.pageTable.get(process);

        int mascara = 0b00000011;
        int index = (address / (MemoryManager.pageSize * 4)) & mascara;
        int displacement = (address / 4) % MemoryManager.pageSize;
>>>>>>> Stashed changes

        if (virtualTableProcess[index] == null) {

            process.setMisses(process.getMisses() + 1);
            addInstruction(address, index, displacement);

        } else {
            if (virtualTableProcess[index].getInstructions()[displacement] == address && virtualTableProcess[index].isPresent()) {
                process.setHits(process.getHits() + 1);
                virtualTableProcess[index].setReferencedPage(true);
                return;
            }

            process.setMisses(process.getMisses() + 1);
            addInstruction(address, index, displacement);
        }
    }
}
