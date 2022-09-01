package com.flappybirdlp.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.flappybirdlp.game.FlappyBirdLP;
import com.flappybirdlp.game.sprites.Bird;
import com.flappybirdlp.game.sprites.Tube;

import jdk.internal.misc.TerminatingThreadLocal;
import sun.font.TrueTypeFont;

public class PlayState extends Estado{
    private static final int TUBE_SPACING = 137;//135
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;
    private int flag=0, iterator=0;
    private Array<Tube> tubes;
    Preferences prefs = Gdx.app.getPreferences("My Preferences");

    private Bird bird;
    private Texture bg, digit1, digit2, digit3; //background
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Texture retrybtn;
    private Texture homebtn;


    private int val=0, score;
    private int highscore;
    private int hasCrashed; //0 si el pájaro aún no ha chocado con un tubo, 1 si ya chocó con un tubo
    private Sound crash, tubepassed;
    private BitmapFont fontgameover; //COMENTAR A PARTIR DE AQUÍ SI SE VE MAL
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        if (prefs.getInteger("highscore")> 0){
            highscore = prefs.getInteger("highscore");
        }else{
            highscore=0;
        }
        bird = new Bird(30,300);
        camera.setToOrtho(false, FlappyBirdLP.WIDTH/2, FlappyBirdLP.HEIGHT/2);
        bg = new Texture("playbackground.png");
        bird.setStartmoving(0);
        //gr = new Texture("GetReady.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth/2) + ground.getWidth(), GROUND_Y_OFFSET);

        crash = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        tubepassed = Gdx.audio.newSound(Gdx.files.internal("pointsound.mp3"));

        score=0;
        hasCrashed = 0;
        fontgameover = new BitmapFont();
        /*fontScore.setColor(Color.WHITE);
        fontScore.getData().setScale(3);*/

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("flappy-font.ttf"));//"OpenSans-ExtraBold.ttf"
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size=25;
        fontParameter.borderWidth=3;
        //fontParameter.color = Color.WHITE;
        fontParameter.borderColor = Color.BLACK;
        fontgameover = fontGenerator.generateFont(fontParameter);
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
        digit1 = new Texture("0.png");
        digit2 = new Texture("0.png");
        digit3 = new Texture("0.png");
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched()){
            if(hasCrashed==0){
                bird.jump();
            }
            if (showGameOverInRender==1){
                Vector3 tmp= new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                camera.unproject(tmp);
                Rectangle retryBounds=new Rectangle(camera.position.x - (camera.viewportWidth/2) + 30, 110,retrybtn.getWidth(),retrybtn.getHeight());
                if(retryBounds.contains(tmp.x, tmp.y)){ //Si le da al botón, lo lleva al getReady state
                    retrybtn = new Texture("playbtnpressed.png");
                    flag=1;
                }
                Rectangle homeBounds=new Rectangle(camera.position.x - (camera.viewportWidth/2) + 120, 110,homebtn.getWidth(),homebtn.getHeight());
                if(homeBounds.contains(tmp.x, tmp.y)){ //Si le da al botón, lo lleva al getReady state
                    homebtn = new Texture("home_pressed.png");
                    flag=2;
                }
            }

        }
    }

    private int showGameOverInRender = 0;
    @Override
    public void update(float dt) {

        handleInput();
        updateGround();
        bird.update(dt);

        if(hasCrashed==0){
            camera.position.x = bird.getPosition().x + 70;
        }

        if(hasCrashed==0){
            for(Tube tube : tubes){
                if(camera.position.x - (camera.viewportWidth/2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()){
                    val = val + TUBE_SPACING;
                    tube.reposition(val);
                    //tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                }
                if(tube.collides(bird.getBounds())){ //si choca con un tubo

                    crash.play(0.1f);
                    hasCrashed=1;
                    bird.setItsGameOver(1);
                    bird.gameOverFrame();
                    //gsm.set(new PlayState(gsm));
                    break; //borrar si no funciona
                }

                if(tube.isPassed(bird.getPosition())){
                    tubepassed.play(0.1f);
                    score++;
                    if(score>=1 && score<=9){
                        digit1 = new Texture(score+".png");
                    }
                    else if(score>=10 && score<=99){
                        String[] rutas = digitosContador(score);
                        digit1 = new Texture(rutas[0]);
                        digit2 = new Texture(rutas[1]);
                    }
                    else if(score>=100 && score<=999){
                        String[] rutas = digitosContador(score);
                        digit1 = new Texture(rutas[0]);
                        digit2 = new Texture(rutas[1]);
                        digit3 = new Texture(rutas[2]);
                    }
                }

            }
        }

        //Matar al pájaro cuando toque el ground
        if(bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET){
            if(hasCrashed==1 || hasCrashed==0){ //1 indica que ya chocó con un tubo, 0 indica que solo chocó con el piso de un solo
                crash.play(0.1f);
                hasCrashed=2;
            }
            if (score > highscore) {
                prefs.putInteger("highscore", score);
                highscore= score;
                prefs.flush();
            }
            bird.setItsGameOver(1);
            bird.gameOverFrame();
            bird.getPosition().y = ground.getHeight()+GROUND_Y_OFFSET;
            showGameOverInRender = 1;
            //gsm.set(new PlayState(gsm));
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth/2), 0);
        //sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        if(hasCrashed==0){
            sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
            for(Tube tube : tubes){
                sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
                sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
            }
        }
        else{//Para que se dibujen los tubos primero y encima el pájaro
            for(Tube tube : tubes){
                sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
                sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
            }
            sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        }
        //fontScore.draw(sb, String.valueOf(score), camera.position.x - (camera.viewportWidth/2) + 85, 380);
        if(String.valueOf(score).length()==1){
            sb.draw(digit1, camera.position.x - (camera.viewportWidth/2) + 80, 320);
        }
        else if(String.valueOf(score).length()==2){
            sb.draw(digit1, camera.position.x - (camera.viewportWidth/2) + 70, 320);
            sb.draw(digit2, camera.position.x - (camera.viewportWidth/2) + 85, 320);
        }
        else if(String.valueOf(score).length()==3){
            sb.draw(digit1, camera.position.x - (camera.viewportWidth/2) + 65, 320);
            sb.draw(digit2, camera.position.x - (camera.viewportWidth/2) + 80, 320);
            sb.draw(digit3, camera.position.x - (camera.viewportWidth/2) + 97, 320);
        }

        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        if(showGameOverInRender==1){
            fontgameover.setColor(Color.GOLD);
            fontgameover.getData().setScale(1);
            fontgameover.draw(sb, "Game Over", camera.position.x - (camera.viewportWidth/2) + 20, 300);
            fontgameover.setColor(Color.WHITE);
            fontgameover.getData().setScale(0.8f);
            fontgameover.draw(sb, "Best: "+ highscore,camera.position.x - (camera.viewportWidth/2) + 20, 250);
            if (score == highscore && highscore!= 0){
                fontgameover.getData().setScale(0.5f);

                fontgameover.draw(sb, "(NEW)",camera.position.x - (camera.viewportWidth/2) + 130, 255);
                fontgameover.getData().setScale(0.8f);
            }
            fontgameover.draw(sb, "\n\nScore: "+score,camera.position.x - (camera.viewportWidth/2) + 20, 245);

            if(flag!=1){
                retrybtn = resize("playbtn.png", 60, 50);
            }
            else{
                retrybtn = resize("playbtnpressed.png", 60, 50);
            }

            if(flag!=2){
                homebtn = resize("home.png", 60, 50);
            }
            else{
                homebtn = resize("home_pressed.png", 60, 50);
            }
            fontgameover.getData().setScale(0.5f);
            fontgameover.draw(sb, "Retry",camera.position.x - (camera.viewportWidth/2) + 30, 110);
            fontgameover.draw(sb, "Home",camera.position.x - (camera.viewportWidth/2) + 120, 110);
            sb.draw(retrybtn, camera.position.x - (camera.viewportWidth/2) + 20, 120);
            sb.draw(homebtn, camera.position.x - (camera.viewportWidth/2) + 110, 120);

            if(flag ==1){
                iterator++;
                if (iterator>=8){
                    iterator=0;
                    gsm.set(new GetReady(gsm));
                }
            }
            if (flag == 2){
                iterator++;
                if (iterator>=2){
                    iterator=0;
                    gsm.set(new EstadoMenu(gsm));
                }

            }
        }
        sb.end();
    }

    @Override
    public void dispose() {
        fontGenerator.dispose();
        fontgameover.dispose();
        bg.dispose();
        bird.dispose();
        ground.dispose();
        for(Tube tube : tubes){
            tube.dispose();
        }
        tubepassed.dispose();
        crash.dispose();
        digit1.dispose();
        digit2.dispose();
        digit3.dispose();
        retrybtn.dispose();
        homebtn.dispose();

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

    private Texture resize (String png, int x, int y){
        Pixmap sizefull = new Pixmap(Gdx.files.internal(png));
        Pixmap newsize = new Pixmap(x, y, sizefull.getFormat());
        newsize.drawPixmap(sizefull,
                0, 0, sizefull.getWidth(), sizefull.getHeight(),
                0, 0, newsize.getWidth(), newsize.getHeight()
        );
        Texture texture = new Texture(newsize);
        return texture;
    }

    private String[] digitosContador(int numero){
        String[] rutas = new String[String.valueOf(numero).length()];
        String number  = String.valueOf(numero);

        for(int i=0; i<number.length(); i++){
            if(number.charAt(i) == '0'){
                rutas[i] = "0.png";
            }
            else if(number.charAt(i)=='1'){
                rutas[i] = "1.png";
            }
            else if(number.charAt(i)=='2'){
                rutas[i] = "2.png";
            }
            else if(number.charAt(i)=='3'){
                rutas[i] = "3.png";
            }
            else if(number.charAt(i)=='4'){
                rutas[i] = "4.png";
            }
            else if(number.charAt(i)=='5'){
                rutas[i] = "5.png";
            }
            else if(number.charAt(i)=='6'){
                rutas[i] = "6.png";
            }
            else if(number.charAt(i)=='7'){
                rutas[i] = "7.png";
            }
            else if(number.charAt(i)=='8'){
                rutas[i] = "8.png";
            }
            else if(number.charAt(i)=='9'){
                rutas[i] = "9.png";
            }
        }
        return rutas;
    }
}
