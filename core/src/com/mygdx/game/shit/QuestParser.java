package com.mygdx.game.shit;

import com.mygdx.game.npc.Ghost;
import com.mygdx.game.obj.House;
import com.mygdx.game.shit.action.StepAction;
import com.mygdx.game.shit.action.StepListener;
import com.mygdx.game.shit.action.TriggerListener;
import com.mygdx.game.shit.trigger.CatchTrigger;
import com.mygdx.game.shit.trigger.ClickTrigger;
import com.mygdx.game.shit.trigger.ComeInTrigger;
import com.mygdx.game.shit.trigger.FindTrigger;
import com.mygdx.game.shit.trigger.HideTrigger;
import com.mygdx.game.shit.trigger.SearchTrigger;
import com.mygdx.game.shit.trigger.TalkTrigger;
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



public class QuestParser {

    public static Quest parse(House house, String filepath) {
        String name = filepath.substring(filepath.indexOf("/") + 1, filepath.length() - 4);
        Map<Integer, Dialog> dialogsMap = new HashMap<>();
        Map<Integer, Monolog> monologsMap = new HashMap<>();
        List<Trigger> triggers = null;
        List<StepListener> stepListeners = null;
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
            triggers = optTriggers(element);
            stepListeners = optStepListeners(element);
            triggerListeners = optTriggerListeners(element);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Quest(house, dialogsMap, monologsMap, triggers, stepListeners, triggerListeners, name);
    }

    private static List<StepListener> optStepListeners(Element element) {
        NodeList stepNodeList = element.getElementsByTagName("step-listener").item(0).getChildNodes();
        List<StepListener> stepListeners = new ArrayList<>();
        for (int i = 0; i < stepNodeList.getLength(); i++) {
            Node stepListenerNode = stepNodeList.item(i);
            if (stepListenerNode.getNodeType() == Node.ELEMENT_NODE && stepListenerNode.getNodeName().equals("step")) {
                int number = optInt(stepListenerNode.getAttributes(), "number");
                NodeList stepActionsNode = stepListenerNode.getChildNodes();
                List<StepAction> stepActions = new ArrayList<>();
                for (int j = 0; j < stepActionsNode.getLength(); j++) {
                    Node actionNode = stepActionsNode.item(j);
                    if (actionNode.getNodeType() == Node.ELEMENT_NODE) {
                        String actionName = actionNode.getNodeName();
                        NamedNodeMap attributes = actionNode.getAttributes();
                        Map<String, String> params = new HashMap<>();
                        for (int k = 0; k < attributes.getLength(); k++) {
                            Node attr = attributes.item(k);
                            params.put(attr.getNodeName(), attr.getNodeValue());
                        }
                        stepActions.add(new StepAction(actionName, params));
                    }
                }
                stepListeners.add(new StepListener(number, stepActions));
            }
        }
        return stepListeners;
    }

    private static List<TriggerListener> optTriggerListeners(Element element) {
        NodeList triggerNodeList = element.getElementsByTagName("trigger-listeners").item(0).getChildNodes();
        List<TriggerListener> triggerListeners = new ArrayList<>();
        for (int i = 0; i < triggerNodeList.getLength(); i++) {
            Node triggerListenerNode = triggerNodeList.item(i);
            if (triggerListenerNode.getNodeType() == Node.ELEMENT_NODE && triggerListenerNode.getNodeName().equals("listener")) {
                int tid = optInt(triggerListenerNode.getAttributes(), "tid");
                NodeList triggerActionsNode = triggerListenerNode.getChildNodes();
                List<StepAction> triggerActions = new ArrayList<>();
                for (int j = 0; j < triggerActionsNode.getLength(); j++) {
                    Node actionNode = triggerActionsNode.item(j);
                    if (actionNode.getNodeType() == Node.ELEMENT_NODE) {
                        String actionName = actionNode.getNodeName();
                        NamedNodeMap attributes = actionNode.getAttributes();
                        Map<String, String> params = new HashMap<>();
                        for (int k = 0; k < attributes.getLength(); k++) {
                            Node attr = attributes.item(k);
                            params.put(attr.getNodeName(), attr.getNodeValue());
                        }
                        triggerActions.add(new StepAction(actionName, params));
                    }
                }
                triggerListeners.add(new TriggerListener(tid, triggerActions));
            }
        }
        return triggerListeners;
    }

    private static List<Trigger> optTriggers(Element element) {
        NodeList triggersNodeList = element.getElementsByTagName("triggers").item(0).getChildNodes();
        List<Trigger> triggers = new ArrayList<>();
        for (int i = 0; i < triggersNodeList.getLength(); i++) {
            Node triggerNode = triggersNodeList.item(i);
            if (triggerNode.getNodeType() == Node.ELEMENT_NODE && triggerNode.getNodeName().equals("trigger")) {
                NamedNodeMap triggerAttr = triggerNode.getAttributes();
                int id = optInt(triggerAttr, "id");
                String type = optString(triggerAttr, "type");
                int ifStep = optInt(triggerAttr, "ifStep");
                Trigger trigger = null;
                switch (type) {
                    case "talk":
                        String dialog = optString(triggerAttr, "dialog");
                        String[] dg = dialog.split(":");
                        trigger = new TalkTrigger(id, ifStep, Integer.parseInt(dg[0]),
                                Integer.parseInt(dg[1]), true);
                        break;
                    case "click":
                        String room_subject_type = optString(triggerAttr, "room_subject_type");
                        String sid = optString(triggerAttr, "sid");
                        trigger = new ClickTrigger(id, ifStep, ClickTrigger.SubjectType.valueOf(room_subject_type), sid, true);
                        break;
                    case "find":
                        int fSid = optInt(triggerAttr, "sid");
                        trigger = new FindTrigger(id, ifStep, fSid, true);
                        break;
                    case "comeIn":
                        int roomId = optInt(triggerAttr, "room");
                        trigger = new ComeInTrigger(id, ifStep, roomId, true);
                        break;
                    case "catch":
                        trigger = new CatchTrigger(id, ifStep, true);
                        break;
                    case "search":
                        int sSid = optInt(triggerAttr, "sid");
                        trigger = new SearchTrigger(id, ifStep, sSid, true);
                        break;
                    case "hide":
                        int hSid = optInt(triggerAttr, "rsid");
                        trigger = new HideTrigger(id, ifStep, hSid, true);
                        break;
                }
                triggers.add(trigger);
            }
        }
        return triggers;
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
