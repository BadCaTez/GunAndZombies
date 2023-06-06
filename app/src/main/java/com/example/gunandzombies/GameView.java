package com.example.gunandzombies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

public class GameView extends SurfaceView implements Runnable, View.OnTouchListener {

    volatile boolean plaing;
    private Thread gameThread = null;

    private Player player;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    float xTouchUser;
    float yTouchUser;

    Display display;
    WindowManager windowManager;
    int widthDisplay;
    int heightDisplay;
    Context contextGame;

    Zombie[] zombieArray = new Zombie[150];
    Bullet[] bulletsArray = new Bullet[150];
    Barricade mainBarricade;

    int[] spritePlayer;
    int[] spriteZombie;
    int[] spriteBullet;


    private int xMovePlayer;
    private int tick;
    private int cycleTick = 0;
    boolean isMove;

    private int spawnChance = 0;

    public GameView(Context context) {
        super(context);
        contextGame = context;

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        widthDisplay = display.getWidth();
        heightDisplay = display.getHeight();


       spritePlayer = new int[]{R.drawable.playermove_left1, R.drawable.playermove_left2,
               R.drawable.playermove_left3, R.drawable.playermove_left4, R.drawable.playermove_left5,
               R.drawable.playermove_right1, R.drawable.playermove_right2, R.drawable.playermove_right3,
               R.drawable.playermove_right4, R.drawable.playermove_right5, R.drawable.player_fire1,
               R.drawable.player_fire2};

       spriteZombie = new int[]{R.drawable.skeleton_move1, R.drawable.skeleton_move2,
               R.drawable.skeleton_move3, R.drawable.skeleton_move4, R.drawable.skeleton_move5};

       spriteBullet = new int[]{R.drawable.bullet_fire1, R.drawable.bullet_fire2};

        player = new Player(context, widthDisplay / 2, heightDisplay - 350,
                88, 180, spritePlayer[10], spritePlayer);
        player.setSpeed(1.5);


        mainBarricade = new Barricade(contextGame, widthDisplay / 2 - 1080, heightDisplay - 460,
                2250,110, R.drawable.barricade, null);
        mainBarricade.setHP(1000);

        setOnTouchListener(this);

        surfaceHolder = getHolder();
        paint = new Paint();

        tick = 0;
    }

    @Override
    public void run() {
        while(plaing){
            update();
            draw();
            control();
        }
    }

    private void update(){
        movePlayer(isMove ,xMovePlayer);
        spawnZombie();
        spawnBullet();
        checkBullet();
        checkBarricade();
        moveZombie();

        if(tick < 100){tick = tick + 1;}
        else {
            cycleTick = cycleTick + 1;
            tick = 0;}
    }

    private void draw(){
        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);

            if(mainBarricade != null) {
                canvas.drawBitmap(
                        mainBarricade.getSprite(),
                        mainBarricade.getX(),
                        mainBarricade.getY(),
                        paint);
            }

            canvas.drawBitmap(
                    player.getSprite(),
                    player.getX(),
                    player.getY(),
                    paint);

            for (Zombie zombie : zombieArray) {
                if (zombie != null) {
                    canvas.drawBitmap(
                            zombie.getSprite(),
                            zombie.getX(),
                            zombie.getY(),
                            paint);
                }
            }

            for (Bullet bullet : bulletsArray) {
                if (bullet != null) {
                    canvas.drawBitmap(
                            bullet.getSprite(),
                            bullet.getX(),
                            bullet.getY(),
                            paint);
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control(){
     try{ gameThread.sleep(12);}
     catch (InterruptedException e){ e .printStackTrace();}
    }

    public void pause() {
        plaing = false;
        try{gameThread.join();}
        catch (InterruptedException e ){}
    }

    public void resume() {
        plaing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event){
        xTouchUser = event.getX();
        yTouchUser = event.getY();
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    isMove = true;
                    xMovePlayer = sideMoveX(xTouchUser);
                    break;
                case MotionEvent.ACTION_UP:
                    isMove = false;
                    break;
            }
        return true;
    }

    private void movePlayer(boolean isMove, int x){
        if(isMove){player.setCoordinates(x, 0, tick);}
        else {player.setCoordinates(0,0, tick);}
    }

    private int sideMoveX(float xTouch){
        if((widthDisplay / 2) < xTouch){return 5;}
        else {return -5;}
    }

    private void spawnZombie(){
        boolean spawnOn = true;

        if(tick % (40 - spawnChance) == 0) {
            for(int i = 0; i < zombieArray.length; i++){
                if(spawnOn && zombieArray[i] == null) {
                    int x = (int) (Math.random() * 1500 + 250);
                    zombieArray[i] = new Zombie(contextGame, x, 0, 100, 150,
                            spriteZombie[1], spriteZombie);
                    spawnOn = false;
                }
            }

            if(spawnChance < 30){
                spawnChance = (int) (0.1 * cycleTick);
            }
        }
    }

    private void moveZombie(){
        for (Zombie zombie : zombieArray) {
            if (zombie != null) {
                zombie.setCoordinates(0,5,tick);
            }
        }
    }

    private void spawnBullet (){
        boolean fireOn = true;

        if(tick % 20 == 0 && !isMove){
            for (int i = 0; i < bulletsArray.length; i++){
                if(fireOn && bulletsArray[i] == null) {
                    bulletsArray[i] = new Bullet(contextGame,
                            player.getX(), (player.getY() + 10), 10, 50,
                            spriteBullet[0], spriteBullet);
                    fireOn = false;
                }
            }

        }
    }

    private void checkBullet (){
        for(int i = 0; i < bulletsArray.length; i++){

            if(bulletsArray[i] != null) {
                bulletsArray[i].setCoordinates(0, -5, tick);

                for (int j = 0; j < zombieArray.length; j++) {

                    if(zombieArray[j] != null) {

                        if (Rect.intersects(bulletsArray[i].getDetectCollision(),
                                zombieArray[j].getDetectCollision())) {
                            zombieArray[j] = null;
                        }

                    }

                }

                if (bulletsArray[i].getY() <= -10) {
                    bulletsArray[i] = null;
                }

            }

        }
    }

    private void checkBarricade(){

        if (mainBarricade != null) {
            for (Zombie zombie : zombieArray) {
                if (zombie != null) {
                    if (Rect.intersects(zombie.getDetectCollision(),
                            mainBarricade.getDetectCollision())) {
                        if (mainBarricade.getHP() > 0 && tick % 5 == 0) {
                            mainBarricade.setHP(-1);
                            zombie.setSpeed(0);
                        }  else if(mainBarricade.getHP() <= 0) {zombie.setSpeed(1);}
                    }
                }
            }
            if(mainBarricade.getHP() <= 0){mainBarricade = null;}
        }

    }

}
