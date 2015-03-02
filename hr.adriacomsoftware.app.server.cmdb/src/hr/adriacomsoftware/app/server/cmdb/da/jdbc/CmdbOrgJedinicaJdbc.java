package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbOrgJedinicaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class CmdbOrgJedinicaJdbc extends CMDBJdbc {
    public static final String POSLOVNICA_SQL = "select organizacijska_jedinica AS id, rtrim(convert(char(20),organizacijska_jedinica))+'-'+rtrim(naziv) AS name from cmdb_org_jedinica ";
    public CmdbOrgJedinicaJdbc() {
        setTableName("cmdb_org_jedinica");
    }
    public AS2RecordList daoFind(CmdbOrgJedinicaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM cmdb_org_jedinica ");
        sql.append("ORDER BY organizacijska_jedinica");  
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
    public AS2RecordList daoFindListu(String sql) {
        return null;
    }
}