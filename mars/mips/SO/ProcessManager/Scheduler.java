package mars.mips.SO.ProcessManager;

public abstract class Scheduler {
    
    /**
     * Method for scheduling the next ready process.
     * This works like a FIFO process list: the first process in ready process list is selected to be executed.
     */
    public static void schedule() {
        ProcessControlBlock nextProcess;
        if (!ProcessTable.getReadyProcesses().isEmpty()) {
            
            // Remove the process from ready process list
            nextProcess = ProcessTable.getReadyProcesses().pop();
            
            // Retrieves the context from PCB to hardware
            nextProcess.copyToHardware();
            
            // Change state
            nextProcess.setState(ProcessControlBlock.ProcessState.RUNNING);
            
            // Set the new execution process
            ProcessTable.setExecutionProcess(nextProcess);
        }
    }

}
