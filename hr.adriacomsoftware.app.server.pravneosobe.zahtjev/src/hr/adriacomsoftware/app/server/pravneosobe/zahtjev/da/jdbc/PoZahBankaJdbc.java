package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahBankaJdbc extends PoZahJdbc {
	public PoZahBankaJdbc() {
		setTableName("po_zah_banka");
	}

	public AS2RecordList daoFind(AS2Record aFields) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select CONVERT(char(10), datum_stanja,104) as datum, jamstveni_kapital, "
				+ "broj_dionica, cijena_dionice, datum_stanja FROM "
				+ getTableName() + " ORDER BY datum_stanja desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}