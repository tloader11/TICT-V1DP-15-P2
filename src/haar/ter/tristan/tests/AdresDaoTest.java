package haar.ter.tristan.tests;

import haar.ter.tristan.implementations.AdresOracleDaoImpl;
import haar.ter.tristan.interfaces.AdresDao;
import haar.ter.tristan.interfaces.DaoTest;
import haar.ter.tristan.models.Adres;

public class AdresDaoTest implements DaoTest
{
    public void doTest()
    {
        AdresDao adresDAO = new AdresOracleDaoImpl();

        Adres adres = new Adres();
        adres.setPostcode("2811RH");
        adres.setHuisnummer("48");
        adres.setStraat("Zwanebloem");
        adres.setWoonplaats("Reeuwijk");
        adres.setAdresID(1);

        System.out.println("CREATE TEST");
        //save
        adresDAO.save(adres);
        for(Adres opgehaaldAdres : adresDAO.findAll())
        {
            System.out.println(opgehaaldAdres.getPostcode() + " " + opgehaaldAdres.getHuisnummer());
        }

        System.out.println("UPDATE TEST");
        //update
        adres.setHuisnummer("50");
        adresDAO.update(adres);
        for(Adres opgehaaldAdres : adresDAO.findAll())
        {
            System.out.println(opgehaaldAdres.getPostcode() + " " + opgehaaldAdres.getHuisnummer());
        }

        System.out.println("DELETE TEST");
        //delete
        adresDAO.delete(adres.getAdresID());
        for(Adres opgehaaldAdres : adresDAO.findAll())
        {
            System.out.println("THIS SHOULD NOT RETURN: " + opgehaaldAdres.getPostcode() + " " + opgehaaldAdres.getHuisnummer());
        }
        if(adresDAO.findAll().size() == 0)
        {
            System.out.println("No items found anymore, remove was successful.");
        }
    }
}
