package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class CmdbImovinaVezaJdbc extends CMDBJdbc {
    public CmdbImovinaVezaJdbc() {
        setTableName("cmdb_imovina_veza");
    }
    public AS2RecordList daoFind(AS2Record value) {
		String sql = "select id_imovine,id_imovine_veza, CONVERT(char(10), datum, 104) AS datum_,datum, id where id_imovine = ?";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("id_imovine"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}