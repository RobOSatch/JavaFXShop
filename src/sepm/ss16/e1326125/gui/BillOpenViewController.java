package sepm.ss16.e1326125.gui;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.entity.Bill;
import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

import java.text.DecimalFormat;
import java.util.List;

public class BillOpenViewController {

    private static final Logger logger = LogManager.getLogger(BillOpenViewController.class);
    private Service service;
    private Stage stage;
    private BillViewController billsController;
    private Bill bill;
    private Double total;

    @FXML
    private TableView<BillEntry> billEntryTable;

    @FXML
    private TableColumn<BillEntry, String> productNameColumn;

    @FXML
    private TableColumn<BillEntry, String> productPriceColumn;

    @FXML
    private TableColumn<BillEntry, String> quantityColumn;

    @FXML
    private Labeled invoiceNumberLabel;

    @FXML
    private Labeled issueDateLabel;

    @FXML
    private Labeled firstNameLabel;

    @FXML
    private Labeled lastNameLabel;

    @FXML
    private Labeled totalLabel;

    public void initialize() {

        productNameColumn.setCellValueFactory(new PropertyValueFactory<BillEntry, String>("productName"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<BillEntry, String>("productPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<BillEntry, String>("quantity"));

        logger.debug("productViewController initialized.");
    }

    public void setControllerAndStage(BillViewController billsController, Stage stage) {
        this.stage = stage;
        this.billsController = billsController;
        this.service = billsController.getService();
        this.bill = billsController.getBillTable().getSelectionModel().getSelectedItem();

        this.invoiceNumberLabel.setText(String.valueOf(bill.getInvoiceNumber()));
        this.issueDateLabel.setText(String.valueOf(bill.getIssueDate()));
        this.firstNameLabel.setText(bill.getBillRecipientFirstName());
        this.lastNameLabel.setText(bill.getBillRecipientLastName());

        List<BillEntry> list = null;
        total = 0.0;

        try {
            list = service.getEntriesForBill(bill.getInvoiceNumber());
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        for (BillEntry entry : list) {
            total += (entry.getProductPrice() * entry.getQuantity());
        }

        DecimalFormat df = new DecimalFormat("0.00");

        this.totalLabel.setText(String.valueOf(df.format(total)));
        billEntryTable.setItems(FXCollections.observableArrayList(list));
        billEntryTable.setSelectionModel(null);

        stage.setTitle("Show Bill");
    }

    public Service getService(){
        return service;
    }

}
