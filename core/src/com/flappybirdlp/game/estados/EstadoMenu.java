package com.flappybirdlp.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.flappybirdlp.game.FlappyBirdLP;

public class EstadoMenu extends Estado{
    private Texture background;
    private Texture playBtn;
    private Texture startTitle;
    private Texture birdFirstScreen;
    private int cont=3, i=0;
    private int[] array = {30, 25, 35};
    public static Music music;

    public EstadoMenu(GameStateManager gsm) {
        super(gsm);
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
        //camera.setToOrtho(false, FlappyBirdLP.WIDTH/2, FlappyBirdLP.HEIGHT/2);
        camera.setToOrtho(false, FlappyBirdLP.WIDTH, FlappyBirdLP.HEIGHT);
        background = new Texture("background.png");
        playBtn = new Texture("playbtn.png");
        startTitle = new Texture("FlappyBirdLogo.png");
        birdFirstScreen = new Texture("bird2.png");
    }

    private int flag=0, iterator=0;
    @Override
    public void handleInput() {

        if(Gdx.input.isTouched()){
            Vector3 tmp= new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(tmp);
            Rectangle textureBounds=new Rectangle((FlappyBirdLP.WIDTH/2) - (playBtn.getWidth()/2), FlappyBirdLP.HEIGHT/2 - 150,playBtn.getWidth(),playBtn.getHeight());
            if(textureBounds.contains(tmp.x, tmp.y)){ //Si le da al botón, lo lleva al getReady state
                playBtn = new Texture("playbtnpressed.png");
                flag=1;
                //changeState();
            }

        }
    }

    public void changeState(){
        gsm.set(new GetReady(gsm));
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(background, 0,0, FlappyBirdLP.WIDTH, FlappyBirdLP.HEIGHT);
        //sb.draw(background, 0, 0);

        sb.draw(startTitle, (FlappyBirdLP.WIDTH/2) - (startTitle.getWidth()/2), FlappyBirdLP.HEIGHT/2 + 130);
        sb.draw(birdFirstScreen, (FlappyBirdLP.WIDTH/2) - (birdFirstScreen.getWidth()/2), FlappyBirdLP.HEIGHT/2 + array[i]);
        try {
            sb.draw(birdFirstScreen, (FlappyBirdLP.WIDTH/2) - (birdFirstScreen.getWidth()/2), FlappyBirdLP.HEIGHT/2 + array[i]);
            i++;
            if(i==3){
                i=0;
            }
            Thread.sleep(100);
            if(cont==4){
                cont=1;
            }
            birdFirstScreen = new Texture("bird"+cont+".png");
            cont++;
            //System.out.println("hello");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sb.draw(playBtn, (FlappyBirdLP.WIDTH/2) - (playBtn.getWidth()/2), FlappyBirdLP.HEIGHT/2 - 150); //FlappyBirdLP.HEIGHT/2 - 150);

        if(flag==1){
            iterator++;
            if(iterator>=2){
                changeState();
            }
        }
        //sb.draw(playBtn, camera.position.x - playBtn.getWidth()/2, camera.position.y);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        birdFirstScreen.dispose();
        startTitle.dispose();
        //System.out.println("Menu State disposed");
    }
}
