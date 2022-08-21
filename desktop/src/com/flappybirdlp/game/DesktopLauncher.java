package com.flappybirdlp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.flappybirdlp.game.FlappyBirdLP;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(FlappyBirdLP.WIDTH, FlappyBirdLP.HEIGHT);
		config.setForegroundFPS(60);
		config.setTitle("Flappy Bird LP");
		new Lwjgl3Application(new FlappyBirdLP(), config);
	}
}
