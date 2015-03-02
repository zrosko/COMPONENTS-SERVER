package hr.adriacomsoftware.app.server.gk.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class TemeljnicaJdbc extends J2EEDataAccessObjectJdbc {
	// poziv funkcije za izradu temeljnice (OPOMENE, FAKTORING, itd.)
	public AS2RecordList daoTemeljnica(AS2Record value, String vrsta) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from dbo.fn_tbl_gk_temeljnica(?,?) ");
		sql.appendln("order by sort_oznaka, broj_konta, ABS(iznos), duguje_potrazuje");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setString(1, vrsta);
			pstmt.setDate(2, value.getAsSqlDate("datum"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}