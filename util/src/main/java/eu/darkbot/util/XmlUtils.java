package eu.darkbot.util;

import lombok.experimental.UtilityClass;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@UtilityClass
public class XmlUtils {
    private static final ThreadLocal<DocumentBuilder> BUILDER = ThreadLocal.withInitial(XmlUtils::createBuilder);

    private static DocumentBuilder createBuilder() {
        try {
            // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#java
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Document parse(InputStream in) throws IOException, SAXException {
        return BUILDER.get().parse(in);
    }

    public static boolean hasChildElements(Element parent, String name) {
        return parent.getElementsByTagName(name).getLength() > 0;
    }

    public static Element getChildElement(Element parent, String name) {
        return (Element) parent.getElementsByTagName(name).item(0);
    }

    public static Integer attrToInt(Element e, String attr) {
        String value = e.getAttribute(attr);
        return value.isEmpty() ? null : Integer.parseInt(value);
    }

    public static Integer childValueToInt(Element e, String name) {
        return valueToInt(getChildElement(e, name));
    }

    public static Integer valueToInt(Element e) {
        if (e == null) return null;
        String value = e.getTextContent();
        return value == null || value.isEmpty() ? null : Integer.parseInt(value);
    }

    public static Stream<Element> streamOf(NodeList list) {
        return IntStream.range(0, list.getLength()).mapToObj(i -> (Element) list.item(i));
    }
}
