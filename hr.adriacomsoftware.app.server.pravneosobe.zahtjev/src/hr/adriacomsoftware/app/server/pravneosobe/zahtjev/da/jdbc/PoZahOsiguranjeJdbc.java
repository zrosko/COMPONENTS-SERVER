package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.OsiguranjeVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahOsiguranjeJdbc extends PoZahJdbc {
    public PoZahOsiguranjeJdbc() {
        setTableName("po_zah_osiguranje");
    }
    public AS2RecordList daoFind(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
    	sql.append("SELECT * FROM po_zah_osiguranje where broj_zahtjeva = ? ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
            pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
            pstmt.setMaxRows(0);
            AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
            pstmt.close();
            return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsiguranjeVo daoCitajOsiguranje(ZahtjevPravnaOsobaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
    	sql.append("select top 1 * from po_zah_osiguranje where broj_zahtjeva = ? ");
        OsiguranjeVo j2eers = null;
        try {
            PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
            pstmt.setObject(1, value.getBrojZahtjeva());
            pstmt.setMaxRows(0);
            AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
            j2eers = new OsiguranjeVo(loc_rs);
            pstmt.close();
            return j2eers;
        } catch (Exception e) {
            return j2eers;
        }
    }
}