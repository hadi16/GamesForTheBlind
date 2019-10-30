package gamesforblind.logger;

import gamesforblind.ProgramAction;
import gamesforblind.sudoku.OriginalSudokuGrid;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class LogReader {
    private ArrayList<Node> getLogFileXmlNodeList(String logFilePath) {
        String actionsXmlString;
        try {
            actionsXmlString = new String(Files.readAllBytes(Paths.get(logFilePath)));
        } catch (IOException e) {
            return new ArrayList<>();
        }

        Optional<Document> actionsXmlDocument = XmlHelpers.convertXmlStringToDocument(actionsXmlString);
        if (actionsXmlDocument.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Node> actionsNodeList = new ArrayList<>();
        NodeList actionsChildren = actionsXmlDocument.get().getChildNodes().item(0).getChildNodes();

        for (int i = 0; i < actionsChildren.getLength(); i++) {
            Node actionsChild = actionsChildren.item(i);
            if (actionsChild.getNodeType() == Node.TEXT_NODE) {
                continue;
            }

            actionsNodeList.add(actionsChild);
        }

        return actionsNodeList;
    }

    private Optional<Object> convertXmlNodeToAction(Node xmlNode) {
        try {
            String actionClassName = xmlNode.getNodeName();
            Class actionClass;
            if (actionClassName.equals("OriginalSudokuGrid")) {
                actionClass = OriginalSudokuGrid.class;
            } else if (actionClassName.toLowerCase().contains("loader")) {
                actionClass = Class.forName("gamesforblind.loader.action." + actionClassName);
            } else if (actionClassName.toLowerCase().contains("sudoku")) {
                actionClass = Class.forName("gamesforblind.sudoku.action." + actionClassName);
            } else {
                throw new IllegalArgumentException("Invalid action class name: " + actionClassName);
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(actionClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return Optional.of(jaxbUnmarshaller.unmarshal(xmlNode));
        } catch (JAXBException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public LogFactory restoreLoggedProgramActions(String logFilePath) {
        ArrayList<Node> xmlNodeList = this.getLogFileXmlNodeList(logFilePath);
        if (xmlNodeList.isEmpty()) {
            return new LogFactory();
        }

        LogFactory logFactory = new LogFactory();
        for (Node actionNode : xmlNodeList) {
            Optional<Object> logItems = this.convertXmlNodeToAction(actionNode);

            logItems.ifPresent(logItem -> {
                if (logItem instanceof OriginalSudokuGrid) {
                    logFactory.setOriginalSudokuGrid((OriginalSudokuGrid) logItem);
                } else if (logItem instanceof ProgramAction) {
                    logFactory.addProgramAction((ProgramAction) logItem);
                }
            });
        }

        return logFactory;
    }
}
