package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Const;
import com.mygdx.game.TextureManager;
import com.mygdx.game.utils.FillType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomSubject extends HouseObject {


    protected boolean sng;
    private int id;
    private String name;
    private String descriptions[];
    private Set<Type> types;
    private String song;

    private List<Subject> subjects;
    private TextureRegion image;
    private String specialDescription;
    private int roomId;
    private boolean visible;
    private String specialSearchDescription;
    private String regionName;


    public RoomSubject(House house, JSONObject object, int roomId) {
        super(house, object);
        this.roomId = roomId;
        id = object.optInt(FillType.ID);
        name = object.getString(FillType.NAME);
        float x = (float) object.optDouble(FillType.X);
        float y = (float) object.optDouble(FillType.Y);
        float w = (float) object.optDouble(FillType.WIDTH);
        float h = (float) object.optDouble(FillType.HEIGHT);
        song = object.optString(FillType.SONG);
        sng = object.optBoolean(FillType.SNG);
        JSONArray array = object.optJSONArray(FillType.TYPE);
        types = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            types.add(Type.convert(array.optString(i)));
        }

        JSONArray descriptionsArray = object.optJSONArray(FillType.DESCRIPTION);
        descriptions = new String[descriptionsArray.length()];

        for (int i = 0; i < descriptionsArray.length(); i++) {
            descriptions[i] = descriptionsArray.optString(i);
        }

        String type = object.optString(FillType.IS_STATIC_TYPE);
        if (type == null || !type.equals(FillType.ANIMATION_TYPE_VALUE)) {

            regionName = object.optString(FillType.BACKGROUND);
            regionName = regionName.substring(0, regionName.length() - 4);
            image = TextureManager.getInstance().region(regionName);

        }
        setBounds(x * Const.W, y * Const.H, Const.W * w, Const.H * h);

        if (canSearch()) {
            JSONArray subjectsJson = object.optJSONArray(FillType.SUBJECTS);
            subjects = new ArrayList<>();
            for (int i = 0; i < subjectsJson.length(); i++) {
                subjects.add(new Subject(subjectsJson.optJSONObject(i)));
            }
        }
        specialDescription = null;
        specialSearchDescription = null;
        visible = true;
    }

    public Set<Type> getTypes() {
        return types;
    }

    public int getRoomId() {
        return roomId;
    }

    public boolean canHide() {
        for (Type type : types) {
            if (type.equals(Type.HIDE))
                return true;
        }
        return false;
    }

    public String getSpecialDescription() {
        return specialDescription;
    }

    public String getSpecialSearchDescription() {
        return specialSearchDescription;
    }

    public void setSpecialDescription(String specialDescription) {
        this.specialDescription = specialDescription;
    }

    public void setSpecialSearchDescription(String specialSearchDescription) {
        this.specialSearchDescription = specialSearchDescription;
    }
    public void removeType(Type t) {
        types.remove(t);
    }

    public void addType(Type t) {
        types.add(t);
        if(t.equals(Type.SEARCH) && subjects == null) {
            subjects = new ArrayList<>();
        }
    }

    public boolean canSearch() {
        for (Type type : types) {
            if (type.equals(Type.SEARCH))
                return true;
        }
        return false;
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String[] getDescriptions() {
        return descriptions;
    }

    public int getDescriptionsCount() {
        return descriptions.length;
    }

    public String getDescription(int index) {
        return descriptions[index];
    }

    public String getSong() {
        return song;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public void clearAll() {
        subjects.clear();
    }

    public boolean isIntersect(float x, float y) {
        return this.getBoundingRectangle().contains(x, y);
    }

    public void setTexture(String file) {
        this.regionName = file;
        image = TextureManager.getInstance().region(file);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Subject> getSubject() {
        return subjects;
    }

    public void clearSubjects() {
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).isVisible()) {
                subjects.remove(i);
                i--;
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        if(visible)
            super.draw(batch);
    }

    public boolean isSng() {
        return sng;
    }

    public String getRegionName() {
        return regionName;
    }

    @Override
    public TextureRegion getFrame(float dt) {
        return image;
    }

    public void removeAllType() {
        types.clear();
    }

    public enum Type {
        SEARCH, HIDE, SPEAK;

        public static Type convert(String str) {
            switch (str) {
                case "search":
                    return SEARCH;
                case "hide":
                    return HIDE;
                case "speak":
                    return SPEAK;
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return "RoomSubject{" +
                "sng=" + sng +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", descriptions=" + Arrays.toString(descriptions) +
                ", types=" + types +
                ", song='" + song + '\'' +
                ", subjects=" + subjects +
                ", image=" + image +
                ", specialDescription='" + specialDescription + '\'' +
                ", roomId=" + roomId +
                ", visible=" + visible +
                ", specialSearchDescription='" + specialSearchDescription + '\'' +
                ", regionName='" + regionName + '\'' +
                '}';
    }
}
