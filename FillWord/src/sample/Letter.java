package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import static sample.Letter.State.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Максим on 14.12.2016.
 */
public class Letter implements EventHandler<MouseEvent> {
    private char value;
    private int i, j; // TODO do i need this ??
    private Word word;
    private List rootList;
    private Button button;
    private Color initial, chosen, solved;
    private State state = INITIAL;
    private ArrayList<OnLetterListener> onLetterListeners = new ArrayList<>();


    @Override
    public void handle(MouseEvent event) {
        setState(CHOSEN);
        for (OnLetterListener listener : onLetterListeners) {
            listener.onLetterPressed(this);
        }
    }

    public void addOnLetterListener(OnLetterListener listener) {
        onLetterListeners.add(listener);
    }

    public State getState() {
        return state;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Word getWord() {
        return word;
    }


    private void setColor(Color color) {
        button.setBackground((new Background(new BackgroundFill(
                color,
                CornerRadii.EMPTY,
                Insets.EMPTY))));
    }

    public void setState(State state) {
        this.state = state;
        if (state == INITIAL)
            setColor(initial);
        else if (state == SOLVED) {
            button.setDisable(true);
            setColor(solved);
        } else if (state == CHOSEN) {
            setColor(chosen);
        }
    }

    public static enum State {
        INITIAL, CHOSEN, SOLVED
    }

    public Letter(char value,
                  int i,
                  int j,
                  double x,
                  double y,
                  double width,
                  double height,
                  Color initial,
                  Color chosen,
                  Color solved,
                  List rootList) {
        this.value = value;
        this.i = i;
        this.j = j;
        this.rootList = rootList;
        this.initial = initial;
        this.chosen = chosen;
        this.solved = solved;
        createButton(x, y, width, height);
        rootList.add(button);
    }

    @Override
    public String toString() {
        return value + "";
    }

    private void createButton(double x, double y, double width, double height) {
        button = new Button();
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setMaxWidth(width);
        button.setMinWidth(width);
        button.setMaxHeight(height);
        button.setMinHeight(height);
        button.setText(value + "");
        button.setOnMouseClicked(this);
        setColor(initial);
    }


    @Override
    protected void finalize() {
        rootList.remove(button);
    }
}
