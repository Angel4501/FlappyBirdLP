package com.flappybirdlp.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.flappybirdlp.game.FlappyBirdLP;
import com.flappybirdlp.game.sprites.Bird;

public class GetReady extends Estado{
    private static final int GROUND_Y_OFFSET = -50;
    private Bird bird;
    private Texture bg; //background
    private Texture gr, tap; //get ready title
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private int cont=0, i=0;
    private long startTime=0;

    public GetReady(GameStateManager gsm) {
        super(gsm);
        //EstadoMenu.music.stop();
        //FlappyBirdLP.music.play();
        //startTime = System.currentTimeMillis();
        bird = new Bird(33,300);
        camera.setToOrtho(false, FlappyBirdLP.WIDTH/2, FlappyBirdLP.HEIGHT/2);
        bg = new Texture("playbackground.png");
        gr = new Texture("GetReady.png");
        tap = new Texture("GetReadyTap.png");
        bird.setStartmoving(300); //Para que no se caiga el pájaro
        bird.setTexture(new Texture("birddown.png"));
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth/2) + ground.getWidth(), GROUND_Y_OFFSET);
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            bird.getFlapSound();
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);

        camera.position.x = bird.getPosition().x + 70;
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth/2), 0);
        sb.draw(gr, camera.position.x - (camera.viewportWidth/2)+50, 350);
        sb.draw(tap, camera.position.x - (camera.viewportWidth/2)+60, 170);
        //sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);//BORRAR?

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);
        i++;
        if(i>=20){
            sb.draw(bird.TextureForGetReady(), bird.getPosition().x, bird.getPosition().y);
            bird.setTexture(new Texture("birddown.png"));
            gr = new Texture("GetReady2.png");
            if(i>=40){
                i = 0;
            }
        }
        else{
            sb.draw(bird.TextureForGetReady(), bird.getPosition().x, bird.getPosition().y-2);
            bird.setTexture(new Texture("birdup.png"));
            gr = new Texture("GetReady.png");
        }
        sb.end();
    }

    @Override
    public void dispose() {
        //bird.dispose();
        bird.TextureForGetReady().dispose();
        bg.dispose();
        gr.dispose();
        tap.dispose();
        ground.dispose();
        //System.out.println("Get Ready State disposed");
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
