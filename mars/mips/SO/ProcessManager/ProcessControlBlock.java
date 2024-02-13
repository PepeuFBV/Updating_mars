package mars.mips.SO.ProcessManager;

import mars.mips.hardware.Memory;
import mars.mips.hardware.Register;
import mars.mips.hardware.RegisterFile;

public class ProcessControlBlock {
    
    // Special registers storage
    public static final int GLOBAL_POINTER_REGISTER = 28;
    public static final int STACK_POINTER_REGISTER = 29;
    
    // Registers array
    private static final Register[] regFile = {
        new Register("$zero", 0, 0), new Register("$at", 1, 0),
        new Register("$v0", 2, 0), new Register("$v1", 3, 0),
        new Register("$a0", 4, 0), new Register("$a1", 5, 0),
        new Register("$a2", 6, 0), new Register("$a3", 7, 0),
        new Register("$t0", 8, 0), new Register("$t1", 9, 0),
        new Register("$t2", 10, 0), new Register("$t3", 11, 0),
        new Register("$t4", 12, 0), new Register("$t5", 13, 0),
        new Register("$t6", 14, 0), new Register("$t7", 15, 0),
        new Register("$s0", 16, 0), new Register("$s1", 17, 0),
        new Register("$s2", 18, 0), new Register("$s3", 19, 0),
        new Register("$s4", 20, 0), new Register("$s5", 21, 0),
        new Register("$s6", 22, 0), new Register("$s7", 23, 0),
        new Register("$t8", 24, 0), new Register("$t9", 25, 0),
        new Register("$k0", 26, 0), new Register("$k1", 27, 0),
        new Register("$gp", GLOBAL_POINTER_REGISTER, Memory.globalPointer),
        new Register("$sp", STACK_POINTER_REGISTER, Memory.stackPointer),
        new Register("$fp", 30, 0), new Register("$ra", 31, 0)
    };
    
    /**
     * Class initializer.
     *
     * @param pid Integer value that serves as process identifier
     * @param programAddress Address of the start of the program
     * @param state The current state of the process
      *
     */
    public ProcessControlBlock(int pid, int programAddress, ProcessState state) {
        try {
            this.pid = pid;
            setProgramAddress(programAddress);
            setState(state);
        } catch (RuntimeException e) {
            System.err.println("Error: ProcessControlBlock creation failed.");
        }
    }
    
    /**
     * Method for displaying the register values for debugging.
      *
     */
    public static void showRegisters() {
        for (Register rf : regFile) {
            System.out.println("Name: " + rf.getName());
            System.out.println("Number: " + rf.getNumber());
            System.out.println("Value: " + rf.getValue());
            System.out.println("");
        }
    }
    
    public static Register[] getRegisters() {
        return regFile;
    }
    
    // More special registers
    private static Register programCounter = new Register("pc", 32, Memory.textBaseAddress);
    private static Register hi = new Register("hi", 33, 0);//this is an internal register with arbitrary number
    private static Register lo = new Register("lo", 34, 0);// this is an internal register with arbitrary number
    
    public Register getProgramCounter() {
        return programCounter;
    }
    
    public void setProgramCounter(Register programCounter) {
        ProcessControlBlock.programCounter = programCounter;
    }
    
    public static Register getHi() {
        return hi;
    }
    
    public static void setHi(Register hi) {
        ProcessControlBlock.hi = hi;
    }
    
    public static Register getLo() {
        return lo;
    }
    
    public static void setLo(Register lo) {
        ProcessControlBlock.lo = lo;
    }
    
    // Logical atributes
    private int pid;
    private int programAddress;
    private ProcessState state;
    
    // Possible states for a process
    public enum ProcessState {
        READY, RUNNING, BLOCKED
    }
    
    public int getPid() {
        return this.pid;
    }
    
    public int getProgramAddress() {
        return this.programAddress;
    }
    
    public final void setProgramAddress(int programAddress) throws RuntimeException {
        if (programAddress != 0) {
            this.programAddress = programAddress;
        } else {
            throw new RuntimeException("Error: program address is null.");
        }
    }
    
    public ProcessState getState() {
        return this.state;
    }
    
    public final void setState(ProcessState state) {
        this.state = state;
    }
    
    /**
     * Method to copy the registers content from hardware this class.
     */
    public void copyFromHardware() {
        // Gets reference in registers array
        Register[] hardwareRegFile = RegisterFile.getRegisters();
        
        // Saves the normal registers
        for (int i = 0; i < regFile.length; i++) {
            regFile[i].setValue(hardwareRegFile[i].getValue());
        }
        
        // Saves the special registers
        programCounter.setValue(RegisterFile.getProgramCounter());
        hi.setValue(RegisterFile.getValue(33));
        lo.setValue(RegisterFile.getValue(34));
    }
    
    /**
     * Method to copy the registers content from this class to hardware.
     */
    public void copyToHardware() {
        // Gets reference in registers array
        Register[] hardwareRegFile = RegisterFile.getRegisters();

        // Saves the normal registers
        for (int i = 0; i < regFile.length; i++) {
            hardwareRegFile[i].setValue(regFile[i].getValue());
        }

        // Saves the special registers
        RegisterFile.setProgramCounter(programCounter.getValue());
        RegisterFile.updateRegister(33, hi.getValue());
        RegisterFile.updateRegister(34, lo.getValue());
    }
    
}
