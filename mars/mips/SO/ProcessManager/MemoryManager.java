package mars.mips.SO.ProcessManager;

import mars.mips.hardware.RegisterFile;

import java.util.Queue;

import mars.mips.hardware.AddressErrorException;
import mars.simulator.Simulator;

public abstract class MemoryManager {

    public static int PROGRAM_END_ADDRESS; // It is defined in assembly time
    public static int pageSize = 4;
    public static int maxNumPages = 16;

    public static SchedulerEPag schedulerEPag;

    public enum SchedulerEPag {
        FIFO,
        NRU,
        SECOND_CHANCE,
        LRU,
    }

    public static void setPageSize(int pageSize) {
        MemoryManager.pageSize = pageSize;
    }

    public static void setMaxNumPages(int maxNumPages) {
        MemoryManager.maxNumPages = maxNumPages;
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
    // - NRU
    // – Segunda chance
    // – Usadas recentemente (LRU –last recently used)
    // Implementar algoritmo FIFO de troca de página virtual

    public static void FIFO() {
        // Obtém o processo em execução
        ProcessControlBlock procExec = ProcessTable.getExecutionProcess();
        if (procExec == null) {
            return; // Se não houver processo em execução, saia do método
        }

        // Obtém a lista de blocos associada ao processo em execução
        VirtualTable entries = MMU.virtualTable.get(procExec);
        Queue<VirtualTableEntry> lastEntries = MMU.lastPage.get(procExec);

        // Remove o primeiro bloco na fila (FIFO)
        VirtualTableEntry removedEntry = lastEntries.poll();

        // Desloca todos os blocos restantes uma posição para a esquerda
        for (int i = 0; i < entries.getSize() - 1; i++) {
            //entries.setPage(i, entries.getPage(i + 1));
            //entries[i] = entries[i + 1];
        }
    }
}
