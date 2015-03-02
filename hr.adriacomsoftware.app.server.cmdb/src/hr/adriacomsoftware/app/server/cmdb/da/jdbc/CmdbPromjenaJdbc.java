package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbPromjenaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbPromjenaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class CmdbPromjenaJdbc extends CMDBJdbc {
    public CmdbPromjenaJdbc() {
        setTableName("cmdb_promjena");
    }
    public AS2RecordList daoFind(AS2Record value) {
		String sql = "SELECT CONVERT(char(10), datum, 104) AS datum_, naziv, zaduzena_osoba, id_poziva, id_radnog_naloga, "+
		"vrsta_dogadaja, rok_provedbe, inacica, opis, izvrseni_radovi, utroseni_materijal, zapazanja, status, id_promjene, "+
		"id_imovine, datum, CONVERT(decimal(13, 0), zaduzena_osoba) AS zaduzena_osoba FROM "+getTableName()+" where id_imovine = ? ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql);
			pstmt.setObject(1,value.get(CmdbPromjenaVo.CMDB_PROMJENA__ID_IMOVINE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		 } catch (Exception e) {
			 throw new AS2DataAccessException(e);
		 }
	}
    public CmdbPromjenaRs daoListaPromjena(CmdbPromjenaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_cmdb_promjene_pogled  ");
        sql.append(" ORDER BY datum desc ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        CmdbPromjenaRs j2eers = new CmdbPromjenaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
        	throw new AS2DataAccessException(e);
		}
    }
}