package barrelrace.hss.ui.com.barrelracegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by spock on 2/12/14.
 */
public class ScoreUtility {

    String sharePreferenceFileKey = "scoreboard";
    String nameKey = "names";

    Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    HashMap<String,Long> scores = new HashMap<String, Long>();

    ArrayList<Score> scoreList = new ArrayList<Score>();

    /*
    * Scoreboard file will have first line with the player names list
    * Followed by
    * each player name as key and their scores as value set
    * */

    public ScoreUtility(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(sharePreferenceFileKey, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public ArrayList<Score> readAllScores(){
        scoreList.clear();

        Set<String> nameSet;
        nameSet = prefs.getStringSet(nameKey,new HashSet<String>());
        Log.i("SCORE","names size "+ nameSet.size());
        Log.i("SCORE","names "+ nameSet.toString());
        int i = 0;
        for(String name: nameSet){
            Set<String> scoreForName;
            String scoreLine = "";
            Log.i("SCORE","getting all scores for : "+ name);
            scoreForName = prefs.getStringSet(name,null);
            for(String score : scoreForName){
                scoreList.add(new Score(name,Long.parseLong(score)));
                scoreLine+=score + ",";
            }
            Log.i("SCORE","AND scores " + scoreLine);
        }

        Log.i("SCORE SIZE","size "+ scoreList.size());
        // TODO sort the scoreList
        Collections.sort(scoreList);
        Log.i("SCORE SIZE","size "+ scoreList.size());

        return scoreList;
    }

    // returns 1 if the score is a high score | else returns 0
    public int addScore(String playerName,long score){

        Log.i("SCORE ADDING", "player "+ playerName + " score " + score);
        scoreList.add(new Score(playerName,score));
        Log.i("SCORE ADDING", "score list (should be 11)size "+ scoreList.size());
        Collections.sort(scoreList);

        Score firstItem = scoreList.get(0);

        //refreshList();

        persistList();

if(scoreList.size()!= 0 ) {
    Log.i("SCORE ADDING", "player "+ firstItem.getName() + " score " + firstItem.getTime());
    if (firstItem.getName().equals(playerName) && firstItem.getTime() == score) {
        // you have a high score
        return 1;
    }
}
        return 0;
    }
    public void persistList(){

        editor.clear();
        Set<String> nameSet = new HashSet<String>();
        HashMap<String,Set<String>> scoresMap = new HashMap<String, Set<String>>();
        // Discard the other values and keep only 10 highest values
        int i = 0;
        for(Score score : scoreList){
            nameSet.add(score.getName());
            scoresMap.put(score.getName(),new HashSet<String>());
            if(++i == 10){
                break;
            }
        }
        editor.putStringSet(nameKey,nameSet);

        i = 0;
        for(Score score : scoreList){
            Set<String> tempSet = scoresMap.get(score.getName());
            tempSet.add(score.getTime()+"");
            if(++i == 10){
                break;
            }
        }

        for(String name : nameSet){
            editor.putStringSet(name,scoresMap.get(name));
        }

        editor.commit();
    }
/*    public void refreshList(){
        ArrayList<Score> temp = new ArrayList<Score>(10);
        for(Score s : scoreList){
            try{
                temp.add(s);
            }catch(IndexOutOfBoundsException e){
                Log.i("Exception"," trying to add 11th value. " + e.getMessage());
            }
        }
        scoreList = temp;
    }*/

    public String getPlayerName(){
        return prefs.getString("playername","Foo");
    }
    public void setPlayerName(String name){
        editor.putString("playername",name);
        editor.commit();
    }
}

class Score implements Comparable<Score>{
    String name;
    long time;

    Score(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int compareTo(Score o) {
        if (this.getTime() == o.getTime())
            return 0;
        else if (this.getTime() > o.getTime())
            return 1;
        else
            return -1;
    }
}