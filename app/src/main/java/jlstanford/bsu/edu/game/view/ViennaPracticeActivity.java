package jlstanford.bsu.edu.game.view;

import android.content.Intent;
import android.graphics.Typeface;
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
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import jlstanford.bsu.edu.game.Gameplay;
import jlstanford.bsu.edu.game.GuitarStrings;
import jlstanford.bsu.edu.game.HorseHair;
import jlstanford.bsu.edu.game.Item;
import jlstanford.bsu.edu.game.Player;

public class ViennaPracticeActivity extends SimpleBaseGameActivity {

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    private boolean scheduleEngineStart;
    private TextureRegion viennaPracticeTextureRegion;
    private Scene viennaPracticeScene = new Scene();
    private Gameplay game;
    private DisplayMetrics metrics = new DisplayMetrics();
    private BitmapTextureAtlas bitmapTextureAtlas;
    private BitmapTextureAtlas backgroundBitmapTextureAtlas;
    private TextureRegion backgroundTiledTextureRegion;
    private Player player;
    private BitmapTextureAtlas guitarStringsBitmapTextureAtlas;
    private TextureRegion guitarStringsTiledTextureRegion;
    private ButtonSprite guitarStringSprite;
    private Font font;
    private GuitarStrings guitarStrings;
    private HorseHair horseHair;
    private HashMap<Item,ButtonSprite> itemMap = new HashMap<Item,ButtonSprite>();
    private ButtonSprite horseHairSprite;
    private Entity inventoryEntity  = new Entity(730,90);;
    private BitmapTextureAtlas beethovenBitmapTextureAtlas;
    private TextureRegion beethovenTiledTextureRegion;
    private ButtonSprite beethovenSprite;


    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0,0,CAMERA_WIDTH,CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new RatioResolutionPolicy(CAMERA_WIDTH,CAMERA_HEIGHT),camera);
    }

    @Override
    protected void onCreateResources() throws IOException {
        game = (Gameplay)getIntent().getSerializableExtra("game");

        guitarStrings = new GuitarStrings(game.getPlayer());
        horseHair = new HorseHair(game.getPlayer());

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        font = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create("Proxima Nova",Typeface.BOLD),32);
        font.load();

        setBackgroundImage();
        setGuitarStringImage();
        setBeethovenImage();
        itemMap.put(guitarStrings, guitarStringSprite);
        itemMap.put(horseHair, horseHairSprite);
        //setHorseHairImage();

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

    private void setBeethovenImage() {
        beethovenBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),64,64,TextureOptions.NEAREST_PREMULTIPLYALPHA);
        beethovenTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(beethovenBitmapTextureAtlas,this,"player.png",0,0);
        beethovenBitmapTextureAtlas.load();
        beethovenSprite = new ButtonSprite(0,0,beethovenTiledTextureRegion,new VertexBufferObjectManager());
    }

    private void setGuitarStringImage() {
        guitarStringsBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),128,64,TextureOptions.NEAREST_PREMULTIPLYALPHA);
        guitarStringsTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(guitarStringsBitmapTextureAtlas,this,"ExtraStrings.png",0,0);
        guitarStringsBitmapTextureAtlas.load();
        guitarStringSprite = new ButtonSprite(0,0,guitarStringsTiledTextureRegion,new VertexBufferObjectManager());
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

        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.mEngine.registerUpdateHandler(new FPSLogger());


        inventoryEntity.attachChild(new Rectangle(0,0,125,780,new VertexBufferObjectManager()));
        viennaPracticeScene.attachChild(inventoryEntity);


        final Entity doorEntity = new Entity(50,270,100,100);
        Rectangle r = new Rectangle(0,0,120,300,new VertexBufferObjectManager());
        r.setSkewY(-20);
        r.setColor(Color.BLACK);
        doorEntity.attachChild(r);
        viennaPracticeScene.attachChild(doorEntity);

        final Entity beethovenEntity = new Entity(CAMERA_WIDTH/2,CAMERA_HEIGHT/2);
     /*  Rectangle beethovenRectangle = new Rectangle(0,0,100,100,new VertexBufferObjectManager());
        beethovenRectangle.setSkewX(-10);
        beethovenRectangle.setSkewY(20);*/
        beethovenEntity.attachChild(beethovenSprite);
        viennaPracticeScene.attachChild(beethovenEntity);


        final Text beethovenSpeech = new Text(0,70,this.font,"...Alas!\nI, the great Beethoven, have broken my strings!",this.getVertexBufferObjectManager());
        beethovenSpeech.setWidth(850);
        beethovenSpeech.setAutoWrap(AutoWrap.NONE);
        beethovenSpeech.setHorizontalAlign(HorizontalAlign.CENTER);

        //org.andengine.entity.scene.menu.item.IMenuItem; use this possibly make inventory a menu
        //org.andengine.entity.scene.menu.MenuScene;


        guitarStringSprite.setColor(Color.WHITE);
        guitarStringSprite.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                toggleColor();
                ViennaPracticeActivity.this.game.handleItemClick(new GuitarStrings(player));
            }
        });
        viennaPracticeScene.registerTouchArea(guitarStringSprite);

        beethovenSprite.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                beethovenEntity.attachChild(beethovenSpeech);
                Timer timer = new Timer();
                Long delayTime = (long) (30 * 100);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        beethovenEntity.detachChild(beethovenSpeech);
                    }
                }, delayTime);
            }
        });
        viennaPracticeScene.registerTouchArea(beethovenSprite);

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
               } else if (pSceneTouchEvent.isActionDown() && ((xCoordinate < 335 && xCoordinate > 263) && (yCoordinate < 284 && yCoordinate > 124))) {
                   player = ViennaPracticeActivity.this.game.getPlayer();
                   ViennaPracticeActivity.this.game.handleItemClick(new GuitarStrings(player));
                   ViennaPracticeActivity.this.toastOnUiThread("Got Guitar Strings!");
                   //inventoryEntity.attachChild(guitarStringSprite);
                   loadInventoryItems();
               }/* else if (pSceneTouchEvent.isActionDown() && ((xCoordinate < CAMERA_WIDTH / 2 + 100 && xCoordinate > CAMERA_WIDTH / 2) && (yCoordinate < CAMERA_HEIGHT / 2 + 100 && yCoordinate > CAMERA_HEIGHT / 2 - 100))) {
                   beethovenEntity.attachChild(beethovenSpeech);
                   Timer timer = new Timer();
                   Long delayTime = (long) (30 * 100);
                   timer.schedule(new TimerTask() {
                       @Override
                       public void run() {
                           beethovenEntity.detachChild(beethovenSpeech);
                       }
                   }, delayTime);
               }*/ else {
               }
               return false;
           }
       });
        loadInventoryItems();
        final Sprite spriteBG = new Sprite(405,240,CAMERA_WIDTH,CAMERA_HEIGHT,this.backgroundTiledTextureRegion,this.getVertexBufferObjectManager());
        SpriteBackground bg = new SpriteBackground(spriteBG);
        viennaPracticeScene.setBackground(bg);


        return viennaPracticeScene;
    }

    private void toggleColor() {
        if(guitarStringSprite.getColor() != Color.YELLOW) {
            guitarStringSprite.setColor(Color.YELLOW);
        }else{guitarStringSprite.setColor(Color.WHITE);}
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
