package mars.tools;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MemoryManagerTool extends AbstractMarsToolAndApplication {

    private JLabel hitsLabel;
    private JTextField hitsTxt;
    private JComboBox<String> instPageCheckbox;
    private JLabel instPageLabel;
    private JPanel mainPanel;
    private JScrollPane jScrollPane1;
    private JComboBox<String> memSizeCheckbox;
    private JLabel memSizeLabel;
    private JLabel missesLabel;
    private JTextField missesTxt;
    private JComboBox<String> numPagesCheckbox;
    private JLabel numPagesLabel;
    private JLabel pagesReplacementsLabel;
    private JTextField pagesReplacementsTxt;
    private JLabel paginMethodLabel;
    private JComboBox<String> pagingMethodCheckbox;
    private JTable table;
    
    public MemoryManagerTool() {
        super("Memory Management Unit", "Memory Manager");
    }
    
    public void initComponents() {
        mainPanel = new JPanel();
        numPagesLabel = new JLabel();
        instPageLabel = new JLabel();
        memSizeLabel = new JLabel();
        paginMethodLabel = new JLabel();
        numPagesCheckbox = new JComboBox<>();
        instPageCheckbox = new JComboBox<>();
        memSizeCheckbox = new JComboBox<>();
        pagingMethodCheckbox = new JComboBox<>();
        jScrollPane1 = new JScrollPane();
        table = new JTable();
        hitsLabel = new JLabel();
        missesLabel = new JLabel();
        pagesReplacementsLabel = new JLabel();
        hitsTxt = new JTextField();
        missesTxt = new JTextField();
        pagesReplacementsTxt = new JTextField();

        numPagesLabel.setText("# Pages");

        instPageLabel.setText("Instructions/Page");

        memSizeLabel.setText("Memory Size");

        paginMethodLabel.setText("Paging Method");

        numPagesCheckbox.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        instPageCheckbox.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        memSizeCheckbox.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        pagingMethodCheckbox.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        table.setModel(new DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Instruction PID", "Page Table Index", "Memory Frame"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);

        hitsLabel.setText("Hits");

        missesLabel.setText("Misses");

        pagesReplacementsLabel.setText("Pages Replacements");

        hitsTxt.setEditable(false);
        hitsTxt.setText("0");
        hitsTxt.setEnabled(false);

        missesTxt.setEditable(false);
        missesTxt.setText("0");
        missesTxt.setEnabled(false);

        pagesReplacementsTxt.setEditable(false);
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
                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(memSizeCheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(instPageCheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(numPagesCheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(paginMethodLabel)
                            .addComponent(memSizeLabel)
                            .addComponent(instPageLabel)
                            .addComponent(numPagesLabel)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pagingMethodCheckbox, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE)
                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(pagesReplacementsTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(pagesReplacementsLabel)
                            .addComponent(missesTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(hitsTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addComponent(missesLabel))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(hitsLabel)))
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
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
                .addComponent(pagesReplacementsTxt, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(numPagesLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numPagesCheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(instPageLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(instPageCheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(memSizeLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(memSizeCheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(paginMethodLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pagingMethodCheckbox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
    
    @Override
    public String getName() {
        return "Memory Manager";
    }

    @Override
    protected JComponent buildMainDisplayArea() {
        initComponents();
        return mainPanel;
    }
    
}
