package ru.fruitcutter;

import static com.badlogic.gdx.math.MathUtils.random;
import static ru.fruitcutter.Main.isSoundOn;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;
import java.util.List;
public class ScreenGame implements Screen {
    public static final float W_WIDTH = 16, W_HEIGHT = 9;
    private SpriteBatch batch,batchTxt;
    private Vector3 touch;
    private World world;
    private Main main;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera,cameraTxt;
    private BitmapFont font;
    public int score;
    public static int maxScore;
    List<DynamicObjectFruit> fruits = new ArrayList<>();
    Texture background ,apple, watermelon,orange, abricot, bomb;
    TextureRegion imgBackground;
    TextureRegion[] imgFruits = new TextureRegion[5];
    long timeSpawnFruitLast, timeSpawnFruitInterval;
    boolean isGameOver;
    Buttons btnMenu,btnRestart;
    Sound sndCut,sndExplosion;
    public ScreenGame(Main main){
        this.main = main;
        batch = main.batch;
        camera = main.camera;
        batchTxt = main.batchTxt;
        cameraTxt = main.cameraTxt;
        touch = main.touch;
        font = main.font;
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(new MyInputProcessor());
        background = new Texture("background.jpg");
        btnMenu = new Buttons("Menu", font, 1470, 890);
        btnRestart = new Buttons("Restart", font, 700, W_HEIGHT*100/2-100);
        apple = new Texture("apple.png");
        watermelon = new Texture("watermelon.png");
        orange = new Texture("orange.png");
        abricot = new Texture("abricot.png");
        bomb = new Texture("bomb.png");
        imgBackground = new TextureRegion(background,1600,900);
        imgFruits[0] = new TextureRegion(apple, 256, 256);
        imgFruits[1] = new TextureRegion(watermelon, 384,384);
        imgFruits[2] = new TextureRegion(orange, 256,256);
        imgFruits[3] = new TextureRegion(abricot,256,256);
        imgFruits[4] = new TextureRegion(bomb, 384,384);
        sndCut = Gdx.audio.newSound(Gdx.files.internal("cut.mp3"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
    }
    @Override
    public void show() {
    }
    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cameraTxt.unproject(touch);
            if (btnMenu.buttonHit(touch)) {
                main.setScreen(main.screenMenu);
                isGameOver = false;
                score = 0;
            }
            if (btnRestart.buttonHit(touch)) {
                isGameOver = false;
                score = 0;
            }
        }
        if(TimeUtils.millis()> timeSpawnFruitLast+timeSpawnFruitInterval && !isGameOver){
            timeSpawnFruitInterval = random(200,1000);
            timeSpawnFruitLast = TimeUtils.millis();
            spawnFruit();
        }
        for (int i = fruits.size()-1; i >=0 ; i--) {
            if(fruits.get(i).out()) {
                world.destroyBody(fruits.get(i).body);
                fruits.get(i).body = null;
                fruits.remove(i);
            }
        }
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(imgBackground,0,0,W_WIDTH,W_HEIGHT);
        for(DynamicObjectFruit f: fruits) {
            batch.draw(imgFruits[f.type], f.getX(), f.getY(), f.getWidth()/2, f.getHeight()/2, f.getWidth(), f.getHeight(), 1, 1, f.getAngle());
        }
        batch.end();
        batchTxt.setProjectionMatrix(cameraTxt.combined);
        batchTxt.begin();
        if(!isGameOver) {
            font.draw(batchTxt, "score:" + score, 0, W_HEIGHT * 100 - 10, W_WIDTH * 100 - 10, Align.right, true);
        }
        else {
            font.draw(batchTxt, "Game Over", 0, W_HEIGHT*100/2+100, W_WIDTH*100, Align.center, true);
            font.draw(batchTxt, "Your Score: " + score, 0, W_HEIGHT*100/2, W_WIDTH*100, Align.center, true);
            btnMenu.font.draw(batchTxt, btnMenu.text, btnMenu.x, btnMenu.y);
            btnRestart.font.draw(batchTxt, btnRestart.text, btnRestart.x, btnRestart.y);
        }
        batchTxt.end();
        world.step(1/60f, 6, 2);
    }
    private void spawnFruit(){
        fruits.add(new DynamicObjectFruit(world, random(0,W_WIDTH),-2, 0.5f, MathUtils.random(imgFruits.length-1)));
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
        batch.dispose();
        font.dispose();
    }
    class MyInputProcessor implements InputProcessor {
        Vector3 touchStartPos = new Vector3();
        Vector3 touchFinishPos = new Vector3();
        private Body bodyTouched;
        @Override
        public boolean keyDown(int keycode) {
            return false;
        }
        @Override
        public boolean keyUp(int keycode) {
            return false;
        }
        @Override
        public boolean keyTyped(char character) {
            return false;
        }
        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            touchStartPos.set(screenX, screenY, 0);
            camera.unproject(touchStartPos);
            return false;
        }
        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            touchFinishPos.set(screenX, screenY, 0);
            camera.unproject(touchFinishPos);
            if (bodyTouched != null) {
                Vector3 swipe = new Vector3(touchFinishPos).sub(touchStartPos);
                bodyTouched.applyLinearImpulse(new Vector2(-swipe.x, -swipe.y), bodyTouched.getPosition(), true);
                bodyTouched = null;
            }
            return false;
        }
        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            return false;
        }
        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (!isGameOver) {
                touchStartPos.set(screenX, screenY, 0);
                camera.unproject(touchStartPos);
                for (int i = fruits.size() - 1; i >= 0; i--) {
                    if (fruits.get(i).hit(touchStartPos)) {
                        if (fruits.get(i).type == 4) {
                            isGameOver = true;
                            score--;
                            if(isSoundOn) sndExplosion.play();
                        }
                        world.destroyBody(fruits.get(i).body);
                        fruits.get(i).body = null;
                        fruits.remove(i);
                        score++;
                        if(maxScore<=score){
                            maxScore = score;
                        }
                        if(isSoundOn) sndCut.play();
                    }
                }}
                return false;
            }
        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }
        @Override
        public boolean scrolled(float amountX, float amountY) {
            return false;
        }
    }
}




