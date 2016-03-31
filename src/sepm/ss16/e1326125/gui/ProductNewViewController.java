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

public class ProductNewViewController {
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
    private TextField nameTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField stockTextField;

    @FXML
    private Button createProductButton;

    public void setControllerAndStage(ProductViewController productsController, Stage stage) {
        this.stage = stage;
        this.productsController = productsController;
        this.service = productsController.getService();
        this.productTable = productsController.getProductTable();

        stage.setTitle("Create Product");
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
    void onClickedCreateProduct() {
        if (!inputIsValid(priceTextField, stockTextField) || (nameTextField.getText().isEmpty() || priceTextField.getText().isEmpty() || stockTextField.getText().isEmpty() || imagePath.isEmpty())) {
            productsController.fxDialog("Creating Product", "Invalid Input", "Missing image or one or more text fields are empty or contain invalid characters.", Alert.AlertType.ERROR);
        } else {
            this.product = new Product(null, nameTextField.getText(), Double.valueOf(priceTextField.getText()), Integer.valueOf(stockTextField.getText()), imagePath, false);

            try {
                Product p = service.newProduct(product);
                productTable.getItems().add(p);
                productTable.getSelectionModel().selectLast();
                productTable.getColumns().get(0).setVisible(false);
                productTable.getColumns().get(0).setVisible(true);
                File file = new File(product.getImage());
                Image value = new Image(file.toURI().toURL().toExternalForm());
                productsController.getProductImageView().setImage(value);
                stage.close();
                productsController.fxDialog("Creating Product", "", "Product creation successful.", Alert.AlertType.INFORMATION);
            } catch (ServiceException e) {
                productsController.fxDialog("Creating Product", "ERROR", "Failed to create product.", Alert.AlertType.ERROR);
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
