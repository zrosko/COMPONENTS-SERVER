package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaKorisnikVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class CmdbImovinaKorisnikJdbc extends CMDBJdbc {
	public CmdbImovinaKorisnikJdbc() {
		setTableName("cmdb_imovina_korisnik");
	}

	public AS2RecordList daoFind(AS2Record value) {
		String sql = "select CONVERT(decimal(13, 0),jmbg) AS jmbg, organizacijska_jedinica,"
				+ "id_imovine, id FROM "
				+ getTableName()
				+ " where id_imovine = ? ";
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setObject(1,value.get(CmdbImovinaKorisnikVo.CMDB_IMOVINA_KORISNIK__ID_IMOVINE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}