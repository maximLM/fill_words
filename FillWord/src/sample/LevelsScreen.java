package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import static sample.Main.SIZE;
import static sample.Main.root;

/**
 * Created by Максим on 24.12.2016.
 */
public class LevelsScreen implements LMScreen {
    private double width;
    private double height;
    private List rootList;
    private Stage stage;
    private LMLevel levels[];
    private double delta;
    private double levelWidth, levelHeight;
    private Button openBuilder;

    public LevelsScreen(double size, List rootList, Stage stage) {
        width = size;
        height = size;
        this.rootList = rootList;
        this.stage = stage;
        createLevels();
    }

    private void createLevels() {
        File file = new File("Levels");
        File levFiles[] = file.listFiles();
        levels = new LMLevel[levFiles.length];
        int n = (int) Math.sqrt(levels.length) + 1;
        levels = new LMLevel[levFiles.length];
        double div = SIZE / n;
        delta = div * 0.2;
        div -= delta;
        levelHeight = levelWidth = div;
        int cursor = 0;
        int maxI = 0;
        int maxJ = 0;
        for (int i = 0; i < n && cursor < levFiles.length; i++) {
            for (int j = 0; j < n && cursor < levFiles.length; j++, cursor++) {
                maxI = Math.max(i, maxI);
                maxJ = Math.max(j, maxJ);
                levels[cursor] = new LMLevel(levFiles[cursor], countX(j), countY(i), levelWidth, rootList, stage);
            }
        }
        double width = countX(maxJ + 1);
        double height = countY(maxI + 2);

        stage.setHeight(height);
        stage.setWidth(width);
        openBuilder = new Button();
        openBuilder.setLayoutX(width / 2);
        openBuilder.setLayoutY(countX(maxI + 1));
        openBuilder.setText("BUILDER");
        openBuilder.setOnAction(event -> Main.setLmScreen(new BuilderMapScreen(Main.SIZE, rootList, stage, Main.root)));
        rootList.add(openBuilder);
    }


    private double countX(int i) {
        return (i + 1) * delta + i * levelWidth;
    }

    private double countY(int i) {
        return (i + 1) * delta + i * levelWidth;
    }


    @Override
    public void clearScreen() {
        for (LMLevel level : levels) {
            level.finalize();
        }
        rootList.remove(openBuilder);
    }

}
