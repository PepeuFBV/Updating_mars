package mars.mips.SO.ProcessManager;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import mars.mips.SO.ProcessManager.MemoryManager.SchedulerEPag;
import mars.mips.hardware.AddressErrorException;

/**
 * Memory Management Unit
 */
public abstract class MMU {
    /**
     * Represents a block of instructions in the virtual memory. Also known as a
     * virtual table entry.
     */
    public static final Map<ProcessControlBlock, VirtualTable> virtualTable = new LinkedHashMap<>(
            Collections.singletonMap(ProcessTable.getExecutionProcess(), new VirtualTable(MemoryManager.maxNumPages)));
    public static Map<ProcessControlBlock, Queue<VirtualTableEntry>> lastPage = new LinkedHashMap<>();
    public static int hits;
    public static int misses;
    public static int pageReplacements;

    public static void deletProcess(ProcessControlBlock process) {
        MMU.virtualTable.remove(process);
    }

    public static void verifyInstruction(int address) throws AddressErrorException {
        // Verify if the instruction is in the page table
        // If not, call the page fault handler
        ProcessControlBlock process = ProcessTable.getExecutionProcess();
        VirtualTable virtualTable1 = MMU.virtualTable.get(process);

        int index = ((address / (MemoryManager.pageSize * 4)) % MemoryManager.maxNumPages);
        int displacement = (address / 4) % MemoryManager.pageSize;

        System.err.println(
                "============================================================\n1 - Instrução procurada: " + address
                        + " está mapeada para:\nPágina: "
                        + index + " Deslocamento: " + displacement);

        if (virtualTable1.getPage(index) == null) {
            // Se entrou aqui, é porque a página é nula, ou seja, não existe.

            System.out.println("2 - Entrou no primeiro if, então a página é nula, logo, deve ser criada.");
            process.setMisses(process.getMisses() + 1);
            MMU.misses++;
            addPageNull(address, index, displacement, virtualTable1, process);
            virtualTable1.getPage(index).setReferencedPage(true);
        } else {
            // Se entrou aqui, é porque a página não é nula, ou seja, existe.

            System.out.println("2 - Entrou no else, então a página não é nula.");
            if (virtualTable1.getPage(index).getInstructions()[displacement] == address) {
                // Se entrou aqui, é porque a instrução foi encontrada na página.

                System.out.println("3 - Instrução encontrada já carregada na página: Hit! "
                        + virtualTable1.getPage(index).getInstructions()[displacement] +
                        " instrução procurada: " + address + " do processo " + process.getPid() + ".");

                process.setHits(process.getHits() + 1);
                MMU.hits++;
                virtualTable1.getPage(index).setReferencedPage(true);

            } else {
                // Se entrou aqui, é porque a instrução não foi encontrada na página. Ou seja, a
                // página existe, mas a instrução não.

                System.out.println(
                        "3 - Página existe, mas a instrução não foi encontrada na posição mapeada. Vamos carregar a instrução.");
                process.setMisses(process.getMisses() + 1);
                MMU.misses++;
                addInstruction(address, index, displacement, virtualTable1, process);
                virtualTable1.getPage(index).setReferencedPage(true);

            }
        }
    }

    public static void addInstruction(int addressInstructions, int numPage, int displacement, VirtualTable virtualTable,
            ProcessControlBlock process) {

        if (virtualTable.getPage(numPage).getInstructions()[displacement] == 0) {
            // Se entrou aqui é porque a página existe, mas não tem nenhum instrução na
            // posição displacement.

            virtualTable.getPage(numPage).addInstruction(addressInstructions, displacement); // instrução adicionada
            MMU.virtualTable.put(process, virtualTable); // página adicionada na tabela de páginas virtuais

            System.out.println(
                    "4 - Entrou no primeiro if, então a página existe, mas não tem instrução na posição. Vamos carregar a instrução para a página.");
            System.out.println("5 - Instrução: " + addressInstructions + " do processo " + process.getPid()
                    + " adicionada na página " + numPage
                    + " no deslocamento " + displacement);
            System.out.println(
                    "Operação de adição de instrução concluída com sucesso.\n============================================================");

        } else if (virtualTable.getPage(numPage).getInstructions()[displacement] != addressInstructions) {
            // Se entrou aqui é porque a página existe, mas a instrução na posição
            // displacement é diferente da instrução que está sendo procurada.
            // Ou seja, a página existe, mas a instrução não está nela e como essa instrução
            // é guardada nessa posição.
            // Então, é necessário substituir a página.

            System.out.println(
                    "4 - Entrou no segundo if, então a página existe, mas a instrução encontrada na posição é diferente da instrução procurada. Vamos substituir a página.");

            // A substituição nesse caso é feita porque: Como a página existe, mas a
            // instrução encontrada na posição é diferente da instrução procurada
            // então uma outra página que também foi mapeada para essa página/posição, foi
            // carregada, essa instrução foi mapeada para esse canto e não pode ser mapeada
            // para outra página
            // então é necessário substituir a página para que a instrução requisitada seja
            // mapeada para essa posição e o processo possa continuar a execução.

            switch (MemoryManager.getSchedulerEPag()) {
                case NRU:
                    // chama lá
                case FIFO:
                    MemoryManager.FIFO();
                case SECOND_CHANCE:
                    // chama lá
                case LRU:
                    // chama lá
            }

            process.setPageFaults(process.getPageFaults() + 1);
            MMU.pageReplacements++;

            virtualTable.createPage(numPage);
            virtualTable.getPage(numPage).addInstruction(addressInstructions, displacement);
            MMU.virtualTable.put(process, virtualTable);

            Queue<VirtualTableEntry> processLastPage = lastPage.get(process);
            if (processLastPage == null) {
                processLastPage = new LinkedList<>();
                lastPage.put(process, processLastPage);
            }
            processLastPage.add(virtualTable.getPage(numPage));

            System.out.println("6 - Página " + numPage + " adicionada");
            System.out.println("7 - Número de páginas do processo "
                    + process.getPid() + " pós-criação da nova página: " + virtualTable.getSize());
            System.out.println(
                    "Operação de substituição de página concluída com sucesso.\n============================================================");
        }
    }

    public static void addPageNull(int addressInstructions, int numPage, int displacement, VirtualTable virtualTable,
            ProcessControlBlock process) {
        // Se entrou aqui, é porque a página é nula, então cria uma página nova e
        // adiciona a instrução nela.
        System.out.println("3 - Número de páginas do processo "
                + process.getPid() + " antes da criação da nova página: " + virtualTable.getSize());
        virtualTable.createPage(numPage); // página criada
        virtualTable.getPage(numPage).addInstruction(addressInstructions, displacement); // instrução adicionada
        MMU.virtualTable.put(process, virtualTable); // página adicionada na tabela de páginas virtuais

        Queue<VirtualTableEntry> processLastPage = lastPage.get(process); // adiciona a página na fila de páginas
        if (processLastPage == null) { // se a fila de páginas for nula, cria uma nova
            processLastPage = new LinkedList<>(); // cria a fila
            lastPage.put(process, processLastPage); // adiciona a fila na lista de páginas
        }
        processLastPage.add(virtualTable.getPage(numPage)); // adiciona a página na fila de páginas

        System.out.println("4 - Página " + numPage + " adicionada");
        System.out.println(
                "5 - Número de páginas do processo " + process.getPid() + " pós-criação: " + virtualTable.getSize());
        System.out.println(
                "Operação de criação de página concluída com sucesso.\n============================================================");
    }
}