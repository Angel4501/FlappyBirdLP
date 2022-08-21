package com.flappybirdlp.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flappybirdlp.game.estados.EstadoMenu;
import com.flappybirdlp.game.estados.GameStateManager;
import com.flappybirdlp.game.estados.GetReady;
import com.flappybirdlp.game.estados.MenuState;

public class FlappyBirdLP extends ApplicationAdapter {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 800;
	public static final String TITLE = "FlappyBirdLP";

	private GameStateManager gsm;
	private SpriteBatch batch;

	public static Music music;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		//gsm.push(new MenuState(gsm));
		gsm.push(new EstadoMenu(gsm));
		//gsm.push(new GetReady(gsm));
	}

	@Override
	public void render () {
		//ScreenUtils.clear(1, 0, 0, 1);
		//batch.begin();
		//batch.draw(img, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
		//batch.end();
	}

	@Override
	public void dispose() {
		super.dispose();
		music.dispose();
	}

	//@Override
	/*public void dispose () {
		batch.dispose();
		img.dispose();
	}*/
}
