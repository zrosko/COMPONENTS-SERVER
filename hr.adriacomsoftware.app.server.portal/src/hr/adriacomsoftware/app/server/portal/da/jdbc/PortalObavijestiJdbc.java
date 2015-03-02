package hr.adriacomsoftware.app.server.portal.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class PortalObavijestiJdbc extends OLTPJdbc {

    public PortalObavijestiJdbc() {
        setTableName("portal_obavijesti");
    }
    
    public AS2RecordList daoFindObavijesti(AS2Record value) {
		StringBuilder sql = new StringBuilder();		
		sql.append("select * from dbo.view_portal_obavijesti order by id_obavijesti desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    
    public AS2Record daoLoadObavijest(AS2Record value) {
		StringBuilder sql = new StringBuilder();		
		sql.append("select * from dbo.view_portal_obavijesti "
				+ " where id_obavijesti = ? "
				+ " order by id_obavijesti ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("id_obavijesti"));
			AS2Record as2_vo = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return as2_vo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}