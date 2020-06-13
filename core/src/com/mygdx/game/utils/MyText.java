package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class MyText {

    private final GlyphLayout glyphLayout = new GlyphLayout();

    private String message;
    private float x, y;
    private BitmapFont font;

    public MyText(String message) {
        font = FontUtils.generateFont();
        setMessage(message);
    }

    public MyText(BitmapFont font, String message, float height) {
        this.font = font;
        setMessage(message);
        setHeight(height);
    }

    public MyText(String message, float height) {
        font = FontUtils.generateFont();
        setMessage(message);
        setHeight(height);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        glyphLayout.setText(font, message);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return glyphLayout.width;
    }

    public void setWidth(float width) {
        font.getData().setScale(1);
        glyphLayout.setText(font, message);
        font.getData().setScale(width / glyphLayout.width);
        glyphLayout.setText(font, message);
    }

    public float getHeight() {
        return glyphLayout.height;
    }

    public void setHeight(float height) {
        font.getData().setScale(1);
        glyphLayout.setText(font, message);
        font.getData().setScale(height / glyphLayout.height);
        glyphLayout.setText(font, message);

    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, message, x, y);
    }

    public void addEnters(float width, float height) {
        float per = font.getScaleX();
        font.getData().setScale(per);
        glyphLayout.setText(font, message);
        int id = 0;
        do {
            if (width < glyphLayout.width || glyphLayout.height > height) {
                String[] words = message.split(" ");
                String subMessage = "";
                boolean badHeight = false;
                for (String word : words) {
                    subMessage = subMessage.concat(word).concat(" ");
                    glyphLayout.setText(font, subMessage);

                    if (glyphLayout.width > width) {
                        subMessage = subMessage.substring(0, subMessage.length() - word.length() - 1).concat("\n").concat(word).concat(" ");
                        glyphLayout.setText(font, subMessage);
                    }
                    if (glyphLayout.height > height) {
                        badHeight = true;
                        font.getData().setScale(per);
                        per /= 1.2f;
                        break;
                    }
                }
                if (badHeight)
                    continue;
                setMessage(subMessage);
                id++;
                System.out.println(id + " " + message);
                System.out.println();
            }

            font.getData().setScale(per);
            glyphLayout.setText(font, message);
            per /= 2f;
        } while (glyphLayout.height > height);
        System.out.println();
    }

    public boolean isClicked(float x, float y) {
        return this.x < x && this.x + getWidth() > x && this.y < y && this.y + getHeight() > y;
    }

    public void central() {
        String str[] = message.split("\n");
        int maxL = -1;
        for (String aStr1 : str) {
            if (aStr1.length() > maxL)
                maxL = aStr1.length();
        }
        for (int i = 0; i < str.length; i++) {
            if (str[i].length() != maxL) {
                int len = str[i].length();
                int spaces = (maxL - len) / 2 + 1;
                for (int j = 0; j < spaces; j++) {
                    str[i] = " ".concat(str[i]);
                }
                for (int j = 0; j < spaces; j++) {
                    str[i] = str[i].concat(" ");
                }
            }
        }
        message = "";
        for (String aStr : str) {
            message = message.concat(aStr).concat("\n");
        }
        System.out.println();
    }

    public void dispose() {
        font.dispose();
    }
}
