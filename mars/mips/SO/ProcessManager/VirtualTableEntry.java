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

    /**
     * @param instructions the instructions to set
     */
    public void setInstructions(int[] instructions) {
        this.instructions = instructions;
    }

    /**
     * @param cont the cont to set
     */
    public void setCont(int cont) {
        this.cont = cont;
    }

    /**
     * @return boolean return the referencedPage
     */
    public boolean isReferencedPage() {
        return referencedPage;
    }

    /**
     * @param referencedPage the referencedPage to set
     */
    public void setReferencedPage(boolean referencedPage) {
        this.referencedPage = referencedPage;
    }

    /**
     * @return boolean return the modifiedPage
     */
    public boolean isModifiedPage() {
        return modifiedPage;
    }

    /**
     * @param modifiedPage the modifiedPage to set
     */
    public void setModifiedPage(boolean modifiedPage) {
        this.modifiedPage = modifiedPage;
    }

    /**
     * @return protectionBits return the protection
     */
    public protectionBits getProtection() {
        return protection;
    }

    /**
     * @param protection the protection to set
     */
    public void setProtection(protectionBits protection) {
        this.protection = protection;
    }

    /**
     * @return boolean return the present
     */
    public boolean isPresent() {
        return present;
    }

    /**
     * @param present the present to set
     */
    public void setPresent(boolean present) {
        this.present = present;
    }

    /**
     * @return int return the frameNumber
     */
    public int getFrameNumber() {
        return frameNumber;
    }

    /**
     * @param frameNumber the frameNumber to set
     */
    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

}
