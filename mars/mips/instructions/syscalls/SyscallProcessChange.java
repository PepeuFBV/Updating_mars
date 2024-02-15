package mars.mips.instructions.syscalls;

import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        process.copyFromHardware(); // Save the process's context
        
        // Change the process's state and adds to ready process list
        try {
            ProcessTable.changeState(ProcessControlBlock.ProcessState.READY);
            ProcessTable.getReadyProcesses().addLast(process);
        } catch (Exception ex) {
            Logger.getLogger(SyscallProcessChange.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scheduler.schedule();
        
        // For debug purposes
        ProcessTable.listProcesses();
    }
    
}
