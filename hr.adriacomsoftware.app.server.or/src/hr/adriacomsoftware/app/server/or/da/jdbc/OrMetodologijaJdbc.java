package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.datasources.J2EEDefaultJDBCService;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class OrMetodologijaJdbc extends CMDBJdbc {
	//ocjena
    public static final J2EESqlBuilder SQL_OCJENA_IZNOS = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'iznos' ");
    public static final J2EESqlBuilder SQL_OCJENA_UCESTALOST = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'ucestalost' ");
    public static final J2EESqlBuilder SQL_OCJENA_UCINAK = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'ucinak' ");
    public static final J2EESqlBuilder SQL_OCJENA_VISINA_RIZIKA = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'visina_rizika' ");
    public static final J2EESqlBuilder SQL_OCJENA_STATUS_OPERATIVNOG_RIZIKA = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'status_operativnog_rizika' ");
    public static final J2EESqlBuilder SQL_OCJENA_STATUS_DOGADAJA = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'status_dogadaja' ");
    public static final J2EESqlBuilder SQL_OCJENA_VISINA_GUBITKA = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'visina_gubitka' ");
    //ocjene eksternalizacija
    public static final J2EESqlBuilder SQL_OCJENA__EKST_EKONOM_FINANCIJSKA = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'ekst_ekonom_financijska' ");
    public static final J2EESqlBuilder SQL_OCJENA__EKST_ZAPOSLENICI = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'ekst_zapslenici' ");
    public static final J2EESqlBuilder SQL_OCJENA__EKST_RIZIK = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'ekst_rizik' ");
    public static final J2EESqlBuilder SQL_OCJENA__EKST_USLUGA = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'ekst_usluga' ");
    public static final J2EESqlBuilder SQL_OCJENA__EKST_UGOVOR = new J2EESqlBuilder("SELECT stupanj AS id, CONVERT(varchar(2), stupanj) + ' - ' + RTRIM(naziv) AS name FROM or_ocjena where vrsta = 'ekst_ugovor' ");

    //ostalo
    public static final J2EESqlBuilder SQL_PROIZVOD = new J2EESqlBuilder("SELECT DISTINCT proizvod as id, rtrim(CONVERT(varchar(20), proizvod)) + ' - ' + RTRIM(naziv) AS name FROM BI_PROD.dbo.bi_proizvod_katalog");
    public static final J2EESqlBuilder SQL_OR_FUNKCIJE = new J2EESqlBuilder("SELECT DISTINCT kategorija as id, rtrim(CONVERT(varchar(20), kategorija)) + ' - ' + RTRIM(naziv) AS name FROM or_poslovna_funkcija ");
    public static final J2EESqlBuilder SQL_OR_LINIJE = new J2EESqlBuilder("SELECT DISTINCT id_poslovne_linije AS id, rtrim(CONVERT(varchar(10), id_poslovne_linije)) + ' - ' + RTRIM(naziv) AS name FROM or_poslovna_linija");
    
    public OrMetodologijaJdbc() {
        setTableName("or_");
    }
    public AS2RecordList daoFindListu(String sql, String order_by)  {
        String cacheKey = "OrMetodologijaJdbc.daoFindListu";
        AS2RecordList cache_rs = J2EEDefaultJDBCService.getInstance().getFromCache(cacheKey);
        if (cache_rs != null)
            return cache_rs; //return from cache
        if(order_by == null)
            sql = sql +" order by id";
        else
            sql = sql + order_by;
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