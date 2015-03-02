package hr.adriacomsoftware.app.server.gradani.stednja.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class GradaniObavijestJdbc extends J2EEDataAccessObjectJdbc {
	public GradaniObavijestJdbc() {
		setTableName("bi_gr_devize_obavijest_doznake_iskljucene_partije");
	}

	public AS2RecordList daoFind(AS2Record aFields) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select CONVERT(decimal(18, 0), broj_partije) as broj_partije, CONVERT(char(10), datum_od, 104)"
				+ "as od_datuma, dbo.bi_fn_gr_osoba_ime_prezime(broj_partije) as ime_prezime, datum_od, id from bi_gr_devize_obavijest_doznake_iskljucene_partije order by broj_partije");
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

	public OsnovniRs daoListaObavijesti(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from bi_fn_gr_devize_obavijest_doznake(?,?) ");
		sql.appendln(" ORDER BY broj_partije, iznos_valuta desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoListaMirovine(OsnovniVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("bi_gr_devize_obavijest_mirovine");
		sp.append(" (?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}