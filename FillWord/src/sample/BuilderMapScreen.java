package sample;

import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Максим on 25.12.2016.
 */
public class BuilderMapScreen implements LMScreen, EventHandler<KeyEvent> {
    private final double SIZE;
    private final List rootList;
    private final Stage stage;
    private Field map[][];
    private double delta;
    private double fieldWidth, fieldHeight;
    private Field current = null;
    private ArrayList<Field> temp;
    private int free;
    private HashSet<FieldWord> words;
    private Field old = null;
    private Button addWord, clear, back, clearAll;

    public BuilderMapScreen(double size, List rootList, Stage stage, Group root) {
        this.SIZE = size;
        this.rootList = rootList;
        this.stage = stage;
        int n = detN();
        preComputing(n);
        fillMap();
        root.requestFocus();
        root.setOnKeyPressed(this);
    }

    private int detN() {
        int n = 3;
        ArrayList<Integer> data = new ArrayList<Integer>() {{
            int i = 3;
            add(i++);
            add(i++);
            add(i++);
            add(i++);
            add(i++);
            add(i++);
        }};
        ChoiceDialog<Integer> dialog = new ChoiceDialog<Integer>(data.get(0), data);
        dialog.setTitle("Choose dimension");
        dialog.setHeaderText("Select your choice");
        Optional<Integer> result = dialog.showAndWait();
        if (result.isPresent()) {
            n = result.get();
        }

        return n;
    }

    private void preComputing(int n) {
        temp = new ArrayList<>();
        words = new HashSet<>();
        map = new Field[n][n];
        free = n * n;
        double div = SIZE / n;
        delta = div * 0.2;
        div -= delta;
        fieldHeight = fieldWidth = div;
    }

    private void fillMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = new Field(i, j, countX(j), countY(i), rootList, fieldWidth, this);
            }
        }
        double width = countX(map.length);
        stage.setWidth(width);
        stage.setHeight(countY(map.length + 2));
        double butH = countY(map.length);
        addWord = new Button("ADD");
        addWord.setOnAction(event -> addWord());
        addWord.setLayoutX(delta);
        addWord.setLayoutY(butH);
        clear = new Button("CLEAR");
        clear.setOnAction(event -> clear());
        clear.setLayoutX(width * 2 / 8);
        clear.setLayoutY(butH);
        clearAll = new Button("CLEAR ALL");
        clearAll.setOnAction(event -> {
            if (current != null) current.setState(Field.State.EMPTY);
            current = old = null;
            for (Field field : temp) {
                field.setState(Field.State.EMPTY);
            }
            temp.clear();
        });
        clearAll.setLayoutX(width * 2 / 4);
        clearAll.setLayoutY(butH);
        back = new Button("BACK");
        back.setOnAction(event -> back());
        back.setLayoutX(width * 3 / 4);
        back.setLayoutY(butH);
        rootList.add(addWord);
        rootList.add(back);
        rootList.add(clear);
        rootList.add(clearAll);
    }

    private void addWord() {
        words.add(new FieldWord(temp));
        free -= temp.size();
        current = null;
        old = null;
        temp.clear();
        if (free == 0) {
            writeToFile();
            back();
        }

    }

    private void clear() {
        if (current != null) current.setState(Field.State.EMPTY);
        current = null;
        if (temp.size() == 0) return;
        temp.remove(temp.size() - 1).setState(Field.State.EMPTY);
        old = temp.size() == 0 ? null : temp.get(temp.size() - 1);
    }

    private void writeToFile() {
        int cur = new File(Main.LEVELS).listFiles().length + 1;
        File file = new File(Main.LEVELS + "/" + cur + Main.EXT);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(map.length + "\n");
            for (Iterator<FieldWord> it = words.iterator(); it.hasNext(); ) {
                writer.write(it.next() + "");
                if (it.hasNext()) {
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void back() {
        System.out.println("What's up ???");
        Main.setLmScreen(new LevelsScreen(Main.SIZE, rootList, stage));
        System.out.println("here again");
    }


    private double countX(int i) {
        return (i + 1) * delta + i * fieldWidth;
    }

    private double countY(int i) {
        return (i + 1) * delta + i * fieldWidth;
    }

    @Override
    public void clearScreen() {
        for (Field[] fields : map) {
            for (Field field : fields) {
                field.finalize();
            }
        }
        rootList.remove(addWord);
        rootList.remove(back);
        rootList.remove(clear);
        rootList.remove(clearAll);
    }

    public void madeChoice(Field field) {
        if ((old == null && current == null) || (current == null && Math.abs(old.getI() - field.getI()) + Math.abs(old.getJ() - field.getJ()) == 1)) {
            field.setState(Field.State.CHOSEN);
            current = field;
        }
    }

    public void inputted(Field field, char val) {
        if (current != null && field.equals(current)) {
            current.setState(Field.State.INPUTTED);
            current.setVal(val);
            temp.add(current);
            old = current;
            current = null;
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.BACK_SPACE) {
            clear();
        } else if (event.getCode() == KeyCode.ENTER) {
            addWord();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            back();
        } else if (current != null)
            inputted(current, event.getCode().toString().charAt(0));
    }
}
