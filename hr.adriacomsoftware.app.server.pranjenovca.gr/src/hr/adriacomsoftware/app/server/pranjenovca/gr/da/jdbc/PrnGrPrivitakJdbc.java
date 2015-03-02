package hr.adriacomsoftware.app.server.pranjenovca.gr.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrnGrPrivitakJdbc extends J2EEDataAccessObjectJdbc {
    public PrnGrPrivitakJdbc() {
        setTableName("prn_gr_privitak");
    }
    public AS2RecordList daoFind(AS2Record value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select naziv_dokumenta,id_privitka,id_upitnika from prn_gr_privitak where id_upitnika = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("id_upitnika"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}