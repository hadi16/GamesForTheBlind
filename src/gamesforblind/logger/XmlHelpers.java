package gamesforblind.logger;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
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

public class XmlHelpers {
    public static Optional<Document> convertXmlStringToDocument(String xmlString) {
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource inputSource = new InputSource();
            inputSource.setCharacterStream(new StringReader(xmlString));
            return Optional.of(documentBuilder.parse(inputSource));
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static Optional<String> beautifyXmlDocument(Document xmlDocument) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StreamResult streamResult = new StreamResult(new StringWriter());
            transformer.transform(new DOMSource(xmlDocument), streamResult);

            return Optional.of(streamResult.getWriter().toString());
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static Optional<String> convertXmlNodeToString(Node xmlNode) {
        try {
            StringWriter stringWriter = new StringWriter();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(xmlNode), new StreamResult(stringWriter));

            return Optional.of(stringWriter.toString());
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
