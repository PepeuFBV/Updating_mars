package mars.mips.SO.ProcessManager;

public abstract class Scheduler {
    
    /**
     * Method for scheduling the next ready process.
     */
    public static void schedule() {
        ProcessControlBlock nextProcess;
        if (!ProcessTable.getReadyProcesses().isEmpty()) {
            nextProcess = ProcessTable.getReadyProcesses().pop(); // Remove the process from ready process list
            nextProcess.copyToHardware();// Retrieves the context to PCB
            nextProcess.setState(ProcessControlBlock.ProcessState.RUNNING); // Change state
            ProcessTable.setExecutionProcess(nextProcess); // Set the new process in execution
        }
    }

}
