module org.example.crud {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens org.example.crud to javafx.fxml;
    exports org.example.crud;
}