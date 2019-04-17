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
 * Klasa reprezentuj¹ca przeciwników.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public class Enemy extends GameElement {

	/**
	 * Prêdkoœæ poruszania siê przeciwników.
	 */
	private int speedWalking;

	/**
	 * Iloœæ punktów ¿ycia przeciwników.
	 */
	private int healthPoints;
	/**
	 * Prêdkoœæ poruszania siê przeciwników chodz¹cych na ukos.
	 */
	private float diagonalSpeedWalking;

	/**
	 * Obiekt u¿ywany do generowania liczb pseudolosowych, m.in. podczas
	 * losowania typu przeciwników.
	 */
	private Random rm;

	/**
	 * Tablica przechowujaca klatki animacji œmierci przeciwnika.
	 */
	private TextureRegion[] animationFramesDeath;

	/**
	 * W¹tek odpowiedzialny za sprawdzanie kolizji.
	 */
	private Thread collsionThread;

	/**
	 * Tablica przechowuj¹ca przeciwników.
	 */
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	/**
	 * Konstruktor klasy Enemy.
	 * 
	 * @param spriteBatch
	 *            Bufor na którym rysowane s¹ tekstury.
	 * @param gameScreen
	 *            Pomieszczenie w którym obecnie znajduje siê gracz i w którym
	 *            maja byæ przeciwnicy.
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
	 * Metoda losuj¹ca typ przeciwnika. W grze wystêpuj¹ trzy rodzaje
	 * przeciwników: zwykli - posiadaj¹cy œredni¹ iloœæ ¿ycia oraz prêdkoœæ
	 * poruszania siê, zieloni - z mniejsz¹ iloœcia ¿ycia ale szybsi oraz
	 * czerwoni - posiadj¹cy du¿o ¿ycia jednak powolni.
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
	 * Wczytanie tekstur przeciwników.
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
	 * Metoda odpowiedzialna za poruszanie siê przeciwników, oraz odpowiednia
	 * animacjê zale¿nie od kierunku poruszania siê.
	 * 
	 * @param delta
	 *            Czas pomiêdzy
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
	 * Metoda odpowiedzialna za sprawdzanie kolizji. Wykonywana jest w w¹tku.
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
				// góra [alfa, 180-alfa)
				else if (alfa >= angle && alfa < 180.0d - angle) {
					setXY(bounds.getX() + bounds.getWidth() * 0.5f,
							rec.getY() + rec.height + bounds.getHeight() * 0.5f);
				}
				// dó³ (-180 + alfa, -alfa)
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
	 * Metoda wywo³ywana w momencie trafienia przeciwnika pociskiem gracza.
	 * Zabiera ona odpowiedni¹ iloœæ ¿ycia.
	 * 
	 * @param damage
	 *            Iloœæ punktów ¿ycia które pocisk zabiera przeciwnikowi.
	 * @param b
	 *            Pocisk dla którego ma byæ odtworzony dŸwiêk uderzenia.
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
	 * Zwraca aktualn¹ iloœæ punktów ¿ycia.
	 * 
	 * @return Aktualna iloœæ punktów ¿ycia.
	 */
	public int getHealthPoints() {
		return healthPoints;
	}

	/*
	 * Metoda odpowiedzialna za rysowanie przeciwników, animacjê oraz
	 * wywo³ywanie metody odpowiedzialnej za poruszanie siê przeciwników.
	 * Dodatkowo uruchamia w¹tek odpowiedzialny za sprawdzanie kolizji.
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
