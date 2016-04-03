package sepm.ss16.e1326125.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sepm.ss16.e1326125.dao.*;
import sepm.ss16.e1326125.dao.impl.JDBCBillDAO;
import sepm.ss16.e1326125.dao.impl.JDBCBillEntryDAO;
import sepm.ss16.e1326125.dao.impl.JDBCProductDAO;
import sepm.ss16.e1326125.entity.Bill;
import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.entity.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimpleService implements Service {

    private ProductDAO productDAO;
    private BillDAO billDAO;
    private BillEntryDAO billEntryDAO;
    private HashMap<Bill, Product> billEntries;
    private static final Logger logger = LogManager.getLogger(SimpleService.class);

    public SimpleService() throws ServiceException {

        this.billEntries = new HashMap<Bill, Product>();
        try {
            this.productDAO = new JDBCProductDAO();
            this.billDAO = new JDBCBillDAO();
            this.billEntryDAO = new JDBCBillEntryDAO();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
        logger.debug("Service started.");
    }

    @Override
    public Product newProduct(Product product) throws ServiceException {
        logger.debug("Trying to create product \n{}", product);
        try {
            return productDAO.create(product);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void editProduct(Product product) throws ServiceException {
        logger.debug("Trying to edit product \n{}", product);
        try {
            productDAO.update(product);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Product> listAllProducts() throws ServiceException {
        logger.debug("Listing all products.");
        try {
            logger.debug("Entered listAll-Method.");
            return productDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Product> searchProducts(Product from, Product to) throws ServiceException {
        logger.debug("Trying to search for product between \n{} and \n{}", from, to);
        try {
            return productDAO.search(from, to);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Product product) throws ServiceException {
        logger.debug("Trying to delete product \n{}", product);
        try {
            productDAO.delete(product);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Bill newBill(Bill bill) throws ServiceException {
        logger.debug("Trying to create bill \n{}", bill);
        try {
            return billDAO.create(bill);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Bill> listAllBills() throws ServiceException {
        logger.debug("Listing all bills.");
        try {
            return billDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Bill> searchBills(Bill from, Bill to) throws ServiceException {
        logger.debug("Trying to search for bills between \n{} and \n{}", from, to);
        try {
            return billDAO.search(from, to);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addProductsToBill(List<BillEntry> entries) throws ServiceException {
        logger.debug("Trying to add products to bill.");
        try {
            billEntryDAO.create(entries);
            logger.debug("Successfully added products to bill.");
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, Integer> calculateStatisticsForAllProducts(Integer amountOfDays) throws ServiceException {
        logger.debug("Calculating statistics for all products in the last {} days.", amountOfDays);
        try {
            return billEntryDAO.calculateStatistics(amountOfDays);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, Integer> calculateStatisticsForOneProduct(Integer productID, Integer amountOfDays) throws ServiceException {
        logger.debug("Calculating statistics for product with ID {} in the last {} days.", productID, amountOfDays);
        try {
            return billEntryDAO.calculateStatisticsForProduct(productID, amountOfDays);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void alterPriceByAmount(Integer amountOfDays, Double amount, Boolean decreasePrice, Integer limit, LimitType limitType) throws ServiceException {
        logger.debug("Entering alterPriceByAmount-Method.");
        List<Product> products = null;
        try {
            products = billEntryDAO.filterProductsForAlteration(amountOfDays, limit, limitType);
            productDAO.alterPriceByAmount(products, amount, decreasePrice);

        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void alterPriceByPercentage(Integer amountOfDays, Double percentage, Boolean decreasePrice, Integer limit, LimitType limitType) throws ServiceException {
        logger.debug("Entering alterPriceByPercentage-Method.");
        List<Product> products = null;
        try {
            products = billEntryDAO.filterProductsForAlteration(amountOfDays, limit, limitType);
            productDAO.alterPriceByPercentage(products, percentage, decreasePrice);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Integer getAlterPriceSize(Integer amountOfDays, Integer limit, LimitType limitType) throws ServiceException {
        logger.debug("Entering getAlterPriceSize-Method.");
        try {
            if (limitType == LimitType.LEAST || limitType == LimitType.MOST) {
                if (limit > productDAO.findAll().size()) {
                    throw new DAOException("Limit can't be greater than the total amount of products.");
                } else {
                    return billEntryDAO.filterProductsForAlteration(amountOfDays, limit, limitType).size();
                }
            } else {
                return billEntryDAO.filterProductsForAlteration(amountOfDays, limit, limitType).size();
            }

        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void close() throws ServiceException {
        try {
            productDAO.close();
            billDAO.close();
            billEntryDAO.close();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public HashMap<String, Integer> getNames() throws ServiceException {
        logger.debug("Entering getNames-Method.");
        try {
            return productDAO.getNames();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String getNameForProduct(Integer productID) throws ServiceException {
        logger.debug("Entering getNameForProduct-Method with productID: {}", productID);
        try {
            return productDAO.getNameForProduct(productID);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<BillEntry> getEntriesForBill(Integer billNumber) throws ServiceException {
        logger.debug("Entering getEntriesForBill-Method with billNumber: {}", billNumber);
        try {
            return billEntryDAO.filterByBill(billNumber);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Product getProductForId(Integer productId) throws ServiceException {
        logger.debug("Entering getProductForId-Method with ID: {}", productId);
        Product result = null;
        try {
            List<Product> list = productDAO.findAll();
            for (Product p : list) {
                if (p.getProductId() == productId) {
                    return p;
                }
            }
        } catch (DAOException e) {
            throw new ServiceException("No such Id: " + productId);
        }
        return null;
    }
}
