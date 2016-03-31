package sepm.ss16.e1326125.gui;

import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.dao.LimitType;
import sepm.ss16.e1326125.entity.Product;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

import java.util.List;
import java.util.Optional;

public class PriceAdjustmentViewController {

    private static final Logger logger = LogManager.getLogger(PriceAdjustmentViewController.class);

    private Stage stage;
    private ProductViewController productsController;
    private Service service;
    private TableView<Product> productTable;
    private Boolean decreasePrice;
    private LimitType limitType;
    private Double amountOrPercentage;
    private Integer limit, amountOfDays;


    ObservableList<String> signChoices = FXCollections.observableArrayList("Increase", "Decrease");
    ObservableList<String> kindOfLimitChoices = FXCollections.observableArrayList("or more times", "or less times", "most", "least");
    ObservableList<String> kindOfAdjustmentChoices = FXCollections.observableArrayList("percentage", "amount");

    @FXML
    private ChoiceBox<String> signChoiceBox;

    @FXML
    private TextField limitTextField;

    @FXML
    private ChoiceBox<String> kindOfLimitChoiceBox;

    @FXML
    private TextField amountOfDaysTextField;

    @FXML
    private ChoiceBox<String> kindOfAdjustmentChoiceBox;

    @FXML
    private TextField amountTextField;

    @FXML
    private Button confirmButton;


    public void initialize() {
        signChoiceBox.setValue("Increase");
        kindOfLimitChoiceBox.setValue("or more times");
        kindOfAdjustmentChoiceBox.setValue("percentage");
        signChoiceBox.setItems(signChoices);
        kindOfLimitChoiceBox.setItems(kindOfLimitChoices);
        kindOfAdjustmentChoiceBox.setItems(kindOfAdjustmentChoices);
    }

    public void setControllerAndStage(ProductViewController productsController, Stage stage) {
        this.stage = stage;
        this.productsController = productsController;
        this.service = productsController.getService();
        this.productTable = productsController.getProductTable();

        stage.setTitle("Price Adjustment");
    }

    @FXML
    void onClickedClose() {
        stage.close();
    }

    @FXML
    void onClickedConfirm() {
        if (!inputIsValid()) {
            productsController.fxDialog("Price Adjustment", "Invalid Input", "One or more text fields are empty or contain invalid characters.", Alert.AlertType.ERROR);
        } else {

            convertInput();

            try {
                Integer toBeChanged = service.getAlterPriceSize(amountOfDays, limit, limitType);

                if (kindOfAdjustmentChoiceBox.getValue().equals("percentage")) {
                    Optional<ButtonType> result = productsController.fxDialog("PriceAdjustment", "Are you sure?", toBeChanged + " products are going to be affected.", Alert.AlertType.CONFIRMATION);
                    if (result.get() == ButtonType.OK) {
                        logger.debug("User wants percentage adjustment.");
                        service.alterPriceByPercentage(amountOfDays, amountOrPercentage, decreasePrice, limit, limitType);
                    }
                }
                if (kindOfAdjustmentChoiceBox.getValue().equals("amount")) {
                    Optional<ButtonType> result = productsController.fxDialog("PriceAdjustment", "Are you sure?", toBeChanged + " products are going to be affected.", Alert.AlertType.CONFIRMATION);
                    if (result.get() == ButtonType.OK) {
                        service.alterPriceByAmount(amountOfDays, amountOrPercentage, decreasePrice, limit, limitType);
                    }
                }

                logger.debug("Refreshing table.");
                productsController.setAndFill(service);
                productTable.getColumns().get(0).setVisible(false);
                productTable.getColumns().get(0).setVisible(true);
                stage.close();
            } catch (ServiceException e) {
                productsController.fxDialog("Price Adjustment", "ERROR", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void convertInput() {
        if (signChoiceBox.getValue().equals("Increase")) {
            this.decreasePrice = false;
        } else {
            this.decreasePrice = true;
        }

        if (kindOfLimitChoiceBox.getValue().equals("or more times")) {
            this.limitType = LimitType.MINIMUM;
        } else if (kindOfLimitChoiceBox.getValue().equals("or less times")) {
            this.limitType = LimitType.MAXIMUM;
        } else if (kindOfLimitChoiceBox.getValue().equals("most")) {
            this.limitType = LimitType.MOST;
        } else {
            this.limitType = LimitType.LEAST;
        }

        this.amountOfDays = Integer.valueOf(amountOfDaysTextField.getText());
        this.amountOrPercentage = Double.valueOf(amountTextField.getText());
        this.limit = Integer.valueOf(limitTextField.getText());
    }

    private boolean inputIsValid() {
        boolean valid1 = productsController.inputIsValid(limitTextField, productsController.INT_REGEX);
        boolean valid2 = productsController.inputIsValid(amountOfDaysTextField, productsController.INT_REGEX);
        boolean valid3 = productsController.inputIsValid(amountTextField, productsController.DOUBLE_REGEX);

        return (valid1 && valid2 && valid3);
    }
}
