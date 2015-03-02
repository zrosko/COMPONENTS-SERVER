package hr.adriacomsoftware.app.server.dionice.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class JavnaPonudaJdbc extends OLTPJdbc {

	public JavnaPonudaJdbc() {
		setTableName("dio_javna_ponuda");
	}

	public AS2RecordList daoFindUpisnice(AS2Record value) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from dio_javna_ponuda ");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}