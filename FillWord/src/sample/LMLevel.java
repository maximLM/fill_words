package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by Максим on 24.12.2016.
 */
public class LMLevel implements EventHandler<ActionEvent> {
    private File src;
    private Button button;
    private List rootList;
    private Stage stage;

    public LMLevel(File file, double x, double y, double size, List list, Stage stage) {
        src = file;
        button = new Button();
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setText(file.getName().replace(".txt", ""));
        list.add(button);
        button.setOnAction(this);
        rootList = list;
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        Main.setLmScreen(new LetterMap(src, Main.SIZE, rootList, stage));
    }

    @Override
    protected void finalize() {
        rootList.remove(button);
    }
}
