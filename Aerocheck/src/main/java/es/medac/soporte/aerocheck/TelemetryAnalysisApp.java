package es.medac.soporte.aerocheck;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TelemetryAnalysisApp {

    public static void main(String[] args) {
    ConsoleEncoding.forceUtf8();
        try {
            // 1) Carga DOM
            Document doc = new TelemetryDomLoader().load();

            // 2) Consultas XPath
            TelemetryXPathQueries q = new TelemetryXPathQueries();

            System.out.println("=== ALERTAS (XPath) ===");
            List<String> alertas = q.alertasResumen(doc);
            for (String a : alertas) System.out.println(a);

            System.out.println("\n=== DRONES CON BATERIA < 20 (XPath) ===");
            System.out.println(q.dronesConBateriaBaja(doc, 20));

            System.out.println("\n=== ULTIMO GPS POR DRONE (XPath) ===");
            Map<String, String> gps = q.ultimoGpsPorDrone(doc);
            for (String id : gps.keySet()) {
                System.out.println(id + " -> " + gps.get(id));
            }

        } catch (ParserConfigurationException e) {
            System.err.println("Error de configuración del parser DOM: " + e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            System.err.println("El XML no es válido o está mal formado: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error de lectura del recurso telemetria.xml: " + e.getMessage());
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            System.err.println("Expresión XPath inválida: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
