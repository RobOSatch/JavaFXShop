package sepm.ss16.e1326125.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;
import sepm.ss16.e1326125.service.SimpleService;

public class Main extends Application {

    public static Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            Service service = new SimpleService();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainViewController controller = loader.getController();
            controller.setServicesAndFillTablesWithData(service);
            controller.setPrimaryStage(primaryStage);
            primaryStage.setTitle("Super Erfolgreicher Pferde Megaoutlet");
            primaryStage.setMinHeight(500);
            primaryStage.setMinWidth(800);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            logger.info("Application started.");
        }
        catch (ServiceException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Super Erfolgreicher Pferde Megaoutlet");
            alert.setHeaderText("ERROR");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
