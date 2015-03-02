package hr.adriacomsoftware.app.server.pravneosobe.krediti.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajRs;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.OtplatniPlanRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.OtplatniPlanVo;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.PartijaKreditaRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.PartijaKreditaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class PartijaKreditaPravnihOsobaJdbc extends BankarskiJdbc
		implements BankaConstants {
	public PartijaKreditaPravnihOsobaJdbc() {
		setTableName("");
	}

	public PravnaOsobaRs daoPronadiPravneOsobe(OsnovniVo value,	boolean pretrazivanje)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_pravne_osobe_krediti ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		if (pretrazivanje) {
			sql.appLike("AND", "naziv_klijenta", value.getImePrezime());
			sql.appEqualNoQuote("AND", "maticni_broj", value.getJmbg()); 
			sql.appEqualNoQuote("AND", "oib", value.getOib());
			sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
		}
		sql.appendln("ORDER BY naziv_klijenta");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			PravnaOsobaRs j2eers = new PravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoIzvjestajStanjaKredita(IzvjestajVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendWhere();
		sql.appPrefix();
		sql.appendln("select * from dbo.bi_fn_po_krediti_komitent_izvjestaj(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiSamoZaPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" ORDER BY naziv");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoIzvjestajStanjaKreditaSuzeni(IzvjestajVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendWhere();
		sql.appPrefix();
		sql.appendln("select * from dbo.bi_fn_po_krediti_komitent_izvjestaj_suzeni(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiSamoZaPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" ORDER BY naziv");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoHamagIzvjesce(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from dbo.bi_fn_po_hamag(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY stari_broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoListaKreditniProgramSvePartije(IzvjestajVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.bi_fn_po_krediti_kreditni_program_sve_partije(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3,4) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoRevalorizacijaPoPartijama(IzvjestajVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.bi_fn_po_krediti_revalorizacija_partija(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY broj_partije,broj_konta");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoRevalorizacijaPoKontima(IzvjestajVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from bi_fn_po_krediti_revalorizacija_konto(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY broj_konta");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoPregledUgovora(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT id_ugovora, broj_partije, ugovoreni_iznos_valuta, ");
		sql.append("pocetni_datum, zavrsni_datum, sifra_valute, sifra_protuvalute, ugovoreni_iznos_protuvaluta,");
		sql.append("operater_unosa, vrijeme_posljednje_izmjene, dbo.bi_fn_po_krediti_kamatna_stopa(broj_partije,getdate()) AS kamatna_stopa ");
		sql.append("FROM bi_po_ugovor ");
		sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
		sql.appendln("ORDER BY broj_partije");
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

	public OsnovniRs daoPregledPrometa(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM bi_glavna_knjiga_bsa_arhiva ");
		sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj());
		sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
		sql.appendln("ORDER BY datum_knjizenja");
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

	public OsnovniRs daoPregledStanja(OsnovniVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("bi_po_krediti_stanje_partije");
		sp.append(" (?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			cs.setObject(2,	value.getProperty(JBDataDictionary.BI_PARTIJA__BROJ_PARTIJE));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoKamatniOstaliPrihodi(OsnovniVo value, boolean zbirno) {
		boolean _test = false;
		if (!_test)
			daoCallSpDatumOdDatumDoProfitniCentar(value,"bi_po_krediti_struktura_prihoda");
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (zbirno) {
			// zbirno po kontima
			sql.append("SELECT * FROM bi_view_po_krediti_izvjestaj_struktura_prihoda_zbirno ");
			sql.appendln("ORDER BY radni_broj, naziv");
		} else {
			sql.append("select * from bi_view_po_krediti_izvjestaj_struktura_prihoda ");
			if (value.get(OsnovniVo.IZBOR_NAZIV).equals(OsnovniVo.IZBOR_B))
				sql.appendln("WHERE grupa NOT IN ('P','R') "); // bez dugorocnih
																// i
																// kratkorocnih
																// kredita
			else if (value.get(OsnovniVo.IZBOR_NAZIV).equals(OsnovniVo.IZBOR_C))
				sql.appendln("WHERE grupa IN ('P','R') "); // samo dugorocni i
															// kratkorocni
															// krediti
			sql.appendln("ORDER BY grupa, naziv");
		}
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

	public AS2RecordList daoListaKredita(AS2Record value, String view_name) {
		String sql = prepareSqlFind(value, view_name);
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

	public OsnovniRs daoPregledPrometaZaPeriod(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from bi_fn_po_krediti_kartica_prometa(?,?,?) ");
		sql.append("order by broj_konta,datum_knjizenja,datum_valutiranja");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setObject(3, value.getBrojPartije());
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OtplatniPlanRs daoOtplatniPlan(OtplatniPlanVo value, String sp_name) {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			short count = 1;
			cs.setObject(count++,value.getProperty(JBDataDictionary.BI_PARTIJA__BROJ_PARTIJE));
			cs.setObject(count++, value.getProperty(JBDataDictionary.BI_OTPLATNI_PLAN__IZNOS_KREDITA));
			cs.setObject(count++, value.getProperty(JBDataDictionary.BI_OTPLATNI_PLAN__KAMATNA_STOPA));
			cs.setObject(count++, value.getProperty(JBDataDictionary.BI_OTPLATNI_PLAN__RATA_KREDITA));
			cs.setDate(count++, value.getAsSqlDate(JBDataDictionary.BI_OTPLATNI_PLAN__POCETAK_KORISTENJA));
			cs.setDate(count++,	value.getAsSqlDate(JBDataDictionary.BI_OTPLATNI_PLAN__DOSPIJECE_PRVE_RATE));
			cs.setDate(count++,	value.getAsSqlDate(JBDataDictionary.BI_OTPLATNI_PLAN__PRVI_OBRACUN_KAMATE));
			cs.setObject(count++, value.getProperty(JBDataDictionary.BI_OTPLATNI_PLAN__BROJ_MJESECI_RATE));
			cs.setObject(count++, value.getProperty(JBDataDictionary.BI_OTPLATNI_PLAN__BROJ_MJESECI_KAMATE));
			cs.execute();
			cs.close();
			J2EESqlBuilder sql = new J2EESqlBuilder("select * FROM bi_po_izvjestaj_plan_otpl_kred order by br_partije, brreda");
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OtplatniPlanRs j2eers = new OtplatniPlanRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoDospjeleObveze(IzvjestajVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_kredit_pregled_dospjelih_obveza(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoPoveznicaPartija(IzvjestajVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_kredit_poveznica_po_partiji(?)");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoRacunDobitiGubitka(IzvjestajVo value) {
		boolean _test = false;
		if (!_test)
			daoCallSpDatumOdDatumDo(value,
					"bi_po_krediti_racun_dobiti_i_gubitka");
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from bi_po_krediti_izvjestaj_racun_dobiti ");
		sql.appendln("ORDER BY broj");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaKreditaRs daoAnalizaKredita(PartijaKreditaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM RIZIK_PROD.dbo.view_kr_transakcija where broj_partije between 8100000000 and 8700000000 and datum = ? ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appLike("AND", "naziv", value.getNaziv()); // ime prezime znaci
														// naziv
		sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj()); //
		sql.appLike("AND", "naziv_proizvoda", value.getProgram());
		sql.appEqualNoQuote("AND", "kamatna_stopa", value.getKamatnaStopa());
		sql.appEqual("AND", "valuta", value.getValutaRacuna());
		sql.appEqual("AND", "rizicna_skupina", value.getOcjena());
		sql.appGreatherOrEqual("AND", "odobreni_iznos", value.getIznosKredita());
		sql.appLessOrEqual("AND", "odobreni_iznos", value.getIznosKredita());
		sql.appGreatherOrEqual("AND", "ostatak_duga", value.getGlavniDug());
		sql.appLessOrEqual("AND", "ostatak_duga", value.getGlavniDugDo());
		sql.appGreatherOrEqual("AND", "dospjela_glavnica",
				value.getDospjelaRata());
		sql.appLessOrEqual("AND", "dospjela_glavnica",
				value.getDospjelaRataDo());
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY naziv");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			PartijaKreditaRs j2eers = new PartijaKreditaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoKreditiFazaDva(IzvjestajVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_kredit_faza_kredita_2(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoKreditiFazaCetiri(IzvjestajVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_kredit_faza_kredita_4(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoTudaKnjizenja(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_tudja_knjizenja_period(?,?)");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		// sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
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

	public OsnovniRs daoNovoodobreniKrediti(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.");
		sql.append("bi_fn_po_krediti_novoodobreni_plasmani");
		sql.append(" (?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
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

	public OsnovniRs daoPromjenaFazeKredita(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.bi_fn_po_krediti_promjena_faze (?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3,4) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" ORDER BY datum_zavrsetka ");
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

	public OsnovniRs daoManipulativniTroskovi(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_manipulativni_troskovi (?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" ORDER BY maticni_broj ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoManipulativniTroskoviKnjizenja(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_manipulativni_troskovi_knjizenja (?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" ORDER BY broj_partije, datum_knjizenja ");
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

	public OsnovniRs daoPriljeviPoKreditima(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_dekadni_izvjestaj_likvidnost (?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		// sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" ORDER BY sektor desc,broj_konta ");
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

	public OsnovniRs daoPriljeviPoKreditimaBuduci(OsnovniVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_dekadni_izvjestaj_likvidnost_buduca (?,?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		// sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" ORDER BY sektor ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoPretplata(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_krediti_pretplata(?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoBrutoBilanca(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_brutto_bilanca(?) ");
		sql.appendln("ORDER BY broj_konta");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoNovootvoreniKrediti(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from dbo.bi_fn_po_krediti_novootvoreni_za_razdoblje(?,?) ");
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

	public OsnovniRs daoNovootvoreneGarancije(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from dbo.bi_fn_po_garancije_novootvoreni_za_razdoblje(?,?) ");
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

	public OsnovniRs daoSaldoKunskihRacuna(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_saldo_kunskih_racuna(?) "
				+ "ORDER BY broj_konta,broj_partije ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoIzdaniKrediti(OsnovniVo value, int vrsta, int tip) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_kreditni_program_sredstva_Banke(?) ");
		if (vrsta == 1)
			sql.appendln("WHERE oznaka='D' ");
		else if (vrsta == 2)
			sql.appendln("WHERE oznaka='K' ");
		if (tip == 1) // ako je iReport ne prikazuj ocjenu C (za Excel ce
						// prikazati)
			sql.appendln("and ocjena_partije not like 'C%' ");
		sql.appendln("ORDER BY ocjena_partije,datum_dospijeca ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoSaldaKontaKnjizenja(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_stanje_konta_direkcije_kreditiranja(?) "
				+ "ORDER BY broj_konta,valuta_racuna,protuvaluta_racuna ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoSaldaKontaSuspendiranihPrihoda(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_stanje_konta_suspendiranih_prihoda(?) "
				+ "ORDER BY oznaka,broj_konta,organizacijska_jedinica,valuta_racuna,protuvaluta_racuna ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public IzvjestajRs daoKreditiFazaTri(IzvjestajVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_kredit_faza_kredita_3(?) "
				+ "WHERE broj_partije>8400000000 " + "ORDER BY kraj_faze_ ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}			
	}

	public IzvjestajRs daoGarancijeFazaTri(IzvjestajVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_garancije_faza_3(?) "
				+ "WHERE valuta_racuna<>0 " + "ORDER BY datum_odobrenja_ ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoKreditiKnjizenjaNedospjeleGlavnice(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_krediti_knjizenja_nedospjele_glavnice(?,?) "
				+ "ORDER BY datum_knjizenja,broj_partije ");
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

	public OsnovniRs daoIOSObrazac(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_IOS(?,?) "
				+ "ORDER BY maticni_broj,pripadnost,broj_konta ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get(JBDataDictionary.BI_MATICNI_BROJ));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public IzvjestajRs izvjestajPregledIskljucenihPartija(IzvjestajVo  value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("select * from fn_tbs_po_krediti_opomene_iskljuceno(?) ");
        try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.ZADANI_DATUM));
			pstmt.setMaxRows(0);
			IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}