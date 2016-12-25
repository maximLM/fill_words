package sample;

import org.omg.CORBA.INITIALIZE;

/**
 * Created by Максим on 15.12.2016.
 */
public class Word {
    private Letter letters[];
    private String value;
    private int position;

    public Word(Letter ... letters) {
        this.letters = letters.clone();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < letters.length; i++) {
            builder.append(letters[i]);
            letters[i].setWord(this);
        }
        value = builder.toString();
    }

    public boolean input(Letter letter) {
        System.out.println(letter);
        return letter.equals(letters[position++]);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean completed() {
        return position == letters.length;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == Word.class && ((Word) obj).value.equals(value);
    }

    public void mark() {
        for (Letter letter : letters) {
            letter.setState(Letter.State.SOLVED);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
