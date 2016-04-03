package sepm.ss16.e1326125.gui;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsViewController extends MainViewController {

    HashMap<String, Integer> products;
    ObservableList<Integer> IDs;
    ObservableList<String> names;
    Integer amountOfDays;
    Boolean changeView = false;

    @FXML
    private ChoiceBox productChoiceBox;

    @FXML
    private BarChart productBarChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TextField amountOfDaysTextField;

    public void initialize() {

    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setChoices() {
        try {
            products = service.getNames();
            IDs = FXCollections.observableArrayList();
            names = FXCollections.observableArrayList();

            for (Map.Entry<String, Integer> entry : products.entrySet()) {
                names.add(entry.getKey());
                IDs.add(entry.getValue());
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        productChoiceBox.setItems(names);
        productChoiceBox.getItems().add("All Products");
        productChoiceBox.setValue("All Products");
    }


    @FXML
    void onClickedCalculate() {
        if (!amountOfDaysTextField.getText().isEmpty() && amountOfDaysTextField.getText().matches(INT_REGEX)) {
            amountOfDays = Integer.valueOf(amountOfDaysTextField.getText());
        } else {
            fxDialog("Statistics", "Invalid Input", "Only possible input is integer.", Alert.AlertType.ERROR);
        }

        Integer productID = products.get(productChoiceBox.getValue());
        HashMap<Integer, Integer> stats = new HashMap<Integer, Integer>();
        Integer max = 0;


        XYChart.Series series = new XYChart.Series();

        try {
            if (productChoiceBox.getValue().equals("All Products")) {
                productBarChart.getData().clear();
                changeView = true;
                stats = service.calculateStatisticsForAllProducts(amountOfDays);
                String label = "";

                for (Map.Entry<Integer, Integer> entry : stats.entrySet()) {
                    if(entry.getValue() > max){
                        max = entry.getValue();
                    }
                    series.getData().add(new XYChart.Data(service.getNameForProduct(entry.getKey()) + "", entry.getValue()));
                }
                series.setName("All Products in the last " + amountOfDays + " days");
            } else {
                if (changeView) {
                    productBarChart.getData().clear();
                    changeView = false;
                }
                stats = service.calculateStatisticsForOneProduct(productID, amountOfDays);

                for (Map.Entry<Integer, Integer> entry : stats.entrySet()) {
                    if(entry.getValue() > max){
                        max = entry.getValue();
                    }
                    series.getData().add(new XYChart.Data(entry.getKey() + "", entry.getValue()));
                }
                series.setName(service.getNameForProduct(productID) + " in the last " + amountOfDays + " days");
            }

            yAxis.setUpperBound(max + 1);
            productBarChart.setCategoryGap(0.5);
            xAxis.getCategories().clear();
            productBarChart.getData().addAll(series);
        } catch (ServiceException e) {
            fxDialog("Statistics", "ERROR", e.getMessage(), Alert.AlertType.ERROR);
        }

    }

}
