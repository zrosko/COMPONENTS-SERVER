package hr.adriacomsoftware.app.server.pranjenovca.transakcija.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.pranjenovca.transakcija.dto.PrnTransakcijaRs;
import hr.adriacomsoftware.app.common.pranjenovca.transakcija.dto.PrnTransakcijaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class PrnTransakcijaJdbc extends BankarskiJdbc {
	public static final String MAX_BROJ_STAVKE_GR_SQL = "SELECT max(broj_stavke) as max_broj_stavke FROM prn_transakcija where pravna_fizicka = 'GR'";
	public static final String MAX_BROJ_STAVKE_PO_SQL = "SELECT max(broj_stavke) as max_broj_stavke FROM prn_transakcija where pravna_fizicka = 'PO'";

	public PrnTransakcijaJdbc() {
		setTableName("prn_transakcija");
	}

	public PrnTransakcijaVo daoLoad(PrnTransakcijaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select id_transakcije,vrsta_transakcije,datum_transakcije,datum_prometa,svrha_transakcije,");
		sql.append("svrha_obrazlozenje,valuta_transakcije, iznos_valuta,iznos_protuvaluta, status_transakcije, nacin_provodenja ");
		sql.append("FROM prn_transakcija where id_transakcije = ?");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdTransakcije());
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			PrnTransakcijaVo j2eers = new PrnTransakcijaVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoListaObradaTransakcijaGradana(PrnTransakcijaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select CONVERT(char(10), datum_pocetak, 104) AS datum, ");
		sql.appendln("CONVERT(char(10), datum_kraj, 104) AS do_datuma, count(*) as ukupno_stavki,");
		sql.appendln("datum_pocetak, datum_kraj FROM prn_transakcija ");
		sql.append(" WHERE pravna_fizicka = 'GR' ");
		sql.append(" GROUP BY datum_pocetak, datum_kraj ");
		sql.append(" ORDER BY datum_pocetak DESC ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			PrnTransakcijaRs  j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public int daoPronadiNoveTransakcijeGradana(PrnTransakcijaVo input_value) {
		String sp_name = "stp_prn_gr_transakcija_obrada";
		J2EESqlBuilder sp = new J2EESqlBuilder();
		int count = 0;
		if (input_value.get(
				PrnTransakcijaVo.PRN_TRANSAKCIJA__PONAVLJANJE_OBRADE).length() == 0)
			input_value.set(
					PrnTransakcijaVo.PRN_TRANSAKCIJA__PONAVLJANJE_OBRADE,
					PrnTransakcijaVo.VALID_IND_NO);
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(++count, input_value.getAsSqlDate(PrnTransakcijaVo.PRN_TRANSAKCIJA__DATUM_POCETAK));
			cs.setDate(++count, input_value.getAsSqlDate(PrnTransakcijaVo.PRN_TRANSAKCIJA__DATUM_KRAJ));
			cs.setString(++count, input_value.get(PrnTransakcijaVo.PRN_TRANSAKCIJA__PONAVLJANJE_OBRADE));
			cs.registerOutParameter(++count, java.sql.Types.INTEGER);
			cs.execute();
			int status = cs.getInt(count);
			cs.close();
			if (status == 100)
				throw new AS2DataAccessException("10004");
			else if (status == 200)
				throw new AS2DataAccessException("12010");
			return status;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoFindTransakcije(AS2Record value,	String pravna_fizicka)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbs_prn_transakcija_lista(?) ");
		sql.appendln("ORDER BY datum_pocetak DESC ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, pravna_fizicka);
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoSveStavkeTransakcije(PrnTransakcijaVo value,	String pravna_fizicka)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbs_prn_transakcija_lista_stavke(?) ");
		sql.appendln("where id_transakcije_veza = ? ");
		sql.appendln("ORDER BY broj_stavke ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, pravna_fizicka);
			pstmt.setObject(2, value.get("id_transakcije"));
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoSearchTransakcije(AS2Record value,String pravna_fizicka)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbs_prn_transakcija_lista(?) where datum_pocetak >= ? and datum_pocetak <= ? ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appIn("AND", "stanje_isporucenosti_uredu",
				value.get("@@StanjeIsporucenostiUredu"));
		sql.appIn("AND", "stanje_obrasca", value.get("@@StanjeObrasca"));
		if (value.get("@@gotovinske").length() > 0) {
			sql.append(" AND ( (");
			sql.append("vrsta_transakcije=1 ");
			sql.appIn("AND", "status_transakcije", value.get("@@gotovinske"));
			sql.append(") ");
		}
		if (value.get("@@gotovinske").length() > 0
				&& value.get("@@negotovinske").length() > 0) {
			sql.append(" OR (");
			sql.append("vrsta_transakcije=2 ");
			sql.appIn("AND", "status_transakcije", value.get("@@negotovinske"));
			sql.append(") ");
		}
		if (value.get("@@negotovinske").length() > 0
				&& value.get("@@gotovinske").length() <= 0) {
			sql.append(" AND ( (");
			sql.append("vrsta_transakcije=2 ");
			sql.appIn("AND", "status_transakcije", value.get("@@negotovinske"));
			sql.append(") ");
		}
		if (value.get("@@gotovinske").length() > 0
				|| value.get("@@negotovinske").length() > 0) {
			sql.append(" ) ");
		}
		sql.appEqual("AND", "kanal_transakcije", value.get("kanal_transakcije"));
		sql.appEqual("AND", "vrsta_prometa", value.get("vrsta_prometa"));
		sql.appEqual("AND", "oib", value.get("oib"));
		sql.appEqual("AND", "jmbg_mb", value.get("jmbg_mb"));
		sql.appLike("AND", "ime_prezime_naziv", value.get("ime_prezime_naziv"));
		sql.appendln("ORDER BY datum_pocetak DESC");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, pravna_fizicka);
			pstmt.setDate(2, value.getAsSqlDate(PrnTransakcijaVo.PRN_TRANSAKCIJA__DATUM_POCETAK));
			pstmt.setDate(3, value.getAsSqlDate(PrnTransakcijaVo.PRN_TRANSAKCIJA__DATUM_KRAJ));
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoFilterTransakcije(AS2Record value,String pravna_fizicka)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbs_prn_transakcija_lista(?) ");
		sql.appEqual("AND", "vrsta_transakcije", value.get("vrsta_transakcije"));
		sql.appEqual("AND", "status_transakcije",
				value.get("status_transakcije"));
		sql.appendln("ORDER BY datum_pocetak DESC");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, pravna_fizicka);
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoFindTransakcijeZaSlanjeUredu(AS2Record value,String pravna_fizicka)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select ime_prezime_naziv, kanal_transakcije,opis,datum_prometa,iznos_valuta,");
		sql.append("valuta_transakcije,stanje_isporucenosti_uredu_, 'nizak' as rizik, id_transakcije,stanje_isporucenosti_uredu ");
		sql.append("FROM fn_tbs_prn_transakcija_lista(?) WHERE stanje_isporucenosti_uredu is not null ");
		sql.appendln("ORDER BY datum_pocetak DESC ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, pravna_fizicka);
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}			
	}

	public PrnTransakcijaRs daoSearchTransakcijeZaSlanjeUredu(AS2Record value,String pravna_fizicka)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select ime_prezime_naziv, kanal_transakcije,opis,datum_prometa,iznos_valuta,");
		sql.append("valuta_transakcije,stanje_isporucenosti_uredu_, 'nizak' as rizik,id_transakcije,stanje_isporucenosti_uredu ");
		sql.append("FROM fn_tbs_prn_transakcija_lista(?) ");
		sql.appEqual("AND", "stanje_isporucenosti_uredu", " is not null ");
		sql.appEqual("AND", "oib", value.get("oib"));
		sql.appEqual("AND", "jmbg_mb", value.get("jmbg_mb"));
		sql.appLike("AND", "ime_prezime_naziv", value.get("ime_prezime"));
		sql.appendln("ORDER BY datum_pocetak DESC");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, pravna_fizicka);
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoFilterTransakcijeZaSlanjeUredu(AS2Record value,String pravna_fizicka)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select ime_prezime_naziv, kanal_transakcije,opis,datum_prometa,iznos_valuta,");
		sql.append("valuta_transakcije,stanje_isporucenosti_uredu_, 'nizak' as rizik, id_transakcije,stanje_isporucenosti_uredu ");
		sql.append("FROM fn_tbs_prn_transakcija_lista(?) ");
		sql.appEqual("AND", "stanje_isporucenosti_uredu",
				value.get("stanje_isporucenosti_uredu"));
		sql.appendln("ORDER BY datum_pocetak DESC");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, pravna_fizicka);
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoTransakcijeListaStavke(PrnTransakcijaVo value,String pravna_fizicka)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.get("@@izvjestaj").equals("@@izvjestaj")) {
			sql.append("SELECT distinct I.jmbg_mb_, I.oib_, I.ime_prezime_naziv , "
					+ "I.datum_prometa_, B.biljeska, I.datum_od, I.datum_do, I.isms_povjerljivost, "
					+ "ISNULL(I.jmbg_mb,ISNULL(I.oib,I.broj_partije)) as id_osobe "
					+ "FROM fn_tbs_prn_transakcija_izvjestaj(?,?,?,?) as I inner join "
					+ "prn_biljeska as B on I.jmbg_mb = B.jmbg_mb or I.oib = B.oib "
					+ "or I.broj_partije = B.broj_partije ");
			sql.appLike("AND", "I.biljeska_da_ne", "DA");
			sql.append(" and B.datum_prometa =I.datum_prometa ");
		} else {
			sql.append("SELECT * FROM ");
			sql.append("fn_tbs_prn_transakcija_izvjestaj(?,?,?,?) as I ");
		}
		if (value.get("@@analiza").equals("@@analiza")) {
			sql.appEqual("AND", "I.jmbg_mb", value.get("jmbg_mb"));
			sql.appEqual("AND", "I.oib", value.get("oib"));
			sql.appLike("AND", "I.ime_prezime_naziv",
					value.get("ime_prezime_naziv"));
		}
		if (value.get("@@izvjestaj").equals("@@izvjestaj"))
			sql.append("order by I.jmbg_mb_,I.oib_ ");
		else
			sql.append("order by jmbg_mb,datum_prometa,duguje_potrazuje,broj_partije,vrsta_prometa,oznaka_storna ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, pravna_fizicka);
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setObject(4, value.getVrstaTransakcije()); // 1-gotovinske, 2-negotovinske
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoTransakcijePreko105000Staro(PrnTransakcijaVo value, String tip)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM bi_fn_gr_stednja_preko105000_dnevno_sve(?) ");
		if (tip.equals("1")) {// gotovinske
			sql.append("WHERE vrsta_transakcije like 'Gotovinske transakcije' ");
		} else if (tip.equals("2")) {// negotovinske
			sql.append("WHERE vrsta_transakcije like 'Ostale transakcije' ");
		} else
			sql.append(" ");
		sql.append("order by jmbg, broj_partije ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoAzurirajStatus(PrnTransakcijaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE prn_transakcija SET stanje_isporucenosti_uredu = NULL where id_transakcije = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdTransakcije());
			pstmt.setMaxRows(1);
			pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoDubinskaAnalizaTransakcijePravneOsobe(PrnTransakcijaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from prn_crna_lista ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(200);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnTransakcijaRs daoDubinskaAnalizaTransakcijeFizickeOsobe(PrnTransakcijaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbl_prn_gr_procjena_rizika_transakcije(?) ");
		sql.append("order by jmbg_mb,redni_broj, vrsta_ocjena, ocjena");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdTransakcije());
			pstmt.setMaxRows(0);
			PrnTransakcijaRs j2eers = new PrnTransakcijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}