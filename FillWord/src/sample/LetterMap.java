package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Максим on 15.12.2016.
 */
public class LetterMap implements OnLetterListener, LMScreen, EventHandler<KeyEvent> {
    private static double WIDTH;
    private Stage stage;
    private Letter map[][];
    private Button clear, back;
    private ArrayList<Letter> chosen;
    private Word currentWord;
    private HashSet<Word> allWords;
    private final double SIZE;
    private final List rootList;
    private double letterWidth, letterHeight;
    private double delta;
    private boolean failed;
    private double HEIGHT;
    private Group root;

    public LetterMap(File file, double size, List rootList, Stage stage, Group root) {
        SIZE = size;
        this.root = root;
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
        root.setOnKeyPressed(this);
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
        HEIGHT = countY(maxI + 3);
        clear = new Button();
        rootList.add(clear);
        clear.setOnAction(event -> clear());
        clear.setText("CLEAR");
        double canlX = (WIDTH - clear.getWidth()) / 2;
        double canlY = countY(maxI + 1);

        clear.setLayoutX(delta);
        clear.setLayoutY(canlY);
        back = new Button("BACK");
        back.setLayoutX(canlX);
        back.setLayoutY(canlY);
        back.setOnAction(event -> back());
        rootList.add(back);
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

    private void clear() {
        fail();
        failed = false;
        for (Letter letter : chosen) {
            letter.setState(Letter.State.INITIAL);
        }
        chosen.clear();
    }


    @Override
    public void clearScreen() {
        rootList.remove(clear);
        rootList.remove(back);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j].finalize();
            }
        }
    }

    private void back() {
        Main.setLmScreen(new LevelsScreen(Main.SIZE, rootList, stage));
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.BACK_SPACE) {
            clear();
        } else if (event.getCode() == KeyCode.ESCAPE){
            back();
        }
    }
}
