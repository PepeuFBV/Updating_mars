package mars.mips.SO.ProcessManager;

public class VirtualTableEntry {

  private int[] instructions;

  private boolean referencedPage;
  private boolean modifiedPage;

  private enum protectionBits {
    R, W, X, RW, RX, WX, RWX
  }

  private protectionBits protection;
  private boolean present;
  private int frameNumber;
  private int size;

  public int[] getInstructions() {
    return instructions;
  }

  public VirtualTableEntry() {
    instructions = new int[MemoryManager.pageSize];
  }

  public VirtualTableEntry(int[] instructions) {
    if (instructions.length > 4) {
      return;
    }
    instructions = new int[MemoryManager.pageSize];
    size = 1;
    System.arraycopy(instructions, 0, this.instructions, 0, instructions.length);
  }

  public void addInstruction(int address, int displacement) {
    if (instructions[displacement] != 0) {
      size++;
    }

    instructions[displacement] = address;
    frameNumber = displacement;
  }

  public int size() {
    return size;
  }

  /**
   * @param instructions the instructions to set
   */
  public void setInstructions(int[] instructions) {
    this.instructions = instructions;
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
