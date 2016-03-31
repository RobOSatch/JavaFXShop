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
    private ArrayList<BillEntry> billEntries;
    private static final Logger logger = LogManager.getLogger(SimpleService.class);

    public SimpleService() throws ServiceException {

        this.billEntries = new ArrayList<BillEntry>();
        try {
            this.productDAO = new JDBCProductDAO();
            this.billDAO = new JDBCBillDAO();
            this.billEntryDAO = new JDBCBillEntryDAO();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        logger.debug("Service started.");
    }

    @Override
    public Product newProduct(Product product) throws ServiceException {
        try {
            return productDAO.create(product);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void editProduct(Product product) throws ServiceException {
        try {
            productDAO.update(product);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Product> listAllProducts() throws ServiceException {
        try {
            logger.debug("Entered listAll-Method.");
            return productDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Product> searchProducts(Product from, Product to) throws ServiceException {
        try {
            return productDAO.search(from, to);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Product product) throws ServiceException {
        try {
            productDAO.delete(product);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Bill newBill(Bill bill) throws ServiceException {
        try {
            return billDAO.create(bill);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Bill> listAllBills() throws ServiceException {
        try {
            return billDAO.findAll();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Bill> searchBills(Bill from, Bill to) throws ServiceException {
        try {
            return billDAO.search(from, to);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public BillEntry addProductToBill(Bill bill, Product product, Integer quantity) throws ServiceException {
        return null;
    }

    @Override
    public HashMap<Integer, Integer> calculateStatisticsForAllProducts(Integer amountOfDays) throws ServiceException {
        try {
            return billEntryDAO.calculateStatistics(amountOfDays);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public HashMap<Integer, Integer> calculateStatisticsForOneProduct(Integer productID, Integer amountOfDays) throws ServiceException {
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
        try {
            return billEntryDAO.filterProductsForAlteration(amountOfDays, limit, limitType).size();
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
        try {
            return productDAO.getNames();
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public String getNameForProduct(Integer productID) throws ServiceException {
        try {
            return productDAO.getNameForProduct(productID);
        } catch (DAOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
