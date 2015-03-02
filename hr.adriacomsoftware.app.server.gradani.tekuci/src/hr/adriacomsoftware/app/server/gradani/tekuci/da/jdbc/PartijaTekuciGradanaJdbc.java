package hr.adriacomsoftware.app.server.gradani.tekuci.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PartijaTekuciGradanaJdbc extends BankarskiJdbc implements
		BankaConstants {
	public PartijaTekuciGradanaJdbc() {
		setTableName("");
	}

	public OsobaRs daoPronadiOsobe(OsnovniVo value, boolean pretrazivanje) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_gr_tekuci_pogled ");
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
			pstmt.setMaxRows(100);
			OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoKamateNaknade(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		short count = 1;
		sql.append("select * FROM dbo.bi_fn_gr_tekuci_kamate_naknade(?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		// zr. 22.2.2012. sql.append(" order by organizacijska_jedinica");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(count++, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(count++, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
		}

	public PartijaRs daoListaPartijaBezPrometa(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		short count = 1;
		sql.append("select * FROM dbo.bi_fn_gr_tekuci_bez_prometa(?,?,?,?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" order by ime_prezime");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(count++, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(count++, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setInt(count++, value.getAsInt(OsnovniVo.IZBOR_A_NAZIV, 0));
			pstmt.setBigDecimal(count++,value.getAsBigDecimal(JBDataDictionary.BI_PARTIJA__STANJE_PARTIJE_OD));
			pstmt.setBigDecimal(count++,value.getAsBigDecimal(JBDataDictionary.BI_PARTIJA__STANJE_PARTIJE_DO));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoListaPartijaNedozvoljeno(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_gr_tekuci_nedozvoljeno_prekoracenje ");
		sql.appLess("AND", "nedozvoljeno_prekoracenje", "0");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY broj_partije");
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

	public PartijaRs daoListaPartijaKriterij(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_gr_tekuci_stanje ");
		sql.appGreatherOrEqual("AND", "stanje_partije",
				value.get(JBDataDictionary.BI_PARTIJA__STANJE_PARTIJE_OD));
		sql.appLessOrEqual("AND", "stanje_partije",
				value.get(JBDataDictionary.BI_PARTIJA__STANJE_PARTIJE_DO));
		sql.appGreatherOrEqual("AND", "ugovoreno_prekoracenje", value
				.get(JBDataDictionary.BI_PARTIJA__UGOVORENO_PREKORACENJE_OD));
		sql.appLessOrEqual("AND", "ugovoreno_prekoracenje", value
				.get(JBDataDictionary.BI_PARTIJA__UGOVORENO_PREKORACENJE_DO));
		sql.appGreatherOrEqual("AND", "moguci_kredit",
				value.get(JBDataDictionary.BI_PARTIJA__MOGUCI_KREDIT_OD));
		sql.appLessOrEqual("AND", "moguci_kredit",
				value.get(JBDataDictionary.BI_PARTIJA__MOGUCI_KREDIT_DO));
		sql.appGreatherOrEqual("AND", "dozvoljeno_prekoracenje", value
				.get(JBDataDictionary.BI_PARTIJA__DOZVOLJENO_PREKORACENJE_OD));
		sql.appLessOrEqual("AND", "dozvoljeno_prekoracenje", value
				.get(JBDataDictionary.BI_PARTIJA__DOZVOLJENO_PREKORACENJE_DO));
		sql.appGreatherOrEqual("AND", "nedozvoljeno_prekoracenje", value
				.get(JBDataDictionary.BI_PARTIJA__NEDOZVOLJENO_PREKORACENJE_OD));
		sql.appLessOrEqual("AND", "nedozvoljeno_prekoracenje", value
				.get(JBDataDictionary.BI_PARTIJA__NEDOZVOLJENO_PREKORACENJE_DO));
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY " + value.getOrderBy());
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

	public PartijaRs daoListaTrajnihNaloga(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_gr_tekuci_trajni_nalozi ");
		sql.appEqual("AND", "vrsta_trajnog_naloga", value
				.get(JBDataDictionary.BI_TRAJNI_NALOG__VRSTA_TRAJNOG_NALOGA));
		sql.appEqual("AND", "broj_partije_duguje", value
				.get(JBDataDictionary.BI_TRAJNI_NALOG__BROJ_PARTIJE_DUGUJE));
		sql.appLike("AND", "platitelj",
				value.get(JBDataDictionary.BI_TRAJNI_NALOG__PLATITELJ));
		sql.appLike("AND", "primatelj",
				value.get(JBDataDictionary.BI_TRAJNI_NALOG__PRIMATELJ));
		sql.appLike("AND", "broj_trajnog_naloga", value
				.get(JBDataDictionary.BI_TRAJNI_NALOG__BROJ_TRAJNOG_NALOGA));
		sql.appLike("AND", "operater_unosa",
				value.get(JBDataDictionary.BI_TRAJNI_NALOG__OPERATER_UNOSA));
		sql.appEqual("AND", "sifra_valute",
				value.get(JBDataDictionary.BI_TRAJNI_NALOG__SIFRA_VALUTE));
		sql.appEqual("AND", "broj_partije_potrazuje", value
				.get(JBDataDictionary.BI_TRAJNI_NALOG__BROJ_PARTIJE_POTRAZUJE));
		sql.appLike("AND", "svrha_placanja",
				value.get(JBDataDictionary.BI_TRAJNI_NALOG__SVRHA_PLACANJA));
		sql.appLike("AND", "poziv_na_broj_odobrenja", value
				.get(JBDataDictionary.BI_TRAJNI_NALOG__POZIV_NA_BROJ_ODOBRENJA));
		sql.appEqual("AND",	"broj_partije_duguje_postoji",
				value.get(JBDataDictionary.BI_TRAJNI_NALOG__BROJ_PARTIJE_DUGUJE_POSTOJI));
		if (value.get(JBDataDictionary.BI_TRAJNI_NALOG__AKTIVNI).equals(
				OsnovniVo.OPTION_DA)) {
			sql.appendWhere(" (zavrsni_datum IS NULL OR zavrsni_datum >= GETDATE()) AND (pocetni_datum IS NULL OR pocetni_datum <= GETDATE())");
		}
		if (value.get(JBDataDictionary.BI_TRAJNI_NALOG__NEAKTIVNI).equals(
				OsnovniVo.OPTION_DA)) {

			sql.appendWhere(" (zavrsni_datum IS NOT NULL AND zavrsni_datum <= GETDATE())");
		}
		if (value.get(JBDataDictionary.BI_TRAJNI_NALOG__NEISPRAVAN_DATUM)
				.equals(OsnovniVo.OPTION_DA)) {
			sql.appendWhere(" (godina > 0 OR mjesec > 0 OR dan = 0 )");
		}
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY vrsta_trajnog_naloga");
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

	public PartijaRs daoListaRocnostNedozvoljenogPrekoracenja(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * FROM dbo.bi_fn_gr_tekuci_rocnost( ?, 3200000000,3299999999,99) ");
		sql.appNotLike("AND", "status_partije", value.get("@@ssp"));
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY broj_dana_nedozvoljenog_prek desc");
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

	public PartijaRs daoListaRocnostNedozvoljenogPrekoracenjaPregled(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select R.*, CMDB_PROD.dbo.fn_scl_isms_imovina_povjerljivost(1400) as isms_povjerljivost ");
		sql.appendln("FROM bi_gr_tekuci_rocnost R ");
		sql.appendWhere();
		sql.appPrefix();

		sql.append("WHERE godina = DATEPART(year,'");
		sql.append(value.get("datum"));
		sql.append("')");

		sql.append("AND mjesec = DATEPART(month,'");
		sql.append(value.get("datum"));
		sql.append("')");

		sql.append("and dan = DATEPART(day,'");
		sql.append(value.get("datum"));
		sql.append("')");
		sql.appNotLike("AND", "status_partije", value.get("@@ssp"));
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY broj_dana_nedozvoljenog_prek desc");
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

	public PartijaRs daoNaplataRezervacija(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from bi_fn_gr_tekuci_naplata_rezerviranih_racuna(?,?) ");
		// Ograničavanje za sigurnosne razine (1,2,3) POČETAK //
		sql = odrediRazinuOvlasti(sql);
		// ograničavanje za sigurnosne razine KRAJ//
		sql.appendln("ORDER BY broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoCrnaLista(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * FROM dbo.bi_fn_gr_tekuci_crnalista (?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
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

	public PartijaRs daoDnevneOpomene(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * FROM dbo.bi_fn_gr_tekuci_dnevne_opomene (?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
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

	public PartijaRs daoRegistarRacuna(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * FROM dbo.bi_view_gr_tekuci_registar_racuna  ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln(" ORDER BY prezime");
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
}