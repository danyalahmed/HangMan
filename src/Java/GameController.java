package Java;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class GameController {

    private static final String IMAGE_PATH__PREFIX = "file:///";

    // 0 being the easiest ... 4 being the extreme
    private int difficultyLevel;

    private List<String> easyLevelWords = null;
    private List<String> normalLevelWords = null;
    private List<String> hardLevelWords = null;
    private List<String> insaneLevelWords = null;
    private List<String> activeList = null;

    // a temp string that hold the current word
    private String wordToGuess;
    // number of mistakes
    private int numWrongGuess = 0;
    // if its false the game has been lost
    private boolean gameStatusActive;
    // if true the user has guessed all the word in current difficulty
    private boolean gameEnded;

    // fields from Gamewindow
    @FXML
    private ImageView hangmanImageHolder;

    @FXML
    private HBox hbWordToGuess;

    @FXML
    private GridPane gvLetters;

    @FXML
    private Label lblIncorrect;

    @FXML
    private Label lblIncorrectPlaceHolder;

    @FXML
    private Label lblGuessStatus;

    @FXML
    private MenuBar menuTop;

    @FXML
    private Menu menuItemFile;

    @FXML
    private MenuItem newGameMenuItem;

    @FXML
    private MenuItem saveGameMenuItem;

    @FXML
    private MenuItem loadGameMenuItem;

    @FXML
    private MenuItem menuItemClose;

    @FXML
    private MenuItem easyMenuBarItem;

    @FXML
    private MenuItem normalMenuBarItem;

    @FXML
    private MenuItem hardMenuBarItem;

    @FXML
    private MenuItem insaneMenuBarItem;

    @FXML
    private MenuItem tutorialMenuBarItem;

    @FXML
    private MenuItem aboutMenuBarItem;

    @FXML
    private Label lblScore;

    @FXML
    private Label lblScorePlaceHolder;

    private int indexOfWordToGuess;

    private HashSet<Character> charsGuessed;

    private int score;
    // to store or retore the game state
    private GameStateHandller gameStateHandller;

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
        if (difficultyLevel < 0 || difficultyLevel > 4)
            this.difficultyLevel = 0;
        else
            this.difficultyLevel = difficultyLevel;
    }

    @FXML
    public void menuItemClose() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you Sure you want to exit the game?");
        Optional<ButtonType> actionCaptured = alert.showAndWait();
        if (actionCaptured.isPresent() && actionCaptured.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    public GameController() {
    }

    @FXML
    void saveGameMenuItemClick(ActionEvent event) {
        gameStateHandller = new GameStateHandller();

        gameStateHandller.setEasyLevelWords(easyLevelWords);
        gameStateHandller.setNormalLevelWords(normalLevelWords);
        gameStateHandller.setHardLevelWords(hardLevelWords);
        gameStateHandller.setInsaneLevelWords(insaneLevelWords);
        gameStateHandller.setActiveList(activeList);

        gameStateHandller.setDifficultyLevel(difficultyLevel);
        gameStateHandller.setWordToGuess(wordToGuess);
        gameStateHandller.setNumWrongGuess(numWrongGuess);
        gameStateHandller.setGameStatusActive(gameStatusActive);
        gameStateHandller.setGameEnded(gameEnded);

        gameStateHandller.setIndexOfWordToGuess(indexOfWordToGuess);
        gameStateHandller.setCharsGuessed(charsGuessed);
        gameStateHandller.setScore(score);

        gameStateHandller.saveAllToFile();
    }

    @FXML
    void loadGameMenuItemClick(ActionEvent event) {
        gameStateHandller = new GameStateHandller();
        gameStateHandller.getAllFromFile();
        if (!gameStateHandller.getLoadError()) {
            easyLevelWords = gameStateHandller.getEasyLevelWords();
            normalLevelWords = gameStateHandller.getNormalLevelWords();
            hardLevelWords = gameStateHandller.getHardLevelWords();
            insaneLevelWords = gameStateHandller.getInsaneLevelWords();
            activeList = gameStateHandller.getActiveList();

            difficultyLevel = gameStateHandller.getDifficultyLevel();
            wordToGuess = gameStateHandller.getWordToGuess();
            numWrongGuess = gameStateHandller.getNumWrongGuess();
            gameStatusActive = gameStateHandller.isGameStatusActive();
            gameEnded = gameStateHandller.isGameEnded();

            indexOfWordToGuess = gameStateHandller.getIndexOfWordToGuess();
            charsGuessed = gameStateHandller.getCharsGuessed();
            score = gameStateHandller.getScore();

            loadLletters(true);

            lblIncorrectPlaceHolder.setText(String.valueOf(6 - numWrongGuess));
            lblScorePlaceHolder.setText(String.valueOf(score));
            lblGuessStatus.setText("");

            if (numWrongGuess > 3)
                lblIncorrectPlaceHolder.setTextFill(Color.web("Red"));
            else
                lblIncorrectPlaceHolder.setTextFill(Color.web("Green"));
            loadToGuess(true);
            loadHangman(false);
        }

    }

    void gameOn() {
        preparLettersLists();
        loadGame(true);
    }

    private void incrementIndexOfWordToGuess() {
        indexOfWordToGuess++;
        if (indexOfWordToGuess >= 50)
            gameEnds();
    }

    private void gameEnds() {
        gameEnded = true;
        loadHangman(true);
    }

    @SuppressWarnings({"unchecked"})
    private void preparLettersLists() {

        List<String> allWords = new ArrayList<>();
        List<String> oneFile;

        // Initialising the lists
        easyLevelWords = new ArrayList<>();
        normalLevelWords = new ArrayList<>();
        hardLevelWords = new ArrayList<>();
        insaneLevelWords = new ArrayList<>();
        activeList = new ArrayList<>();

        FileInputStream inputFileStream = null;
        try {
            inputFileStream = new FileInputStream(Main.SERIALIZEDFILE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(inputFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputFileStream != null && objectInputStream != null) {
            for (int i = 1; i < 5; i++) {
                oneFile = (List<String>) serializeFiles.deSerialize(objectInputStream, null);
                allWords.addAll(oneFile);
            }

            for (String allWord : allWords) {
                switch (allWord.length()) {
                    case 5: {
                        easyLevelWords.add(allWord);
                        break;
                    }
                    case 6: {
                        normalLevelWords.add(allWord);
                        break;
                    }
                    case 7: {
                        hardLevelWords.add(allWord);
                        break;
                    }
                    case 8: {
                        insaneLevelWords.add(allWord);
                        break;
                    }
                    default: {
                        break;
                    }
                }

            }
            try {
                inputFileStream.close();
                objectInputStream.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            switch (difficultyLevel) {
                case 0: {
                    activeList.addAll(easyLevelWords);
                    break;
                }
                case 1: {
                    activeList.addAll(normalLevelWords);
                    break;
                }
                case 2: {
                    activeList.addAll(hardLevelWords);
                    break;
                }
                case 3: {
                    activeList.addAll(insaneLevelWords);
                    break;
                }
                default: {
                    break;
                }
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ooops!!! Something went wrong.\n Contact System Administrator.");
            alert.showAndWait();
        }
        // System.out.println("easy: " + easyLevelWords.size() + "\nnormal: " +
        // normalLevelWords.size() + "\nhard: "
        // + hardLevelWords.size() + "\ninsane: " + insaneLevelWords.size());
    }

    private void loadHangman(boolean isWon) {
        ImageHandller imgH = new ImageHandller();
        try {
            if (isWon) {
                hangmanImageHolder.setImage(new Image(IMAGE_PATH__PREFIX + imgH.getHangmanImagePath(-1)));
            } else {
                hangmanImageHolder.setImage(new Image(IMAGE_PATH__PREFIX + imgH.getHangmanImagePath(numWrongGuess)));
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void loadLletters(boolean reload) {
        int imageCol = 0;
        int imageRow = 0;
        MyImageTile pic;
        HBox hb = new HBox(1);

        gvLetters.setVgap(1);
        gvLetters.getChildren().clear();

        for (char c = 'a'; c <= 'z'; c++) {
            // pathToImage = imgH.getLetterImagePath(false, c);
            // System.out.println(pathToImage);

            pic = new MyImageTile(c, this, false);
            pic.setId("" + c);

            FadeTransition ft = new FadeTransition();
            ft.setNode(pic);
            ft.setDuration(new Duration(500));
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            pic.setOnMouseClicked(me -> ft.play());

            if (reload && charsGuessed.contains(c)) {
                pic.invertImage(false);
                pic.setClickActive(false);
            }

            hb.getChildren().add(pic);
            imageCol++;
            // To check if all the 5 images of a row are completed
            if (imageCol > 3) {
                gvLetters.add(hb, 0, imageRow);
                hb = new HBox(1);
                // Reset Column
                imageCol = 0;
                // Next Row
                imageRow++;
            }

            if (c == 'z')
                gvLetters.add(hb, 0, imageRow);
        }
    }

    private void loadToGuess(boolean isToGuess) {
        hbWordToGuess.getChildren().clear();
        MyImageTile pic;
        // System.out.println(wordToGuess);
        for (int i = 0; i < wordToGuess.length(); i++) {
            pic = new MyImageTile(wordToGuess.charAt(i), this, isToGuess);
            pic.setId(wordToGuess.charAt(i) + "wordToGuess" + i);
            if (charsGuessed.contains(wordToGuess.charAt(i))) {
                pic.invertImage(true);
            }
            FadeTransition ft = new FadeTransition();
            ft.setNode(pic);
            ft.setDuration(new Duration(500));
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();
            hbWordToGuess.getChildren().add(pic);
        }
    }

    public void update(char c, Scene scene) {
        // System.out.println(c);
        charsGuessed.add(c);
        boolean rightGuess = false;

        // System.out.print(charsGuessed);
        // System.out.println(wordToGuess + " ");

        for (int i = 0; i < wordToGuess.length(); i++) {
            if (charsGuessed.contains(wordToGuess.charAt(i))) {
                MyImageTile pic = (MyImageTile) scene.lookup("#" + c + "wordToGuess" + i);
                if (pic != null) {
                    pic.invertImage(true);
                    FadeTransition ft = new FadeTransition();
                    ft.setNode(pic);
                    ft.setDuration(new Duration(500));
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    ft.setCycleCount(2);
                    ft.setAutoReverse(true);
                    ft.play();
                    rightGuess = true;
                }
            }
        }

        if (!rightGuess) {
            lblGuessStatus.setText("Ooops!!! The Character Guessed is not part of Word!");
            lblGuessStatus.setTextFill(Color.web("Red"));
            incrementNumWrongGuess();
            checkIfLost();
        } else {
            lblGuessStatus.setText("The Character Guessed is part of Word!");
            lblGuessStatus.setTextFill(Color.web("Green"));
            checkIfWin(scene);
        }
    }

    private void checkIfWin(Scene scene) {
        boolean winStatus = true;
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (!charsGuessed.contains(wordToGuess.charAt(i))) {
                winStatus = false;
            }
        }
        if (winStatus) {
            incrementIndexOfWordToGuess();
            score = (difficultyLevel + 1) * indexOfWordToGuess;
            // System.out.println("index: " + indexOfWordToGuess +
            // "\ndifficulty: " + difficultyLevel + "\nscore: " + score);
            lblScorePlaceHolder.setText(String.valueOf(score));

            loadNextWord();
        }
        loadHangman(false);
    }

    private void loadNextWord() {
        lblGuessStatus.setText("");
        loadLletters(false);
        loadGame(false);
    }

    private void checkIfLost() {
        loadHangman(false);
        lblIncorrectPlaceHolder.setText(String.valueOf(6 - numWrongGuess));
        if (numWrongGuess > 3)
            lblIncorrectPlaceHolder.setTextFill(Color.web("Red"));
        else
            lblIncorrectPlaceHolder.setTextFill(Color.web("Green"));
        if (numWrongGuess > 5) {
            gameStatusActive = false;
            MyImageTile.gameStatusActive = false;

            loadToGuess(false);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("Do you want to restart?");
            Optional<ButtonType> actionCaptured = alert.showAndWait();
            if (actionCaptured.isPresent() && actionCaptured.get() == ButtonType.OK) {
                loadGame(true);
            } else if (actionCaptured.isPresent() &&actionCaptured.get() == ButtonType.CANCEL) {
                System.exit(0);
            }
        }
    }

    @FXML
    void newGame(ActionEvent event) {
        loadGame(true);
    }

    private void loadGame(boolean isNewGame) {
        // these steps are only to be used if it is a new game
        if (isNewGame) {
            indexOfWordToGuess = 0;
            score = 0;
            shakeList();
        }
        // these steps are common in new game and next step
        gameStatusActive = true;
        MyImageTile.gameStatusActive = true;
        setNumWrongGuess(0);
        charsGuessed = new HashSet<>();
        lblIncorrectPlaceHolder.setText(String.valueOf(6));
        lblGuessStatus.setText("");
        if (numWrongGuess > 3)
            lblIncorrectPlaceHolder.setTextFill(Color.web("Red"));
        else
            lblIncorrectPlaceHolder.setTextFill(Color.web("Green"));
        lblScorePlaceHolder.setText(String.valueOf(score));
        loadHangman(false);
        loadLletters(false);
        wordToGuess = activeList.get(indexOfWordToGuess);
        loadToGuess(true);
    }

    // shuffles the list for a fresh start ...
    private void shakeList() {
        activeList = new ArrayList<>();
        switch (difficultyLevel) {
            case 0: {
                Collections.shuffle(easyLevelWords);
                activeList.addAll(easyLevelWords);

                break;
            }
            case 1: {
                Collections.shuffle(normalLevelWords);
                activeList.addAll(normalLevelWords);
                break;
            }
            case 2: {
                Collections.shuffle(hardLevelWords);
                activeList.addAll(hardLevelWords);
                break;
            }
            case 3: {
                Collections.shuffle(insaneLevelWords);
                activeList.addAll(insaneLevelWords);
                break;
            }
            default: {
                break;
            }
        }
    }

    private void incrementNumWrongGuess() {
        numWrongGuess++;
    }

    @FXML
    void easyMenuBarItemClick(ActionEvent event) {
        difficultyLevel = 0;
        newOrContinue();
    }

    @FXML
    void normalMenuBarItemClick(ActionEvent event) {
        difficultyLevel = 1;
        newOrContinue();
    }

    @FXML
    void hardMenuBarItemClick(ActionEvent event) {
        difficultyLevel = 2;
        newOrContinue();
    }

    @FXML
    void insaneMenuBarItemClick(ActionEvent event) {
        difficultyLevel = 3;
        newOrContinue();
    }

    @FXML
    void tutorialMenuBarItemClick(ActionEvent event) {
        try {
            Stage window = new Stage();
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("../Views/Tutorial.fxml"));
            Pane myPane = myLoader.load();

            Tutorial controller = myLoader.getController();
            controller.setTutorialWindow(window);
            controller.loadImageAndText();

            window.setScene(new Scene(myPane));
            window.setTitle("Hangman | Tutorial");
            // this will allow the control over the closing request ...
            window.setOnCloseRequest(event1 -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you Sure you want to exit the Tutorial?");
                Optional<ButtonType> actionCaptured = alert.showAndWait();
                if (actionCaptured.isPresent() && actionCaptured.get() == ButtonType.CANCEL) {
                    event1.consume();
                }
            });
            // window.setResizable(false);
            window.initModality(Modality.APPLICATION_MODAL);
            window.showAndWait();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void aboutMenuBarItemClick(ActionEvent event) {
        Stage window = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("../Views/About.fxml"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Scene scene;
        if (root != null) {
            scene = new Scene(root);
            window.setScene(scene);
            window.setTitle("Hangman | About");
            window.setResizable(false);
            window.initModality(Modality.APPLICATION_MODAL);
            window.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ooops!!! Something went wrong.\n Contact System Administrator.");
            alert.showAndWait();
        }
    }

    private void newOrContinue() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to restart?");
        Optional<ButtonType> actionCaptured = alert.showAndWait();
        if (actionCaptured.isPresent() && actionCaptured.get() == ButtonType.OK) {
            loadGame(true);
        }
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
    private void setNumWrongGuess(int numWrongGuess) {
        if (numWrongGuess > 6 || numWrongGuess < 0)
            this.numWrongGuess = 0;
        else
            this.numWrongGuess = numWrongGuess;
    }

}
