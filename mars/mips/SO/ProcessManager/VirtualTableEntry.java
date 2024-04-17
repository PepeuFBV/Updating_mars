package mars.mips.SO.ProcessManager;

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

  public int getCont() {
    return cont;
  }

  public int[] getInstructions() {
    return instructions;
  }

  public VirtualTableEntry() {
    instructions = new int[MemoryManager.pageSize];
    cont = 0;
  }

  public VirtualTableEntry(int[] instructions) {
    if (instructions.length > 4) {
      return;
    }
    instructions = new int[MemoryManager.pageSize];
    System.arraycopy(instructions, 0, this.instructions, 0, instructions.length);
    cont = instructions.length;
  }

  public void addInstruction(int address) {
    if (cont < MemoryManager.pageSize) {
      instructions[cont] = address;
      cont++;
    }
  }
}
