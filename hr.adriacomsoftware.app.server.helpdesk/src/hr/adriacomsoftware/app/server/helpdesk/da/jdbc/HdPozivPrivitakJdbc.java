package hr.adriacomsoftware.app.server.helpdesk.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class HdPozivPrivitakJdbc extends CMDBJdbc {
	public HdPozivPrivitakJdbc() {
		setTableName("hd_poziv_privitak");
	}
	public AS2RecordList daoFind(AS2Record value) {
		String sql = "select naziv_dokumenta,id_poziva,id_privitka from hd_poziv_privitak where id_poziva = ?";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("id_poziva"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}