package mars.mips.SO.ProcessManager;

import java.util.ArrayList; // TODO ArrayList é o melhor para armazenar?
//import java.util.Deque;


public class ProcessTable { // TODO checar se há a necessidade dos métodos: changeState, adicionarProcesso e removerProcesso
    
    // Possible processes
    private ArrayList<ProcessControlBlock> readyProcesses; // TODO ArrayList é o melhor para armazenar?
    private ProcessControlBlock executionProcess;
    
    public ArrayList<ProcessControlBlock> getReadyProcesses() {
        return readyProcesses;
    }
    
    public void setReadyProcesses(ArrayList<ProcessControlBlock> readyProcesses) {
        this.readyProcesses = readyProcesses;
    }
    
    public ProcessControlBlock getExecutionProcess() {
        return executionProcess;
    }
    
    public void setExecutionProcess(ProcessControlBlock executionProcess) {
        this.executionProcess = executionProcess;
    }
    
    /**
      * Class initializer.
      **/
    public ProcessTable() {
        readyProcesses = new ArrayList<ProcessControlBlock>();
        executionProcess = null;
    }
    
    /**
      * Method for creating a new process and assessing where it should be depending on its state.
      *  @param pid Integer value that serves as process identifier
      *  @param programAddress Address of the start of the program
      *  @param state The current state of the process
      */
    public void createProcess(int pid, String programAddress, ProcessControlBlock.ProcessState state) {
        ProcessControlBlock newProcess = new ProcessControlBlock(pid, programAddress, state);
        
        if (state == ProcessControlBlock.ProcessState.READY) {
            readyProcesses.add(newProcess);
        } else if (state == ProcessControlBlock.ProcessState.RUNNING) { // TODO checar se já há processo em execução
            executionProcess = newProcess;
        }
    }
    
    /**
      * Method for removing a process through its PID.
      *  @param pid Integer value that serves as process identifier
      */
    public void removeProcess(int pid) throws Exception {
        boolean found = false;
        
        for (int i = 0; i < readyProcesses.size(); i++) {
            ProcessControlBlock process = readyProcesses.get(i);
            
            if (process.getPid() == pid) {
                readyProcesses.remove(i);
                found = true;
                break;
            }
        }
        
        if (executionProcess != null) {
            if (executionProcess.getPid() == pid) {
                executionProcess = null;
                found = true;
            }
        }
        
        if (!found) {
            throw new Exception("Process with PID " + pid + " doesn't exist in the process table.");
        }
    }
    
    /**
      * Method for changing a processe's state in the table.
      *  @param pid Integer value that serves as process identifier
      *  @param newState New process state
      */
    public void changeState(int pid, ProcessControlBlock.ProcessState newState) throws Exception {
        boolean found = false;
        
        for (int i = 0; i < readyProcesses.size(); i++) {
            ProcessControlBlock process = readyProcesses.get(i);
            
            if (process.getPid() == pid) {
                process.setState(newState);
                if (newState == ProcessControlBlock.ProcessState.RUNNING) {
                    readyProcesses.remove(i);
                    executionProcess = process;
                }
                found = true;
                break;
            }
        }
        
        if (executionProcess != null) {
            if (executionProcess.getPid() == pid) {
                executionProcess.setState(newState);
                if (newState == ProcessControlBlock.ProcessState.READY) {
                    readyProcesses.add(executionProcess);
                    executionProcess = null;
                }
                found = true;
            }
        }
        
        if (!found) {
            throw new Exception("Process with PID " + pid + " doesn't exist in the process table.");
        }
    }
    
    /**
      * Method for displaying all processes for debugging.
      */
    public void listProcesses() {
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
