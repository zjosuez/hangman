module org.jogo.da.forca.jogodaforca {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens org.jogo.da.forca.jogodaforca to javafx.fxml;
    opens org.jogo.da.forca.jogodaforca.model to javafx.base;
    exports org.jogo.da.forca.jogodaforca;
}