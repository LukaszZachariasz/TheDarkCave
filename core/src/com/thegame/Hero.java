package com.thegame;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.thegame.screens.GameScreen;

/**
 * Klasa reprezentuj¹ca gracza.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public class Hero extends GameElement {

	/**
	 * Prêdkoœæ chodzenia.
	 */
	private int speedWalking;
	/**
	 * Prêdkoœæ biegania.
	 */
	private int speedRunning;
	/**
	 * Aktualna prêdkoœæ z jak¹ porusza siê gracz.
	 */
	private int speed;
	/**
	 * Iloœæ puntków ¿ycia gracza.
	 */
	private int healthPoints;

	/**
	 * Aktualna prêdkoœæ gracza podczas poruszania siê na ukos.
	 */
	private float diagonalSpeed;
	/**
	 * Prêdkoœæ gracza podczas biegania na ukos.
	 */
	private float diagonalSpeedRunning;
	/**
	 * Prêdkoœæ gracza podczas chodzenia na ukos.
	 */
	private float diagonalSpeedWalking;
	/**
	 * Czas pomiêdzy nastêpnymi strza³ami.
	 */
	private float shotDelay;
	/**
	 * Licznik odpowiedzialny za opóŸnienie w otrzymywaniu obra¿eñ. Gracz mo¿e
	 * otrzymaæ obra¿enia raz na 2 sekundy.
	 */
	private Timer timerHP;
	/**
	 * DŸwiêk odtwarzany podczas trafienia gracza przez przeciwnika.
	 */
	public static Music hitSound;
	/**
	 * Tablica przechowuj¹ca aktualne klatki animacji.
	 */
	private TextureRegion[] animationFramesIdle;
	/**
	 * W¹tek odpowiedzialny za sprawdzanie kolizji.
	 */
	private Thread collisionThread;

	/**
	 * Konstruktor klasy Hero.
	 * 
	 * @param spriteBatch
	 *            Bufor na którym rysowane s¹ tekstury.
	 */
	public Hero(SpriteBatch spriteBatch) {
		super(spriteBatch, "characters/hero_1.png");
		setPosition(TheGame.HALF_WIDTH, TheGame.HALF_HEIGHT);

		initSound();

		timerHP = new Timer();
		collisionThread = new Thread((Runnable) () -> {
			while (true) {
				wallCollisionControl();
				try {
					Thread.sleep(15L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		healthPoints = 6;
		speedWalking = 500;
		speedRunning = 700;
		speedWalking = 450;
		speedRunning = 650;
		shotDelay = -1f;
		diagonalSpeedRunning = speedRunning / 1.4f;
		diagonalSpeedWalking = speedWalking / 1.4f;

		setFrameEdgeSize(128);
		setAnimationFrameSpeed(0.06f);
		setNumberOfFrames(8);
		initFrames();

		initAnimationFrames();

		setBounds(getX(), getY());
	}

	/**
	 * Wczytanie dŸwiêków.
	 */
	private void initSound() {
		hitSound = Gdx.audio.newMusic(Gdx.files.internal("sound/hit.mp3"));
		hitSound.setVolume(TheGame.FX_VOLUME);
		hitSound.setLooping(false);
	}

	/**
	 * Wczytanie klatek.
	 */
	private void initAnimationFrames() {

		animationFramesIdle = new TextureRegion[getNumberOfFrames()];

		for (int j = 0; j < getNumberOfFrames(); j++) {

			animationFramesW[j] = frames[2][j];
			animationFramesA[j] = frames[0][j];
			animationFramesS[j] = frames[6][j];
			animationFramesD[j] = frames[4][j];

			animationFramesAW[j] = frames[1][j];
			animationFramesWD[j] = frames[3][j];
			animationFramesDS[j] = frames[5][j];
			animationFramesSA[j] = frames[7][j];
			animationFramesIdle[j] = frames[8][j];
		}

		setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesIdle));
	}

	/**
	 * Metoda odpowiedzialna za renderowanie. Wywo³uje ona metody
	 * movementControl(), shootingControl() oraz uruchamia w¹tek odpowiedzialny
	 * za sprawdzanie kolizji.
	 * 
	 * @param delta
	 *            Czas pomiêdzy poszczególnymi wywo³aniami metody render().
	 */
	public void render(float delta) {
		movementControl(delta);
		shootingControl(delta);
		if (collisionThread.getState() == Thread.State.NEW)
			collisionThread.start();
	}

	/**
	 * Metoda odpowiedzialna za poruszanie siê gracza oraz animacje ruchu.
	 * 
	 * @param delta
	 *            Czas pomiêdzy poszczególnymi wywo³aniami metody render().
	 */
	private void movementControl(float delta) {

		if (getSpriteBatch() != null && getSpriteBatch().isDrawing())
			getSpriteBatch().draw((TextureRegion) getAnimation().getKeyFrame(getElapsedTime(), true), getX(), getY());

		setElapsedTime(getElapsedTime() + delta);
		setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesIdle));

		// Jesli tylko A
		if (Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D)
				&& !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
			setPosition(getX() - speed * delta, getY());
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesA));
		}
		// Jesli tylko D
		if (Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.W)
				&& !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
			setPosition(getX() + speed * delta, getY());
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesD));
		}

		// Jesli tylko W
		if (Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A)
				&& !Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.S)) {
			setPosition(getX(), getY() + speed * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesW));
		}

		// Jesli tylko S
		if (Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.D)
				&& !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
			setPosition(getX(), getY() - speed * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesS));
		}

		// Jesli tylko A i W
		if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)
				&& !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.D)) {
			setPosition(getX() - diagonalSpeed * delta, getY() + diagonalSpeed * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesAW));
		}

		// Jesli tylko A i S
		if (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)
				&& !Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.W)) {
			setPosition(getX() - diagonalSpeed * delta, getY() - diagonalSpeed * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesSA));
		}

		// Jesli tylko D i W
		if (Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W)
				&& !Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
			setPosition(getX() + diagonalSpeed * delta, getY() + diagonalSpeed * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesWD));
		}

		// Jesli tylko S i D
		if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)
				&& !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A)) {
			setPosition(getX() + diagonalSpeed * delta, getY() - diagonalSpeed * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesDS));
		}

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.W)
						|| Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.A))) {
			this.speed = speedRunning;
			this.diagonalSpeed = diagonalSpeedRunning;
			setElapsedTime(getElapsedTime() + Gdx.graphics.getDeltaTime());
		} else {
			this.speed = speedWalking;
			this.diagonalSpeed = diagonalSpeedWalking;
		}

		bounds.setX(getX());
		bounds.setY(getY());
	}

	/**
	 * Metoda sprawdzaj¹ca kolizjê. Wykonywana jest w w¹tku.
	 */
	private void wallCollisionControl() {
		Rectangle boundsDoorDown = gameScreen.getBoundsDoorDown();
		Rectangle boundsDoorLeft = gameScreen.getBoundsDoorLeft();
		Rectangle boundsDoorRight = gameScreen.getBoundsDoorRight();
		Rectangle boundsDoorUp = gameScreen.getBoundsDoorUp();

		GameScreen up = gameScreen.getOutSceneUp();
		GameScreen down = gameScreen.getOutSceneDown();
		GameScreen left = gameScreen.getOutSceneLeft();
		GameScreen right = gameScreen.getOutSceneRight();

		synchronized (gameScreen) {
			if (bounds.overlaps(GameScreen.getWallDown())) {
				if (gameScreen.isRoomBlocked() == false && boundsDoorDown != null && bounds.overlaps(boundsDoorDown)) {
					if (bounds.getY() + bounds.getHeight() * 0.25f < 0) {
						Bullet.bullets.clear();
						Enemy.enemies.clear();
						gameScreen.playChangeRoomSound();
						gameScreen.getTheGame().setGameScreen(down);
						setXY(TheGame.WIDTH * 0.5f, TheGame.HEIGHT - bounds.getHeight());
						setGameScreen(down);
						return;
					}
				} else {
					setXY(bounds.getX() + bounds.getWidth() * 0.5f, TheGame.WALL_SIZE + bounds.getWidth() * 0.5f);
				}
			}
			if (bounds.overlaps(GameScreen.getWallUp())) {
				if (gameScreen.isRoomBlocked() == false && boundsDoorUp != null && bounds.overlaps(boundsDoorUp)) {
					if (bounds.getY() + bounds.getHeight() * 0.75f > TheGame.HEIGHT) {
						Bullet.bullets.clear();
						Enemy.enemies.clear();
						gameScreen.playChangeRoomSound();
						gameScreen.getTheGame().setGameScreen(up);
						setXY(TheGame.WIDTH * 0.5f, bounds.getHeight());
						setGameScreen(up);
						return;
					}
				} else {
					setXY(bounds.getX() + bounds.getWidth() * 0.5f,
							TheGame.HEIGHT - TheGame.WALL_SIZE - bounds.getWidth() * 0.5f);
				}
			}
			if (bounds.overlaps(GameScreen.getWallLeft())) {
				if (gameScreen.isRoomBlocked() == false && boundsDoorLeft != null && bounds.overlaps(boundsDoorLeft)) {
					if (bounds.getX() + bounds.getWidth() * 0.25f < 0) {
						Bullet.bullets.clear();
						Enemy.enemies.clear();
						gameScreen.playChangeRoomSound();
						gameScreen.getTheGame().setGameScreen(left);
						setXY(TheGame.WIDTH - bounds.getWidth(), TheGame.HEIGHT * 0.5f);
						setGameScreen(left);
						return;
					}
				} else {
					setXY(TheGame.WALL_SIZE + bounds.getWidth() * 0.5f, bounds.getY() + bounds.getHeight() * 0.5f);
				}
			}
			if (bounds.overlaps(GameScreen.getWallRight())) {
				if (gameScreen.isRoomBlocked() == false && boundsDoorRight != null
						&& bounds.overlaps(boundsDoorRight)) {
					if (bounds.getX() + bounds.getWidth() * 0.75f > TheGame.WIDTH) {
						Bullet.bullets.clear();
						Enemy.enemies.clear();
						gameScreen.playChangeRoomSound();
						gameScreen.getTheGame().setGameScreen(right);
						setXY(bounds.getWidth(), TheGame.HEIGHT * 0.5f);
						setGameScreen(right);
						return;
					}
				} else {
					setXY(TheGame.WIDTH - TheGame.WALL_SIZE - bounds.getWidth() * 0.5f,
							bounds.getY() + bounds.getWidth() * 0.5f);
				}
			}
		}

		Vector2 vHero = new Vector2();
		vHero = bounds.getCenter(vHero);
		for (Rectangle rec : gameScreen.impedimentList) {
			if (bounds.overlaps(rec)) {
				Vector2 vX = new Vector2();
				vX = rec.getCenter(vX);

				// kat wektora wodzacego - srodek obiektu -> srodek hero
				double alfa = Math.atan2(vHero.y - vX.y, vHero.x - vX.x) * 180.0d / Math.PI;

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
	 * Metoda odpowiedzialna za kontrolê pocisków podczas strzelania przez
	 * gracza. Po oddaniu strza³u tworzy nowy pocisk oraz ustawia odpowiednie
	 * opó¿nienie.
	 * 
	 * @param delta
	 *            Czas pomiêdzy kolejnymi wywo³aniami metody.
	 */
	private void shootingControl(float delta) {

		shotDelay -= delta;

		Iterator<Bullet> i = Bullet.bullets.iterator();

		while (i.hasNext()) {
			Bullet b = i.next();
			b.render(delta);

			if (b.getX() < 0 || b.getX() > TheGame.WIDTH || b.getY() < 0 || b.getY() > TheGame.HEIGHT) {
				i.remove();
			}

		}

		if (shotDelay <= 0) {

			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				new Bullet(getSpriteBatch(), getX(), getY(), Input.Keys.UP, delta);
			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				new Bullet(getSpriteBatch(), getX(), getY(), Input.Keys.DOWN, delta);
			} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				new Bullet(getSpriteBatch(), getX(), getY(), Input.Keys.LEFT, delta);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				new Bullet(getSpriteBatch(), getX(), getY(), Input.Keys.RIGHT, delta);
			}

			shotDelay += 0.4f;
		}

	}

	/**
	 * Metoda wywo³ywana w momencie trafienia gracza przez przeciwnika.
	 * 
	 * @param damage
	 *            Iloœæ punktów ¿ycia które przeciwnik zabra³ graczowi.
	 */
	public void hit(int damage) {
		if (timerHP.isEmpty()) {
			healthPoints -= damage;
			hitSound.play();
			timerHP.scheduleTask(new Task() {
				@Override
				public void run() {
					timerHP.clear();
				}
			}, 2);
			timerHP.start();
		}
	}

	/**
	 * Zwraca aktualn¹ iloœæ punktów ¿ycia gracza.
	 * 
	 * @return Iloœæ punktów ¿ycia gracza.
	 */
	public int getHealthPoints() {
		return healthPoints;
	}
}
