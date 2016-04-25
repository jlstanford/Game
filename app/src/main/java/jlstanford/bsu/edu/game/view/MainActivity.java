package jlstanford.bsu.edu.game.view;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

import java.io.IOException;

import jlstanford.bsu.edu.game.Gameplay;

public class MainActivity extends SimpleBaseGameActivity {



    private int cameraHeight = 480;
    private int cameraWidth = 800;
    private static int RECTANGLE_SIZE = 180;
    private Entity thingToShow = new Entity(0,cameraHeight);
    private Entity inventoryEntity = new Entity(cameraWidth-60,0,60,cameraHeight);
    private Entity windowEntity = new Entity(cameraWidth - 150, 0, 60,cameraHeight);
    private Entity playerEntity;
    private Scene startScene;
    private Scene viennaStreetScene;
    private Scene viennaPracticeScene;
    private BitmapTextureAtlas mBackgroundTextureAtlas;
    private ITexture backgroundTexture;
    private TextureRegion viennaStreetTextureRegion;
    private TextureRegion viennaPracticeTextureRegion;
    private Gameplay game;
    private EngineOptions opts;
    private boolean scheduleEngineStart;


    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0,0,cameraWidth,cameraHeight);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new RatioResolutionPolicy(cameraWidth,cameraHeight),camera);
    }


    @Override
    protected void onCreateResources() throws IOException {
        /*createViennaStreetResources();
        createViennaPracticeResources();
        setUpViennaStreetScene();
        setUpViennaPracticeScene();*/
    }




    @Override
    public Scene onCreateScene() {
       if(this.mEngine != null) {
           this.mEngine.registerUpdateHandler(new FPSLogger());
       }else{System.out.println("mEngine is null");}

        final Scene scene = new Scene();
        scene.getBackground().setColor(Color.BLACK);
        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if (pSceneTouchEvent.isActionDown()) {
                    MainActivity.this.toastOnUiThread("Screen Pressed!");
                    pScene.detachChild(thingToShow);
                    pScene.attachChild(makeColoredRectangle(10, 0, Color.GREEN));
                }

                return true;
            }
        });

        thingToShow.attachChild(makeColoredRectangle(0, 0, Color.PINK));


		/* Create the rectangles. */
        final Entity windowEntity = new Entity(cameraHeight / 2, cameraWidth / 2);
        windowEntity.attachChild(this.makeColoredRectangle(-RECTANGLE_SIZE / 2, -RECTANGLE_SIZE / 2, Color.WHITE));
        windowEntity.attachChild(this.makeColoredRectangle(RECTANGLE_SIZE / 2, -RECTANGLE_SIZE / 2, Color.BLUE));
        windowEntity.attachChild(this.makeColoredRectangle(RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, Color.BLUE));
        windowEntity.attachChild(this.makeColoredRectangle(-RECTANGLE_SIZE / 2, RECTANGLE_SIZE / 2, Color.BLUE));

        scene.attachChild(thingToShow);

        return scene;
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
    /*
     * Methods
     */


    private void createViennaStreetResources() {

        /*this.mFaceTexture = new AssetBitmapTexture(this.getTextureManager(), this.getAssets(), "[path to picture]", TextureOptions.BILINEAR);
		this.mFaceTextureRegion = TextureRegionFactory.extractFromTexture(mFaceTexture);
		this.mFaceTexture.load();*/

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        final BuildableBitmapTextureAtlas bitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(),1024,1024, TextureOptions.BILINEAR);

        viennaStreetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas,this,"ViennaStreet.jpg");
        try{
            bitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder(0,0,0));
            bitmapTextureAtlas.load();
        }catch(ITextureAtlasBuilder.TextureAtlasBuilderException e){
            e.printStackTrace();
        }


/*
        TextureManager manager = new TextureManager();
        this.mBackgroundTextureAtlas = new BitmapTextureAtlas(manager,1024,1024);
        backgroundTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTextureAtlas,this,*//*pathname,*//*,0,0);
        this.mEngine.getTextureManager().loadTexture(backgroundTexture);*/
    }



    private void setUpViennaStreetScene() {
        viennaStreetScene = new Scene();
        /*This scene will have a sprite background*/
        final Entity backButton = new Entity(cameraWidth/3, 0, cameraWidth/3, cameraHeight/8);
        final Entity inventory = new Entity(cameraWidth-(cameraWidth/10), 0, cameraWidth/10 ,cameraHeight);

        final float centerX = (cameraWidth-this.viennaStreetTextureRegion.getWidth()/2);
        final float centerY = (cameraHeight-this.viennaStreetTextureRegion.getHeight()/2);

        final Sprite spriteBG = new Sprite(centerX,centerY,this.viennaStreetTextureRegion,this.getVertexBufferObjectManager());
        //viennaStreetScene.attachChild(spriteBG);
        SpriteBackground bg = new SpriteBackground(spriteBG);
        viennaStreetScene.setBackground(bg);

        viennaStreetScene.setOnAreaTouchListener(new IOnAreaTouchListener() {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if(pSceneTouchEvent.isActionDown()&& pTouchArea.contains(backButton.getX(),backButton.getY())){
                    //go to practice scene
                    runOnUpdateThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.this.getEngine().setScene(viennaPracticeScene);
                        }
                    });
                    //gameplay.changescene
                }else if(pSceneTouchEvent.isActionDown()&& pTouchArea.contains(inventory.getX(),inventory.getY())) {
                    // checkTouchedAreaIsItem();
                }/*else if(pSceneTouchEvent.isActionDown()&& pTouchArea.contains(horseAreaX, horseAreaY)){
                    //get Horsehair
                }*/
                return true;
            }
        });
    }

    private Rectangle makeColoredRectangle(final float pX, final float pY, final Color pColor) {
        final Rectangle coloredRect = new Rectangle(pX, pY, RECTANGLE_SIZE/2, RECTANGLE_SIZE/2, this.getVertexBufferObjectManager());
        coloredRect.setColor(pColor);
        return coloredRect;
    }

}
