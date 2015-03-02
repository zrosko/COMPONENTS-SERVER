package hr.adriacomsoftware.app.server.mp.gk.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.mp.gk.dto.MpGkKnjizenjaRs;
import hr.adriacomsoftware.app.common.mp.gk.dto.MpGkKnjizenjaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class MpGkKnjizenjaJdbc extends TESTJdbc {
	public MpGkKnjizenjaJdbc() {
		setTableName("bi_mp_glavna_knjiga");
	}

	public MpGkKnjizenjaRs daoFindPronadiKnjizenja(MpGkKnjizenjaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " + " from view_bi_mp_gk_knjizenja ");
		sql.appGreatherOrEqual("AND", "datum_knjizenja", value.get("datum_od1"));
		sql.appLessOrEqual("AND", "datum_knjizenja", value.get("datum_do1"));
		sql.appEqualNoQuote("AND", "ispravno", value.get("ispravno"));

		if (value.get("@novo").equals("true"))
			sql.appEqual("AND", "operater_zadnje_izmjene",
					value.get("operater_zadnje_izmjene"));

		sql.append(" order by datum_knjizenja desc, id_glavni desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpGkKnjizenjaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGkKnjizenjaRs daoFindListajKnjizenja(MpGkKnjizenjaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select  top 100 * " + " from view_bi_mp_gk_knjizenja " + // view_bi_mp_gk_knjizenja_pogled
				" order by datum_knjizenja desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpGkKnjizenjaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGkKnjizenjaRs daoFindPronadiKnjizenjaPogled(MpGkKnjizenjaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select top 100 * " + " from view_bi_mp_gk_knjizenja " + // view_bi_mp_gk_knjizenja_pogled
				" order by datum_knjizenja desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpGkKnjizenjaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGkKnjizenjaRs daoFindPronadiPrometeKratki(MpGkKnjizenjaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select id_glavni, " + value.get("ispravno")
				+ " as ispravno "
				+ " from view_bi_mp_gk_knjizenja where ispravno = 2 ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appEqual("AND", "operater_zadnje_izmjene",
				value.get("operater_zadnje_izmjene"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpGkKnjizenjaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGkKnjizenjaVo daoProvjeriVrstuIznosa(MpGkKnjizenjaVo value) {
		MpGkKnjizenjaVo j2eevo = null;
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select TOP 1 opis as opis_prometa "
				+ " from bi_mp_vrsta_prometa ");
		sql.appEqualNoQuote("AND", "vrsta_prometa", value.get("vrsta_prometa"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			j2eevo = new MpGkKnjizenjaVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpGkKnjizenjaRs daoIzvjestaji(MpGkKnjizenjaVo value) {
		MpGkKnjizenjaRs j2eers = null;
		StringBuffer sp = new StringBuffer();
		int counter = 0;
		sp.append("{call ");
		sp.append("stp_mp_gk_knjizenja_facade");
		sp.append(" (?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(++counter, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(++counter, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(++counter, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(++counter, value.get("@@report_selected"));
			cs.setObject(++counter, value.get("broj_konta"));
			j2eers = new MpGkKnjizenjaRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

}