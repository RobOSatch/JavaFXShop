package sepm.ss16.e1326125.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.dao.LimitType;
import sepm.ss16.e1326125.dao.BillEntryDAO;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.JDBCSingletonConnection;
import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.entity.Product;

import java.sql.*;
import java.util.*;


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

        LinkedHashMap<Integer, Integer> stats = new LinkedHashMap<Integer, Integer>();

        ResultSet rs = null;
        ResultSet rs1 = null;

        try {
            rs = connection.createStatement().executeQuery("SELECT billEntry.fkProductID, (SUM(billEntry.quantity)) AS sold_since_date FROM billEntry JOIN bill on billEntry.fkInvoiceNumber = bill.invoiceNumber where bill.issueDate > getDate() - " + amountOfDays + " group by billEntry.fkProductID order by sold_since_date desc;");

            while(rs.next()) {
                stats.put(rs.getInt(1), rs.getInt(2));
            }

            String query = "SELECT productID FROM PRODUCT WHERE is_Deleted=false AND (productID <> ";
            for (Map.Entry<Integer, Integer> entry : stats.entrySet()) {
                query += entry.getKey() + " AND productID <> ";
            }
            query += " 0);";
            rs1 = connection.createStatement().executeQuery(query);
            while (rs1.next()) {
                stats.put(rs1.getInt(1), 0);
            }
        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
        return stats;
    }

    @Override
    public List<Product> filterProductsForAlteration(Integer amountOfDays, Integer limit, LimitType limitType) throws DAOException {
        logger.debug("Enter adjustment method!");
        HashMap<Integer, Integer> stats = calculateStatistics(amountOfDays);
        System.out.println(stats.toString());
        ArrayList<Product> result = new ArrayList<Product>();
        ResultSet rs = null;

        try {
            if (limitType == LimitType.MINIMUM) {
                for (Map.Entry<Integer, Integer> entry : stats.entrySet()) {
                    if (entry.getValue() >= limit) {
                        rs = connection.createStatement().executeQuery("SELECT * FROM PRODUCT WHERE productID =" + entry.getKey() + ";");
                        while(rs.next()) {
                            result.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4), rs.getString(5), rs.getBoolean(6)));
                        }
                    }
                }
            }

            if (limitType == LimitType.MAXIMUM) {
                for (Map.Entry<Integer, Integer> entry : stats.entrySet()) {
                    if (entry.getValue() <= limit) {
                        rs = connection.createStatement().executeQuery("SELECT * FROM PRODUCT WHERE productID =" + entry.getKey() + ";");
                        while(rs.next()) {
                            result.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4), rs.getString(5), rs.getBoolean(6)));
                        }
                    }
                }
            }

            ArrayList<Integer> keyList = new ArrayList<Integer>();
            for (Map.Entry<Integer, Integer> entry : stats.entrySet()) {
                keyList.add(entry.getKey());
            }

            if (limitType == LimitType.LEAST) {
                Collections.reverse(keyList);

                if (limit <= keyList.size()) {
                    for (int i = 0; i < limit; i++) {
                        rs = connection.createStatement().executeQuery("SELECT * FROM PRODUCT WHERE productID =" + keyList.get(i));
                        while (rs.next()) {
                            result.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4), rs.getString(5), rs.getBoolean(6)));
                        }
                    }
                }
            }

            if (limitType == LimitType.MOST) {
                if (limit <= keyList.size()) {
                    for (int i = 0; i < limit; i++) {
                        rs = connection.createStatement().executeQuery("SELECT * FROM PRODUCT WHERE productID =" + keyList.get(i));
                        while (rs.next()) {
                            result.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4), rs.getString(5), rs.getBoolean(6)));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Couldn't create filtered list.");
        }

        return result;
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
