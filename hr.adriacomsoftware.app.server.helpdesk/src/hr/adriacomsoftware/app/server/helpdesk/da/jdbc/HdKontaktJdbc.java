package hr.adriacomsoftware.app.server.helpdesk.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class HdKontaktJdbc extends CMDBJdbc {
	public static String _FIND = "SELECT *  FROM view_hd_kontakt_pogled ";

	public HdKontaktJdbc() {
		setTableName("hd_kontakt");
	}

	public AS2RecordList daoFind(AS2Record value) {
		String sql = _FIND + " order by naziv";
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}