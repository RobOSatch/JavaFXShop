package sepm.ss16.e1326125.test;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import sepm.ss16.e1326125.dao.BillEntryDAO;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.JDBCSingletonConnection;
import sepm.ss16.e1326125.dao.ProductDAO;
import sepm.ss16.e1326125.dao.impl.JDBCBillEntryDAO;
import sepm.ss16.e1326125.dao.impl.JDBCProductDAO;
import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.entity.Product;

import javax.sql.DataSource;
import java.sql.SQLException;

public class JDBCBillEntryDAOTest extends AbstractBillEntryDAOTest {


    @Before
    public void setUp() throws SQLException, DAOException {
        BillEntryDAO billEntryDAO = new JDBCBillEntryDAO();
        setBillEntryDAO(billEntryDAO);
        BillEntry testBillEntry1 = new BillEntry(null, 1, 2, "Kraftfutter 1kg", 49.99, 4);
        BillEntry testBillEntry2 = new BillEntry(null, 1, 5, "Abschwitzdecke", 129.99, 1);
        BillEntry testBillEntry3 = new BillEntry(null, 1, 4, "Sattel", 79.99, 1);
        setBillEntries(testBillEntry1, testBillEntry2, testBillEntry3);
        JDBCSingletonConnection.getConnection().setAutoCommit(false);
    }

    @After
    public void tearDown() throws SQLException, DAOException {
        JDBCSingletonConnection.getConnection().rollback();
    }
}
