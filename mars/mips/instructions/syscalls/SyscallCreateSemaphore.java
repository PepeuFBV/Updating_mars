package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.Semaphore;
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
        int semaphoreAddress = RegisterFile.getValue(4);    // Gets the semaphore address from $a0
        int semaphoreValue = RegisterFile.getValue(5);      // Gets the semaphore value from $a1

        System.out.println("Creating semaphore...");
        Semaphore semaphore = new Semaphore(semaphoreAddress, semaphoreValue);

        ProcessTable.semaphores.add(semaphore);
        System.out.println("Semaphores list:");
        ProcessTable.listSemaphores();
    }
}
