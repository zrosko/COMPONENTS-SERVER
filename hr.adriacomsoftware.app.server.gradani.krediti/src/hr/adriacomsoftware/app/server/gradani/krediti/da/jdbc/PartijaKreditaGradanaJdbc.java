package hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class PartijaKreditaGradanaJdbc extends BankarskiJdbc implements
		BankaConstants {
	public PartijaKreditaGradanaJdbc() {
		setTableName("");
	}

	public OsobaRs daoPronadiOsobe(OsnovniVo value, boolean pretrazivanje) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from bi_view_gr_krediti_pogled ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		if (pretrazivanje) {
			sql.appLike("AND", "ime_prezime_klijenta", value.getImePrezime());
			sql.appEqualNoQuote("AND", "jmbg", value.getJmbg());
			sql.appEqualNoQuote("OR", "oib", value.getOib());
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

	public OsnovniRs daoDospjeliKrediti(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from dbo.bi_fn_gr_krediti_dug_do_100_kn (?) ");
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

	public OsnovniRs daoSviDospjeliKrediti(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * from dbo.bi_fn_gr_krediti_isteklo_dospijece(?) ");
		sql.append("ORDER BY broj_partije,broj_konta");
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

	public OsnovniRs daoKamatePoKlasiRacuna(OsnovniVo value, String klasa_racuna) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_krediti_kamate_po_klasi_racuna(?,?,?)");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln(" order by broj_konta, broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setString(1, klasa_racuna);
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoKamatePoKlasiRacunaSTARO(OsnovniVo value,String klasa_racuna)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("bi_gr_krediti_kamate_po_klasi_racuna");
		sp.append(" (?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setString(1, klasa_racuna);
			cs.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoHamagIzvjesce(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from dbo.bi_fn_po_hamag(?) ");
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

	public PartijaRs daoListaIzdanihKreditaZaPeriod(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from bi_fn_gr_krediti_izdani_krediti(?,?) ");
		sql.appendln(" ORDER BY organizacijska_jedinica,broj_konta,broj_partije ");
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

	public PartijaRs daoUsporedbaPrometaSaGlavnomKnjigom(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from bi_fn_gr_krediti_analitika_gl_knjiga(?) ");
		sql.appendln("ORDER BY broj_konta");
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

	public OsnovniRs daoCallStoredProcedure(String sp_name, OsnovniVo value) {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoStanjeKontaPartija(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * from bi_fn_gr_krediti_konto_po_partijama(?,?) ");
		sql.appendln(" ORDER BY broj_konta, broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setString(2, value.get(JBDataDictionary.BI_BROJ_KONTA));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_DATUM));
			pstmt.setString(2, value.get(JBDataDictionary.BI_BROJ_KONTA));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoPregledPrometaZaPeriod(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from bi_fn_gr_krediti_kartica_prometa(?,?,?) ");
		sql.append("order by broj_konta,datum_prometa,datum_valutiranja");
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

	public OsnovniRs daoIzvjesceHBOR(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_krediti_HBOR(?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln(" order by broj_konta,organizacijska_jedinica,broj_partije");
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

	public OsnovniRs daoNaplataPotrazivanja(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_krediti_naplate_potrazivanja(?,?,?,?,?,?,?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setInt(3, value.getAsInt("broj_opomena"));
			pstmt.setInt(4, value.getAsInt("opomene_otkaz"));
			pstmt.setInt(5, value.getAsInt("protest_mjenica"));
			pstmt.setInt(6, value.getAsInt("ovrhe"));
			pstmt.setInt(7, value.getAsInt("zapljene"));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoPoredakKredita(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_krediti_stanovnistvu_profitni_centar(?,?,?) order by iznos_kr_valuta desc");
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

	public OsnovniRs daoRasporedAnuiteta(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.bi_fn_gr_krediti_mjesecni_obracun_anuiteta");
		sql.append(" (?,?) where broj_partije >0 ORDER BY datum_obrade,konto_dospjele,konto_ug_kamate,broj_partije");
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

	public OsnovniRs daoRasporedAnuitetaRekap(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.bi_fn_gr_krediti_mjesecni_obracun_anuiteta");
		sql.append(" (?,?) where broj_partije =0 AND otplatna_kvota <>0 ORDER BY valuta_racuna,konto_dospjele");
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

	public OsnovniRs daoProlazniRacun(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT *,? as broj_konta FROM dbo.bi_fn_gr_krediti_prolazni_racun(?,?) ");
		sql.appendln("ORDER BY broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setString(1, value.get(JBDataDictionary.BI_BROJ_KONTA));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setString(3, value.get(JBDataDictionary.BI_BROJ_KONTA));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OsnovniRs daoHNBKreditiZaKupnjuDionica(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_HNB_dionice(?,?) ");
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

	public PartijaRs daoPotvrdaPlacenihKamata(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_krediti_potvrde_placenih_kamata(?,?,?) ");
		sql.appendln("ORDER BY broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getAsObject(J2EEDataDictionary.BROJ_PARTIJE));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(3, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}		
	}

	public OsnovniRs daoR1(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_gr_kredit_racuni_PDV_1 ");
		sql.appendln("WHERE (datum_od = '"
				+ value.get("datum_od").substring(0, 10) + "' and datum_do = '"
				+ value.get("datum_do").substring(0, 10) + "' ) ");
		sql.appendWhere();
		sql.appPrefix();
		if (value.getMaticniBroj().length() > 0)
			sql.appEqualNoQuote("AND", "jmbg", value.getMaticniBroj());
		else if (value.getOib().length() > 0)
			sql.appEqualNoQuote("AND", "oib", value.getOib());
		sql.appendln("ORDER BY jmbg, broj_partije, broj_konta");
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

	public OsnovniRs daoPrometProlazniRacuni(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from dbo.fn_tbl_gr_krediti_prolazni_racun(?,?) ");
		sql.appendln("ORDER BY jmbg, broj_partije, datum_prometa");
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
}