package com.thegame.screens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.thegame.Bullet;
import com.thegame.Enemy;
import com.thegame.Hero;
import com.thegame.MusicSound;
import com.thegame.TheGame;

/**
 * Ekran wyœwietlaj¹cy pomieszczenie w którym obecnie rozgrywa siê akcja.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public class GameScreen extends AbstractScreen {
	/**
	 * Obiekt klasy Hero reprezentuj¹cy g³ównego bohatera.
	 */
	private static Hero hero;
	/**
	 * Zawiera informacjê o tym czy gracz znajduje siê w pierwszym pomieszczeniu
	 * (bez przeciwników).
	 */
	private static boolean firstRoom = true;

	/**
	 * Tekstura t³a.
	 */
	private Texture background;
	/**
	 * Tekstura pokazuj¹ca ile gracz ma ¿ycia.
	 */
	private Texture health;

	/**
	 * Iloœæ przeciwników w pomieszczeniu.
	 */
	private int spawnedEnemies;
	/**
	 * Maksymalna iloœæ przeciwników w pomieszczeniu.
	 */
	private int maxEnemies;
	/**
	 * Typ pomieszczenia (pomieszczenia ró¿ni¹ siê uk³adem przeszkód).
	 */
	private int roomType;

	/**
	 * S³u¿y do losowania maksymalnej iloœci przeciwników.
	 */
	private Random randMaxEnemies;
	/**
	 * DŸwiêk przechodzenia miêdzy pomieszczeniami.
	 */
	public Music gateSound;

	/**
	 * Pomieszczenie znajduj¹ce siê powy¿ej.
	 */
	private GameScreen outSceneUp;
	/**
	 * Pomieszczenie znajduj¹ce siê poni¿ej.
	 */
	private GameScreen outSceneDown;
	/**
	 * Pomieszczenie znajduj¹ce siê po lewej stronie.
	 */
	private GameScreen outSceneLeft;
	/**
	 * Pomieszczenie znajduj¹ce siê po prawej stronie.
	 */
	private GameScreen outSceneRight;
	/**
	 * Obiekt klasy Rectangle zawieraj¹cy wspó³rzêdne drzwi znajduj¹cych siê na
	 * górze.
	 */
	private Rectangle boundsDoorUp;
	/**
	 * Obiekt klasy Rectangle zawieraj¹cy wspó³rzêdne drzwi znajduj¹cych siê na
	 * dole.
	 */
	private Rectangle boundsDoorDown;
	/**
	 * Obiekt klasy Rectangle zawieraj¹cy wspó³rzêdne drzwi znajduj¹cych siê po
	 * lewej stronie.
	 */
	private Rectangle boundsDoorLeft;
	/**
	 * Obiekt klasy Rectangle zawieraj¹cy wspó³rzêdne drzwi znajduj¹cych siê po
	 * prawej stronie.
	 */
	private Rectangle boundsDoorRight;

	/**
	 * Obiekt klasy Rectangle zawieraj¹cy wspó³rzêdne górnej œciany.
	 */
	private static Rectangle wallUp;
	/**
	 * Obiekt klasy Rectangle zawieraj¹cy wspó³rzêdne górnej œciany.
	 */
	private static Rectangle wallDown;
	/**
	 * Obiekt klasy Rectangle zawieraj¹cy wspó³rzêdne œciany znajduj¹cej siê po
	 * lewej stronie.
	 */
	private static Rectangle wallLeft;
	/**
	 * Obiekt klasy Rectangle zawieraj¹cy wspó³rzêdne œciany znajduj¹cej siê po
	 * prawej stronie.
	 */
	private static Rectangle wallRight;

	/**
	 * Okreœla czy gracz mo¿e opuœciæ pomieszczenie (tylko jeœli pokona
	 * wszystkich przeciwników).
	 */
	private boolean roomBlocked = true;
	/**
	 * Zmienna pomocniczna. U¿ywana jest na pocz¹tku by metoda render narysowa³a
	 * drzwi tylko raz.
	 */
	private boolean locker = false;

	/**
	 * Przechowujê informacjê o wspó³rzêdnych obiektów znajduj¹cych siê na
	 * ziemi.
	 */
	public ArrayList<Rectangle> impedimentList;

	/**
	 * 
	 */
	public static int roomsLeft;

	/**
	 * Konstruktor klasy GameScreen.
	 * 
	 * @param theGame
	 *            Referencja na obiekt klasy TheGame.
	 * @param roomType
	 *            Informacja o typie pomieszczenia (liczba z zakresu 0 - 2).
	 */
	public GameScreen(TheGame theGame, int roomType) {
		super(theGame);

		this.roomType = roomType;

		spawnedEnemies = 0;
		randMaxEnemies = new Random();

		gateSound = Gdx.audio.newMusic(Gdx.files.internal("sound/changeRoom.mp3"));
		gateSound.setVolume(TheGame.FX_VOLUME);
		gateSound.setLooping(false);

		if (firstRoom == false) {
			maxEnemies = randMaxEnemies.nextInt(10) + 1;
		} else {
			firstRoom = false;
		}

		background = new Texture(new Pixmap(Gdx.files.internal("background.png")));
		health = new Texture(new Pixmap(Gdx.files.internal("heart.png")));

		wallUp = new Rectangle(0, TheGame.HEIGHT - TheGame.WALL_SIZE, TheGame.WIDTH, TheGame.WALL_SIZE);
		wallDown = new Rectangle(0, 0, TheGame.WIDTH, TheGame.WALL_SIZE);
		wallLeft = new Rectangle(0, 0, TheGame.WALL_SIZE, TheGame.HEIGHT);
		wallRight = new Rectangle(TheGame.WIDTH - TheGame.WALL_SIZE, 0, TheGame.WALL_SIZE, TheGame.HEIGHT);

		Pixmap p = background.getTextureData().consumePixmap();
		impedimentList = new ArrayList<Rectangle>();

		switch (this.roomType) {
		case 0:
			// nic
			break;
		case 1:
			p.drawPixmap(new Pixmap(Gdx.files.internal("impediment_1.png")), 0, 0);
			impedimentList.add(new Rectangle(478, 268, 409, 231));
			break;
		case 2:
			p.drawPixmap(new Pixmap(Gdx.files.internal("impediment_2.png")), 0, 0);
			impedimentList.add(new Rectangle(341, 192, 137, 76));
			impedimentList.add(new Rectangle(887, 192, 137, 76));
			impedimentList.add(new Rectangle(341, 499, 137, 76));
			impedimentList.add(new Rectangle(887, 499, 137, 76));
			break;
		default:
			break;
		}
	}

	/**
	 * Odtwarza dŸwiêk podczas przechodzenia miêdzy pomieszczeniami.
	 */
	public void playChangeRoomSound() {
		gateSound.play();
	}

	/**
	 * Metoda rysuj¹ca drzwi na teksturze t³a.
	 */
	public void drawDoors() {
		Pixmap p = background.getTextureData().consumePixmap();
		int verticalDoorWidth = TheGame.WALL_SIZE;
		int verticalDoorHeigth = 65;
		int horizontalDoorWidth = verticalDoorHeigth;
		int horizontalDoorHeigth = verticalDoorWidth;

		if (outSceneUp != null) {
			if (roomBlocked == false) {
				p.drawPixmap(new Pixmap(Gdx.files.internal("doors/doorUp.png")), 0, 0);
			} else {
				p.drawPixmap(new Pixmap(Gdx.files.internal("doors/doorUpLocked.png")), 0, 0);
			}
			boundsDoorUp = new Rectangle((TheGame.WIDTH - horizontalDoorWidth) * 0.5f,
					(TheGame.HEIGHT - horizontalDoorHeigth), horizontalDoorWidth, horizontalDoorHeigth);
		}
		if (outSceneDown != null) {
			if (roomBlocked == false) {
				p.drawPixmap(new Pixmap(Gdx.files.internal("doors/doorDown.png")), 0, 0);
			} else {
				p.drawPixmap(new Pixmap(Gdx.files.internal("doors/doorDownLocked.png")), 0, 0);
			}
			boundsDoorDown = new Rectangle((TheGame.WIDTH - horizontalDoorWidth) * 0.5f, 0, horizontalDoorWidth,
					horizontalDoorHeigth);
		}
		if (outSceneLeft != null) {
			if (roomBlocked == false) {
				p.drawPixmap(new Pixmap(Gdx.files.internal("doors/doorLeft.png")), 0, 0);
			} else {
				p.drawPixmap(new Pixmap(Gdx.files.internal("doors/doorLeftLocked.png")), 0, 0);
			}
			boundsDoorLeft = new Rectangle(0, (TheGame.HEIGHT - verticalDoorHeigth) * 0.5f, verticalDoorWidth,
					verticalDoorHeigth);
		}
		if (outSceneRight != null) {
			if (roomBlocked == false) {
				p.drawPixmap(new Pixmap(Gdx.files.internal("doors/doorRight.png")), 0, 0);
			} else {
				p.drawPixmap(new Pixmap(Gdx.files.internal("doors/doorRightLocked.png")), 0, 0);
			}
			boundsDoorRight = new Rectangle((TheGame.WIDTH - verticalDoorWidth),
					(TheGame.HEIGHT - verticalDoorHeigth) * 0.5f, verticalDoorWidth, verticalDoorHeigth);
		}
		background.draw(p, 0, 0);
	}

	/**
	 * Wywo³ywana w momencie kiedy obiekt ma zwolniæ zasoby.
	 * 
	 * @see com.thegame.screens.AbstractScreen#dispose()
	 */
	@Override
	public void dispose() {
		getSpriteBatch().dispose();
		background.dispose();
		MusicSound.gameMusic.stop();
	}

	/**
	 * Wywo³ywana w momencie kiedy stajê siê aktualnym ekranem wyœwietlanym
	 * przez Game.
	 * 
	 * @see com.thegame.screens.AbstractScreen#show()
	 */
	@Override
	public void show() {
		MusicSound.gameMusic.play();
	}

	/**
	 * Metoda s³u¿aca do renderowania pomieszczenia. Odpowiada za rysowanie
	 * tekstur, wywolywanie metod render() znajduj¹cych siê w obiektach klasy
	 * Enemy oraz Hero. Dodatkowo odpowiada za sprawdzanie kolizji pocisków i
	 * przeciwników.
	 * 
	 * @see com.thegame.screens.AbstractScreen#render(float)
	 */
	@Override
	public void render(float delta) {

		getSpriteBatch().begin();
		getSpriteBatch().draw(background, 0, 0);

		if (maxEnemies > spawnedEnemies) {
			new Enemy(getSpriteBatch(), this);
			spawnedEnemies++;
		}

		if (hero.getHealthPoints() <= 0) {
			getTheGame().setDeathScreen();
		}

		// Rysuje serduszka do poziomu zycia
		for (int i = 0; i < hero.getHealthPoints(); i++)
			getSpriteBatch().draw(health, 10 + 55 * i, 10);

		roomBlocked = false;
		for (Enemy x : Enemy.enemies) {

			x.render(delta);

			if (x.getHealthPoints() > 0)
				roomBlocked = true;
		}

		hero.render(delta);
		getSpriteBatch().end();

		if (roomBlocked == false && locker == false) {
			drawDoors();
			roomsLeft--;
			locker = true;
		}

		if (roomsLeft < 0) {
			Timer.schedule(new Task() {
				@Override
				public void run() {
					getTheGame().setWinScreen();
				}
			}, 1.5f);
		}

		for (Enemy x : Enemy.enemies) {
			if (x.getBounds().overlaps(hero.getBounds()) && x.getHealthPoints() > 0) {
				hero.hit(1);
			}
		}

		Iterator<Enemy> en = Enemy.enemies.iterator();
		while (en.hasNext()) {
			Iterator<Bullet> bu = Bullet.bullets.iterator();
			Enemy e = en.next();
			while (bu.hasNext()) {
				Bullet b = bu.next();
				if (b.getBounds().overlaps(e.getBounds()) && e.getHealthPoints() > 0) {
					e.hit(50, b);
					bu.remove();
				}
			}
		}
	}

	/**
	 * Wywo³ywana w momencie kiedy ekran nie jest ju¿ d³u¿ej u¿ywany przez Game.
	 * 
	 * @see com.thegame.screens.AbstractScreen#hide()
	 */
	@Override
	public void hide() {
		MusicSound.gameMusic.pause();
	}

	/**
	 * Ustawia pomieszczenie znajduj¹ce siê powy¿ej.
	 * 
	 * @param outSceneUp
	 *            Pomieszczenie znajduj¹ce siê powy¿ej.
	 */
	public void setOutSceneUp(GameScreen outSceneUp) {
		this.outSceneUp = outSceneUp;
	}

	/**
	 * Ustawia pomieszczenie znajduj¹ce siê poni¿ej.
	 * 
	 * @param outSceneDown
	 *            Pomieszczenie znajduj¹ce siê poni¿ej.
	 */
	public void setOutSceneDown(GameScreen outSceneDown) {
		this.outSceneDown = outSceneDown;
	}

	/**
	 * Ustawia pomieszczenie znajduj¹ce siê po lewej stronie.
	 * 
	 * @param outSceneLeft
	 *            Pomieszczenie znajduj¹ce siê po lewej stronie.
	 */
	public void setOutSceneLeft(GameScreen outSceneLeft) {
		this.outSceneLeft = outSceneLeft;
	}

	/**
	 * Ustawia pomieszczenie znajduj¹ce siê po prawej stronie.
	 * 
	 * @param outSceneRight
	 *            Pomieszczenie znajduj¹ce siê po prawej stronie.
	 */
	public void setOutSceneRight(GameScreen outSceneRight) {
		this.outSceneRight = outSceneRight;
	}

	/**
	 * Zwraca pomieszczenie znajduj¹ce siê powy¿ej.
	 * 
	 * @return Pomieszczenie znajduj¹ce siê powy¿ej.
	 */
	public GameScreen getOutSceneUp() {
		return outSceneUp;
	}

	/**
	 * Zwraca pomieszczenie znajduj¹ce siê poni¿ej.
	 * 
	 * @return Pomieszczenie znajduj¹ce siê poni¿ej.
	 */
	public GameScreen getOutSceneDown() {
		return outSceneDown;
	}

	/**
	 * Zwraca pomieszczenie znajduj¹ce siê po lewej stronie.
	 * 
	 * @return Pomieszczenie znajduj¹ce siê po lewej stronie.
	 */
	public GameScreen getOutSceneLeft() {
		return outSceneLeft;
	}

	/**
	 * Zwraca pomieszczenie znajduj¹ce siê po prawej stronie.
	 * 
	 * @return Pomieszczenie znajduj¹ce siê po prawej stronie.
	 */
	public GameScreen getOutSceneRight() {
		return outSceneRight;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy wymiary drzwi znajduj¹cych
	 * siê powy¿ej.
	 * 
	 * @return Zwraca wymiary drzwi znajduj¹cych siê powy¿ej.
	 */
	public Rectangle getBoundsDoorUp() {
		return boundsDoorUp;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy wymiary drzwi znajduj¹cych
	 * siê poni¿ej.
	 * 
	 * @return Zwraca wymiary drzwi znajduj¹cych siê poni¿ej.
	 */
	public Rectangle getBoundsDoorDown() {
		return boundsDoorDown;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy wymiary drzwi znajduj¹cych
	 * siê po lewej stronie.
	 * 
	 * @return Zwraca wymiary drzwi znajduj¹cych siê po lewej stronie.
	 */
	public Rectangle getBoundsDoorLeft() {
		return boundsDoorLeft;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy wymiary drzwi znajduj¹cych
	 * siê po prawej stronie.
	 * 
	 * @return Zwraca wymiary drzwi znajduj¹cych siê po prawej stronie.
	 */
	public Rectangle getBoundsDoorRight() {
		return boundsDoorRight;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy wymiary œciany znajduj¹cej
	 * siê na górze.
	 * 
	 * @return Zwraca wymiary ciany znajduj¹cej siê na górze.
	 */
	public static Rectangle getWallUp() {
		return wallUp;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy wymiary œciany znajduj¹cej
	 * siê na dole.
	 * 
	 * @return Zwraca wymiary ciany znajduj¹cej siê na dole.
	 */
	public static Rectangle getWallDown() {
		return wallDown;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy wymiary œciany znajduj¹cej
	 * siê po lewej stronie.
	 * 
	 * @return Zwraca wymiary ciany znajduj¹cej siê po lewej stronie.
	 */
	public static Rectangle getWallLeft() {
		return wallLeft;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy wymiary œciany znajduj¹cej
	 * siê po prawej stronie.
	 * 
	 * @return Zwraca wymiary ciany znajduj¹cej siê po prawej stronie.
	 */
	public static Rectangle getWallRight() {
		return wallRight;
	}

	/**
	 * Zwraca informacjê dotycz¹ca tego czy gracz mo¿e opuœcic pomieszczenie.
	 * 
	 * @return true - gracz moze opuœcic pomieszczenie, false - nie mo¿e.
	 */
	public boolean isRoomBlocked() {
		return roomBlocked;
	}

	/**
	 * Ustawia nowego bohatera.
	 * 
	 * @param hero
	 *            Referencja na obiekt klasy Hero.
	 */
	public static void setHero(Hero hero) {
		GameScreen.hero = hero;
	}

	/**
	 * Zwraca reference na obiekt klasy Hero.
	 * 
	 * @return Referencja na obiekt klasy Hero.
	 */
	public static Hero getHero() {
		return hero;
	}

	/**
	 * Ustawia informacjê o tym ze aktualne pomieszczenie bêdzie pierwszym
	 * pomieszczeniem (bez przeciwników).
	 * 
	 * @param firstRoom
	 *            true - pokój bêdzie bez przeciwników.
	 */
	public static void setFirstRoom(boolean firstRoom) {
		GameScreen.firstRoom = firstRoom;
	}

	/**
	 * Wywo³ywana podczas zmiany wymiarów okna aplikacji.
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	/**
	 * Wywo³ywana podczas zapauzowania aplikacji (g³ównie kiedy okno nie jest
	 * aktywne).
	 * 
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/**
	 * Wywo³ywana podczas wznowienia aplikacji (g³ównie kiedy okno jest
	 * aktywne).
	 * 
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
}
