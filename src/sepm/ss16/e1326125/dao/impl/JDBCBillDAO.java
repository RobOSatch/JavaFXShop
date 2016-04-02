package sepm.ss16.e1326125.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.dao.BillDAO;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.JDBCSingletonConnection;
import sepm.ss16.e1326125.entity.Bill;
import sepm.ss16.e1326125.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JDBCBillDAO implements BillDAO {

    public static final Logger logger = LogManager.getLogger(JDBCBillDAO.class);
    private Connection connection = null;

    public JDBCBillDAO() throws DAOException {
        connection = JDBCSingletonConnection.getConnection();
        logger.debug("BillDAO created successfully.");
    }

    @Override
    public Bill create(Bill bill) throws DAOException {
        logger.debug("Entering create-Method with parameters:\n{}", bill);

        checkIfBillIsNull(bill);

        try {
            PreparedStatement createStatement = connection.prepareStatement("INSERT INTO Bill (BillRecipientFirstName, BillRecipientLastName, PaymentMethod) VALUES (?,?,?);", Statement.RETURN_GENERATED_KEYS);
            createStatement.setString(1, bill.getBillRecipientFirstName());
            createStatement.setString(2, bill.getBillRecipientLastName());
            createStatement.setString(3, bill.getPaymentMethod());
            createStatement.executeUpdate();
            ResultSet rs = createStatement.getGeneratedKeys();
            rs.next();
            bill.setInvoiceNumber(rs.getInt(1));
            connection.commit();
            logger.debug("Successfully inserted row into the table Bill:\n{}", bill);
        } catch (SQLException ex) {
            logger.debug(ex.getMessage());
            throw new DAOException(ex.getMessage());
        }

        return bill;
    }

    @Override
    public List<Bill> findAll() throws DAOException {
        logger.debug("Entering findAll-Method");

        ArrayList<Bill> result = new ArrayList<Bill>();

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Bill;");
            while (rs.next()) {
                result.add(new Bill(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                logger.debug("Entry added to list.");
            }
        } catch (SQLException ex) {
            logger.debug(ex.getMessage());
            throw new DAOException(ex.getMessage());
        }

        logger.debug("Successfully returned list of all entries.");
        return result;
    }

    @Override
    public List<Bill> search(Bill from, Bill to) throws DAOException {
        logger.debug("Entering search-Method with parameters:\n{} and \n{}", from, to);

        checkIfBillIsNull(from);
        checkIfBillIsNull(to);

        ArrayList<Bill> result = new ArrayList<Bill>();

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Bill WHERE IssueDate BETWEEN '" + from.getIssueDate() + "' AND '" + to.getIssueDate() + "';");
            while (rs.next()) {
                result.add(new Bill(rs.getInt(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                logger.debug("Entry added to list.");
            }
        } catch (SQLException ex) {
            logger.debug(ex.getMessage());
            throw new DAOException(ex.getMessage());
        }

        logger.debug("Successfully returned list of filtered entries.");
        return result;
    }

    @Override
    public void close() throws DAOException {
        JDBCSingletonConnection.closeConnection();
    }

    private void checkIfBillIsNull(Bill bill) throws DAOException {
        if (bill == null){
            logger.error("Bill is null.");
            throw new DAOException("Bill can't be null.");
        }
    }
}
