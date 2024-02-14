package mars.mips.SO.ProcessManager;

import java.util.Random;

public abstract class Scheduler {

	private static Random random = new Random();

	/**
	 * Method for scheduling the next ready process.
	 */
	public static void schedule() {
		switch (ProcessTable.getSchedulingAlgorithm()) {
		case FIFO:
			scheduleFIFO();
			break;
		case PRIORITY:
			schedulePriority();
			break;
		case LOTTERY:
			scheduleLottery();
			break;
		default:
			scheduleFIFO(); // Default to FIFO
			break;
		}
	}

	/**
	 * Schedule process using First-In-First-Out algorithm.
	 */
	private static void scheduleFIFO() {
		ProcessControlBlock nextProcess;
		if (!ProcessTable.getReadyProcesses().isEmpty()) {
			nextProcess = ProcessTable.getReadyProcesses().pop(); // Remove the process from ready process list
			nextProcess.copyToHardware(); // Retrieves the context to PCB
			nextProcess.setState(ProcessControlBlock.ProcessState.RUNNING); // Change state
			ProcessTable.setExecutionProcess(nextProcess); // Set the new process in execution
		}
	}

	/**
	 * Schedule process using Fixed Priority algorithm.
	 */
	private static void schedulePriority() {
		ProcessControlBlock highestPriorityProcess = null;
		int highestPriority = Integer.MIN_VALUE;

		for (ProcessControlBlock process : ProcessTable.getReadyProcesses()) {
			if (process.getPriority() > highestPriority) {
				highestPriority = process.getPriority();
				highestPriorityProcess = process;
			}
		}

		if (highestPriorityProcess != null) {
			highestPriorityProcess.copyToHardware(); // Retrieves the context to PCB
			highestPriorityProcess.setState(ProcessControlBlock.ProcessState.RUNNING); // Change state
			ProcessTable.setExecutionProcess(highestPriorityProcess); // Set the new process in execution
			ProcessTable.getReadyProcesses().remove(highestPriorityProcess); // Remove from ready queue
		}
	}

	/**
	 * Schedule process using Lottery algorithm.
	 */
	private static void scheduleLottery() {
		if (!ProcessTable.getReadyProcesses().isEmpty()) {
			int totalTickets = 0;
			for (ProcessControlBlock process : ProcessTable.getReadyProcesses()) {
				totalTickets += process.getPriority(); // Suponha que a prioridade do processo é usada como número de
														// bilhetes
			}

			int winningTicket = random.nextInt(totalTickets);
			int cumulativeTickets = 0;

			for (ProcessControlBlock process : ProcessTable.getReadyProcesses()) {
				cumulativeTickets += process.getPriority();
				if (cumulativeTickets > winningTicket) {
					process.copyToHardware(); // Retrieves the context to PCB
					process.setState(ProcessControlBlock.ProcessState.RUNNING); // Change state
					ProcessTable.setExecutionProcess(process); // Set the new process in execution
					ProcessTable.getReadyProcesses().remove(process); // Remove from ready queue
					break;
				}
			}
		}
	}

}
