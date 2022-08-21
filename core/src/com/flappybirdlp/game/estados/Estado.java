package com.flappybirdlp.game.estados;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class Estado {
    protected OrthographicCamera camera; //Mostramos una parte, segmento, del juego. Señalizamos qué queremos ver en ese momento
    protected Vector3 mouse; //Nos sirve para saber dónde dimos click o dónde está posicionado el "mouse"
    protected GameStateManager gsm;//Administrador de los distintos estados del juego

    protected Estado(GameStateManager gsm){
        this.gsm = gsm;
        camera = new OrthographicCamera();
        mouse = new Vector3();
    }

    protected abstract void handleInput();//Detecta las entradas que nosotros demos, en el estado que esté
    public abstract void update(float dt); //float delta time //Este método,
    public abstract void render(SpriteBatch sb); //y este método, comoponen el loop game
    public abstract void dispose();


}
