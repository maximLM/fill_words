package sample;

import java.util.ArrayList;

/**
 * Created by Максим on 25.12.2016.
 */
public class FieldWord {
    private ArrayList<Field> body;
    public FieldWord(ArrayList<Field> body) {
        StringBuilder builder = new StringBuilder();
        this.body = new ArrayList<>();
        for (Field field : body) {
            builder.append(field.getVal());
            this.body.add(field);
            field.setState(Field.State.CONFIRMED);
        }
        System.out.println(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Field field : body) {
            builder.append(field).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
