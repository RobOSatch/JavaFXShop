package sepm.ss16.e1326125.dao;

import sepm.ss16.e1326125.entity.BillEntry;

import java.util.List;


public interface BillEntryDAO {

    /**
     * Creates a new bill entry.
     * @param billEntry Contains the values for the new bill entry.
     * @return A new bill entry.
     * @throws DAOException If the bill entry could not be created.
     */
    public BillEntry create(BillEntry billEntry) throws DAOException;

    /**
     * Returns a list of bill entries filtered by the product's, bill's and bill entry's IDs.
     * @param billEntry Contains the filter values.
     * @return The search result.
     * @throws DAOException If the search could not be performed.
     */
    public List<BillEntry> search(BillEntry billEntry) throws DAOException;
}
