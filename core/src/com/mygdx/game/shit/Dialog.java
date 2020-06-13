package com.mygdx.game.shit;

import com.mygdx.game.npc.Ghost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Dialog {

    private List<DialogGroup> dialogGroups;
    private Map<Integer, DialogGroup> dialogGroupMap;
    private DialogGroup currentGroup;
    private int currentText;
    private int id;

    public Dialog(int id, List<DialogGroup> dialogGroups) {
        this.id = id;
        this.dialogGroups = dialogGroups;
        dialogGroupMap = new HashMap<>();
        for (DialogGroup group : dialogGroups) {
            group.setDialog(this);
            dialogGroupMap.put(group.getId(), group);
        }
        currentText = 0;
    }

    public DialogText currentText() {
        return currentGroup.texts.get(currentText);
    }

    public void setFirstGroup() {
        currentGroup = dialogGroups.get(0);
        currentText = 0;
    }

    public int getId() {
        return id;
    }

    public boolean hasNextText() {
        return currentText + 1 != currentGroup.texts.size();
    }

    public boolean isCurrentGroupWithOptions() {
        return currentGroup instanceof DialogGroupWithOptions;
    }

    public DialogText nextText() {
        return currentGroup.texts.get(++currentText);
    }

    public DialogGroup currentGroup() {
        return currentGroup;
    }

    public void choose(int index) {
        DialogGroupWithOptions dwo = (DialogGroupWithOptions) this.currentGroup;
        DOption option = dwo.options.get(index);
        currentGroup = getGroup(option.groupId);
        currentText = 0;
    }

    public DialogGroup getGroup(int id) {
        return dialogGroupMap.get(id);
    }

    public static class DialogText {
        private String text;

        public DialogText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public static class GhostDialogText extends DialogText {
        private Ghost ghost;

        public GhostDialogText(String text, Ghost ghost) {
            super(text);
            this.ghost = ghost;
        }

        public Ghost getGhost() {
            return ghost;
        }
    }

    public static class DialogGroupWithOptions extends DialogGroup {

        private List<DOption> options;

        public DialogGroupWithOptions(int id, List<DialogText> texts, List<DOption> options) {
            super(id, texts);
            this.options = options;
        }

        public List<DOption> getOptions() {
            return options;
        }

        public List<DOption> getOptionsByStep() {
            return options;
        }
    }

    public static class DOption {
        private String text;
        private int groupId;
        private int startStep, endStep;

        public DOption(String text, int groupId, int startStep, int endStep) {
            this.text = text;
            this.groupId = groupId;
            this.startStep = startStep;
            this.endStep = endStep;
        }

        public String getText() {
            return text;
        }

        public int getGroupId() {
            return groupId;
        }
    }

    public static class DialogGroup {
        private int id;
        private List<DialogText> texts;
        private Dialog dialog;

        public DialogGroup(int id, List<DialogText> texts) {
            this.id = id;
            this.texts = texts;
        }

        public Dialog getDialog() {
            return dialog;
        }

        private void setDialog(Dialog dialog) {
            this.dialog = dialog;
        }

        public int getId() {
            return id;
        }
    }


}
