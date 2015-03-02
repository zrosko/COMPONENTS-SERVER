package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciBiljeskaRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciBiljeskaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTNaplataJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

/************* DAO_JDBC  naplata_gr_tekuci_Biljeska ************/


public final class NaplataGrTekuciBiljeskaJdbc extends TESTNaplataJdbc { 
	public NaplataGrTekuciBiljeskaJdbc () {
		setTableName("naplata_gr_tekuci_biljeska");
	}
	
	public NaplataGrTekuciBiljeskaVo daoLoad(NaplataGrTekuciBiljeskaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * ");
		sql.append(" FROM view_naplata_gr_tekuci_biljeska ");
		sql.append(" where id_biljeske = ? " );
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getIdBiljeske());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			NaplataGrTekuciBiljeskaVo j2eevo = new NaplataGrTekuciBiljeskaVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public NaplataGrTekuciBiljeskaRs daoFindAll(NaplataGrTekuciBiljeskaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " +
				   " FROM view_naplata_gr_tekuci_biljeska " +
				   " where id_pracenja = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getIdPracenja());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrTekuciBiljeskaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE naplata_gr_tekuci_biljeska SET ispravno = 0 " +
				   "WHERE id_biljeske = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_biljeske"));
			pstmt.setMaxRows(0);
		    pstmt.executeUpdate();
		    pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}
