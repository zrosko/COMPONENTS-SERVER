package hr.adriacomsoftware.app.server.gradani.kreditnizahtjev.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;

import java.sql.PreparedStatement;


public final class KreditniZahtjevJdbc extends J2EEDataAccessObjectJdbc {
	public KreditniZahtjevJdbc() {
		setTableName("jb_gr_kreditni_zahtjev");
	}

	public AS2RecordList daoLoadSifra(AS2Record value) {
		String sql = "select * from jb_gr_kreditni_zahtjev where sifra_zahtjeva = ?";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("sifra_zahtjeva"));
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}