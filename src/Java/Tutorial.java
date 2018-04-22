package Java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tutorial {

    private Stage tutorialWindow;
    private int imageCounter = 1;

    private final List<String> textForImage = new ArrayList<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            add("This is the main wnidow. Here user need to choose how shall "
                    + "the program choose some random words from the source files. "
                    + "This program allows user to choose in a sequential way which is "
                    + "nothing but just choose one word at time, or user can opt to choose "
                    + "various words from all four files at the same time via parallel option "
                    + "but it requires that the hardware on top of which this program is running "
                    + "is capable of multithreading.");
            add("When the user chooses to get the words either parallel or sequential, the "
                    + "program tells how long it took to prepare the words to be used during the game.");
            add("The next stage is where user has to decide what level of dificulty the game should be. "
                    + "It can be easy, normal, hard or insane having 5, 6, 7 and 8 characters in a word "
                    + "to guess respectively.");
            add("Here is the main scene for the game, where user can actually play, in this particular "
                    + "image the user opted to play in easy mode as you can see there are 5 characters to guess. "
                    + "On the top there is a menubar the tutorial for that is in the coming slides ... \n"
                    + "On the left side we have all the alphabets, beneath them there are two informative labels"
                    + " that tell us what number of guesses can be wrong and what our acore is. \n"
                    + "on the left side there is an image that correspondes to the wrong guessed characters. "
                    + "Left bottom shows us the question marks this is a word to be guessed ...\n");
            add("Here is the File menu, where we have the options to play a completley new game, save our current game, "
                    + "load a previously saved game or just simply close /quit the game.");
            add("The Difficulty menu shows us the options we already talk about that let us choose the level "
                    + "of difficulty a user wants to play.");
            add("In the Help menu we have the Tutorial which we are going through and an About option that"
                    + " tells us about this software.");
            add("Back to the Game Play:\n"
                    + "When a user press an alphabet from the right side panel the program checks whether it is "
                    + "a character part of the word, if so it shows it in the left bottom part where the word is to "
                    + "be guessed and tells user that this was part of the word.");
            add("In case the character chosen is not part of the word to be guessed the program shows the message in Red"
                    + " colour and changes the number of wrong guess as well as repaints the image corresponding.");
            add("If a user is able to guess the whole word without choosing the wrong chracter 6 time at most the "
                    + "program resets the view and gives a score based on the difficulty level.");
            add("If the user guess 6 wrong chracters then the game is over and the word to be guess is shown.");
            add("When the game finishes the user is given a choice to restart the game or quit.");
        }
    };

    /**
     * @return the tutorialWindow
     */
    public Stage getTutorialWindow() {
        return tutorialWindow;
    }

    /**
     * @param tutorialWindow the tutorialWindow to set
     */
    public void setTutorialWindow(Stage tutorialWindow) {
        this.tutorialWindow = tutorialWindow;
    }

    @FXML
    private ImageView imgvTutorialImage;

    @FXML
    private TextArea txtAreaForImage;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnNext;

    @FXML
    private Button btnClose;

    @FXML
    void btnCloseClick(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you Sure you want to exit the Tutorial?");
        Optional<ButtonType> actionCaptured = alert.showAndWait();
        if (actionCaptured.isPresent() && actionCaptured.get() == ButtonType.OK) {
            tutorialWindow.close();
        }
    }

    @FXML
    void btnNextClick(ActionEvent event) {
        imageCounter++;
        loadImageAndText();
    }

    private void setEvertthing() {
        if (imageCounter <= 1) {
            btnPrevious.setDisable(true);
            btnNext.setDisable(false);
        } else if (imageCounter >= 12) {
            btnNext.setDisable(true);
            btnPrevious.setDisable(false);
        } else if (imageCounter >= 1 && imageCounter <= 12) {
            btnPrevious.setDisable(false);
            btnNext.setDisable(false);
        }
        tutorialWindow.sizeToScene();
    }

    @FXML
    void btnPreviousClick(ActionEvent event) {
        imageCounter--;
        loadImageAndText();
    }

    public void loadImageAndText() {
        String path = null;
        try {
            String IMAGE_DIRECTORY = "./resources/Images/Tutorial/";
            String IMAGE_TYPE = ".png";
            String BASE_NAME = "Tutorial";
            path = new java.io.File(IMAGE_DIRECTORY + BASE_NAME + imageCounter + IMAGE_TYPE).getCanonicalPath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (path != null) {
//			System.out.println(IMAGE_PATH__PREFIX + path + "\n imageCounter" + imageCounter);
            String IMAGE_PATH__PREFIX = "file:///";
            imgvTutorialImage.setImage(new Image(IMAGE_PATH__PREFIX + path));
            txtAreaForImage.setText(textForImage.get(imageCounter - 1));

        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ooops!!! Something went wrong.\n Contact System Administrator.");
            alert.showAndWait();
        }
        setEvertthing();
    }
}
