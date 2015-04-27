package barrelrace.hss.ui.com.barrelracegame;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ScoreBoardActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        TextView emptyTextView = (TextView) findViewById(R.id.empty);

        emptyTextView.setVisibility(View.GONE);

        MyListAdapter listAdapter = new MyListAdapter(this,R.layout.listview_item,MainActivity.getScores());
        setListAdapter(listAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}


// A custom list adapter to bind the list with a custom view
class MyListAdapter extends ArrayAdapter<Score> {

    private ArrayList<Score> scoreList;
    private int listItemView;

    public MyListAdapter(Context context, int listItemView, ArrayList<Score> objects) {
        super(context, listItemView, objects);
        this.listItemView = listItemView;
        this.scoreList = objects;
    }

    // binds the list item for the custom view
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(listItemView, null);
        }

        Score scoreItem = scoreList.get(position);

        if (scoreItem != null) {

            TextView playerName = (TextView) v.findViewById(R.id.itemName);
            TextView playerTime = (TextView) v.findViewById(R.id.itemTime);

            // TODO display as minutes:seconds

            playerName.setText(scoreItem.getName());
            playerTime.setText(scoreItem.getTime()+" seconds");
        }

        return v;
    }
}
