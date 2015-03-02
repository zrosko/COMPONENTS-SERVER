package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbKvarVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class CmdbKvarJdbc extends CMDBJdbc {
	public CmdbKvarJdbc() {
		setTableName("cmdb_kvar");
	}

	public AS2RecordList daoFind(AS2Record value) {
		String sql = "SELECT CONVERT(char(10), datum_prijave, 104) AS datum_prijave_, naziv, datum_prijave, osoba_prijave, "
				+ "opis_kvara, id_pozvia, id_kvara, id_imovine FROM "
				+ getTableName() + " where id_imovine = ? ";
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql);
			pstmt.setObject(1, value.get(CmdbKvarVo.CMDB_KVAR__ID_IMOVINE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}