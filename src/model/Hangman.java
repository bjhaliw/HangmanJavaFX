package model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Hangman {

	private ArrayList<String> wordList;
	private int numStrikes;

	public Hangman() {
		this.numStrikes = 0;
		this.wordList = new ArrayList<>();
		loadWordList();
	}

	private void loadWordList() {
		InputStream is = getClass().getResourceAsStream("/words_alpha.txt");
		Scanner scanner = new Scanner(is);

		while (scanner.hasNext()) {
			this.wordList.add(scanner.nextLine());
		}

		scanner.close();
	}

	public String pickRandomWord() {
		String output = null;

		if (this.wordList.size() > 0) {
			Random rand = new Random();

			output = this.wordList.get(rand.nextInt(this.wordList.size() - 1));
		}
		// System.out.println("Random Word: " + output);
		return output;
	}

	/**
	 * Determine if the given entry fits the criteria to be used in the game.
	 * 
	 * @param entry
	 * @param word
	 * @param hiddenWord
	 * @param usedLetters
	 * @return - true if accepted format, false if not
	 */
	public boolean evaluateEntry(String entry, String word, String hiddenWord, String usedLetters) {
		if (entry.equals(word)) {
			return true;
		} else if (!entry.matches("[A-Za-z]")) {
			return false;
		} else if (entry.length() > 1) {
			return false;
		} else if (usedLetters.contains(entry)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean checkIfStrike(String word, String entry) {
		if (!word.contains(entry)) {
			this.numStrikes++;
			return true;
		} else {
			return false;
		}
	}

	public String createHiddenWord(String word) {
		String hiddenWord = "";
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == ' ') {
				hiddenWord += " ";
			} else if (!(word.charAt(i) + "").matches("[A-Za-z]")) {
				hiddenWord += word.charAt(i) + "";
			} else {
				hiddenWord += "_";
			}
		}

		return hiddenWord;
	}

	public String revealHiddenLetters(String word, String hiddenWord, String entry) {
		String newWord = "";

		if (entry.equals(word)) {
			System.out.println("Player Wins!");
			return word;
		} else {
			for (int i = 0; i < hiddenWord.length(); i++) {

				if (word.charAt(i) == entry.charAt(0)) {
					newWord += entry;
				} else {
					newWord += hiddenWord.charAt(i);
				}
			}

			return newWord;
		}
	}

	public boolean checkIfWin(String hiddenWord, String word) {
		if (hiddenWord.equals(word)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Getter for the number of strikes the player has made
	 * 
	 * @return - int containing number of strikes
	 */
	public int getNumStrikes() {
		return this.numStrikes;
	}
}
