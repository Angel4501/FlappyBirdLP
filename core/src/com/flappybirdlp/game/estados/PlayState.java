package com.flappybirdlp.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.flappybirdlp.game.FlappyBirdLP;
import com.flappybirdlp.game.sprites.Bird;
import com.flappybirdlp.game.sprites.Tube;

import jdk.internal.misc.TerminatingThreadLocal;

public class PlayState extends Estado{
    private static final int TUBE_SPACING = 135;//125
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;
    private Array<Tube> tubes;

    private Bird bird;
    private Texture bg; //background
    private Texture ground;
    private Vector2 groundPos1, groundPos2;

    private int val=0;
    private Sound crash;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(30,300);
        camera.setToOrtho(false, FlappyBirdLP.WIDTH/2, FlappyBirdLP.HEIGHT/2);
        bg = new Texture("playbackground.png");
        bird.setStartmoving(0);
        //gr = new Texture("GetReady.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth/2) + ground.getWidth(), GROUND_Y_OFFSET);

        crash = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));

        tubes = new Array<Tube>();
        val=0;
        for(int i=1; i<=TUBE_COUNT; i++){
            if(i==1){
                val=i*300;
                tubes.add(new Tube(val));
            }
            else{
                //tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
                val += TUBE_SPACING;
                tubes.add(new Tube(val));
            }

            //tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);

        camera.position.x = bird.getPosition().x + 70;

        for(Tube tube : tubes){
            if(camera.position.x - (camera.viewportWidth/2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()){
                val = val + TUBE_SPACING;
                tube.reposition(val);
                //tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }
            if(tube.collides(bird.getBounds())){

                crash.play(0.1f);
                gsm.set(new PlayState(gsm));
                break; //borrar si no funciona
            }
        }

        //Matar al pájaro cuando toque el ground
        if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET){
            crash.play(0.1f);
            gsm.set(new PlayState(gsm));
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth/2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);

        for(Tube tube : tubes){
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        for(Tube tube : tubes){
            tube.dispose();
        }
        //System.out.println("Play state disposed");
    }

    private void updateGround(){
        if(camera.position.x - (camera.viewportWidth/2) > groundPos1.x + ground.getWidth()){
            groundPos1.add(ground.getWidth()*2, 0);
        }
        if(camera.position.x - (camera.viewportWidth/2) > groundPos2.x + ground.getWidth()){
            groundPos2.add(ground.getWidth()*2, 0);
        }
    }
}
