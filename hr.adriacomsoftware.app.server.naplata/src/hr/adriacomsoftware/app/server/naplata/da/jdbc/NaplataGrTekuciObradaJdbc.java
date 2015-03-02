package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktAktivnostVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciObradaRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciObradaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTNaplataJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

/************* DAO_JDBC  naplata_gr_tekuci_obrada ************/

public final class NaplataGrTekuciObradaJdbc extends TESTNaplataJdbc { 

	public NaplataGrTekuciObradaJdbc() {
		setTableName("naplata_gr_tekuci_obrada");
	}

	
	public NaplataGrTekuciObradaVo daoLoad(NaplataGrTekuciObradaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * ");
		sql.append(" FROM view_naplata_gr_tekuci_obrada ");
		sql.append(" where id_obrade = ? " );
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getIdObrade());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			NaplataGrTekuciObradaVo j2eevo = new NaplataGrTekuciObradaVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public NaplataGrTekuciObradaRs daoFindAll(NaplataGrTekuciObradaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " +
				   " FROM view_naplata_gr_tekuci_obrada ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrTekuciObradaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE naplata_gr_tekuci_obrada SET ispravno = 0 " +
				   "WHERE id_obrade = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_obrade"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
	public void daoRemoveDopis(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE naplata_gr_tekuci_dopis SET ispravno = 0 " +
				   "WHERE id_obrade = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_obrade"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
	public String daoLastIdObrade() {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select max(id_obrade) as last_id_obrade from naplata_gr_tekuci_obrada ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			KontaktAktivnostVo vo = new KontaktAktivnostVo(j2eers.getRowAt(0));
			return vo.get("last_id_obrade");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public NaplataGrTekuciObradaRs daoLoadStavke(NaplataGrTekuciObradaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " +
				   " FROM view_naplata_gr_tekuci_dopis where id_obrade = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_obrade"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrTekuciObradaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}
