package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    public static final String LEVELS = "Levels", EXT = ".txt";
    public static double SIZE = 300;
    public static Group root;
    private static LMScreen lmScreen;

    @Override
    public void start(Stage stage) throws Exception {
        root = new Group();
        stage.setTitle("FillWord");
        stage.setHeight(SIZE);
        stage.setWidth(SIZE);
        stage.setResizable(false);
        stage.setScene(new Scene(root, SIZE, SIZE));
        stage.show();
        root.requestFocus();
        setLmScreen(new LevelsScreen(SIZE, root.getChildren(), stage));
    }



    public static void main(String[] args) {
        launch(args);
    }

    public static void setLmScreen(LMScreen newScreen) {
        if (lmScreen != null) lmScreen.clearScreen();
        System.out.println("What is happening");
        lmScreen = newScreen;
    }
}
