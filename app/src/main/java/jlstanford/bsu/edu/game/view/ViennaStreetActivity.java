package jlstanford.bsu.edu.game.view;

import android.content.Intent;
import android.graphics.Typeface;

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
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.color.Color;

import java.io.IOException;
import java.util.HashMap;

import jlstanford.bsu.edu.game.Gameplay;
import jlstanford.bsu.edu.game.GuitarStrings;
import jlstanford.bsu.edu.game.HorseHair;
import jlstanford.bsu.edu.game.Item;
import jlstanford.bsu.edu.game.Player;


public class ViennaStreetActivity extends SimpleBaseGameActivity {
    private boolean scheduleEngineStart;
    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    private TextureRegion viennaStreetTextureRegion;
    private Scene viennaStreetScene = new Scene();;
    private Gameplay game;
    private BitmapTextureAtlas backgroundBitmapTextureAtlas;
    private ITextureRegion backgroundTiledTextureRegion;
    private Player player;
    private BitmapTextureAtlas guitarStringsBitmapTextureAtlas;
    private TextureRegion guitarStringsTiledTextureRegion;
    private ButtonSprite guitarStringSprite;
    private GuitarStrings guitarStrings;
    private HorseHair horseHair;
    private HashMap<Item,ButtonSprite> itemMap = new HashMap<Item,ButtonSprite>();
    private ButtonSprite horseHairSprite;
    private Entity inventoryEntity = new Entity(730,90);
    private Font font;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),camera);
    }

    @Override
    protected void onCreateResources() throws IOException {
        game = (Gameplay) getIntent().getSerializableExtra("game");

         guitarStrings = new GuitarStrings(game.getPlayer());
        horseHair = new HorseHair(game.getPlayer());

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        backgroundBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),4096,2048, TextureOptions.NEAREST_PREMULTIPLYALPHA);
        backgroundTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundBitmapTextureAtlas, this, "ViennaStreet.jpg", 0, 0);
        backgroundBitmapTextureAtlas.load();

        font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create("Proxima Nova", Typeface.BOLD), 32);
        font.load();

        setGuitarStringImage();
        itemMap.put(guitarStrings, guitarStringSprite);
        itemMap.put(horseHair, horseHairSprite);

    }

    private void loadInventoryItems() {
        for (Item i : itemMap.keySet()){
            attach(i);
        }
    }

    private void attach(Item i) {
        if(this.game.getPlayer().hasInInventory(i)&& i instanceof GuitarStrings){
            inventoryEntity.attachChild(itemMap.get(i));
        }
    }

    private void setGuitarStringImage() {
        guitarStringsBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),128,64,TextureOptions.NEAREST_PREMULTIPLYALPHA);
        guitarStringsTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(guitarStringsBitmapTextureAtlas,this,"ExtraStrings.png",0,0);
        guitarStringsBitmapTextureAtlas.load();
        guitarStringSprite = new ButtonSprite(0,0,guitarStringsTiledTextureRegion,new VertexBufferObjectManager());
    }

    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger());


        final Text backButtonText = new Text(0,0,this.font,"BACK",this.getVertexBufferObjectManager());
        final Entity backButton = new Entity(400, 520, 100, 100);
        final Rectangle backRectangle = new Rectangle(0,0,200,100,new VertexBufferObjectManager());
        backRectangle.setColor(Color.PINK);
        backButton.attachChild(backRectangle);
        backButton.attachChild(backButtonText);
        viennaStreetScene.attachChild(backButton);



        inventoryEntity.attachChild(new Rectangle(0,0,125,780,new VertexBufferObjectManager()));
        viennaStreetScene.attachChild(inventoryEntity);


        guitarStringSprite.setColor(Color.WHITE);
        guitarStringSprite.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                toggleColor();
                ViennaStreetActivity.this.game.handleItemClick(new GuitarStrings(player));
            }
        });
        viennaStreetScene.registerTouchArea(guitarStringSprite);

        loadInventoryItems();
        final Sprite spriteBG = new Sprite(405,230,CAMERA_WIDTH,CAMERA_HEIGHT,this.backgroundTiledTextureRegion,this.getVertexBufferObjectManager());
        SpriteBackground bg = new SpriteBackground(spriteBG);
        viennaStreetScene.setBackground(bg);



        viennaStreetScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
                if(pSceneTouchEvent.isActionDown() && (pSceneTouchEvent.getY() > 400 && (pSceneTouchEvent.getX()<CAMERA_WIDTH-200 && pSceneTouchEvent.getX()>200) )){
                    Intent startIntent = new Intent(ViennaStreetActivity.this.getApplication(), ViennaPracticeActivity.class);
                    startIntent.putExtra("game",game);
                    ViennaStreetActivity.this.startActivity(startIntent);
                    ViennaStreetActivity.this.finish();
                    game.switchToScene(jlstanford.bsu.edu.game.Scene.VIENNA_PRACTICE);
                }
                return false;
            }
        });

        return viennaStreetScene;
    }

    private void toggleColor() {
        if(guitarStringSprite.getColor() != Color.YELLOW) {
            guitarStringSprite.setColor(Color.YELLOW);
        }else {guitarStringSprite.setColor(Color.WHITE);}
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
