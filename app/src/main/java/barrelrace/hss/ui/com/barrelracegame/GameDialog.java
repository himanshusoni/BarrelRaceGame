package barrelrace.hss.ui.com.barrelracegame;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import barrelrace.hss.ui.com.barrelracegame.R;

public class GameDialog extends Dialog implements
        android.view.View.OnClickListener {

    public GameActivity c;
    public Dialog d;
    public RelativeLayout relativeLayout;
    public String action;
    public String time;

    private TextView gameInfoTextView;
    private TextView gameTimeTextView;
    private TextView gameTitleTextView;

    String infoText = "Click here to start";
    boolean isHighScore;
boolean isFinished;

    public GameDialog(GameActivity a,String action,String time,boolean isHighScore,boolean isFinished) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.action = action;
        this.isHighScore = isHighScore;
        this.time = time;
        this.isFinished = isFinished;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.game_dialog);

        relativeLayout = (RelativeLayout) findViewById(R.id.gameInfoLayout);
        relativeLayout.setOnClickListener(this);

        gameInfoTextView = (TextView) findViewById(R.id.gameInfoTextView);
        gameTimeTextView = (TextView) findViewById(R.id.gameTimeTextView);
        gameTitleTextView = (TextView) findViewById(R.id.gameTitleTextView);

        if(action.equals("start")){
            gameInfoTextView.setText(infoText);
        }else if(action.equals("restart")){
            gameInfoTextView.setText(infoText + " again");
        }

        if(isFinished){
            gameTitleTextView.setText("Completed");
        }

        if(isHighScore)
            time += " HighScore!!";
        if(gameTimeTextView == null){
            Log.i("AGAIN","time is null for " + time);
        }
        gameTimeTextView.setText(time);

    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.btn_yes:
                c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }*/
        c.startGame();
        dismiss();
    }
}