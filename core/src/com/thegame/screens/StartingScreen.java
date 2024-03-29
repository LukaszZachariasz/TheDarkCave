package com.thegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.thegame.TheGame;

/**
 * Ekran wczytywania gry.
 * 
 * @author Norbert Wychowski, Łukasz Zachariasz
 *
 */
public class StartingScreen extends AbstractScreen {

	/**
	 * Tekstura wyświetlana podczas wczytywania gry.
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
	 * Wczytanie tekstury ekranu ładowania.
	 */
	private void initialize() {
		startScreenTexture = new Texture(Gdx.files.internal("startingScreen.png"));

	}

	/**
	 * Metoda odpowiedzialna za renderowanie obrazu.
	 * 
	 * @param delta
	 *            Czas pomiędzy poszczególnymi wywołaniami metody render().
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
	 * Wywoływana podczas zmiany wymiarów okna aplikacji.
	 * 
	 */
	@Override
	public void resize(int width, int height) {
	}

	/**
	 * Wywoływana podczas zapauzowania aplikacji (głównie kiedy okno nie jest
	 * aktywne).
	 */
	@Override
	public void pause() {
	}

	/**
	 * Wywoływana podczas wznowienia aplikacji (głównie kiedy okno jest
	 * aktywne).
	 */
	@Override
	public void resume() {
	}
}
