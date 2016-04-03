package sepm.ss16.e1326125.test;

import org.junit.Test;
import sepm.ss16.e1326125.entity.Product;
import sepm.ss16.e1326125.service.Service;
import sepm.ss16.e1326125.service.ServiceException;

public class AbstractServiceTest {
    private Service service;
    private Product invalidProduct;

    protected void setService(Service service){
        this.service = service;
    }

    protected void setServiceEntities(Product invalidProduct) {
        this.invalidProduct = invalidProduct;
    }

    @Test (expected = ServiceException.class)
    public void createInvalidProductShouldThrowException() throws ServiceException {
        service.newProduct(invalidProduct);
    }

    @Test (expected = ServiceException.class)
    public void createWithNullShouldThrowException() throws ServiceException {
        service.newProduct(null);
    }

    @Test (expected = ServiceException.class)
    public void calculateStatisticsForNonExistingIdShouldThrowException() throws ServiceException {
        service.calculateStatisticsForOneProduct(-1, 10);
    }
}
