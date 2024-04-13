package mars.mips.SO.ProcessManager;

import mars.mips.hardware.RegisterFile; 
import mars.simulator.Simulator;
import mars.util.SystemIO;
import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class MemoryManager {
    public static final int tamPagVirtual = 4;
    public static final int qntMaxBlocos = 16;

    public enum SchedulerEPag {
        FIFO,
        NRU,
        SECOND_CHANCE,
        CLOCK,
        LRU,
        WORKING_SET,
        OPTIMAL,
        NOT_USED_RECENTLY
    }

    public static SchedulerEPag schedulerEPag = SchedulerEPag.FIFO;

    public static void setGetSchedulingAlgorithm(SchedulerEPag SchedulingAlgorithm) {
        MemoryManager.schedulerEPag = SchedulingAlgorithm;
    }

    public static void verifyMemory() {
        ProcessControlBlock procExec = ProcessTable.getExecutionProcess();
        if (procExec == null) {
            return;
        }

        int pc = RegisterFile.getProgramCounter();
        if (procExec.getInferiorLimit() < pc && procExec.getSuperiorLimit() > pc) {
            SystemIO.printString(
                    "Os limites de endereço do processo em execução, que possui limite superior: " +
                            procExec.getSuperiorLimit() + " e limite inferior: " +
                            procExec.getInferiorLimit() + " estão fora da área de acesso.\n");
            SystemIO.printString("Endereço da tentativa de acesso: " + pc);

            Simulator.getInstance().stopExecution(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                }
            });
        }
    }

    // -----------------------------------------
    // Algoritmos de substituição de página que precisam ser implementados
    // - O algoritmo ótimo (melhor de todos)
    // – Página que não foi usada recentemente
    // - NRU
    // – Segunda chance
    // – Relógio
    // – Usadas recentemente (LRU –last recently used)
    // – Grupo de trabalho

    // Implementar algoritmo FIFO de troca de página virtual
    public static void FIFO() {
        // Obtém o processo em execução
        ProcessControlBlock procExec = ProcessTable.getExecutionProcess();
        if (procExec == null) {
            return; // Se não houver processo em execução, saia do método
        }

        // Obtém a lista de blocos associada ao processo em execução
        VirtualTable.Block[] blocos = VirtualTable.tabelaPaginas.get(procExec);
        int freeBlock2 = 0;

        // Verifica se há um bloco livre para carregar novas instruções
        boolean freeblock1 = false;


        for (int i = 0; i < blocos.length; i++) {
            if (blocos[i] == null) {
                freeblock1 = true;
                freeBlock2 = i;
                break;
            }
        }

        // Se não houver bloco livre, remova o bloco mais antigo (primeiro na fila)
        if (!freeblock1) {
            // Remove o primeiro bloco na fila (FIFO)
            VirtualTable.Block blocoRemovido = blocos[0];

            // Desloca todos os blocos restantes uma posição para a esquerda
            for (int i = 0; i < blocos.length - 1; i++) {
                blocos[i] = blocos[i + 1];
            }

            // Adiciona o novo bloco no final da lista (última posição)
            // blocos[blocos.length - 1] = new Block();

            // carregar as novas instruções para o bloco recém-adicionado
        } else {
            // Se houver um bloco livre, adicione as novas instruções a ele
            // blocos[freeBlock2] = new Block();
            // for (int i = 0; i < blocos.length; i++) {
            //     if (blocos[i] == null) {
            //         blocos[i] = new Block();
            //         break;
            //     }
            // }
        }
    }
}