package mars.mips.SO.ProcessManager;

import java.util.Deque;
import java.util.LinkedList;
import mars.mips.hardware.RegisterFile;

public abstract class ProcessTable {
    
    // Initial generated PID value for new processes
    private static int PID = 1;
    
    // Possible processes
    private static Deque<ProcessControlBlock> readyProcesses = new LinkedList<>();
    private static ProcessControlBlock executionProcess = new ProcessControlBlock(0, RegisterFile.getInitialProgramCounter(), ProcessControlBlock.ProcessState.RUNNING);
   
    // PID is unique and incremets for each new process
    public static int getPID() {
        return PID++;
    }
    
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
    
    /**
     * Changes the execution process state.
     * @param newState New process state
     */
    public static void changeState(ProcessControlBlock.ProcessState newState) {
        executionProcess.setState(newState);
    }
    
    /**
     * Display all process data for debugging, including the execution process.
     */
    public static void listProcesses() {
        System.out.println("PID\tAddress\tState");

        // Show ready processes
        for (ProcessControlBlock processo : readyProcesses) {
            System.out.println(processo.getPid() + "\t" + processo.getProgramAddress() + "\t" + processo.getState());
        }
        
        // Show execution process
        System.out.println(executionProcess.getPid() + "\t" + executionProcess.getProgramAddress() + "\t" + executionProcess.getState());
    }
    
}
