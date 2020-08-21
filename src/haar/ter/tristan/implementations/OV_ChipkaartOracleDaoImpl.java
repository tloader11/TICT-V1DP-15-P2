package haar.ter.tristan.implementations;

import haar.ter.tristan.interfaces.AdresDao;
import haar.ter.tristan.interfaces.OV_ChipkaartDao;
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

    public OV_ChipkaartOracleDaoImpl(){
        this.reizigerDao = new ReizigerOracleDaoImpl(true);
    }

    public OV_ChipkaartOracleDaoImpl(boolean lazyLoading)
    {
        this.lazyLoading = lazyLoading;
        if(!lazyLoading) {
            this.reizigerDao = new ReizigerOracleDaoImpl(true);
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
                if(this.reizigerDao == null)
                {
                    System.out.println("WTFFFFFFF");
                }
                ov_chipkaart.setReiziger(this.reizigerDao.findByID(rs.getInt("REIZIGERID")));
            }
            rs.close();
            stmt.close();
            return ov_chipkaart;
        }
        catch (SQLException ex)
        {
            return null;
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

            return true;
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}
