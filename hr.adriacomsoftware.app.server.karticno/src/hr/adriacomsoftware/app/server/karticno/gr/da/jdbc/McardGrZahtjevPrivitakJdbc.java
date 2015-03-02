package hr.adriacomsoftware.app.server.karticno.gr.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.TESTJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class McardGrZahtjevPrivitakJdbc extends TESTJdbc {//TODO vrati OLTP
	public McardGrZahtjevPrivitakJdbc() {
		setTableName("mcard_gr_zahtjev_privitak");
	}
    public AS2RecordList daoFind(AS2Record value) {
		String sql = "select naziv_dokumenta,broj_zahtjeva,id_privitka,tip_dokumenta from view_mcard_gr_zahtjev_privitak where broj_zahtjeva = ?";
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