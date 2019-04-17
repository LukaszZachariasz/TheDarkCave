package com.thegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Klasa odpowiedzialna za muzykê w grze.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public class MusicSound {

	/**
	 * Muzyka odtwarza w menu gry.
	 */
	public static Music menuMusic;
	/**
	 * Muzyka odtwarzana w trakcie gry.
	 */
	public static Music gameMusic;
	/**
	 * Muzyka odtwarzana na ekranie œmierci.
	 */
	public static Music deathMusic;

	/**
	 * Wywo³anie metod odpowiedzialnych za za³adowanie i ustawienie muzyki.
	 */
	public static void initSound() {

		loadSoundFiles();
		setSoundSettings();
	}

	/**
	 * Wczytanie muzyki z plików.
	 */
	private static void loadSoundFiles() {

		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/intro.mp3"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/battle.mp3"));
		deathMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/gameover.mp3"));

	}

	/**
	 * Ustawienie g³oœnoœci oraz zapêtlania muzyki.
	 */
	private static void setSoundSettings() {

		menuMusic.setVolume(TheGame.MUSIC_VOLUME);
		menuMusic.setLooping(true);

		gameMusic.setVolume(TheGame.MUSIC_VOLUME);
		gameMusic.setLooping(true);

		deathMusic.setVolume(TheGame.MUSIC_VOLUME);
		deathMusic.setLooping(true);

	}
}
