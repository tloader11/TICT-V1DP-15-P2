package haar.ter.tristan.models;

import java.util.ArrayList;
import java.util.List;

public class Product
{
    private long productNummer;
    private String productNaam;
    private String beschrijving;
    private float prijs;        //double or decimal do not seem that important for this setup.

    private List<OV_Chipkaart> ov_chipkaarten = new ArrayList<>();

    public Product()
    {
    }

    public Product(long productNummer, String productNaam, String beschrijving, float prijs) {
        this.productNummer = productNummer;
        this.productNaam = productNaam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public long getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(long productNummer) {
        this.productNummer = productNummer;
    }

    public String getProductNaam() {
        return productNaam;
    }

    public void setProductNaam(String productNaam) {
        this.productNaam = productNaam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public float getPrijs() {
        return prijs;
    }

    public void setPrijs(float prijs) {
        this.prijs = prijs;
    }

    public List<OV_Chipkaart> getOv_chipkaarten() {
        return ov_chipkaarten;
    }

    public void setOv_chipkaarten(List<OV_Chipkaart> ov_chipkaarten) {
        this.ov_chipkaarten = ov_chipkaarten;
    }
}
