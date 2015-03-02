package hr.adriacomsoftware.app.server.pranjenovca.po.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPravnaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class PrnPoUpitnikJdbc extends BankarskiJdbc {

	public PrnPoUpitnikJdbc() {
		setTableName("prn_po_upitnik");
	}

	public AS2RecordList daoFind(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_po_upitnik_pogled ");
		sql.appendln("ORDER BY naziv ");
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

	public PrnPravnaOsobaVo daoLoad(PrnPravnaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_po_upitnik_pogled WHERE id_upitnika = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdUpitnika());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			PrnPravnaOsobaVo j2eers = new PrnPravnaOsobaVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoSearch(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_po_upitnik_pogled ");
		sql.appEqual("AND", "oib", value.get("oib"));
		sql.appEqual("AND", "maticni_broj", value.get("jmbg"));
		sql.appLike("AND", "naziv", value.get("ime_prezime"));
		sql.appendln("ORDER BY naziv");
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

	public PravnaOsobaRs daoFindPravnaOsoba(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_po_osoba_pogled ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(200);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new PravnaOsobaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PravnaOsobaRs daoSearchPravnaOsoba(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.get("broj_partije").length() > 0)
			sql.append("select * from view_prn_po_osoba_pogled_trazi ");
		else
			sql.append("select * from view_prn_po_osoba_pogled ");
		sql.appEqualNoQuote("AND", "oib", value.get("oib"));
		sql.appEqualNoQuote("AND", "maticni_broj", value.get("jmbg"));
		sql.appEqualNoQuote("AND", "broj_partije", value.get("broj_partije"));
		sql.appLike("AND", "naziv", value.get("ime_prezime"));
		sql.append(" order by naziv ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(200);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new PravnaOsobaRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoZadnjiBrojUpitnika()  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT isnull(max(id_upitnika),0) as id_upitnika FROM prn_po_upitnik ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers.get("id_upitnika");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PravnaOsobaRs daoIzvjestajiPravneOsobe(PravnaOsobaVo value) {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_po_prn_pravne_osobe_facade");
		sp.append(" (?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(4, value.get("@@report_selected"));
			PravnaOsobaRs j2eers = new PravnaOsobaRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}