package haar.ter.tristan.models;

import haar.ter.tristan.DatabaseConfig;

public class Adres
{
    private long adresID;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private long reizigerID;

    private Reiziger reiziger;

    public Adres()
    {
    }

    public Adres(long adresID, String postcode, String huisnummer, String straat, String woonplaats) {
        this.adresID = adresID;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
    }

    public long getAdresID() {
        return adresID;
    }

    public void setAdresID(long adresID) {
        this.adresID = adresID;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    public Reiziger getReiziger() {
        return this.reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public long getReizigerID() {
        return reizigerID;
    }

    public void setReizigerID(long reizigerID) {
        this.reizigerID = reizigerID;
    }
}
