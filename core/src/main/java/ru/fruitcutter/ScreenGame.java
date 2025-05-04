package ru.fruitcutter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ScreenGame implements Screen {
    public static final float W_WIDTH = 16, W_HEIGHT = 9;
    private SpriteBatch batch;
    private Vector3 touch;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    List<DynamicObjectCircle> fruits = new ArrayList<>();
    Texture circleRed, circleGreen;
    TextureRegion cRed, cGreen;
    long timeSpawnFruitLast,timeSpawnFruitInterval;

    public ScreenGame(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, W_WIDTH, W_HEIGHT);
        touch = new Vector3();
        Box2D.init();
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(new MyInputProcessor());

        circleRed = new Texture("red_circle.png");
        circleGreen = new Texture("green_circle.png");
        cRed = new TextureRegion(circleRed, 256, 256);
        cGreen = new TextureRegion(circleGreen, 256, 256);


    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(TimeUtils.millis()> timeSpawnFruitLast+timeSpawnFruitInterval){
            timeSpawnFruitInterval = MathUtils.random(200, 2000);
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
        debugRenderer.render(world, camera.combined);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for(DynamicObjectCircle f: fruits) {
            batch.draw(f.img, f.getX(), f.getY(), f.getWidth()/2, f.getHeight()/2, f.getWidth(), f.getHeight(), 1, 1, f.getAngle());
        }
        batch.end();
        world.step(1/60f, 6, 2);

    }
    private void spawnFruit(){
        fruits.add(new DynamicObjectCircle(world,MathUtils.random(0,W_WIDTH),-2, 0.5f , cRed));
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
                    world.destroyBody(fruits.get(i).body);
                    fruits.get(i).body = null;
                    fruits.remove(i);
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




