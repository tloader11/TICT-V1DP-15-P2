package haar.ter.tristan.models;

import haar.ter.tristan.DatabaseConfig;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OV_Chipkaart
{
    private long kaartNummer;
    private Date geldigTot;
    private short klasse;
    private float saldo;
    private long reizigerID;

    private Reiziger reiziger;

    private List<Product> products = new ArrayList<>();

    public OV_Chipkaart()
    {
    }

    public long getKaartNummer() {
        return kaartNummer;
    }

    public OV_Chipkaart(long kaartNummer, Date geldigTot, short klasse, float saldo) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
//        this.reizigerID = reizigerID;
    }

    public void setKaartNummer(long kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public short getKlasse() {
        return klasse;
    }

    public void setKlasse(short klasse) {
        this.klasse = klasse;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public long getReizigerID() {
        return reizigerID;
    }

    public void setReizigerID(long reizigerID) {
        this.reizigerID = reizigerID;
    }

    public Reiziger getReiziger()
    {
        if(this.reiziger == null)   //local memory caching :-)
        {
            this.reiziger = DatabaseConfig.reizigerDao.findByID(this.getReizigerID());
        }
        return this.reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reizigerID = reiziger.getReizigerID();
        this.reiziger = reiziger;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
