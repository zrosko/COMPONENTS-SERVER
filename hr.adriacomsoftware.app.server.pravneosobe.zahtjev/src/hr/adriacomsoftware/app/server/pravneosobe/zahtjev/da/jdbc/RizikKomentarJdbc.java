package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.AS2ResultSetUtilityJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class RizikKomentarJdbc extends PoZahJdbc {
	public RizikKomentarJdbc() {
		setTableName("po_zah_rizik_komentar");
	}

	public AS2RecordList daoFind(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_po_zah_rizik_komentar_pogled where id_rizika = ? order by id_komentara, datum_komentara ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("id_rizika"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = AS2ResultSetUtilityJdbc.transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}