package mars.mips.SO.ProcessManager;

import java.util.Deque;
import java.util.LinkedList;
import mars.Globals;
import mars.mips.hardware.AddressErrorException;

public class Semaphore {

    public final int address;
    private int value;
    private Deque<ProcessControlBlock> blockedProcesses;

    public Semaphore(int address) {
        this.address = address;
    }

    public Semaphore(int address, int value) {
        this.address = address;
        this.value = value;
        this.blockedProcesses = new LinkedList<>();
    }

    public void down() {
        if (value == 0) {
            // Blocks the current process and invokes the scheduler
            blockedProcesses.add(ProcessTable.getExecutionProcess());
            ProcessTable.getExecutionProcess().setState(ProcessControlBlock.ProcessState.BLOCKED);
            Scheduler.schedule();
            
            System.out.println("Semaphore Down");
            ProcessTable.listProcesses();
        } else {
            try {
                Globals.memory.setWord(address, --value);
            } catch (AddressErrorException ex) {
                System.err.println("Error in memory manipulation. " + ex);
            }
        }
    }

    public void up() {
        try {
            // Updates the semaphore value in MARS memory
            Globals.memory.setWord(address, ++value);
            
            // If there's process in blocked processes list, adds the first to the ready processes list
            if (!blockedProcesses.isEmpty()) {
                var process = blockedProcesses.removeFirst();
                process.setState(ProcessControlBlock.ProcessState.READY);
                ProcessTable.getReadyProcesses().addLast(process);
                
                System.out.println("Semaphore Up");
                ProcessTable.listProcesses();
            }
        } catch (AddressErrorException ex) {
            System.err.println("Error in memory manipulation. " + ex);
        }
    }

    public Deque<ProcessControlBlock> getBlockedProcesses() {
        return blockedProcesses;
    }

    @Override
    public String toString() {
        return "{ Address: " + address + ", Value: " + value + " }";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Semaphore other = (Semaphore) obj;
        return this.address == other.address;
    }

    @Override
    public int hashCode() {
        return 217 + this.address;
    }

}
