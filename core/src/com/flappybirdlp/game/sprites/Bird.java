package com.flappybirdlp.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.util.Vector;

public class Bird {
    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;

    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation birdAnimation;
    private Texture texture;
    private Texture bird;
    private int startmoving = 0; //CAMBIARLO A 0;
    private Sound flap;

    public void setStartmoving(int startmoving) {
        this.startmoving = startmoving;
    }

    public Bird(int x, int y, String path){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        setAnimation(x, y, path);
        //bird = new Texture("bird.png");
        //sfx_wing.ogg
    }

    private int itsGameOver = 0; //flag
    public void update(float dt){
        if(startmoving==0){
            birdAnimation.update(dt);
        }

        if(position.y>0){
            velocity.add(0, GRAVITY, 0);
        }
        velocity.scl(dt);

        if(itsGameOver==0){ //0 significa falso, entonces NO es game over todavía
            position.add(MOVEMENT*dt, velocity.y, 0);
        }
        else{ //Ya es game over
            position.add(0, velocity.y, 0);
        }
        /*if(movflag==0){
            position.add(0, velocity.y, 0); //position.add(MOVEMENT*dt, velocity.y, 0);
        }
        else{
            position.add(MOVEMENT*dt, velocity.y, 0);
        }*/
        if(position.y<startmoving){
            position.y=startmoving;
        }
        velocity.scl(1/dt);
        bounds.setPosition(position.x, position.y);
    }

    public void setItsGameOver(int itsGameOver){
        this.itsGameOver = itsGameOver;
    }

    public void gameOverFrame(){
        birdAnimation.setFrameCount(1);
    }

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return birdAnimation.getFrame();

    }

    public void setAnimation(float x, float y, String path){
        texture = new Texture(path);
        birdAnimation = new Animation(new TextureRegion(texture), 3, 0.5f);
        bounds = new Rectangle(x, y, 32, 24);
        flap = Gdx.audio.newSound(Gdx.files.internal("wingsound.mp3"));
    }

    public Texture TextureForGetReady(){ //Que retorne
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void jump(){
        flap.play(0.1f);
        velocity.y = 250;
    }

    public void getFlapSound(){
        flap.play(0.1f);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void dispose(){
        texture.dispose();
        flap.dispose();
    }
}
