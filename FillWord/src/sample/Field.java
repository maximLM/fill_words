package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.List;


import static sample.Field.State.*;

/**
 * Created by Максим on 25.12.2016.
 */
public class Field {

    private Character val = null;
    private final int i;
    private final int j;
    private final double x;
    private final double y;
    private final List rootList;
    private FieldWord word;
    private Button button;
    private State state;
    private Color empty, chosen, confirmed;
    private BuilderMapScreen map;
    private Color inputted;


    public FieldWord getWord() {
        return word;
    }

    public void setWord(FieldWord word) {
        this.word = word;
    }

    public void setVal(char val) {
        button.setText(val + "");
        this.val = val;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public Character getVal() {
        return val;
    }

    public static enum State {
        EMPTY, CHOSEN, INPUTTED, CONFIRMED
    }

    public Field(int i, int j, double x, double y, List rootList, double size, BuilderMapScreen map) {
        state = EMPTY;
        this.map = map;
        empty = new Color(.4,.4,.4,1);
        chosen = Color.CORAL;
        confirmed = Color.WHEAT;
        inputted = Color.BLANCHEDALMOND;
        this.i = i;
        this.j = j;
        this.x = x;
        this.y = y;
        this.rootList = rootList;
        button = new Button();
        button.setLayoutY(y);
        button.setLayoutX(x);
        rootList.add(button);
        button.setOnAction(event -> map.madeChoice(this));
        button.setPrefWidth(size);
        button.setPrefHeight(size);
        setColor(empty);
    }

    public void setState(State state) {
        this.state = state;
        if (state == EMPTY) {
            button.setText("");
            button.setOnKeyPressed(null);
            button.setDisable(false);
            setColor(empty);
        } else if (state == CHOSEN) {
            setColor(chosen);
        } else if (state == INPUTTED) {
            button.setOnKeyPressed(null);
            setColor(inputted);
            button.setDisable(true);
        } else if (state == CONFIRMED) {
            setColor(confirmed);
            button.setDisable(true);
        }
    }

    private void setColor(Color color) {
        button.setBackground((new Background(new BackgroundFill(
                color,
                CornerRadii.EMPTY,
                Insets.EMPTY))));
    }

    @Override
    protected void finalize() {
        rootList.remove(button);
    }

    @Override
    public String toString() {
        return val + " " + i + " " + j;
    }
}
