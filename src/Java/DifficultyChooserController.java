package Java;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Optional;

public class DifficultyChooserController {

    private Stage window;

    @FXML
    private RadioButton easyRadioButton;

    @FXML
    private ToggleGroup levelChooser;

    @FXML
    private RadioButton normalRadioButton;

    @FXML
    private RadioButton hardRadioButton;

    @FXML
    private RadioButton insaneRadioButton;

    @FXML
    private Button btnNext;

    @FXML
    void btnNextClicked(ActionEvent event) {
        loadGameWindow();
    }

    public void setPrevStage(Stage stage) {
        this.window = stage;
    }

    private void loadGameWindow() {
        try {
            Stage window = new Stage();
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("../Views/GameWindow.fxml"));
            Pane myPane = myLoader.load();
            GameController controller = myLoader.getController();

            if (easyRadioButton.isSelected())
                controller.setDifficultyLevel(0);
            else if (normalRadioButton.isSelected())
                controller.setDifficultyLevel(1);
            else if (hardRadioButton.isSelected())
                controller.setDifficultyLevel(2);
            else if (insaneRadioButton.isSelected())
                controller.setDifficultyLevel(3);

            window.setScene(new Scene(myPane));
            window.setTitle("Hangman | The Game");
            // this will allow the control over the closing request ...
            window.setOnCloseRequest(event -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you Sure you want to exit the game?");
                Optional<ButtonType> actionCaptured = alert.showAndWait();
                if (actionCaptured.isPresent() &&actionCaptured.get() == ButtonType.CANCEL) {
                    event.consume();
                }
            });
            this.window.hide();
            controller.gameOn();
            window.setResizable(false);
            window.show();
        } catch (Exception e) {
            System.err.println(e.getMessage() + "\n Contact System Administrator.");
        }
    }
}
