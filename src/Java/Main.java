package Java;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    private static ObjectOutputStream objectOutputStream;

    final static File SERIALIZEDFILE = new File("./resources/words.bin");
    final static String FILEEXTENTION = ".txt";

    static myLists allListsToFileContainer;

    private long secs;

    private Stage FirstView;

    // fields from MainDialog
    @FXML
    RadioButton parallel;
    @FXML
    RadioButton oneAtTime;
    @FXML
    Button btnChoose;

    private void setPrevStage(Stage stage) {
        FirstView = stage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader myLoader = new FXMLLoader(getClass().getResource("../Views/MainDialogue.fxml"));
        Pane myPane = myLoader.load();

        Main controller = myLoader.getController();
        controller.setPrevStage(primaryStage);

        Scene myScene = new Scene(myPane);
        primaryStage.setScene(myScene);
        primaryStage.setTitle("Hangman Word Chooser");
        primaryStage.setResizable(false);
        // this will allow the control over the closing request ...
        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you Sure you want to exit the game?");
            Optional<ButtonType> actionCaptured = alert.showAndWait();
            if (actionCaptured.isPresent() && actionCaptured.get() == ButtonType.CANCEL) {
                event.consume();
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void btnChooseOnClick() {
        if (oneAtTime.isSelected())
            sequentialWordChooser();
        if (parallel.isSelected())
            parallelWordChoose();

    }

    private void sequentialWordChooser() {
        secs = System.nanoTime();
        filesReadNWrite(true);
    }

    private void parallelWordChoose() {
        // here we control the file numbering ...
        ReadNWrite.lastFileNum = 0;
        secs = System.nanoTime();
        filesReadNWrite(false);
    }

    private void filesReadNWrite(boolean sequential) {
        try {
            allListsToFileContainer = new myLists();

            FileOutputStream outputFileStream = new FileOutputStream(SERIALIZEDFILE);
            objectOutputStream = new ObjectOutputStream(outputFileStream);

            FileInputStream inputFileStream = new FileInputStream(SERIALIZEDFILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputFileStream);

            if (!sequential) {
                ReadNWrite rNW1 = new ReadNWrite();
                ReadNWrite rNW2 = new ReadNWrite();
                ReadNWrite rNW3 = new ReadNWrite();
                ReadNWrite rNW4 = new ReadNWrite();

                ExecutorService executorService = Executors.newFixedThreadPool(4);
                executorService.submit(rNW1);
                executorService.submit(rNW2);
                executorService.submit(rNW3);
                executorService.submit(rNW4);

                executorService.shutdown();

                while (!executorService.isTerminated()) {
                    try {
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                for (int i = 1; i < 5; i++)
                    sequentialDoIt(i);
            }
            // Serialising all 4 lists to one
            for (int i = 0; i < 4; i++)
                serializeFiles.serialize(allListsToFileContainer.getFile(i), Main.objectOutputStream);

            infoBox("It took " + ((System.nanoTime()) - secs) / 1000000 + " miliseconds"
            );

            objectInputStream.close();
            inputFileStream.close();

            loadNextWindow();
        } catch (IOException e) {
            System.out.println("Oops, Something went wrong \n " + e.toString() + "\n Contact System Administrator.");
        }
    }

    private void loadNextWindow() {
        try {
            Stage window = new Stage();
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("../Views/DifficultyLevelChooser.fxml"));
            Pane myPane = myLoader.load();
            DifficultyChooserController controller = myLoader.getController();
            controller.setPrevStage(window);
            window.setScene(new Scene(myPane));
            window.setTitle("Hangman | The Level Chooser");
            // this will allow the control over the closing request ...
            window.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Are you Sure you want to exit the game?");
                Optional<ButtonType> actionCaptured = alert.showAndWait();
                if (actionCaptured.isPresent() && actionCaptured.get() == ButtonType.CANCEL) {
                    event.consume();
                }
            });
            FirstView.hide();
            window.setResizable(false);
            window.show();
        } catch (Exception e) {
            System.err.println(e.getMessage() + "\n Contact System Administrator.");
        }
    }

    private void sequentialDoIt(int fileNumber) {
        List<String> toWrite = new ArrayList<>();
        try {
            String filesPath = new java.io.File("./resources/ProjectFiles/Files/file").getCanonicalPath();
            // check if the file number is valid
            if (fileNumber > 0 && fileNumber < 5) {
                // Generates the file path to read a specific file
                String file = filesPath + fileNumber + Main.FILEEXTENTION;
                // Reader reads the whole line from the file, which is actually
                // a word and passes to the line String
                @SuppressWarnings("resource")
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                // creating the list of words /The whole File
                List<String> words = new ArrayList<>();
                while (line != null) {
                    String[] wordsLine = line.split(" ");
                    words.addAll(Arrays.asList(wordsLine));
                    line = reader.readLine();
                }

                // here we go through the list of words /The whole file, and
                // choose only 50 words randomly.
                for (int i = 0; i < 50; i++) {
                    Random rand;
                    String tempWord;
                    if (i == 0)
                        rand = new Random(System.currentTimeMillis());
                    else
                        rand = new Random(System.currentTimeMillis() * i);
                    tempWord = words.get(rand.nextInt(words.size()));
                    while (toWrite.contains(tempWord))
                        tempWord = words.get(rand.nextInt(words.size()));
                    toWrite.add(tempWord);
                }
//				System.out.println("index: " + fileNumber + " List: " + toWrite);

                // write the 50 chosen word to allListsToFileContainer
                allListsToFileContainer.setFile(fileNumber, toWrite);

                // if the file number is not between 1 and 4, somebody must have
                // tampered our variable ...
            } else {
                System.out.println("Oops, Something is wrong with File Numbering!\n Contact System Administrator.");
            }

        } catch (IOException e) {
            System.out.println("Oops, Something went wrong \n " + e.toString() + "\n Contact System Administrator.");
        }
    }

    private static void infoBox(String infoMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Time Taken to prepare the Lists");
        alert.setHeaderText(null);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

}
