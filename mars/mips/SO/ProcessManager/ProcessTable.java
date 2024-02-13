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
    
    public static int getPID() {
        return PID++;
    }
    
    /**
     * Method for changing a process's state in the table.
     *
     * @param newState New process state
     * @throws java.lang.Exception
     */
    public static void changeState(ProcessControlBlock.ProcessState newState) throws Exception {
        executionProcess.setState(newState);
    }
    
    /**
     * Method for displaying all processes for debugging.
     */
    public static void listProcesses() {
        System.out.println("PID\tAddress\tState");

        for (ProcessControlBlock processo : readyProcesses) {
            // Imprime na tela os dados do processo
            System.out.println(processo.getPid() + "\t" + processo.getProgramAddress() + "\t" + processo.getState());
        }
        // Verifica se o processo em execução não é nulo
        if (executionProcess != null) {
            // Imprime na tela os dados do processo
            System.out.println(executionProcess.getPid() + "\t" + executionProcess.getProgramAddress() + "\t" + executionProcess.getState());
        }
    }
    
}
