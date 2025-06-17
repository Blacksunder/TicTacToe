package app.domain.model;

public class DomainGameField {
    private FieldTypes[][] field;

    public DomainGameField() {
        field = new FieldTypes[3][3];
        for (int y = 0; y < field.length; ++y) {
            for (int x = 0; x < field[y].length; ++x) {
                field[y][x] = FieldTypes.EMPTY;
            }
        }
    }

    public FieldTypes[][] getField() {
        return field;
    }

    public void setField(FieldTypes[][] field) {
        this.field = field;
    }
}
