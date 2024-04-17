package mars.mips.SO.ProcessManager;

import java.util.LinkedHashMap;
import java.util.Map;

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

  public static boolean addInstruction(int addressInstructions, int numPage) {
    ProcessControlBlock process = ProcessTable.getExecutionProcess();

    VirtualTableEntry[] entries = pageTable.get(process);

    if (entries == null) {
      entries = new VirtualTableEntry[1];
      entries[0].addInstruction(addressInstructions);
      pageTable.put(process, entries);

      return true;
    } else {
      for (VirtualTableEntry entry : entries) {
        if (entry.getCont() < MemoryManager.pageSize) {
          entry.addInstruction(addressInstructions);

          return true;
        }
      }

      if (entries.length < MemoryManager.maxNumPages) {
        VirtualTableEntry[] newEntries = new VirtualTableEntry[entries.length + 1];
        System.arraycopy(entries, 0, newEntries, 0, entries.length);
        newEntries[entries.length] = new VirtualTableEntry();
        newEntries[entries.length].addInstruction(addressInstructions);
        pageTable.put(process, newEntries);

        return true;
      }
    }

    return false;
  }

  public static void verifyInstruction(int address) {
    // Verify if the instruction is in the page table
    // If not, call the page fault handler
    ProcessControlBlock process = ProcessTable.getExecutionProcess();
    VirtualTableEntry[] pageTable = MMU.pageTable.get(process);
    int pageNumber = (address / 4) % MemoryManager.pageSize;
    if (pageTable[pageNumber] == null) {
      addInstruction(address, pageNumber);
    } else {
      for (int instruction : pageTable[pageNumber].getInstructions()) {
        if (instruction == address) {
          process.setHits(process.getHits() + 1);
          return;
        } 
      }

      process.setMisses(process.getMisses() + 1);
    }
  }

  // private static void pageFaultHandler(ProcessControlBlock process, int
  // address, int pageNumber) {
  // process.setPageFaults(process.getPageFaults() + 1);

  // VirtualTableEntry[] pageTable = MMU.pageTable.get(process);
  // int[] instructions = new int[MemoryManager.pageSize];
  // }
}