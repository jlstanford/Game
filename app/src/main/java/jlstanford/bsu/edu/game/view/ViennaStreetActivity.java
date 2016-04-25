package jlstanford.bsu.edu.game.view;

import android.content.Intent;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import java.io.IOException;

import jlstanford.bsu.edu.game.Gameplay;

/**
 * Created by Jessica on 4/24/2016.
 */
public class ViennaStreetActivity extends SimpleBaseGameActivity {
    private boolean scheduleEngineStart;
    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    private TextureRegion viennaStreetTextureRegion;
    private Scene viennaStreetScene;
    private Gameplay game;
    private BitmapTextureAtlas backgroundBitmapTextureAtlas;
    private ITextureRegion backgroundTiledTextureRegion;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),camera);
    }

    @Override
    protected void onCreateResources() throws IOException {


        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        backgroundBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),4096,2048, TextureOptions.NEAREST_PREMULTIPLYALPHA);
        backgroundTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundBitmapTextureAtlas, this, "ViennStreet.png", 0, 0);
        backgroundBitmapTextureAtlas.load();
    }

    @Override
    protected Scene onCreateScene() {
        game = (Gameplay) getIntent().getSerializableExtra("game");
        viennaStreetScene = new Scene();

        final Entity backButton = new Entity(400, 520, 100, 100);
        final Rectangle backRectangle = new Rectangle(0,0,200,100,new VertexBufferObjectManager());
        backButton.attachChild(backRectangle);


        Entity inventoryEntity = new Entity(730,90);
        inventoryEntity.attachChild(new Rectangle(0,0,125,780,new VertexBufferObjectManager()));



        final Sprite spriteBG = new Sprite(405,240,this.backgroundTiledTextureRegion,this.getVertexBufferObjectManager());
        //viennaStreetScene.attachChild(spriteBG);
        SpriteBackground bg = new SpriteBackground(spriteBG);
        viennaStreetScene.setBackground(bg);
        viennaStreetScene.attachChild(backButton);
        viennaStreetScene.attachChild(inventoryEntity);
        viennaStreetScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if(pSceneTouchEvent.isActionDown() && (pSceneTouchEvent.getY() > 400 && (pSceneTouchEvent.getX()<CAMERA_WIDTH-200 && pSceneTouchEvent.getX()>200) )){
                    Intent startIntent = new Intent(ViennaStreetActivity.this.getApplication(), ViennaPracticeActivity.class);
                    ViennaStreetActivity.this.startActivity(startIntent);
                    ViennaStreetActivity.this.finish();
                    game.switchToScene(jlstanford.bsu.edu.game.Scene.VIENNA_PRACTICE);
                }
                return false;
            }
        });

        return viennaStreetScene;
    }



    @Override
    public Engine onCreateEngine(EngineOptions engineOptions){
        Engine engine = new Engine(engineOptions);
        if(scheduleEngineStart){
            engine.start();
            scheduleEngineStart = !scheduleEngineStart;
        }
        return engine;
    }

    @Override
    public synchronized  void onResumeGame() {
        if(this.mEngine != null){
            super.onResumeGame();
            scheduleEngineStart = true;
        }
    }
}
