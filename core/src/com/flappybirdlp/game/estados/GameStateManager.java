package com.flappybirdlp.game.estados;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {

    private Stack<Estado> estados;

    public GameStateManager(){
        estados = new Stack<Estado>();
    }

    public void push(Estado estado){
        estados.push(estado);
    }

    public void pop(){
        estados.pop().dispose(); //estados.pop();
    }

    public void set(Estado estado){
        estados.pop().dispose(); //estados.pop();
        estados.push(estado);
    }

    public void update(float dt){
        estados.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        estados.peek().render(sb);
    }

}
