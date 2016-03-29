package sepm.ss16.e1326125.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

    public static Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        logger.error("Logger Test");
        primaryStage.setTitle("Super Erfolgreicher Pferde Megaoutlet");
        primaryStage.setScene(new Scene(root, 980, 750));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
