package hr.adriacomsoftware.app.server.sukl.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class SuklAktJdbc extends CMDBJdbc {
    public SuklAktJdbc() {
        setTableName("sukl_akt");
    }
    public AS2RecordList daoFind(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM *** ");
        sql.append("ORDER BY ***"); 
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