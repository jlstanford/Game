package jlstanford.bsu.edu.game.view;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.ui.activity.SimpleLayoutGameActivity;

import java.io.IOException;

import jlstanford.bsu.edu.game.Gameplay;
import jlstanford.bsu.edu.game.R;

public class StartScreen extends SimpleLayoutGameActivity implements TextWatcher {

    private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    private EditText nameBox;
    private Button startButton;
    private Font mFont;
    private Text mText;
    private Line mRight;
    private Line mLeft;
    private Scene startScene;
    private Camera camera;
    Gameplay game;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_start_screen;
    }

    @Override
    protected int getRenderSurfaceViewID() {
        return R.id.surfaceView;
    }


    @Override
    public EngineOptions onCreateEngineOptions() {
        camera = new Camera(0,0,StartScreen.CAMERA_WIDTH,StartScreen.CAMERA_HEIGHT);

        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,new RatioResolutionPolicy(StartScreen.CAMERA_WIDTH,StartScreen.CAMERA_HEIGHT),camera);

    }

    @Override
    protected void onCreateResources() throws IOException {

    }


    @Override
    protected Scene onCreateScene() throws IOException {

        this.mEngine.registerUpdateHandler(new FPSLogger());

        /*startScene = new Scene();
        startScene.getBackground().setColor(0.09804f, 0.6274f, 0.8784f);

        final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
        this.mText = new Text(50, 40, this.mFont, "", 800, new TextOptions(AutoWrap.LETTERS, AUTOWRAP_WIDTH, HorizontalAlign.CENTER), vertexBufferObjectManager);*/
        return null;
    }






    @Override
    protected void onSetContentView(){
        super.onSetContentView();
        this.nameBox = (EditText) findViewById(R.id.nameBox);
        this.startButton = (Button) findViewById(R.id.startButton);
        this.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = StartScreen.this.nameBox.getText().toString();
                game = new Gameplay(playerName);
                Intent startIntent = new Intent(StartScreen.this.getApplication(), ViennaPracticeActivity.class);
                startIntent.putExtra("game",game);
                StartScreen.this.startActivity(startIntent);
                StartScreen.this.finish();

            }
        });
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
