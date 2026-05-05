package es.medac.soporte.aerocheck;

import org.w3c.dom.*;

public class DomTreeView {

    public static void main(String[] args) throws Exception {
        ConsoleEncoding.forceUtf8();

        Document doc = new TelemetryDomLoader().load();

        // Nodo raíz real del documento (primer elemento)
        Element root = doc.getDocumentElement();
        printNode(root, 0);
    }

    private static void printNode(Node node, int depth) {
        String indent = "  ".repeat(depth);

        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE -> {
                Element e = (Element) node;

                // Nombre de etiqueta
                System.out.print(indent + "<" + e.getTagName());

                // Atributos
                NamedNodeMap attrs = e.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node a = attrs.item(i);
                    System.out.print(" " + a.getNodeName() + "=\"" + a.getNodeValue() + "\"");
                }
                System.out.println(">");

                // Hijos
                NodeList children = e.getChildNodes();
                for (int i = 0; i < children.getLength(); i++) {
                    Node c = children.item(i);

                    // Ignorar texto vacío (saltos de línea/espacios)
                    if (c.getNodeType() == Node.TEXT_NODE) {
                        String txt = c.getTextContent().trim();
                        if (txt.isEmpty()) continue;
                    }
                    printNode(c, depth + 1);
                }

                System.out.println(indent + "</" + e.getTagName() + ">");
            }
            case Node.TEXT_NODE -> {
                String txt = node.getTextContent().trim();
                if (!txt.isEmpty()) {
                    System.out.println(indent + txt);
                }
            }
            default -> {
                // Otros tipos (comentarios, etc.)
                System.out.println(indent + "(Nodo tipo " + node.getNodeType() + ")");
            }
        }
    }
}
