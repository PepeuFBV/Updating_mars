package mars.mips.instructions.syscalls;

import java.util.Arrays;
import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.hardware.RegisterFile;

public class SyscallTerminateSemaphore extends AbstractSyscall {

    /**
     * Constructor is provided so subclass may initialize instance variables.
     */
    public SyscallTerminateSemaphore() {
        super(22, "TerminateSemaphore");
    }

    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {
        int semaphoreAddress = RegisterFile.getValue(4);

        // Removes the semaphore from the semaphores list
        System.out.println("Removing semaphore...");
        if (!ProcessTable.semaphores.removeIf(s -> s.address == semaphoreAddress)) {
            System.err.println("This semaphore doesn't exist in the semaphores list!");
        } else {
            System.out.println("Semaphores list:");
            ProcessTable.listSemaphores();
        }
    }
    
}
