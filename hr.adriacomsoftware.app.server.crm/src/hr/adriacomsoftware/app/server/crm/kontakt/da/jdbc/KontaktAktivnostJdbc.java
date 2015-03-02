package hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc;

import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktAktivnostRs;
import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktAktivnostVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class KontaktAktivnostJdbc extends KontaktJdbc {

	public KontaktAktivnostJdbc() {
		setTableName("kontakt_aktivnost");
	}

	public KontaktAktivnostRs daoFindListuAktivnosti(KontaktAktivnostVo value) {
		StringBuffer sp = new StringBuffer();
		int counter = 0;
		sp.append("{call ");
		sp.append("stp_crm_kontakt_aktivnost");
		sp.append(" (?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(++counter, value.getKategorija());
			cs.setDate(++counter, value.getAsSqlDate("datum_otvaranja"));
			cs.setDate(++counter, value.getAsSqlDate("datum_zatvaranja"));
			cs.setObject(++counter, value.getIdPredmeta());
			cs.setObject(++counter, value.getIdAktivnosti());
			KontaktAktivnostRs j2eers = new KontaktAktivnostRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public void daoRemove(KontaktAktivnostVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE dbo.kontakt_aktivnost SET ispravno = 0 " +
				   "WHERE id_aktivnosti = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("id_aktivnosti"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
	public String daoZadnjiIdAktivnosti(KontaktAktivnostVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select id_aktivnosti from dbo.kontakt_aktivnost ");
		sql.append(" where id_temp = '"+value.get("id_temp")+"' and isnull(ispravno,1)=1 ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			KontaktAktivnostVo vo = new KontaktAktivnostVo(j2eers.getRowAt(0));
			return vo.get("id_aktivnosti");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

}