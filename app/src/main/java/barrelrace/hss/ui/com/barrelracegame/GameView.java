package barrelrace.hss.ui.com.barrelracegame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by spock on 29/11/14.
 */
public class GameView extends View {

    private String playerName;

    private GameActivity parent;

    Path path;

    private float xSpeed,ySpeed;
    private float centerX,centerY;
    private float oldX,oldY;

    private float barrel1X,barrel1Y,barrel2X,barrel2Y,barrel3X,barrel3Y;
    private Paint framePaint,barrelPaint1,barrelPaint2,barrelPaint3,horsePaint,trailPaint,startLinePaint;

    private float frameWidth,frameHeight;
    private int radius = 30;

    public Date startTime;
    public Date timer;
    private Date pauseTime;
    private Date resumeTime;

    public long timeElapsedInSeconds;

    private boolean running;


    ArrayList<Float> pathX = new ArrayList<Float>();
    ArrayList<Float> pathY = new ArrayList<Float>();

    ArrayList<Integer> firstBarrel = new ArrayList<Integer>();
    ArrayList<Integer> secondBarrel = new ArrayList<Integer>();
    ArrayList<Integer> thirdBarrel = new ArrayList<Integer>();

    private boolean barrel1Completed,barrel2Completed,barrel3Completed;

    private int firstQuadrant1,secondQuadrant1,thirdQuadrant1,forthQuadrant1;
    private int firstQuadrant2,secondQuadrant2,thirdQuadrant2,forthQuadrant2;
    private int firstQuadrant3,secondQuadrant3,thirdQuadrant3,forthQuadrant3;
    
    boolean initDone = false;

    boolean ignoreFrameCollision = false;
    private int frameCollisionCount;
    private Date frameCollisionStart;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        /*if((Activity)this.getContext() instanceof  GameActivity){
            Log.i("PARENT","TRUE");
        }*/


    }

    public void initGame(){

        initDone = true;

        pathX = new ArrayList<Float>();
        pathY = new ArrayList<Float>();
        //path = new Path();

        startTime = new Date();
        timer = startTime;
        updateTimer();

        frameCollisionCount = 0;

        xSpeed = oldX = centerX;
        ySpeed = oldX = frameHeight - 50;
        path = new Path();
        path.reset();
        Log.i("PATH INIT"," old x "+oldX + "oldY " + oldY);
        path.moveTo(oldX,oldY);

        barrel1X = centerX;
        barrel1Y = centerY-150;

        barrel2X = centerX - 150;
        barrel2Y = centerY +100;

        barrel3X = centerX + 150;
        barrel3Y = centerY + 100;

        framePaint = new Paint();
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setStrokeWidth(6);
        framePaint.setColor(Color.BLACK);

        barrelPaint1 = new Paint();
        barrelPaint1.setColor(Color.MAGENTA);
        barrelPaint2 = new Paint();
        barrelPaint2.setColor(Color.MAGENTA);
        barrelPaint3 = new Paint();
        barrelPaint3.setColor(Color.MAGENTA);

        horsePaint = new Paint();
        horsePaint.setColor(Color.YELLOW);

        trailPaint = new Paint();
        trailPaint.setColor(Color.RED);
        trailPaint.setAntiAlias(true);
        trailPaint.setDither(true);
        trailPaint.setColor(0xFFFF0000);
        trailPaint.setStyle(Paint.Style.STROKE);
        trailPaint.setStrokeJoin(Paint.Join.ROUND);
        trailPaint.setStrokeCap(Paint.Cap.ROUND);
        trailPaint.setStrokeWidth(12);

        startLinePaint = new Paint();
        startLinePaint.setStyle(Paint.Style.STROKE);
        startLinePaint.setStrokeWidth(4);
        startLinePaint.setColor(Color.BLUE);

        pathX = new ArrayList<Float>();
        pathY = new ArrayList<Float>();

        firstBarrel = new ArrayList<Integer>();
        secondBarrel = new ArrayList<Integer>();
        thirdBarrel = new ArrayList<Integer>();

        barrel1Completed = false;
                barrel2Completed = false;
        barrel3Completed = false;

        firstQuadrant1= 0;secondQuadrant1=0;thirdQuadrant1=0;forthQuadrant1=0;
        firstQuadrant2= 0;secondQuadrant2=0;thirdQuadrant2=0;forthQuadrant2=0;
        firstQuadrant3= 0;secondQuadrant3=0;thirdQuadrant3=0;forthQuadrant3=0;

    }
    protected void onDraw(Canvas canvas)
    {

        frameWidth = this.getWidth();
        //frameHeight = this.getHeight();
        frameHeight = this.getWidth() + 100 ;
        centerX = frameWidth/2;
        centerY = frameHeight/2;

        Log.i("Dimensions","Width "+frameWidth + " Height " + frameHeight);

        if(!initDone){
           initGame();
            Log.i("INIT"," init done at time : "+startTime.toString());
        }

        // draw frame
        canvas.drawRect(0,0,frameWidth,frameHeight,framePaint);

        canvas.drawLine(0,frameHeight-100,frameWidth,frameHeight-100,startLinePaint);

        //draw barrels
        canvas.drawCircle(barrel1X,barrel1Y, radius, barrelPaint1);
        canvas.drawCircle(barrel2X,barrel2Y, radius, barrelPaint2);
        canvas.drawCircle(barrel3X,barrel3Y, radius, barrelPaint3);

        int check = checkForCollision();
        Log.i("COLLISION","Collision Check "+check);
        // barrel collision detected
        if(check==1){

            // TODO GAME OVER BABY

            //canvas.drawCircle(oldX, oldY, radius, horsePaint);

            gameOver(false);

        }else if(check == 2){ //frame collision detected

            // might need to add some delay over here
            frameCollisionCount++;
            Log.i("FRAMECOLLISION",frameCollisionCount+"");
            framePaint.setColor(Color.RED);
            canvas.drawCircle(oldX, oldY, radius, horsePaint);

            // TODO add 5 secs to timer
        }else if(check == 0){ //no collision detected
            framePaint.setColor(Color.MAGENTA);
            canvas.drawCircle(xSpeed, ySpeed, radius, horsePaint);
            //drawTrail(canvas);

            oldX = xSpeed;
            oldY = ySpeed;
        }

        updateTimer();

        /*if(running)
            invalidate();*/
    }

    public void setParent(GameActivity parent){
        this.parent = parent;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
    public void startGame(){

        initDone = false;
        running = true;

    }

    public void pauseGame(){
        running = false;
        pauseTime = new Date();
    }

    public void resumeGame(){
        running = true;
    }

    public String updateTimer(){
        String timeTaken = "--:--";
        timer = new Date();
        if(running) {
            long diffInMillies = timer.getTime() - startTime.getTime() + frameCollisionCount * 5000;
            long timeInSeconds = diffInMillies / 1000;
            long diffSeconds = diffInMillies / 1000 % 60;
            long diffMinutes = diffInMillies / (60 * 1000) % 60;

            timeElapsedInSeconds = timeInSeconds;

            timeTaken = diffMinutes + " : " + diffSeconds;

            Log.i("TIME", timeTaken);
            parent.setTime(timeTaken, timeInSeconds);
        }

        return timeTaken;
    }
    private void gameOver(boolean finished) {

        String time = updateTimer();
        boolean highScore = false;
        // won game
        if(finished){
            //MainActivity.scoreUtility.addScore();
            if(parent.addScore(timeElapsedInSeconds)==1){
                highScore = true;
            }
        }else{

        }
        running = false;

        Log.i("INIT","player " + playerName + " time " +time + " collisions : " +frameCollisionCount+ "/n at " + timer.toString());
        GameDialog gameDialog=new GameDialog(parent,"restart",time,highScore,finished);
        gameDialog.show();
    }

    public void update(float xValue, float yValue){
        //Log.i("UPDATE"," x and y");
        if(running) {
            //Log.i("RUNNING"," game running ");
            xSpeed += xValue;
            ySpeed += yValue;

            pathX.add(xSpeed);
            pathY.add(ySpeed);

            invalidate();
        }
    }
    public int checkForCollision(){

        if(barrel1Completed){
            barrelPaint1.setColor(Color.GREEN);
        }
        if(barrel2Completed){
            barrelPaint2.setColor(Color.GREEN);
        }
        if(barrel3Completed){
            barrelPaint3.setColor(Color.GREEN);
        }

        int distance1 = (int)Math.sqrt((barrel1X-xSpeed)*(barrel1X-xSpeed) + (barrel1Y-ySpeed)*(barrel1Y-ySpeed));
        int distance2 = (int)Math.sqrt((barrel2X-xSpeed)*(barrel2X-xSpeed) + (barrel2Y-ySpeed)*(barrel2Y-ySpeed));
        int distance3 = (int)Math.sqrt((barrel3X-xSpeed)*(barrel3X-xSpeed) + (barrel3Y-ySpeed)*(barrel3Y-ySpeed));

        if(distance1<=(radius*2)||distance2<=(radius*2)||distance3<=(radius*2)){
            return 1;
        }

        Log.i("DISTANCE","d1 "+ distance1 + " d2 "+ distance2+" d3 "+ distance3);
        boolean frameCollision = false;
        // side is 1,2,3,4 starting from top Clockwise
        int side = -1;
        if (xSpeed+radius + 5> frameWidth) {
            xSpeed=frameWidth-radius;
            frameCollision = true;
            side = 2;
        }
        if (ySpeed+radius + 5> frameHeight){
            ySpeed=frameHeight-radius;
            frameCollision = true;
            side = 3;
        }
        if (xSpeed - 5 < radius){
            xSpeed=radius;
            frameCollision = true;
            side = 4;
        }
        if (ySpeed - 5 < radius){
            ySpeed=radius;
            frameCollision = true;
            side = 1;
        }

        Log.i("SIDE COLLIDED",side+"");
        if(ignoreFrameCollision == true && (new Date().getTime() - frameCollisionStart.getTime()) < 1500){

        }else{
            ignoreFrameCollision = false;
        }

        // TODO return side later to highlight the collided side
        if(frameCollision && ignoreFrameCollision == false){
            frameCollisionStart = new Date();
            ignoreFrameCollision = true;
            return 2;
        }

        checkForCircleCompletion(distance1,distance2,distance3);
        // all barrels circled
        if(barrel1Completed && barrel2Completed && barrel3Completed && (ySpeed > frameHeight - 50))
            gameOver(true);
        return 0;
    }

    public void drawTrail(Canvas canvas){

        // TODO handle list size
        //path.moveTo(oldX,oldY);

        /*for(int i = 0;i<pathX.size();i++){
            path.lineTo(pathX.get(i),pathY.get(i));
            path.moveTo(pathX.get(i),pathY.get(i));
        }*/

        int i = pathX.size()-1;
        if(pathX.size() == 0){
            return;
        }
        path.lineTo(pathX.get(i),pathY.get(i));
        path.moveTo(pathX.get(i),pathY.get(i));

        canvas.drawPath(path, trailPaint);

        //Log.i("PATH","Path X list Size : "+ pathX.size());
    }
    public void checkForCircleCompletion(int dist1,int dist2,int dist3){
        Log.i("DISTANCE","d1 " + dist1 + " d2 "+dist2 + " d3 "+dist3);
        if(dist1 < radius*5 ){
            int flag = 0;
            int flag1 = 0;
            int flag2 = 0;
            double xDiff = barrel1X - xSpeed;
            double yDiff = barrel1Y - ySpeed;
            int angle = (int)Math.toDegrees(Math.atan2(yDiff,xDiff));
            if(angle<0){
                angle = angle + 360;
            }
            for (int i = 0; i < firstBarrel.size(); i++) {
                if(firstBarrel.get(i) == angle){
                    flag = 1;
                }
                if(firstBarrel.get(i) == angle+1){
                    flag1 = 1;
                }
                if(firstBarrel.get(i) == angle-1){
                    flag2 = 1;
                }
            }
            if(flag == 0){
                if(angle >= 0 && angle <= 90){
                    firstQuadrant1++;
                }else if (angle > 90 && angle <= 180){
                    secondQuadrant1++;
                }else if (angle > 180 && angle <=270){
                    thirdQuadrant1++;
                }else if(angle > 270 && angle <= 360){
                    forthQuadrant1++;
                }
                firstBarrel.add(angle);
            }
            if(flag1 == 0){
                if(angle+1 >= 0 && angle+1 <= 90){
                    firstQuadrant1++;
                }else if (angle+1 > 90 && angle+1 <= 180){
                    secondQuadrant1++;
                }else if (angle+1 > 180 && angle+1 <=270){
                    thirdQuadrant1++;
                }else if(angle+1 > 270 && angle+1 <= 360){
                    forthQuadrant1++;
                }
                firstBarrel.add(angle+1);
            }
            if(flag2 == 0){
                if(angle-1 >= 0 && angle-1 <= 90){
                    firstQuadrant1++;
                }else if (angle-1 > 90 && angle-1 <= 180){
                    secondQuadrant1++;
                }else if (angle-1 > 180 && angle-1 <=270){
                    thirdQuadrant1++;
                }else if(angle-1 > 270 && angle-1 <= 360){
                    forthQuadrant1++;
                }
                firstBarrel.add(angle-1);
            }
            if(firstBarrel.size()-1 > 360 || (firstQuadrant1 >= 85 && secondQuadrant1 >= 85 && thirdQuadrant1 >= 85 && forthQuadrant1 >= 85)){
                barrel1Completed = true;
            }
        }else{
            firstBarrel.clear();
            firstQuadrant1 = 0;
            secondQuadrant1 = 0;
            thirdQuadrant1 = 0;
            forthQuadrant1 = 0;
        }

        if(dist2 < radius*5 ){
            int flag = 0;
            int flag1 = 0;
            int flag2 = 0;
            double xDiff = barrel2X - xSpeed;
            double yDiff = barrel2Y - ySpeed;
            int angle = (int)Math.toDegrees(Math.atan2(yDiff,xDiff));
            if(angle<0){
                angle = angle + 360;
            }
            for (int i = 0; i < secondBarrel.size(); i++) {
                if(secondBarrel.get(i) == angle){
                    flag = 1;
                }
                if(secondBarrel.get(i) == angle+1){
                    flag1 = 1;
                }
                if(secondBarrel.get(i) == angle-1){
                    flag2 = 1;
                }
            }
            if(flag == 0){
                if(angle >= 0 && angle <= 90){
                    firstQuadrant2++;
                }else if (angle > 90 && angle <= 180){
                    secondQuadrant2++;
                }else if (angle > 180 && angle <=270){
                    thirdQuadrant2++;
                }else if(angle > 270 && angle <= 360){
                    forthQuadrant2++;
                }
                secondBarrel.add(angle);
            }
            if(flag1 == 0){
                if(angle+1 >= 0 && angle+1 <= 90){
                    firstQuadrant2++;
                }else if (angle+1 > 90 && angle+1 <= 180){
                    secondQuadrant2++;
                }else if (angle+1 > 180 && angle+1 <=270){
                    thirdQuadrant2++;
                }else if(angle+1 > 270 && angle+1 <= 360){
                    forthQuadrant2++;
                }
                secondBarrel.add(angle+1);
            }
            if(flag2 == 0){
                if(angle-1 >= 0 && angle-1 <= 90){
                    firstQuadrant2++;
                }else if (angle-1 > 90 && angle-1 <= 180){
                    secondQuadrant2++;
                }else if (angle-1 > 180 && angle-1 <=270){
                    thirdQuadrant2++;
                }else if(angle-1 > 270 && angle-1 <= 360){
                    forthQuadrant2++;
                }
                secondBarrel.add(angle-1);
            }
            if(secondBarrel.size()-1 > 360 || (firstQuadrant2 >= 85 && secondQuadrant2 >= 85 && thirdQuadrant2 >= 85 && forthQuadrant2 >= 85)){
                barrel2Completed = true;
            }
        }else{
            secondBarrel.clear();
            firstQuadrant2 = 0;
            secondQuadrant2 = 0;
            thirdQuadrant2 = 0;
            forthQuadrant2 = 0;
        }

        if(dist3 < radius*5 ){
            int flag = 0;
            int flag1 = 0;
            int flag2 = 0;
            double xDiff = barrel3X - xSpeed;
            double yDiff = barrel3Y - ySpeed;
            int angle = (int)Math.toDegrees(Math.atan2(yDiff,xDiff));
            if(angle<0){
                angle = angle + 360;
            }
            for (int i = 0; i < thirdBarrel.size(); i++) {
                if(thirdBarrel.get(i) == angle){
                    flag = 1;
                }
                if(thirdBarrel.get(i) == angle+1){
                    flag1 = 1;
                }
                if(thirdBarrel.get(i) == angle-1){
                    flag2 = 1;
                }
            }
            if(flag == 0){
                if(angle >= 0 && angle <= 90){
                    firstQuadrant3++;
                }else if (angle > 90 && angle <= 180){
                    secondQuadrant3++;
                }else if (angle > 180 && angle <=270){
                    thirdQuadrant3++;
                }else if(angle > 270 && angle <= 360){
                    forthQuadrant3++;
                }
                thirdBarrel.add(angle);
            }
            if(flag1 == 0){
                if(angle+1 >= 0 && angle+1 <= 90){
                    firstQuadrant3++;
                }else if (angle+1 > 90 && angle+1 <= 180){
                    secondQuadrant3++;
                }else if (angle+1 > 180 && angle+1 <=270){
                    thirdQuadrant3++;
                }else if(angle+1 > 270 && angle+1 <= 360){
                    forthQuadrant3++;
                }
                thirdBarrel.add(angle+1);
            }
            if(flag2 == 0){
                if(angle-1 >= 0 && angle-1 <= 90){
                    firstQuadrant3++;
                }else if (angle-1 > 90 && angle-1 <= 180){
                    secondQuadrant3++;
                }else if (angle-1 > 180 && angle-1 <=270){
                    thirdQuadrant3++;
                }else if(angle-1 > 270 && angle-1 <= 360){
                    forthQuadrant3++;
                }
                thirdBarrel.add(angle-1);
            }
            if(thirdBarrel.size()-1 > 360 || (firstQuadrant3 >= 85 && secondQuadrant3 >= 85 && thirdQuadrant3 >= 85 && forthQuadrant3 >= 85)){
                barrel3Completed = true;
            }
        }else{
            thirdBarrel.clear();
            firstQuadrant3 = 0;
            secondQuadrant3 = 0;
            thirdQuadrant3 = 0;
            forthQuadrant3 = 0;
        }
    }
}
