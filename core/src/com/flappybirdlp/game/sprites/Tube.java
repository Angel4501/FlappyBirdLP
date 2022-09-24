package com.flappybirdlp.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class Tube {
    public static final int TUBE_WIDTH=52;

    private static final int FLUCTUATION = 130;//130
    private static final int TUBE_GAP = 80;//80
    private static final int LOWEST_OPENING = 120;
    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Rectangle boundsTop, boundsBot; //para detectar la colisión
    private Random rand;
    private int score;

    public Tube(float x, int score, int r){
        topTube = new Texture("toptube"+r+".png");
        bottomTube = new Texture("bottomtube"+r+".png");
        rand = new Random();
        this.score=score;

        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight());
        boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth(), bottomTube.getHeight());
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public int getScore(){return score;}

    public void setScore (int score){ this.score=score;}

    public void reposition(float x){
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + LOWEST_OPENING + TUBE_GAP);
        posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight());

        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBotTube.x, posBotTube.y);
    }

    public boolean collides(Rectangle player){
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public boolean isPassed(Vector3 birdPosition){ //Si se pasa en medio de dos tubos, debe hacer el sonido y aumentar al contador
        float x = birdPosition.x;
        float result = x - posBotTube.x;
        float result2 = x - posTopTube.x;
        if((result >52) && (result2>52)){
            return true;
        }
        return false;
    }

    public void dispose(){
        topTube.dispose();
        bottomTube.dispose();
    }

}
