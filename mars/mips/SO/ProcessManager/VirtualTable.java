package mars.mips.SO.ProcessManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class VirtualTable {

    public class Block {

        public int[] instructions = new int[MemoryManager.VIRTUAL_PAGE_SIZE];

        public Block(int[] instructions) {
            System.arraycopy(instructions, 0, this.instructions, 0, instructions.length);
        }
    }

    public static final Map<ProcessControlBlock, Block[]> pageTable = new LinkedHashMap<>();
    
}
