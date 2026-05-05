package es.medac.soporte.aerocheck;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class TelemetryDomLoader {

    public Document load() throws ParserConfigurationException, IOException, SAXException {
        try (InputStream is = TelemetryDomLoader.class.getResourceAsStream("/telemetria.xml")) {

            if (is == null) {
                throw new IllegalStateException("No se encuentra /telemetria.xml en src/main/resources");
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false); // sin DTD/XSD -> no validar
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(is);
        }
    }
}
