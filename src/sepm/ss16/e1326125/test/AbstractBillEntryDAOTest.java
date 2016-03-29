package sepm.ss16.e1326125.test;


import org.junit.Test;
import sepm.ss16.e1326125.dao.BillEntryDAO;
import sepm.ss16.e1326125.dao.DAOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import sepm.ss16.e1326125.dao.DAOException;
import sepm.ss16.e1326125.dao.ProductDAO;
import sepm.ss16.e1326125.entity.BillEntry;
import sepm.ss16.e1326125.entity.Product;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public abstract class AbstractBillEntryDAOTest {

    protected BillEntryDAO billEntryDAO;
    private BillEntry testBillEntry1;
    private BillEntry testBillEntry2;
    private BillEntry testBillEntry3;

    protected void setBillEntryDAO(BillEntryDAO billEntryDAO) {
        this.billEntryDAO = billEntryDAO;
    }

    protected void setBillEntries(BillEntry testBillEntry1, BillEntry testBillEntry2, BillEntry testBillEntry3) {
        this.testBillEntry1 = testBillEntry1;
        this.testBillEntry2 = testBillEntry2;
        this.testBillEntry3 = testBillEntry3;
    }

    @Test (expected = DAOException.class)
    public void createWithBillEntryNullShouldThrowException() throws DAOException {
        ArrayList<BillEntry> entries = new ArrayList<BillEntry>();
        entries.add(testBillEntry1);
        entries.add(null);
        entries.add(testBillEntry2);
        entries.add(testBillEntry3);
        billEntryDAO.create(entries);
    }

    @Test (expected = DAOException.class)
    public void createWithListNullShouldthrowException() throws DAOException {
        billEntryDAO.create(null);
    }

    @Test (expected = DAOException.class)
    public void createWithSameProductOnOneBillShouldThrowException() throws DAOException {
        ArrayList<BillEntry> entries = new ArrayList<BillEntry>();
        entries.add(testBillEntry1);
        testBillEntry2.setFkProductId(2);
        entries.add(testBillEntry2);
        entries.add(testBillEntry3);
        billEntryDAO.create(entries);
    }

    @Test
    public void createWithValidParametersShouldPersist() throws DAOException {
        ArrayList<BillEntry> entries = new ArrayList<BillEntry>();
        entries.add(testBillEntry1);
        entries.add(testBillEntry2);
        entries.add(testBillEntry3);
        billEntryDAO.create(entries);
        List<BillEntry> result = billEntryDAO.filterByBill(1);
        assertEquals(result.size(), 6);
        assertTrue(result.contains(testBillEntry1));
    }

    @Test
    public void filterWithInvoiceNumberShouldReturnRightAmountOfResults() throws DAOException {
        List<BillEntry> result = billEntryDAO.filterByBill(1);
        assertEquals(result.size(), 3);
    }

    @Test
    public void statisticsShouldReturnRightSize() throws DAOException {
        HashMap<Integer, Integer> stats = billEntryDAO.calculateStatistics(100);
        assertTrue(stats.containsValue(4));
        assertTrue(stats.containsValue(10));
    }

    @Test
    public void statisticsForProductShouldReturnRightAmount() throws DAOException {
        HashMap<Integer, Integer> stats = billEntryDAO.calculateStatisticsForProduct(5, 100);
        assertTrue(stats.containsValue(10));
    }
}
