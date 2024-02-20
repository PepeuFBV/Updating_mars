package mars.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Observable;

import mars.mips.SO.ProcessManager.ProcessControlBlock;
import mars.mips.SO.ProcessManager.ProcessTable;
import mars.mips.SO.ProcessManager.Scheduler;
import mars.mips.hardware.AccessNotice;
import mars.mips.hardware.Memory;
import mars.mips.hardware.MemoryAccessNotice;

public class TimerTool extends AbstractMarsToolAndApplication {

	private static final long serialVersionUID = 1L;
	private static final String NAME = "Timer Tool";
	private static final String VERSION = "1.0";
	private static final String HEADING = "Timer Tool";

	private int instructionCount = 0;
	private int interruptInterval = 1; // Default value of interrupt interval
	private JComboBox<String> schedulingAlgorithmComboBox;

	private JTextField intervalField;
	private JButton startButton;
	private JButton helpButton;

	public TimerTool() {
		super(NAME + ", " + VERSION, HEADING);
	}

	@Override
	public String getName() {
		return NAME;
	}

	private JTextField counterField;

	@Override
	protected JComponent buildMainDisplayArea() {
		JPanel panel = new JPanel(new GridBagLayout());

		counterField = new JTextField(10);
		counterField.setEditable(false);

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_END;
		c.gridheight = c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		panel.add(counterField, c);

		intervalField = new JTextField(Integer.toString(interruptInterval), 10);
		startButton = new JButton("Start Timer");
		helpButton = new JButton("Help");

		schedulingAlgorithmComboBox = new JComboBox<>();
		schedulingAlgorithmComboBox.addItem("FIFO");
		schedulingAlgorithmComboBox.addItem("Priority");
		schedulingAlgorithmComboBox.addItem("Lottery");

		c.gridx = 2;
		panel.add(intervalField, c);

		c.gridx = 3;
		panel.add(startButton, c);

		c.gridx = 4;
		panel.add(helpButton, c);

		c.gridx = 5;
		panel.add(schedulingAlgorithmComboBox, c);

		startButton.addActionListener((ActionEvent e) -> {
            try {
                interruptInterval = Integer.parseInt(intervalField.getText());
            } catch (NumberFormatException ex) {
                interruptInterval = 1;
                intervalField.setText(Integer.toString(interruptInterval));
            }
            System.out.println("interrupt interval : " + interruptInterval);
            instructionCount = 0;
        });

		helpButton.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(null, "This is the Timer Tool of MARS.\n"
                    + "You can configure the interrupt interval and start the timer with the 'Start Timer' button.\n"
                    + "When the number of executed instructions reaches the specified interval, an interrupt occurs.\n"
                    + "For more information, consult the MARS documentation.");
        });

		return panel;
	}

	@Override
	protected void addAsObserver() {
		addAsObserver(Memory.textBaseAddress, Memory.textLimitAddress);
	}

	@Override
	protected void processMIPSUpdate(Observable resource, AccessNotice notice) {
		if (!notice.accessIsFromMIPS())
			return;
		if (notice.getAccessType() != AccessNotice.READ)
			return;

		MemoryAccessNotice memoryNotice = (MemoryAccessNotice) notice;
		int address = memoryNotice.getAddress();

		if (address >= Memory.textBaseAddress && address <= Memory.textLimitAddress) {
			instructionCount++;

			if (instructionCount == interruptInterval) {
				handleTimerInterrupt();
				instructionCount = 0;
			}
		}
	}

	@Override
	protected void updateDisplay() {
		counterField.setText(Integer.toString(instructionCount));
	}

	private void handleTimerInterrupt() {

		ProcessControlBlock process = ProcessTable.getExecutionProcess();

		process.copyFromHardware();

        ProcessTable.changeState(ProcessControlBlock.ProcessState.READY);
        ProcessTable.getReadyProcesses().addLast(process);

		// Here, call the appropriate scheduling algorithm based on the selected option
		switch (schedulingAlgorithmComboBox.getSelectedItem().toString()) {
		case "FIFO":
            process.showRegisters();
			Scheduler.fifo();
			process.showRegisters();
            break;
		case "Priority":			
			Scheduler.priority();
			break;
		case "Lottery":
			Scheduler.lottery();
			break;
		default:
			Scheduler.fifo();
			break;
		}

		ProcessTable.listProcesses();
	}

	@Override
	protected void initializePreGUI() {
		instructionCount = 0;
		interruptInterval = 1;
	}

	@Override
	protected void reset() {
		instructionCount = 0;
		intervalField.setText(Integer.toString(interruptInterval));
	}
}
