package barrelrace.hss.ui.com.barrelracegame;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

import barrelrace.hss.ui.com.barrelracegame.R;

/**
 * Created by spock on 28/11/14.
 */
public class GameActivity extends Activity implements SensorEventListener{
    private SensorManager sensorManager = null;

    private GameView gameView;

    private FrameLayout frameLayout;
    private RelativeLayout gameInfoLayout;
    private TextView timeTextView;
    private TextView playerNameTextView;

    private Date startTime;

    private String playerName = "Foo";
    private ArrayList<Score> scoreList = new ArrayList<Score>();

    public void GameActivity(String playerName){
        this.playerName = playerName;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_layout);
        // Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);

        gameView = (GameView) findViewById(R.id.gameViewBoard);
        gameView.setParent(this);
        playerName = getIntent().getStringExtra("playerName");
        gameView.setPlayerName(playerName);

        MainActivity.scoreUtility.setPlayerName(playerName);

        playerNameTextView = (TextView) findViewById(R.id.playerNameTextView);
        playerNameTextView.setText(playerName);

        /*gameInfoLayout = (RelativeLayout) findViewById(R.id.gameInfoLayout);
        gameInfoLayout.setOnClickListener(this);*/
        timeTextView = (TextView) findViewById(R.id.timeTextView);
        timeTextView.setText("--:--");

        GameDialog gameDialog=new GameDialog(this,"start","",false,false);
        gameDialog.show();
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        gameView.update(-sensorEvent.values[0],sensorEvent.values[1]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void startGame(){
        gameView.startGame();
    }

    public GameView getGameView(){
        return gameView;
    }

    public void setTime(String time,long timeInSeconds){

        timeTextView.setText(time);
    }
    public int addScore(long timeElapsedInSeconds){
        Log.i("Completed","player " + playerName + " time " +timeElapsedInSeconds );
        return MainActivity.scoreUtility.addScore(playerName, timeElapsedInSeconds);
    }
}
