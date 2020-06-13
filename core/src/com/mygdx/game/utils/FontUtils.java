package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontUtils {

    // Font names
    public static final String FONT_NAME = "fonts/10323.ttf";


    // Russian cyrillic characters
    public static final String RUSSIAN_CHARACTERS = "ABCDEFGHIJKLMNOPQSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ"
            + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
            + "1234567890.,:;_¡!¿?\"'+-*/()[]={}";
    private static final GlyphLayout glyphLayout = new GlyphLayout();

    private static BitmapFont generateFont(String fontName, FreeTypeFontParameter params) {

        // Generate font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        BitmapFont newFont = generator.generateFont(params);

        // Dispose resources
        generator.dispose();

        return newFont;
    }

    private static FreeTypeFontParameter parameters() {
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.characters = RUSSIAN_CHARACTERS;
        parameter.size = 124;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 0;
        parameter.borderColor = Color.BLACK;
        return parameter;
    }

    public static BitmapFont generateFont() {
        return generateFont(FONT_NAME, parameters());
    }

    public static BitmapFont generateFont(int size) {
        FreeTypeFontParameter p = parameters();
        p.size = size;
        return generateFont(FONT_NAME, p);
    }

    public static synchronized BitmapFont generateFontWithWidth(int width, String text) {
        BitmapFont font = generateFont(FONT_NAME, parameters());
        glyphLayout.setText(font, text);
        float fw = glyphLayout.width;
        float scale = fw / width;
        font.getData().setScale(scale);
        return font;
    }

}