package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaDodatakVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class CmdbImovinaDodatakJdbc extends CMDBJdbc {
	public CmdbImovinaDodatakJdbc() {
		setTableName("cmdb_imovina_dodatak");
	}

	public AS2RecordList daoFind(AS2Record value) {
		String sql = "select naziv, datum_porijekla, status, id_dodatka, id_imovine "
				+ "FROM " + getTableName() + " where id_imovine = ? ";
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setObject(1, value.get(CmdbImovinaDodatakVo.CMDB_IMOVINA_DODATAK__ID_IMOVINE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}