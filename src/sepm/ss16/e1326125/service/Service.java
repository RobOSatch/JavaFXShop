package sepm.ss16.e1326125.service;


import sepm.ss16.e1326125.dao.LimitType;
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
     * @throws ServiceException If the product is not valid.
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
     * @throws ServiceException If one of the products from and to is invalid.
     */
    public List<Product> searchProducts(Product from, Product to) throws ServiceException;

    /**
     * Deletes a product from the database.
     * @param product The product to be deleted.
     * @throws ServiceException If the product is invalid.
     */
    public void deleteProduct(Product product) throws ServiceException;

    /**
     * Creates a new bill.
     * @param bill The bill to be created.
     * @return The new bill.
     * @throws ServiceException If the bill is not valid and can't be created.
     */
    public Bill newBill(Bill bill) throws ServiceException;

    /**
     * Lists all bills.
     * @return A list of all the bills.
     * @throws ServiceException If the bills can't be read from the database.
     */
    public List<Bill> listAllBills() throws ServiceException;

    /**
     * Searches for bills in the ranges of from and to.
     * @param from The first criteria object.
     * @param to The second criteria object.
     * @return A list with all the search results.
     * @throws ServiceException If one of the bills from or to is invalid.
     */
    public List<Bill> searchBills(Bill from, Bill to) throws ServiceException;

    /**
     * Adds the bill entries to a bill.
     * @throws ServiceException If the list is null.
     */
    public void addProductsToBill(List<BillEntry> entries) throws ServiceException;

    /**
     * Calculates the statistics for all the products.
     * @param amountOfDays The amount of days to narrow the statistics.
     * @return A HashMap with the statistics.
     * @throws ServiceException If amountOfDays is NULL, zero or negative.
     */
    public HashMap<Integer, Integer> calculateStatisticsForAllProducts(Integer amountOfDays) throws ServiceException;

    /**
     * Calculates the statistics for one product.
     * @param productID The productID for which the statistics want to be calculated.
     * @param amountOfDays The amount of days to narrow the statistics.
     * @return A HashMap with the statistics.
     * @throws ServiceException If the productID is not registered in the database or amountOfDays is NULL, zero or negative.
     */
    public HashMap<Integer, Integer> calculateStatisticsForOneProduct(Integer productID, Integer amountOfDays) throws ServiceException;

    /**
     * Alters the prices of all the matching products by a specified amount.
     * @param amountOfDays The amount of days to narrow down the products.
     * @param amount The amount for alteration.
     * @param decreasePrice Whether the amount is added or subtracted.
     * @param limit The limit.
     * @param limitType The limit type for alteration (MIN, MAX, LEAST, MOST).
     * @throws ServiceException If one of the parameters is invalid.
     */
    public void alterPriceByAmount(Integer amountOfDays, Double amount, Boolean decreasePrice, Integer limit, LimitType limitType) throws ServiceException;

    /**
     * Alters the prices of all the matching products by a specified percentage.
     * @param amountOfDays The amount of days to narrow down the products.
     * @param percentage The percentage for alteration.
     * @param decreasePrice Whether the amount if added or subtracted.
     * @param limit The limit.
     * @param limitType the limit type for alteration (MIN, MAX, LEAST, MOST).
     * @throws ServiceException If one of the parameters is invalid.
     */
    public void alterPriceByPercentage(Integer amountOfDays, Double percentage, Boolean decreasePrice, Integer limit, LimitType limitType) throws ServiceException;

    /**
     * Calculates the size of the product list, which prices will be altered later.
     * @param amountOfDays The amount of days to narrow the result.
     * @param limit The limit.
     * @param limitType The limit type for alteration.
     * @return The size of the resulting list.
     * @throws ServiceException If one of the parameters is invalid.
     */
    Integer getAlterPriceSize(Integer amountOfDays, Integer limit, LimitType limitType) throws ServiceException;

    /**
     * Closes the service.
     * @throws ServiceException If the service cant' be closed.
     */
    public void close() throws ServiceException;

    /**
     * Saves the products in a HashMap, where the key is the name and the ID is the value.
     * @return The resulting HashMap.
     * @throws ServiceException If the operation can't be completed.
     */
    HashMap<String, Integer> getNames() throws ServiceException;

    /**
     * Gets the name for one product.
     * @param productID The product's ID.
     * @return The products name.
     * @throws ServiceException If the ID is invalid.
     */
    String getNameForProduct(Integer productID) throws ServiceException;

    /**
     * Gets the entries for a certain invoice number.
     * @param billNumber The invoice number.
     * @return A list with all the entries.
     * @throws ServiceException If the invoice number is invalid.
     */
    List<BillEntry> getEntriesForBill(Integer billNumber) throws ServiceException;

    /**
     * Gets the product for an ID.
     * @param productId The product's ID.
     * @return The product.
     * @throws ServiceException If the ID is invalid.
     */
    Product getProductForId(Integer productId) throws ServiceException;
}
