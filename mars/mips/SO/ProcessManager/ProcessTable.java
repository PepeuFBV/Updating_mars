package mars.mips.SO.ProcessManager;

import java.util.Deque;
import java.util.LinkedList;
import mars.mips.hardware.RegisterFile;

public abstract class ProcessTable {

	// Initial generated PID value for new processes
	private static int PID = 1;

	// Possible processes
	private static Deque<ProcessControlBlock> readyProcesses = new LinkedList<>();
	private static Deque<ProcessControlBlock> blockedProcesses = new LinkedList<>();
	private static ProcessControlBlock executionProcess = new ProcessControlBlock(0,RegisterFile.getInitialProgramCounter(), ProcessControlBlock.ProcessState.RUNNING, 5);
	private static int[] semaphores;

	public static void setSemaphores(int[] semaphores) {
		ProcessTable.semaphores = semaphores;
	}

	public static int[] getSemaphores() {
		return semaphores;
	}

	public static Deque<ProcessControlBlock> getReadyProcesses() {
		return readyProcesses;
	}

	public static void setReadyProcesses(Deque<ProcessControlBlock> readyProcesses) {
		ProcessTable.readyProcesses = readyProcesses;
	}

	public static Deque<ProcessControlBlock> getBlockedProcesses() {
		return blockedProcesses;
	}

	public static void setBlockedProcesses(Deque<ProcessControlBlock> blockedProcesses) {
		ProcessTable.blockedProcesses = blockedProcesses;
	}

	public static ProcessControlBlock getExecutionProcess() {
		return executionProcess;
	}

	public static void setExecutionProcess(ProcessControlBlock executionProcess) {
		ProcessTable.executionProcess = executionProcess;
	}

    // PID is unique and increments for each new process
	public static int getPID() {
		return PID++;
	}

	/**
	 * Method for changing a process's state in the table.
	 * @param newState New process state
	 */
	public static void changeState(ProcessControlBlock.ProcessState newState) {
		executionProcess.setState(newState);
	}

	/**
	 * Method for displaying all processes for debugging.
	 */
	public static void listProcesses() {
        System.out.println("PID\tAddress\tState\tPriority");
        
        // Show ready processes
        for (ProcessControlBlock process : readyProcesses) {
            System.out.println(process.getPid() + "\t" + process.getProgramAddress() + "\t" + process.getState() + "\t" + process.getPriority());
        }
        
        // Show execution process
        System.out.println(executionProcess.getPid() + "\t" + executionProcess.getProgramAddress() + "\t" + executionProcess.getState() + "\t" + executionProcess.getPriority());
	}

	private static SchedulerE schedulingAlgorithm = SchedulerE.FIFO;

    public static SchedulerE getSchedulingAlgorithm() {
        return schedulingAlgorithm;
    }

    public static void setSchedulingAlgorithm(String algorithm) {
        switch (algorithm) {
            case "FIFO" -> schedulingAlgorithm = SchedulerE.FIFO;
            case "Priority" -> schedulingAlgorithm = SchedulerE.PRIORITY;
            case "Lottery" -> schedulingAlgorithm = SchedulerE.LOTTERY;
        }
    }

}
