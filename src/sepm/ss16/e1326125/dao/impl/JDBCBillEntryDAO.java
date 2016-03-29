package sepm.ss16.e1326125.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.dao.BillEntryDAO;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.JDBCSingletonConnection;
import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JDBCBillEntryDAO implements BillEntryDAO {

    public static final Logger logger = LogManager.getLogger(JDBCBillEntryDAO.class);
    private Connection connection = null;

    public JDBCBillEntryDAO() throws DAOException {
        this.connection = JDBCSingletonConnection.getConnection();
        logger.debug("BillEntryDAO created successfully.");
    }

    @Override
    public void create(List<BillEntry> billEntries) throws DAOException {
        logger.debug("Entering create-Method with parameters", billEntries);

        if (billEntries == null) {
            logger.debug("List is null.");
            throw new DAOException("List can't be null.");
        }

        try {

            for (BillEntry bE : billEntries) {
                checkIfBillEntryIsNull(bE);

                ResultSet rs = connection.createStatement().executeQuery("SELECT count(*) FROM BillEntry WHERE (fkInvoiceNumber = " + bE.getFkInvoiceNumber() + " AND fkProductID = " + bE.getFkProductId() + ");");
                rs.next();
                if(rs.getInt(1)>0){
                    logger.debug("BillEntry with this ProductID on this Bill already exists.");
                    throw new DAOException("The same product can't occur more than once on the same bill.");
                }

                PreparedStatement createStatement = connection.prepareStatement("INSERT INTO BillEntry (fkInvoiceNumber, fkProductID, productName, productPrice, Quantity) VALUES ( " + bE.getFkInvoiceNumber() + "," + bE.getFkProductId() + ", ? ," + bE.getProductPrice() + "," + bE.getQuantity() + ");", Statement.RETURN_GENERATED_KEYS);
                createStatement.setString(1, bE.getProductName());
                createStatement.executeUpdate();
                ResultSet rs1 = createStatement.getGeneratedKeys();
                rs1.next();
                bE.setId(rs1.getInt(1));
                }

                logger.debug("Successfully inserted row into table BillEntry.");
                connection.commit();
        } catch (SQLException e) {
            logger.debug(e.getMessage());

            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.debug(ex.getMessage());
                throw new DAOException(ex.getMessage());
            }

            throw new DAOException(e.getMessage());
        }
    }

    @Override
    public List<BillEntry> filterByBill(Integer fkInvoiceNumber) throws DAOException {
        logger.debug("Entering filter-Method with parameters", fkInvoiceNumber);

        ArrayList<BillEntry> result = new ArrayList<BillEntry>();
        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM BillEntry WHERE fkInvoiceNumber = " + fkInvoiceNumber + ";");
            while (rs.next()) {
                result.add(new BillEntry(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getInt(6)));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }

        logger.debug("Successfully returned list of filtered entries.");
        return result;
    }

    @Override
    public HashMap<Integer, Integer> calculateStatistics(Integer amountOfDays) throws DAOException {

        HashMap<Integer, Integer> stats = new HashMap<Integer, Integer>();

        ResultSet rs = null;

        try {
            rs = connection.createStatement().executeQuery("SELECT billEntry.fkProductID, (SUM(billEntry.quantity)) AS sold_since_date FROM billEntry JOIN bill on billEntry.fkInvoiceNumber = bill.invoiceNumber where bill.issueDate > getDate() - " + amountOfDays + " group by billEntry.fkProductID;");

            while(rs.next()) {
                stats.put(rs.getInt(1), rs.getInt(2));
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return stats;
    }

    @Override
    public HashMap<Integer, Integer> calculateStatisticsForProduct(Integer productID, Integer amountOfDays) throws DAOException {
        HashMap<Integer, Integer> stats = calculateStatistics(amountOfDays);
        HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();

        for (Map.Entry<Integer, Integer> entry : stats.entrySet()) {
            if (entry.getKey() == productID) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public void close() throws DAOException {
        JDBCSingletonConnection.closeConnection();
    }

    private void checkIfBillEntryIsNull(BillEntry billEntry) throws DAOException {
        if (billEntry == null){
            logger.error("BillEntry is null.");
            throw new DAOException("BillEntry can't be null.");
        }
    }
}
