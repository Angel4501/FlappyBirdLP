package com.flappybirdlp.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.flappybirdlp.game.FlappyBirdLP;
import com.flappybirdlp.game.sprites.Bird;
import com.flappybirdlp.game.sprites.Tube;

import java.util.Random;

public class PlayState extends Estado{
    private static final int TUBE_SPACING = 137;//135
    private static final int TUBE_COUNT = 4;
    private static final int GROUND_Y_OFFSET = -50;
    private int flag=0, iterator=0, hi_flag, has_hammer =0, limitplace=5;
    private float powerup_margin=0;
    private Array<Tube> tubes;
    private int fps = 60; //limit to 60 fps
    Preferences prefs = Gdx.app.getPreferences("My Preferences");

    private Bird bird;
    private Texture bg, digit1, digit2, digit3; //background
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Texture retrybtn;
    private Texture homebtn;
    private Texture hammer;
    private Texture phantom;
    private Texture block;
    private int r;
    private int randopower;
    private float powerpos=0;
    private boolean has_power=true;
    private boolean isPhantom=false;
    private int phantomcount;

    private int val=0, score=0;
    private int highscore;
    private int hasCrashed; //0 si el pájaro aún no ha chocado con un tubo, 1 si ya chocó con un tubo
    private Sound crash, tubepassed, powerupsound, blockhit, gameover;
    private BitmapFont fontgameover; //COMENTAR A PARTIR DE AQUÍ SI SE VE MAL
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    private Rectangle hammerbound;
    private Rectangle blockbound;
    private Rectangle phantombound;
    private static Random rand;
    private static int max, min;
    private int limits, blocklimit;
    private int dynamicpowerupflag;
    //private int position;

    public PlayState(GameStateManager gsm, int r) {
        super(gsm);
        if (prefs.getInteger("highscore")> 0){
            highscore = prefs.getInteger("highscore");
        }else{
            highscore=0;
        }
        bird = new Bird(30,300, "birdanimation.png");
        camera.setToOrtho(false, FlappyBirdLP.WIDTH/2, FlappyBirdLP.HEIGHT/2);
        bg = new Texture("playbackground"+r+".png");
        bird.setStartmoving(0);
        bird.jump();
        //gr = new Texture("GetReady.png");
        ground = new Texture("ground"+r+".png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth/2, GROUND_Y_OFFSET);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth/2) + ground.getWidth(), GROUND_Y_OFFSET);

        crash = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        tubepassed = Gdx.audio.newSound(Gdx.files.internal("pointsound.mp3"));
        powerupsound = Gdx.audio.newSound(Gdx.files.internal("powerupsound.mp3"));
        blockhit = Gdx.audio.newSound(Gdx.files.internal("block_hit.mp3"));
        gameover = Gdx.audio.newSound(Gdx.files.internal("gameover.mp3"));

        hi_flag=0;
        hasCrashed = 0;
        fontgameover = new BitmapFont();
        /*fontScore.setColor(Color.WHITE);
        fontScore.getData().setScale(3);*/
        //POWERUP
        hammer = resize("hammer.png", 30, 30);
        hammerbound = new Rectangle(0,0,0,0);
        block = resize("block.png", 52, 80);
        blockbound = new Rectangle(0,0,0,0);
        phantom = new Texture("phantom.png");
        phantombound = new Rectangle(0,0,0,0);

        rand = new Random();
        min = 5; max = 7;
        //min = randomNumber();//BORRAR
        limits=randomNumber();
        powerup_margin= rand.nextInt(11)-5;
        blocklimit= limits+2;
        randopower = rand.nextInt(2)+1;

        font_color(Color.WHITE, Color.BLACK);
        tubes = new Array<Tube>();
        val=0;
        for(int i=1; i<=TUBE_COUNT; i++){
            if(i==1){
                val=i*350; //i*300;
                tubes.add(new Tube(val,10, r));
            }
            else{
                //tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
                val += TUBE_SPACING;
                tubes.add(new Tube(val,10, r));
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
                if(homeBounds.contains(tmp.x, tmp.y)){ //Si le da al botón, lo lleva a home
                    homebtn = new Texture("home_pressed.png");
                    flag=2;
                }
            }

        }
    }

    private int showGameOverInRender = 0;
    private int flagforpowerupsound = 0;
    private int values(){
        //Random random = new Random();
        int r = rand.nextInt(3)+1;//entre 0 y 2
        switch (r){
            case 1:
                if(mod(score,5)>=0 && mod(score,5)<=3 && score!=0){
                    if(mod(limitplace, 5)==1){
                        limitplace--;
                    }
                    if(mod(limitplace, 5)==2){
                        limitplace=limitplace-2;
                    }
                    return limitplace;
                }
                break;
            case 2:
                if(mod(score,5)>=0 && mod(score,5)<=3 && score!=0){
                    if(mod(limitplace, 5)==0){
                        limitplace++;
                    }
                    if(mod(limitplace, 5)==2){
                        limitplace--;
                    }
                    return limitplace;
                }
                break;
            case 3:
                if(mod(score,5)>=0 && mod(score,5)<=3 && score!=0){
                    if(mod(limitplace, 5)==0){
                        limitplace=limitplace+2;
                    }
                    if(mod(limitplace, 5)==1){
                        limitplace++;
                    }
                    return limitplace;
                }
                break;
            default:
                break;
        }
        return 0;
    }

    @Override
    public void update(float dt) {

        handleInput();
        updateGround();
        bird.update(dt);

        if(hasCrashed==0){
            camera.position.x = bird.getPosition().x + 70;
        }

        if(hasCrashed==0){//pajaro esta vivo
            for(Tube tube : tubes){
                if(camera.position.x - (camera.viewportWidth/2) > tube.getPosTopTube().x + tube.getTopTube().getWidth()){
                    val = val + TUBE_SPACING;
                    tube.reposition(val);
                    //tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
                }
                if(tube.collides(bird.getBounds()) && isPhantom==false){ //si choca con un tubo
                    crash.play(0.1f);
                    hasCrashed=1;
                    bird.setItsGameOver(1);
                    bird.gameOverFrame();
                    break; //borrar si no funciona
                }
                if(bird.getPosition().y > camera.viewportHeight){ //SI INTENTA IRSE POR ARRIBA
                    if(bird.getPosition().x >= tube.getPosTopTube().x){ //si choca con un tubo en la parte de arriba de la pantalla
                        crash.play(0.1f);
                        hasCrashed=1;
                        bird.setItsGameOver(1);
                        bird.gameOverFrame();
                        break; //borrar si no funciona
                    }
                }
                if (powerpos+1==score && has_power==false){//actualizar posicion de powerup aun cunado ne se agarra uno
                    powerpos=0;
                    limitplace=limitplace+5;
                    limits = values();
                }

                if(getsHammer(bird.getBounds())){ //Si toca el martillo
                    has_hammer =1;
                    has_power=true;
                    if(flagforpowerupsound==0){
                        powerupsound.play(0.1f);
                        flagforpowerupsound=1;
                        bird.setAnimation(bird.getPosition().x, bird.getPosition().y,"bird_hammer_animation.png");
                    }
                    hammerbound = new Rectangle(0,0,0,0);
                    blocklimit= limits+1;
                    limitplace=limitplace+5;
                    limits = values(); //limits=10;
                    powerup_margin= rand.nextInt(11)-5;

                    dynamicpowerupflag=0;
                    break;
                }

                if(getsPhantom(bird.getBounds())){ //Si toca el phantom
                    has_power=true;
                    isPhantom=true;
                    phantomcount=3;
                    if(flagforpowerupsound==0){
                        powerupsound.play(0.1f);
                        flagforpowerupsound=1;
                        bird.setAnimation(bird.getPosition().x, bird.getPosition().y,"phantom_animation.png");
                    }
                    blocklimit= 0;
                    phantombound = new Rectangle(0,0,0,0);
                    limitplace=limitplace+5;
                    limits = values(); //limits=10;
                    powerup_margin= rand.nextInt(11)-5;
                    dynamicpowerupflag=0;

                    randopower = rand.nextInt(2)+1;
                    break;
                }

                if (phantomcount==0 && isPhantom){//cuanda se acaba el contador del powerup phantom
                    isPhantom=false;
                    bird.setAnimation(bird.getPosition().x, bird.getPosition().y,"birdanimation.png");
                }

                if(touchblock(bird.getBounds())){//cuando toca un bloque
                    if (has_hammer ==0){//si tiene martillo
                        crash.play(0.1f);
                        hasCrashed=1;
                        bird.setItsGameOver(1);
                        bird.gameOverFrame();
                        break;
                    }else{//si no tiene martillo
                        blockhit.play(0.1f);
                        blocklimit= limits+1;
                        blockbound= new Rectangle(0,0,0,0);
                        has_hammer =0;
                        randopower = rand.nextInt(2)+1;
                        bird.setAnimation(bird.getPosition().x, bird.getPosition().y,"birdanimation.png");

                    }
                }

                if(tube.isPassed(bird.getPosition())){//aumentar score cuando pasa tubo y actualizar contador visual
                    if(tube.getScore()!=score){
                        tubepassed.play(0.1f);
                        score++;
                        tube.setScore(score);
                        phantomcount--;
                    }
                    flagforpowerupsound=0;
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
            //flagforpowerupsound=0;
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
                hi_flag = 1;
                prefs.flush();
            }
            if(flagforpowerupsound==0){
                gameover.play(0.1f);
                flagforpowerupsound=1;
            }
            bird.setItsGameOver(1);
            bird.gameOverFrame();
            bird.getPosition().y = ground.getHeight()+GROUND_Y_OFFSET;
            showGameOverInRender = 1;
            //gsm.set(new PlayState(gsm));
        }
        camera.update();
    }



    //private int limits = 0, dynamicpowerupflag=0; //limit 10 es get(3), limit 11 es get(0), limit 12 es get(1)
    @Override                                     //limit 5 es get(2), limit 6 es get(3), limit 7 es get(0)
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth/2), 0);
        //sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        if(hasCrashed==0){

            for(Tube tube : tubes){
                sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
                sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);

                PowerupPlacement(sb);//aqui poner powerups
            }
            sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        }
        else{//Para que se dibujen los tubos primero y encima el pájaro
            for(Tube tube : tubes){
                sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
                sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);

                PowerupPlacement(sb);//para que los powerups no desaparezcan de la nada
            }
            sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        }
        if(String.valueOf(score).length()==1){//posicion del contador
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

        if(showGameOverInRender==1){//aqui se desplegara la pantalla game over
            fontgameover.setColor(Color.GOLD);
            fontgameover.getData().setScale(1);
            fontgameover.draw(sb, "Game Over", camera.position.x - (camera.viewportWidth/2) + 20, 300);
            fontgameover.setColor(Color.WHITE);
            fontgameover.getData().setScale(0.8f);

            if (hi_flag == 1){
                fontgameover.draw(sb, "Best: "+ highscore,camera.position.x - (camera.viewportWidth/2) + 75, 250);
                font_color(Color.RED, Color.BLACK);
                fontgameover.getData().setScale(0.8f);
                fontgameover.draw(sb, "NEW",camera.position.x - (camera.viewportWidth/2) + 20, 250);
                font_color(Color.WHITE, Color.BLACK);
                fontgameover.getData().setScale(0.8f);
            }
            else{
                fontgameover.draw(sb, "Best: "+ highscore,camera.position.x - (camera.viewportWidth/2) + 20, 250);
            }
            fontgameover.getData().setScale(0.8f);
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
        sleep(fps);
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
        hammer.dispose();
        powerupsound.dispose();
        block.dispose();
        blockhit.dispose();
        phantom.dispose();
        gameover.dispose();
        //homebtn.dispose();
        //System.out.println("Play state disposed");
    }

    private void updateGround(){//actualiza el piso para que sea infinito
        if(camera.position.x - (camera.viewportWidth/2) > groundPos1.x + ground.getWidth()){
            groundPos1.add(ground.getWidth()*2, 0);
        }
        if(camera.position.x - (camera.viewportWidth/2) > groundPos2.x + ground.getWidth()){
            groundPos2.add(ground.getWidth()*2, 0);
        }
    }

    private void font_color (Color text, Color border){// cambiar color de font y borde
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("flappy-font.ttf"));//"OpenSans-ExtraBold.ttf"
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size=25;
        fontParameter.borderWidth=3;
        fontParameter.borderColor = border;
        fontParameter.color = text;
        fontgameover = fontGenerator.generateFont(fontParameter);
    }

    private Texture resize (String png, int x, int y){//modifica el tamaño de uan texture
        Pixmap sizefull = new Pixmap(Gdx.files.internal(png));
        Pixmap newsize = new Pixmap(x, y, sizefull.getFormat());
        newsize.drawPixmap(sizefull,
                0, 0, sizefull.getWidth(), sizefull.getHeight(),
                0, 0, newsize.getWidth(), newsize.getHeight()
        );
        Texture texture = new Texture(newsize);
        return texture;
    }

    private void PowerupPlacement (SpriteBatch sb){//posiciona los powerups
        switch (randopower){
            case 1:
                if(score>=limits && score<=limits+1){//score>=limits && score<=limits+1
                    int position = exactPositionForPowerup(limits); //exactPositionForPowerup(limits);
                    hammerbound = new Rectangle(tubes.get(position).getPosTopTube().x-60, tubes.get(position).getPosTopTube().y-45+(powerup_margin*10), hammer.getWidth(), hammer.getHeight());
                    has_power=false;
                    blocklimit= limits+1;
                    powerpos = tubes.get(position).getScore()+3;
                    sb.draw(hammer, tubes.get(position).getPosTopTube().x-60, tubes.get(position).getPosTopTube().y-45+(powerup_margin*10));
                }
                if (score>=blocklimit && score<=blocklimit+1){
                    int position = exactPositionForPowerup(blocklimit);
                    blockbound = new Rectangle(tubes.get(position).getPosTopTube().x, tubes.get(position).getPosTopTube().y-80, block.getWidth(), block.getHeight());
                    sb.draw(block, tubes.get(position).getPosTopTube().x, tubes.get(position).getPosTopTube().y-80);
                }
                break;
            case 2:
                if(score>=limits && score<=limits+1){//score>=limits && score<=limits+1
                    int position = exactPositionForPowerup(limits); //exactPositionForPowerup(limits);
                    powerpos = tubes.get(position).getScore()+3;
                    has_power=false;
                    phantombound = new Rectangle(tubes.get(position).getPosTopTube().x-60, tubes.get(position).getPosTopTube().y-45+(powerup_margin*10), phantom.getWidth(), phantom.getHeight());
                    sb.draw(phantom, tubes.get(position).getPosTopTube().x-60, tubes.get(position).getPosTopTube().y-45+(powerup_margin*10));
                }
                break;
            default:
                break;
        }
    }

    private String[] digitosContador(int numero){//imagenes del contador
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

    private boolean getsHammer(Rectangle r){
        return r.overlaps(hammerbound);
    }

    private boolean touchblock (Rectangle r){
        return r.overlaps(blockbound);
    }

    private boolean getsPhantom(Rectangle r){
        return r.overlaps(phantombound);
    }

    private static int randomNumber(){ //Para los rangos de cúando aparece el powerup
        //rand = new Random();
        int r = (int)Math.floor(Math.random()*(max-min+1)+min);//rand.nextInt((max - min) + 1) + min;
        return r;
    }

    private long diff, start = System.currentTimeMillis();

    public void sleep(int fps) {//sleep thread para limiar fps a 60
        if(fps>0){
            diff = System.currentTimeMillis() - start;
            long targetDelay = 1000/fps;
            if (diff < targetDelay) {
                try{
                    Thread.sleep(targetDelay - diff);
                } catch (InterruptedException e) {}
            }
            start = System.currentTimeMillis();
        }
    }

    private int exactPositionForPowerup(int limit){//encontrar la posicion del tubo correspondiente al valor de los limites
        int k=-1;
        for(int i=3; i<=limit; i++){
            k++;
            if(k==4){
                k=0;
            }
        }
        return k;
    }

    private int mod(int x, int y)//encontrar residuo
    {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }
}


