package ru.fruitcutter;

import static ru.fruitcutter.ScreenGame.*;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static boolean isSoundOn = true;
    public SpriteBatch batch;
    public SpriteBatch batchTxt;
    public OrthographicCamera camera;
    public OrthographicCamera cameraTxt;
    public Vector3 touch;
    public BitmapFont font;

    ScreenMenu screenMenu;
    ScreenGame screenGame;
    ScreenAbout screenAbout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        cameraTxt = new OrthographicCamera();
        batchTxt = new SpriteBatch();
        camera.setToOrtho(false, W_WIDTH, W_HEIGHT);
        cameraTxt.setToOrtho(false, W_WIDTH*100, W_HEIGHT*100);
        touch = new Vector3();
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        screenMenu = new ScreenMenu(this);
        screenGame = new ScreenGame(this);
        screenAbout = new ScreenAbout(this);
        setScreen(screenAbout);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();

    }
}
