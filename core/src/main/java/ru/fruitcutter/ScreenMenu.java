package ru.fruitcutter;

import static ru.fruitcutter.Main.isSoundOn;
import static ru.fruitcutter.ScreenGame.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

public class ScreenMenu implements Screen {
    private SpriteBatch batch;
    private SpriteBatch batchTxt;
    private OrthographicCamera camera;
    private OrthographicCamera cameraTxt;
    private Vector3 touch;
    private BitmapFont font;
    private Main main;

    Texture imgBackGround;

    FruitButton btnPlay;
    FruitButton btnAbout;
    FruitButton btnExit;
    FruitButton btnSound;
    String soundText;

    public ScreenMenu(Main main) {
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;
        batchTxt = main.batch;
        cameraTxt = main.cameraTxt;
        if(isSoundOn){
            soundText = "Sound Off";
        } else {
            soundText = "Sound  On";
        }
        imgBackGround = new Texture("menubackground.jpg");
        btnPlay = new FruitButton("Play", font, 700, 600);
        btnAbout = new FruitButton("About", font, 680, 500);
        btnExit = new FruitButton("Exit", font, 707, 300);
        btnSound = new FruitButton(soundText, font, 615, 400);
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
            if(btnPlay.buttonHit(touch)){
                main.setScreen(main.screenGame);
            }
            if(btnSound.buttonHit(touch)){
                isSoundOn = !isSoundOn;
            }
            if(btnAbout.buttonHit(touch)){
                main.setScreen(main.screenAbout);
            }
            if(btnExit.buttonHit(touch)){
                Gdx.app.exit();
            }
        }
        // отрисовка
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackGround, 0, 0, W_WIDTH, W_HEIGHT);
        batch.end();
        batchTxt.setProjectionMatrix(cameraTxt.combined);
        batchTxt.begin();
        btnPlay.font.draw(batchTxt, btnPlay.text, btnPlay.x, btnPlay.y);
        btnAbout.font.draw(batchTxt, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(batchTxt, btnExit.text, btnExit.x, btnExit.y);
        btnSound.font.draw(batchTxt, btnSound.text, btnSound.x, btnSound.y);
        font.draw(batchTxt,"Your best score: " + menuScore, 10, 50, W_WIDTH * 100 - 10, Align.left, true);
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
