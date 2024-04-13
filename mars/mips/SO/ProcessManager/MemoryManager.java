package mars.mips.SO.ProcessManager;

import mars.mips.hardware.RegisterFile;
import mars.simulator.Simulator;
import mars.util.SystemIO;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class MemoryManager {
    private static int tamPagVirtual = 4;
    private static int qntMaxBlocos = 16;

    public static int getTamPagVirtual() {
		return tamPagVirtual;
	}

	public static void setTamPagVirtual(int tamPagVirtual) {
		MemoryManager.tamPagVirtual = tamPagVirtual;
	}

	public static int getQntMaxBlocos() {
		return qntMaxBlocos;
	}

	public static void setQntMaxBlocos(int qntMaxBlocos) {
		MemoryManager.qntMaxBlocos = qntMaxBlocos;
	}

	public static String getGetSchedulingAlgorithm() {
		return getSchedulingAlgorithm;
	}

	public static void setGetSchedulingAlgorithm(String getSchedulingAlgorithm) {
		MemoryManager.getSchedulingAlgorithm = getSchedulingAlgorithm;
	}

	//-----------------------------------------
    private static String getSchedulingAlgorithm = "FIFO"; // Case default
    // Algoritmos de substituição de página que precisam ser implementados
    // - O algoritmo ótimo (melhor de todos) 
    // – Página que não foi usada recentemente 
    // - NRU 
    // – Segunda chance 
    // – Relógio 
    // – Usadas recentemente (LRU –last recently used) 
    // – Grupo de trabalho 

    public static void verifyMemory() {
        ProcessControlBlock procExec = ProcessTable.getExecutionProcess();
        if (procExec == null) return;

        int pc = RegisterFile.getProgramCounter();
        if (procExec.getSuperiorLimit() > pc || procExec.getInferiorLimit() < pc) {
            SystemIO.printString(
                    "Os limites de endereço do processo em execução, que possui limite superior: " +
                            procExec.getSuperiorLimit() + " e limite inferior: " +
                            procExec.getInferiorLimit() + " estão fora da área de acesso.\n"
            );
            SystemIO.printString("Endereço da tentativa de acesso: " + pc);

            Simulator.getInstance().stopExecution(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                }
            });
        } else {
        }
    }

    public static String getSchedulingAlgorithm() {
        return getSchedulingAlgorithm;
    }

    public static int getMaxBlocos() {
        return qntMaxBlocos;
    }
}