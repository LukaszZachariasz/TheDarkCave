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
 * Ekran wy�wietlaj�cy pomieszczenie w kt�rym obecnie rozgrywa si� akcja.
 * 
 * @author Norbert Wychowski, �ukasz Zachariasz
 *
 */
public class GameScreen extends AbstractScreen {
	/**
	 * Obiekt klasy Hero reprezentuj�cy g��wnego bohatera.
	 */
	private static Hero hero;
	/**
	 * Zawiera informacj� o tym czy gracz znajduje si� w pierwszym pomieszczeniu
	 * (bez przeciwnik�w).
	 */
	private static boolean firstRoom = true;

	/**
	 * Tekstura t�a.
	 */
	private Texture background;
	/**
	 * Tekstura pokazuj�ca ile gracz ma �ycia.
	 */
	private Texture health;

	/**
	 * Ilo�� przeciwnik�w w pomieszczeniu.
	 */
	private int spawnedEnemies;
	/**
	 * Maksymalna ilo�� przeciwnik�w w pomieszczeniu.
	 */
	private int maxEnemies;
	/**
	 * Typ pomieszczenia (pomieszczenia r�ni� si� uk�adem przeszk�d).
	 */
	private int roomType;

	/**
	 * S�u�y do losowania maksymalnej ilo�ci przeciwnik�w.
	 */
	private Random randMaxEnemies;
	/**
	 * D�wi�k przechodzenia mi�dzy pomieszczeniami.
	 */
	public Music gateSound;

	/**
	 * Pomieszczenie znajduj�ce si� powy�ej.
	 */
	private GameScreen outSceneUp;
	/**
	 * Pomieszczenie znajduj�ce si� poni�ej.
	 */
	private GameScreen outSceneDown;
	/**
	 * Pomieszczenie znajduj�ce si� po lewej stronie.
	 */
	private GameScreen outSceneLeft;
	/**
	 * Pomieszczenie znajduj�ce si� po prawej stronie.
	 */
	private GameScreen outSceneRight;
	/**
	 * Obiekt klasy Rectangle zawieraj�cy wsp�rz�dne drzwi znajduj�cych si� na
	 * g�rze.
	 */
	private Rectangle boundsDoorUp;
	/**
	 * Obiekt klasy Rectangle zawieraj�cy wsp�rz�dne drzwi znajduj�cych si� na
	 * dole.
	 */
	private Rectangle boundsDoorDown;
	/**
	 * Obiekt klasy Rectangle zawieraj�cy wsp�rz�dne drzwi znajduj�cych si� po
	 * lewej stronie.
	 */
	private Rectangle boundsDoorLeft;
	/**
	 * Obiekt klasy Rectangle zawieraj�cy wsp�rz�dne drzwi znajduj�cych si� po
	 * prawej stronie.
	 */
	private Rectangle boundsDoorRight;

	/**
	 * Obiekt klasy Rectangle zawieraj�cy wsp�rz�dne g�rnej �ciany.
	 */
	private static Rectangle wallUp;
	/**
	 * Obiekt klasy Rectangle zawieraj�cy wsp�rz�dne g�rnej �ciany.
	 */
	private static Rectangle wallDown;
	/**
	 * Obiekt klasy Rectangle zawieraj�cy wsp�rz�dne �ciany znajduj�cej si� po
	 * lewej stronie.
	 */
	private static Rectangle wallLeft;
	/**
	 * Obiekt klasy Rectangle zawieraj�cy wsp�rz�dne �ciany znajduj�cej si� po
	 * prawej stronie.
	 */
	private static Rectangle wallRight;

	/**
	 * Okre�la czy gracz mo�e opu�ci� pomieszczenie (tylko je�li pokona
	 * wszystkich przeciwnik�w).
	 */
	private boolean roomBlocked = true;
	/**
	 * Zmienna pomocniczna. U�ywana jest na pocz�tku by metoda render narysowa�a
	 * drzwi tylko raz.
	 */
	private boolean locker = false;

	/**
	 * Przechowuj� informacj� o wsp�rz�dnych obiekt�w znajduj�cych si� na
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
	 * Odtwarza d�wi�k podczas przechodzenia mi�dzy pomieszczeniami.
	 */
	public void playChangeRoomSound() {
		gateSound.play();
	}

	/**
	 * Metoda rysuj�ca drzwi na teksturze t�a.
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
	 * Wywo�ywana w momencie kiedy obiekt ma zwolni� zasoby.
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
	 * Wywo�ywana w momencie kiedy staj� si� aktualnym ekranem wy�wietlanym
	 * przez Game.
	 * 
	 * @see com.thegame.screens.AbstractScreen#show()
	 */
	@Override
	public void show() {
		MusicSound.gameMusic.play();
	}

	/**
	 * Metoda s�u�aca do renderowania pomieszczenia. Odpowiada za rysowanie
	 * tekstur, wywolywanie metod render() znajduj�cych si� w obiektach klasy
	 * Enemy oraz Hero. Dodatkowo odpowiada za sprawdzanie kolizji pocisk�w i
	 * przeciwnik�w.
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
	 * Wywo�ywana w momencie kiedy ekran nie jest ju� d�u�ej u�ywany przez Game.
	 * 
	 * @see com.thegame.screens.AbstractScreen#hide()
	 */
	@Override
	public void hide() {
		MusicSound.gameMusic.pause();
	}

	/**
	 * Ustawia pomieszczenie znajduj�ce si� powy�ej.
	 * 
	 * @param outSceneUp
	 *            Pomieszczenie znajduj�ce si� powy�ej.
	 */
	public void setOutSceneUp(GameScreen outSceneUp) {
		this.outSceneUp = outSceneUp;
	}

	/**
	 * Ustawia pomieszczenie znajduj�ce si� poni�ej.
	 * 
	 * @param outSceneDown
	 *            Pomieszczenie znajduj�ce si� poni�ej.
	 */
	public void setOutSceneDown(GameScreen outSceneDown) {
		this.outSceneDown = outSceneDown;
	}

	/**
	 * Ustawia pomieszczenie znajduj�ce si� po lewej stronie.
	 * 
	 * @param outSceneLeft
	 *            Pomieszczenie znajduj�ce si� po lewej stronie.
	 */
	public void setOutSceneLeft(GameScreen outSceneLeft) {
		this.outSceneLeft = outSceneLeft;
	}

	/**
	 * Ustawia pomieszczenie znajduj�ce si� po prawej stronie.
	 * 
	 * @param outSceneRight
	 *            Pomieszczenie znajduj�ce si� po prawej stronie.
	 */
	public void setOutSceneRight(GameScreen outSceneRight) {
		this.outSceneRight = outSceneRight;
	}

	/**
	 * Zwraca pomieszczenie znajduj�ce si� powy�ej.
	 * 
	 * @return Pomieszczenie znajduj�ce si� powy�ej.
	 */
	public GameScreen getOutSceneUp() {
		return outSceneUp;
	}

	/**
	 * Zwraca pomieszczenie znajduj�ce si� poni�ej.
	 * 
	 * @return Pomieszczenie znajduj�ce si� poni�ej.
	 */
	public GameScreen getOutSceneDown() {
		return outSceneDown;
	}

	/**
	 * Zwraca pomieszczenie znajduj�ce si� po lewej stronie.
	 * 
	 * @return Pomieszczenie znajduj�ce si� po lewej stronie.
	 */
	public GameScreen getOutSceneLeft() {
		return outSceneLeft;
	}

	/**
	 * Zwraca pomieszczenie znajduj�ce si� po prawej stronie.
	 * 
	 * @return Pomieszczenie znajduj�ce si� po prawej stronie.
	 */
	public GameScreen getOutSceneRight() {
		return outSceneRight;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy wymiary drzwi znajduj�cych
	 * si� powy�ej.
	 * 
	 * @return Zwraca wymiary drzwi znajduj�cych si� powy�ej.
	 */
	public Rectangle getBoundsDoorUp() {
		return boundsDoorUp;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy wymiary drzwi znajduj�cych
	 * si� poni�ej.
	 * 
	 * @return Zwraca wymiary drzwi znajduj�cych si� poni�ej.
	 */
	public Rectangle getBoundsDoorDown() {
		return boundsDoorDown;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy wymiary drzwi znajduj�cych
	 * si� po lewej stronie.
	 * 
	 * @return Zwraca wymiary drzwi znajduj�cych si� po lewej stronie.
	 */
	public Rectangle getBoundsDoorLeft() {
		return boundsDoorLeft;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy wymiary drzwi znajduj�cych
	 * si� po prawej stronie.
	 * 
	 * @return Zwraca wymiary drzwi znajduj�cych si� po prawej stronie.
	 */
	public Rectangle getBoundsDoorRight() {
		return boundsDoorRight;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy wymiary �ciany znajduj�cej
	 * si� na g�rze.
	 * 
	 * @return Zwraca wymiary ciany znajduj�cej si� na g�rze.
	 */
	public static Rectangle getWallUp() {
		return wallUp;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy wymiary �ciany znajduj�cej
	 * si� na dole.
	 * 
	 * @return Zwraca wymiary ciany znajduj�cej si� na dole.
	 */
	public static Rectangle getWallDown() {
		return wallDown;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy wymiary �ciany znajduj�cej
	 * si� po lewej stronie.
	 * 
	 * @return Zwraca wymiary ciany znajduj�cej si� po lewej stronie.
	 */
	public static Rectangle getWallLeft() {
		return wallLeft;
	}

	/**
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy wymiary �ciany znajduj�cej
	 * si� po prawej stronie.
	 * 
	 * @return Zwraca wymiary ciany znajduj�cej si� po prawej stronie.
	 */
	public static Rectangle getWallRight() {
		return wallRight;
	}

	/**
	 * Zwraca informacj� dotycz�ca tego czy gracz mo�e opu�cic pomieszczenie.
	 * 
	 * @return true - gracz moze opu�cic pomieszczenie, false - nie mo�e.
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
	 * Ustawia informacj� o tym ze aktualne pomieszczenie b�dzie pierwszym
	 * pomieszczeniem (bez przeciwnik�w).
	 * 
	 * @param firstRoom
	 *            true - pok�j b�dzie bez przeciwnik�w.
	 */
	public static void setFirstRoom(boolean firstRoom) {
		GameScreen.firstRoom = firstRoom;
	}

	/**
	 * Wywo�ywana podczas zmiany wymiar�w okna aplikacji.
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	/**
	 * Wywo�ywana podczas zapauzowania aplikacji (g��wnie kiedy okno nie jest
	 * aktywne).
	 * 
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	/**
	 * Wywo�ywana podczas wznowienia aplikacji (g��wnie kiedy okno jest
	 * aktywne).
	 * 
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
}
