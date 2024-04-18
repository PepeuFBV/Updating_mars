package mars.mips.SO.ProcessManager;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

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

    public static void addInstruction(int addressInstructions, int numPage, int displacement) {
        ProcessControlBlock process = ProcessTable.getExecutionProcess();
        VirtualTable virtualTable = MMU.virtualTable.get(process);

        if (virtualTable.getSize() < MemoryManager.maxNumPages) {
            System.out.println("Instrução adicionada na página " + numPage + " do processo " + process.getPid() + ".");
            if (virtualTable.getPage(numPage) == null) {
                virtualTable.createPage(numPage);
                virtualTable.getPage(numPage).addInstruction(addressInstructions, displacement);
                MMU.virtualTable.put(process, virtualTable);
                lastPage.get(process).add(virtualTable.getPage(numPage));
            } else if (virtualTable.getPage(numPage) != null) {
                virtualTable.getPage(numPage).addInstruction(addressInstructions, displacement);
                MMU.virtualTable.put(process, virtualTable);
                lastPage.get(process).add(virtualTable.getPage(numPage));
            }
        } else if (virtualTable.getSize() == MemoryManager.maxNumPages){
            // Com base no algoritmo de substituição de páginas escolhido na ferramenta MemomryManagerToo.java
            // é necessário substituir uma página

            process.setPageFaults(process.getPageFaults() + 1);
            MMU.pageReplacements++;
            System.out.println("Substituindo página...");
        }
    }

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

        if (virtualTable1.getPage(index) == null) {

            process.setMisses(process.getMisses() + 1);
            MMU.misses++;
            addInstruction(address, index, displacement);

        } else {
            if (virtualTable1.getPage(index).getInstructions()[displacement] == address && virtualTable1.getPage(index).isPresent()) {
                process.setHits(process.getHits() + 1);
                MMU.hits++;
                virtualTable1.getPage(index).setReferencedPage(true);
                return;
            }

            process.setMisses(process.getMisses() + 1);
            MMU.misses++;
            addInstruction(address, index, displacement);
        }
    }
}
