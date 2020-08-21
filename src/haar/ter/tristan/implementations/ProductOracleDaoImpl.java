package haar.ter.tristan.implementations;

import haar.ter.tristan.interfaces.OV_ChipkaartDao;
import haar.ter.tristan.interfaces.ProductDao;
import haar.ter.tristan.interfaces.ProductDao;
import haar.ter.tristan.models.Adres;
import haar.ter.tristan.models.OV_Chipkaart;
import haar.ter.tristan.models.Product;
import haar.ter.tristan.models.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductOracleDaoImpl implements ProductDao
{
    private boolean lazyLoading = false;

    OV_ChipkaartDao ov_chipkaartDao = null;

    public ProductOracleDaoImpl(){
        this.ov_chipkaartDao = new OV_ChipkaartOracleDaoImpl(true);
    }

    public ProductOracleDaoImpl(boolean lazyLoading)
    {
        this.lazyLoading = lazyLoading;
        if(!lazyLoading) {
            this.ov_chipkaartDao = new OV_ChipkaartOracleDaoImpl(true);
        }
    }

    @Override
    public List<Product> findAll() {
        try
        {
            Statement stmt = this.connection.createStatement();
            String query = "SELECT * FROM PRODUCT";
            ResultSet rs = stmt.executeQuery(query);
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                Product prod = new Product(
                        rs.getInt("PRODUCTNUMMER"),
                        rs.getString("PRODUCTNAAM"),
                        rs.getString("BESCHRIJVING"),
                        rs.getFloat("PRIJS")
                );
                prod.setOv_chipkaarten(this.ov_chipkaartDao.findByProductnummer(prod.getProductNummer()));
                products.add(prod);
            }
            rs.close();
            stmt.close();
            return products;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public Product findByNummer(long nummer) {
        String query = "SELECT * FROM PRODUCT WHERE PRODUCTNUMMER = " + nummer;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Product product = null;
            rs.next();
            product = new Product(
                    rs.getInt("PRODUCTNUMMER"),
                    rs.getString("PRODUCTNAAM"),
                    rs.getString("BESCHRIJVING"),
                    rs.getFloat("PRIJS")
            );
            product.setOv_chipkaarten(this.ov_chipkaartDao.findByProductnummer(product.getProductNummer()));
            rs.close();
            stmt.close();
            return product;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public Product save(Product product) {
        String query = "INSERT INTO PRODUCT (PRODUCTNUMMER, PRODUCTNAAM, BESCHRIJVING, PRIJS) VALUES ("+
                product.getProductNummer()+",'"+
                product.getProductNaam()+"','"+
                product.getBeschrijving()+"',"+
                product.getPrijs() + ")";
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();

            String query3 = "DELETE FROM OV_CHIPKAART_PRODUCT WHERE PRODUCTNUMMER="+product.getProductNummer();
            stmt = this.connection.createStatement();
            rs = stmt.executeQuery(query3);
            rs.close();
            stmt.close();
            for (OV_Chipkaart ov_chipkaart : product.getOv_chipkaarten())
            {
                String query2 = "INSERT INTO OV_CHIPKAART_PRODUCT (KAARTNUMMER, PRODUCTNUMMER) VALUES ("+
                        ov_chipkaart.getKaartNummer()+","+
                        product.getProductNummer()+
                        ")";
                stmt = this.connection.createStatement();
                rs = stmt.executeQuery(query2);
                rs.close();
                stmt.close();
            }
            return product;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Product update(Product product) {
        String query = "UPDATE PRODUCT SET "+
                "PRODUCTNAAM='" + product.getProductNaam()+"',"+
                "BESCHRIJVING='" + product.getBeschrijving()+"',"+
                "PRIJS=" + product.getPrijs()+
                " WHERE PRODUCTNUMMER=" + product.getProductNummer();
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();

            String query3 = "DELETE FROM OV_CHIPKAART_PRODUCT WHERE PRODUCTNUMMER="+product.getProductNummer();
            stmt = this.connection.createStatement();
            rs = stmt.executeQuery(query3);
            rs.close();
            stmt.close();
            for (OV_Chipkaart ov_chipkaart : product.getOv_chipkaarten())
            {
                String query2 = "INSERT INTO OV_CHIPKAART_PRODUCT (KAARTNUMMER, PRODUCTNUMMER) VALUES ("+
                        ov_chipkaart.getKaartNummer()+","+
                        product.getProductNummer()+
                        ")";
                stmt = this.connection.createStatement();
                rs = stmt.executeQuery(query2);
                rs.close();
                stmt.close();
            }

            return product;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(long productID) {
        String query = "DELETE FROM PRODUCT WHERE PRODUCTNUMMER=" + productID;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();
            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByKaartnummer(long nummer) {
        String query = "SELECT PRODUCT.* FROM PRODUCT LEFT JOIN OV_CHIPKAART_PRODUCT ON OV_CHIPKAART_PRODUCT.PRODUCTNUMMER = PRODUCT.PRODUCTNUMMER WHERE PRODUCT.PRODUCTNUMMER IN (SELECT OV_CHIPKAART_PRODUCT.PRODUCTNUMMER FROM OV_CHIPKAART_PRODUCT WHERE KAARTNUMMER = "+nummer+")";

        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                //productNummer, String productNaam, String beschrijving, float prijs
                products.add(
                        new Product(
                                rs.getInt("PRODUCTNUMMER"),
                                rs.getString("PRODUCTNAAM"),
                                rs.getString("BESCHRIJVING"),
                                rs.getFloat("PRIJS")
                        )
                );
            }
            rs.close();
            stmt.close();
            return products;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
