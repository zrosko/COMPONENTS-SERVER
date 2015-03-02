package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KratkorocniPlanVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahKratkorocniPlanJdbc extends PoZahJdbc {
    public PoZahKratkorocniPlanJdbc() {
        setTableName("po_zah_kratkorocni_plan");
    }
    public KratkorocniPlanVo daoFindPovijest(ZahtjevPravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT TOP 1 KP.*, PK.maticni_broj  ");
		sql.append("FROM po_zah_kratkorocni_plan AS KP LEFT OUTER JOIN ");
		sql.append("po_zah_podaci_klijenta AS PK ON KP.broj_zahtjeva = PK.broj_zahtjeva LEFT OUTER JOIN ");
		sql.append("po_zah_zahtjev AS ZA ON PK.broj_zahtjeva = ZA.broj_zahtjeva ");
		sql.append("WHERE (PK.maticni_broj = ? AND KP.broj_zahtjeva <> ?)");
		sql.append("ORDER BY ZA.broj_zahtjeva DESC"); //uzimamo samo jedna red i to zadnji iz baze
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setObject(2,value.getBrojZahtjeva());
			pstmt.setMaxRows(1);
			KratkorocniPlanVo j2eers = new KratkorocniPlanVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	        
    }
    public AS2RecordList daoFind(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.po_fn_zah_kratkorocni_plan (?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }