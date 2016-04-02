package sepm.ss16.e1326125.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import sepm.ss16.e1326125.entity.Bill;
import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.entity.Product;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;
import javafx.scene.control.TableColumn.CellEditEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BillCreateViewController {

    private BillViewController billsController;
    private Stage stage;
    private Service service;
    private Integer invoice;

    @FXML
    private TableView<Product> selectProductTable;

    @FXML
    private TableColumn<Product, String> nameSelectColumn;

    @FXML
    private TableColumn<Product, String> priceSelectColumn;

    @FXML
    private TableColumn<Product, String> stockSelectColumn;

    @FXML
    private TableView<BillEntry> toAddProductTable;

    @FXML
    private TableColumn<BillEntry, String> nameAddColumn;

    @FXML
    private TableColumn<BillEntry, String> quantityAddColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private ChoiceBox<String> paymentChoiceBox;

    public void initialize() {
        paymentChoiceBox.getItems().add("Cash");
        paymentChoiceBox.getItems().add("Credit Card");
        paymentChoiceBox.getItems().add("Giro");
        paymentChoiceBox.getItems().add("PayPal");
        paymentChoiceBox.getItems().add("Check");
        paymentChoiceBox.setValue("Cash");

        nameSelectColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        priceSelectColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        stockSelectColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("stock"));

        nameAddColumn.setCellValueFactory(new PropertyValueFactory<BillEntry, String>("productName"));
        quantityAddColumn.setCellValueFactory(new PropertyValueFactory<BillEntry, String>("quantity"));

        if (toAddProductTable.getItems().isEmpty()) {
            removeButton.setDisable(true);
        }
    }

    public void setControllerAndStage(BillViewController billsController, Stage stage) {
        this.stage = stage;
        this.billsController = billsController;
        this.service = billsController.getService();

        List<Product> products = null;
        try {
            products = service.listAllProducts();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        selectProductTable.setItems(FXCollections.observableArrayList(products));
        selectProductTable.getSelectionModel().selectFirst();

        stage.setTitle("Create Bill");
    }

    @FXML
    void onClickedAdd() {
        selectionIsValid(selectProductTable);
        Product toAdd = selectProductTable.getSelectionModel().getSelectedItem();
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Item");
        dialog.setHeaderText("Enter quantity");
        dialog.setContentText("");

        Optional<String> result = dialog.showAndWait();
        if (result.get() != null) {
            if (result.get().matches(billsController.INT_REGEX)) {
                if (Integer.valueOf(result.get()) > 0 && Integer.valueOf(result.get()) <= toAdd.getStock()) {
                    toAddProductTable.getItems().add(new BillEntry(null, null, toAdd.getProductId(), toAdd.getName(), toAdd.getPrice(), Integer.valueOf(result.get())));
                    selectProductTable.getItems().remove(toAdd);
                } else {
                    billsController.fxDialog("Create Bill", "Invalid Input", "Quantity can't be greater than available quantity.", Alert.AlertType.ERROR);
                }
            } else {
                billsController.fxDialog("Create Bill", "Invalid Input", "Quantity must be positive integer.", Alert.AlertType.ERROR);
            }
        }
        toggleButtons();
    }

    @FXML
    void onClickedCancel() {
        stage.close();
    }

    @FXML
    void onClickedCreate() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String paymentMethod = paymentChoiceBox.getValue();

        Bill bill = new Bill(null, null, firstName, lastName, paymentMethod);
        List<BillEntry> billEntries = new ArrayList<BillEntry>();

        try {
            service.newBill(bill);

            List<BillEntry> entries = toAddProductTable.getItems();

            for (BillEntry entry : entries) {
                billEntries.add(new BillEntry(null, bill.getInvoiceNumber(), entry.getFkProductId(), entry.getProductName(), entry.getProductPrice(), entry.getQuantity()));
                Product p = service.getProductForId(entry.getFkProductId());
                p.setStock(p.getStock() - entry.getQuantity());
                service.editProduct(p);
            }

            service.addProductsToBill(billEntries);
            stage.close();
        } catch (ServiceException e) {
            billsController.fxDialog("Create Bill", "ERROR", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onClickedRemove() {
        selectionIsValid(toAddProductTable);
        try {
            Product toRemove = service.getProductForId(toAddProductTable.getSelectionModel().getSelectedItem().getFkProductId());
            selectProductTable.getItems().add(toRemove);
            toAddProductTable.getItems().remove(toAddProductTable.getSelectionModel().getSelectedItem());

            toggleButtons();
        } catch (ServiceException e) {
        }
    }

    private void toggleButtons() {
        if (selectProductTable.getItems().isEmpty()) {
            addButton.setDisable(true);
        } else {
            addButton.setDisable(false);
        }

        if (toAddProductTable.getItems().isEmpty()) {
            removeButton.setDisable(true);
        } else {
            removeButton.setDisable(false);
        }
    }

    private void selectionIsValid(TableView tv) {
        if (tv.getSelectionModel().getSelectedItem() == null) {
            billsController.fxDialog("Create Bill", "ERROR", "Nothing selected.", Alert.AlertType.ERROR);
        }
    }
}
