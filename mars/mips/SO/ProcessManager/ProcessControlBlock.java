package mars.mips.SO.ProcessManager;

import mars.mips.hardware.Memory;
import mars.mips.hardware.Register;
import mars.mips.hardware.RegisterFile;

public class ProcessControlBlock {

    // Process priority
    private int priority;
    private int upperLimit;
    private int lowerLimit;
    private int hits;
    private int misses;
    private int pageFaults;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    // Special registers storage
    public static final int GLOBAL_POINTER_REGISTER = 28;
    public static final int STACK_POINTER_REGISTER = 29;

    // Registers array
    private final Register[] regFile = {
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
     * @param priority
     */
    public ProcessControlBlock(int pid, int programAddress, ProcessState state, int priority) {
        try {
            this.pid = pid;
            setProgramAddress(programAddress);
            this.state = state;
            this.priority = priority;
            this.hits = 0;
            this.misses = 0;
            this.pageFaults = 0;
        } catch (RuntimeException e) {
            System.err.println("Error: ProcessControlBlock creation failed.");
        }
    }

    public ProcessControlBlock(int pid, int programAddress, ProcessState state, int priority, int superiorLimit) {
        try {
            this.pid = pid;
            setProgramAddress(programAddress);
            this.state = state;
            this.priority = priority;
            this.upperLimit = superiorLimit;
            this.hits = 0;
            this.misses = 0;
            this.pageFaults = 0;
        } catch (RuntimeException e) {
            System.err.println("Error: ProcessControlBlock creation failed.");
        }
    }

    public ProcessControlBlock(int pid, int programAddress, ProcessState state) {
        try {
            this.pid = pid;
            setProgramAddress(programAddress);
            this.state = state;
            this.priority = 1;
            this.hits = 0;
            this.misses = 0;
            this.pageFaults = 0;
        } catch (RuntimeException e) {
            System.err.println("Error: ProcessControlBlock creation failed.");
        }
    }

    /**
     * Method for displaying the register values for debugging.
     *
     */
    public void showRegisters() {
        System.out.println("Process " + pid + " " + programAddress + " " + state + " " + priority + " registers:");
        for (Register rf : regFile) {
            System.out.println("Name: " + rf.getName());
            System.out.println("Value: " + rf.getValue());
        }
    }

    public Register[] getRegisters() {
        return regFile;
    }

    // More special registers
    private Register programCounter = new Register("pc", 32, Memory.textBaseAddress);
    private Register hi = new Register("hi", 33, 0);// this is an internal register with arbitrary number
    private Register lo = new Register("lo", 34, 0);// this is an internal register with arbitrary number

    public Register getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(Register programCounter) {
        this.programCounter = programCounter;
    }

    public Register getHi() {
        return hi;
    }

    public void setHi(Register hi) {
        this.hi = hi;
    }

    public Register getLo() {
        return lo;
    }

    public void setLo(Register lo) {
        this.lo = lo;
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
        return pid;
    }

    public int getProgramAddress() {
        return programAddress;
    }

    private void setProgramAddress(int programAddress) throws RuntimeException {
        if (programAddress != 0) {
            this.programAddress = programAddress;
        } else {
            throw new RuntimeException("Error: program address is null.");
        }
    }

    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
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


    /**
     * @return int return the hits
     */
    public int getHits() {
        return hits;
    }

    /**
     * @param hits the hits to set
     */
    public void setHits(int hits) {
        this.hits = hits;
    }

    /**
     * @return int return the misses
     */
    public int getMisses() {
        return misses;
    }

    /**
     * @param misses the misses to set
     */
    public void setMisses(int misses) {
        this.misses = misses;
    }

    /**
     * @return int return the pageFaults
     */
    public int getPageFaults() {
        return pageFaults;
    }

    /**
     * @param pageFaults the pageFaults to set
     */
    public void setPageFaults(int pageFaults) {
        this.pageFaults = pageFaults;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

}
