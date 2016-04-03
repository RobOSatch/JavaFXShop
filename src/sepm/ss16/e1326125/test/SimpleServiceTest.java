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
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;
import sepm.ss16.e1326125.service.SimpleService;

import javax.sql.DataSource;
import java.sql.SQLException;

public class SimpleServiceTest extends AbstractServiceTest {
    private SimpleService service;

    @Before
    public void setUp() throws ServiceException {
        this.service = new SimpleService();
        this.setService(service);
        Product invalidProduct = new Product(1, "Test Object Invalid", -12.00, 12, "testobject.png", false);
        this.setServiceEntities(invalidProduct);
    }
}
