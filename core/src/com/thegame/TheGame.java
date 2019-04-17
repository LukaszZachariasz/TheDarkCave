package com.thegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.thegame.screens.DeathScreen;
import com.thegame.screens.GameScreen;
import com.thegame.screens.MenuScreen;
import com.thegame.screens.StartingScreen;
import com.thegame.screens.WinScreen;

/**
 * Klasa dziedzicz�ca po klasie Game. S�u�y do zmiany aktualnie wy�wietlanego
 * ekranu (screenu).
 * 
 * @author Norbert
 *
 */
public class TheGame extends Game {

	/**
	 * Tablica przechowuj�ca rozk�ad pomieszczenia.
	 */
	private GameScreen[][] gameScreen;

	/**
	 * Ekran menu.
	 */
	private MenuScreen menuScreen;

	/**
	 * Ekran startowy.
	 */
	private StartingScreen startingScreen;

	/**
	 * Ekran wy�wietlany po �mierci gracza.
	 */
	private DeathScreen deathScreen;

	/**
	 * Ekran wy�wietlany po uko�czeniu gry.
	 */
	private WinScreen winScreen;

	/**
	 * Nazwa gry.
	 */
	public static final String GAME_NAME = "The Dark Cave";

	/**
	 * Szeroko�� okna.
	 */
	public static final int WIDTH = 1366;

	/**
	 * Wysoko�� okna.
	 */
	public static final int HEIGHT = 768;

	/**
	 * Po�owa szeroko�ci okna.
	 */
	public static final int HALF_WIDTH = WIDTH / 2;

	/**
	 * Po�owa wysoko�ci okna.
	 */
	public static final int HALF_HEIGHT = HEIGHT / 2;

	/**
	 * Wymiar planszy.
	 */
	public static final int DIMENSION = 11;

	/**
	 * Rozmiar scian (podany w pixelach).
	 */
	public static final int WALL_SIZE = 30;

	/**
	 * G�o�no�� muzyki.
	 */
	public static final float MUSIC_VOLUME = 0.5f;
	/**
	 * G�o�no�� efekt�w.
	 */
	public static final float FX_VOLUME = 0.8f;

	/**
	 * Metoda wywo�ywana przy pierwszym utworzeniu aplikacji.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	@Override
	public void create() {
		MusicSound.initSound();
		setStartingScreen();
	}

	/**
	 * Ustawia ekran startowy jako obecny.
	 */
	private void setStartingScreen() {
		startingScreen = new StartingScreen(this);
		setScreen(startingScreen);
	}

	/**
	 * Ustawia podane przez parametr pomieszczenie jako obecny ekran.
	 * 
	 * @param gameScreen
	 *            Pomieszczenie kt�re ma zosta� ustawione jako obecne.
	 */
	public void setGameScreen(GameScreen gameScreen) {
		setScreen(gameScreen);
	}

	/**
	 * Inicjacja planszy gry oraz ustawienie pocz�tkowego pomieszczenia.
	 * 
	 * @param gameScreen
	 *            Plansza z wszystkimi pomieszczeniami.
	 */
	public void setStartingGameScreen(GameScreen[][] gameScreen) {
		this.gameScreen = gameScreen;
		setGameScreen(gameScreen[DIMENSION / 2][DIMENSION / 2]);
		Hero hero = new Hero(gameScreen[DIMENSION / 2][DIMENSION / 2].getSpriteBatch());
		hero.setGameScreen(gameScreen[DIMENSION / 2][DIMENSION / 2]);
		GameScreen.setFirstRoom(true);
		Enemy.enemies.clear();
		GameScreen.setHero(hero);
	}

	/**
	 * Ustawia ekran menu jako obecny.
	 */
	public void setMenuScreen() {
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	/**
	 * Ustawia ekran �mierci jako obecny.
	 */
	public void setDeathScreen() {
		deathScreen = new DeathScreen(this);
		setScreen(deathScreen);
	}

	/**
	 * Ustawia ekran wygranej jako obecny.
	 */
	public void setWinScreen() {
		winScreen = new WinScreen(this);
		setScreen(winScreen);
	}

	/**
	 * Wywo�ywana podczas niszczenia aplikacji.
	 * 
	 * @see com.badlogic.gdx.Game#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Wywo�ywana podczas renderowania aplikacji.
	 * 
	 * @see com.badlogic.gdx.Game#render()
	 */
	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render();
	}

	/**
	 * Wywo�ywana podczas zmiany wymiar�w okna aplikacji.
	 * 
	 * @see com.badlogic.gdx.Game#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	/**
	 * Wywo�ywana podczas zapauzowania aplikacji (g��wnie kiedy okno nie jest
	 * aktywne).
	 * 
	 * @see com.badlogic.gdx.Game#pause()
	 */
	@Override
	public void pause() {
		super.pause();
	}

	/**
	 * Wywo�ywana podczas wznowienia aplikacji (g��wnie kiedy okno jest
	 * aktywne).
	 * 
	 * @see com.badlogic.gdx.Game#resume()
	 */
	@Override
	public void resume() {
		super.resume();
	}

}
