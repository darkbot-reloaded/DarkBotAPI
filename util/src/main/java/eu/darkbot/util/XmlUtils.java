package eu.darkbot.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XmlUtils {
    private static DocumentBuilderFactory FACTORY;

    // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#java
    public static Document parse(InputStream in) throws Exception {
        if (FACTORY == null) {
            FACTORY = DocumentBuilderFactory.newInstance();
            FACTORY.setXIncludeAware(false);
            FACTORY.setExpandEntityReferences(false);
            FACTORY.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            FACTORY.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        }

        return FACTORY.newDocumentBuilder().parse(in);
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
