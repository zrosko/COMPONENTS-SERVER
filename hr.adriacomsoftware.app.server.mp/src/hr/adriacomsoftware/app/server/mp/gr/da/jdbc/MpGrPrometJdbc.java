package hr.adriacomsoftware.app.server.mp.gr.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrPrometRs;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrPrometVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public class MpGrPrometJdbc extends TESTJdbc {
	public MpGrPrometJdbc() {
		setTableName("bi_mp_gr_promet");
	}

	public MpGrPrometRs daoFindPronadiPromete(MpGrPrometVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " + " from view_bi_mp_gr_promet ");
		sql.appGreatherOrEqual("AND", "datum_prometa", value.get("datum_od1"));
		sql.appLessOrEqual("AND", "datum_prometa", value.get("datum_do1"));
		sql.appEqual("AND", "datum_prometa", value.get("datum_prometa"));
		sql.appEqualNoQuote("AND", "ispravno", value.get("ispravno"));
		sql.appEqualNoQuote("AND", "valuta_racuna", value.get("valuta"));

		if (value.get("@novo").equals("true"))
			sql.appEqual("AND", "operater_zadnje_izmjene",
					value.get("operater_zadnje_izmjene"));
		sql.append(" order by datum_prometa desc, id_prometa desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpGrPrometRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGrPrometRs daoFindListajKnjizenja(MpGrPrometVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select  top 100 * "
				+ " from view_bi_mp_gr_knjizenja_pogled "
				+ " order by datum_prometa desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpGrPrometRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGrPrometRs daoFindPronadiKnjizenja(MpGrPrometVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select top 100 * "
				+ " from view_bi_mp_gr_knjizenja_pogled "
				+ " order by datum_prometa desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpGrPrometRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGrPrometRs daoFindPronadiPrometeKratki(MpGrPrometVo value){
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select id_prometa, " + value.get("ispravno")
				+ " as ispravno "
				+ " from view_bi_mp_gr_promet where ispravno = 2 ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appEqual("AND", "operater_zadnje_izmjene",
				value.get("operater_zadnje_izmjene"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpGrPrometRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGrPrometVo daoProvjeriVrstuIznosa(MpGrPrometVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select TOP 1 opis as opis_prometa "
				+ " from bi_mp_vrsta_prometa ");
		sql.appEqualNoQuote("AND", "vrsta_prometa", value.get("vrsta_prometa"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			MpGrPrometVo j2eevo = new MpGrPrometVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGrPrometRs daoIzvjestaji(MpGrPrometVo value) {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_mp_gr_knjizenja_facade");
		sp.append(" (?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(4, value.get("@@report_selected"));
			cs.setObject(5, value.get("broj_partije"));
			cs.setObject(6, value.get("broj_konta"));
			MpGrPrometRs j2eers = new MpGrPrometRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGrPrometRs daoKnjizenje(MpGrPrometVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_mp_gr_knjizenje");
		sp.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(1, value.get("operater_zadnje_izmjene"));
			MpGrPrometRs j2eers = new MpGrPrometRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

}