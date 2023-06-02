package mx.uv.fei.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainStage extends Application {
    private static Scene scene;
    public static final int HEIGHT_OFFSET = 44;

    public static void main(String[] args) {
        launch();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainStage.class.getResource(fxml));
        return fxmlLoader.load();
    }

    public static void changeView(String url, int width, int height) throws IOException {
        Stage currentStage = (Stage) scene.getWindow();
        configureStage(currentStage, width, height);
        MainStage.setRoot(url);
    }

    private static void configureStage(Stage stage, int width, int height) {
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login-view.fxml"), 600, 400);
        stage.setTitle("Gestión de Proyecto Guiado y Experiencia Recepcional");
        stage.setScene(scene);
        stage.show();
    }
}
