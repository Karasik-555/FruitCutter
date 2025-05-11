package ru.fruitcutter;

import static com.badlogic.gdx.math.MathUtils.random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
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
    private SpriteBatch batch;
    private SpriteBatch batchTxt;
    private Vector3 touch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private OrthographicCamera cameraTxt;
    private BitmapFont font;
    List<DynamicObjectCircle> fruits = new ArrayList<>();
    Texture background ,apple, watermelon,orange,abricot, bomb;
    TextureRegion imgBackground;
    TextureRegion[] imgFruits = new TextureRegion[5];
    long timeSpawnFruitLast, timeSpawnFruitInterval;
    int score;
    boolean isGameOver;



    public ScreenGame(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        batchTxt = new SpriteBatch();
        cameraTxt = new OrthographicCamera();
        camera.setToOrtho(false, W_WIDTH, W_HEIGHT);
        cameraTxt.setToOrtho(false, W_WIDTH*100, W_HEIGHT*100);
        touch = new Vector3();
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(new MyInputProcessor());
        background = new Texture("background.png");

        font = new BitmapFont(Gdx.files.internal("font.fnt"));
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





    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(TimeUtils.millis()> timeSpawnFruitLast+timeSpawnFruitInterval && !isGameOver){
            timeSpawnFruitInterval = random(200, 2000);
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

        // отрисовка
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(imgBackground,0,0,W_WIDTH,W_HEIGHT);
        batch.end();
        //debugRenderer.render(world, camera.combined);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for(DynamicObjectCircle f: fruits) {
            batch.draw(imgFruits[f.type], f.getX(), f.getY(), f.getWidth()/2, f.getHeight()/2, f.getWidth(), f.getHeight(), 1, 1, f.getAngle());
        }

        batch.end();
        batchTxt.setProjectionMatrix(cameraTxt.combined);
        batchTxt.begin();
        if(!isGameOver) {
            font.draw(batchTxt, "score:" + score, 0, W_HEIGHT * 100 - 10, W_WIDTH * 100 - 10, Align.right, true);
        }
        else {
            font.draw(batchTxt, "Game Over", 0, W_HEIGHT*100/2+50, W_WIDTH*100, Align.center, true);
            font.draw(batchTxt, "Your Score: " + score, 0, W_HEIGHT*100/2-50, W_WIDTH*100, Align.center, true);
        }
        batchTxt.end();
        world.step(1/60f, 6, 2);

    }
    private void spawnFruit(){
        fruits.add(new DynamicObjectCircle(world, random(0,W_WIDTH),-2, 0.5f, MathUtils.random(imgFruits.length-1)));
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
            /*for (int i = 0; i < balls.length; i++) {
                if(balls[i].hit(touchStartPos)) {
                    bodyTouched = balls[i].body;
                }
            }*/
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            touchFinishPos.set(screenX, screenY, 0);
            camera.unproject(touchFinishPos);
            if(bodyTouched != null) {
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
            touchStartPos.set(screenX, screenY, 0);
            camera.unproject(touchStartPos);
            for (int i= fruits.size()-1; i>=0; i--) {
                if(fruits.get(i).hit(touchStartPos)) {
                    if(fruits.get(i).type == 4) {
                        isGameOver = true;
                        score--;
                    }
                    world.destroyBody(fruits.get(i).body);
                    fruits.get(i).body = null;
                    fruits.remove(i);
                    score++;
                }
            }
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




