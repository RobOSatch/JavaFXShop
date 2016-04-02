package sepm.ss16.e1326125.gui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.entity.Product;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

public class ProductViewController extends MainViewController {

    private static final Logger logger = LogManager.getLogger(Main.class);
    private Double fromPrice, toPrice;
    private Integer fromStock, toStock;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> productIDcolumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> productPriceColumn;

    @FXML
    private TableColumn<Product, String> productStockColumn;

    @FXML
    private ImageView productImage;

    @FXML
    private TextField productPriceFromTextField;

    @FXML
    private TextField productPriceToTextField;

    @FXML
    private CheckBox priceCheckBox;

    @FXML
    private CheckBox stockCheckBox;

    @FXML
    private TextField productStockFromTextField;

    @FXML
    private TextField productStockToTextField;

    @FXML
    private Button productSearchStartButton;

    @FXML
    private Button productListAllButton;

    @FXML
    private Button productEditButton;

    @FXML
    private Button productDeleteButton;

    @FXML
    private Button productPriceAlterationButton;

    public void initialize() {

        productIDcolumn.setCellValueFactory(new PropertyValueFactory<Product, String>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("stock"));
        productTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
                if(newValue==null || newValue.getImage().isEmpty()){
                    productImage.setVisible(false);
                } else {
                    try {
                        File file = new File(newValue.getImage());
                        Image value = new Image(file.toURI().toURL().toExternalForm());
                        productImage.setImage(value);
                        productImage.setVisible(true);
                    } catch (MalformedURLException e) {
                        productImage.setVisible(false);
                    }
                }
            }
        });
        logger.debug("productViewController initialized.");
    }

    public void setAndFill(Service service) throws ServiceException {
        this.service = service;
        priceCheckBox.setSelected(false);
        stockCheckBox.setSelected(false);
        List<Product> list = service.listAllProducts();
        productTable.setItems(FXCollections.observableArrayList(list));
        productTable.getSelectionModel().selectFirst();
    }

    public Service getService(){
        return service;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public TableView<Product> getProductTable() {
        return productTable;
    }

    public ImageView getProductImageView(){
        return productImage;
    }

    @FXML
    public void onClickedDelete() {
        logger.debug("User clicked on delete product button.");

        Product product = productTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Product");
        alert.setHeaderText("");
        alert.setContentText("Are you sure you want to delete this product? This action can't be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                service.deleteProduct(product);
                productTable.getItems().remove(product);
            } catch (ServiceException e) {
                fxDialog("Deleting Product", "Couldn't delete product", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void onSelectProductCheckBox() {
        boolean cBPrice = priceCheckBox.isSelected();
        boolean cBStock = stockCheckBox.isSelected();
        productPriceFromTextField.setDisable(!cBPrice);
        productPriceToTextField.setDisable(!cBPrice);
        productStockFromTextField.setDisable(!cBStock);
        productStockToTextField.setDisable(!cBStock);

        if (!cBPrice && !cBStock) {
            productSearchStartButton.setDisable(true);
        } else {
            productSearchStartButton.setDisable(false);
        }
    }

    @FXML
    public void onClickedProductStartSearch() {

        // Validate input.
        boolean textIsValid1 = inputIsValid(productPriceFromTextField, DOUBLE_REGEX);
        boolean textIsValid2 = inputIsValid(productPriceToTextField, DOUBLE_REGEX);
        boolean textIsValid3 = inputIsValid(productStockFromTextField, INT_REGEX);
        boolean textIsValid4 = inputIsValid(productStockToTextField, INT_REGEX);

        boolean cBPrice = priceCheckBox.isSelected();
        boolean cBStock = stockCheckBox.isSelected();

        if (!(cBPrice && textIsValid1 && textIsValid2) && !(cBStock && textIsValid3 && textIsValid4)) {
            fxDialog("Searching products", "Invalid Input", "Only numbers allowed, decimal for price and integer for stock.", Alert.AlertType.ERROR);
        } else {
                // Get values from text fields.
                if (!productPriceFromTextField.isDisabled() && !productPriceToTextField.isDisabled()) {
                    fromPrice = Double.valueOf(productPriceFromTextField.getText());
                    toPrice = Double.valueOf(productPriceToTextField.getText());
                } else {
                    fromPrice = 0.0;
                    toPrice = 1000000000000.0;
                }

                if (!productStockFromTextField.isDisabled() && !productStockToTextField.isDisabled()) {
                    fromStock = Integer.valueOf(productStockFromTextField.getText());
                    toStock = Integer.valueOf(productStockToTextField.getText());
                } else {
                    fromStock = 0;
                    toStock = 1000000000;
                }

                // Search in database.
                try {
                    Product from = new Product(null, null, fromPrice, fromStock, null, null);
                    Product to = new Product(null, null, toPrice, toStock, null, null);
                    List<Product> list = service.searchProducts(from, to);
                    productTable.setItems(FXCollections.observableArrayList(list));
                    productTable.getSelectionModel().selectFirst();

                    if (list.isEmpty()) {
                        fxDialog("Searching products", "", "No products matched your search criteria.", Alert.AlertType.INFORMATION);
                    }
                } catch (ServiceException e) {
                    fxDialog("Searching products", "Search failed to execute", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
    }

    @FXML
    public void onClickedListAllProducts() {
        try {
            List<Product> list = service.listAllProducts();
            productTable.setItems(FXCollections.observableArrayList(list));
            productTable.getSelectionModel().selectFirst();
        } catch (ServiceException e) {
            fxDialog("List Products", "Listing Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickedEditProduct() {
        try {
            openEditWindow();
        } catch (IOException e) {
            fxDialog("Edit Product", "ERROR", "Can't open edit window.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickedPriceAdjustment() {
        try {
            openPriceAdjustmentWindow();
        } catch (IOException e) {
            fxDialog("Price Adjustment", "ERROR", "Can't open price adjustment window.", Alert.AlertType.ERROR);
        }
    }

    public void openEditWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductEditView.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        ProductEditViewController controller = loader.getController();
        controller.setControllerAndStage(this, stage);
        stage.showAndWait();
    }

    public void openCreateWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductNewView.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        ProductNewViewController controller = loader.getController();
        controller.setControllerAndStage(this, stage);
        stage.showAndWait();
    }

    public void openPriceAdjustmentWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PriceAdjustmentView.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        PriceAdjustmentViewController controller = loader.getController();
        controller.setControllerAndStage(this, stage);
        stage.showAndWait();
    }
}
