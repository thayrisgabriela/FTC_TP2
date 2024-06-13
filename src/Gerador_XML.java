package FTC_TP2.src;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Gerador_XML {
    public void escrever_xml(HashMap<String, State> afd, String path) throws IOException {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("structure");
            doc.appendChild(rootElement);

            // Type element
            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode("fa"));
            rootElement.appendChild(type);

            // Automaton element
            Element automaton = doc.createElement("automaton");
            rootElement.appendChild(automaton);

            // Add states and transitions
            for (State state : afd.values()) {
                Element xmlState = doc.createElement("state");
                xmlState.setAttribute("id", state.getName());
                xmlState.setAttribute("name", state.getName());
                automaton.appendChild(xmlState);

                if (state.isInitial()) {
                    Element initial = doc.createElement("initial");
                    xmlState.appendChild(initial);
                }

                if (state.getAcceptance()) {
                    Element finale = doc.createElement("final");
                    xmlState.appendChild(finale);
                }

                for (String symbol : state.getTransitions().keySet()) {
                    for (State target : state.getTransitions().get(symbol)) {
                        Element transition = doc.createElement("transition");
                        automaton.appendChild(transition);

                        Element from = doc.createElement("from");
                        from.appendChild(doc.createTextNode(state.getName()));
                        transition.appendChild(from);

                        Element to = doc.createElement("to");
                        to.appendChild(doc.createTextNode(target.getName()));
                        transition.appendChild(to);

                        Element read = doc.createElement("read");
                        read.appendChild(doc.createTextNode(symbol));
                        transition.appendChild(read);
                    }
                }
            }

            // Write the content into XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(path));

            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }
}
