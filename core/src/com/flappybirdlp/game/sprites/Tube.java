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

    public Tube(float x){
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();

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
        if(result >=50 && result <=52/*(x - posBotTube.x >0) && (x - posBotTube.x <= 2)*/){
            return true;
        }
        return false;
    }

    public void dispose(){
        topTube.dispose();
        bottomTube.dispose();
    }

}
