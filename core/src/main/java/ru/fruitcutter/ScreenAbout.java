package ru.fruitcutter;

import static ru.fruitcutter.Main.*;
import static ru.fruitcutter.ScreenGame.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenAbout implements Screen {
    private SpriteBatch batch;
    private SpriteBatch batchTxt;
    private OrthographicCamera camera;
    private OrthographicCamera cameraTxt;
    private Vector3 touch;
    private BitmapFont font;
    private Main main;

    Texture imgBackGround;

    FruitButton btnBack, Text;
    private String text = "    В этой игре ты должен нажимать\n" +
        "  на фрукты и, тем самым, разрезать их.\n" +
        " Чтобы начать игру, нажми кнопку 'Play'\n" +
    "   Чтобы выключить или включить звук,\n"+
        "         нажми 'Sound' в меню";

    public ScreenAbout(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;
        batchTxt = main.batch;
        cameraTxt = main.cameraTxt;

        imgBackGround = new Texture("background.jpg");

        btnBack = new FruitButton("Back", font, 800);
        Text = new FruitButton(text, font, 600);
    }


    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // касания
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);

            if(btnBack.buttonHit(touch)){
                main.setScreen(main.screenMenu);
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, W_WIDTH, W_HEIGHT);
        batch.end();
        batchTxt.setProjectionMatrix(cameraTxt.combined);
        batchTxt.begin();
        Text.font.draw(batch, Text.text,Text.x, Text.y);
        btnBack.font.draw(batch, btnBack.text, btnBack.x, btnBack.y);
        batchTxt.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
