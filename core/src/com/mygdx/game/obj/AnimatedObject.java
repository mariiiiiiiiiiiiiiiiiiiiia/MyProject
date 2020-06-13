package com.mygdx.game.obj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimatedObject {

    private Animation animation;

    // Переменная для отслеживания прошедшего времени анимации
    private float stateTime;


    public AnimatedObject(String filepath, int w, int h, float frameDuration, Animation.PlayMode playMode) {

        Texture sheet = new Texture(filepath);

        // Метод split нужен чтобы разделить нашу текстуру на
        // подтекстуры в виде столбцов и строк матрицы размером
        //  (walkSheet.getWidth() / w, walkSheet.getHeight() / h)
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / w,
                sheet.getHeight() / h);

        // Переносим нашу матрицу текстур из 2D в 1D, начиная с верхнего левого угла
        // и двигаясь с лева на право
        TextureRegion[] frames = new TextureRegion[w * h];
        int index = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        this.animation = new Animation(frameDuration, new Array<>(frames),
                playMode);

        stateTime = 0f;
    }

    public void draw(float x, float y, float w, float h, boolean flipX, boolean flipY, SpriteBatch batch, float delta) {
        stateTime += delta;

        TextureRegion currentFrame = (TextureRegion) animation.getKeyFrame(stateTime, true);
        currentFrame.flip(flipX, flipY);
        batch.draw(currentFrame, x, y, w, h); // Рисуем текущий кадр
        currentFrame.flip(flipX, flipY);
    }

    public void draw(float x, float y, float w, float h, SpriteBatch batch, float delta) {
        draw(x, y, w, h, false, false, batch, delta);
    }

    public void stop() {
        stateTime = 0f;
    }
}
