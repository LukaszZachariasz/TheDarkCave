package com.thegame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Klasa reprezentuj�ca pociski.
 * 
 * @author Norbert Wychowski, �ukasz Zachariasz
 *
 */
public class Bullet extends GameElement {

	/** Pr�dko�� pocisku */
	private int bulletSpeed;
	/**
	 * Kierunek pocisku - przechowuj� warto�ci odpowiednich przycisk�w
	 * odpowiedzialnych za kierunek wystrza�u.
	 */
	private int direction;

	/** D�wi�k odtwarzany podczas wystrzelenia pocisku. */
	private Music shootSound;

	/** D�wi�k odtwarzany podczas kolizji pocisku z przeciwnikiem. */
	private Music punchSound;

	/** Przechowuje pociski wystrzelone przez gracza. */
	public static ArrayList<Bullet> bullets = new ArrayList<>();

	/**
	 * Konstruktor klasy Bullet.
	 * 
	 * @param spriteBatch
	 *            Bufor na kt�rym rysowane s� tekstury.
	 * @param x
	 *            Wsp�rz�dna X bohatera w momencie oddania strza�u.
	 * @param y
	 *            Wsp�rz�dna Y bohatera w momencie oddania strza�u.
	 * @param direction
	 *            Kierunek pocisku.
	 * @param delta
	 *            Czas pomi�dzy kolejnymi wywo�aniami metody render().
	 */
	public Bullet(SpriteBatch spriteBatch, float x, float y, int direction, float delta) {
		super(spriteBatch, "bullet.png");

		setPosition(x + 25, y + 50);
		setDirection(direction);
		setBulletSpeed(900);
		setFrameEdgeSize(64);
		setNumberOfFrames(8);
		initFrames();
		initSound();
		setAnimationFrameSpeed(0.05f);
		setBounds(x, y);
		loadAnimationFrames();
		render(delta);
		bullets.add(this);
	}

	/**
	 * Utworzenie obiekt�w odpowiedzialnych za odtwarzanie d�wi�ku.
	 */
	private void initSound() {

		punchSound = Gdx.audio.newMusic(Gdx.files.internal("sound/punch.mp3"));
		shootSound = Gdx.audio.newMusic(Gdx.files.internal("sound/shoot.mp3"));

		punchSound.setVolume(TheGame.FX_VOLUME);
		punchSound.setLooping(false);

		shootSound.setVolume(TheGame.FX_VOLUME);
		shootSound.setLooping(false);

		shootSound.play();
	}

	/**
	 * Wczytanie tekstur animacji pocisku.
	 */
	private void loadAnimationFrames() {
		for (int j = 0; j < getNumberOfFrames(); j++) {

			animationFramesW[j] = frames[2][j];
			animationFramesA[j] = frames[0][j];
			animationFramesS[j] = frames[6][j];
			animationFramesD[j] = frames[4][j];
		}
	}
	
	/**
	 * @see com.thegame.GameElement#render(float)
	 */
	public void render(float delta) {

		if (getDirection() == Input.Keys.LEFT) {
			setPosition(getX() - bulletSpeed * delta, getY());
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesA));
		} else if (getDirection() == Input.Keys.RIGHT) {
			setPosition(getX() + bulletSpeed * delta, getY());
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesD));
		} else if (getDirection() == Input.Keys.UP) {
			setPosition(getX(), getY() + bulletSpeed * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesW));
		} else if (getDirection() == Input.Keys.DOWN) {
			setPosition(getX(), getY() - bulletSpeed * delta);
			setAnimation(new Animation<>(getAnimationFrameSpeed(), animationFramesS));
		}

		if (getAnimation() != null)
			getSpriteBatch().draw(getAnimation().getKeyFrame(getElapsedTime(), true), getX(), getY());

		setElapsedTime(getElapsedTime() + delta);

		bounds.setX(getX());
		bounds.setY(getY());
	}

	/**
	 * Odtworzenie d�wi�ku uderzenia pocisku w przeciwnika.
	 */
	public void playPunchSound() {
		punchSound.play();
	}

	/**
	 * Zwraca predko�� z jak� porusza si� pocisk.
	 * 
	 * @return Zwraca predko�� pocisku.
	 */
	public int getBulletSpeed() {
		return bulletSpeed;
	}

	/**
	 * Ustawia pr�dko�� z jak� ma si� porusza� pocisk.
	 * 
	 * @param bulletSpeed
	 *            Pr�dko�� pocisku.
	 */
	public void setBulletSpeed(int bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	/**
	 * Zwraca kierunek w kt�rym si� porusza pocisk:
	 *  21 - w lewo, 
	 *  22 - w prawo,
	 *  19 - do g�ry, 
	 *  20 - w d�.
	 * 
	 * @return Zwraca kierunek pocisku.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Ustawia kierunek lotu pocisku:
	 *  21 - w lewo, 
	 *  22 - w prawo,
	 *  19 - do g�ry, 
	 *  20 - w d�.
	 * 
	 * @param direction Kierunek w jakim ma leciec pocisk.
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

}
