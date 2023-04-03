module mx.uv.fei.gui {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
    requires java.logging;
    requires java.sql;

    opens mx.uv.fei.gui to javafx.fxml;
    exports mx.uv.fei.gui;
}