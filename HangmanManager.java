// Lucas Liu
// The HangmanManager class allows the user to play the evil hangman game.
// Given a collection of words, the desired length of 
    // words and the max guesses, the
    // the user is able to use HangmanManager class to see 
    // the remaining number of guesses, 
    // the words being considered, and correctly guessed letters.
// The class also allows the user to narrow down the remaining possible 
    // words that can be the answer to the evil hangman game.

import java.util.*;

public class HangmanManager {

    // int representing the number of guesses left the player has
    private int currentGuesses;

    // Set that records the current possible words that can be the answer
    private Set<String> wordsSet;

    // String that records the pattern of the current wordsSet
    private String currentPattern;

    // Set storing the letters that have already been guessed
    private Set<Character> guessedLetters;

    // pre: takes in a collection of words, the desired length of the answer, and 
    // amount of guesses (if length is less than one or the amount of guesses is less 
    // than zero, throws new IllegalArgumentException)
    // post: constructs new HangmanManager object with all words in the given Collection
    // of the desired length being considered as the answer
    public HangmanManager(Collection<String> dictionary, int length, int max) {
        if (length < 1 || max < 0) {
            throw new IllegalArgumentException();
        }
        this.currentGuesses = max;
        this.wordsSet = new TreeSet<String>();
        for (String s: dictionary) {
            if (s.length() == length) {
                wordsSet.add(s);
            }
        }
        this.currentPattern = "";
        for (int n = 0; n < length; n++) {
            currentPattern += "- ";
        }
        this.guessedLetters = new TreeSet<Character>();
    }

    // post: returns the Set recording all the words being considered
    public Set<String> words() {
        return wordsSet;
    }

    // post: returns the number of guesses the player has left
    public int guessesLeft() {
        return currentGuesses;
    }

    // post: returns letters that have been correctly guessed
    public Set<Character> guesses() {
        return guessedLetters;
    }

    // post: returns the current pattern of the words being considered (if there are no
    // words currently being considered, throws new IllegalStateException)
    public String pattern() {
        if (wordsSet.isEmpty()) {
            throw new IllegalStateException();
        }
        return currentPattern.trim();
    }

    // pre: takes in a letter that is the player's guess (if the player has no 
    // guesses left or if there are no words currently being considered, throws 
    // IllegalStateException; if the given letter has already been guessed,
    // throws IllegalArgumentException)
    // post: returns number of occurences of letter in answer word, updates the 
    // set of words being considered, and decreases the number of guesses left 
    // if there are no occurence of the guessed letter 
    public int record(char guess) {
        if (currentGuesses < 1 || wordsSet.isEmpty()) {
            throw new IllegalStateException();
        }
        if (guessedLetters.contains(guess)) {
            throw new IllegalArgumentException();
        }
        guessedLetters.add(guess);
        Map<String, Set<String>> patternFamilyMap = new TreeMap<String, Set<String>>();
        for (String s: wordsSet) {
            String pattern = createPattern(s, guess);
            Set<String> familySet = new TreeSet<String>();
            if (!patternFamilyMap.containsKey(pattern)) {
                patternFamilyMap.put(pattern,familySet);
            }
            patternFamilyMap.get(pattern).add(s);
        }
        int largestSize = 0;
        for (String s: patternFamilyMap.keySet()) {
            if (patternFamilyMap.get(s).size() > largestSize) {
                wordsSet = patternFamilyMap.get(s);
                currentPattern = s;
                largestSize = patternFamilyMap.get(s).size();
            }
        }
    return occurencesOfLetter(guess);
    }

    // pre: takes in a word whose pattern is to be created and the guessed letter
    // post: returns the pattern of the given word respective to the guessed letter
    private String createPattern(String word, char guessedLetter) {
        String newPattern = "";
        int patternIndex = 0;
        for (int c = 0; c < word.length(); c++) {
            if (word.charAt(c) != guessedLetter) {
                newPattern += currentPattern.substring(patternIndex, patternIndex + 2);
            } else {
                newPattern += guessedLetter + " ";
            }
            patternIndex += 2;
        }
        return newPattern;
    }

    // pre: takes in letter to be found
    // post: returns number of occurence of given letter in the current pattern
    private int occurencesOfLetter(char guessedLetter) {
        int occurenceCount = 0;
        for (int c = 0; c < currentPattern.length(); c++) {
            if (currentPattern.charAt(c) == guessedLetter) {
                occurenceCount++;
            }
        }
        if (occurenceCount == 0) {
            currentGuesses--;
        }
        return occurenceCount;
    }
}
