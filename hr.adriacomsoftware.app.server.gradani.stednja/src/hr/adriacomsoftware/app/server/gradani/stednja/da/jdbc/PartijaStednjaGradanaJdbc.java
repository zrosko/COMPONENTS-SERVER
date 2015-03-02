package hr.adriacomsoftware.app.server.gradani.stednja.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.stednja.dto.StednjaVo;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class PartijaStednjaGradanaJdbc extends BankarskiJdbc implements
		BankaConstants {
	public PartijaStednjaGradanaJdbc() {
		setTableName("");
	}

	public OsobaRs daoPronadiOsobe(OsnovniVo value, boolean pretrazivanje)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_gr_stednja_devize_pogled ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		if (pretrazivanje) {
			sql.appLike("AND", "ime_prezime_klijenta", value.getImePrezime());
			sql.appEqualNoQuote("AND", "jmbg", value.getJmbg());
			sql.appEqualNoQuote("AND", "oib", value.getOib());
			sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
		}
		sql.appendln("ORDER BY ime_prezime_klijenta");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1000);
			OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	private void daoCallExecuteStoredProcedure(String sp_name, OsnovniVo input_value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, input_value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2, input_value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoListaTecajnihRazlika(OsnovniVo value)  {
		daoCallExecuteStoredProcedure("bi_gr_devize_tecajne_razlike_gd", value);
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM bi_gr_devize_tecajne_razlike");
		sql.appendln(" ORDER BY broj_konta, sifra_valute");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoStednjaFiksnaKamata(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_stednja_fiksna_kst(?)");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln(" ORDER BY broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoPronadiNerezidente(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_stednja_nerezidenti(?)");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln(" ORDER BY tip_partije, datum_otvaranja");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoListaNegativnihPartija(OsnovniVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_stednja_negativni ()");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln(" ORDER BY broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoListaPrometaPogresnaValuta(OsnovniVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_stednja_promet_u_pogresnoj_valuti (?)");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln(" ORDER BY broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoPoredakStednja(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT TOP(" + value.getBrojac() + ") * ");
		sql.appendln("FROM dbo.bi_fn_gr_stednja_profitni_centar(?,?,?) order by iznos_kune desc ");
		try{	
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setString(2, value.getProfitniCentar());
			pstmt.setObject(3, value.getBrojac());
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoKunskaStednja(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_kune_stednja_oj(?) ");
		sql.append("group by organizacijska_jedinica,naziv,rb,saldo,datum ");
		sql.append("order by organizacijska_jedinica,rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoKunskaStednjaBrojPartija(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_kune_broj_partija_oj(?,?) ");
		sql.append("group by organizacijska_jedinica,naziv,rb,broj_partija,datum_poc, datum_kraj ");
		sql.append("order by organizacijska_jedinica,rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
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

	public OsnovniRs daoDeviznaStednja(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_devize_stednja_oj(?) ");
		sql.append("group by organizacijska_jedinica,naziv,rb,saldo,datum ");
		sql.append("order by organizacijska_jedinica,rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoDeviznaStednjaBrojPartija(OsnovniVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_devize_broj_partija_oj(?,?) ");
		sql.append("group by organizacijska_jedinica,naziv,rb,broj_partija,datum_poc, datum_kraj ");
		sql.append("order by organizacijska_jedinica,rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
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

	public OsnovniRs daoOrocenaStednja(OsnovniVo value, int izvjestaj)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_stednja_oj(?) ");
		if (izvjestaj == 1) {
			sql.append("where rb1 between 1 and 11 ");
		} else if (izvjestaj == 2) {
			sql.append("where rb1 between 12 and 22 ");
		} else
			sql.append(" ");
		sql.append("order by rb1,rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoOrocenaStednjaPartije(StednjaVo value, String org,
			String prof, String ozn, String roc, String vk)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.bi_fn_gr_stednja_orocena_profitni_centar(?)");
		if (org.equals("")) {
		} else {
			sql.appendWhere("organizacijska_jedinica = " + org);
		}
		if (prof.equals("")) {
		} else {
			sql.appendWhere("profitni_centar = " + prof);
		}
		if (ozn.equals("")) {
		} else {
			sql.appendWhere("oznaka_stednje like " + "'" + ozn + "%'");
		}
		if (roc.equals("")) {
		} else {
			sql.appendWhere("rocnost like " + "'%" + roc + "%'");
		}
		if (vk.equals("")) {
		} else {
			String tempVk = vk.replace('_', ' ');
			sql.appendWhere("valutna_klauzula like " + "'" + tempVk + "%'");
		}
		sql.append(" ORDER BY oznaka_stednje,rocnost,valutna_klauzula,broj_konta ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(StednjaVo.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoPregledPartijaKonto924061(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * " + "FROM dbo.bi_fn_gr_kune_48(?) "
				+ "ORDER BY broj_partije ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoNeregularnoZatvorenePartije(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * "
				+ "FROM dbo.bi_fn_gr_stednja_neregularno_zatvorene_partije(?,?) "
				+ "ORDER BY datum_zatvaranja,broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
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

	public OsnovniRs daoRekapitulacijaPoKontima(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * "
				+ "FROM dbo.bi_fn_gr_stednja_rekapitulacija_konta(?) "
				+ "ORDER BY broj_konta ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoRekapitulacijaPoValutama(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * "
				+ "FROM dbo.bi_fn_gr_devize_rekapitulacija_valute(?) "
				+ "ORDER BY sifra_valute ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoDeviznaStednjaPoIntervalima(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT oznaka, sifra_valute, broj_konta, saldo_valuta, "
				+ "saldo_u_kunama, od, do, broj_depozita, iznos, postotak, "
				+ "postotak_uk, od1, do1, broj_depozita1, iznos1, postotak1, "
				+ "postotak_uk1, od2, do2, broj_depozita2, iznos2, postotak2, "
				+ "postotak_uk2, broj_racuna,datum "
				+ "FROM dbo.bi_fn_gr_devize_stednja_po_intervalima(?) "
				+ "WHERE (oznaka = 1) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoNajvecih20(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_stednja_20max(?,?) ");
		sql.appendln("ORDER BY saldo_u_kunama_ukupno DESC, br, broj_partije ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setObject(2, AS2SessionFactory.getInstance().getCurrentUser().get("korisnik"));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}