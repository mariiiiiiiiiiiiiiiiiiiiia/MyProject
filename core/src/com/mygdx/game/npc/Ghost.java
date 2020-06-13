package com.mygdx.game.npc;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Const;
import com.mygdx.game.MyAnimation;
import com.mygdx.game.TextureManager;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.Room;
import com.mygdx.game.obj.StatableHouseObject;
import com.mygdx.game.utils.AnimationUtils;
import com.mygdx.game.utils.FillType;
import org.json.JSONObject;


public class Ghost extends StatableHouseObject<Ghost.State> {


    public Animation faceAnimation;
    protected TextureRegion standImage;
    protected Room startRoom;
    protected float startX;
    protected State currentState;
    protected State previousState;
    protected boolean turnRight;
    protected float stateTimer;
    private int id;
    private String name;
    private boolean isTalking;
    private boolean agring;
    private Room room;
    private String faceImagePath;
    private boolean isBad;
    protected MyAnimation walkAnimation;

    public Ghost(House house, JSONObject object) {
        super(house, object);
        id = object.optInt(FillType.ID);
        name = object.optString(FillType.NAME);
        isBad = object.optBoolean(FillType.IS_BAD, false);

        float w = (float) object.optDouble(FillType.WIDTH);
        float h = (float) object.optDouble(FillType.HEIGHT);
        JSONObject startPosition = object.optJSONObject(FillType.START_POSITION);
        float start_x = (float) startPosition.optDouble(FillType.X);
        float start_y = (float) startPosition.optDouble(FillType.Y);
        String background = object.optString(FillType.BACKGROUND);

        standImage = TextureManager.getInstance().region(background);
        setBounds(start_x * Const.W, start_y * Const.H, w * Const.W, h * Const.H);

        room = house.getRoom(startPosition.optInt(FillType.ROOM));
        startRoom = room;
        startX = getX();
        faceImagePath = object.optString(FillType.FACE_IMAGE);

        stateTimer = 0;
        turnRight = true;
        agring = false;

        currentState = State.STAND;
        previousState = State.STAND;

        if (object.has(FillType.ANIMATION_PARAMS))
            faceAnimation = AnimationUtils.createAnimation(object.optJSONObject("animation"));
        walkAnimation = new MyAnimation();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFaceImagePath() {
        return faceImagePath;
    }

    public void turnToHero(Hero hero) {
        if (getX() > hero.getX() && !isTurnRight())
            turn(true);
        else if (getX() < hero.getX() && isTurnRight())
            turn(false);
    }

    public void turnToGo(float x) {
        if (getX() > x && !isTurnRight())
            turn(true);
        else if (getX() < x && isTurnRight())
            turn(false);
    }


    public boolean isAgring() {
        return agring;
    }

    @Override
    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region = null;
        switch (currentState) {
            case STAND:
                region = standImage;
                break;
            case TALK:
                region = standImage;
                break;
            case WALK:
                region = standImage;
                break;
            case AGR:
                region = standImage;
                break;
        }

        if (!turnRight && region.isFlipX()) {
            region.flip(true, false);
        } else if (turnRight && !region.isFlipX()) {
            region.flip(true, false);
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public void returnToStartPoint() {
        setRoom(startRoom);
        setX(startX);
    }

    public void setAgring(boolean agring) {
        this.agring = agring;
    }

    public boolean isBad() {
        return isBad;
    }

    public boolean isTurnRight() {
        return turnRight;
    }

    public void turn(boolean turnR) {
        if (turnRight != turnR) {
            turnRight = turnR;
            setFlip(turnR, false);
        }
    }

    public boolean isTalking() {
        return isTalking;
    }

    public void setTalking(boolean talking) {
        isTalking = talking;
    }

    @Override
    public State getState() {
        if (isTalking())
            return State.TALK;
        if (agring)
            return State.AGR;
        if (walkAnimation != null && walkAnimation.isEnabled())
            return State.WALK;

        return State.STAND;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void draw(SpriteBatch batch) {
        if (room == getHouse().getCurrentRoom())
            super.draw(batch);
    }

    public boolean isClick(int x, int y) {
        return getBoundingRectangle().contains(x, y);
    }

    public void changeStartPoint(int roomId, float x) {
        startRoom = getHouse().getRoom(roomId);
        startX = x * Const.W;
    }

    public void go(float x, final MyAnimation.OnAnimationEndListener onAnimationEndListener) {
        float dist = x - getX() - getWidth() / 2f;
        if (Math.abs(dist) < 0.1f * Const.W)
            return;
        final float k = dist < 0 ? -1 : 1;
        if (k < 0 && turnRight)
            turn(false);
        else if (k > 0 && !turnRight)
            turn(true);
        final float velX = 7.2f * Const.W;

        turnToGo(x);
        walkAnimation.interrupt();
        walkAnimation.setMaxTime(Math.abs(dist) / velX);
        walkAnimation.setOnAnimationEndListener(new MyAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                if (onAnimationEndListener != null) {
                    onAnimationEndListener.onAnimationEnd();
                }
                stateTimer = 0f;
            }
        });
        walkAnimation.setOnTick(new MyAnimation.OnTick() {
            @Override
            public void onTick(float delta) {
                translateX(delta * velX * k);
            }
        });
        walkAnimation.start();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (walkAnimation.isEnabled()) {
            walkAnimation.onAnimatorTick(dt);
        }
    }

    public enum State {
        STAND, TALK, AGR, WALK
    }
}
