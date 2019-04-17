package com.thegame;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.thegame.screens.GameScreen;

/**
 * Klasa reprezentuj�ca przeciwnik�w.
 * 
 * @author Norbert Wychowski, �ukasz Zachariasz
 *
 */
public class Enemy extends GameElement {

	/**
	 * Pr�dko�� poruszania si� przeciwnik�w.
	 */
	private int speedWalking;

	/**
	 * Ilo�� punkt�w �ycia przeciwnik�w.
	 */
	private int healthPoints;
	/**
	 * Pr�dko�� poruszania si� przeciwnik�w chodz�cych na ukos.
	 */
	private float diagonalSpeedWalking;

	/**
	 * Obiekt u�ywany do generowania liczb pseudolosowych, m.in. podczas
	 * losowania typu przeciwnik�w.
	 */
	private Random rm;

	/**
	 * Tablica przechowujaca klatki animacji �mierci przeciwnika.
	 */
	private TextureRegion[] animationFramesDeath;

	/**
	 * W�tek odpowiedzialny za sprawdzanie kolizji.
	 */
	private Thread collsionThread;

	/**
	 * Tablica przechowuj�ca przeciwnik�w.
	 */
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	/**
	 * Konstruktor klasy Enemy.
	 * 
	 * @param spriteBatch
	 *            Bufor na kt�rym rysowane s� tekstury.
	 * @param gameScreen
	 *            Pomieszczenie w kt�rym obecnie znajduje si� gracz i w kt�rym
	 *            maja by� przeciwnicy.
	 */
	public Enemy(SpriteBatch spriteBatch, GameScreen gameScreen) {

		super(spriteBatch, "characters/enemy_1.png");

		this.gameScreen = gameScreen;

		chooseEnemyType();

		collsionThread = new Thread((Runnable) () -> {
			while (true) {
				wallCollisionControl();
				try {
					Thread.sleep(15L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		setNumberOfFrames(8);
		setFrameEdgeSize(128);
		initFrames();

		setBounds(getX(), getY());
		setPosition(rm.nextInt(TheGame.HALF_WIDTH - TheGame.WALL_SIZE) + TheGame.WALL_SIZE,
				rm.nextInt(TheGame.HALF_HEIGHT - TheGame.WALL_SIZE) + TheGame.WALL_SIZE);

		loadAnimationFrames();

		Enemy.enemies.add(this);

	}

	/**
	 * Metoda losuj�ca typ przeciwnika. W grze wyst�puj� trzy rodzaje
	 * przeciwnik�w: zwykli - posiadaj�cy �redni� ilo�� �ycia oraz pr�dko��
	 * poruszania si�, zieloni - z mniejsz� ilo�cia �ycia ale szybsi oraz
	 * czerwoni - posiadj�cy du�o �ycia jednak powolni.
	 */
	public void chooseEnemyType() {

		rm = new Random();
		int enemyType = rm.nextInt(6);

		if (enemyType == 1) {
			super.setTexture("characters/enemy_2.png");
			speedWalking = 60 + rm.nextInt(5);
			diagonalSpeedWalking = speedWalking / 1.4f;
			healthPoints = 400;
			setAnimationFrameSpeed(0.375f);
		} else if (enemyType == 2) {
			super.setTexture("characters/enemy_3.png");
			speedWalking = 135 + rm.nextInt(13);
			diagonalSpeedWalking = speedWalking / 1.4f;
			healthPoints = 150;
			setAnimationFrameSpeed(0.133f);
		} else {
			speedWalking = 90 + rm.nextInt(10);
			diagonalSpeedWalking = speedWalking / 1.4f;
			healthPoints = 200;
			setAnimationFrameSpeed(0.25f);
		}
	}

	/**
	 * Wczytanie tekstur przeciwnik�w.
	 */
	public void loadAnimationFrames() {

		animationFramesDeath = new TextureRegion[getNumberOfFrames()];

		for (int j = 0; j < getNumberOfFrames(); j++) {

			animationFramesW[j] = frames[2][j];
			animationFramesA[j] = frames[0][j];
			animationFramesS[j] = frames[6][j];
			animationFramesD[j] = frames[4][j];

			animationFramesAW[j] = frames[1][j];
			animationFramesWD[j] = frames[3][j];
			animationFramesDS[j] = frames[5][j];
			animationFramesSA[j] = frames[7][j];
			animationFramesDeath[j] = frames[8][j];
		}

		setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesA));
	}

	/**
	 * Metoda odpowiedzialna za poruszanie si� przeciwnik�w, oraz odpowiednia
	 * animacj� zale�nie od kierunku poruszania si�.
	 * 
	 * @param delta
	 *            Czas pomi�dzy
	 */
	private void movementControl(float delta) {

		// Jesli tylko w lewo
		if (GameScreen.getHero().getX() <= this.getX()) {
			setElapsedTime(getElapsedTime() + delta);
			setPosition(getX() - speedWalking * delta, getY());
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesA));
		}

		// Jesli tylko w prawo
		if (GameScreen.getHero().getX() > this.getX()) {
			setElapsedTime(getElapsedTime() + delta);
			setPosition(getX() + speedWalking * delta, getY());
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesD));
		}

		// Jesli tylko w gore
		if (GameScreen.getHero().getY() + 25 > this.getY()) {
			setElapsedTime(getElapsedTime() + delta);
			setPosition(getX(), getY() + speedWalking * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesW));
		}

		// Jesli tylko w dol
		if (GameScreen.getHero().getY() + 25 <= this.getY()) {
			setElapsedTime(getElapsedTime() + delta);
			setPosition(getX(), getY() - speedWalking * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesS));
		}

		// Jesli tylko lewo-gora
		if (GameScreen.getHero().getY() > this.getY() && GameScreen.getHero().getX() <= this.getX()) {
			setElapsedTime(getElapsedTime() + delta);
			setPosition(getX() - diagonalSpeedWalking * delta, getY() + diagonalSpeedWalking * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesAW));
		}

		// Jesli tylko lewo-dol
		if (GameScreen.getHero().getY() < this.getY() && GameScreen.getHero().getX() <= this.getX()) {
			setElapsedTime(getElapsedTime() + delta);
			setPosition(getX() - diagonalSpeedWalking * delta, getY() - diagonalSpeedWalking * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesSA));
		}

		// Jesli tylko prawo-gora
		if (GameScreen.getHero().getY() > this.getY() && GameScreen.getHero().getX() > this.getX()) {
			setElapsedTime(getElapsedTime() + delta);
			setPosition(getX() + diagonalSpeedWalking * delta, getY() + diagonalSpeedWalking * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesWD));
		}

		// Jesli tylko prawo-dol
		if (GameScreen.getHero().getY() <= this.getY() && GameScreen.getHero().getX() > this.getX()) {
			setElapsedTime(getElapsedTime() + delta);
			setPosition(getX() + diagonalSpeedWalking * delta, getY() - diagonalSpeedWalking * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesDS));
		}

		bounds.setX(getX());
		bounds.setY(getY());
	}

	/**
	 * Metoda odpowiedzialna za sprawdzanie kolizji. Wykonywana jest w w�tku.
	 */
	private void wallCollisionControl() {

		if (bounds.overlaps(GameScreen.getWallDown())) {
			setXY(bounds.getX() + bounds.getWidth() * 0.5f, TheGame.WALL_SIZE + bounds.getWidth() * 0.5f);
		}
		if (bounds.overlaps(GameScreen.getWallUp())) {
			setXY(bounds.getX() + bounds.getWidth() * 0.5f,
					TheGame.HEIGHT - TheGame.WALL_SIZE - bounds.getWidth() * 0.5f);
		}
		if (bounds.overlaps(GameScreen.getWallLeft())) {
			setXY(TheGame.WALL_SIZE + bounds.getWidth() * 0.5f, bounds.getY() + bounds.getHeight() * 0.5f);
		}
		if (bounds.overlaps(GameScreen.getWallRight())) {
			setXY(TheGame.WIDTH - TheGame.WALL_SIZE - bounds.getWidth() * 0.5f,
					bounds.getY() + bounds.getHeight() * 0.5f);
		}

		Vector2 vEnemy = new Vector2();
		vEnemy = bounds.getCenter(vEnemy);
		for (Rectangle rec : gameScreen.impedimentList) {
			if (bounds.overlaps(rec)) {
				Vector2 vX = new Vector2();
				vX = rec.getCenter(vX);

				// kat wektora wodzacego - srodek obiektu -> srodek hero
				double alfa = Math.atan2(vEnemy.y - vX.y, vEnemy.x - vX.x) * 180.0d / Math.PI;

				// polowa kata przeciecia przekatnych - nie pytaj
				double angle = Math.tan(rec.height / rec.width) * 180.0d / Math.PI;

				// prawo [-alfa, alfa)
				if (alfa < angle && alfa >= -angle) {
					setXY(rec.getX() + rec.width + bounds.getWidth() * 0.5f, bounds.getY() + bounds.getHeight() * 0.5f);
				}
				// g�ra [alfa, 180-alfa)
				else if (alfa >= angle && alfa < 180.0d - angle) {
					setXY(bounds.getX() + bounds.getWidth() * 0.5f,
							rec.getY() + rec.height + bounds.getHeight() * 0.5f);
				}
				// d� (-180 + alfa, -alfa)
				else if (alfa < -angle && alfa >= -180.0d + angle) {
					setXY(bounds.getX() + bounds.getWidth() * 0.5f, rec.getY() - bounds.getHeight() * 0.5f);
				} // lewo [180-alfa, 180] U [-180, -180 + alfa]
				else {
					setXY(rec.getX() - bounds.getWidth() * 0.5f, bounds.getY() + bounds.getHeight() * 0.5f);
				}
			}
		}
	}

	/**
	 * Metoda wywo�ywana w momencie trafienia przeciwnika pociskiem gracza.
	 * Zabiera ona odpowiedni� ilo�� �ycia.
	 * 
	 * @param damage
	 *            Ilo�� punkt�w �ycia kt�re pocisk zabiera przeciwnikowi.
	 * @param b
	 *            Pocisk dla kt�rego ma by� odtworzony d�wi�k uderzenia.
	 */
	public void hit(int damage, Bullet b) {
		if (b.getDirection() == Input.Keys.RIGHT)
			setX(getX() + 20);
		if (b.getDirection() == Input.Keys.LEFT)
			setX(getX() - 20);
		if (b.getDirection() == Input.Keys.UP)
			setY(getY() + 20);
		if (b.getDirection() == Input.Keys.DOWN)
			setY(getY() - 20);
		healthPoints -= damage;
		b.playPunchSound();
	}

	/**
	 * Zwraca aktualn� ilo�� punkt�w �ycia.
	 * 
	 * @return Aktualna ilo�� punkt�w �ycia.
	 */
	public int getHealthPoints() {
		return healthPoints;
	}

	/*
	 * Metoda odpowiedzialna za rysowanie przeciwnik�w, animacj� oraz
	 * wywo�ywanie metody odpowiedzialnej za poruszanie si� przeciwnik�w.
	 * Dodatkowo uruchamia w�tek odpowiedzialny za sprawdzanie kolizji.
	 */
	public void render(float delta) {

		if (healthPoints > 0) {
			movementControl(delta);
			if (collsionThread.getState() == Thread.State.NEW)
				collsionThread.start();
		} else {
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesDeath));
		}
		try {
			if (!getAnimation().getKeyFrame(getElapsedTime(), true).equals(animationFramesDeath[7])) {
				setElapsedTime(getElapsedTime() + delta);
				getSpriteBatch().draw(getAnimation().getKeyFrame(getElapsedTime(), true), getX(), getY());
			} else {
				getSpriteBatch().draw(animationFramesDeath[7], getX(), getY());
			}

		} catch (IllegalStateException e) {
			getSpriteBatch().begin();
		}
	}
}
