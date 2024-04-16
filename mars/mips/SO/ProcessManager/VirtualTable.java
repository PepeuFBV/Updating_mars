package mars.mips.SO.ProcessManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class VirtualTable {

    public class Block {

        public int[] instructions;
        public int cont;

        public Block() {
            instructions = new int[MemoryManager.VIRTUAL_PAGE_SIZE];
            cont = 0;
        }

        public Block(int[] instructions) {
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

    public static final Map<ProcessControlBlock, Block[]> pageTable = new LinkedHashMap<>();

    public boolean addPage(int[] addressInstructions) {
        ProcessControlBlock process = ProcessTable.getExecutionProcess();

        // Check if there is a running process
        if (process == null) {
            return false; // If not, cannot add the page
        }

        Block[] blocks = pageTable.get(process);

        // If the process doesn't have any associated blocks yet, create a new block
        // array
        if (blocks == null) {
            blocks = new Block[1];
            blocks[0] = new Block(addressInstructions);
            pageTable.put(process, blocks);
            return true;
        }

        // If the process already has associated blocks and there are already 16 blocks,
        // return false
        if (blocks.length >= 16) {
            return false;
        }

        // If the process already has associated blocks and there are less than 16
        // blocks, add the new block
        Block[] newBlocks = new Block[blocks.length + 1];
        System.arraycopy(blocks, 0, newBlocks, 0, blocks.length);
        newBlocks[blocks.length] = new Block(addressInstructions);
        pageTable.put(process, newBlocks);
        return true;
    }

    public boolean addInstruction(int addressInstructions) {
        ProcessControlBlock process = ProcessTable.getExecutionProcess();

        Block[] blocks = pageTable.get(process);

        if (blocks == null) {
            blocks = new Block[1];
            blocks[0].addInstruction(addressInstructions);
            pageTable.put(process, blocks);

            return true;
        } else {
            for (Block block : blocks) {
                if (block.cont < MemoryManager.VIRTUAL_PAGE_SIZE) {
                    block.addInstruction(addressInstructions);

                    return true;
                }
            }

            if (blocks.length < 16) {
                Block[] newBlocks = new Block[blocks.length + 1];
                System.arraycopy(blocks, 0, newBlocks, 0, blocks.length);
                newBlocks[blocks.length] = new Block();
                newBlocks[blocks.length].addInstruction(addressInstructions);
                pageTable.put(process, newBlocks);
    
                return true;
            }
        }

        return false;
    }
}
