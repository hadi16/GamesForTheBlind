package gamesforblind.logger;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Contains various static helper functions for dealing with XML Strings & Documents.
 */
public class XmlHelpers {
    /**
     * Converts a XML String to a {@link Document}.
     *
     * @param xmlString The XML String to convert.
     * @return A {@link Document} that is the equivalent of the passed XML String. If an error occurred, return empty().
     */
    public static Optional<Document> convertXmlStringToDocument(@NotNull String xmlString) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xmlString));
            return Optional.of(documentBuilder.parse(inputSource));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        // If an error occurred, return just empty()
        return Optional.empty();
    }

    /**
     * "Beautify" a XML {@link Document} (properly indent it).
     *
     * @param xmlDocument The XML {@link Document} to beautify.
     * @return A XML String that has been properly indented/beautified. If an error occurred, return empty().
     */
    public static Optional<String> beautifyXmlDocument(@NotNull Document xmlDocument) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Set the indentation amount to 2.
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult streamResult = new StreamResult(new StringWriter());
            transformer.transform(new DOMSource(xmlDocument), streamResult);

            return Optional.of(streamResult.getWriter().toString());
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        // If an error occurred, return just empty()
        return Optional.empty();
    }
}
