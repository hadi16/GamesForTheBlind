package gamesforblind.logger;

import gamesforblind.ProgramAction;
import gamesforblind.sudoku.OriginalSudokuGrid;
import org.jetbrains.annotations.NotNull;
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

/**
 * Class to read a log file, so that the actions can be replayed in realtime for the user.
 */
public class LogReader {
    /**
     * Gets a list of XML nodes. The first {@link Node} in the list is always the {@link OriginalSudokuGrid} object
     * while each of the other {@link Node}s in list are individual {@link ProgramAction}s that were taken in the game.
     *
     * @param logFilePath The path to the XML log file, as a String.
     * @return A list of {@link Node}s in the game representing either the {@link OriginalSudokuGrid}
     * or an individual {@link ProgramAction} that was taken in the game.
     */
    private ArrayList<Node> getLogFileXmlNodeList(@NotNull String logFilePath) {
        // Step 1: read in the XML log file's raw contents as a String.
        String logXmlString;
        try {
            logXmlString = new String(Files.readAllBytes(Paths.get(logFilePath)));
        } catch (IOException e) {
            return new ArrayList<>();
        }

        // Step 2: convert this XML String to a Document.
        Optional<Document> logXmlDocument = XmlHelpers.convertXmlStringToDocument(logXmlString);
        if (logXmlDocument.isEmpty()) {
            return new ArrayList<>();
        }

        // Step 3: get the children of <log>, which hold the original Sudoku board state & actions in the game.
        ArrayList<Node> logXmlNodeList = new ArrayList<>();
        NodeList logChildren = logXmlDocument.get().getChildNodes().item(0).getChildNodes();

        // Step 4: loop over all of these children & add the non-textual Nodes to the list of nodes.
        for (int i = 0; i < logChildren.getLength(); i++) {
            Node logChild = logChildren.item(i);
            if (logChild.getNodeType() == Node.TEXT_NODE) {
                continue;
            }

            logXmlNodeList.add(logChild);
        }

        return logXmlNodeList;
    }

    /**
     * Converts a XML {@link Node} to a Java object.
     *
     * @param xmlNode {@link Node} to convert. Either a {@link ProgramAction} in game or {@link OriginalSudokuGrid} object.
     * @return A Java object that is an instance of {@link OriginalSudokuGrid} or {@link ProgramAction}.
     * If an error occurred, just return empty().
     */
    private Optional<Object> convertXmlLogNodeToObject(@NotNull Node xmlNode) {
        try {
            Class originalGridClass = OriginalSudokuGrid.class;

            String xmlNodeName = xmlNode.getNodeName();
            Class classToMarshall;

            // Gets the class name without the package name.
            if (xmlNodeName.equals(originalGridClass.getSimpleName())) {
                classToMarshall = originalGridClass;
            } else if (xmlNodeName.toLowerCase().contains("loader")) {
                // Need the fully qualified name to the class that needs marshalling.
                classToMarshall = Class.forName("gamesforblind.loader.action." + xmlNodeName);
            } else if (xmlNodeName.toLowerCase().contains("sudoku")) {
                // Need the fully qualified name to the class that needs marshalling.
                classToMarshall = Class.forName("gamesforblind.sudoku.action." + xmlNodeName);
            } else {
                throw new IllegalArgumentException("Invalid log file class name: " + xmlNodeName);
            }

            JAXBContext jaxbContext = JAXBContext.newInstance(classToMarshall);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return Optional.of(jaxbUnmarshaller.unmarshal(xmlNode));
        } catch (JAXBException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // If an error occurred, just return empty().
        return Optional.empty();
    }

    /**
     * Gets a {@link LogFactory} object containing the log file's saved program state.
     *
     * @param logFilePath The path to the XML log file, as a String.
     * @return A {@link LogFactory} object containing the log file's saved state.
     */
    public LogFactory restoreLoggedProgram(@NotNull String logFilePath) {
        // Step 1: Get the list of XML Nodes from the log file.
        ArrayList<Node> xmlNodeList = this.getLogFileXmlNodeList(logFilePath);

        if (xmlNodeList.isEmpty()) {
            return new LogFactory();
        }

        LogFactory logFactory = new LogFactory();
        for (Node xmlNode : xmlNodeList) {
            // Step 2: convert each XML node to a Java object.
            Optional<Object> logItems = this.convertXmlLogNodeToObject(xmlNode);

            // Step 3: if an action, add it to the list of actions. Otherwise, add it to the list of OriginalSudokuGrids
            logItems.ifPresent(logItem -> {
                if (logItem instanceof OriginalSudokuGrid) {
                    logFactory.addOriginalSudokuGrid((OriginalSudokuGrid) logItem);
                } else if (logItem instanceof ProgramAction) {
                    logFactory.addProgramAction((ProgramAction) logItem);
                }
            });
        }

        return logFactory;
    }
}
