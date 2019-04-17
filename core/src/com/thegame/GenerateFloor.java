package com.thegame;

import java.util.Random;

import com.thegame.screens.GameScreen;

/**
 * Generator pomieszczeñ.
 * 
 * @author Norbert Wychowski, £ukasz Zachariasz
 *
 */
public class GenerateFloor {
	/**
	 * Wymiar planszy.
	 */
	private int dim;

	/**
	 * Tablica przechowuj¹ca rozk³ad pomieszczeñ.
	 */
	private GameScreen[][] floorTab;

	/**
	 * Konstruktor klasy GenerateFloor.
	 * 
	 * @param dim
	 *            Wymiar planszy.
	 * @param max
	 *            Maksymalna iloœæ pomieszczeñ.
	 * @param theGame
	 *            Referencja na obiekt klasy TheGame (potrzebna podczas
	 *            tworzenia obiektów klasy GameScreen).
	 */
	public GenerateFloor(int dim, int max, TheGame theGame) {
		this.dim = dim;
		this.floorTab = new GameScreen[dim][dim];

		if (max > dim * dim) {
			throw new RuntimeException("Za du¿a iloœæ pomieszczeñ");
		}

		Random r = new Random();
		int x = dim / 2;
		int y = dim / 2;
		int rm;

		floorTab[y][x] = new GameScreen(theGame, 0);

		for (int i = 1; i <= max; i++) {
			do {
				rm = r.nextInt(12);
				if (rm % 2 == 0) {
					if (rm < 6) {
						x--;
					} else {
						x++;
					}
					x = this.checkValue(x);
					y = this.checkRow(y);
				} else {
					if (rm < 6) {
						y--;
					} else {
						y++;
					}
					y = this.checkValue(y);
					x = this.checkCol(x);
				}
				if (i == max)
					break;
			} while (floorTab[y][x] != null);
			floorTab[y][x] = new GameScreen(theGame, r.nextInt(3));
		}
		this.connectRooms();
		// this.print();
	}

	/**
	 * Metoda ³¹cz¹ca pomieszczenia ze sob¹. Ka¿de pomieszczenie zawiera
	 * referencjê do s¹siednich pomieszczeñ.
	 */
	private void connectRooms() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (floorTab[i][j] != null) {
					if (i - 1 >= 0)
						floorTab[i][j].setOutSceneUp(floorTab[i - 1][j]);
					if (i + 1 < dim)
						floorTab[i][j].setOutSceneDown(floorTab[i + 1][j]);
					if (j - 1 >= 0)
						floorTab[i][j].setOutSceneLeft(floorTab[i][j - 1]);
					if (j + 1 < dim)
						floorTab[i][j].setOutSceneRight(floorTab[i][j + 1]);

					floorTab[i][j].drawDoors();
				}
			}
		}
	}

	/**
	 * Metoda s³u¿¹ca do wyœwietlania uk³adu pomieszczeñ.
	 */
	void print() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (floorTab[i][j] != null) {
					System.out.print(1);
				} else {
					System.out.print(0);
				}
			}
			System.out.println();
		}
		System.out.println("------------------------");
	}

	/**
	 * Sprawdza czy podany indeks jest prawid³owy. U¿ywana by sprawdziæ czy
	 * podczas generowania planszy nie opuszczono jej zakresu.
	 * 
	 * @param i
	 *            Indeks który ma zostaæ sprawdzony.
	 * @return Jeœli podany indeks "i" jest prawid³owy to zwraca go, jeœli nie
	 *         to zwraca po³owê wymiaru pomieszczenia.
	 */
	private int checkValue(int i) {
		if (i < 0 || i >= dim)
			return dim / 2;
		return i;
	}

	/**
	 * Sprawdza czy w podanym wierszu znajdujê siê miejsce na wstawienie
	 * kolejnego pomieszczenia.
	 * 
	 * @param y
	 *            Indeks wiersza który ma zostaæ sprawdzony.
	 * @return Zwraca indeks wiersza w którym mo¿na wstawiæ pomieszczenie.
	 */
	private int checkRow(int y) {
		boolean correct = false;
		for (int i = 0; i < dim; i++)
			if (floorTab[y][i] == null)
				correct = true;

		if (correct == true) {
			return y;
		} else if (y < dim - 1) {
			return y++;
		}
		return y--;
	}

	/**
	 * Sprawdza czy w podanej kolumnie znajdujê siê miejsce na wstawienie
	 * kolejnego pomieszczenia.
	 * 
	 * @param x
	 *            Indeks kolumny która ma zostaæ sprawdzona.
	 * @return Zwraca indeks kolumny w której mo¿na wstawiæ pomieszczenie.
	 */
	private int checkCol(int x) {
		boolean correct = false;
		for (int i = 0; i < dim; i++)
			if (floorTab[i][x] == null)
				correct = true;

		if (correct == true) {
			return x;
		} else if (x < dim - 1) {
			return x++;
		}
		return x--;
	}

	/**
	 * Zwraca rozk³ad pomieszczeñ.
	 * 
	 * @return Rozk³ad pomieszczeñ.
	 */
	public GameScreen[][] getFloorTab() {
		return floorTab;
	}
}