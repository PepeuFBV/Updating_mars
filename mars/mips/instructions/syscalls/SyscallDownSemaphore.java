package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;

public class SyscallDownSemaphore extends AbstractSyscall {
    /**
     * Constructor is provided so subclass may initialize instance variables.
     */
    public SyscallDownSemaphore() {
        super(23, "DownSemaphore");
    }

    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {

    }
}
