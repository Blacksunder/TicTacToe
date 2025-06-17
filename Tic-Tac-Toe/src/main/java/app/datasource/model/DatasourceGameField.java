package app.datasource.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.Type;

@Embeddable
public class DatasourceGameField {

    @Column (name = "field", length = 9)
    private String field = "---------";

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
