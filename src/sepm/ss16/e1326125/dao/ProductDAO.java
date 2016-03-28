package sepm.ss16.e1326125.dao;

import sepm.ss16.e1326125.entity.Product;

import java.util.List;


public interface ProductDAO {

    /**
     * Creates a new product.
     * @param product Contains the values for the new product.
     * @return A new product, if successful.
     * @throws DAOException If the product could not be created.
     */
    public Product create(Product product) throws DAOException;

    /**
     * Returns a list of all products.
     * @return All products.
     */
    public List<Product> findAll() throws DAOException;

    /**
     * Returns a list of products for which the price and stock are in between the
     * correspondent values in the two objects "from" and "to".
     * @param from The first criteria object.
     * @param to The second criteria object.
     * @return The search result.
     * @throws DAOException If the search could not be performed.
     */
    public List<Product> search(Product from, Product to) throws DAOException;

    /**
     * Updates a product by assigning new values to it.
     * @param product Contains the new values for the product.
     * @throws DAOException If the product could not be updated.
     */
    public void update(Product product) throws DAOException;

    /**
     * Deletes a certain product.
     * @param product The product to be removed from the database.
     * @throws DAOException If the product could not be removed.
     */
    public void delete(Product product) throws DAOException;
}
