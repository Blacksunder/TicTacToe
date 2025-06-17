package app.web.model;

public class WebGameField {
    private char[][] field;

    public WebGameField() {
        field = new char[3][3];
    }

    public char[][] getField() {
        return field;
    }

    public void setField(char[][] field) {
        this.field = field;
    }
}
