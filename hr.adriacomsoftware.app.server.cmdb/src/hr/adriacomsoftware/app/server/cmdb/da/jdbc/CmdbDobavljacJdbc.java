package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;

public final class CmdbDobavljacJdbc extends CMDBJdbc {
	public CmdbDobavljacJdbc() {
		setTableName("cmdb_dobavljac");
	}

	public AS2RecordList daoFind(AS2Record aFields) {
		String sql = "select naziv, ulica, mjesto, postanski_broj, drzava, telefon, fax, email, kontakt_osoba, "
				+ "serivser, broj_ugovora_odrzavanja, status,id_dobavljaca FROM "
				+ getTableName();
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