package haar.ter.tristan.implementations;

import haar.ter.tristan.interfaces.AdresDao;
import haar.ter.tristan.models.Adres;
import haar.ter.tristan.models.Reiziger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdresOracleDaoImpl implements AdresDao
{

    private boolean lazyLoading = false;

    public AdresOracleDaoImpl() {

    }

    public AdresOracleDaoImpl(boolean lazyLoading) {
        this.lazyLoading = lazyLoading;
    }

    List<Adres> adressen = new ArrayList<>();
    @Override
    public List<Adres> findAll() {
        try
        {
            Statement stmt = this.connection.createStatement();
            String query = "SELECT * FROM ADRES";
            ResultSet rs = stmt.executeQuery(query);
            List<Adres> adressen = new ArrayList<>();
            while (rs.next()) {
                //long reizigerID, String voornaam, String tussenvoegsel, String achternaam, Date geboortedatum
                adressen.add(
                        new Adres(
                                rs.getInt("ADRESID"),
                                rs.getString("POSTCODE"),
                                rs.getString("HUISNUMMER"),
                                rs.getString("STRAAT"),
                                rs.getString("WOONPLAATS")
//                                rs.getInt("REIZIGERID")
                        )
                );
            }
            rs.close();
            stmt.close();
            return adressen;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public List<Adres> findByReizigerID(long id) {
        String query = "SELECT * FROM ADRES WHERE REIZIGERID =" + id;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<Adres> adresMetReiziger = new ArrayList<>();
            while (rs.next()) {
                //long reizigerID, String voornaam, String tussenvoegsel, String achternaam, Date geboortedatum
                adresMetReiziger.add(
                        new Adres(
                                rs.getInt("ADRESID"),
                                rs.getString("POSTCODE"),
                                rs.getString("HUISNUMMER"),
                                rs.getString("STRAAT"),
                                rs.getString("WOONPLAATS")
//                                rs.getInt("REIZIGERID")
                        )
                );
            }
            rs.close();
            stmt.close();
            return adresMetReiziger;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public Adres findByID(long id) {
        String query = "SELECT * FROM ADRES WHERE ADRESID = " + id;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Adres adres = null;
            rs.next();
            adres = new Adres(
                    rs.getInt("ADRESID"),
                    rs.getString("POSTCODE"),
                    rs.getString("HUISNUMMER"),
                    rs.getString("STRAAT"),
                    rs.getString("WOONPLAATS")
//                    rs.getInt("REIZIGERID")
            );
            rs.close();
            stmt.close();
            return adres;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public Adres save(Adres adres) {
        String query = "INSERT INTO ADRES (ADRESID, POSTCODE, HUISNUMMER, STRAAT, WOONPLAATS, REIZIGERID) VALUES ("+
                adres.getAdresID()+",'"+
                adres.getPostcode()+"','"+
                adres.getHuisnummer()+"','"+
                adres.getStraat()+"','"+
                adres.getWoonplaats()+"',"+
                adres.getReizigerID() +
                ")";
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();
            return adres;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Adres update(Adres adres) {
        String query = "UPDATE ADRES SET "+
                "POSTCODE='" + adres.getPostcode()+"',"+
                "HUISNUMMER='" + adres.getHuisnummer()+"',"+
                "STRAAT='" + adres.getStraat()+"',"+
                "WOONPLAATS='" + adres.getWoonplaats()+"',"+
                "REIZIGERID=" + adres.getReiziger().getReizigerID()+
                " WHERE ADRESID=" + adres.getAdresID();
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();
            return adres;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(long adresID) {
        String query = "DELETE FROM ADRES WHERE ADRESID=" + adresID;
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
}
