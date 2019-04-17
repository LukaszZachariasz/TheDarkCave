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
 * Klasa po której dziedzicz¹ klasy Bullet, Enemy oraz Hero.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 */
public class GameElement extends Sprite {

	/**
	 * Tekstura przechowuj¹ca siatkê z poszczególnymi klatkami animacji.
	 */

	private Texture texture;
	/**
	 * Bufor na którym rysowane s¹ tekstury.
	 */
	private SpriteBatch spriteBatch;

	/**
	 * Pomieszczenie w którym znajduje siê obiekt.
	 */
	protected GameScreen gameScreen;

	/**
	 * Obiekt klasy Rectangle reprezentuj¹cej prostok¹t. Przechowuje wspó³rzêdne
	 * obiektu.
	 */
	protected Rectangle bounds;

	/**
	 * Iloœæ klatek z których sk³ada siê animacja.
	 */
	private int numberOfFrames;
	/**
	 * Rozmiar ramki.
	 */
	private int frameEdgeSize;
	/**
	 * Szybkoœæ zmieniania klatek podczas animacji, podana w sekundach.
	 */
	private float animationFrameSpeed;
	/**
	 * Czas który up³yn¹³ od rozpoczêcia animacji.
	 */
	private float elapsedTime;

	/**
	 * Tablica przechowuj¹ca wszystkie klatki animacji.
	 */
	protected TextureRegion[][] frames;

	/**
	 * Obiekt odpowiedzialny za odtwarzanie animacji.
	 */
	private Animation<TextureRegion> animation;

	/**
	 * Tablica przechowuj¹ca klatki przedstawiaj¹ce poruszanie siê w lewo.
	 */
	protected TextureRegion[] animationFramesA;
	/**
	 * Tablica przechowuj¹ca klatki przedstawiaj¹ce poruszanie siê w prawo.
	 */
	protected TextureRegion[] animationFramesD;
	/**
	 * Tablica przechowuj¹ca klatki przedstawiaj¹ce poruszanie siê w górê.
	 */
	protected TextureRegion[] animationFramesW;
	/**
	 * Tablica przechowuj¹ca klatki przedstawiaj¹ce poruszanie siê w dó³.
	 */
	protected TextureRegion[] animationFramesS;

	/**
	 * Tablica przechowuj¹ca klatki przedstawiaj¹ce poruszanie siê na ukos.
	 */
	protected TextureRegion[] animationFramesAW;
	/**
	 * Tablica przechowuj¹ca klatki przedstawiaj¹ce poruszanie siê na ukos.
	 * 
	 */
	protected TextureRegion[] animationFramesWD;
	/**
	 * Tablica przechowuj¹ca klatki przedstawiaj¹ce poruszanie siê na ukos.
	 */
	protected TextureRegion[] animationFramesDS;
	/**
	 * Tablica przechowuj¹ca klatki przedstawiaj¹ce poruszanie siê na ukos.
	 */
	protected TextureRegion[] animationFramesSA;

	/**
	 * Konstruktor klasy GameElement.
	 * 
	 * @param spriteBatch
	 *            Bufor na którym rysowane s¹ tekstury.
	 * @param path
	 *            Œcie¿ka do tekstury zawieraj¹cej wszystkie klatki.
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
	 *            Czas pomiêdzy poszczególnymi wywo³aniami metody render().
	 */
	public void render(float delta) {

	}

	/**
	 * Zwraca aktualn¹ teksturê przechowuj¹c¹ wszystkie klatki.
	 * 
	 * @return Zwraca aktualn¹ teksturê przechowuj¹c¹ wszystkie klatki.
	 */
	public Texture getTexture() {
		return texture;
	}

	/**
	 * Ustawia teksturê przechowuj¹ca wszystkie klatki.
	 * 
	 * @param path
	 *            Œcie¿ka do tekstury.
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
	 * Zwraca obiekt klasy Rectangle reprezentuj¹cy prostok¹t zawieraj¹cy
	 * wymiary obiektu.
	 * 
	 * @return Zwraca prostok¹t zawieraj¹cy wymiary obiektu.
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Ustawia wymiary nowego prostok¹tu.
	 * 
	 * @param x
	 *            Wspó³rzêdna X.
	 * @param y
	 *            Wspó³rzêdna Y.
	 */
	public void setBounds(float x, float y) {
		this.bounds = new Rectangle(x + 100, y + 100, getFrameEdgeSize() / 2, getFrameEdgeSize() / 2);
	}

	/**
	 * Ustawia po³o¿enie obiektu.
	 * 
	 * @param x
	 *            Wspó³rzêdna X wskazuj¹ca nowe po³o¿enie œrodka obiektu.
	 * @param y
	 *            Wspó³rzêdna Y wskazuj¹ca nowe po³o¿enie œrodka obiektu.
	 */
	public void setXY(float x, float y) {
		bounds.setCenter(x, y);
		setPosition(bounds.getX(), bounds.getY());
	}

	/**
	 * Ustawienie w którym pomieszczeniu znajdujê sie element.
	 * 
	 * @param gameScreen
	 *            Pomieszczenie w którym maj¹ znajdowaæ siê elementy.
	 */
	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
		setSpriteBatch(gameScreen.getSpriteBatch());
	}

	/**
	 * Pobranie iloœci klatek z których sk³ada siê animacja.
	 * 
	 * @return Iloœæ klatek z których sk³ada siê animacja.
	 */
	public int getNumberOfFrames() {
		return numberOfFrames;
	}

	/**
	 * Ustawienie iloœci klatek z których ma sk³adac siê animacja.
	 * 
	 * @param numberOfFrames
	 *            Iloœci klatek z których ma sk³adac siê animacja
	 */
	public void setNumberOfFrames(int numberOfFrames) {
		this.numberOfFrames = numberOfFrames;
	}

	/**
	 * Zwraca szybkoœæ z jak¹ zmieniane s¹ klatki animacji.
	 * 
	 * @return Szybkoœæ z jak¹ zmieniane s¹ klatki animacji.
	 */
	public float getAnimationFrameSpeed() {
		return animationFrameSpeed;
	}

	/**
	 * Ustawienie z jak¹ szybkoœci¹ maj¹ byæ zmieniane klatki animacji.
	 * 
	 * @param animationFrameSpeed
	 *            Szybkoœæ z jak¹ maja byæ zmieniane klatki animacji.
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
	 * Zwraca obiekt odpowiedzialny za animacjê.
	 * 
	 * @return Zwraca obiekt odpowiedzialny za animacjê.
	 */
	public Animation<TextureRegion> getAnimation() {
		return animation;
	}

	/**
	 * Ustawienie animacji.
	 * 
	 * @param animation
	 *            Obiekt który ma byæ odpowiedzialny za animacjê.
	 */
	public void setAnimation(Animation<TextureRegion> animation) {
		this.animation = animation;
	}

	/**
	 * Zwraca czas który up³yn¹³ od rozpoczêcia animacji.
	 * 
	 * @return Czas który up³yn¹³ od rozpoczêcia animacji.
	 */
	public float getElapsedTime() {
		return elapsedTime;
	}

	/**
	 * Ustawia czas który up³yn¹³ od rozpoczêcia animacji.
	 * 
	 * @param elapsedTime
	 *            Czas który up³yn¹³ od rozpoczêcia animacji.
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
