package com.thegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.thegame.TheGame;

/**
 * Klasa abstrakcyjna implementuj¹ca interfejs Screen.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public abstract class AbstractScreen implements Screen {

	
	/**
	 * Referencja na obiekt klasy TheGame.
	 */
	private TheGame theGame;

	/**
	 * Odpowiada za obs³ugê sygna³ów.
	 */
	protected Stage stage;
	/**
	 * Bufor na którym rysowane s¹ tekstury.
	 */
	private SpriteBatch spriteBatch;

	/**
	 * Konstruktor klasy AbstractScreen.
	 * 
	 * @param game
	 *            Referencja na obiekt klasy TheGame.
	 */
	public AbstractScreen(TheGame game) {
		this.setTheGame(game);
		stage = new Stage(new StretchViewport(TheGame.WIDTH, TheGame.HEIGHT));
		setSpriteBatch(new SpriteBatch());
		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * Metoda odpowiedzialna za renderowanie obrazu.
	 * 
	 * @param delta
	 *            Czas pomiêdzy poszczególnymi wywo³aniami metody render().
	 * 
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		clearScreen();
	}

	/**
	 * Wywo³ywana w momencie kiedy stajê siê aktualnym ekranem wyœwietlanym
	 * przez Game.
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {
	}

	/**
	 * Wype³nia obszar jednolitym kolorem.
	 */
	private void clearScreen() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	/**
	 * Wywo³ywana w momencie kiedy obiekt ma zwolniæ zasoby.
	 * 
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		getTheGame().dispose();
	}

	/**
	 * Wywo³ywana w momencie kiedy ekran nie jest ju¿ d³u¿ej u¿ywany przez Game.
	 * 
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
	}

	/**
	 * Zwraca bufor spriteBatch.
	 * 
	 * @return Bufor spriteBatch.
	 */
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	/**
	 * Ustawia bufor spriteBatch.
	 * 
	 * @param spriteBatch
	 *            Bufor spriteBatch.
	 */
	public void setSpriteBatch(SpriteBatch spriteBatch) {
		this.spriteBatch = spriteBatch;
	}

	/**
	 * Zwraca obiekt klasy TheGame.
	 * 
	 * @return Obiekt klasy TheGame.
	 */
	public TheGame getTheGame() {
		return theGame;
	}

	/**
	 * Ustawia TheGame
	 * 
	 * @param theGame
	 *            Referencja na obiekt TheGame.
	 */
	public void setTheGame(TheGame theGame) {
		this.theGame = theGame;
	}

}
