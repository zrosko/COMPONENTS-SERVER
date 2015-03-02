package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahPrivitakJdbc extends PoZahJdbc {
    public PoZahPrivitakJdbc() {
        setTableName("po_zah_privitak");
    }
    public AS2RecordList daoFind(AS2Record value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
    	sql.append("select naziv_dokumenta,broj_zahtjeva,id_privitka from po_zah_privitak where broj_zahtjeva = ?");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("broj_zahtjeva"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}