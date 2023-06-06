package com.example.gunandzombies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Entity  {

    Bitmap asset;
    Bitmap sprite;
    int idSprite;
    int[] animSprite;

    int widthSprite;
    int heightSprite;

    int x;
    int y;
    double speed;
    int hp;
    Context context;

    public Rect detectCollision;

    Entity(Context contextGame, int xSet, int ySet, int wSet,
           int hSet, int nameSprite, int[] animArray) {

        this.x = xSet;
        this.y = ySet;
        this.speed = 1;
        this.hp = -1;

        this.context = contextGame;
        this.widthSprite = wSet;
        this.heightSprite = hSet;
        this.idSprite = nameSprite;
        this.animSprite = animArray;

        asset = BitmapFactory.decodeResource(context.getResources(), idSprite);
        sprite = Bitmap.createScaledBitmap(asset, widthSprite,heightSprite, false);

        setDetectCollision();
        setCollision();
    }

    public void setCoordinates(int setX, int setY, int tick){
        setSprite(setX, setY, tick);

        x = (int) (x + (setX * speed));
        y = (int) (y + (setY * speed));

        setCollision();
    }

    public void setDetectCollision(){
        detectCollision = new Rect(x, y, sprite.getWidth(), sprite.getHeight());
    }

    public void setCollision() {
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + sprite.getWidth();
        detectCollision.bottom = y + sprite.getHeight();
    }

    int stageMoveAnim = 0;

   public void setSprite(int setX, int setY, int tick){

   }

    public int getX (){return x;}
    public int getY (){return y;}
    public Bitmap getSprite (){return sprite;}
    public Rect getDetectCollision(){return detectCollision;}
    public int getHP (){return hp;}

    public void setSpeed (double setSpeed){speed = setSpeed;}
    public void setHP (int setHp){ hp += setHp;}
}

class Player extends Entity {

    Player(Context contextGame, int xSet, int ySet, int wSet,
           int hSet, int nameSprite, int[] animArray){
        super(contextGame, xSet, ySet, wSet, hSet,
                nameSprite, animArray);

    }

   @Override
   public void setSprite(int setX, int setY, int tick) {
       super.setSprite(setX, setY, tick);

                if(tick % 10 == 0) {

                    if (x > (x + setX)) {
                        asset = BitmapFactory.decodeResource(context.getResources(),
                                animSprite[stageMoveAnim]);
                        sprite = Bitmap.createScaledBitmap(asset, widthSprite, heightSprite, false);
                        stageMoveAnim++;

                    } else if (x < (x + setX)) {
                        asset = BitmapFactory.decodeResource(context.getResources(),
                                animSprite[stageMoveAnim + 5]);
                        sprite = Bitmap.createScaledBitmap(asset, widthSprite, heightSprite, false);
                        stageMoveAnim++;

                    } else {
                        asset = BitmapFactory.decodeResource(context.getResources(), idSprite);
                        sprite = Bitmap.createScaledBitmap(asset, widthSprite, heightSprite, false);
                        stageMoveAnim = 0;
                    }

                    if (stageMoveAnim >= 5) {stageMoveAnim = 0;}

                }

   }

}

class Zombie extends Entity {

    Zombie(Context contextGame, int xSet, int ySet, int wSet,
           int hSet, int nameSprite, int[] animArray) {
        super(contextGame, xSet, ySet, wSet, hSet, nameSprite, animArray);
    }

    public void setSprite(int setX, int setY, int tick) {

        if (tick % 10 == 0) {

                asset = BitmapFactory.decodeResource(context.getResources(),
                        animSprite[stageMoveAnim]);
                sprite = Bitmap.createScaledBitmap(asset, widthSprite, heightSprite, false);
                stageMoveAnim++;
            if (stageMoveAnim >= 5) {stageMoveAnim = 0;}

        }
    }
}

class Bullet extends Entity {

    Bullet(Context contextGame, int xSet, int ySet, int wSet,
           int hSet, int nameSprite, int[] animArray){
        super(contextGame, xSet, ySet, wSet, hSet, nameSprite, animArray);
    }

    public void setSprite(int setX, int setY, int tick) {

        if(tick % 6 == 0) {
            asset = BitmapFactory.decodeResource(context.getResources(),
                    animSprite[stageMoveAnim]);
            sprite = Bitmap.createScaledBitmap(asset, widthSprite, heightSprite, false);
            stageMoveAnim++;
            if(stageMoveAnim >= 2){stageMoveAnim = 0;}
        }

    }
}

class Barricade extends Entity{

    Barricade(Context contextGame, int xSet, int ySet, int wSet,
              int hSet, int nameSprite, int[] animArray){
        super(contextGame, xSet, ySet, wSet, hSet, nameSprite, animArray);

    }

    @Override
    public void setDetectCollision() {
        super.setDetectCollision();


        detectCollision = new Rect(x, y-1000, sprite.getWidth(), sprite.getHeight()-1000);
    }
}