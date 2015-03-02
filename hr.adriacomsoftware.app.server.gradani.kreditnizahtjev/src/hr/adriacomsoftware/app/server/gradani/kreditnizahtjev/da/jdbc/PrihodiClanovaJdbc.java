package hr.adriacomsoftware.app.server.gradani.kreditnizahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;


public final class PrihodiClanovaJdbc extends J2EEDataAccessObjectJdbc {
	public PrihodiClanovaJdbc() {
		setTableName("jb_gr_prihodi_clanova");
	}

	public void daoRemovePc(AS2Record value) throws Exception {
		J2EEConnectionJDBC co = null;
		int counter = 1;
		co = getConnection();
		Connection jco = co.getJdbcConnection();
		PreparedStatement pstmt = jco
				.prepareStatement("delete FROM jb_gr_prihodi_clanova WHERE id_zahtjeva = ? and id_osobe = ?");
		pstmt.setBigDecimal(
				counter,
				value.getAsBigDecimal(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV__ID_ZAHTJEVA));
		counter++;
		pstmt.setInt(counter, value.getAsInt(
				JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV_OSOBA__ID_OSOBE, 0));
		pstmt.executeUpdate();
		pstmt.close();
	}
}