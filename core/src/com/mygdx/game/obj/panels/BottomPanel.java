package com.mygdx.game.obj.panels;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.Const;
import com.mygdx.game.MyAnimation;
import com.mygdx.game.TextureManager;
import com.mygdx.game.obj.Hero;
import com.mygdx.game.obj.RoomSubject;

import java.util.Random;

public class BottomPanel extends Panel {

    private static BottomPanel instance;
    private Sprite hideSprite;
    private Sprite searchSprite;
    private RoomSubject subject;
    private BottomPanel() {
        super();
        searchSprite = TextureManager.getInstance().sprite(Const.BUTTON_SEARCH_IMAGE_FILEPATH);
        float w = 1.313f * Const.W;
        float h = 1.258f * Const.H;
        searchSprite.setBounds(Const.SCREEN_WIDTH - Const.W * 0.4f - w, PANEL_HEIGHT / 2f - h / 2f, w, h);

        hideSprite = TextureManager.getInstance().sprite(Const.BUTTON_HIDE_IMAGE_FILEPATH);
        w = 1.313f * Const.W;
        h = 1.258f * Const.H;
        hideSprite.setBounds(Const.W * 0.4f, PANEL_HEIGHT / 2f - h / 2f, w, h);

        maxTextWidth = Const.SCREEN_WIDTH - 2 * Const.W * 0.4f - 2 * w;
    }

    public static BottomPanel getInstance() {
        if (instance == null)
            instance = new BottomPanel();
        return instance;
    }

    @Override
    public void setPanelVisible(boolean panelVisible) {
        super.setPanelVisible(panelVisible);
        if (!panelVisible) {
            subject = null;
        }
    }

    @Override
    public void onClick(float x, float y) {
        if (y > 0 && y < PANEL_HEIGHT) {
            if (subject != null) {
                if (subject.canHide() && hideSprite.getBoundingRectangle().contains(x, y)) {
                    final Hero hero = subject.getHouse().getHero();
                    final RoomSubject rs = subject;
                    hero.go(subject, new MyAnimation.OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd() {
                            hero.hide(rs);
                        }
                    });
                }
                if (subject.canSearch() && searchSprite.getBoundingRectangle().contains(x, y)) {
                    subject.getHouse().getHero().search(subject);
                }
            }
        }
    }

    public void showPanel(RoomSubject roomSubject) {
        this.subject = roomSubject;

        setPanelVisible(true);
        String panelText;
        if (roomSubject.getSpecialDescription() == null) {
            Random rand = new Random();
            int descriptionIndex = rand.nextInt(roomSubject.getDescriptionsCount());
            panelText = roomSubject.getDescription(descriptionIndex);
        } else
            panelText = roomSubject.getSpecialDescription();

        changePanelText(panelText);
    }
    public OnHidePanelListener onHidePanelListener;

    public void showPanel(String message) {
        showPanel(message, null);
    }

    public void showPanel(String message, OnHidePanelListener onHidePanelListener) {
        this.onHidePanelListener = onHidePanelListener;
        subject = null;
        setPanelVisible(true);

        changePanelText(message);
    }

    public interface OnHidePanelListener {
        void onPanelHide();
    }

    @Override
    public void draw(ShapeRenderer renderer, SpriteBatch batch) {
        text.setPosition(Const.SCREEN_WIDTH / 2f - text.getWidth() / 2f, PANEL_HEIGHT / 2f + text.getHeight() / 2f);
        super.draw(renderer, batch);

        batch.begin();
        if (subject != null) {
            if (subject.canHide()) {
                hideSprite.draw(batch);
            }

            if (subject.canSearch()) {
                searchSprite.draw(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        instance = null;
    }
}
