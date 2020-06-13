package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.DebugOptions;
import com.mygdx.game.GhostFindBlackScreen;
import com.mygdx.game.MyAnimation;
import com.mygdx.game.MyGame;
import com.mygdx.game.factory.DoorFactory;
import com.mygdx.game.npc.BadGhost;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.npc.cutscene.CutsceneManager;
import com.mygdx.game.utils.FillType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class House {

    public interface OnEndGame {
        void onEnd();
    }
    private List<ChangeLogItem> items;
    private Room rooms[];
    private Door doors[][];
    private List<Ghost> ghostList;
    private HashMap<String, Door> doorsMap;
    private HashMap<Integer, Room> roomsMap;
    private Room currentRoom;
    private Hero hero;
    public MyGame myGame;

    private OnEndGame endGame;

    public House(JSONObject object, MyGame myGame, OnEndGame endGame) {
        this.endGame = endGame;
        this.myGame = myGame;
        doorsMap = new HashMap<>();
        roomsMap = new HashMap<>();
        items = new ArrayList<>();
        parseJson(object);

        hero = new Hero(this, object);
        setCurrentRoom(rooms[13], null);
    }

    public void addChangeLog(ChangeLogItem item) {
        boolean change = false;
        for (ChangeLogItem logItem : items) {
            if (logItem.roomId == item.roomId && logItem.rsId == item.rsId) {
                logItem.list = item.list;
                change = true;
                break;
            }
        }
        if (!change) {
            items.add(item);
        }
    }

    public List<ChangeLogItem> getItems() {
        return items;
    }

    public Room[] getRooms() {
        return rooms;
    }

    private void parseJson(JSONObject object) {
        int w = object.optInt(FillType.HOUSE_W);
        int h = object.optInt(FillType.HOUSE_H);

        JSONArray array = object.optJSONArray(FillType.HOUSE_ROOMS);
        rooms = new Room[array.length()];
        int maxX = -1;
        int maxY = -1;
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new Room(this, array.optJSONObject(i));
            Room room = rooms[i];
            if (room.getHouseX() > maxX)
                maxX = room.getHouseX();

            if (room.getHouseY() > maxY)
                maxY = room.getHouseY();

            roomsMap.put(room.getId(), room);
        }

        Room[][] roomsMatrix = new Room[maxX][maxY];
        for (Room room : rooms) {
            roomsMatrix[room.getHouseX() - 1][room.getHouseY() - 1] = room;
        }

        doors = new Door[rooms.length][rooms.length];

        JSONArray doorsJson = object.optJSONArray(FillType.DOORS);
        for (int i = 0; i < doorsJson.length(); i++) {
            JSONObject doorJson = doorsJson.optJSONObject(i);
            Door door = DoorFactory.createDoor(this, doorJson);
            doorsMap.put(door.getId(), door);

            int d1 = door.from.getId();
            int d2 = door.to.getId();
            doors[d1 - 1][d2 - 1] = door;
        }

        ghostList = new ArrayList<>();
        JSONArray ghosts = object.optJSONArray(FillType.GHOSTS);
        for (int i = 0; i < ghosts.length(); i++) {
            JSONObject js = ghosts.optJSONObject(i);
            boolean isBad = js.optBoolean(FillType.IS_BAD, false);
            if (isBad)
                ghostList.add(new BadGhost(this, js));
            else
                ghostList.add(new Ghost(this, js));
        }
    }

    public void update(float dt) {
        currentRoom.update(dt);
        for (Ghost ghost : ghostList) {
            ghost.update(dt);
        }
        hero.update(dt);

        for (Ghost ghost : ghostList) {
            if (ghost.isBad() && ghost.getRoom() == currentRoom
                    && ((BadGhost) ghost).isIntersect(hero)&& !CutsceneManager.getInstance().isSomeCutscenePlaying()) {
                ghost.setAgring(false);
                hero.setVisible(true);
                hero.stopWalking();
                myGame.setScreen(new GhostFindBlackScreen(this, myGame, ghost));
                break;
            }
        }
    }

    public RoomSubject findRoomSubject(int rsId) {
        for (Room room : rooms) {
            RoomSubject roomSubject = room.getRoomSubject(rsId);
            if(roomSubject != null)
                return roomSubject;
        }
        return null;
    }


    public void draw(SpriteBatch batch, float delta) {
        currentRoom.draw(batch);


        for (Door[] door : doors) {
            for (Door aDoor : door) {
                if (aDoor != null) {
                    aDoor.draw(batch, delta);
                }
            }
        }

        for (Ghost ghost : ghostList) {
            if (ghost.getRoom() == currentRoom)
                ghost.draw(batch);
        }
        hero.draw(batch);
    }

    public Room getRoom(int id) {
        return roomsMap.get(id);
    }

    public Door getDoor(String id) {
        return doorsMap.get(id);
    }

    public Collection<Door> getDoors() {
        return doorsMap.values();
    }

    public Door getDoor(int x, int y) {
        for (Door[] door : doors) {
            for (Door aDoor : door) {
                if (aDoor != null) {
                    if (aDoor.touch(x, y))
                        return aDoor;
                }
            }
        }
        return null;
    }

    public Hero getHero() {
        return hero;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(final Room currentRoom, Door door) {
        for (final Ghost ghost : ghostList) {
            if (ghost.isBad() && ghost.getState() == Ghost.State.AGR && House.this.currentRoom == ghost.getRoom()) {
                final BadGhost bg = (BadGhost) ghost;
                final Door finalD = door;
                float newX = 0;
                if (door instanceof CentralDoor) {
                    newX = door.getX() + door.getWidth() / 2f;
                } else {
                    if (door.getType().equals(Door.DoorType.TO)) {
                        newX = door.getX() + door.getWidth();
                    } else {
                        newX = door.getX();
                    }
                }
                bg.go(newX, new MyAnimation.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        bg.setRoom(currentRoom);
                        if (!(finalD instanceof CentralDoor)) {
                            if (finalD.getType().equals(Door.DoorType.FROM)) {
                                bg.setX(finalD.getX() - finalD.getWidth());
                            } else if (finalD.getType().equals(Door.DoorType.TO)) {
                                bg.setX(finalD.getX() + finalD.getWidth());
                            }
                        }
                        if(ghost.getRoom() == House.this.currentRoom && hero.isVisible()) {
                            bg.turnToHero(hero);
                            bg.setWalkingRight(bg.getX() < bg.getHouse().getHero().getX());
                        } else {
                            bg.setAgring(false);
                            bg.returnToStartPoint();
                        }
                    }
                });
            }
        }
        this.currentRoom = currentRoom;
        currentRoom.update(0);


        if (DebugOptions.COLLECT_SUBJECTS_WHEN_COME_TO_ROOM) {
            for (RoomSubject roomSubject : currentRoom.roomSubjects) {
                if (roomSubject != null && roomSubject.getSubject() != null)
                    for (Subject subject : roomSubject.getSubject()) {
                        hero.getInventar().add(subject);
                    }
            }
        }

    }

    public void dispose() {
        hero.dispose();
        for (Room room : rooms) {
            room.dispose();
        }
    }

    public Ghost getGhostClick(int x, int y) {
        for (Ghost ghost : ghostList) {
            if (ghost.getRoom() == currentRoom && ghost.isClick(x, y)) {
                return ghost;
            }
        }
        return null;
    }

    public Ghost getGhost(int ghostId) {
        for (Ghost ghost : ghostList) {
            if (ghost.getId() == ghostId)
                return ghost;
        }
        return null;
    }

    public List<Ghost> getGhostsInRoom(int roomId) {
        List<Ghost> list = new ArrayList<>();
        for (Ghost ghost : ghostList) {
            if (ghost.getRoom().getId() == roomId) {
                list.add(ghost);
            }
        }
        return list;
    }

    public Subject findSubject(int id) {
        for (Room room : rooms) {
            for (RoomSubject rs : room.roomSubjects) {
                if (rs.getSubject() != null) {
                    for (Subject subject : rs.getSubject()) {
                        if (subject.getId() == id) {
                            return subject;
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<Ghost> getGhosts() {
        return ghostList;
    }

    public void exit() {
        myGame.goToMenuScreen();
    }

    public void removeFromSubjectToInventar(int[] s_ids) {
        for (Room room : roomsMap.values()) {
            for (RoomSubject rs : room.roomSubjects) {
                if(rs.getSubject() != null) {
                    for (int j = 0; j < rs.getSubject().size(); j++) {
                        Subject subject = rs.getSubject().get(j);
                        for (int s_id : s_ids) {
                            if (s_id == subject.getId()) {
                                rs.getSubject().remove(j);
                                j--;
                                subject.setVisible(true);
                                hero.getInventar().add(subject);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void putSubjectToRoomSubject(int rsId, Subject s)  {
        for (Room room : rooms) {
            for (RoomSubject rs : room.roomSubjects) {
                if(rs.getId() == rsId) {
                    rs.getSubject().add(s);
                    break;
                }
            }
        }
    }

    public void replaceSubject(Subject s, int toRsId) {
        for (Room room : rooms) {
            for (RoomSubject rs : room.roomSubjects) {
                if (rs.getSubject() != null) {
                    for (Subject subject : rs.getSubject()) {
                        if (subject.getId() == s.getId()) {
                            if(rs.getId() != toRsId) {
                                rs.getSubject().remove(subject);
                                putSubjectToRoomSubject(toRsId, s);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public List<Subject> getSubjects() {
        List<Subject> subjects = new ArrayList<>();
        for (Room room : rooms) {
            for (RoomSubject roomSubject : room.roomSubjects) {
                for (Subject subject : roomSubject.getSubject()) {
                    subjects.add(subject);
                }
            }
        }
        return subjects;
    }

    public void endGame() {
        endGame.onEnd();
    }

    public static class ChangeLogItem {
        public int roomId;
        public int rsId;
        public List<Integer> list;

        public ChangeLogItem(int roomId, int rsId) {
            this.roomId = roomId;
            this.rsId = rsId;
            list = new ArrayList<>();
        }
    }
}