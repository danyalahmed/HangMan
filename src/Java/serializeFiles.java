package Java;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("serial")
class serializeFiles implements Serializable {

    public serializeFiles() {

    }

    public static void serialize(Object object, ObjectOutputStream objectOutputStream) {

        try {
            // writes to the file
            objectOutputStream.writeObject(object);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<?> deSerialize(ObjectInputStream objectInputStream, GameStateHandller gameStateHandller) {
        List<?> toReturn = null;
        boolean error = false;
        try {
            toReturn = (List<?>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            error = true;
            e.printStackTrace();
        }

        if (error) {
            infoBox("Resource File is not a valid resource!", "Error", null);
            if (gameStateHandller != null)
                gameStateHandller.setLoadError(true);
        }
        return toReturn;
    }

    public static int deSerializeInteger(ObjectInputStream objectInputStream, GameStateHandller gameStateHandller) {
        int toReturn = 0;
        boolean error = false;
        try {
            toReturn = (Integer) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            error = true;
            e.printStackTrace();
        }
        if (error) {
            infoBox("Resource File is not a valid resource!", "Error", null);
            if (gameStateHandller != null)
                gameStateHandller.setLoadError(true);
        }
        return toReturn;
    }

    public static String deSerializeString(ObjectInputStream objectInputStream, GameStateHandller gameStateHandller) {
        String toReturn = null;
        boolean error = false;
        try {
            toReturn = (String) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            error = true;
            e.printStackTrace();
        }
        if (error) {
            infoBox("Resource File is not a valid resource!", "Error", null);
            if (gameStateHandller != null)
                gameStateHandller.setLoadError(true);
        }
        return toReturn;
    }

    public static boolean deSerializeBoolean(ObjectInputStream objectInputStream, GameStateHandller gameStateHandller) {
        boolean toReturn = false;
        boolean error = false;
        try {
            toReturn = (boolean) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            error = true;
            e.printStackTrace();
        }
        if (error) {
            infoBox("Resource File is not a valid resource!", "Error", null);
            if (gameStateHandller != null)
                gameStateHandller.setLoadError(true);
        }
        return toReturn;
    }

    @SuppressWarnings("unchecked")
    public static HashSet<Character> deSerializeHashSetOfChars(ObjectInputStream objectInputStream,
                                                               GameStateHandller gameStateHandller) {
        HashSet<Character> toReturn = new HashSet<>();
        boolean error = false;
        try {
            toReturn = (HashSet<Character>) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
        } catch (ClassNotFoundException e) {
            error = true;
            e.printStackTrace();
        }
        if (error) {
            infoBox("Resource File is not a valid resource!", "Error", null);
            if (gameStateHandller != null)
                gameStateHandller.setLoadError(true);
        }
        return toReturn;
    }

    private static void infoBox(String infoMessage, String titleBar, String headerMessage) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titleBar);
        alert.setHeaderText(headerMessage);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }
}
