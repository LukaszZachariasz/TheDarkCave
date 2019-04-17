package com.thegame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.thegame.GenerateFloor;
import com.thegame.MusicSound;
import com.thegame.TheGame;

/**
 * Ekran menu gry.
 * 
 * @author Norbert Wychowski, �ukasz Zachariasz
 *
 */
public class MenuScreen extends AbstractScreen {

	/**
	 * Referencja na obiekt klasy TheGame. Wykorzystywany do zmiany ekran�w.
	 */
	private TheGame theGame;

	/**
	 * Przycisk rozpoczynaj�cy gr�.
	 */
	private ImageButton playButton;
	/**
	 * Przycisk wyj�cia z gry.
	 */
	private ImageButton exitButton;

	/**
	 * T�o menu gry.
	 */
	private ImageButton background;

	/**
	 * Wymiar planszy.
	 */
	public static final int DIMENSION = TheGame.DIMENSION;
	/**
	 * Ilo�� pomieszcze�.
	 */
	public static final int ROOMS_ON_FLOOR = 11;

	/**
	 * Konstruktor klasy MenuScreen
	 * 
	 * @param theGame
	 *            Referencja na obiekt klasy TheGame.
	 */
	public MenuScreen(TheGame theGame) {

		super(theGame);

		this.theGame = theGame;

		setSpriteBatch(new SpriteBatch());
		stage = new Stage();

		initButtons();
		initControl();

	}

	/**
	 * Utworzenie obs�ugi przycisk�w rozpocz�cia oraz zako�czenia gry.
	 */
	private void initControl() {

		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameScreen.roomsLeft = ROOMS_ON_FLOOR;
				GenerateFloor gen = new GenerateFloor(DIMENSION, ROOMS_ON_FLOOR, theGame);
				theGame.setStartingGameScreen(gen.getFloorTab());
			}
		});

		exitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});

	}

	/**
	 * Utworzenie przycisk�w.
	 */
	private void initButtons() {

		background = new ImageButton(
				new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menuScreen.png")))));

		playButton = new ImageButton(
				new TextureRegionDrawable(
						new TextureRegion(new Texture(Gdx.files.internal("buttons/playButtonActive.png")))),
				new TextureRegionDrawable(
						new TextureRegion(new Texture(Gdx.files.internal("buttons/playButtonNoActive.png")))));

		exitButton = new ImageButton(
				new TextureRegionDrawable(
						new TextureRegion(new Texture(Gdx.files.internal("buttons/exitButtonActive.png")))),
				new TextureRegionDrawable(
						new TextureRegion(new Texture(Gdx.files.internal("buttons/exitButtonNoActive.png")))));

		playButton.setPosition(TheGame.HALF_WIDTH, TheGame.HALF_HEIGHT - playButton.getHeight(), 100);
		exitButton.setPosition(TheGame.HALF_WIDTH, TheGame.HALF_HEIGHT - playButton.getHeight() * 2, 100);

		stage.addActor(background);
		stage.addActor(playButton);
		stage.addActor(exitButton);

	}

	/**
	 * Metoda odpowiedzialna za renderowanie.
	 * 
	 * @see com.thegame.screens.AbstractScreen#render(float)
	 */
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		getSpriteBatch().begin();

		stage.act(delta);
		stage.draw();
		getSpriteBatch().end();
	}

	/**
	 * Wywo�ywana podczas zapauzowania aplikacji (g��wnie kiedy okno nie jest
	 * aktywne).
	 * 
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		MusicSound.menuMusic.pause();
	}

	/**
	 * Wywo�ywana podczas wznowienia aplikacji (g��wnie kiedy okno jest
	 * aktywne).
	 * 
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		MusicSound.menuMusic.play();
	}

	/**
	 * Wywo�ywana w momencie kiedy ekran nie jest ju� d�u�ej u�ywany przez Game.
	 * 
	 * @see com.thegame.screens.AbstractScreen#hide()
	 */
	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		MusicSound.menuMusic.pause();
	}

	/**
	 * Wywo�ywana w momencie kiedy obiekt ma zwolni� zasoby.
	 * 
	 * @see com.thegame.screens.AbstractScreen#dispose()
	 */
	@Override
	public void dispose() {
		getSpriteBatch().dispose();
		MusicSound.menuMusic.stop();
	}

	/**
	 * Wywo�ywana w momencie kiedy staj� si� aktualnym ekranem wy�wietlanym
	 * przez Game.
	 * 
	 * @see com.thegame.screens.AbstractScreen#show()
	 */
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		MusicSound.menuMusic.play();
	}

	/**
	 * Wywo�ywana podczas zmiany wymiar�w okna aplikacji.
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
	}

}
