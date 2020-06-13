package com.mygdx.game.npc.cutscene;

import com.mygdx.game.utils.FileUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;



public class CutsceneParser {

    public static List<CutScene> parse(String filepath) {
        List<CutScene> cutScenes = new ArrayList<>();
        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            String body = FileUtils.getBody(filepath, true);
            InputSource is = new InputSource(new StringReader(body));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            cutScenes = optCutscene(element);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return cutScenes;
    }

    public static List<CutScene> optCutscene(Element element) {
        NodeList dialogs = element.getElementsByTagName("scene");
        List<CutScene> cutScenes = new ArrayList<>();
        for (int i = 0; i < dialogs.getLength(); i++) {
            Node item = dialogs.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && item.hasAttributes()) {// && item.getNodeName().equals("scene")d
                int id = optInt(item.getAttributes(), "id");
                List<Task> tasks = new ArrayList<>();
                NodeList childNodes = item.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node child = childNodes.item(j);
                    if (child.getNodeType() == Node.ELEMENT_NODE && child.hasAttributes() && child.getNodeName().equals("task")) {
                        NamedNodeMap attributes = child.getAttributes();
                        int taskId = optInt(attributes, "id");
                        String type = optString(attributes, "type");
                        Map<String, String> params = new HashMap<>();

                        for (int k = 0; k < attributes.getLength(); k++) {
                            Node node = attributes.item(k);
                            String nodeName = node.getNodeName();
                            if(!nodeName.equals("id") && !nodeName.equals("type")) {
                                String nodeValue = node.getNodeValue();
                                params.put(nodeName, nodeValue);
                            }
                        }
                        tasks.add(new Task(taskId, type, params));
                    }
                }
                cutScenes.add(new CutScene(id, tasks));

            }
        }
        return cutScenes;
    }

    public static int optInt(NamedNodeMap map, String name) {
        return Integer.parseInt(map.getNamedItem(name).getNodeValue());
    }

    public static String optString(NamedNodeMap map, String name) {
        return map.getNamedItem(name).getNodeValue();
    }

    public static boolean optBoolean(NamedNodeMap map, String name) {
        return Boolean.parseBoolean(map.getNamedItem(name).getNodeValue());
    }

}
