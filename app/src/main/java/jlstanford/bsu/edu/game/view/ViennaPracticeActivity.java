package jlstanford.bsu.edu.game.view;

import android.content.Intent;
import android.util.DisplayMetrics;

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
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

import java.io.IOException;

import jlstanford.bsu.edu.game.Gameplay;
import jlstanford.bsu.edu.game.GuitarStrings;
import jlstanford.bsu.edu.game.Player;

public class ViennaPracticeActivity extends SimpleBaseGameActivity {

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    private boolean scheduleEngineStart;
    private TextureRegion viennaPracticeTextureRegion;
    private Scene viennaPracticeScene;
    private Gameplay game;
    private DisplayMetrics metrics = new DisplayMetrics();
    private BitmapTextureAtlas bitmapTextureAtlas;
    private BitmapTextureAtlas backgroundBitmapTextureAtlas;
    private TextureRegion backgroundTiledTextureRegion;
    private Player player;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),camera);
    }

    @Override
    protected void onCreateResources() throws IOException {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        final BuildableBitmapTextureAtlas buildableBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(),512,1024, TextureOptions.BILINEAR);
        setBackgroundImage();

    }

    private void setBackgroundImage() {
        backgroundBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),4096,2048,TextureOptions.NEAREST_PREMULTIPLYALPHA);
        backgroundTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundBitmapTextureAtlas,this,"ViennaPractice.png",0,0);
        backgroundBitmapTextureAtlas.load();
    }

    /*
    Scene starts at bottom left corner at position (50,50). Top right corner at approx (850,780)
     */
    @Override
    protected Scene onCreateScene() {
        game = (Gameplay)getIntent().getSerializableExtra("game");
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mEngine.registerUpdateHandler(new FPSLogger());

        final Scene viennaPracticeScene = new Scene();

        Entity inventoryEntity = new Entity(730,90);
        inventoryEntity.attachChild(new Rectangle(0,0,125,780,new VertexBufferObjectManager()));
        viennaPracticeScene.attachChild(inventoryEntity);

        final Entity doorEntity = new Entity(50,270,100,100);
        Rectangle r = new Rectangle(0,0,120,300,new VertexBufferObjectManager());
        r.setSkewY(-20);
        r.setColor(Color.BLACK);
        doorEntity.attachChild(r);
        viennaPracticeScene.attachChild(doorEntity);

        Entity beethovenEntity = new Entity(CAMERA_WIDTH/2,CAMERA_HEIGHT/2);
        Rectangle beethovenRectangle = new Rectangle(0,0,100,100,new VertexBufferObjectManager());
        beethovenRectangle.setSkewX(-10);
        beethovenRectangle.setSkewY(20);
        beethovenEntity.attachChild(beethovenRectangle);
        viennaPracticeScene.attachChild(beethovenEntity);




       viennaPracticeScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
           @Override
           public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
              float xCoordinate = pSceneTouchEvent.getX();
               float yCoordinate = pSceneTouchEvent.getY();
               if (pSceneTouchEvent.isActionDown() && pSceneTouchEvent.getX() < 70) {
                   Intent startIntent = new Intent(ViennaPracticeActivity.this.getApplication(), ViennaStreetActivity.class);
                   startIntent.putExtra("game", game);
                   ViennaPracticeActivity.this.startActivity(startIntent);
                   ViennaPracticeActivity.this.finish();
                   ViennaPracticeActivity.this.game.switchToScene(jlstanford.bsu.edu.game.Scene.VIENNA_STREET);
               } else if (pSceneTouchEvent.isActionDown() && ((xCoordinate<335&&xCoordinate>263)&&(yCoordinate<284&&yCoordinate>124))){
                   player = ViennaPracticeActivity.this.game.getPlayer();
                   ViennaPracticeActivity.this.game.handleItemClick(new GuitarStrings(player));
                   ViennaPracticeActivity.this.toastOnUiThread("Got Guitar Strings!");
               }
                   return false;
           }
       });


        final Sprite spriteBG = new Sprite(405,240,CAMERA_WIDTH,CAMERA_HEIGHT,this.backgroundTiledTextureRegion,this.getVertexBufferObjectManager());
        SpriteBackground bg = new SpriteBackground(spriteBG);
        viennaPracticeScene.setBackground(bg);



        return viennaPracticeScene;
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
