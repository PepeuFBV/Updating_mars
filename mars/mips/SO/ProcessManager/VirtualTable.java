package mars.mips.SO.ProcessManager;

public class VirtualTable {

    private final VirtualTableEntry[] virtualTable;
    private int size;

    public VirtualTable(int size) {
        virtualTable = new VirtualTableEntry[size];
        this.size = 0;
    }

    public VirtualTableEntry getPage(int numPage) {
        return virtualTable[numPage];
    }

    public VirtualTableEntry[] getPages() {
        return virtualTable;
    }

    public void createPage(int numPage) {
        virtualTable[numPage] = new VirtualTableEntry();
        if (size < MemoryManager.maxNumPages){
            size++;
        }
    }

    public void setPage(VirtualTableEntry page) {
        for (int i = 0; i < size; i++) {
            if (virtualTable[i] == null) {
                virtualTable[i] = page;
                break;
            }
        }
    }

    public void setPage(VirtualTableEntry page, int index) {
        for (int i = 0; i < size; i++) {
            if (virtualTable[i] == null) {
                virtualTable[i] = page;
                break;
            }
        }
    }

    public int getSize() {
        return size;
    }

    // método para remover uma página da tabela de páginas virtuais que recebe como parâmetro a página a ser removida
    public void removePage(VirtualTableEntry page) {
        for (int i = 0; i < size; i++) {
            if (virtualTable[i] == page) {
                virtualTable[i] = null;
                size--;
                break;
            }
        }
    }

}
