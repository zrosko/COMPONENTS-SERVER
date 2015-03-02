package hr.adriacomsoftware.app.server.helpdesk.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class HdRadniNalogJdbc extends CMDBJdbc {
	public HdRadniNalogJdbc() {
		setTableName("hd_radni_nalog");
	}

	public AS2RecordList daoFind(AS2Record aFields) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_hd_radni_nalog_pogled ");
		sql.append("order by zaduzena_osoba, datum_otvaranja desc");
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