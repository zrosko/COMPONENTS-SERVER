package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsKategorijaImovineVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class IsmsKategorijaImovineJdbc extends CMDBJdbc {
    public IsmsKategorijaImovineJdbc() {
        setTableName("isms_kategorija_imovine");
    }
    public AS2RecordList daoFind(IsmsKategorijaImovineVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM isms_kategorija_imovine ");
        sql.append("ORDER BY id_kategorije");  
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