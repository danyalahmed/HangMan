package Java;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

class MyImageTile extends ImageView {

    public static boolean gameStatusActive;

    /**
     * The letter to be displayed on the tile
     */
    private final char IMAGE_LETTER;

    /**
     * The directory containing the letter images.
     */
    private final String IMAGE_DIRECTORY = "./resources/Images/";

    /**
     * The type of image (.jpg, .png, etc. to include the period).
     */
    private final String IMAGE_TYPE = ".png";

    /**
     * The preferred width of the tiles.
     */
    private final int PREFERRED_WIDTH = 50;

    /**
     * The preferred height of the tiles.
     */
    private final int PREFERRED_HEIGHT = 50;

    /**
     * The current path of the current image.
     */
    private String path;

    /**
     * this is a constant that is basically the prefix saying that its a file i
     * am looking for
     */
    private final String IMAGE_PATH__PREFIX = "file:///";

    private boolean isClickActive;

    private final GameController controller;

    /**
     * The default constructor.
     */
    public MyImageTile() {
        this('a', new GameController(), false);
    }

    public MyImageTile(char imageLetter, GameController gameController, boolean isWordToGuess) {
        this.controller = gameController;
        IMAGE_LETTER = imageLetter;
        try {
            if (!isWordToGuess) {
                path = new java.io.File(IMAGE_DIRECTORY + IMAGE_LETTER + IMAGE_TYPE).getCanonicalPath();
                isClickActive = true;
                addMouseListener();
            } else {
                String WORD_TO_GUESS = "wordToGuess";
                path = new java.io.File(IMAGE_DIRECTORY + WORD_TO_GUESS + IMAGE_TYPE).getCanonicalPath();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.setFitHeight(PREFERRED_HEIGHT);
        this.setFitWidth(PREFERRED_WIDTH);
        this.setImage(new Image(IMAGE_PATH__PREFIX + path));
    }

    /**
     * Loads a new image in the hangman image series.
     *
     */
    public void invertImage(boolean isToGuess) {
        try {
            if (!isToGuess) {
                /*
      the suffix for the inverted images
     */
                String INVERTED_IMAGE_PATTERN = "_Inverted";
                path = new java.io.File(IMAGE_DIRECTORY + IMAGE_LETTER + INVERTED_IMAGE_PATTERN + IMAGE_TYPE)
                        .getCanonicalPath();
            } else {
                path = new java.io.File(IMAGE_DIRECTORY + IMAGE_LETTER + IMAGE_TYPE).getCanonicalPath();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
      the image of current letter
     */
        Image image = new Image(IMAGE_PATH__PREFIX + path);
        // System.out.println(IMAGE_PATH__PREFIX + path);
        this.setImage(image);
    }

    /**
     * Add a imageView mouse listener
     *
     */
    private void addMouseListener() {
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (isClickActive && gameStatusActive) {
                // System.out.println("Tile pressed " + ((ImageView)
                // event.getSource()).getId());
                MyImageTile m = (MyImageTile) event.getSource();
                m.invertImage(false);
                isClickActive = false;
                controller.update(IMAGE_LETTER, ((Node) event.getSource()).getScene());
            }
            event.consume();
        });
    }

    /**
     * @return the isClickActive
     */
    public boolean isClickActive() {
        return isClickActive;
    }

    /**
     * @param isClickActive the isClickActive to set
     */
    public void setClickActive(boolean isClickActive) {
        this.isClickActive = isClickActive;
    }

    /**
     * @return the pREFERRED_WIDTH
     */
    public int getPREFERRED_WIDTH() {
        return PREFERRED_WIDTH;
    }

    /**
     * @return the pREFERRED_HEIGHT
     */
    public int getPREFERRED_HEIGHT() {
        return PREFERRED_HEIGHT;
    }

    /**
     * @return the iMAGE_LETTER
     */
    public char getIMAGE_LETTER() {
        return IMAGE_LETTER;
    }

}
