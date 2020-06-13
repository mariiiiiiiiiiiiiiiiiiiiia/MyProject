package com.mygdx.game.shit.level;

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

import static com.mygdx.game.shit.level.LevelParser.optBoolean;
import static com.mygdx.game.shit.level.LevelParser.optInt;
import static com.mygdx.game.shit.level.LevelParser.optString;


public class LQuestParser {

    public static LQuest parse(House house, String filepath, Node node) {
        try {
            DocumentBuilderFactory dbFactory
                    = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            NamedNodeMap attr = node.getAttributes();
            int id = optInt(attr, "id");
            String file = optString(attr, "name");
            boolean visible = optBoolean(attr, "visible");

            String body = FileUtils.getBody(filepath.concat("/").concat(file), true);
            InputSource is = new InputSource(new StringReader(body));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            System.out.println();

            List<TriggerListener> triggerListeners = optTriggerListeners(element);

            List<Trigger> triggers = optTriggers(element);
            List<StepAction> onStart = optOnStart(element);
            List<StepAction> onEnd = optOnEnd(element);

            return new LQuest(id, file.substring(0, file.length() - 4), triggers, triggerListeners, onStart, onEnd, visible, house);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static List<TriggerListener> optTriggerListeners(Element element) {
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


    public static List<Trigger> optTriggers(Element element) {
        NodeList triggersNodeList = element.getElementsByTagName("triggers").item(0).getChildNodes();
        List<Trigger> triggers = new ArrayList<>();
        for (int i = 0; i < triggersNodeList.getLength(); i++) {
            Node triggerNode = triggersNodeList.item(i);
            if (triggerNode.getNodeType() == Node.ELEMENT_NODE && triggerNode.getNodeName().equals("trigger")) {
                NamedNodeMap triggerAttr = triggerNode.getAttributes();
                int id = optInt(triggerAttr, "id");
                String type = optString(triggerAttr, "type");
                int ifStep = -1;//optInt(triggerAttr, "ifStep");
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
                    StepAction stepAction = optStepAction(actionNode);
                    if(stepAction != null)
                        stepActions.add(stepAction);
                }
                stepListeners.add(new StepListener(number, stepActions));
            }
        }
        return stepListeners;
    }

    private static StepAction optStepAction(Node actionNode) {
        if (actionNode.getNodeType() == Node.ELEMENT_NODE) {
            String actionName = actionNode.getNodeName();
            NamedNodeMap attributes = actionNode.getAttributes();
            Map<String, String> params = new HashMap<>();
            for (int k = 0; k < attributes.getLength(); k++) {
                Node attr = attributes.item(k);
                params.put(attr.getNodeName(), attr.getNodeValue());
            }
            return new StepAction(actionName, params);
        }
        return null;
    }

    private static List<StepAction> optStepActions(Element element, String tagName) {
        List<StepAction> stepActions = new ArrayList<>();
        NodeList stepNodeList = element.getElementsByTagName(tagName).item(0).getChildNodes();
        for (int i = 0; i < stepNodeList.getLength(); i++) {
            Node stepActionNode = stepNodeList.item(i);
            StepAction sa = optStepAction(stepActionNode);
            if(sa != null)
                stepActions.add(sa);
        }
        return stepActions;
    }

    private static List<StepAction> optOnStart(Element element) {
       return optStepActions(element, "onStart");
    }

    private static List<StepAction> optOnEnd(Element element) {
        return optStepActions(element, "onEnd");

    }

}
