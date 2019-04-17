package com.thegame;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Klasa reprezentuj¹ca pociski.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public class Bullet extends GameElement {

	/** Prêdkoœæ pocisku */
	private int bulletSpeed;
	/**
	 * Kierunek pocisku - przechowujê wartoœci odpowiednich przycisków
	 * odpowiedzialnych za kierunek wystrza³u.
	 */
	private int direction;

	/** DŸwiêk odtwarzany podczas wystrzelenia pocisku. */
	private Music shootSound;

	/** DŸwiêk odtwarzany podczas kolizji pocisku z przeciwnikiem. */
	private Music punchSound;

	/** Przechowuje pociski wystrzelone przez gracza. */
	public static ArrayList<Bullet> bullets = new ArrayList<>();

	/**
	 * Konstruktor klasy Bullet.
	 * 
	 * @param spriteBatch
	 *            Bufor na którym rysowane s¹ tekstury.
	 * @param x
	 *            Wspó³rzêdna X bohatera w momencie oddania strza³u.
	 * @param y
	 *            Wspó³rzêdna Y bohatera w momencie oddania strza³u.
	 * @param direction
	 *            Kierunek pocisku.
	 * @param delta
	 *            Czas pomiêdzy kolejnymi wywo³aniami metody render().
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
	 * Utworzenie obiektów odpowiedzialnych za odtwarzanie dŸwiêku.
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
	 * Odtworzenie dŸwiêku uderzenia pocisku w przeciwnika.
	 */
	public void playPunchSound() {
		punchSound.play();
	}

	/**
	 * Zwraca predkoœæ z jak¹ porusza siê pocisk.
	 * 
	 * @return Zwraca predkoœæ pocisku.
	 */
	public int getBulletSpeed() {
		return bulletSpeed;
	}

	/**
	 * Ustawia prêdkoœæ z jak¹ ma siê poruszaæ pocisk.
	 * 
	 * @param bulletSpeed
	 *            Prêdkoœæ pocisku.
	 */
	public void setBulletSpeed(int bulletSpeed) {
		this.bulletSpeed = bulletSpeed;
	}

	/**
	 * Zwraca kierunek w którym siê porusza pocisk:
	 *  21 - w lewo, 
	 *  22 - w prawo,
	 *  19 - do góry, 
	 *  20 - w dó³.
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
	 *  19 - do góry, 
	 *  20 - w dó³.
	 * 
	 * @param direction Kierunek w jakim ma leciec pocisk.
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

}
