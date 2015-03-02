package hr.adriacomsoftware.app.server.pranjenovca.gr.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnFizickaOsobaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class PrnGrUpitnikJdbc extends BankarskiJdbc {

	public PrnGrUpitnikJdbc() {
		setTableName("prn_gr_upitnik");
	}

	public AS2RecordList daoFind(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_gr_upitnik_pogled ");
		sql.appendln("ORDER BY prezime_ime ");
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

	public PrnFizickaOsobaVo daoLoad(PrnFizickaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_gr_upitnik_pogled WHERE id_upitnika = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdUpitnika());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			PrnFizickaOsobaVo j2eers = new PrnFizickaOsobaVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoSearch(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_gr_upitnik_pogled ");
		sql.appEqual("AND", "oib", value.get("oib"));
		sql.appEqual("AND", "jmbg", value.get("jmbg"));
		sql.appLike("AND", "prezime_ime", value.get("ime_prezime"));
		sql.appendln("ORDER BY prezime_ime");
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

	public OsobaRs daoFindOsoba(OsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_gr_osoba_pogled");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(200);
			OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsobaRs daoSearchOsoba(OsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.get("broj_partije").length() > 0)
			sql.append("select * from view_prn_gr_osoba_pogled_trazi ");
		else
			sql.append("select * from view_prn_gr_osoba_pogled ");
		sql.appEqual("AND", "oib", value.get("oib"));
		sql.appEqual("AND", "jmbg", value.get("jmbg"));
		sql.appEqual("AND", "broj_partije", value.get("broj_partije"));
		sql.appLike("AND", "prezime_ime", value.get("ime_prezime"));
		sql.append(" order by prezime_ime ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(200);
			OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoZadnjiBrojUpitnika()  {
		J2EESqlBuilder sql= new J2EESqlBuilder();
		sql.append("SELECT isnull(max(id_upitnika),0) as id_upitnika FROM prn_gr_upitnik ");
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

	public OsobaRs daoIzvjestajiFizickeOsobe(OsobaVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_gr_prn_fizicke_osobe_facade");
		sp.append(" (?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(4, value.get("@@report_selected"));
			cs.setObject(5, value.get("org_jedinica"));
			cs.setObject(6, value.get("id_upitnika"));
			OsobaRs j2eers = new OsobaRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnFizickaOsobaVo daoUcitaj(PrnFizickaOsobaVo value)	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_gr_upitnik_ucitaj WHERE rbr=1 and oib = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getOib());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			PrnFizickaOsobaVo j2eers = new PrnFizickaOsobaVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoProcitajSifre(OsnovniVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		int counter = 0;
		sp.append("{call ");
		sp.append("stp_prn_sifrarnik");
		sp.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(++counter, value.get("izvor"));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}