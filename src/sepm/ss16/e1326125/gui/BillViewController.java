package sepm.ss16.e1326125.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.entity.Bill;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillViewController extends MainViewController {

    private static final Logger logger = LogManager.getLogger(BillViewController.class);


    private MainViewController mainViewController;

    @FXML
    private TableView<Bill> billTable;

    @FXML
    private TableColumn<Bill, String> billInvoiceNumberColumn;

    @FXML
    private TableColumn<Bill, String> billIssueDateColumn;

    @FXML
    private TableColumn<Bill, String> billFirstNameColumn;

    @FXML
    private TableColumn<Bill, String> billLastNameColumn;

    @FXML
    private TableColumn<Bill, String> billPaymentMethodColumn;

    @FXML
    private DatePicker datePickerFrom;

    @FXML
    private DatePicker datePickerTo;

    @FXML
    private Button productSearchStartButton;

    public void initialize() {
        billInvoiceNumberColumn.setCellValueFactory(new PropertyValueFactory<Bill, String>("invoiceNumber"));
        billIssueDateColumn.setCellValueFactory(new PropertyValueFactory<Bill, String>("issueDate"));
        billFirstNameColumn.setCellValueFactory(new PropertyValueFactory<Bill, String>("billRecipientFirstName"));
        billLastNameColumn.setCellValueFactory(new PropertyValueFactory<Bill, String>("billRecipientLastName"));
        billPaymentMethodColumn.setCellValueFactory(new PropertyValueFactory<Bill, String>("paymentMethod"));
        logger.debug("productViewController initialized.");
    }

    public void setAndFill(Service service) throws ServiceException {
        this.service = service;
        List<Bill> list = service.listAllBills();
        billTable.setItems(FXCollections.observableArrayList(list));
        billTable.getSelectionModel().selectFirst();
        datePickerFrom.setValue(LocalDate.now());
        datePickerTo.setValue(LocalDate.now());
    }

    public Service getService(){
        return service;
    }

    public void setPrimaryStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public TableView<Bill> getBillTable() {
        return billTable;
    }

    @FXML
    void onClickedBillStartSearch() {
        Date fromDate = Date.valueOf(datePickerFrom.getValue());
        Date toDate = Date.valueOf(datePickerTo.getValue());


        Bill from = new Bill(null, fromDate, null, null, null);
        Bill to = new Bill(null, toDate, null, null, null);

        try {
            List<Bill> list = service.searchBills(from, to);
            billTable.setItems(FXCollections.observableArrayList(list));
            billTable.getSelectionModel().selectFirst();

            if (list.isEmpty()) {
                fxDialog("Searching Bills", "", "No bills matched your search criteria.", Alert.AlertType.INFORMATION);
            } else if(list.size() == 1) {
                fxDialog("Searching Bills", "", billTable.getItems().size() + " bill matched your criteria.", Alert.AlertType.INFORMATION);
            } else {
                fxDialog("Searching Bills", "", billTable.getItems().size() + " bills matched your criteria.", Alert.AlertType.INFORMATION);
            }
        } catch (ServiceException e) {
            fxDialog("Searching Bills", "Search failed to execute", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

    @FXML
    void onClickedOpen() {
        try {
            if (billTable.getSelectionModel().getSelectedItem() == null) {
                throw new IOException();
            } else {
                openBillWindow();
            }
        } catch (IOException e) {
            fxDialog("Show Bill", "ERROR", "Bill can't be null.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onClickedListAll() {
        try {
            List<Bill> list = service.listAllBills();
            billTable.setItems(FXCollections.observableArrayList(list));
            billTable.getSelectionModel().selectFirst();
        } catch (ServiceException e) {
            fxDialog("List Bills", "Listing Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void openBillWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BillOpenView.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        BillOpenViewController controller = loader.getController();
        controller.setControllerAndStage(this, stage);
        stage.showAndWait();
    }

    public void openCreateWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BillCreateView.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        stage.setScene(scene);
        BillCreateViewController controller = loader.getController();
        controller.setControllerAndStage(this, stage);
        stage.showAndWait();
    }
}
