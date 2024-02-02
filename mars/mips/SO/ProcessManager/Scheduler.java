package mars.mips.SO.ProcessManager;

import mars.mips.hardware.Register;

public class Scheduler {
    
    private static ProcessTable table;
    
    /**
      * Class constructor.
      *  @param table Saved process table
      */
    public Scheduler(ProcessTable table) {
        this.table = table;
    }
    
    /**
     * Method for scheduling a ready process.
     * @return The next ready process or null if there isn't any
     */
    public static ProcessControlBlock schedule() {
        if (!table.getReadyProcesses().isEmpty()) {
            return table.getReadyProcesses().remove(0);
        }
        return null;
    }
    
    /**
      * Method for executing a process.
      *  @param process Process to be executed
      */
    public static void execute(ProcessControlBlock process) {
        process.setState(ProcessControlBlock.ProcessState.RUNNING);
        
        //increasing the PC value by + 1 TODO check if is right
        Register tempReg = process.getProgramCounter();
        tempReg = new Register(tempReg.getName(),tempReg.getNumber(),tempReg.getValue() + 1);
        
        process.copyToHardware();
    }
    
    /**
      * Method to finalize a process.
      * @param process Process to be finalized
      */
    public static void finalize(ProcessControlBlock process) {
        process.setState(ProcessControlBlock.ProcessState.BLOCKED);
        
        process.copyFromHardware();
    }
    
    /**
      * Method to run the scheduler
      */
    public void run() {
        ProcessControlBlock process;
        
        while ((process = schedule()) != null) {
            execute(process);
            finalize(process);
        }
    }
    
}
