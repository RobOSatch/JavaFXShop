package sepm.ss16.e1326125.entity;


public class BillEntry {

    private Integer id;
    private Integer fkInvoiceNumber;
    private Integer fkProductId;
    private String productName;
    private Double productPrice;
    private String productImage;
    private Integer quantity;

    public BillEntry(Integer id, Integer fkInvoiceNumber, Integer fkProductId, String productName, Double productPrice, String productImage, Integer quantity) {
        this.id = id;
        this.fkInvoiceNumber = fkInvoiceNumber;
        this.fkProductId = fkProductId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkInvoiceNumber() {
        return fkInvoiceNumber;
    }

    public void setFkInvoiceNumber(Integer fkInvoiceNumber) {
        this.fkInvoiceNumber = fkInvoiceNumber;
    }

    public Integer getFkProductId() {
        return fkProductId;
    }

    public void setFkProductId(Integer fkProductId) {
        this.fkProductId = fkProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BillEntry billEntry = (BillEntry) o;

        if (!id.equals(billEntry.id)) return false;
        if (!fkInvoiceNumber.equals(billEntry.fkInvoiceNumber)) return false;
        return fkProductId.equals(billEntry.fkProductId);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + fkInvoiceNumber.hashCode();
        result = 31 * result + fkProductId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "BillEntry{" +
                "id=" + id +
                ", fkInvoiceNumber=" + fkInvoiceNumber +
                ", fkProductId=" + fkProductId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productImage='" + productImage + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
