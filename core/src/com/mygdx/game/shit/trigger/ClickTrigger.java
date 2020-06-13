package com.mygdx.game.shit.trigger;

public class ClickTrigger extends Trigger {

    private SubjectType subjectType;
    private String sid;

    public ClickTrigger(int id, int ifStep, SubjectType subjectType, String sid, boolean enabled) {
        super(id, Type.CLICK, ifStep, enabled);
        this.sid = sid;
        this.subjectType = subjectType;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }

    public String getSid() {
        return sid;
    }

    @Override
    public boolean validate(Object[] params) {
        try {
            int x = (Integer) params[1];
            int y = (Integer) params[2];
            SubjectType type = (SubjectType) params[3];

            if(!type.equals(getSubjectType()))
                return false;

            if (getSubjectType() == SubjectType.SUBJECT) {
                int roomSubjectId = (Integer) params[0];
                if(getSid().contains(",")) {
                    String sp[] = getSid().split(",");
                    for (String id : sp) {
                        int idd = Integer.parseInt(id);
                        if(idd == roomSubjectId) {
                            return true;
                        }
                    }
                } else if(Integer.parseInt(getSid()) == roomSubjectId){
                    return true;
                }
            } else if(getSubjectType() == SubjectType.DOOR){
                String doorId = (String) params[0];
                return doorId.equals(sid);
            }
        }catch (Exception e) {
            throw new IllegalArgumentException("Неправильные аргументы для ClickTrigger");
        }

        return false;
    }

    public enum SubjectType {
        DOOR, SUBJECT
    }
}
