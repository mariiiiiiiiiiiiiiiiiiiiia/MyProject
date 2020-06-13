package com.mygdx.game.shit.level;

import com.mygdx.game.npc.Ghost;
import com.mygdx.game.obj.House;
import com.mygdx.game.shit.Dialog;
import com.mygdx.game.shit.Monolog;
import com.mygdx.game.shit.action.TriggerListener;
import com.mygdx.game.shit.trigger.Trigger;
import com.mygdx.game.utils.FileUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



public class LevelParser {
    public static final String levelFile = "/level.xml";

    public static Level parse(House house, String filepath) {
        String name = filepath.substring(filepath.indexOf("/") + 1, filepath.length());
        filepath = filepath.concat(levelFile);
        Map<Integer, Dialog> dialogsMap = new HashMap<>();
        Map<Integer, Monolog> monologsMap = new HashMap<>();
        List<Step> steps = null;
        List<Trigger> triggers = null;
        List<TriggerListener> triggerListeners = null;
        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            String body = FileUtils.getBody(filepath, true);
            InputSource is = new InputSource(new StringReader(body));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            dialogsMap = optDialogsMap(house, element);
            monologsMap = optMonologsMap(element);
            steps = optSteps(house, filepath.substring(0, filepath.length() - levelFile.length()), element);
            triggers = LQuestParser.optTriggers(element);
            triggerListeners = LQuestParser.optTriggerListeners(element);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Level(name, dialogsMap, monologsMap, steps, triggers, triggerListeners, house);
    }

    private static  Map<Integer, Dialog> optDialogsMap(House house, Element element) {
        Map<Integer, Dialog> dialogsMap = new HashMap<>();
        NodeList dialogs = element.getElementsByTagName("dialogs").item(0).getChildNodes();
        for (int i = 0; i < dialogs.getLength(); i++) {
            Node item = dialogs.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && item.hasAttributes()) {
                NamedNodeMap attributes = item.getAttributes();
                int id = optInt(attributes, "id");
                int ghostId = optInt(attributes, "ghost_id");

                Ghost ghost = house.getGhost(ghostId);

                List<Dialog.DialogGroup> groups = new ArrayList<>();

                NodeList dialogItems = item.getChildNodes();
                for (int j = 0; j < dialogItems.getLength(); j++) {
                    Node dialogItem = dialogItems.item(j);

                    if (dialogItem.getNodeType() == Node.ELEMENT_NODE) {
                        if (dialogItem.getNodeName().equals("group")) {
                            groups.add(optGroup(dialogItem, ghost));
                        }
                    }
                }
                dialogsMap.put(id, new Dialog(id, groups));
            }
        }
        return dialogsMap;
    }

    private static  Map<Integer, Monolog> optMonologsMap(Element element) {
        Map<Integer, Monolog> monologsMap = new HashMap<>();
        NodeList monologs = element.getElementsByTagName("monologs").item(0).getChildNodes();
        for (int i = 0; i < monologs.getLength(); i++) {
            Node item = monologs.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && item.hasAttributes()) {
                NamedNodeMap attributes = item.getAttributes();
                int id = optInt(attributes, "id");

                List<String> texts = new ArrayList<>();
                NodeList monologItems = item.getChildNodes();
                for (int j = 0; j < monologItems.getLength(); j++) {
                    Node monologItem = monologItems.item(j);

                    if (monologItem.getNodeType() == Node.ELEMENT_NODE) {
                        if (monologItem.getNodeName().equals("text")) {
                            texts.add(monologItem.getChildNodes().item(0).getNodeValue());
                        }
                    }
                }
                monologsMap.put(id, new Monolog(id, texts));
            }
        }
        return monologsMap;
    }

    private static List<Step> optSteps(House house, String filepath, Element element) {
        List<Step> steps = new ArrayList<>();
        NodeList stepNodeList = element.getElementsByTagName("steps").item(0).getChildNodes();
        for (int i = 0; i < stepNodeList.getLength(); i++) {
            Node stepListenerNode = stepNodeList.item(i);
            if (stepListenerNode.getNodeType() == Node.ELEMENT_NODE && stepListenerNode.getNodeName().equals("step")) {
                int number = optInt(stepListenerNode.getAttributes(), "num");
                NodeList stepQuestsNode = stepListenerNode.getChildNodes();
                List<LQuest> lQuests = new ArrayList<>();
                for (int j = 0; j < stepQuestsNode.getLength(); j++) {
                    Node actionNode = stepQuestsNode.item(j);
                    if (actionNode.getNodeType() == Node.ELEMENT_NODE) {
                        lQuests.add(LQuestParser.parse(house, filepath, actionNode));
                    }
                }
                steps.add(new Step(number, lQuests));
            }
        }
        return steps;
    }

    private static List<Dialog.DOption> getOptions(Node node) {
        List<Dialog.DOption> options = new ArrayList<>();
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node item = list.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE && item.getNodeName().equals("option")) {
                NamedNodeMap optionAttr = item.getAttributes();
                String nextGroupId = optionAttr.getNamedItem("group").getNodeValue();
                String step = optionAttr.getNamedItem("step").getNodeValue();
                int startStep = 0, endStep = 0;
                if (step.contains(":")) {
                    String[] split = step.split(":");
                    startStep = Integer.parseInt(split[0]);
                    endStep = Integer.parseInt(split[1]);
                } else {
                    startStep = Integer.parseInt(step);
                    endStep = startStep;
                }
                String textValue = item.getChildNodes().item(0).getNodeValue();
                options.add(new Dialog.DOption(textValue, Integer.parseInt(nextGroupId),
                        startStep, endStep));
            }
        }
        return options;
    }

    private static Dialog.DialogGroup optGroup(Node dialogItem, Ghost ghost) {
        NamedNodeMap group_attr = dialogItem.getAttributes();
        int groupId = optInt(group_attr, "id");
        boolean isGroupWithOptions = optBoolean(group_attr, "options");

        NodeList texts = dialogItem.getChildNodes();
        List<Dialog.DialogText> dialogTexts = new ArrayList<>();
        List<Dialog.DOption> optionsList = null;

        for (int k = 0; k < texts.getLength(); k++) {
            Node text = texts.item(k);
            if (text.getNodeType() == Node.ELEMENT_NODE) {
                if (isGroupWithOptions && text.getNodeName().equals("options")) {
                    optionsList = getOptions(text);
                } else if (text.getNodeName().equals("text")) {
                    String textValue = text.getChildNodes().item(0).getNodeValue();
                    NamedNodeMap textAttributes = text.getAttributes();
                    String who = textAttributes.getNamedItem("who").getNodeValue();
                    if (who.equals("hero"))
                        dialogTexts.add(new Dialog.DialogText(textValue));
                    else if (who.equals("ghost")) {
                        dialogTexts.add(new Dialog.GhostDialogText(textValue, ghost));
                    }
                    System.out.println(textValue);
                }
            }
        }
        Dialog.DialogGroup group;
        if (isGroupWithOptions) {
            group = new Dialog.DialogGroupWithOptions(groupId,
                    dialogTexts, optionsList);
        } else {
            group = new Dialog.DialogGroup(groupId, dialogTexts);
        }
        return group;
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
