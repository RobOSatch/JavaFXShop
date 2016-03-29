package sepm.ss16.e1326125.dao;

import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.entity.Product;

import java.util.HashMap;
import java.util.List;


public interface BillEntryDAO {

    /**
     * Creates a new bill entry.
     * @param billEntries Contains the bill entries to be added.
     * @throws DAOException If the bill entries could not be created.
     */
    public void create(List<BillEntry> billEntries) throws DAOException;

    /**
     * Returns a list of bill entries filtered by the bill's ID.
     * @param fkInvoiceNumber Contains the filter value.
     * @return The search result.
     * @throws DAOException If the search could not be performed.
     */
    public List<BillEntry> filterByBill(Integer fkInvoiceNumber) throws DAOException;

    /**
     * Returns a HashMap with the total amount of times all products have been sold in the last
     * x days. x can be specified by the user.
     */
    public HashMap<Integer, Integer> calculateStatistics(Integer amountOfDays) throws DAOException;

    /**
     * Returns a HashMap with the total amount of times a specific product has been sold in the last
     * x days. x can be specified by the user.
     */
    public HashMap<Integer, Integer> calculateStatisticsForProduct(Integer productID, Integer amountOfDays) throws DAOException;

    /**
     * Closes the database connection.
     */
    public void close() throws DAOException;
}
