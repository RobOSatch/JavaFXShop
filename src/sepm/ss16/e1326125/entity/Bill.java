package sepm.ss16.e1326125.entity;


import java.sql.Date;

public class Bill {

    private Integer invoiceNumber;
    private Date issueDate;
    private String billRecipientFirstName;
    private String billRecipientLastName;
    private String paymentMethod;

    public Bill(Integer invoiceNumber, Date issueDate, String billRecipientFirstName, String billRecipientLastName, String paymentMethod) {
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.billRecipientFirstName = billRecipientFirstName;
        this.billRecipientLastName = billRecipientLastName;
        this.paymentMethod = paymentMethod;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getBillRecipientFirstName() {
        return billRecipientFirstName;
    }

    public void setBillRecipientFirstName(String billRecipientFirstName) {
        this.billRecipientFirstName = billRecipientFirstName;
    }

    public String getBillRecipientLastName() {
        return billRecipientLastName;
    }

    public void setBillRecipientLastName(String billRecipientLastName) {
        this.billRecipientLastName = billRecipientLastName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bill bill = (Bill) o;

        return invoiceNumber != null ? invoiceNumber.equals(bill.invoiceNumber) : bill.invoiceNumber == null;

    }

    @Override
    public int hashCode() {
        return invoiceNumber != null ? invoiceNumber.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "invoiceNumber=" + invoiceNumber +
                ", issueDate=" + issueDate +
                ", billRecipientFirstName='" + billRecipientFirstName + '\'' +
                ", billRecipientLastName='" + billRecipientLastName + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }
}
