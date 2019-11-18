package gamesforblind.logger;

import gamesforblind.ProgramAction;
import gamesforblind.sudoku.OriginalSudokuGrid;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static gamesforblind.Constants.LOG_FILES_DIRECTORY;

/**
 * Class that allows stored Java objects to be written to a XML log file.
 */
public class LogWriter {
    /**
     * All of the stored ProgramActions & the original state of the Sudoku board in the game.
     */
    private final LogFactory logFactory;

    /**
     * Creates a new LogWriter
     *
     * @param logFactory All of the stored ProgramActions & the original state of the Sudoku board in the game.
     */
    public LogWriter(LogFactory logFactory) {
        this.logFactory = logFactory;
    }

    /**
     * Uses JAXB to marshall a JAVA object to a XML string.
     *
     * @param objectToMarshall The Java object to marshall.
     * @return The XML body representing the Java object (NO XML prolog included). If an error occurred, just empty().
     */
    private Optional<String> convertObjectToXmlString(Object objectToMarshall) {
        try {
            Marshaller jaxbMarshaller = JAXBContext.newInstance(objectToMarshall.getClass()).createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(objectToMarshall, stringWriter);

            // Remove the XML prolog & return it wrapped in an Optional.
            return Optional.of(
                    stringWriter.toString().replaceAll("<\\?xml(.+?)\\?>", "").trim()
            );
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        // If an error occurred, just return empty().
        return Optional.empty();
    }

    /**
     * Converts an entire game into a XML string that can be saved.
     *
     * @return The XML String representing the stored game to be saved as a log file.
     */
    private String marshalLogToXmlString() {
        // Add a standard XML prolog to the beginning, along with a <log> opening tag.
        StringBuilder xmlStringBuilder = new StringBuilder(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><log>"
        );

        // Serialize the logged Sudoku grids.
        ArrayList<OriginalSudokuGrid> originalSudokuGrids = this.logFactory.getOriginalSudokuGridList();
        for (OriginalSudokuGrid originalGrid : originalSudokuGrids) {
            this.convertObjectToXmlString(originalGrid).ifPresent(xmlStringBuilder::append);
        }

        // Serialize all of the ProgramActions in the game.
        ArrayList<ProgramAction> programActions = this.logFactory.getProgramActionList();
        for (ProgramAction action : programActions) {
            this.convertObjectToXmlString(action).ifPresent(xmlStringBuilder::append);
        }

        // Add a closing <log> tag.
        xmlStringBuilder.append("</log>");

        // I don't want any whitespace between tags (e.g. ">  <") or newlines.
        return xmlStringBuilder.toString()
                .replaceAll(">\\s*<", "><")
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
    }

    /**
     * Writes a XML String to a XML file with the name being the current time.
     *
     * @param beautifiedLogXmlString The XML log file String to save.
     * @return true if the save operation was successful (otherwise, false).
     */
    private boolean writeXmlLogFile(String beautifiedLogXmlString) {
        // The name of the XML log file is the current date & time.
        File logFile = new File(String.format(
                "%s/%s.xml", LOG_FILES_DIRECTORY.toString(), LocalDateTime.now().toString()
        ));

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile);
            fileWriter.write(beautifiedLogXmlString);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Saves the current state of the instance variable logFactory to a XML log file.
     */
    public void saveGameLog() {
        // Step 1: get the basic XML log Document to save.
        String logXmlString = this.marshalLogToXmlString();
        Optional<Document> logXmlDocument = XmlHelpers.convertXmlStringToDocument(logXmlString);

        if (logXmlDocument.isEmpty()) {
            return;
        }

        // Step 2: get the beautified XML log file String.
        Optional<String> beautifiedLogXmlString = XmlHelpers.beautifyXmlDocument(logXmlDocument.get());
        if (beautifiedLogXmlString.isEmpty()) {
            return;
        }

        // Step 3: save the beautified XML log file String. If successful, print message to stdout.
        if (this.writeXmlLogFile(beautifiedLogXmlString.get())) {
            int numberOfLoggedSudokuBoards = this.logFactory.getOriginalSudokuGridList().size();
            if (numberOfLoggedSudokuBoards != 0) {
                System.out.println("Saved " + numberOfLoggedSudokuBoards + " Sudoku board(s) to the logs directory!");
            }

            int numberOfLoggedActions = this.logFactory.getProgramActionList().size();
            if (numberOfLoggedActions != 0) {
                System.out.println("Saved " + numberOfLoggedActions + " action(s) to the logs directory!");
            }
        }
    }
}
