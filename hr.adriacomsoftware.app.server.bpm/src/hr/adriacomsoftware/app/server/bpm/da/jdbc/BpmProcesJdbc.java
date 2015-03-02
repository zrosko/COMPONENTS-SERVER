package hr.adriacomsoftware.app.server.bpm.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.datasources.J2EEDefaultJDBCService;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

//
public final class BpmProcesJdbc extends CMDBJdbc {
    public static final String SQL_PROCESI = "SELECT id_procesa AS id, rtrim(CONVERT(char(10), id_procesa)) + '-' + RTRIM(naziv_procesa) AS name FROM bpm_proces  ";
    public BpmProcesJdbc() {
        setTableName("bpm_proces");
    }
    public AS2RecordList daoFindListu(String sql, String order_by) {
        String cacheKey = "BpmProcesJdbc.daoFindListu";
        AS2RecordList cache_rs = J2EEDefaultJDBCService.getInstance().getFromCache(cacheKey);
        if (cache_rs != null)
            return cache_rs; //return from cache
        if(order_by == null)
            sql = sql +" order by id";
        else
            sql = sql + order_by;
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql);
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        //add to cache
	        J2EEDefaultJDBCService.getInstance().addToCache(cacheKey, j2eers);
	        return j2eers;
	    }catch(Exception e){
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoFind(AS2Record value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.append("select * from view_bpm_proces_pogled ORDER BY razina, prioritet DESC, vaznost DESC, id_procesa, tip ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
	    }catch(Exception e){
			throw new AS2DataAccessException(e);
		}
	}
}