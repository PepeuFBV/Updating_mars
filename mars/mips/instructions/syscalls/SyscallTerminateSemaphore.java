package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;

public class SyscallTerminateSemaphore extends AbstractSyscall {
    /**
     * Constructor is provided so subclass may initialize instance variables.
     */
    public SyscallTerminateSemaphore() {
        super(22, "TerminateSemaphore");
    }

    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {

    }
}
