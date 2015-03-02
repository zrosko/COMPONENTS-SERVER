package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahBonitetJdbc extends PoZahJdbc {
    public PoZahBonitetJdbc() {
        setTableName("po_zah_bonitet");//po_zah_bonitet_statisticko
    }
    /**
    Insert sloga za bonitet GFI ili porezna. Podatke čuvamo kako se ne bi 
    pri izmjeni podataka u aplikaciji Bonitet dogodilo da se razlikuje referat
    u vrijeme izrade i kasnije pri povlačenju iz "arhive".
 */
    public void daoCreateBonitetGfiPorezna(ZahtjevPravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("DELETE FROM po_zah_bonitet ");
        sql.appendln("WHERE broj_zahtjeva = ?");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.executeUpdate();
	        pstmt.close();
	        
	        sql = new J2EESqlBuilder();
	        sql.appendln("INSERT INTO po_zah_bonitet ");
	        sql.appendln("SELECT * FROM BI_PROD.dbo.bi_fn_po_bonitet_referat(?,?,?)");
	        pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setDate(2, value.getAsSqlDate("datum_obrade"));
	        pstmt.setObject(3,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.executeUpdate();
	        pstmt.close();  
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoCreateBonitetStatisticko(ZahtjevPravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("DELETE FROM po_zah_bonitet_statisticko ");
        sql.appendln("WHERE broj_zahtjeva = ?");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.executeUpdate();
	        pstmt.close();
	        
	        sql = new J2EESqlBuilder();
	        sql.appendln("INSERT INTO po_zah_bonitet_statisticko ");
	        sql.appendln("SELECT * FROM BI_PROD.dbo.bi_fn_po_bonitet_referat_statisticko_NOVO(?,?,?)");
	        pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setDate(2, value.getAsSqlDate("datum_obrade"));
	        pstmt.setObject(3,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.executeUpdate();
	        pstmt.close(); 
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }