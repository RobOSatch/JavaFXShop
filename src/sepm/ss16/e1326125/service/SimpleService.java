package sepm.ss16.e1326125.service;


import sepm.ss16.e1326125.dao.BillDAO;
import sepm.ss16.e1326125.dao.BillEntryDAO;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.ProductDAO;
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

    public SimpleService() throws ServiceException {
        try {
            this.productDAO = new JDBCProductDAO();
            this.billDAO = new JDBCBillDAO();
            this.billEntryDAO = new JDBCBillEntryDAO();
        } catch (DAOException e) {
            e.printStackTrace();
        }
        this.billEntries = new ArrayList<BillEntry>();
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
        return calculateStatisticsForOneProduct(productID, amountOfDays);
    }

    @Override
    public void alterPriceByPercentageWithMinMax(Integer amountOfDays, Integer min, Integer max, Double percentage, Boolean decreasePrice) throws ServiceException {

    }

    @Override
    public void alterPriceByPercentageWithFrequency(Integer amountOfDays, Integer least, Integer most, Double percentage, Boolean decreasePrice) throws ServiceException {

    }

    @Override
    public void alterPriceByAmountWithMinMax(Integer amountOfDays, Integer min, Integer max, Double amount) throws ServiceException {

    }

    @Override
    public void alterPriceByAmountWithFrequency(Integer amountOfDays, Integer least, Integer most, Double amount) throws ServiceException {

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
}
