package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.npc.cutscene.CutsceneManager;
import com.mygdx.game.obj.CentralDoor;
import com.mygdx.game.obj.Door;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.Room;
import com.mygdx.game.obj.RoomSubject;
import com.mygdx.game.obj.panels.BottomPanel;
import com.mygdx.game.obj.panels.DialogPanel;
import com.mygdx.game.obj.panels.MonologPanel;
import com.mygdx.game.shit.BigListener;
import com.mygdx.game.shit.level.LevelManager;

import java.util.List;

public class InputListener implements InputProcessor {

    public   House house;
    static  House ho;
    private GameScreen gameScreen;

    public InputListener(GameScreen gameScreen, House house) {
        this.gameScreen = gameScreen;
        this.house = house;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK) {
            gameScreen.dispose();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        System.out.println("touchDown " + screenX + " " + screenY);
        final int realX = screenX;
        final int realY = Const.SCREEN_HEIGHT - screenY;

        final Hero hero = house.getHero();

        if (!hero.isVisible() && !CutsceneManager.getInstance().isSomeCutscenePlaying()) {
            hero.setVisible(true);
            return true;
        }
        if (gameScreen.menuBatchClick(realX, realY)) {
            return true;
        }

        System.out.println(realX + " " + realY + " " + Thread.currentThread().toString());
        if (BottomPanel.getInstance().isPanelVisible()) {
            BottomPanel.getInstance().onClick(realX, realY);
            BottomPanel.getInstance().setPanelVisible(false);
            if (BottomPanel.getInstance().onHidePanelListener != null)
                BottomPanel.getInstance().onHidePanelListener.onPanelHide();
            return true;
        }

        if (DialogPanel.getInstance().isPanelVisible()) {
            DialogPanel.getInstance().onClick(realX, realY);// click();
            return true;
        }

        if (MonologPanel.getInstance().isPanelVisible()) {
            MonologPanel.getInstance().onClick(realX, realY);// click();
            return true;
        }

        if(CutsceneManager.getInstance().isSomeCutscenePlaying())
            return true;

        Ghost ghost = house.getGhostClick(realX, realY);

        if (ghost != null) {
            if(ghost.isAgring())
                return true;

            if (hero.isWalking()) {
                hero.stopWalking();
                MusicManager.stopSound();
            }
            DialogPanel.getInstance().startDialog(ghost);
            return true;
        }

        Room room = house.getCurrentRoom();

        if (room.isFloorClick(realX, realY)) {
            if (( room.getId() == 14) &&(realX < 9.76f*Const.W)){
                BottomPanel.getInstance().showPanel("Я не пойду дальше! Там так темно...");
                return true;
            }
            float goX = realX;
            List<Ghost> ghosts = house.getGhostsInRoom(room.getId());
            for (Ghost g : ghosts) {
                if (realX > g.getX() && realX < g.getX() + g.getWidth()) {
                    if (hero.getX() < g.getX()) {
                        goX = g.getX() - hero.getWidth() / 2f;
                    } else {
                        goX = g.getX() + g.getWidth() + hero.getWidth() / 2f;
                    }
                } else {
                    Rectangle floor = room.getFloor();
                    float nx = goX + hero.getWidth() / 2f;
                    boolean il = (nx > g.getX() && nx < g.getX() + g.getWidth()) &&
                            (g.getX() - hero.getWidth() > floor.getX());

                    float nx2 = goX - hero.getWidth() / 2f;
                    boolean ir = (nx2 > g.getX() && nx2 < g.getX() + g.getWidth()) &&
                            (g.getX() + g.getWidth() + hero.getWidth() < floor.getX() + floor.getWidth());
                    if (il) {
                        goX = g.getX() - hero.getWidth() / 2f;
                    }
                    if (ir) {
                        goX = g.getX() + g.getWidth() + hero.getWidth() / 2f;
                    }
                }
            }

            if (goX != hero.getX() + hero.getWidth() / 2f) {
                hero.stopWalking();
                hero.go(goX);
            }
            return true;
        }

        RoomSubject roomSubject = room.getSubject(realX, realY);
        if ((roomSubject != null) && (roomSubject.isVisible())) {
            BigListener.getInstance().roomSubjectClick(roomSubject, realX, realY);
            LevelManager.getInstance().roomSubjectClick(roomSubject, realX, realY);
            if (roomSubject.isSng()) {
                MusicManager.playSound(roomSubject.getSong(), false);
            }

            BottomPanel.getInstance().showPanel(roomSubject);
            hero.stopWalking();
            return true;
        }

        final Door door = house.getDoor(realX, realY);

        if (door != null) {

            MyAnimation.OnAnimationEndListener end = new MyAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd() {
                    if (door.isClosed()) {
                        BottomPanel.OnHidePanelListener listener = new BottomPanel.OnHidePanelListener() {
                            @Override
                            public void onPanelHide() {
                                LevelManager.getInstance().doorClick(door, realX, realY);
                            }
                        };
                        if (door.getSpecialDescription() == null)
                            BottomPanel.getInstance().showPanel("Дверь не хочет пускать меня дальше!", listener);
                        else
                            BottomPanel.getInstance().showPanel(door.getSpecialDescription(), listener);
                        MusicManager.playSound(MusicManager.SoundType.DOORLOCK, false);
                        return;
                    }

                    Room whereWeWantToGo = door.getFrom();
                    if (door.getType().equals(Door.DoorType.FROM)) {
                        whereWeWantToGo = door.getTo();

                    }
                    house.setCurrentRoom(whereWeWantToGo, door);
                    if (!(door instanceof CentralDoor)) {
                        if (door.getType().equals(Door.DoorType.FROM)) {
                            hero.setX(door.getX() - door.getWidth());
                        } else if (door.getType().equals(Door.DoorType.TO)) {
                            hero.setX(door.getX() + door.getWidth());

                        }
                    }

                    BigListener.getInstance().comeInRoom(whereWeWantToGo);
                    LevelManager.getInstance().comeInRoom(whereWeWantToGo);

                    for (Ghost ghost : house.getGhosts()) {
                        if(ghost.isBad() &&  house.getCurrentRoom() == ghost.getRoom() && !ghost.isAgring()){
                            DialogPanel.getInstance().startDialog(ghost);
                            break;
                        }else{
                            ghost.turnToHero(hero);
                        }
                    }
                    MusicManager.playSound(MusicManager.SoundType.DOOR, false);

                }
            };

            if (door instanceof CentralDoor) {
                hero.go(door.getX() + door.getWidth() / 2f, end);
            } else {
                if (door.getType().equals(Door.DoorType.TO)) {
                    hero.go(door.getX() + door.getWidth(), end);
                } else {
                    hero.go(door.getX(), end);
                }
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
