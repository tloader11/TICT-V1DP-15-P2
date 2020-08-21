package haar.ter.tristan;

import haar.ter.tristan.implementations.AdresOracleDaoImpl;
import haar.ter.tristan.implementations.OV_ChipkaartOracleDaoImpl;
import haar.ter.tristan.implementations.ProductOracleDaoImpl;
import haar.ter.tristan.implementations.ReizigerOracleDaoImpl;
import haar.ter.tristan.implementations.database.OracleDatabaseDao;
import haar.ter.tristan.interfaces.*;
import haar.ter.tristan.interfaces.database.DatabaseDao;
import haar.ter.tristan.models.*;
import haar.ter.tristan.tests.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Connection connection;

    public static void main(String[] args) {
        try {
            connection = DatabaseConfig.databaseDao.getConnection();
        } catch (SQLException ex) {
            System.out.println("ERROR STARTING DB CONNECTION. PLEASE MAKE SURE THE ORACLE DB SERVER IS RUNNING");
        }

        //clear all tables
        System.out.println("---==== CLEARING ALL TABLES ====---");
        for (OV_Chipkaart ov_chipkaart : DatabaseConfig.ov_chipkaartDao.findAll()) {
            DatabaseConfig.ov_chipkaartDao.delete(ov_chipkaart.getKaartNummer());   //OV_Chipkaart_Product is cascaded....
        }
        for (Reiziger reiziger : DatabaseConfig.reizigerDao.findAll()) {
            DatabaseConfig.reizigerDao.delete(reiziger.getReizigerID());    //adres table is cascaded as well, so no need to manually empty that one.
        }
        for (Product product : DatabaseConfig.productDao.findAll()) {
            DatabaseConfig.productDao.delete(product.getProductNummer());
        }
        System.out.println("---==== CLEARED ALL TABLES ====---");

        System.out.println("---==== FILLING TABLES ====---");

        //productNummer, String productNaam, String beschrijving, float prijs
        Product testProduct1 = new Product(1, "Studenten OV", "Gratis reizen door de weeks!", 0.0f);
        Product testProduct2 = new Product(2, "Weekend OV", "Gratis reizen in het weekend!", 0.0f);
        Product testProduct3 = new Product(3, "NS Business", "Naar je werk met de trein is prima te doen met onze business pas!", 40.5f);
        Product testProduct4 = new Product(4, "Platinum", "Inclusief alle bussen en metros in Nederland!", 89.9f);
        DatabaseConfig.productDao.save(testProduct1);
        DatabaseConfig.productDao.save(testProduct2);
        DatabaseConfig.productDao.save(testProduct3);
        DatabaseConfig.productDao.save(testProduct4);

        //reizigerID, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum
        Reiziger testReiziger1 = new Reiziger(1, "T.F.", "ter", "Haar", Date.valueOf("1998-04-15"));
        Reiziger testReiziger2 = new Reiziger(2, "T.E.S.T.", "", "Pashouder", Date.valueOf("1970-01-01"));

        //kaartNummer, Date geldigTot, short klasse, float saldo
        OV_Chipkaart testOV_Chipkaart1 = new OV_Chipkaart(1, Date.valueOf("2021-01-01"), (short) 1, 995f);  //1
        OV_Chipkaart testOV_Chipkaart2 = new OV_Chipkaart(2, Date.valueOf("2019-01-01"), (short) 1, 0f);    //1
        OV_Chipkaart testOV_Chipkaart3 = new OV_Chipkaart(3, Date.valueOf("2025-01-01"), (short) 1, 0f);    // 2

        Adres testAdres1 = new Adres(1, "2811RH", "48", "Zwanebloem", "Reeuwijk");
        Adres testAdres2 = new Adres(2, "2802ER", "37", "Lazaruskade", "Gouda");
        Adres testAdres3 = new Adres(3, "1000AA", "123", "Grau", "Groningen");

        //assoicate ov_chipkaarten en adressen
        List<OV_Chipkaart> ov_chipkaarten = new ArrayList<>();
        ov_chipkaarten.add(testOV_Chipkaart1);
        ov_chipkaarten.add(testOV_Chipkaart2);
        testReiziger1.setOvChipkaarten(ov_chipkaarten);

        List<Adres> adressen = new ArrayList<>();
        adressen.add(testAdres1);
        adressen.add(testAdres2);
        testReiziger1.setAdressen(adressen);


        //assoicate ov_chipkaarten en adressen for reiziger2
        List<OV_Chipkaart> ov_chipkaarten2 = new ArrayList<>();
        ov_chipkaarten2.add(testOV_Chipkaart3);
        testReiziger2.setOvChipkaarten(ov_chipkaarten2);

        List<Adres> adressen2 = new ArrayList<>();
        adressen2.add(testAdres3);
        testReiziger2.setAdressen(adressen2);

        DatabaseConfig.reizigerDao.save(testReiziger1);
        DatabaseConfig.reizigerDao.save(testReiziger2);

        //READ
        OV_Chipkaart kaart = DatabaseConfig.ov_chipkaartDao.findByID(1);
        System.out.println(kaart.getKaartNummer());
        Reiziger reiziger = kaart.getReiziger();
        System.out.println(reiziger.getName() + " saldo: " + kaart.getSaldo());

        //UPDATE
        kaart.setSaldo(85434.5f);
        DatabaseConfig.ov_chipkaartDao.update(kaart);

        //RE-read
        kaart = DatabaseConfig.ov_chipkaartDao.findByID(1);
        System.out.println(reiziger.getName() + " saldo: " + kaart.getSaldo());

//        DatabaseConfig.reizigerDao.delete(reiziger.getReizigerID());

//        kaart = DatabaseConfig.ov_chipkaartDao.findByID(1); //should be gone, as OV chipkaarten are cascaded.
//        System.out.println(kaart);  //is indeed null now

        List<Product> producten = new ArrayList<>();
        producten.add(testProduct1);
        producten.add(testProduct2);

        kaart.setProducts(producten);
        DatabaseConfig.ov_chipkaartDao.update(kaart);

        kaart = DatabaseConfig.ov_chipkaartDao.findByID(1);

        for(Product product : kaart.getProducts())
        {
            System.out.println(product.getProductNaam());
        }

        Product p = DatabaseConfig.productDao.findByNummer(1);

        for(OV_Chipkaart tmp_kaart : p.getOv_chipkaarten())
        {
            System.out.println(tmp_kaart.getKaartNummer() + " - " + tmp_kaart.getGeldigTot());
        }

        List<OV_Chipkaart> kaartenUpdate = new ArrayList<>();
        kaartenUpdate.add(testOV_Chipkaart2);
        p.setOv_chipkaarten(kaartenUpdate);

        DatabaseConfig.productDao.update(p);

        testOV_Chipkaart2 = DatabaseConfig.ov_chipkaartDao.findByID(2);

        for(Product temp_prod : testOV_Chipkaart2.getProducts())
        {
            System.out.println("GOT: " + temp_prod.getProductNaam());
        }




    }
}
