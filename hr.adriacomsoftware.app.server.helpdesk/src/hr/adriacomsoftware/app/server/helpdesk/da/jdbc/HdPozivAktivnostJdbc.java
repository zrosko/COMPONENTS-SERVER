package hr.adriacomsoftware.app.server.helpdesk.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class HdPozivAktivnostJdbc extends CMDBJdbc {
	public HdPozivAktivnostJdbc() {
		setTableName("hd_radni_nalog_aktivnost");
	}

	public AS2RecordList daoFind(AS2Record value) {
		String sql = "select * from view_hd_aktivnost_pogled where id_radnog_naloga = ?";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("id_radnog_naloga"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}