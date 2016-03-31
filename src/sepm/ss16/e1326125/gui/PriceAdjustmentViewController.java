package sepm.ss16.e1326125.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sepm.ss16.e1326125.entity.Product;
import sepm.ss16.e1326125.service.Service;

public class PriceAdjustmentViewController {

    private Stage stage;
    private ProductViewController productsController;
    private Service service;
    private TableView<Product> productTable;

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
    private TextField amoutOfDaysTextField;

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
    }
}
