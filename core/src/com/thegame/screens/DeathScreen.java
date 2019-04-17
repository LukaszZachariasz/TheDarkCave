package com.thegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.thegame.MusicSound;
import com.thegame.TheGame;


/**
 * Ekran wyœwietlany po œmierci gracza.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public class DeathScreen extends AbstractScreen {

	/**
	 * Obiekt przechowuj¹cy teksturê wyœwietlana na ekranie ³adowania.
	 */
	private Texture startScreenTexture;

	/**
	 * Konstruktor DeathScreen.
	 * 
	 * @param game
	 *            Referencja na obiekt klasy TheGame.
	 */
	public DeathScreen(final TheGame game) {
		super(game);
		initialize();
		Timer.schedule(new Task() {
			@Override
			public void run() {
				getTheGame().setMenuScreen();
				MusicSound.deathMusic.stop();
			}
		}, 3);
	}

	/**
	 * Wczytanie tekstury z pliku.
	 */
	private void initialize() {
		startScreenTexture = new Texture(Gdx.files.internal("death.png"));
		MusicSound.deathMusic.play();

	}

	/**
	 * Metoda odpowiedzialna za renderowanie. Wyœwietla na ekranie teksturê.
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
	 * Wywo³ywana podczas zmiany wymiarów okna aplikacji.
	 */
	@Override
	public void resize(int width, int height) {
	}

	/**
	 * Wywo³ywana podczas zapauzowania aplikacji (g³ównie kiedy okno nie jest
	 * aktywne).
	 */
	@Override
	public void pause() {
	}

	/**
	 * Wywo³ywana podczas wznowienia aplikacji (g³ównie kiedy okno jest
	 * aktywne).
	 */
	@Override
	public void resume() {
	}
}
