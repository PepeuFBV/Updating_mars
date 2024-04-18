package mars.mips.instructions.syscalls;

import mars.ProcessingException;
import mars.ProgramStatement;
import mars.mips.SO.ProcessManager.MMU;
import mars.mips.SO.ProcessManager.MemoryManager;
import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.VirtualTableEntry;
import mars.mips.hardware.RegisterFile;

/**
 * Service to create a process.
 */
public class SyscallFork extends AbstractSyscall {

    /**
     * Build a instance of the Syscall Fork. Default service number is 18, and
     * name is "Fork".
     */
    public SyscallFork() {
        super(18, "Fork");
    }

    /**
     * Performs syscall function to create a process with initial address in
     * $a0.
     *
     * @param statement
     * @throws ProcessingException
     */
    @Override
    public void simulate(ProgramStatement statement) throws ProcessingException {
        // Gets the initial address of the process in $a0
        int processAddress = RegisterFile.getValue(4);

        // Instantiate a new PCB for the process
        ProcessControlBlock fork = new ProcessControlBlock(ProcessTable.getPID(), processAddress,
                ProcessControlBlock.ProcessState.READY);

        // Defines the upper and lower limits
        fork.setUpperLimit(processAddress);
        fork.setLowerLimit(MemoryManager.PROGRAM_END_ADDRESS);

        // Verify whether process address limits conflict and corrects them
        MemoryManager.verifyBounds(fork);
        
        // If SyscallFork(address, priority) has been called
        if (RegisterFile.getValue(6) == 1) {
            // Gets the priority value in $a1
            int priority = RegisterFile.getValue(5);

            fork.setPriority(priority);
        }
        
        
        var pageTable = new VirtualTableEntry[MemoryManager.pageSize];
        MMU.pageTable.put(fork, pageTable);

        // Save the actual context, same as execution process
        fork.copyFromHardware();

        // Updates the program counter to current program address
        fork.getProgramCounter().setValue(processAddress);

        // Add the new process in the process table
        ProcessTable.getReadyProcesses().addLast(fork);

        // For debug purposes
        System.out.println("Syscall Fork");
        ProcessTable.listProcesses();
    }

}
