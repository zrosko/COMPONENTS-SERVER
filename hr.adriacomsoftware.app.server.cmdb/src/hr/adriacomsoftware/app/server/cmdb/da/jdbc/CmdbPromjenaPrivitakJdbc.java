package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;

import java.sql.PreparedStatement;


public final class CmdbPromjenaPrivitakJdbc extends CMDBJdbc {
    public CmdbPromjenaPrivitakJdbc() {
        setTableName("cmdb_promjena_privitak");
    }
    public AS2RecordList daoFind(AS2Record value) {
		String sql = "select naziv_dokumenta,id_promjene,id_privitka from hd_poziv_privitak where id_promjene = ?";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql);
			pstmt.setObject(1,value.get("id_promjene"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		}catch(Exception e){
			throw new AS2Exception(e);
		}
	}
}