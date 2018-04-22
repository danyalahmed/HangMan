package Java;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.HashSet;
import java.util.List;

class GameStateHandller {

    private List<String> easyLevelWords = null;
    private List<String> normalLevelWords = null;
    private List<String> hardLevelWords = null;
    private List<String> insaneLevelWords = null;
    private List<String> activeList = null;

    // 0 being the easiest ... 4 being the extreme
    private int difficultyLevel;
    // a temp string that hold the current word
    private String wordToGuess;
    // number of mistakes
    private int numWrongGuess = 0;
    // if its false the game has been lost
    private boolean gameStatusActive;
    // if true the user has guessed all the word in current difficulty
    private boolean gameEnded;

    private int indexOfWordToGuess;
    private HashSet<Character> charsGuessed;
    private int score;
    private boolean loadError;

    private static FileInputStream inputFileStream;
    private static ObjectInputStream objectInputStream;

    private static FileOutputStream outputFileStream;
    private static ObjectOutputStream objectOutputStream;

    /**
     * @return the easyLevelWords
     */
    public List<String> getEasyLevelWords() {
        return easyLevelWords;
    }

    /**
     * @param easyLevelWords the easyLevelWords to set
     */
    public void setEasyLevelWords(List<String> easyLevelWords) {
        this.easyLevelWords = easyLevelWords;
    }

    /**
     * @return the normalLevelWords
     */
    public List<String> getNormalLevelWords() {
        return normalLevelWords;
    }

    /**
     * @param normalLevelWords the normalLevelWords to set
     */
    public void setNormalLevelWords(List<String> normalLevelWords) {
        this.normalLevelWords = normalLevelWords;
    }

    /**
     * @return the hardLevelWords
     */
    public List<String> getHardLevelWords() {
        return hardLevelWords;
    }

    /**
     * @param hardLevelWords the hardLevelWords to set
     */
    public void setHardLevelWords(List<String> hardLevelWords) {
        this.hardLevelWords = hardLevelWords;
    }

    /**
     * @return the insaneLevelWords
     */
    public List<String> getInsaneLevelWords() {
        return insaneLevelWords;
    }

    /**
     * @param insaneLevelWords the insaneLevelWords to set
     */
    public void setInsaneLevelWords(List<String> insaneLevelWords) {
        this.insaneLevelWords = insaneLevelWords;
    }

    /**
     * @return the activeList
     */
    public List<String> getActiveList() {
        return activeList;
    }

    /**
     * @param activeList the activeList to set
     */
    public void setActiveList(List<String> activeList) {
        this.activeList = activeList;
    }

    /**
     * @return the difficultyLevel
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * @param difficultyLevel the difficultyLevel to set
     */
    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    /**
     * @return the wordToGuess
     */
    public String getWordToGuess() {
        return wordToGuess;
    }

    /**
     * @param wordToGuess the wordToGuess to set
     */
    public void setWordToGuess(String wordToGuess) {
        this.wordToGuess = wordToGuess;
    }

    /**
     * @return the numWrongGuess
     */
    public int getNumWrongGuess() {
        return numWrongGuess;
    }

    /**
     * @param numWrongGuess the numWrongGuess to set
     */
    public void setNumWrongGuess(int numWrongGuess) {
        this.numWrongGuess = numWrongGuess;
    }

    /**
     * @return the gameStatusActive
     */
    public boolean isGameStatusActive() {
        return gameStatusActive;
    }

    /**
     * @param gameStatusActive the gameStatusActive to set
     */
    public void setGameStatusActive(boolean gameStatusActive) {
        this.gameStatusActive = gameStatusActive;
    }

    /**
     * @return the gameEnded
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * @param gameEnded the gameEnded to set
     */
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * @return the indexOfWordToGuess
     */
    public int getIndexOfWordToGuess() {
        return indexOfWordToGuess;
    }

    /**
     * @param indexOfWordToGuess the indexOfWordToGuess to set
     */
    public void setIndexOfWordToGuess(int indexOfWordToGuess) {
        this.indexOfWordToGuess = indexOfWordToGuess;
    }

    /**
     * @return the charsGuessed
     */
    public HashSet<Character> getCharsGuessed() {
        return charsGuessed;
    }

    /**
     * @param charsGuessed the charsGuessed to set
     */
    public void setCharsGuessed(HashSet<Character> charsGuessed) {
        this.charsGuessed = charsGuessed;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    public void saveAllToFile() {
        // to be used to save the game state or restore the same

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Resource File", "*.bin"));
        File selectedFile = fileChooser.showSaveDialog(null);

//		System.out.println(selectedFile.getAbsolutePath());

        String filesPath = null;
        try {
            filesPath = new File(selectedFile.getAbsolutePath()).getCanonicalPath();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            if (filesPath != null) {
                outputFileStream = new FileOutputStream(new File(filesPath));
                objectOutputStream = new ObjectOutputStream(outputFileStream);
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText("The file chosen is not a source file or its a directory!!!");
                alert.showAndWait();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //

        serializeFiles.serialize(easyLevelWords, objectOutputStream);
        serializeFiles.serialize(normalLevelWords, objectOutputStream);
        serializeFiles.serialize(hardLevelWords, objectOutputStream);
        serializeFiles.serialize(insaneLevelWords, objectOutputStream);
        serializeFiles.serialize(activeList, objectOutputStream);

        serializeFiles.serialize(difficultyLevel, objectOutputStream);
        serializeFiles.serialize(wordToGuess, objectOutputStream);
        serializeFiles.serialize(numWrongGuess, objectOutputStream);
        serializeFiles.serialize(gameStatusActive, objectOutputStream);
        serializeFiles.serialize(gameEnded, objectOutputStream);

        serializeFiles.serialize(indexOfWordToGuess, objectOutputStream);
        serializeFiles.serialize(charsGuessed, objectOutputStream);
        serializeFiles.serialize(score, objectOutputStream);

        try {
            outputFileStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void getAllFromFile() {
        // to be used to save the game state or restore the same

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Resource File", "*.bin"));
        File selectedFile = fileChooser.showOpenDialog(null);

//		System.out.println(selectedFile.getAbsolutePath());

        File f = new File(selectedFile.getAbsolutePath());
        if (f.exists() && !f.isDirectory()) {
            try {

                inputFileStream = new FileInputStream(f);
                objectInputStream = new ObjectInputStream(inputFileStream);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // System.out.println("Loading them back\n");
            loadError = false;
            if (!loadError)
                easyLevelWords = (List<String>) serializeFiles.deSerialize(objectInputStream, this);
            if (!loadError)
                normalLevelWords = (List<String>) serializeFiles.deSerialize(objectInputStream, this);
            if (!loadError)
                hardLevelWords = (List<String>) serializeFiles.deSerialize(objectInputStream, this);
            if (!loadError)
                insaneLevelWords = (List<String>) serializeFiles.deSerialize(objectInputStream, this);
            if (!loadError)
                activeList = (List<String>) serializeFiles.deSerialize(objectInputStream, this);

            if (!loadError)
                difficultyLevel = serializeFiles.deSerializeInteger(objectInputStream, this);
            if (!loadError)
                wordToGuess = serializeFiles.deSerializeString(objectInputStream, this);
            if (!loadError)
                numWrongGuess = serializeFiles.deSerializeInteger(objectInputStream, this);
            if (!loadError)
                gameStatusActive = serializeFiles.deSerializeBoolean(objectInputStream, this);
            if (!loadError)
                gameEnded = serializeFiles.deSerializeBoolean(objectInputStream, this);

            if (!loadError)
                indexOfWordToGuess = serializeFiles.deSerializeInteger(objectInputStream, this);
            if (!loadError)
                charsGuessed = serializeFiles.deSerializeHashSetOfChars(objectInputStream, this);
            if (!loadError)
                score = serializeFiles.deSerializeInteger(objectInputStream, this);

            // System.out.println("Loading them back Finished\n");

            try {
                inputFileStream.close();
                objectInputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void setLoadError(boolean b) {
        this.loadError = b;
    }

    public boolean getLoadError() {
        return this.loadError;
    }
}
