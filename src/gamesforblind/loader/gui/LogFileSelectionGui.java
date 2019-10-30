package gamesforblind.loader.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Optional;

public class LogFileSelectionGui extends JFrame {
    private final String selectedLogFilePath;

    public LogFileSelectionGui() {
        this.selectedLogFilePath = this.openLogFileGuiAndReturnPath();
    }

    private String openLogFileGuiAndReturnPath() {
        JFrame openDialog = new JFrame();
        openDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML Files: *.xml", "xml"));
        fileChooser.setDialogTitle("Please choose a log file to open.");

        if (fileChooser.showOpenDialog(openDialog) == JFileChooser.APPROVE_OPTION) {
            String logFilePath = fileChooser.getSelectedFile().getPath();
            System.out.println("File successfully opened: " + logFilePath);
            return logFilePath;
        }

        System.out.println("The open operation was cancelled.");
        return null;
    }

    public Optional<String> getSelectedLogFilePath() {
        if (this.selectedLogFilePath == null) {
            return Optional.empty();
        }

        return Optional.of(this.selectedLogFilePath);
    }
}
