package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.Scheduler;

/**
 * Service to change the state of the running process to ready.
 */
public class SyscallProcessChange extends AbstractSyscall {
    
    /**
     * Default service number of syscall is 19 and name is "ProcessChange".
     */
    public SyscallProcessChange() {
        super(19, "ProcessChange");
    }
    
    /**
     * Perform the syscall of changing process state from running to ready,
     * save the context of the process and calls the scheduler.
     * @param statement
     * @throws ProcessingException 
     */
    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {
        ProcessControlBlock process = ProcessTable.getExecutionProcess();
        
        process.copyFromHardware(); // Save the process context
        
        // Change the process state and add it to the list of ready processes
        ProcessTable.changeState(ProcessControlBlock.ProcessState.READY);
        ProcessTable.getReadyProcesses().addLast(process);
        
        Scheduler.fifo();
        
        // For debug purposes
        ProcessTable.listProcesses();
    }
    
}
