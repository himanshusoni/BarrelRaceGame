package barrelrace.hss.ui.com.barrelracegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnClickListener{

    private String playerName = "Foo";
    public static ScoreUtility scoreUtility;

    private TextView playerNameTextView;
    private Button playGameButton;
    private Button viewScoresButton;
    private EditText playerNameEditText;

    private ArrayList<Score> scoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreUtility = new ScoreUtility(this);
        scoreUtility.readAllScores();
        playerName = scoreUtility.getPlayerName();

        playerNameTextView = (TextView) findViewById(R.id.playerNameTextView);
        playerNameTextView.setText("Welcome " + playerName);

        playGameButton = (Button) findViewById(R.id.playGameButton);
        viewScoresButton = (Button) findViewById(R.id.viewScoresButton);

        playGameButton.setOnClickListener(this);
        viewScoresButton.setOnClickListener(this);

        playerNameEditText = (EditText) findViewById(R.id.playerNameEditText);
        playerNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(charSequence.length()==0){
                    playerName = "Foo";
                }else{
                    playerName = charSequence.toString();
                }
                playerNameTextView.setText("Welcome " + playerName);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_exit) {

            // TODO exit application
            System.exit(1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.playGameButton:
                Intent gameIntent = new Intent(this,GameActivity.class);
                gameIntent.putExtra("playerName",playerName);
                startActivity(gameIntent);

                break;
            case R.id.viewScoresButton:
                Intent viewScoresIntent = new Intent(this,ScoreBoardActivity.class);
                startActivity(viewScoresIntent);
                break;
        }
    }

    public static ArrayList<Score> getScores(){
        return scoreUtility.readAllScores();
    }
}
