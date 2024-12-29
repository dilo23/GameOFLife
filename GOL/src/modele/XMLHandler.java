package modele;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerFactory;
import org.w3c.dom.*;
import java.io.File;

public class XMLHandler {

    public static void saveState(Environnement env, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Grille");
            doc.appendChild(rootElement);

            // Parcours de la grille et enregistrement de l'état de chaque cellule
            for (int i = 0; i < env.getSizeX(); i++) {
                for (int j = 0; j < env.getSizeY(); j++) {
                    Element caseElement = doc.createElement("Case");
                    caseElement.setAttribute("x", String.valueOf(i));
                    caseElement.setAttribute("y", String.valueOf(j));
                    caseElement.setTextContent(String.valueOf(env.getState(i, j)));
                    rootElement.appendChild(caseElement);
                }
            }

            // Écriture du fichier XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void loadState(Environnement env, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Case");

            // Lecture de chaque case et mise à jour de l'état dans l'environnement
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    int x = Integer.parseInt(element.getAttribute("x"));
                    int y = Integer.parseInt(element.getAttribute("y"));
                    boolean state = Boolean.parseBoolean(element.getTextContent());
                    env.setState(x, y, state);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
