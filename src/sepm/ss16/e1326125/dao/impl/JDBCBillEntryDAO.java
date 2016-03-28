package sepm.ss16.e1326125.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.dao.BillEntryDAO;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.entity.BillEntry;

import java.util.List;


public class JDBCBillEntryDAO implements BillEntryDAO {

    public static final Logger logger = LogManager.getLogger(JDBCBillEntryDAO.class);

    @Override
    public BillEntry create(BillEntry billEntry) throws DAOException {
        logger.debug("Entering create-Method with parameters", billEntry);
        return null;
    }

    @Override
    public List<BillEntry> search(BillEntry billEntry) throws DAOException {
        logger.debug("Entering search-Method with parameters", billEntry);
        return null;
    }

    private void checkIfBillEntryIsNull(BillEntry billEntry) throws DAOException {
        if (billEntry == null){
            logger.error("BillEntry is null.");
            throw new DAOException("BillEntry can't be null.");
        }
    }
}
