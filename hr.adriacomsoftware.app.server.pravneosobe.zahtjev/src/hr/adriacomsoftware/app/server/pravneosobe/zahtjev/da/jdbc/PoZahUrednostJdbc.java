package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.UrednostVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class PoZahUrednostJdbc extends PoZahJdbc {
    public PoZahUrednostJdbc() {
        setTableName("po_zah_urednost");
    }
    public AS2RecordList daoFind(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder("SELECT * FROM po_zah_urednost where broj_zahtjeva = ? ");
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
    // urednost klijenta (iz rizika)
    public void daoUrednostZahtjeva(ZahtjevPravnaOsobaVo value)  {
        String sp_name = "stp_po_zah_urednost";
        J2EESqlBuilder sp = new J2EESqlBuilder();
        sp.append("{call ");
        sp.append(sp_name);
        sp.append(" (?,?,?) }");
        try{
        	CallableStatement cs = getConnection().getCallableStatement(sp.toString());
	        cs.setObject(1, value.getBrojZahtjeva());
	        cs.setObject(2, value.getMaticniBroj());
	        cs.setDate(3, value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
	        cs.execute();
	        cs.close();
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public UrednostVo daoCitajUrednost(ZahtjevPravnaOsobaVo value)  {
        UrednostVo j2eers = null;
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("select top 1 * from po_zah_urednost where broj_zahtjeva = ? ");
        try {           
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
            pstmt.setObject(1, value.getBrojZahtjeva());
            pstmt.setMaxRows(0);
            AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
            j2eers = new UrednostVo(loc_rs);
            pstmt.close();
            return j2eers;
        } catch (Exception e) {
            return j2eers;
        }
    }
}