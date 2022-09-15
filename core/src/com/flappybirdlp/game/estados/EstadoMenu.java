package com.flappybirdlp.game.estados;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.flappybirdlp.game.FlappyBirdLP;

public class EstadoMenu extends Estado{
    private Texture background;
    private Texture playBtn;
    private Texture startTitle;
    private Texture birdFirstScreen;
    private Texture infobtn;
    private Texture LP;
    private Texture highscorebtn;
    private Texture star;
    private Texture infobackground;
    private Texture Okbtn;
    private int cont=3, i=0;
    private int[] array = {30, 25, 35};
    public static Music music;
    private BitmapFont fontTitle; //COMENTAR A PARTIR DE AQUÍ SI SE VE MAL
    private BitmapFont infotext;
    private BitmapFont highscorefont, scorefont;
    private FreeTypeFontGenerator fontGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter fontParameter;
    Preferences prefs = Gdx.app.getPreferences("My Preferences");

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
        infobtn = resize("infobtn.png", 50, 50);
        LP = new Texture("LP.png");
        highscorebtn = resize("highscorebtn.png", 160, 60);
        star = resize("star.png", 120, 120);
        infobackground = resize("infobackground.png", 420,400); // x: 420, y: 300
        Okbtn = resize("Ok.png", 150, 60);

        //Tile 'Flappy Bird LP' creado con fonts
        fontTitle = new BitmapFont();
        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("flappy-font.ttf"));//"OpenSans-ExtraBold.ttf"
        fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size=60;
        fontParameter.shadowOffsetX = 5;
        fontParameter.shadowOffsetY = 5;
        fontParameter.borderWidth=4;
        fontParameter.borderColor = Color.BLACK;
        fontParameter.color = Color.CHARTREUSE;//new Color(0x00FF33FF);
        fontTitle = fontGenerator.generateFont(fontParameter);

        //Texto de info de desarrolladores
        infotext = new BitmapFont();
        fontParameter.size=25;
        fontParameter.color = Color.WHITE;//new Color(0x00FF33FF);
        infotext = fontGenerator.generateFont(fontParameter);

        //Texto de Highscore
        highscorefont = new BitmapFont();
        fontParameter.size=30;
        fontParameter.color = Color.YELLOW;
        highscorefont = fontGenerator.generateFont(fontParameter);

        scorefont = new BitmapFont();
        fontParameter.size=55;
        fontParameter.color = Color.WHITE;
        scorefont = fontGenerator.generateFont(fontParameter);
    }

    private int flag=0, iterator=0, iteratorForHS=0, showhighscore=0, iteratorForInfo = 0, showinfo=0,
    iteratorForOk = 0, showOkpressed = 0;
    private int infoselected = 0, highscoreselected = 0;
    @Override
    public void handleInput() {

        if(Gdx.input.isTouched()){
            Vector3 tmp= new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(tmp); //(FlappyBirdLP.WIDTH/2) - (playBtn.getWidth()/2) - 90, FlappyBirdLP.HEIGHT/2 - 310)
            Rectangle textureBounds=new Rectangle((FlappyBirdLP.WIDTH/2) - (playBtn.getWidth()/2), FlappyBirdLP.HEIGHT/2 - 310,playBtn.getWidth(),playBtn.getHeight());
            Rectangle hsBounds=new Rectangle((FlappyBirdLP.WIDTH/2) - (playBtn.getWidth()/2) - 120, FlappyBirdLP.HEIGHT/2 + 325,playBtn.getWidth(),playBtn.getHeight());
            Rectangle infoBounds=new Rectangle((FlappyBirdLP.WIDTH/2) - (infobtn.getWidth()/2)+155, FlappyBirdLP.HEIGHT/2 + 332,playBtn.getWidth(),playBtn.getHeight());
            Rectangle okBounds = new Rectangle((FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2)+138, FlappyBirdLP.HEIGHT/2+90, playBtn.getWidth(),playBtn.getHeight());

            if(textureBounds.contains(tmp.x, tmp.y)  && infoselected==0 && highscoreselected==0){ //Si le da al botón, lo lleva al getReady state
                playBtn = new Texture("playbtnpressed.png");
                flag=1;
            }
            //Botón highscore
            if(hsBounds.contains(tmp.x, tmp.y) && infoselected==0){
                Okbtn = resize("Ok.png", 150, 60);
                highscorebtn = resize("highscorebtnpressed.png", 160, 60);
                showhighscore=1;
                highscoreselected=1;
            }
            //Botón info
            if(okBounds.contains(tmp.x, tmp.y)){
                Okbtn = resize("Okpressed.png", 150, 60);
                showOkpressed = 1;
                //showinfo = 0;
                //infobtn = resize("infobtn.png", 50, 50);
                //infoselected = 0;
            }
            if(infoBounds.contains(tmp.x, tmp.y) && highscoreselected == 0){
                Okbtn = resize("Ok.png", 150, 60);
                infobtn = resize("infobtnpressed.png", 50, 50);
                showinfo=1;
                infoselected = 1; //se está mostrando el pánel de info
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
        //sb.setColor(Color.DARK_GRAY);
        sb.draw(background, 0,0, FlappyBirdLP.WIDTH, FlappyBirdLP.HEIGHT);
        fontTitle.draw(sb, "Flappy Bird", FlappyBirdLP.WIDTH/2 - (startTitle.getWidth()/2)-46, FlappyBirdLP.HEIGHT/2 + 250);
        sb.draw(star, FlappyBirdLP.WIDTH/2 - (star.getWidth()/2)+10, FlappyBirdLP.HEIGHT/2 + 80);
        fontTitle.draw(sb, "LP", FlappyBirdLP.WIDTH/2 - (startTitle.getWidth()/2)+115, FlappyBirdLP.HEIGHT/2 + 170);
        //LP = resize("LP.png", 100,100);
        //sb.draw(LP, (FlappyBirdLP.WIDTH/2) - (startTitle.getWidth()/2)+240, FlappyBirdLP.HEIGHT/2 + 150);
        sb.draw(birdFirstScreen, (FlappyBirdLP.WIDTH/2) - (birdFirstScreen.getWidth()/2), FlappyBirdLP.HEIGHT/2 + array[i]-100);
        try {
            sb.draw(birdFirstScreen, (FlappyBirdLP.WIDTH/2) - (birdFirstScreen.getWidth()/2), FlappyBirdLP.HEIGHT/2 + array[i] - 100);
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

        sb.draw(playBtn, (FlappyBirdLP.WIDTH/2) - (playBtn.getWidth()/2), FlappyBirdLP.HEIGHT/2 - 310); //FlappyBirdLP.HEIGHT/2 - 150);
        //highscorebtn = resize("highscorebtn.png", 160, 60);
        sb.draw(highscorebtn, (FlappyBirdLP.WIDTH/2) - (playBtn.getWidth()/2) - 120, FlappyBirdLP.HEIGHT/2 + 325);

        sb.draw(infobtn, (FlappyBirdLP.WIDTH/2) - (infobtn.getWidth()/2)+155, FlappyBirdLP.HEIGHT/2 + 332);

        if(flag==1){
            iterator++;
            if(iterator>=2){
                changeState();
            }
        }
        if(showhighscore==1){
            iteratorForHS++;
            if(iteratorForHS>=2){
                sb.draw(infobackground, (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2), FlappyBirdLP.HEIGHT/2);
                highscorefont.draw(sb, "Your highscore: ", (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2)+65, FlappyBirdLP.HEIGHT/2+295);
                String scoreValue = String.valueOf(prefs.getInteger("highscore"));
                int offsetInX = 0;
                if(scoreValue.length()<=1){
                    offsetInX = 192;
                }
                else if(scoreValue.length()==2){
                    offsetInX = 177;
                }
                else if(scoreValue.length()==3){
                    offsetInX = 160;
                }
                scorefont.draw(sb, scoreValue, (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2)+offsetInX, FlappyBirdLP.HEIGHT/2+230);
                sb.draw(Okbtn, (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2)+138, FlappyBirdLP.HEIGHT/2+90);
            }
        }
        if(showinfo==1){
            //SpriteBatch nsb = sb;
            iteratorForInfo++;
            if(iteratorForInfo>=2){
                sb.draw(infobackground, (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2), FlappyBirdLP.HEIGHT/2);
                infotext.draw(sb, "Desarrollado por: ", (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2)+65, FlappyBirdLP.HEIGHT/2+295);
                infotext.draw(sb, "* Angel Ponce", (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2)+50, FlappyBirdLP.HEIGHT/2+240);
                infotext.draw(sb, "* Mauricio Aguilera", (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2)+50, FlappyBirdLP.HEIGHT/2+200);
                sb.draw(Okbtn, (FlappyBirdLP.WIDTH/2) - (infobackground.getWidth()/2)+138, FlappyBirdLP.HEIGHT/2+90);
            }
        }
        if(showOkpressed==1){
            iteratorForOk++;
            if(iteratorForOk>=2){
                showinfo = 0;
                showhighscore = 0;
                infobtn = resize("infobtn.png", 50, 50);
                highscorebtn = resize("highscorebtn.png", 160, 60);
                infoselected = 0;
                highscoreselected = 0;
                showOkpressed = 0;
            }
        }
        //sb.draw(playBtn, camera.position.x - playBtn.getWidth()/2, camera.position.y);
        sb.end();
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

    @Override
    public void dispose() {
        background.dispose();
        playBtn.dispose();
        birdFirstScreen.dispose();
        startTitle.dispose();
        music.dispose();
        fontTitle.dispose();
        fontGenerator.dispose();
        //infobtn.dispose();
        LP.dispose();
        highscorebtn.dispose();
        Okbtn.dispose();
        star.dispose();
        infobackground.dispose();
        infotext.dispose();
        //System.out.println("Menu State disposed");
    }
}
