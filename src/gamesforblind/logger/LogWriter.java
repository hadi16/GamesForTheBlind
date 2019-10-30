package gamesforblind.logger;

import gamesforblind.Constants;
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

public class LogWriter {
    private final LogFactory logFactory;

    public LogWriter(LogFactory logFactory) {
        this.logFactory = logFactory;
    }

    private Optional<String> convertObjectToXmlString(Object objectToMarshall) {
        try {
            Marshaller jaxbMarshaller = JAXBContext.newInstance(objectToMarshall.getClass()).createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(objectToMarshall, stringWriter);

            return Optional.of(
                    stringWriter.toString().replaceAll("<\\?xml(.+?)\\?>", "").trim()
            );
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private String marshalGameLogToXmlString() {
        StringBuilder xmlStringBuilder = new StringBuilder(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><log>"
        );

        OriginalSudokuGrid originalSudokuGrid = this.logFactory.getOriginalSudokuGrid();
        this.convertObjectToXmlString(originalSudokuGrid).ifPresent(xmlStringBuilder::append);

        ArrayList<ProgramAction> programActions = this.logFactory.getProgramActionList();
        for (ProgramAction action : programActions) {
            this.convertObjectToXmlString(action).ifPresent(xmlStringBuilder::append);
        }

        xmlStringBuilder.append("</log>");
        return xmlStringBuilder.toString()
                .replaceAll(">\\s*<", "><")
                .replaceAll("\\n", "")
                .replaceAll("\\r", "");
    }

    private boolean writeXmlLogFile(String beautifiedLogXmlString) {
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

    public void saveGameLog() {
        if (!Constants.SAVE_LOGS) {
            return;
        }

        String logXmlString = this.marshalGameLogToXmlString();
        Optional<Document> logXmlDocument = XmlHelpers.convertXmlStringToDocument(logXmlString);

        if (logXmlDocument.isEmpty()) {
            return;
        }

        Optional<String> beautifiedLogXmlString = XmlHelpers.beautifyXmlDocument(logXmlDocument.get());
        if (beautifiedLogXmlString.isEmpty()) {
            return;
        }

        if (this.writeXmlLogFile(beautifiedLogXmlString.get())) {
            System.out.println(
                    "Saved " + this.logFactory.getProgramActionList().size() + " actions to the logs directory!"
            );
        }
    }
}
