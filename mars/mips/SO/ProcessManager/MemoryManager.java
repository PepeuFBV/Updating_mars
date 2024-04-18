package mars.mips.SO.ProcessManager;

import mars.mips.hardware.RegisterFile;
import mars.mips.hardware.AddressErrorException;
import mars.simulator.Simulator;

public abstract class MemoryManager {

    public static final int VIRTUAL_PAGE_SIZE = 4;
    public static final int MAX_NUM_BLOCKS = 16;
    public static int PROGRAM_END_ADDRESS; // It is defined in assembly time

    public static SchedulerEPag schedulerEPag = SchedulerEPag.FIFO;

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

    public static void setSchedulingAlgorithm(SchedulerEPag SchedulingAlgorithm) {
        MemoryManager.schedulerEPag = SchedulingAlgorithm;
    }

    public static void verifyBounds(ProcessControlBlock process) {
        // Temporarily adds the execution process to the queue of ready processes
        var readyProcs = ProcessTable.getReadyProcesses();
        readyProcs.addFirst(ProcessTable.getExecutionProcess());

        // Defines the memory limits of each process created so far
        boolean conflict;
        do {
            conflict = false;
            for (var p : readyProcs) {
                int upper = process.getUpperLimit();
                int lower = process.getLowerLimit();
                int upperP = p.getUpperLimit();
                int lowerP = p.getLowerLimit();

                if (upper >= upperP && upper <= lowerP) {
                    p.setLowerLimit(upper - 4);
//                    System.out.println("Conflict Sup detected " + p.getPid() + " LowerLimit: " + p.getLowerLimit()
//                            + " UpperLimit: " + p.getUpperLimit() + " Criado lower: " + lower + " criado upper: "
//                            + upper);
                    conflict = true;
                } else if (lower >= upperP && lower <= lowerP) {
                    process.setLowerLimit(upperP - 4);
//                    System.out.println("Conflict Inf detected " + p.getPid() + " LowerLimit: " + p.getLowerLimit()
//                            + " UpperLimit: " + p.getUpperLimit() + " Criado lower: " + lower + " criado upper: "
//                            + upper);
                    conflict = true;
                } else if (upperP >= upper && lowerP <= lower) {
                    process.setLowerLimit(upperP - 4);
                }
            }

        } while (conflict);

        readyProcs.removeFirst();
    }

    public static void verifyMemory() throws AddressErrorException {
        ProcessControlBlock procExec = ProcessTable.getExecutionProcess();
        if (procExec == null) {
            return;
        }

        int pc = RegisterFile.getProgramCounter();
        if (pc < procExec.getUpperLimit() || pc > procExec.getLowerLimit()) {
            String errorMessage = "\nThe address limits of the running process, "
                    + "which has an upper limit: " + procExec.getUpperLimit() + " \nand lower limit: "
                    + procExec.getLowerLimit() + " are outside the access area."
                    + "\nAccess attempt address: " + pc + " ";
            throw new AddressErrorException(errorMessage, Simulator.EXCEPTION, pc);
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
        VirtualTable.Block[] blocos = VirtualTable.pageTable.get(procExec);
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
            // if (blocos[i] == null) {
            // blocos[i] = new Block();
            // break;
            // }
            // }
        }
    }
}
