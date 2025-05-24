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
public class ScreenMenu implements Screen {
    private SpriteBatch batch,batchTxt;
    private OrthographicCamera camera,cameraTxt;
    private Vector3 touch;
    private BitmapFont font;
    private Main main;
    Texture imgBackGround;
    Buttons btnPlay;
    Buttons btnAbout;
    Buttons btnExit;
    Buttons btnSound;
    public ScreenMenu(Main main) {
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        touch = main.touch;
        font = main.font;
        batchTxt = main.batch;
        cameraTxt = main.cameraTxt;
        imgBackGround = new Texture("menubackground.jpg");
        btnPlay = new Buttons("Play", font, 700, 600);
        btnAbout = new Buttons("About", font, 680, 500);
        btnExit = new Buttons("Exit", font, 707, 300);
        btnSound = new Buttons(isSoundOn ? "Sound On" : "Sound Off", font, 615, 400);
    }
    @Override
    public void show() {
    }
    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()){
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cameraTxt.unproject(touch);
            //System.out.println("zzzz"+touch.x+" "+touch.y);
            if(btnPlay.buttonHit(touch)){
                main.setScreen(main.screenGame);
            }
            if(btnSound.buttonHit(touch)){
                isSoundOn = !isSoundOn;
                btnSound.setText(isSoundOn ? "Sound On" : "Sound Off");
            }
            if(btnAbout.buttonHit(touch)){
                main.setScreen(main.screenAbout);
            }
            if(btnExit.buttonHit(touch)){

                Gdx.app.exit();
            }
        }
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
        font.draw(batchTxt,"Your best score: " + maxScore, 10, 50, W_WIDTH * 100 - 10, Align.left, true);
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
