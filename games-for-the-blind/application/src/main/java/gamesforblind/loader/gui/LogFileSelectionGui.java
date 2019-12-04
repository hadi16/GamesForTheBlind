package gamesforblind.loader.gui;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Optional;

/**
 * Opens a GUI window that allows a user to select a XML log file to load.
 */
public class LogFileSelectionGui extends JFrame {
    /**
     * The path to the log file that the user selected, as a String.
     */
    @Nullable
    private final String selectedLogFilePath;

    /**
     * Creates a new LogFileSelectionGui.
     */
    public LogFileSelectionGui() {
        this.selectedLogFilePath = this.openLogFileGuiAndReturnPath().orElse(null);
    }

    /**
     * Opens the log file selection GUI & returns the path to the XML file that was selected by the user.
     *
     * @return The path to the selected log file as a String (wrapped in an {@link Optional}).
     */
    private Optional<String> openLogFileGuiAndReturnPath() {
        JFrame openDialog = new JFrame();
        openDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Log files are likely in the same directory as the overall project.
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));

        // Only want to allow the user to select XML files.
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files: *.xml", "xml"));

        fileChooser.setDialogTitle("Please choose a log file to open.");

        if (fileChooser.showOpenDialog(openDialog) == JFileChooser.APPROVE_OPTION) {
            String logFilePath = fileChooser.getSelectedFile().getPath();
            System.out.println("File successfully opened: " + logFilePath);
            return Optional.of(logFilePath);
        }

        System.out.println("The open operation was cancelled.");
        return Optional.empty();
    }

    /**
     * Getter for selectedLogFilePath
     *
     * @return The path to the selected XML log file. Or, Optional.empty() if operation was cancelled.
     */
    public Optional<String> getSelectedLogFilePath() {
        return Optional.ofNullable(this.selectedLogFilePath);
    }
}
