package sepm.ss16.e1326125.test;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.JDBCSingletonConnection;
import sepm.ss16.e1326125.dao.ProductDAO;
import sepm.ss16.e1326125.dao.impl.JDBCProductDAO;
import sepm.ss16.e1326125.entity.Product;

import javax.sql.DataSource;
import java.sql.SQLException;

public class JDBCProductDAOTest extends AbstractProductDAOTest {


    @Before
    public void setUp() throws SQLException, DAOException {
        ProductDAO productDAO = new JDBCProductDAO();
        setProductDAO(productDAO);
        Product validProduct = new Product(null, "Valid Test Object", 40.00, 10, "test1.png", false);
        Product productWithNegativePrice = new Product(null, "Invalid Test Object 1", -1.00, 12, "test2.png", false);
        Product productWithNonExistingId = new Product(100, "Invalid Test Object 2", 20.00, 12, "test3.png", false);
        setProducts(validProduct, productWithNegativePrice, productWithNonExistingId);
        JDBCSingletonConnection.getConnection().setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException, DAOException {
        JDBCSingletonConnection.getConnection().rollback();
    }
}
