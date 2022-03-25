module ca.bcit.comp2522.termproject.jjo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens ca.bcit.comp2522.termproject.jjo to javafx.fxml;
    exports ca.bcit.comp2522.termproject.jjo;
}