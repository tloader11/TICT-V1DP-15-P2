package haar.ter.tristan.implementations;

import haar.ter.tristan.DatabaseConfig;
import haar.ter.tristan.interfaces.AdresDao;
import haar.ter.tristan.interfaces.OV_ChipkaartDao;
import haar.ter.tristan.interfaces.ReizigerDao;
import haar.ter.tristan.models.Adres;
import haar.ter.tristan.models.OV_Chipkaart;
import haar.ter.tristan.models.Reiziger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReizigerOracleDaoImpl implements ReizigerDao
{

    private boolean lazyLoading = false;

    OV_ChipkaartDao ov_chipkaartDao = null;
    AdresDao adresDao = null;

    public ReizigerOracleDaoImpl(){
        ov_chipkaartDao = new OV_ChipkaartOracleDaoImpl(true);
        adresDao = new AdresOracleDaoImpl(true);
    }

    public ReizigerOracleDaoImpl(boolean lazyLoading)
    {
        this.lazyLoading = lazyLoading;
        if(!lazyLoading)
        {
            ov_chipkaartDao = new OV_ChipkaartOracleDaoImpl(true);
            adresDao = new AdresOracleDaoImpl(true);
        }
    }

    @Override
    public List<Reiziger> findAll() {
        try
        {
            Statement stmt = this.connection.createStatement();
            String query = "SELECT * FROM REIZIGER";
            ResultSet rs = stmt.executeQuery(query);
            List<Reiziger> reizigers = new ArrayList<>();
            while (rs.next()) {
                //long reizigerID, String voornaam, String tussenvoegsel, String achternaam, Date geboortedatum
                Reiziger tmp_reiziger = new Reiziger(
                        rs.getInt("REIZIGERID"),
                        rs.getString("VOORLETTERS"),
                        rs.getString("TUSSENVOEGSEL"),
                        rs.getString("ACHTERNAAM"),
                        rs.getDate("GEBOORTEDATUM")
                );
                if(!lazyLoading)
                {
                    tmp_reiziger.setOvChipkaarten( ov_chipkaartDao.findByReizigerID( tmp_reiziger.getReizigerID() ) );
                    tmp_reiziger.setAdressen( adresDao.findByReizigerID( tmp_reiziger.getReizigerID() ) );
                }
                reizigers.add( tmp_reiziger );
            }
            rs.close();
            stmt.close();
            return reizigers;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGeboortedatum(String GBdatum) {
        List<Reiziger> reizigerMetGBDatum = new ArrayList<>();
        String query = "SELECT * FROM REIZIGER WHERE GEBOORTEDATUM LIKE TO_DATE('" + GBdatum + "', 'YYYY-MM-DD')";
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            List<Reiziger> reizigers = new ArrayList<>();
            while (rs.next()) {
                Reiziger tmp_reiziger = new Reiziger(
                        rs.getInt("REIZIGERID"),
                        rs.getString("VOORLETTERS"),
                        rs.getString("TUSSENVOEGSEL"),
                        rs.getString("ACHTERNAAM"),
                        rs.getDate("GEBOORTEDATUM")
                );
                if(!lazyLoading)
                {
                    tmp_reiziger.setOvChipkaarten( ov_chipkaartDao.findByReizigerID( tmp_reiziger.getReizigerID() ) );
                    tmp_reiziger.setAdressen( adresDao.findByReizigerID( tmp_reiziger.getReizigerID() ) );
                }
                reizigerMetGBDatum.add( tmp_reiziger );
            }
            rs.close();
            stmt.close();
            return reizigerMetGBDatum;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public Reiziger findByID(long id) {
        String query = "SELECT * FROM REIZIGER WHERE REIZIGERID = " + id;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Reiziger reiziger = null;
            rs.next();
            reiziger = new Reiziger(
                    rs.getInt("REIZIGERID"),
                    rs.getString("VOORLETTERS"),
                    rs.getString("TUSSENVOEGSEL"),
                    rs.getString("ACHTERNAAM"),
                    rs.getDate("GEBOORTEDATUM")
            );
            if(reiziger != null && !lazyLoading)
            {
                reiziger.setOvChipkaarten( ov_chipkaartDao.findByReizigerID( reiziger.getReizigerID() ) );
                reiziger.setAdressen( adresDao.findByReizigerID( reiziger.getReizigerID() ) );
            }
            rs.close();
            stmt.close();
            return reiziger;
        }
        catch (SQLException ex)
        {
            return null;
        }
    }

    @Override
    public Reiziger save(Reiziger reiziger) {
        //save reiziger itself
        String query = "INSERT INTO REIZIGER (REIZIGERID, VOORLETTERS, TUSSENVOEGSEL, ACHTERNAAM, GEBOORTEDATUM) VALUES ("+
                reiziger.getReizigerID()+",'"+
                reiziger.getVoorletters()+"','"+
                reiziger.getTussenvoegsel()+"','"+
                reiziger.getAchternaam()+"',"+
                "TO_DATE('"+reiziger.getGeboortedatum()+"', 'YYYY-MM-DD')"+
        ")";
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();

            if(!lazyLoading)
            {
                //save associated OV_Chipkaarten
                for (OV_Chipkaart chipkaart : reiziger.getOVChipkaarten()) {
                    ov_chipkaartDao.save(chipkaart);
                }
                //save associated Adressen
                for (Adres adres : reiziger.getAdressen()) {
                    adresDao.save(adres);
                }
            }
            return reiziger;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }


    @Override
    public Reiziger update(Reiziger reiziger)
    {
        String query = "UPDATE REIZIGER SET "+
                "VOORLETTERS='" + reiziger.getVoorletters()+"',"+
                "TUSSENVOEGSEL='" + reiziger.getTussenvoegsel()+"',"+
                "ACHTERNAAM='" + reiziger.getAchternaam()+"',"+
                "GEBOORTEDATUM=" + "TO_DATE('"+reiziger.getGeboortedatum()+"', 'YYYY-MM-DD')"+
                " WHERE REIZIGERID=" + reiziger.getReizigerID();
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();

            if(!lazyLoading)
            {
                //save associated OV_Chipkaarten
                for (OV_Chipkaart chipkaart : reiziger.getOVChipkaarten()) {
                    ov_chipkaartDao.update(chipkaart);
                }
                //save associated Adressen
                for (Adres adres : reiziger.getAdressen()) {
                    adresDao.save(adres);
                }
            }
            return reiziger;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean delete(long reizigerID) {
        Reiziger reiziger = this.findByID(reizigerID);  //get the reiziger before removal

        String query = "DELETE FROM REIZIGER WHERE  REIZIGERID=" + reizigerID;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            stmt.close();

            if(!lazyLoading)
            {
                //delete associated OV_Chipkaarten, as OV_chipkaart is ONE to MANY, but the reziger is always required.
                for (OV_Chipkaart chipkaart : reiziger.getOVChipkaarten()) {
                    ov_chipkaartDao.delete(chipkaart.getKaartNummer());
                }
                //delete associated Adressen, as OV_chipkaart is ONE to MANY, but the reziger is always required.
                for (Adres adres : reiziger.getAdressen()) {
                    adresDao.delete(adres.getAdresID());
                }
            }
            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}
