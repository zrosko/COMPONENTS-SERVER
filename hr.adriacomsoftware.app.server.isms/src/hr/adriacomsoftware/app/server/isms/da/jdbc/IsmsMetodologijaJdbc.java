package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.datasources.J2EEDefaultJDBCService;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class IsmsMetodologijaJdbc extends CMDBJdbc {
    public static final String SQL_POVJERLJIVOST = "SELECT stupanj AS id, CONVERT(char(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_povjerljivost ";
    public static final String SQL_CJELOVITOST = "SELECT stupanj AS id, CONVERT(char(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_cjelovitost ";
    public static final String SQL_RASPOLOZIVOST = "SELECT stupanj AS id, CONVERT(char(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_raspolozivost ";
    public static final String SQL_VAZNOST = "SELECT stupanj AS id, CONVERT(char(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_vaznost ";
    public static final String SQL_VJEROJATNOST_PRIJETNJE = "SELECT stupanj AS id, CONVERT(char(3), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_vjerojatnost_prijetnje ";
    public static final String SQL_PRIJETNJA_VRSTA = "SELECT vrsta_prijetnje AS id, CONVERT(char(3), vrsta_prijetnje) + ' - ' + RTRIM(naziv) AS name FROM isms_prijetnja_vrsta ";
    public static final String SQL_KATEGORIJA_IMOVINE = "SELECT RTRIM(id_kategorije) AS id, RTRIM(id_kategorije) + ' - ' + RTRIM(naziv) AS name, id_kategorije, naziv "+
														" FROM isms_kategorija_imovine WHERE  ({ fn LENGTH(id_kategorije) } > 2) ";
    public static final String SQL_KATEGORIJA_IMOVINE_KRATKO = "SELECT RTRIM(id_kategorije) AS id, RTRIM(id_kategorije) + ' - ' + RTRIM(naziv) AS name, id_kategorije, naziv "+
        												" FROM isms_kategorija_imovine WHERE  ({ fn LENGTH(id_kategorije) } = 2) ";
    public static final String SQL_LOKACIJE = "SELECT L.id_lokacije AS id, RTRIM(CONVERT(char(5), L.id_lokacije)) + ' - ' + isnull(L.kat,'') + ' kat soba ' "+
	  "+ isnull(L.oznaka_sobe,'')+ ' '+ RTRIM(isnull(L.naziv,'')) AS name, L.id_zgrade FROM isms_lokacija AS L LEFT OUTER JOIN isms_zgrada AS Z ON Z.id_zgrade = L.id_zgrade ";

    public static final String SQL_RANJIVOSTI = "SELECT id_ranjivosti as id, convert(varchar(4),id_ranjivosti)+' - '+RTRIM(naziv) as name FROM isms_ranjivost ";
    public static final String SQL_PRIJETNJE = "SELECT id_prijetnje as id, CONVERT(varchar(4),id_prijetnje)+' - '+RTRIM(naziv) as name FROM isms_prijetnja ";
    public static final String SQL_KONTROLE = "SELECT id_kontrole AS id, CONVERT(varchar(4), id_kontrole) + ' - ' + RTRIM(oznaka) + ' ' + RTRIM(naziv) AS name FROM isms_kontrola ";
    public static final String SQL_ZGRADE = "SELECT id_zgrade AS id, rtrim(CONVERT(varchar(10), id_zgrade)) + ' - ' + RTRIM(naziv) AS name FROM isms_zgrada ";
    public static final String SQL_RANJIVOST_VRSTA = "SELECT vrsta_ranjivosti AS id, CONVERT(varchar(3), vrsta_ranjivosti) + ' - ' + RTRIM(naziv) AS name FROM isms_ranjivost_vrsta ";
    public static final String SQL_POSLJEDICA_RANJIVOSTI = "SELECT stupanj AS id, CONVERT(varchar(3), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_posljedica_ranjivosti ";
    public static final String SQL_VJEROJATNOST_OTKRIVANJA = "SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_vjerojatnost_otkrivanja ";
    public static final String SQL_ODGOVORNOST = "SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_odgovornost ";
    public static final String SQL_DOKAZIVOST = "SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM isms_dokazivost ";
    
    public IsmsMetodologijaJdbc() {
        //setTableName("isms");
    }
    public AS2RecordList daoFindListu(J2EESqlBuilder sql, String order_by) {
    	return daoFindListu(sql.toString(),order_by);
    }
    public AS2RecordList daoFindListu(String sql, String order_by) {
        String cacheKey = "IsmsMetodologijaJdbc.daoFindListu";
        AS2RecordList cache_rs = J2EEDefaultJDBCService.getInstance().getFromCache(cacheKey);
        if (cache_rs != null)
            return cache_rs; //return from cache
        if(order_by == null)
            sql=sql +" order by id";
        else
            sql=sql + order_by;
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        //add to cache
	        J2EEDefaultJDBCService.getInstance().addToCache(cacheKey, j2eers);
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}