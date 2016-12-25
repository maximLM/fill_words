package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Максим on 15.12.2016.
 */
public class LetterMap implements OnLetterListener, EventHandler<ActionEvent>, LMScreen{
    private static double WIDTH;
    private Stage stage;
    private Letter map[][];
    private Button cancel;
    private ArrayList<Letter> chosen;
    private Word currentWord;
    private HashSet<Word> allWords;
    private final double SIZE;
    private final List rootList;
    private double letterWidth, letterHeight;
    private double delta;
    private boolean failed;
    private double HEIGHT;

    public LetterMap(File file, double size, List rootList, Stage stage) {
        SIZE = size;
        this.stage = stage;
        this.rootList = rootList;
        chosen = new ArrayList<>();
        currentWord = null;
        allWords = new HashSet<>();
        try {
            parseFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void parseFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int n = Integer.parseInt(reader.readLine());
        preComputing(n);
        String s = null;
        int maxI = 0, maxJ = 0;
        while ((s = reader.readLine()) != null) {
            Color init = new Color(1, 1, 1, 1);
            Color solved = new Color(0, 1, 1, 1);
            Color chosen = new Color(1, 0, 1, 1);
            String a[] = s.split(" ");
            Letter ls[] = new Letter[a.length / 3];
            for (int k = 0; k < a.length; k += 3) {
                int i = Integer.parseInt(a[k + 1]);
                int j = Integer.parseInt(a[k + 2]);
                maxI = Math.max(maxI, i);
                maxJ = Math.max(maxJ, j);
                ls[k / 3] = new Letter(
                        a[k].charAt(0),
                        i,
                        j,
                        countX(j),
                        countY(i),
                        letterWidth,
                        letterHeight,
                        init,
                        chosen,
                        solved,
                        rootList
                );
                ls[k / 3].addOnLetterListener(this);
                map[i][j] = ls[k / 3];
            }
            allWords.add(new Word(ls));
        }
        WIDTH = countX(maxJ + 1);
        HEIGHT = countY(maxI + 2);
        cancel = new Button();
        rootList.add(cancel);
        cancel.setOnAction(this);
        cancel.setText("CLEAR");
        double canlX = (WIDTH - cancel.getWidth()) / 2;
        double canlY = countY(maxI + 1);

        cancel.setLayoutX(canlX);
        cancel.setLayoutY(canlY);
        stage.setHeight(HEIGHT);
        stage.setWidth(WIDTH);

    }

    private void preComputing(int n) {
        map = new Letter[n][n];
        double div = SIZE / n;
        delta = div * 0.2;
        div -= delta;
        letterHeight = letterWidth = div;
    }

    private double countX(int i) {
        return (i + 1) * delta + i * letterWidth;
    }

    private double countY(int i) {
        return (i + 1) * delta + i * letterWidth;
    }

    @Override
    public void onLetterPressed(Letter letter) {
        System.out.println("I am here with " + letter);
        chosen.add(letter);
        System.out.println("failed = " + failed);
        if (failed) return;
        Word word = letter.getWord();
        if (currentWord == null) {
            currentWord = word;
        }
        System.out.println("currentWord = " + currentWord);
        if (!currentWord.equals(word) || !word.input(letter)) {
            fail();
        }
        if (word.completed()) {
            word.mark();
            chosen.clear();
            allWords.remove(word);
            if (allWords.size() == 0) {
                victory();
            }
            currentWord = null;
        }
    }

    private void victory() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fill Word");
        alert.setHeaderText("You won");
        String s = "Congratulations";
        alert.setContentText(s);
        alert.showAndWait();
        Main.setLmScreen(new LevelsScreen(SIZE, rootList, stage));
    }

    private void fail() {
        if (currentWord != null)
            currentWord.setPosition(0);
        currentWord = null;
        failed = true;
    }

    @Override
    public void handle(ActionEvent event) {
        fail();
        failed = false;
        for (Letter letter : chosen) {
            letter.setState(Letter.State.INITIAL);
        }
        chosen.clear();
    }

    @Override
    public void clearScreen() {
        rootList.remove(cancel);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j].finalize();
            }
        }
    }
}
