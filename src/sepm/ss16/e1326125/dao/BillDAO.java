package sepm.ss16.e1326125.dao;

import sepm.ss16.e1326125.entity.Bill;

import java.util.List;


public interface BillDAO {

    /**
     * Creates a new bill.
     * @param bill Contains the values for the new bill.
     * @return A new bill.
     * @throws DAOException If the bill could not be created.
     */
    public Bill create(Bill bill) throws DAOException;

    /**
     * Returns a list of all bills.
     * @return All bills.
     */
    public List<Bill> findAll() throws DAOException;

    /**
     * Returns a list of bills for which the issue date is between the ones in the objects
     * from and to.
     * @param from The first criteria object.
     * @param to The second criteria object.
     * @return The search result.
     * @throws DAOException If the search could not be performed.
     */
    public List<Bill> search(Bill from, Bill to) throws DAOException;

    /**
     * Closes the database connection.
     */
    public void close() throws DAOException;
}
