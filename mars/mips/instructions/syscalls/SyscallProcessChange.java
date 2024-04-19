package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.Scheduler;
import mars.tools.TimerTool;

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
        System.out.println("Timer Tool is scheduling? " + TimerTool.isScheduling());
        if (!TimerTool.isScheduling()) {
            ProcessControlBlock process = ProcessTable.getExecutionProcess();

            process.copyFromHardware(); // Save the process context

            // Change the process state and add it to the list of ready processes
            ProcessTable.changeState(ProcessControlBlock.ProcessState.READY);
            ProcessTable.getReadyProcesses().addLast(process);

            Scheduler.schedule();
            
            // For debug purposes
            System.out.println("Syscall Process Change");
            ProcessTable.listProcesses();
        }
    }
    
}
