package haar.ter.tristan.interfaces;

import haar.ter.tristan.Main;
import haar.ter.tristan.models.Product;

import java.sql.Connection;
import java.util.List;

public interface ProductDao
{
    static Connection connection = Main.connection;

    List<Product> findAll();

    Product findByNummer(long nummer);

    List<Product> findByKaartnummer(long nummer);

    Product save(Product product);

    Product update(Product product);

    boolean delete(long productID);
}
