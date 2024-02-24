package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.Scheduler;

public class SyscallProcessTerminate extends AbstractSyscall {
    
    /**
     * Default service number of syscall is 20 and name is "ProcessTerminate".
     */
    public SyscallProcessTerminate() {
        super(20, "ProcessTerminate");
    }
    
    /**
     * Perform the syscall of changing process state from running to ready,
     * save the context of the process and calls the scheduler.
     * @param statement
     * @throws ProcessingException 
     */
    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {
        Scheduler.schedule();
        
        // For debug purposes
        ProcessTable.listProcesses();
    }
    
}
