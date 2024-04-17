package mars.tools;

import javax.swing.*;
import java.io.Serial;

public class MMUTool extends AbstractMarsToolAndApplication {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String NAME = "MMU Tool";
    private static final String VERSION = "1.0";
    private static final String HEADING = "Memory Management Unit Tool";

    public MMUTool() {
        super(NAME + ", " + VERSION, HEADING);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected JComponent buildMainDisplayArea() {
        return new JLabel("Memory Management Unit Tool");
    }

    @Override
    protected void reset() {
        // Nothing to reset
    }

}
