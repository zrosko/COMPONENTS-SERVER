package hr.adriacomsoftware.app.server.obrtnici.da.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class KreditniProgramJdbc extends BankarskiJdbc {
    public KreditniProgramJdbc() {
        setTableName("jb_kreditni_program_novi");
    }
  
    public AS2RecordList daoFind(AS2Record aFields) {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT CONVERT(decimal(18, 0), IKP.broj_partije) as broj_partije, IKP.sifra_grupe, " +
					 "SIF.naziv_sifre as program, dbo.bi_fn_partija_vlasnik_partije(IKP.broj_partije) as naziv, " +
					 "IKP.id_kreditnog_programa  " +
					 "FROM jb_kreditni_program_novi AS IKP LEFT OUTER JOIN j2ee_sifra AS SIF " +
					 "ON IKP.sifra_grupe = SIF.sifra AND SIF.id_sifarnika IN (162, 163) ");
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
    public int daoFindCountExists(OsnovniVo value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT COUNT(*) AS brojac_partija FROM jb_kreditni_program WHERE (broj_partije = ?) AND (vrsta_programa = 'OB')");
        int counter = 1;
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(counter, value.getBrojPartije());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers.getAsInt("brojac_partija");
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }  
    public void daoRemove(OsnovniVo value) throws Exception {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("DELETE FROM jb_kreditni_program_novi WHERE id_kreditnog_programa = ?");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get("id_kreditnog_programa"));
	        pstmt.setMaxRows(0);
	        pstmt.execute();
	        pstmt.close();
		 } catch (Exception e) {
			 throw new AS2DataAccessException(e);
		 }
    }
}