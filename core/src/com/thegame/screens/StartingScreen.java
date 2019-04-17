package com.thegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.thegame.TheGame;

/**
 * Ekran wczytywania gry.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public class StartingScreen extends AbstractScreen {

	/**
	 * Tekstura wyœwietlana podczas wczytywania gry.
	 */
	private Texture startScreenTexture;

	/**
	 * Konstruktor klasy StartingScreen.
	 * 
	 * @param game
	 *            Referencja na obiekt klasy TheGame.
	 */
	public StartingScreen(final TheGame game) {
		super(game);
		initialize();
		Timer.schedule(new Task() {
			@Override
			public void run() {
				game.setMenuScreen();
			}
		}, 3);

	}

	/**
	 * Wczytanie tekstury ekranu ³adowania.
	 */
	private void initialize() {
		startScreenTexture = new Texture(Gdx.files.internal("startingScreen.png"));

	}

	/**
	 * Metoda odpowiedzialna za renderowanie obrazu.
	 * 
	 * @param delta
	 *            Czas pomiêdzy poszczególnymi wywo³aniami metody render().
	 * @see com.thegame.screens.AbstractScreen#render(float)
	 */
	@Override
	public void render(float delta) {
		super.render(delta);

		getSpriteBatch().begin();
		getSpriteBatch().draw(startScreenTexture, 0, 0);
		getSpriteBatch().end();
	}

	/**
	 * Wywo³ywana podczas zmiany wymiarów okna aplikacji.
	 * 
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
