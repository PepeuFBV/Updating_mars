package mars.mips.SO.ProcessManager;

import java.util.LinkedHashMap;
import java.util.Map;

public class VirtualTable {

  class Block {
    public int[] instructions = new int[MemoryManager.tamPagVirtual];

    public Block(int[] instructions) {
      for (int i = 0; i < instructions.length; i++) {
        this.instructions[i] = instructions[i];
      }
    }
  }

  public static Map<ProcessControlBlock, Block[]> tabelaPaginas = new LinkedHashMap<>();
}
