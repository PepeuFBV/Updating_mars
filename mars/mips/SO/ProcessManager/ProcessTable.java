package mars.mips.SO.ProcessManager;

import java.util.Deque;
import java.util.LinkedList;
import mars.mips.hardware.RegisterFile;

public abstract class ProcessTable {

	// Initial generated PID value for new processes
	private static int PID = 1;

	// Possible processes
	private static Deque<ProcessControlBlock> readyProcesses = new LinkedList<>();
	private static ProcessControlBlock executionProcess = new ProcessControlBlock(0,
			RegisterFile.getInitialProgramCounter(), ProcessControlBlock.ProcessState.RUNNING);

	public static Deque<ProcessControlBlock> getReadyProcesses() {
		return readyProcesses;
	}

	public static void setReadyProcesses(Deque<ProcessControlBlock> readyProcesses) {
		ProcessTable.readyProcesses = readyProcesses;
	}

	public static ProcessControlBlock getExecutionProcess() {
		return executionProcess;
	}

	public static void setExecutionProcess(ProcessControlBlock executionProcess) {
		ProcessTable.executionProcess = executionProcess;
	}

	// PID is unique and incremets for each new process
	public static int getPID() {
		return PID++;
	}

	/**
	 * Method for changing a process's state in the table.
	 * 
	 * @param newState New process state
	 */
	public static void changeState(ProcessControlBlock.ProcessState newState) {
		executionProcess.setState(newState);
	}

	/**
	 * Method for displaying all processes for debugging.
	 */
	public static void listProcesses() {
		System.out.println("=================================");
		System.out.println("PID\tAddress\tState\tpriority");

		// Show ready processes
		if (SchedulerE.PRIORITY.equals(schedulingAlgorithm)) {
			for (ProcessControlBlock processo : readyProcesses) {
				System.out.println(processo.getPid() + "\t" + processo.getProgramAddress() + "\t" + processo.getState() + "\t"
						+ processo.getPriority());
			}

			System.out.println(
				executionProcess.getPid() + "\t" + executionProcess.getProgramAddress() + "\t" + executionProcess.getState() + "\t"
						+ executionProcess.getPriority());
		} else {
			for (ProcessControlBlock processo : readyProcesses) {
				System.out.println(
						processo.getPid() + "\t" + processo.getProgramAddress() + "\t" + processo.getState() + "\t" + "N/A");
			}

			System.out.println(
				executionProcess.getPid() + "\t" + executionProcess.getProgramAddress() + "\t" + executionProcess.getState() + "\t"
						+ "N/A");
		}

		// Show execution process
		
		System.out.println("=================================");
	}

	private static SchedulerE schedulingAlgorithm = SchedulerE.FIFO;

	public static SchedulerE getSchedulingAlgorithm() {
		return schedulingAlgorithm;
	}

	public static void setSchedulingAlgorithm(SchedulerE algorithm) {
		schedulingAlgorithm = algorithm;
	}

}
