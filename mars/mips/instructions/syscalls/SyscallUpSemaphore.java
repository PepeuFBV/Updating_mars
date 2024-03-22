package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;

public class SyscallUpSemaphore extends AbstractSyscall {
    /**
     * Constructor is provided so subclass may initialize instance variables.
     */
    public SyscallUpSemaphore() {
        super(24, "UpSemaphore");
    }

    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {

    }
}
