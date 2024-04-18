package mars.tools;

import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.Observable;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import mars.mips.SO.ProcessManager.MMU;
import mars.mips.SO.ProcessManager.MemoryManager;
import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.VirtualTableEntry;
import mars.mips.SO.ProcessManager.VirtualTable;
import mars.mips.hardware.AccessNotice;
import mars.mips.hardware.Memory;

public class MemoryManagerTool extends AbstractMarsToolAndApplication {

    private JLabel hitsLabel;
    private static JLabel  hitsTxt;
    private JComboBox<String> instPageCheckbox;
    private JLabel instPageLabel;
    private JPanel mainPanel;
    private JScrollPane jScrollPane1;
    private JLabel missesLabel;
    private static JLabel missesTxt;
    private JComboBox<String> numPagesCheckbox;
    private JLabel numPagesLabel;
    private JLabel pagesReplacementsLabel;
    private static JLabel pagesReplacementsTxt;
    private JLabel paginMethodLabel;
    private JComboBox<String> pagingMethodCheckbox;
    private static JTable table;
    private JButton startButton;

    public MemoryManagerTool() {
        super("Memory Management Unit", "Memory Manager Tool");
    }

    public void initComponents() {
        mainPanel = new JPanel();
        numPagesLabel = new JLabel();
        instPageLabel = new JLabel();
        paginMethodLabel = new JLabel();
        numPagesCheckbox = new JComboBox<>();
        instPageCheckbox = new JComboBox<>();
        pagingMethodCheckbox = new JComboBox<>();
        jScrollPane1 = new JScrollPane();
        table = new JTable();
        hitsLabel = new JLabel();
        missesLabel = new JLabel();
        pagesReplacementsLabel = new JLabel();
        hitsTxt = new JLabel();
        missesTxt = new JLabel();
        pagesReplacementsTxt = new JLabel();
        startButton = new JButton("Start");

        numPagesLabel.setText("# Pages");

        instPageLabel.setText("Instructions/Page");

        paginMethodLabel.setText("Paging Method");

        numPagesCheckbox.setModel(new DefaultComboBoxModel<>(new String[] { "2", "4", "8", "16" }));
        numPagesCheckbox.setSelectedIndex(3); // O índice máximo é 3, não 4

        instPageCheckbox.setModel(new DefaultComboBoxModel<>(new String[] { "2", "4" }));
        instPageCheckbox.setSelectedIndex(1);

        pagingMethodCheckbox
                .setModel(new DefaultComboBoxModel<>(new String[] { "NRU", "FIFO", "Segunda chance", "LRU" }));
        pagingMethodCheckbox.setSelectedIndex(1);

        startButton.addActionListener((ActionEvent e) -> {
            start();
        });

        table.setModel(new DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Instruction PID", "Page Table Index", "Memory Frame", "Instruction Address"
                }) {
            Class[] types = new Class[] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[] {
                    false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);

        hitsLabel.setText("Hits");

        missesLabel.setText("Misses");

        pagesReplacementsLabel.setText("Pages Replacements");

        hitsTxt.setText("0");
        hitsTxt.setEnabled(false);

        missesTxt.setText("0");
        missesTxt.setEnabled(false);

        pagesReplacementsTxt.setText("0");
        pagesReplacementsTxt.setEnabled(false);

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addGroup(mainPanelLayout
                                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(instPageCheckbox, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(numPagesCheckbox, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(paginMethodLabel)
                                                        .addComponent(instPageLabel)
                                                        .addComponent(numPagesLabel)))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(pagingMethodCheckbox, GroupLayout.PREFERRED_SIZE, 126,
                                                        GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE)
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout
                                                .createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33,
                                                        Short.MAX_VALUE)
                                                .addGroup(mainPanelLayout
                                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(pagesReplacementsTxt, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(pagesReplacementsLabel)
                                                        .addComponent(startButton)
                                                        .addComponent(missesTxt, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(hitsTxt, GroupLayout.PREFERRED_SIZE,
                                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(16, 16, 16))
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGroup(mainPanelLayout
                                                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                .addGap(43, 43, 43)
                                                                .addComponent(missesLabel))
                                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                                .addGap(51, 51, 51)
                                                                .addComponent(hitsLabel)))
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))));
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(hitsLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(hitsTxt, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(missesLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(missesTxt, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pagesReplacementsLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pagesReplacementsTxt, GroupLayout.PREFERRED_SIZE, 16,
                                        GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(startButton)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 242,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(numPagesLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(numPagesCheckbox, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(instPageLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(instPageCheckbox, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGap(18, 18, 18)
                                                .addComponent(paginMethodLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pagingMethodCheckbox, GroupLayout.PREFERRED_SIZE,
                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                        .addGap(28, 28, 28));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                                        Short.MAX_VALUE)));
    }

    @Override
    public String getName() {
        return "Memory Manager Tool";
    }

    @Override
    protected JComponent buildMainDisplayArea() {
        initComponents();
        return mainPanel;
    }

    public void start() {
        MemoryManager.setPageSize(
                Integer.parseInt(instPageCheckbox.getModel().getElementAt(instPageCheckbox.getSelectedIndex())));
        MemoryManager.setMaxNumPages(
                Integer.parseInt(numPagesCheckbox.getModel().getElementAt(numPagesCheckbox.getSelectedIndex())));
        switch (pagingMethodCheckbox.getModel().getElementAt(pagingMethodCheckbox.getSelectedIndex())) {
            case "NRU":
                MemoryManager.setSchedulingAlgorithm(MemoryManager.SchedulerEPag.NRU);
                break;
            case "FIFO":
                MemoryManager.setSchedulingAlgorithm(MemoryManager.SchedulerEPag.FIFO);
                break;
            case "Segunda chance":
                MemoryManager.setSchedulingAlgorithm(MemoryManager.SchedulerEPag.SECOND_CHANCE);
                break;
            case "LRU":
                MemoryManager.setSchedulingAlgorithm(MemoryManager.SchedulerEPag.LRU);
                break;
        }

        numPagesCheckbox.setEnabled(false);
        instPageCheckbox.setEnabled(false);
        pagingMethodCheckbox.setEnabled(false);

        // System.out.println("page size = " + MemoryManager.pageSize);
        // System.out.println("max num pages = " + MemoryManager.maxNumPages);
        // System.out.println("paging method = " + MemoryManager.schedulerEPag);

        updateTable();
    }

    @Override
    protected void addAsObserver() {
        addAsObserver(Memory.textBaseAddress, Memory.textLimitAddress);
    }

    @Override
    protected void processMIPSUpdate(Observable resource, AccessNotice notice) {
        if (!notice.accessIsFromMIPS()) {
            return;
        }
        if (notice.getAccessType() != AccessNotice.READ) {
            return;
        }
    }

    @Override
    protected void updateDisplay() {
        updateTable();
    }

    public static void updateTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Limpa a tabela antes de adicionar novos dados

        // Percorre o mapa e adiciona as entradas na tabela
        for (Map.Entry<ProcessControlBlock, VirtualTable> entry : MMU.virtualTable.entrySet()) {
            ProcessControlBlock pcb = entry.getKey();
            VirtualTable virtualTable = entry.getValue();

            int pageIndex = 0;
            for (VirtualTableEntry vte : virtualTable.getPages()) {
                // Adiciona uma nova linha na tabela com os dados do ProcessControlBlock e da
                // VirtualTableEntry

                if (vte != null) {
                    for (int i = 0; i < vte.getInstructions().length; i++) {
                        model.addRow(new Object[] { pcb.getPid(), pageIndex, i, vte.getInstructions()[i] });
                    }
                } else {
                    for (int i = 0; i < MemoryManager.pageSize; i++) {
                        model.addRow(new Object[] { pcb.getPid(), pageIndex, i, "Not allocated" });
                    }
                }

                pageIndex++;
            }

            hitsTxt.setText(Integer.toString(MMU.hits));
            missesTxt.setText(Integer.toString(MMU.misses));
            pagesReplacementsTxt.setText(Integer.toString(MMU.pageReplacements));
        }

        // System.out.println("Map:");
        // for (Map.Entry<ProcessControlBlock, VirtualTable> entry :
        // MMU.virtualTable.entrySet()) {
        // ProcessControlBlock pcb = entry.getKey();
        // VirtualTable virtualTable = entry.getValue();

        // int pageIndex = 0;
        // for (VirtualTableEntry vte : virtualTable.getPages()) {
        // if (vte != null) {
        // System.out.println("PID: " + pcb.getPid() + " Page Index: " + pageIndex + "
        // Frame Number: " + vte.getFrameNumber());
        // } else {
        // System.out.println("PID: " + pcb.getPid() + " Page Index: " + pageIndex + "
        // Not allocated");
        // }
        // }
        // }
    }

    @Override
    protected void reset() {

    }

}