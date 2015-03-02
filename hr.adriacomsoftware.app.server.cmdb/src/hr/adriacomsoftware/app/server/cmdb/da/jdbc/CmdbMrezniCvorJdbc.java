package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class CmdbMrezniCvorJdbc extends CMDBJdbc {
	public CmdbMrezniCvorJdbc() {
		setTableName("cmdb_mrezni_cvor");
	}

	public AS2RecordList daoFind(AS2Record value) {
		String sql = "select oznaka, id_lokacije, korisnik, ip_adresa_privatna, komentar, id_cvora FROM "
				+ getTableName();
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql);
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}