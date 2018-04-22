package Java;

import java.io.IOException;

class ImageHandller {

    private final String IMAGE_DIRECTORY = "./resources/Images/";
    private final String IMAGE_TYPE = ".png";

    public ImageHandller() {
    }

    public String getHangmanImagePath(int numWrongGuess) throws IOException {
        String IMAGE_NAME_PATTERN = "hangman_";
        if (numWrongGuess == -1) {
            return new java.io.File(IMAGE_DIRECTORY + IMAGE_NAME_PATTERN + "win" + IMAGE_TYPE).getCanonicalPath();
        } else {
            return new java.io.File(IMAGE_DIRECTORY + IMAGE_NAME_PATTERN + numWrongGuess + IMAGE_TYPE)
                    .getCanonicalPath();
        }
    }

    public String getLetterImagePath(boolean isGuessed, char name) throws IOException {
        String INVERTED_IMAGE_PATTERN = "_Inverted";
        if (isGuessed)
            return new java.io.File(IMAGE_DIRECTORY + name + INVERTED_IMAGE_PATTERN + IMAGE_TYPE).getCanonicalPath();
        else
            return new java.io.File(IMAGE_DIRECTORY + name + IMAGE_TYPE).getCanonicalPath();
    }
}
