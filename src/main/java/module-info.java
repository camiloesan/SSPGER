module mx.uv.fei.gui {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
    requires java.logging;
    requires java.sql;
    requires java.mail;
    requires org.apache.commons.logging;
    requires log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires org.kordamp.bootstrapfx.core;

    opens mx.uv.fei.logic to javafx.fxml;
    opens mx.uv.fei.gui to javafx.fxml;
    exports mx.uv.fei.gui;
    exports mx.uv.fei.logic;
}