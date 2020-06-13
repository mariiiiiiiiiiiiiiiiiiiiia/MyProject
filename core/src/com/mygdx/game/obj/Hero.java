package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Const;
import com.mygdx.game.MusicManager;
import com.mygdx.game.MyAnimation;
import com.mygdx.game.TextureManager;
import com.mygdx.game.obj.panels.BottomPanel;
import com.mygdx.game.shit.BigListener;
import com.mygdx.game.shit.level.LevelManager;
import com.mygdx.game.utils.AnimationUtils;
import com.mygdx.game.utils.FillType;
import com.mygdx.game.utils.PhraseUtils;

import org.json.JSONObject;

public class Hero extends StatableHouseObject<Hero.State> {

    private State currentState;
    private State previousState;
    private Animation heroWalk;
    private boolean turnRight;
    private boolean visible;
    private Inventar inventar;
    private MyAnimation walkAnimation;
    private float stateTimer;
    public Animation faceAnimationh;

    public Hero(House house, JSONObject object) {

        super(house, object);
        JSONObject hero = object.optJSONObject(FillType.HERO);

        String heroStandingImage = hero.optString(FillType.HERO_STANDING_IMAGE);
        TextureRegion standRegion = TextureManager.getInstance().region(heroStandingImage);
        heroWalk = AnimationUtils.createAnimation(hero.optJSONObject(FillType.HERO_WALK_ANIMATION));
        walkAnimation = new MyAnimation();

        float x = (float) hero.optDouble(FillType.X);
        setBounds(x * Const.W, Const.HERO_Y, Const.HERO_W, Const.HERO_H);

        visible = true;
        turnRight = true;

        currentState = State.STAND;
        previousState = State.STAND;
        stateTimer = 0;
        if(hero.has(FillType.ANIMATION_PARAMS))
            faceAnimationh = AnimationUtils.createAnimation(hero.optJSONObject(FillType.ANIMATION_PARAMS));
        inventar = new Inventar(this);

        PhraseUtils.load(hero.optJSONObject(FillType.PHRASES));
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Inventar getInventar() {
        return inventar;
    }

    @Override
    public State getState() {
        if (walkAnimation.isEnabled())
            return State.WALK;
        return State.STAND;
    }

    @Override
    public void draw(Batch batch) {
        if (visible)
            super.draw(batch);
    }

    @Override
    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region = null;
        switch (currentState) {
            case STAND:
                region = (TextureRegion) heroWalk.getKeyFrame(0);
                break;
            case WALK:
                region = (TextureRegion) heroWalk.getKeyFrame(stateTimer, true);
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

    public void go(float x) {
        go(x, null);
    }

    public void go(RoomSubject subject, final MyAnimation.OnAnimationEndListener onAnimationEndListener) {
        go(subject.getX() + subject.getWidth() / 2f, onAnimationEndListener);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (walkAnimation.isEnabled()) {
            walkAnimation.onAnimatorTick(dt);
        }
    }

    public void go(float x, final MyAnimation.OnAnimationEndListener onAnimationEndListener) {
        float dist = x - getX() - getWidth() / 2f;
        if (Math.abs(dist) < 0.1f * Const.W) {
            if (onAnimationEndListener != null) {
                onAnimationEndListener.onAnimationEnd();
            }
            return;
        }
        final float k = dist < 0 ? -1 : 1;
        if (k < 0 && turnRight)
            turn(false);
        else if (k > 0 && !turnRight)
            turn(true);
        final float velX = 5.5f * Const.W;

        walkAnimation.interrupt();
        walkAnimation.setMaxTime(Math.abs(dist) / velX);
        walkAnimation.setOnAnimationEndListener(new MyAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                MusicManager.stopSound();
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
        MusicManager.playSound(MusicManager.SoundType.STEPS, false);
    }

    public void turn(boolean turnR) {
        if (turnRight != turnR) {
            turnRight = turnR;
            setFlip(turnR, false);
        }
    }

    public boolean isWalking() {
        return getState().equals(State.WALK);
    }

    public void stopWalking() {
        if (isWalking()) {
            walkAnimation.interrupt();
            MusicManager.stopSound();
        }
    }

    public void hide(RoomSubject subject) {
        visible = false;
        BigListener.getInstance().hideRoomSubject(subject);
        LevelManager.getInstance().hideRoomSubject(subject);
    }


    public void search(final RoomSubject subject) {
        go(subject, new MyAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                int count = 0;
                if (subject.getSubject().size() != 0) {
                    for (Subject sub : subject.getSubject()) {
                        if (sub.isVisible()) {
                            inventar.add(sub);
                            BigListener.getInstance().findSubject(sub);
                            LevelManager.getInstance().findSubject(sub);
                            count++;
                        }
                    }
                    subject.clearSubjects();
                    House.ChangeLogItem item = new House.ChangeLogItem(subject.getRoomId(), subject.getId());
                    for (Subject sub : subject.getSubject()) {
                        item.list.add(sub.getId());
                    }

                    getHouse().addChangeLog(item);
                }
                BigListener.getInstance().searchRoomSubject(subject);
                LevelManager.getInstance().searchRoomSubject(subject);
                if (count != 0) {
                    MusicManager.playSound(MusicManager.SoundType.SUCSESS, false);
                    if(subject.getSpecialSearchDescription() == null) {
                        BottomPanel.getInstance().showPanel(PhraseUtils.getRandomPhrase(PhraseUtils.PhraseType.FOUND));
                    } else {
                        BottomPanel.getInstance().showPanel(subject.getSpecialSearchDescription());}
                } else {
                    if(subject.getSpecialSearchDescription() == null) {
                    BottomPanel.getInstance().showPanel(PhraseUtils.getRandomPhrase(PhraseUtils.PhraseType.NOT_FOUND));
                }else{
                        BottomPanel.getInstance().showPanel(subject.getSpecialSearchDescription());}
                    }
            }
        });
    }

    enum State {
        STAND, WALK
    }

}
