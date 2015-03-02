package hr.adriacomsoftware.app.server.services.da.jdbc;

import hr.adriacomsoftware.app.server.services.J2EECMDBJDBCService;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.datasources.J2EEDefaultJDBCService;
import hr.as2.inf.server.da.jdbc.AS2ResultSetUtilityJdbc;

import java.sql.PreparedStatement;


public class CMDBJdbc extends BankarskiJdbc {
    public static final String SQL_KATEGORIJA_IMOVINE = "SELECT RTRIM(id_kategorije) AS id, RTRIM(id_kategorije) + ' - ' + RTRIM(naziv) AS name, id_kategorije, naziv"
            + " FROM cmdb_kategorija_imovine";
	public static final String SOFTWARE_SQL = "select id_imovine AS id, RTRIM(naziv)+'-'+convert(varchar(20),id_imovine) AS name, jedinstvena_oznaka "+ 
	"from cmdb_imovina where vrsta = 'S' and id_kategorije = 2002";
    public CMDBJdbc() {
        //setTableName("isms");
    }
    public J2EEConnectionJDBC getConnection() {
        try {
            return (J2EEConnectionJDBC) J2EECMDBJDBCService.getInstance().getConnection();
        } catch (Exception e) {
            //TODO aspect J2EETrace.trace(J2EETrace.E, e);
        }
        return null;
    }

    public AS2RecordList daoFindListu(String sql, String order_by) {
        String cacheKey = "CMDBJdbc.daoFindListu";
        AS2RecordList cache_rs = J2EEDefaultJDBCService.getInstance().getFromCache(cacheKey);
        if (cache_rs != null)
            return cache_rs; //return from cache
        if (order_by == null)
            sql = sql + " order by id";
        else
            sql = sql + order_by;
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = AS2ResultSetUtilityJdbc.transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        //add to cache
	        J2EEDefaultJDBCService.getInstance().addToCache(cacheKey, j2eers);
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
}