package hr.adriacomsoftware.app.server.mp.po.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.mp.po.dto.MpPoPrometRs;
import hr.adriacomsoftware.app.common.mp.po.dto.MpPoPrometVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class MpPoPrometJdbc extends TESTJdbc {
	public MpPoPrometJdbc() {
		setTableName("bi_mp_po_promet");
	}

	public MpPoPrometRs daoFindPronadiPromete(MpPoPrometVo value) {// TODO
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " + " from view_bi_mp_po_promet ");
		sql.appGreatherOrEqual("AND", "datum_knjizenja", value.get("datum_od1"));
		sql.appLessOrEqual("AND", "datum_knjizenja", value.get("datum_do1"));
		sql.appEqual("AND", "datum_knjizenja", value.get("datum_knjizenja"));
		sql.appEqualNoQuote("AND", "ispravno", value.get("ispravno"));
		sql.appEqualNoQuote("AND", "valuta_racuna", value.get("valuta"));

		if (value.get("@novo").equals("true"))
			sql.appEqual("AND", "operater_zadnje_izmjene",
					value.get("operater_zadnje_izmjene"));
		sql.append(" order by datum_knjizenja desc, id_prometa desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpPoPrometRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpPoPrometRs daoFindListajKnjizenja(MpPoPrometVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select  top 100 * "
				+ " from view_bi_mp_po_knjizenja_pogled "
				+ " order by datum_knjizenja desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpPoPrometRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpPoPrometRs daoFindPronadiKnjizenja(MpPoPrometVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select top 100 * "
				+ " from view_bi_mp_po_knjizenja_pogled "
				+ " order by datum_knjizenja desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpPoPrometRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpPoPrometRs daoFindPronadiPrometeKratki(MpPoPrometVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select id_prometa, " + value.get("ispravno")
				+ " as ispravno "
				+ " from view_bi_mp_po_promet where ispravno = 2 ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appEqual("AND", "operater_zadnje_izmjene",
				value.get("operater_zadnje_izmjene"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new MpPoPrometRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpPoPrometVo daoProvjeriVrstuIznosa(MpPoPrometVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select TOP 1 opis as opis_prometa "
				+ " from bi_mp_vrsta_prometa ");
		sql.appEqualNoQuote("AND", "vrsta_prometa", value.get("vrsta_prometa"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			MpPoPrometVo j2eevo = new MpPoPrometVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public MpPoPrometRs daoIzvjestaji(MpPoPrometVo value) {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_mp_po_knjizenja_facade");
		sp.append(" (?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(4, value.get("@@report_selected"));
			cs.setObject(5, value.get("broj_partije"));
			cs.setObject(6, value.get("broj_konta"));
			MpPoPrometRs j2eers = new MpPoPrometRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}