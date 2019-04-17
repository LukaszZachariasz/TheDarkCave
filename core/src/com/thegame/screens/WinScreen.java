package com.thegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.thegame.MusicSound;
import com.thegame.TheGame;

/**
 * Ekran wy�wietlany po wygranej.
 * 
 * @author Norbert Wychowski, �ukasz Zachariasz
 *
 */
public class WinScreen extends AbstractScreen {

	/**
	 * Obiekt przechowuj�cy tekstur� wy�wietlana na ekranie �adowania.
	 */
	private Texture startScreenTexture;

	/**
	 * Konstruktor WinScreen.
	 * 
	 * @param game
	 *            Referencja na obiekt klasy TheGame.
	 */
	public WinScreen(final TheGame game) {
		super(game);
		initialize();
		Timer.schedule(new Task() {
			@Override
			public void run() {
				getTheGame().setMenuScreen();
				MusicSound.deathMusic.stop();
			}
		}, 1.5f);
	}

	/**
	 * Wczytanie tekstury z pliku.
	 */
	private void initialize() {
		startScreenTexture = new Texture(Gdx.files.internal("win.png"));
		MusicSound.deathMusic.play();
	}

	/**
	 * Metoda odpowiedzialna za renderowanie. Wy�wietla na ekranie tekstur�.
	 * 
	 * @see com.thegame.screens.AbstractScreen#render(float)
	 */
	@Override
	public void render(float delta) {
		super.render(delta);

		getSpriteBatch().begin();
		getSpriteBatch().draw(startScreenTexture, 0, 0, TheGame.WIDTH, TheGame.HEIGHT);
		getSpriteBatch().end();
	}

	/**
	 * Wywo�ywana podczas zmiany wymiar�w okna aplikacji.
	 */
	@Override
	public void resize(int width, int height) {
	}

	/**
	 * Wywo�ywana podczas zapauzowania aplikacji (g��wnie kiedy okno nie jest
	 * aktywne).
	 */
	@Override
	public void pause() {
	}

	/**
	 * Wywo�ywana podczas wznowienia aplikacji (g��wnie kiedy okno jest
	 * aktywne).
	 */
	@Override
	public void resume() {
	}
}
