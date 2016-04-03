package sepm.ss16.e1326125.gui;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

import java.io.IOException;
import java.util.Optional;

public class MainViewController {

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
    private BillViewController billsController;
    @FXML
    private StatisticsViewController statisticsController;
    @FXML
    private Tab productsTab;

    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
                if(newTab == productsTab) {
                    try {
                        productsController.setAndFill(service);
                    } catch (ServiceException e) {
                        fxDialog("MainView", "ERROR", "Failed to update", Alert.AlertType.ERROR);
                    }
                }
             }
        });
        }

    public void setAndFill(Service service) throws ServiceException {
        this.service = service;
        productsController.setAndFill(service);
        billsController.setAndFill(service);
        statisticsController.setService(service);
        statisticsController.setChoices();
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
        productsController.setPrimaryStage(primaryStage);
        billsController.setPrimaryStage(primaryStage);
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

    @FXML
    public void onClickedNewProduct() {
        try {
            productsController.openCreateWindow();
        } catch (IOException e) {
            fxDialog("Creating Product", "ERROR", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickedNewBill() {
        try {
            billsController.openCreateWindow();
        } catch (IOException e) {
            fxDialog("Creating Bill", "ERROR", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickedSetThemeRegular() {
        primaryStage.getScene().getStylesheets().remove("res/stylesheet_black.css");
        primaryStage.getScene().getStylesheets().add("res/modena.css");
    }

    @FXML
    public void onClickedSetThemeBlack() {
        primaryStage.getScene().getStylesheets().remove("res/modena.css");
        primaryStage.getScene().getStylesheets().add("res/stylesheet_black.css");
    }
}
