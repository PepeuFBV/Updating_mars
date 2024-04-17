package mars.mips.SO.ProcessManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class VirtualTable {

    /**
     * Represents a block of instructions in the virtual memory.
     * Also known as a virtual table entry.
     */
    public class VirtualTableEntry {

        private int[] instructions;
        private int cont;

        private boolean referencedPage;
        private boolean modifiedPage;
        private enum protectionBits {
            R, W, X, RW, RX, WX, RWX
        }
        private protectionBits protection;
        private boolean present;
        private int frameNumber;

        public int[] getInstructions() {
            return instructions;
        }

        public VirtualTableEntry() {
            instructions = new int[MemoryManager.VIRTUAL_PAGE_SIZE];
            cont = 0;
        }

        public VirtualTableEntry(int[] instructions) {
            if (instructions.length > 4) {
                return;
            }
            instructions = new int[MemoryManager.VIRTUAL_PAGE_SIZE];
            System.arraycopy(instructions, 0, this.instructions, 0, instructions.length);
            cont = instructions.length;
        }

        public void addInstruction(int address) {
            if (cont < MemoryManager.VIRTUAL_PAGE_SIZE) {
                instructions[cont] = address;
                cont++;
            }
        }
    }

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

    public boolean addInstruction(int addressInstructions) {
        ProcessControlBlock process = ProcessTable.getExecutionProcess();

        VirtualTableEntry[] entries = pageTable.get(process);

        if (entries == null) {
            entries = new VirtualTableEntry[1];
            entries[0].addInstruction(addressInstructions);
            pageTable.put(process, entries);

            return true;
        } else {
            for (VirtualTableEntry entry : entries) {
                if (entry.cont < MemoryManager.VIRTUAL_PAGE_SIZE) {
                    entry.addInstruction(addressInstructions);
                    
                    return true;
                }
            }

            if (entries.length < 16) {
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
}