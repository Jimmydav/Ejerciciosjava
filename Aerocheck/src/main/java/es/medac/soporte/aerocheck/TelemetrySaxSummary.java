package es.medac.soporte.aerocheck;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;

public class TelemetrySaxSummary {

    public static void main(String[] args) {
        ConsoleEncoding.forceUtf8();
        try (InputStream is = TelemetrySaxSummary.class.getResourceAsStream("/telemetria.xml")) {

            if (is == null) {
                throw new IllegalStateException("No se encuentra /telemetria.xml en src/main/resources");
            }

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false); // importante: sin DTD/XSD, mejor false para evitar errores
            SAXParser saxParser = factory.newSAXParser();

            SummaryHandler handler = new SummaryHandler();
            saxParser.parse(is, handler);

            handler.printSummary();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class SummaryHandler extends DefaultHandler {

        private int totalDrones = 0;
        private int totalVuelos = 0;
        private int totalEventos = 0;
        private int totalAlertas = 0;

        private boolean inBateria = false;
        private int bateriaMin = Integer.MAX_VALUE;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {

            if ("drone".equals(qName)) {
                totalDrones++;
            }

            if ("vuelo".equals(qName)) {
                totalVuelos++;
            }

            if ("evento".equals(qName)) {
                totalEventos++;
                String tipo = attributes.getValue("tipo");
                if ("ALERTA".equals(tipo)) {
                    totalAlertas++;
                }
            }

            if ("bateria".equals(qName)) {
                inBateria = true;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {

            if (!inBateria) return;

            String texto = new String(ch, start, length).trim();
            if (texto.isEmpty()) return;

            int b = Integer.parseInt(texto);
            if (b < bateriaMin) {
                bateriaMin = b;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if ("bateria".equals(qName)) {
                inBateria = false;
            }
        }

        public void printSummary() {
            System.out.println("=== RESUMEN SAX ===");
            System.out.println("Total drones  : " + totalDrones);
            System.out.println("Total vuelos  : " + totalVuelos);
            System.out.println("Total eventos : " + totalEventos);
            System.out.println("Total alertas : " + totalAlertas);
            System.out.println("Batería mínima: " + (bateriaMin == Integer.MAX_VALUE ? "N/D" : bateriaMin));
        }
    }
}
