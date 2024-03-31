package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.hardware.RegisterFile;

public class SyscallDownSemaphore extends AbstractSyscall {

    /**
     * Constructor is provided so subclass may initialize instance variables.
     */
    public SyscallDownSemaphore() {
        super(23, "DownSemaphore");
    }

    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {
        int semaphoreAddress = RegisterFile.getValue(4);

        ProcessTable.semaphores.forEach(s -> {
            if (s.address == semaphoreAddress) {
                s.down();
            }
        });
    }
}
