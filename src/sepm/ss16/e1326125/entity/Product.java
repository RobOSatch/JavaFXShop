package sepm.ss16.e1326125.entity;


public class Product {

    private Integer productId;
    private String name;
    private Double price;
    private Integer stock;
    private String image;
    private Boolean isDeleted;

    public Product(Integer productId, String name, Double price, Integer stock, String image, Boolean isDeleted) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.isDeleted = isDeleted;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (this.getProductId() == null || ((Product) o).getProductId() == null) {
            return false;
        }

        return productId.equals(product.productId);

    }

    @Override
    public int hashCode() {
        return productId.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", image='" + image + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
