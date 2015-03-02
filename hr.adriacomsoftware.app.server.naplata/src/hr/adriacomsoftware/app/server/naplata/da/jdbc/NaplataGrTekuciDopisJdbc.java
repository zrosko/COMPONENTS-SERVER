package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciDopisRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciDopisVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTNaplataJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

/************* DAO_JDBC  naplata_gr_tekuci_dopis ************/


public final class NaplataGrTekuciDopisJdbc extends TESTNaplataJdbc { 
	public NaplataGrTekuciDopisJdbc () {
		setTableName("naplata_gr_tekuci_dopis");
	}
	
	public NaplataGrTekuciDopisVo daoLoad(NaplataGrTekuciDopisVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * ");
		sql.append(" FROM view_naplata_gr_tekuci_dopis ");
		sql.append(" where id_dopisa = ? " );
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getIdDopisa());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			NaplataGrTekuciDopisVo j2eevo = new NaplataGrTekuciDopisVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public NaplataGrTekuciDopisRs daoFindAll(NaplataGrTekuciDopisVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " +
				   " FROM view_naplata_gr_tekuci_dopis " +
				   " where id_pracenja = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getIdPracenja());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrTekuciDopisRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE naplata_gr_tekuci_dopis SET ispravno = 0 " +
				   "WHERE id_dopisa = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_dopisa"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}
