package haar.ter.tristan.interfaces;

import haar.ter.tristan.Main;
import haar.ter.tristan.models.OV_Chipkaart;
import haar.ter.tristan.models.Product;

import java.sql.Connection;
import java.util.List;

public interface OV_ChipkaartDao
{
    static Connection connection = Main.connection;

    List<OV_Chipkaart> findAll();

    List<OV_Chipkaart> findByReizigerID(long id);

    OV_Chipkaart findByID(long id);

    List<OV_Chipkaart> findByProductnummer(long nummer);

    OV_Chipkaart save(OV_Chipkaart ov_chipkaart);

    OV_Chipkaart update(OV_Chipkaart ov_chipkaart);

    boolean delete(long kaartnummer);
}
