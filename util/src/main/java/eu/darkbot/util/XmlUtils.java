package eu.darkbot.util;

import lombok.experimental.UtilityClass;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@UtilityClass
public class XmlUtils {

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
