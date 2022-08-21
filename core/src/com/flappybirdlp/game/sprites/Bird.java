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

    public Bird(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        //bird = new Texture("bird.png");
        texture = new Texture("birdanimation.png");
        birdAnimation = new Animation(new TextureRegion(texture), 3, 0.5f);
        bounds = new Rectangle(x, y, texture.getWidth() / 3, texture.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("wingsound.mp3"));//sfx_wing.ogg
    }

    public void update(float dt){
        if(startmoving==0){
            birdAnimation.update(dt);
        }

        if(position.y>0){
            velocity.add(0, GRAVITY, 0);
        }
        velocity.scl(dt);

        position.add(MOVEMENT*dt, velocity.y, 0);
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

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return birdAnimation.getFrame();

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

    public Rectangle getBounds(){
        return bounds;
    }

    public void dispose(){
        texture.dispose();
        flap.dispose();
    }
}
