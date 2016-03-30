package sepm.ss16.e1326125.gui;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

import java.util.Optional;

public class MainViewController<T> {

    public final String DOUBLE_REGEX ="(?:\\d*\\.)?\\d+";
    public final String INT_REGEX="\\d+";
    protected Service service;
    protected Stage primaryStage;
    private static final Logger logger = LogManager.getLogger(Main.class);

    @FXML
    private TabPane tabPane;
    @FXML
    private ProductViewController productsController;
    @FXML
    private BillViewController billViewController;
    @FXML
    private StatisticsViewController statisticsViewController;
    @FXML
    private BillEntryViewController billEntryViewController;

    public void initialize() {
        logger.debug("MainViewController initialized.");
    }

    public void setServicesAndFillTablesWithData(Service service) throws ServiceException {
        this.service = service;
        productsController.setServiceAndFillTableWithData(service);
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
        productsController.setPrimaryStage(primaryStage);
        //billViewController.setPrimaryStage(primaryStage);
    }

    public Optional fxDialog(String title, String header, String content, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getDialogPane().setMinSize(200, 150);
        return alert.showAndWait();
    }

    public boolean inputIsValid(TextField textField, String regex) {
        if (!textField.getText().matches(regex)) {
            return false;
        } else {
            return true;
        }
    }

    @FXML
    public void onClickedExit() {
        Optional<ButtonType> result = fxDialog("Exit Application", "", "Are you sure you want to exit the application?", Alert.AlertType.CONFIRMATION);
        if (result.get() == ButtonType.OK) {
            primaryStage.close();
            try {
                service.close();
            } catch (ServiceException e) {
                fxDialog("Exit application", "", e.getMessage(), Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    public void onClickedAbout() {
        fxDialog("About", "", "Super Erfolgreicher Pferde Megaoutlet \n " +
                "Version 1.0 (Beta)\n" +
                "Robert Barta (1326125)", Alert.AlertType.INFORMATION);
    }
}
