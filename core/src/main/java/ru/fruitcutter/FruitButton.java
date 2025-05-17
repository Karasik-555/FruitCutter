package ru.fruitcutter;


import static ru.fruitcutter.ScreenGame.*;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;

public class FruitButton {
    String text;
    BitmapFont font;
    float x, y;
    Vector3 touchPosition;
    public boolean buttonTouched;

    float width, height;

    public FruitButton(String text, BitmapFont font, float x, float y) {
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    public FruitButton(String text, BitmapFont font, float y) {
        this.text = text;
        this.font = font;
        this.y = y;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
        x = W_WIDTH*100/2 - width/2;
    }

    public void setFont(BitmapFont font){
        this.font = font;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
        height = glyphLayout.height;
    }

    public void setText(String text){
        this.text = text;
        GlyphLayout glyphLayout = new GlyphLayout(font, text);
        width = glyphLayout.width;
    }
    boolean buttonHit(Vector3 t){
        return x<t.x && t.x<x+width && y>t.y && t.y>y-height;
        }
    }

