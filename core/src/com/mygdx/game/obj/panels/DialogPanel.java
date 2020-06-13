package com.mygdx.game.obj.panels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Const;
import com.mygdx.game.npc.cutscene.CutsceneManager;
import com.mygdx.game.shit.Dialog;
import com.mygdx.game.MusicManager;
import com.mygdx.game.TextureManager;
import com.mygdx.game.npc.BadGhost;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.House;
import com.mygdx.game.shit.BigListener;
import com.mygdx.game.shit.level.LevelManager;
import com.mygdx.game.utils.FontUtils;
import com.mygdx.game.utils.MyText;
import com.badlogic.gdx.graphics.g2d.Animation;


import java.util.List;



public class DialogPanel extends Panel {
    public interface OnEndDialogListener {
        void oEndDialog();
    }

    private static DialogPanel instance;
    private final float faceWidth = Const.W * 2.418f;
    private final float faceOffsetX = 0.1f * faceWidth;
    private final float faceOffsetY = 0;
    public float stateTime;
    private Dialog.DialogText dialogText;
    private Sprite lelaSprite;
    private Sprite oleandrSprite;
    private Ghost currentGhost;
    private Hero hero;
    private Dialog dialog;
    private boolean isOptionShows;
    private ChoosePanel choosePanel;
    private DialogPanel() {
        super();

        lelaSprite = TextureManager.getInstance().sprite(Const.LELA_FACE_IMAGE_FILEPATH);
        float faceHeight = Const.H * 2.32f;
        lelaSprite.setBounds(faceOffsetX, faceOffsetY, faceWidth, faceHeight);


        maxTextWidth = Const.SCREEN_WIDTH - 3 * faceOffsetX - faceWidth;
        isOptionShows = false;
        choosePanel = new ChoosePanel();
        stateTime = 0f;
    }

    public static DialogPanel getInstance() {
        if (instance == null)
            instance = new DialogPanel();
        return instance;
    }

    private OnEndDialogListener listener;

    public void startDialog(Ghost ghost) {
        startDialog(ghost, null);
    }

    public void startDialog(Ghost ghost, OnEndDialogListener listener) {
        this.listener = listener;

        oleandrSprite = TextureManager.getInstance().sprite(ghost.getFaceImagePath());
        oleandrSprite.setBounds(Const.SCREEN_WIDTH - 2.362f * Const.W - faceOffsetX, faceOffsetY, 2.362f * Const.W, 2.573f * Const.H);

        Sprite ivaSprite = TextureManager.getInstance().sprite(ghost.getFaceImagePath());
        ivaSprite.setBounds(Const.SCREEN_WIDTH - 2.362f * Const.W - faceOffsetX, faceOffsetY, 2.362f * Const.W, 2.573f * Const.H);

        House house = ghost.getHouse();
        hero = house.getHero();
        ghost.turnToHero(hero);

        this.currentGhost = ghost;
        this.currentGhost.setTalking(true);

        setPanelVisible(true);

//        Quest quest = Quest.getCurrentQuest();

        dialog = LevelManager.getInstance().getDialogByGhost(ghost.getId());//quest.getDialogByGhost(ghost.getId());
        dialog.setFirstGroup();
        dialogText = dialog.currentText();

        changePanelText(dialogText.getText());
        if (ghost.getId() == 2) {
            MusicManager.playVoiceSound(MusicManager.VoiceSoundType.IVA, false);
        } else {
            MusicManager.playVoiceSound(MusicManager.VoiceSoundType.OLEANDR, false);
        }
    }

    @Override
    public void draw(ShapeRenderer renderer, SpriteBatch batch) {
        if (!isOptionShows) {
            drawPanel(renderer);
            float dialogTextY = PANEL_HEIGHT / 2f + text.getHeight() / 2f;
            if (dialogText instanceof Dialog.GhostDialogText) {
                float x = faceOffsetX + maxTextWidth / 2f - text.getWidth() / 2f;
                if (currentGhost.faceAnimation == null) {
                    batch.begin();
                    oleandrSprite.draw(batch);
                    batch.end();
                } else {
                    TextureRegion keyFrame = (TextureRegion) currentGhost.faceAnimation.getKeyFrame(stateTime);
                    Rectangle r = oleandrSprite.getBoundingRectangle();
                    batch.begin();
                    batch.draw(keyFrame, r.getX(), r.getY(), r.getWidth(), r.getHeight());
                    batch.end();
                }
                text.setPosition(x, dialogTextY);
            } else {
                TextureRegion keyFrame = (TextureRegion) hero.faceAnimationh.getKeyFrame(stateTime);
                Rectangle r = lelaSprite.getBoundingRectangle();
                batch.begin();
                batch.draw(keyFrame, r.getX(), r.getY(), r.getWidth(), r.getHeight());
                batch.end();
                float x = 2 * faceOffsetX + faceWidth + maxTextWidth / 2f - text.getWidth() / 2f;
                text.setPosition(x, dialogTextY);
            }
            drawText(batch);
        } else {
            choosePanel.draw(renderer, batch);
        }
        stateTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void onClick(float x, float y) {
        if (isOptionShows) {
            int index = choosePanel.onClick(x, y);
            if (index != -1) {
                dialog.choose(index);
                dialogText = dialog.currentText();
                changePanelText(dialogText.getText());
                isOptionShows = false;
                stateTime = 0f;
            }
            return;
        }

        if (dialog.hasNextText()) {
            Dialog.DialogText dt = dialog.nextText();
            if ((dialogText instanceof Dialog.GhostDialogText && !(dt instanceof Dialog.GhostDialogText)) ||
                    !(dialogText instanceof Dialog.GhostDialogText) && dt instanceof Dialog.GhostDialogText) {
                stateTime = 0f;
            }
            this.dialogText = dt;
            changePanelText(this.dialogText.getText());
        } else {
            stateTime = 0f;
            BigListener.getInstance().talkDialogGroup(dialog.currentGroup());
            LevelManager.getInstance().talkDialogGroup(dialog.currentGroup());
            if (dialog.isCurrentGroupWithOptions()) {
                Dialog.DialogGroupWithOptions dwo = (Dialog.DialogGroupWithOptions) dialog.currentGroup();
                List<Dialog.DOption> options = dwo.getOptionsByStep();
                    isOptionShows = true;
                    for (int i = 0; i < options.size(); i++) {
                    System.out.println(i + ") " + options.get(i).getText());
                }
                choosePanel.setText(options);
            } else {
                dialog.setFirstGroup();
                setPanelVisible(false);
                if(listener != null) {
                    listener.oEndDialog();
                }
                currentGhost.setTalking(false);

                if (currentGhost.isBad() && !CutsceneManager.getInstance().isSomeCutscenePlaying()) {
                    BadGhost bg = (BadGhost) currentGhost;
                    bg.setWalkingRight(bg.getX() < bg.getHouse().getHero().getX());
                    currentGhost.setAgring(true);
                }
            }
        }
    }

    private class ChoosePanel {

        private MyText text[];
        private int currentCount;

        private ChoosePanel() {
            text = new MyText[4];
            BitmapFont font = FontUtils.generateFont();
            for (int i = 0; i < text.length; i++) {
                text[i] = new MyText(font, "", PANEL_HEIGHT / 6.5f);//Const.H * 0.3f);
            }
            currentCount = 0;
        }

        public void setText(List<Dialog.DOption> options) {
            for (int i = 0; i < options.size(); i++) {
                text[i].setMessage(options.get(i).getText());
            }
            currentCount = options.size();
        }

        public int onClick(float x, float y) {
            if (y > 0 && y < PANEL_HEIGHT) {
                for (int i = 0; i < currentCount; i++) {
                    if (text[i].isClicked(x, y + text[i].getHeight())) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public void draw(ShapeRenderer renderer, SpriteBatch batch) {
            drawPanel(renderer);
            batch.begin();

            float h = text[0].getHeight();

            float mh = currentCount * h + (currentCount + 1) * h / 2f;

            float start_y = PANEL_HEIGHT / 2f - mh / 2f;
            for (int i = 0; i < currentCount; i++) {
                TextureRegion keyFrame = (TextureRegion) hero.faceAnimationh.getKeyFrame(stateTime);
                Rectangle r = lelaSprite.getBoundingRectangle();
                batch.draw(keyFrame, r.getX(), r.getY(), r.getWidth(), r.getHeight());
                text[i].setPosition(Const.SCREEN_WIDTH / 2f - text[i].getWidth() / 2f, start_y + i * (h + h / 2f) + 1.5f * h);//PANEL_HEIGHT - ((float)i / currentCount) * PANEL_HEIGHT);
                text[i].draw(batch);
            }
            batch.end();
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
