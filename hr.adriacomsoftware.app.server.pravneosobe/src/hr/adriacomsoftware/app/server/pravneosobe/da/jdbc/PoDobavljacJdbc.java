package hr.adriacomsoftware.app.server.pravneosobe.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.DobavljacVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoDobavljacJdbc extends OLTPJdbc {
    public PoDobavljacJdbc() {
        setTableName("klijent_po_dobavljac");
    }
    public AS2RecordList daoFind(AS2Record value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("select naziv, prethodna_promet, prethodna_saldo, tekuca_promet, tekuca_saldo, id_dobavljaca,maticni_broj, oib,strani,  ");
        sql.append("prethodna_postotak_prometa,tekuca_postotak_prometa, prethodna_saldo_ukupno, prethodna_promet_ukupno, tekuca_saldo_ukupno,");
        sql.append("tekuca_promet_ukupno,datum_analize,convert(char(10),datum_analize,104) as datum_analize_ ");
        sql.append("  FROM dbo.fn_po_klijent_dobavljaci (?,?,?,?) ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.get("maticni_broj"));
	        pstmt.setObject(2,value.get("oib"));
	        if(value.exists("datum_analize"))
	            pstmt.setDate(3,value.getAsSqlDate("datum_analize"));
	        else
	            pstmt.setDate(3,null);
	        pstmt.setObject(4,value.getProperty("naprijed_nazad"));
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoRemoveSveDobavljaceZaKlijenta(DobavljacVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("delete FROM klijent_po_dobavljac WHERE broj_zahtjeva = ? or oib = ?");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get("maticni_broj"));
	        pstmt.setObject(2, value.get("oib"));
	        pstmt.executeUpdate();
	        pstmt.close();
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoRemoveUcitaneDobavljace(DobavljacVo value)  {
	    J2EESqlBuilder sql = new J2EESqlBuilder();
	    sql.append("delete FROM klijent_po_dobavljac WHERE maticni_broj = ? ");
	    sql.append(" and isnull(ucitano,0) = 1");
	    try{
		    PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
		    pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
		    pstmt.executeUpdate();
		    pstmt.close();
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}