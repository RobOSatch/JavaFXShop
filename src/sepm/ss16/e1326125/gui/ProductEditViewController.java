package sepm.ss16.e1326125.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.entity.Product;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

import java.io.File;
import java.io.IOException;

public class ProductEditViewController {

    private static final Logger logger = LogManager.getLogger(ProductEditViewController.class);

    private ProductViewController productsController;
    private Stage stage;
    private Service service;
    private TableView<Product> productTable;
    private Product product;
    private String newName;
    private Double newPrice;
    private Integer newStock;
    private String imagePath;

    @FXML
    private TextField newNameTextField;

    @FXML
    private TextField newPriceTextField;

    @FXML
    private TextField newStockTextField;

    @FXML
    private Button updateProductButton;

    public void setControllerAndStage(ProductViewController productsController, Stage stage) {
        this.stage = stage;
        this.productsController = productsController;
        this.service = productsController.getService();
        this.productTable = productsController.getProductTable();
        this.product = this.productTable.getSelectionModel().getSelectedItem();

        this.newName = product.getName();
        this.newPrice = product.getPrice();
        this.newStock = product.getStock();
        this.imagePath = product.getImage();

        stage.setTitle("Edit Product");
    }

    @FXML
    void onClickedCancel() {
        stage.close();
    }

    @FXML
    void onClickedChooseFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter allowedFileTypes = new FileChooser.ExtensionFilter("Images (gif, jpg, jpeg, png)", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.setTitle("Choose an image for the product");
        fileChooser.getExtensionFilters().add(allowedFileTypes);

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            imagePath = file.getAbsolutePath();
        }
    }

    @FXML
    void onClickedUpdateProduct() {
        if (!inputIsValid(newPriceTextField, newStockTextField)) {
            productsController.fxDialog("Edit Product", "Invalid Input", "One or more of the values contain invalid characters.", Alert.AlertType.ERROR);
        } else {
            if (!newNameTextField.getText().isEmpty()) {
                newName = newNameTextField.getText();
            }

            if (!newPriceTextField.getText().isEmpty()) {
                newPrice = Double.valueOf(newPriceTextField.getText());
            }

            if (!newStockTextField.getText().isEmpty()) {
                newStock = Integer.valueOf(newStockTextField.getText());
            }

            product.setName(newName);
            product.setPrice(newPrice);
            product.setStock(newStock);
            product.setImage(imagePath);

            try {
                service.editProduct(product);
                productTable.getColumns().get(0).setVisible(false);
                productTable.getColumns().get(0).setVisible(true);
                File file = new File(product.getImage());
                Image value = new Image(file.toURI().toURL().toExternalForm());
                productsController.getProductImageView().setImage(value);
                stage.close();
                productsController.fxDialog("Edit Product", "", "Update was successful.", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                productsController.fxDialog("Edit Product", "ERROR", "Failed to update product.", Alert.AlertType.ERROR);
            } catch (IOException ex) {

            }
        }
    }

    protected boolean inputIsValid(TextField textField1, TextField textField2){
        boolean a = false;
        boolean b = false
                ;
        if (productsController.inputIsValid(textField1, productsController.DOUBLE_REGEX) || textField1.getText().isEmpty()) {
            a = true;
        }

        if (productsController.inputIsValid(textField2, productsController.INT_REGEX) || textField2.getText().isEmpty()) {
            b = true;
        }

        return (a && b);
    }
}
