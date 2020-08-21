package haar.ter.tristan.implementations;

import haar.ter.tristan.interfaces.AdresDao;
import haar.ter.tristan.interfaces.OV_ChipkaartDao;
import haar.ter.tristan.interfaces.ProductDao;
import haar.ter.tristan.interfaces.ReizigerDao;
import haar.ter.tristan.models.OV_Chipkaart;
import haar.ter.tristan.models.Product;
import haar.ter.tristan.models.Reiziger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OV_ChipkaartOracleDaoImpl implements OV_ChipkaartDao
{


    private boolean lazyLoading = false;

    ReizigerDao reizigerDao = null;
    ProductDao productDao = null;

    public OV_ChipkaartOracleDaoImpl(){
        this.reizigerDao = new ReizigerOracleDaoImpl(true);
        this.productDao = new ProductOracleDaoImpl(true);
    }

    public OV_ChipkaartOracleDaoImpl(boolean lazyLoading)
    {
        this.lazyLoading = lazyLoading;
        if(!lazyLoading) {
            this.reizigerDao = new ReizigerOracleDaoImpl(true);
            this.productDao = new ProductOracleDaoImpl(true);
        }
    }

    @Override
    public List<OV_Chipkaart> findAll() {
        try
        {
            Statement stmt = this.connection.createStatement();
            String query = "SELECT * FROM OV_CHIPKAART";
            ResultSet rs = stmt.executeQuery(query);
            List<OV_Chipkaart> ov_chipkaartList = new ArrayList<>();
            while (rs.next()) {
                OV_Chipkaart tmp_ov_chipkaart = new OV_Chipkaart(
                        rs.getInt("KAARTNUMMER"),
                        rs.getDate("GELDIGTOT"),
                        rs.getShort("KLASSE"),
                        rs.getFloat("SALDO")
//                                rs.getInt("REIZIGERID")
                );
                if(!lazyLoading) {
                    tmp_ov_chipkaart.setReiziger(this.reizigerDao.findByID(rs.getInt("REIZIGERID")));
                    List<Product> products = this.productDao.findByKaartnummer(tmp_ov_chipkaart.getKaartNummer());
                    if(products != null)
                    {
                        tmp_ov_chipkaart.setProducts(products);
                    }
                }
                ov_chipkaartList.add( tmp_ov_chipkaart );
            }
            rs.close();
            stmt.close();
            return ov_chipkaartList;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public List<OV_Chipkaart> findByReizigerID(long id) {
        String query = "SELECT * FROM OV_CHIPKAART WHERE REIZIGERID =" + id;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<OV_Chipkaart> ovChipkaartMetReiziger = new ArrayList<>();
            while (rs.next()) {
                OV_Chipkaart tmp_ov_chipkaart = new OV_Chipkaart(
                        rs.getInt("KAARTNUMMER"),
                        rs.getDate("GELDIGTOT"),
                        rs.getShort("KLASSE"),
                        rs.getFloat("SALDO")
                );
                if(!lazyLoading) {
                    tmp_ov_chipkaart.setReiziger(this.reizigerDao.findByID(rs.getInt("REIZIGERID")));
                    tmp_ov_chipkaart.setProducts(this.productDao.findByKaartnummer(tmp_ov_chipkaart.getKaartNummer()));
                }
                ovChipkaartMetReiziger.add( tmp_ov_chipkaart );
            }
            rs.close();
            stmt.close();
            return ovChipkaartMetReiziger;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public OV_Chipkaart findByID(long id) {
        String query = "SELECT * FROM OV_CHIPKAART WHERE KAARTNUMMER = " + id;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            OV_Chipkaart ov_chipkaart = null;
            rs.next();
            ov_chipkaart = new OV_Chipkaart(
                    rs.getInt("KAARTNUMMER"),
                    rs.getDate("GELDIGTOT"),
                    rs.getShort("KLASSE"),
                    rs.getFloat("SALDO")
//                    rs.getInt("REIZIGERID")
            );
            if(!lazyLoading) {
                ov_chipkaart.setReiziger(this.reizigerDao.findByID(rs.getInt("REIZIGERID")));
                ov_chipkaart.setProducts(this.productDao.findByKaartnummer(ov_chipkaart.getKaartNummer()));
            }
            rs.close();
            stmt.close();

            if(ov_chipkaart != null)
            {

            }
            return ov_chipkaart;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public List<OV_Chipkaart> findByProductnummer(long nummer) {
        String query = "SELECT OV_CHIPKAART.* FROM OV_CHIPKAART LEFT JOIN OV_CHIPKAART_PRODUCT ON OV_CHIPKAART_PRODUCT.KAARTNUMMER = OV_CHIPKAART.KAARTNUMMER WHERE OV_CHIPKAART.KAARTNUMMER IN (SELECT OV_CHIPKAART_PRODUCT.KAARTNUMMER FROM OV_CHIPKAART_PRODUCT WHERE PRODUCTNUMMER = "+nummer+")";

        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<OV_Chipkaart> ov_chipkaarten = new ArrayList<>();
            while (rs.next()) {
                //productNummer, String productNaam, String beschrijving, float prijs
                OV_Chipkaart kaart = new OV_Chipkaart(
                        rs.getInt("KAARTNUMMER"),
                        rs.getDate("GELDIGTOT"),
                        rs.getShort("KLASSE"),
                        rs.getFloat("SALDO")
                );
                kaart.setReizigerID(rs.getInt("REIZIGERID"));

                if(!lazyLoading)
                {
                    kaart.setReiziger(this.reizigerDao.findByID(rs.getInt("REIZIGERID")));
                    kaart.setProducts(this.productDao.findByKaartnummer(kaart.getKaartNummer()));
                }

                ov_chipkaarten.add( kaart );
            }
            rs.close();
            stmt.close();
            return ov_chipkaarten;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public OV_Chipkaart save(OV_Chipkaart ov_chipkaart) {

        String query = "INSERT INTO OV_CHIPKAART (KAARTNUMMER, GELDIGTOT, KLASSE, SALDO, REIZIGERID) VALUES ("+
                ov_chipkaart.getKaartNummer()+","+
                "TO_DATE('"+ov_chipkaart.getGeldigTot()+"', 'YYYY-MM-DD'),"+
                ov_chipkaart.getKlasse()+","+
                ov_chipkaart.getSaldo()+","+
                ov_chipkaart.getReizigerID()+
                ")";
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();

            if(!lazyLoading) {
                reizigerDao.save(ov_chipkaart.getReiziger());

                String query3 = "DELETE * FROM OV_CHIPKAART_PRODUCT WHERE KAARTNUMMER="+ov_chipkaart.getKaartNummer();
                stmt = this.connection.createStatement();
                rs = stmt.executeQuery(query3);
                rs.close();
                stmt.close();
                for (Product prod : ov_chipkaart.getProducts())
                {
                    String query2 = "INSERT INTO OV_CHIPKAART_PRODUCT (KAARTNUMMER, PRODUCTNUMMER) VALUES ("+
                            ov_chipkaart.getKaartNummer()+","+
                            prod.getProductNummer()+
                            ")";
                    stmt = this.connection.createStatement();
                    rs = stmt.executeQuery(query2);
                    rs.close();
                    stmt.close();
                }
            }
            return ov_chipkaart;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public OV_Chipkaart update(OV_Chipkaart ov_chipkaart) {
        String query = "UPDATE OV_CHIPKAART SET "+
                "GELDIGTOT=" + "TO_DATE('"+ov_chipkaart.getGeldigTot()+"', 'YYYY-MM-DD')"+","+
                "KLASSE=" + ov_chipkaart.getKlasse()+","+
                "SALDO=" + ov_chipkaart.getSaldo()+","+
                "REIZIGERID=" + ov_chipkaart.getReizigerID()+
                " WHERE KAARTNUMMER=" + ov_chipkaart.getKaartNummer();
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();

            if(!lazyLoading) {
                reizigerDao.update(ov_chipkaart.getReiziger());

                String query3 = "DELETE FROM OV_CHIPKAART_PRODUCT WHERE KAARTNUMMER="+ov_chipkaart.getKaartNummer();
                stmt = this.connection.createStatement();
                rs = stmt.executeQuery(query3);
                rs.close();
                stmt.close();
                for (Product prod : ov_chipkaart.getProducts())
                {
                    String query2 = "INSERT INTO OV_CHIPKAART_PRODUCT (KAARTNUMMER, PRODUCTNUMMER) VALUES ("+
                            ov_chipkaart.getKaartNummer()+","+
                            prod.getProductNummer()+
                            ")";
                    stmt = this.connection.createStatement();
                    rs = stmt.executeQuery(query2);
                    rs.close();
                    stmt.close();
                }

            }
            return ov_chipkaart;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(long kaartnummer) {
        String query = "DELETE FROM OV_CHIPKAART WHERE  KAARTNUMMER=" + kaartnummer;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();

            //reiziger kan zonder chipkaarten bestaan.
            //product kan zonder chipkaarten bestaan.

            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}
