package sepm.ss16.e1326125.test;

import org.junit.Test;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.ProductDAO;
import sepm.ss16.e1326125.entity.Product;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public abstract class AbstractProductDAOTest {

    protected ProductDAO productDAO;
    private Product validProduct;
    private Product productWithNegativePrice;
    private Product productWithNonExistingId;

    protected void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    protected void setProducts(Product validProduct, Product productWithNegativePrice, Product productWithNonExistingId) {
        this.validProduct = validProduct;
        this.productWithNegativePrice = productWithNegativePrice;
        this.productWithNonExistingId = productWithNonExistingId;
    }

    /**
     * This test creates a new product with correct values and
     * saves it to the database.
     */
    @Test
    public void createWithValidParametersShouldPersist() throws DAOException {
        List<Product> products = productDAO.findAll();
        assertFalse(products.contains(validProduct));
        Product p = productDAO.create(validProduct);
        products = productDAO.findAll();
        assertTrue(products.contains(p));
        productDAO.delete(p);
    }

    /**
     * This test tries to create a new product with an invalid value (NULL).
     * The DAO should throw an exception.
     */
    @Test (expected = DAOException.class)
    public void createWithNullShouldThrowException() throws DAOException {
        productDAO.create(null);
    }

    /**
     * This test tries to create a new product with an negative price.
     * The DAO should throw an exception.
     */
    @Test (expected = DAOException.class)
    public void createWithNegativePriceShouldThrowException() throws DAOException {
        productDAO.create(this.productWithNegativePrice);
    }

    /**
     * This test searches for the products between the specified limits for price
     * and stock and tests if the number of result entries is correct.
     */
    @Test
    public void searchWithValidParametersShouldReturnRightResult() throws DAOException {
        List<Product> products = productDAO.search(new Product(null, null, 20.00, 10, null, null), new Product(null, null, 70.00, 35, null, null));
        assertEquals(products.size(), 3);
    }

    /**
     * This test tries to perform a search with one of the parameters being NULL, which
     * should throw a DAOException.
     */
    @Test (expected = DAOException.class)
    public void searchWithOneParameterBeingNullShouldThrowException() throws DAOException {
        productDAO.search(validProduct, null);
    }

    /**
     * This test checks the correct implementation of the findAll-Method.
     */
    @Test
    public void findAllShouldReturnAllEntries() throws DAOException {
        List<Product> products = productDAO.findAll();
        assertEquals(products.size(), 8);
    }

    /**
     * This test deletes a valid product, which has been created
     * before.
     */
    @Test
    public void deleteValidProductShouldDeleteSuccessfully() throws DAOException {
        Product p = productDAO.create(validProduct);
        List<Product> products = productDAO.findAll();
        assertTrue(products.contains(p));
        productDAO.delete(p);
        products = productDAO.findAll();
        assertFalse(products.contains(p));
    }

    /**
     * This test tries to delete a product, which is not part of the database, therefore
     * the DAO should throw an exception.
     */
    @Test (expected = DAOException.class)
    public void deleteWithNonExistingIdShouldThrowException() throws DAOException {
        productDAO.delete(productWithNonExistingId);
    }

    @Test (expected = DAOException.class)
    public void alterPriceToNegativeShouldThrowException() throws DAOException {
        Product p = productDAO.create(validProduct);
        productDAO.alterPriceByPercentage(p, 150.00, true);
    }

    @Test
    public void alterPriceWithPercentageShouldReturnRightResult() throws DAOException {
        Product p = productDAO.create(validProduct);
        productDAO.alterPriceByPercentage(p, 50.00, true);
        assertEquals(20.00, p.getPrice());
        productDAO.delete(p);
    }

    @Test
    public void alterPriceWithAmountShouldReturnRightResult() throws DAOException {
        Product p = productDAO.create(validProduct);
        productDAO.alterPriceByAmount(p, -10.00);
        assertEquals(30.00, p.getPrice());
        productDAO.delete(p);
    }

}
