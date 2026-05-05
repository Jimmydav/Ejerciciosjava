package es.medac.soporte.aerocheck;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TelemetryXPathQueries {

    private final XPath xPath = XPathFactory.newInstance().newXPath();

    // 1) Eventos ALERTA: ts + codigo (si existe)
    public List<String> alertasResumen(Document doc) throws XPathExpressionException {
        String expr = "//evento[@tipo='ALERTA']";
        NodeList nodes = (NodeList) xPath.compile(expr).evaluate(doc, XPathConstants.NODESET);

        List<String> out = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node evento = nodes.item(i);

            String ts = evento.getAttributes().getNamedItem("ts").getNodeValue();

            // Buscar subelemento <codigo>
            String codigo = null;
            NodeList hijos = evento.getChildNodes();
            for (int j = 0; j < hijos.getLength(); j++) {
                Node h = hijos.item(j);
                if ("codigo".equals(h.getNodeName())) {
                    codigo = h.getTextContent().trim();
                    break;
                }
            }

            out.add(ts + " | " + (codigo == null ? "SIN_CODIGO" : codigo));
        }
        return out;
    }

    // 2) Drones con batería por debajo de un umbral (ej: 20)
    public List<String> dronesConBateriaBaja(Document doc, int umbral) throws XPathExpressionException {
        String expr = "//drone[.//evento/bateria < " + umbral + "]";
        NodeList nodes = (NodeList) xPath.compile(expr).evaluate(doc, XPathConstants.NODESET);

        List<String> ids = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node drone = nodes.item(i);
            ids.add(drone.getAttributes().getNamedItem("id").getNodeValue());
        }
        return ids;
    }

    // 3) Último GPS por cada drone (si existe)
    public Map<String, String> ultimoGpsPorDrone(Document doc) throws XPathExpressionException {
        NodeList drones = (NodeList) xPath.compile("//drone").evaluate(doc, XPathConstants.NODESET);

        Map<String, String> out = new LinkedHashMap<>();
        for (int i = 0; i < drones.getLength(); i++) {
            Node drone = drones.item(i);
            String id = drone.getAttributes().getNamedItem("id").getNodeValue();

            Node gps = (Node) xPath.compile(".//gps[last()]").evaluate(drone, XPathConstants.NODE);

            if (gps != null && gps.getAttributes() != null) {
                String lat = gps.getAttributes().getNamedItem("lat").getNodeValue();
                String lon = gps.getAttributes().getNamedItem("lon").getNodeValue();
                out.put(id, lat + "," + lon);
            } else {
                out.put(id, "N/D");
            }
        }
        return out;
    }
}
