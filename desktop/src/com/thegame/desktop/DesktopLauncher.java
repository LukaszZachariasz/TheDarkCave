package com.thegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thegame.TheGame;

/**
 * Jest to klasa s�u�aca do uruchomienia programu na komputerze. Biblioteka
 * libGDX umo�liwia �atwe portowanie aplikacji na r�zne platformy.
 * 
 * @author Norbert Wychowski, �ukasz Zachariasz
 *
 */
public class DesktopLauncher {

	/**
	 * Metoda main kt�ra wywo�uje si� wraz z uruchomieniem programu. Tworzy ona
	 * obiekt klasy TheGame, b�d�cej podstaw� gry.
	 * 
	 * @param arg
	 *            Argumenty konsoli.
	 */
	public static void main(String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = TheGame.GAME_NAME;
		config.width = TheGame.WIDTH;
		config.height = TheGame.HEIGHT;
		config.foregroundFPS = 60;
		config.resizable = false;
		config.vSyncEnabled = false;
		new LwjglApplication(new TheGame(), config);

	}
}
