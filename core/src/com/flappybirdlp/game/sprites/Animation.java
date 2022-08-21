package com.flappybirdlp.game.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Animation {

    private Array<TextureRegion> frames; //Aquí guardaremos todos nuestros frames
    private float maxFrameTime; //lo máximo de tiempo que se mostrará un frame
    private float currentFrameTime; //tiempo que ha estado el frame actual
    private int frameCount; //cantidad de frames en la animación
    private int frame; //es el frame actual

    public Animation(TextureRegion region, int frameCount, float cycleTime){
        frames = new Array<TextureRegion>();
        int frameWidth = region.getRegionWidth() / frameCount;
        for(int i=0; i<frameCount; i++){
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime/frameCount;
        frame = 0;
    }

    public void update(float dt){
        currentFrameTime += dt;
        if(currentFrameTime > maxFrameTime){
            frame++;
            currentFrameTime=0;
        }
        if(frame >= frameCount){
            frame=0;
        }
    }

    public TextureRegion getFrame(){
        return frames.get(frame);
    }

}
