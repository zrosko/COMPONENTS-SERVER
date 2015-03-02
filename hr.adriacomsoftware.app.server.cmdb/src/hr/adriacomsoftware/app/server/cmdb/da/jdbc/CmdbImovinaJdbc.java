package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class CmdbImovinaJdbc extends CMDBJdbc {
    public CmdbImovinaJdbc() {
        setTableName("cmdb_imovina");
    }
    public CmdbImovinaRs daoPronadiImovinu(CmdbImovinaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_cmdb_imovina_pogled  ");
        sql.appLike("AND", "naziv", value.getNaziv()); 
        sql.appEqual("AND", "id_kategorije", value.getIdKategorije());
        sql.appEqual("AND", "odgovorna_osoba", value.getOdgovornaOsoba());
        sql.appEqual("AND", "zaduzena_osoba", value.getZaduzenaOsoba());
        sql.appEqual("AND", "id_zgrade", value.getIdZgrade());
        sql.appEqual("AND", "kat", value.getKat());
        sql.appLike("AND", "model", value.getModel()); 
        sql.appLike("AND", "serijski_broj2", value.getSerijskiBroj2()); 
        sql.appLike("AND", "inventurni_broj", value.getInventurniBroj()); 
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        //sql = odrediRazinuOvlasti(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.appendln("ORDER BY id_kategorije");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        CmdbImovinaRs j2eers = new CmdbImovinaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public CmdbImovinaRs daoPronadiImovinuNovo(CmdbImovinaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_cmdb_imovina_pogled ");
        if(value.get("pregled").equals("zo"))
            sql.appEqual("AND", "zaduzena_osoba", value.getZaduzenaOsoba());
        else if(value.get("pregled").equals("oj"))
            sql.appEqual("AND", "odgovorna_osoba", value.getOdgovornaOsoba());
        else if(value.get("pregled").equals("zg"))
            sql.appEqual("AND", "id_zgrade", value.getIdZgrade());
        else if(value.get("pregled").equals("so"))
            sql.appLike("AND", "oznaka_sobe", value.getOznakaSobe());
        else if(value.get("pregled").equals("skladiste"))
            sql.appLike("AND", "zaduzena_osoba_naziv", "SKLADIŠTE");
        if(value.get("@@filter").length()>0)
            sql.appIn("AND", "id_kategorije", value.get("@@filter"));;
        sql.appEqual("AND", "ispravno", "1");
        sql.appendln(" ORDER BY id_kategorije");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        CmdbImovinaRs j2eers = new CmdbImovinaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public CmdbImovinaRs daoListaImovine(CmdbImovinaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_cmdb_imovina_pogled  ");
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlasti(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.append(" ORDER BY id_kategorije ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        CmdbImovinaRs j2eers = new CmdbImovinaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}