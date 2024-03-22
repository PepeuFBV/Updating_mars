package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.hardware.RegisterFile;

public class SyscallCreateSemaphore extends AbstractSyscall {

    /**
     * Constructor is provided so subclass may initialize instance variables.
     */
    public SyscallCreateSemaphore() {
        super(21, "CreateSemaphore");
    }

    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {
        int semaphoreValue = RegisterFile.getValue(4);

        ProcessTable.setSemaphores(new int[semaphoreValue]);
        ProcessTable.getSemaphores()[0] = 1;
    }
}
