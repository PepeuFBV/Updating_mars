package mars.mips.SO.ProcessManager;

import mars.mips.hardware.RegisterFile; 
import mars.simulator.Simulator;
import mars.util.SystemIO;
import javax.swing.*;
import java.awt.event.ActionEvent;

public abstract class MemoryManager {
    
    public static final int VIRTUAL_PAGE_SIZE = 4;
    public static final int MAX_NUM_BLOCKS = 16;

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

    public static void setSchedulingAlgorithm(SchedulerEPag SchedulingAlgorithm) {
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
                    "Os limites de endereÃ§o do processo em execuÃ§Ã£o, que possui limite superior: " +
                            procExec.getSuperiorLimit() + " e limite inferior: " +
                            procExec.getInferiorLimit() + " estÃ£o fora da Ã¡rea de acesso.\n");
            SystemIO.printString("EndereÃ§o da tentativa de acesso: " + pc);

            Simulator.getInstance().stopExecution(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                }
            });
        }
    }

    // -----------------------------------------
    // Algoritmos de substituiÃ§Ã£o de pÃ¡gina que precisam ser implementados
    // - O algoritmo Ã³timo (melhor de todos)
    // â PÃ¡gina que nÃ£o foi usada recentemente
    // - NRU
    // â Segunda chance
    // â RelÃ³gio
    // â Usadas recentemente (LRU âlast recently used)
    // â Grupo de trabalho

    // Implementar algoritmo FIFO de troca de pÃ¡gina virtual
    public static void FIFO() {
        // ObtÃ©m o processo em execuÃ§Ã£o
        ProcessControlBlock procExec = ProcessTable.getExecutionProcess();
        if (procExec == null) {
            return; // Se nÃ£o houver processo em execuÃ§Ã£o, saia do mÃ©todo
        }

        // ObtÃ©m a lista de blocos associada ao processo em execuÃ§Ã£o
        VirtualTable.Block[] blocos = VirtualTable.tabelaPaginas.get(procExec);
        int freeBlock2 = 0;

        // Verifica se hÃ¡ um bloco livre para carregar novas instruÃ§Ãµes
        boolean freeblock1 = false;

        for (int i = 0; i < blocos.length; i++) {
            if (blocos[i] == null) {
                freeblock1 = true;
                freeBlock2 = i;
                break;
            }
        }

        // Se nÃ£o houver bloco livre, remova o bloco mais antigo (primeiro na fila)
        if (!freeblock1) {
            // Remove o primeiro bloco na fila (FIFO)
            VirtualTable.Block blocoRemovido = blocos[0];

            // Desloca todos os blocos restantes uma posiÃ§Ã£o para a esquerda
            for (int i = 0; i < blocos.length - 1; i++) {
                blocos[i] = blocos[i + 1];
            }

            // Adiciona o novo bloco no final da lista (Ãºltima posiÃ§Ã£o)
            // blocos[blocos.length - 1] = new Block();

            // carregar as novas instruÃ§Ãµes para o bloco recÃ©m-adicionado
        } else {
            // Se houver um bloco livre, adicione as novas instruÃ§Ãµes a ele
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