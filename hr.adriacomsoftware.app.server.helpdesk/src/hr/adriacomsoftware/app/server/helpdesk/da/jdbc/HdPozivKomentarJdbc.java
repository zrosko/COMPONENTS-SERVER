package hr.adriacomsoftware.app.server.helpdesk.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.AS2ResultSetUtilityJdbc;

import java.sql.PreparedStatement;


public final class HdPozivKomentarJdbc extends CMDBJdbc {
	public HdPozivKomentarJdbc() {
		setTableName("hd_poziv_komentar");
	}

	public AS2RecordList daoFind(AS2Record value) {
		String sql = "select * from view_hd_komentar_pogled where id_poziva = ? order by id_komentara, datum_komentara ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("id_poziva"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = AS2ResultSetUtilityJdbc.transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}