package hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrnCrnaListaDrzavaJdbc extends BankarskiJdbc {

	public PrnCrnaListaDrzavaJdbc() {
		setTableName("prn_crna_lista_drzava");
	}

	public AS2RecordList daoListajDrave(CrnaListaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_crna_lista_drzava ORDER BY naziv_liste, naziv ");
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

	public AS2RecordList daoListajDjelatnosti(CrnaListaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from prn_djelatnost order by rizicnost ");
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

	public AS2RecordList daoSwiftTransakcije(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from fn_tbs_prn_swift_transakcije_crna_lista_drzava(?,?) ");
		sql.append(" order by vrijeme_unosa desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}