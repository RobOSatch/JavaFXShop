package sepm.ss16.e1326125.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.command.Prepared;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.JDBCSingletonConnection;
import sepm.ss16.e1326125.dao.ProductDAO;
import sepm.ss16.e1326125.entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class JDBCProductDAO implements ProductDAO {

    private static final Logger logger = LogManager.getLogger(JDBCProductDAO.class);
    private Connection connection = null;

    public JDBCProductDAO() throws DAOException {
        connection = JDBCSingletonConnection.getConnection();
    }

    public JDBCProductDAO(Connection connection) throws DAOException {
        this.connection = connection;
    }

    @Override
    public Product create(Product product) throws DAOException {
        logger.debug("Entering create-Method with parameters:\n{}", product);

        checkIfProductIsNull(product);

        connection = JDBCSingletonConnection.reconnectIfConnectionLost();

        try {
            PreparedStatement createStatement = connection.prepareStatement("INSERT INTO Product (name, price, stock, image) VALUES (?," + product.getPrice() + "," + product.getStock() + ",?);", Statement.RETURN_GENERATED_KEYS);
            createStatement.setString(1, product.getName());
            createStatement.setString(2, product.getImage());
            createStatement.executeUpdate();
            ResultSet rs = createStatement.getGeneratedKeys();
            rs.next();
            product.setProductId(rs.getInt(1));
            product.setDeleted(false);
            update(product);
            logger.debug("Successfully inserted row into the table Product:\n{}", product);
        } catch (SQLException ex) {
            logger.debug(ex.getMessage());
            throw new DAOException(ex.getMessage());
        }

        return product;
    }

    @Override
    public List<Product> findAll() throws DAOException {
        logger.debug("Entering findAll-Method");

        connection = JDBCSingletonConnection.reconnectIfConnectionLost();

        ArrayList<Product> result = new ArrayList<Product>();

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Product WHERE is_Deleted = false;");
            while (rs.next()) {
                result.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4), rs.getString(5), rs.getBoolean(6)));
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
    public List<Product> search(Product from, Product to) throws DAOException {
        logger.debug("Entering search-Method with parameters:\n{} and \n{}", from, to);

        checkIfProductIsNull(from);
        checkIfProductIsNull(to);

        connection = JDBCSingletonConnection.reconnectIfConnectionLost();

        ArrayList<Product> result = new ArrayList<Product>();

        try {
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM Product WHERE is_Deleted = false AND Price BETWEEN " + from.getPrice() + " AND " + to.getPrice() + " AND Stock BETWEEN " + from.getStock() + " AND " + to.getStock() + ";");
            while (rs.next()) {
                result.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getInt(4), rs.getString(5), rs.getBoolean(6)));
                logger.debug("Entry added to filtered list.");
            }
        } catch (SQLException ex) {
            logger.debug(ex.getMessage());
            throw new DAOException(ex.getMessage());
        }

        logger.debug("Successfully returned list of filtered entries.");
        return result;
    }

    @Override
    public void update(Product product) throws DAOException {
        logger.debug("Entering update-Method with parameters:\n{}", product);

        checkIfProductIsNull(product);

        connection = JDBCSingletonConnection.reconnectIfConnectionLost();

        try {
            PreparedStatement updateStatement = connection.prepareStatement("UPDATE Product SET name=?, price=" + product.getPrice() + ", stock=" + product.getStock() + ", image=?, is_Deleted=" + product.getDeleted() + " WHERE productID=" + product.getProductId() + ";");
            ResultSet rs = connection.createStatement().executeQuery("SELECT* FROM Product WHERE productId=" + product.getProductId() + ";");
            if (!rs.next()) {
                logger.debug("Product with id {} doesn't exist.", product.getProductId());
                throw new DAOException("Product with id " + product.getProductId() + " doesn't exist.");
            }

            updateStatement.setString(1, product.getName());
            updateStatement.setString(2, product.getImage());
            updateStatement.executeUpdate();
            connection.commit();
            logger.debug("Successfully updated product in the table Product:\n{}", product);
        } catch (SQLException ex) {
            logger.debug(ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.debug(e.getMessage());
                throw new DAOException((e.getMessage()));
            }
            throw new DAOException(ex.getMessage());
        }
    }

    @Override
    public void delete(Product product) throws DAOException {
        logger.debug("Entering delete-Method with parameters:\n{}", product);
        checkIfProductIsNull(product);
        product.setDeleted(true);
        update(product);
    }

    private void checkIfProductIsNull(Product product) throws DAOException {
        if (product == null){
            logger.error("Product is null.");
            throw new DAOException("Product can't be null.");
        }
    }
}
