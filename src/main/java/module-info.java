module mx.uv.fei.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.logging;
    requires java.sql;
    requires log4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires javafx.swing;
    requires kernel;
    requires layout;
    requires io;
    opens mx.uv.fei.logic to javafx.fxml;
    opens mx.uv.fei.gui to javafx.fxml;
    exports mx.uv.fei.gui;
    exports mx.uv.fei.logic;
}