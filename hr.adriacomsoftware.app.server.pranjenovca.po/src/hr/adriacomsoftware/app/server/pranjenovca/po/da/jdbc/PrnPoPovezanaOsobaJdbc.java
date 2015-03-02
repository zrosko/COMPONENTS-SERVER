package hr.adriacomsoftware.app.server.pranjenovca.po.da.jdbc;

import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPoPovezanaOsobaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrnPoPovezanaOsobaJdbc extends J2EEDataAccessObjectJdbc {
	public PrnPoPovezanaOsobaJdbc() {
		setTableName("prn_po_povezana_osoba");
	}

	public AS2RecordList daoFind(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbs_prn_po_povezana_osoba(?,?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get(PrnPoPovezanaOsobaVo.PRN_PO_POVEZANA_OSOBA__ID_UPITNIKA));
			pstmt.setObject(2, value.get(PrnPoPovezanaOsobaVo.PRN_PO_POVEZANA_OSOBA__VRSTA_UNOSA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}