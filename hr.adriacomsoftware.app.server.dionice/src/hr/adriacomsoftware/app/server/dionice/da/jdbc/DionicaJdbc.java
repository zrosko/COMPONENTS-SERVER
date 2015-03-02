package hr.adriacomsoftware.app.server.dionice.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class DionicaJdbc extends J2EEDataAccessObjectJdbc {
	public static final String LISTA_DIONICARA_SQL = "select * from bi_view_dionice_stanje where datum_stanja = ? order by oznaka_sektora,stanje desc";

	public DionicaJdbc() {
		setTableName("");
	}

	public AS2RecordList daoFindListuDionicaraNaDan(OsnovniVo value) {
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					LISTA_DIONICARA_SQL);
			pstmt.setDate(1, value.getAsSqlDate("datum"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoUsporednoStanje(OsnovniVo value) {
		String sql = "select * from dbo.bi_fn_dionice_usporedno_stanje(?,?) order by stanje desc,racun_vlasnika";
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoPronadiDionice(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT convert(char(13),convert(decimal(13,0),jmbg_mb)) as jmbg_mb,naziv_vlasnika,stanje,postotak_udjela,convert(char(10),datum_stanja,104) as datum_stanja FROM bi_dionice  ");
		sql.appLike("AND", "jmbg_mb", value.getJmbg());
		sql.appLike("AND", "naziv_vlasnika", value.getImePrezime());
		sql.appendln("ORDER BY datum_stanja desc, naziv_vlasnika");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoListaDionica(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT convert(char(13),convert(decimal(13,0),jmbg_mb)) as jmbg_mb,naziv_vlasnika,stanje,postotak_udjela,convert(char(10),datum_stanja,104) as datum_stanja FROM bi_dionice ");
		sql.appendln(" WHERE(datum_stanja =(SELECT MAX(datum_stanja) FROM bi_dionice))");
		sql.appendln(" ORDER BY datum_stanja desc, naziv_vlasnika ");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}