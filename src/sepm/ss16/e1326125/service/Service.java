package sepm.ss16.e1326125.service;


import sepm.ss16.e1326125.entity.Bill;
import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.entity.Product;

import java.util.HashMap;
import java.util.List;

public interface Service {

    /**
     * Creates a new product.
     * @param product The product to be created.
     * @return The new product.
     * @throws ServiceException If product is null or one of the products values is missing (NULL).
     */
    public Product newProduct(Product product) throws ServiceException;

    /**
     * Edits a product.
     * @param product The product to be modified.
     * @throws ServiceException If the product is null.
     */
    public void editProduct(Product product) throws ServiceException;

    /**
     * Lists all products.
     * @return A list of all products.
     * @throws ServiceException
     */
    public List<Product> listAllProducts() throws ServiceException;

    /**
     * Searches for products in the range of the products from and to.
     * @param from The first criteria object.
     * @param to The second criteria object.
     * @return A list with the search results.
     * @throws ServiceException
     */
    public List<Product> searchProducts(Product from, Product to) throws ServiceException;

    /**
     * Deletes a product from the database.
     * @param product The product to be deleted.
     * @throws ServiceException If the product is null or the ID is not registered in
     * the database.
     */
    public void deleteProduct(Product product) throws ServiceException;

    /**
     * Creates a new bill.
     * @param bill The bill to be created.
     * @return The new bill.
     * @throws ServiceException
     */
    public Bill newBill(Bill bill) throws ServiceException;

    /**
     * Lists all bills.
     * @return A list of all the bills.
     * @throws ServiceException
     */
    public List<Bill> listAllBills() throws ServiceException;

    /**
     * Searches for bills in the ranges of from and to.
     * @param from The first criteria object.
     * @param to The second criteria object.
     * @return A list with all the search results.
     * @throws ServiceException If one of the bills is null.
     */
    public List<Bill> searchBills(Bill from, Bill to) throws ServiceException;

    /**
     * Adds a new BillEntry to a bill.
     * @param bill The bill in which the entry should occur.
     * @param product The product to be added.
     * @param quantity The quantity of the product.
     * @return The new bill entry.
     * @throws ServiceException If one of the parameters is null.
     */
    public BillEntry addProductToBill(Bill bill, Product product, Integer quantity) throws ServiceException;

    /**
     * Calculates the statistics for all the products.
     * @param amountOfDays The amount of days to narrow the statistics.
     * @return A HashMap with the statistics.
     * @throws ServiceException
     */
    public HashMap<Integer, Integer> calculateStatisticsForAllProducts(Integer amountOfDays) throws ServiceException;

    /**
     * Calculates the statistics for one product.
     * @param productID The productID for which the statistics want to be calculated.
     * @param amountOfDays The amount of days to narrow the statistics.
     * @return A HashMap with the statistics.
     * @throws ServiceException
     */
    public HashMap<Integer, Integer> calculateStatisticsForOneProduct(Integer productID, Integer amountOfDays) throws ServiceException;

    /**
     * Alters the price of all the products in the specified range by percentage.
     * @param amountOfDays
     * @param min
     * @param max
     * @param percentage
     * @param decreasePrice
     * @throws ServiceException
     */
    public void alterPriceByPercentageWithMinMax(Integer amountOfDays, Integer min, Integer max, Double percentage, Boolean decreasePrice) throws ServiceException;

    /**
     * Alters the price of all products in the specified frequency by percentage.
     * @param amountOfDays
     * @param least
     * @param most
     * @param percentage
     * @param decreasePrice
     * @throws ServiceException
     */
    public void alterPriceByPercentageWithFrequency(Integer amountOfDays, Integer least, Integer most, Double percentage, Boolean decreasePrice) throws ServiceException;

    /**
     * Alters the price of all products in the specified range by amount.
     * @param amountOfDays
     * @param min
     * @param max
     * @param amount
     * @throws ServiceException
     */
    public void alterPriceByAmountWithMinMax(Integer amountOfDays, Integer min, Integer max, Double amount) throws ServiceException;

    /**
     * Alters the price of all products in the specified frequency be amount.
     * @param amountOfDays
     * @param least
     * @param most
     * @param amount
     * @throws ServiceException
     */
    public void alterPriceByAmountWithFrequency(Integer amountOfDays, Integer least, Integer most, Double amount) throws ServiceException;

    /**
     * Closes the service.
     * @throws ServiceException
     */
    public void close() throws ServiceException;
}
