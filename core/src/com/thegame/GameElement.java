package com.thegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.thegame.screens.GameScreen;

/**
 * Klasa po kt�rej dziedzicz� klasy Bullet, Enemy oraz Hero.
 * 
 * @author Norbert Wychowski, �ukasz Zachariasz
 */
public class GameElement extends Sprite {

	/**
	 * Tekstura przechowuj�ca siatk� z poszczeg�lnymi klatkami animacji.
	 */

	private Texture texture;
	/**
	 * Bufor na kt�rym rysowane s� tekstury.
	 */
	private SpriteBatch spriteBatch;

	/**
	 * Pomieszczenie w kt�rym znajduje si� obiekt.
	 */
	protected GameScreen gameScreen;

	/**
	 * Obiekt klasy Rectangle reprezentuj�cej prostok�t. Przechowuje wsp�rz�dne
	 * obiektu.
	 */
	protected Rectangle bounds;

	/**
	 * Ilo�� klatek z kt�rych sk�ada si� animacja.
	 */
	private int numberOfFrames;
	/**
	 * Rozmiar ramki.
	 */
	private int frameEdgeSize;
	/**
	 * Szybko�� zmieniania klatek podczas animacji, podana w sekundach.
	 */
	private float animationFrameSpeed;
	/**
	 * Czas kt�ry up�yn�� od rozpocz�cia animacji.
	 */
	private float elapsedTime;

	/**
	 * Tablica przechowuj�ca wszystkie klatki animacji.
	 */
	protected TextureRegion[][] frames;

	/**
	 * Obiekt odpowiedzialny za odtwarzanie animacji.
	 */
	private Animation<TextureRegion> animation;

	/**
	 * Tablica przechowuj�ca klatki przedstawiaj�ce poruszanie si� w lewo.
	 */
	protected TextureRegion[] animationFramesA;
	/**
	 * Tablica przechowuj�ca klatki przedstawiaj�ce poruszanie si� w prawo.
	 */
	protected TextureRegion[] animationFramesD;
	/**
	 * Tablica przechowuj�ca klatki przedstawiaj�ce poruszanie si� w g�r�.
	 */
	protected TextureRegion[] animationFramesW;
	/**
	 * Tablica przechowuj�ca klatki przedstawiaj�ce poruszanie si� w d�.
	 */
	protected TextureRegion[] animationFramesS;

	/**
	 * Tablica przechowuj�ca klatki przedstawiaj�ce poruszanie si� na ukos.
	 */
	protected TextureRegion[] animationFramesAW;
	/**
	 * Tablica przechowuj�ca klatki przedstawiaj�ce poruszanie si� na ukos.
	 * 
	 */
	protected TextureRegion[] animationFramesWD;
	/**
	 * Tablica przechowuj�ca klatki przedstawiaj�ce poruszanie si� na ukos.
	 */
	protected TextureRegion[] animationFramesDS;
	/**
	 * Tablica przechowuj�ca klatki przedstawiaj�ce poruszanie si� na ukos.
	 */
	protected TextureRegion[] animationFramesSA;

	/**
	 * Konstruktor klasy GameElement.
	 * 
	 * @param spriteBatch
	 *            Bufor na kt�rym rysowane s� tekstury.
	 * @param path
	 *            �cie�ka do tekstury zawieraj�cej wszystkie klatki.
	 */
	public GameElement(SpriteBatch spriteBatch, String path) {
		setTexture(path);
		setSpriteBatch(spriteBatch);

	}

	/**
	 * Wczytanie klatek.
	 */
	public void initFrames() {

		animationFramesW = new TextureRegion[numberOfFrames];
		animationFramesA = new TextureRegion[numberOfFrames];
		animationFramesS = new TextureRegion[numberOfFrames];
		animationFramesD = new TextureRegion[numberOfFrames];

		animationFramesAW = new TextureRegion[numberOfFrames];
		animationFramesWD = new TextureRegion[numberOfFrames];
		animationFramesDS = new TextureRegion[numberOfFrames];
		animationFramesSA = new TextureRegion[numberOfFrames];

		setFrames(TextureRegion.split(getTexture(), getFrameEdgeSize(), getFrameEdgeSize()));
	}

	/**
	 * Metoda odpowiedzialna za renderowanie.
	 * 
	 * @param delta
	 *            Czas pomi�dzy poszczeg�lnymi wywo�aniami metody render().
	 */
	public void render(float delta) {

	}

	/**
	 * Zwraca aktualn� tekstur� przechowuj�c� wszystkie klatki.
	 * 
	 * @return Zwraca aktualn� tekstur� przechowuj�c� wszystkie klatki.
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * Ustawia tekstur� przechowuj�ca wszystkie klatki.
	 * 
	 * @param path
	 *            �cie�ka do tekstury.
	 */
	public void setTexture(String path) {
		this.texture = new Texture(Gdx.files.internal(path));
	}

	/**
	 * Zwraca aktualny bufor sprieBatch.
	 * 
	 * @return Zwraca bufor sprieBatch.
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
	 * Zwraca obiekt klasy Rectangle reprezentuj�cy prostok�t zawieraj�cy
	 * wymiary obiektu.
	 * 
	 * @return Zwraca prostok�t zawieraj�cy wymiary obiektu.
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Ustawia wymiary nowego prostok�tu.
	 * 
	 * @param x
	 *            Wsp�rz�dna X.
	 * @param y
	 *            Wsp�rz�dna Y.
	 */
	public void setBounds(float x, float y) {
		this.bounds = new Rectangle(x + 100, y + 100, getFrameEdgeSize() / 2, getFrameEdgeSize() / 2);
	}

	/**
	 * Ustawia po�o�enie obiektu.
	 * 
	 * @param x
	 *            Wsp�rz�dna X wskazuj�ca nowe po�o�enie �rodka obiektu.
	 * @param y
	 *            Wsp�rz�dna Y wskazuj�ca nowe po�o�enie �rodka obiektu.
	 */
	public void setXY(float x, float y) {
		bounds.setCenter(x, y);
		setPosition(bounds.getX(), bounds.getY());
	}

	/**
	 * Ustawienie w kt�rym pomieszczeniu znajduj� sie element.
	 * 
	 * @param gameScreen
	 *            Pomieszczenie w kt�rym maj� znajdowa� si� elementy.
	 */
	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		setSpriteBatch(gameScreen.getSpriteBatch());
	}

	/**
	 * Pobranie ilo�ci klatek z kt�rych sk�ada si� animacja.
	 * 
	 * @return Ilo�� klatek z kt�rych sk�ada si� animacja.
	 */
	public int getNumberOfFrames() {
		return numberOfFrames;
	}

	/**
	 * Ustawienie ilo�ci klatek z kt�rych ma sk�adac si� animacja.
	 * 
	 * @param numberOfFrames
	 *            Ilo�ci klatek z kt�rych ma sk�adac si� animacja
	 */
	public void setNumberOfFrames(int numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}

	/**
	 * Zwraca szybko�� z jak� zmieniane s� klatki animacji.
	 * 
	 * @return Szybko�� z jak� zmieniane s� klatki animacji.
	 */
	public float getAnimationFrameSpeed() {
		return animationFrameSpeed;
	}

	/**
	 * Ustawienie z jak� szybko�ci� maj� by� zmieniane klatki animacji.
	 * 
	 * @param animationFrameSpeed
	 *            Szybko�� z jak� maja by� zmieniane klatki animacji.
	 */
	public void setAnimationFrameSpeed(float animationFrameSpeed) {
		this.animationFrameSpeed = animationFrameSpeed;
	}

	/**
	 * Zwraca rozmiar ramki.
	 * 
	 * @return Rozmiar ramki.
	 */
	public int getFrameEdgeSize() {
		return frameEdgeSize;
	}

	/**
	 * Ustawia rozmiar ramki.
	 * 
	 * @param frameEdgeSize
	 *            Rozmiar ramki.
	 */
	public void setFrameEdgeSize(int frameEdgeSize) {
		this.frameEdgeSize = frameEdgeSize;
	}

	/**
	 * Zwraca obiekt odpowiedzialny za animacj�.
	 * 
	 * @return Zwraca obiekt odpowiedzialny za animacj�.
	 */
	public Animation<TextureRegion> getAnimation() {
		return animation;
	}

	/**
	 * Ustawienie animacji.
	 * 
	 * @param animation
	 *            Obiekt kt�ry ma by� odpowiedzialny za animacj�.
	 */
	public void setAnimation(Animation<TextureRegion> animation) {
		this.animation = animation;
	}

	/**
	 * Zwraca czas kt�ry up�yn�� od rozpocz�cia animacji.
	 * 
	 * @return Czas kt�ry up�yn�� od rozpocz�cia animacji.
	 */
	public float getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * Ustawia czas kt�ry up�yn�� od rozpocz�cia animacji.
	 * 
	 * @param elapsedTime
	 *            Czas kt�ry up�yn�� od rozpocz�cia animacji.
	 */
	public void setElapsedTime(float elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * Zwraca klatki.
	 * 
	 * @return Klatki.
	 */
	public TextureRegion[][] getFrames() {
		return frames;
	}

	/**
	 * Ustawia klatki animacji.
	 * 
	 * @param frames
	 *            Klatki.
	 */
	public void setFrames(TextureRegion[][] frames) {
		this.frames = frames;
	}
}
