package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.bonitet.dto.BonitetBilancaVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PodaciKlijentaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PoslovniOdnosBankaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PovezanaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.RizikVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostKodBankeRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaVo;
import hr.adriacomsoftware.app.server.pravneosobe.bonitet.da.jdbc.BonitetJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Types;

public final class PoZahZahtjevJdbc extends PoZahJdbc {
	public PoZahZahtjevJdbc() {
		setTableName("po_zah_zahtjev");
	}

	public ZahtjevPravnaOsobaRs daoPronadiZahtjeve(ZahtjevPravnaOsobaVo value, boolean pretrazivanje)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from dbo.fn_tbl_po_zah_pogled() ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		if (value.get("@obrtnici").equals("1")) {
			sql.appGreatherOrEqualNoQuote("AND", "maticni_broj", "90000000");
		}
		if (pretrazivanje) {
			sql.appEqual("AND", "broj_zahtjeva", value.getBrojZahtjeva());
			sql.appLike("AND", "naziv", value.getImePrezimeNaziv());
			sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj());
			sql.appEqualNoQuote("AND", "oib", value.getOib());
			sql.appIn("AND", "status_zahtjeva", value.getStatusZahtjeva());
			sql.appIn("AND", "hitnost", value.get("@@Hitnost"));
		}
		sql.appendln(" ORDER BY datum_sort desc, broj_zahtjeva desc, status_zahtjeva");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			ZahtjevPravnaOsobaRs j2eers = new ZahtjevPravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoSlijedeciBrojZahtjeva()  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT isnull(max(broj_zahtjeva),0)+1 as broj_zahtjeva FROM po_zah_zahtjev ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public ZahtjevPravnaOsobaVo daoPripremiZahtjev(ZahtjevPravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.fn_po_zah_pravna_osoba_podaci(?,?,?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
			pstmt.setObject(3, value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			ZahtjevPravnaOsobaVo povrat_vo = new ZahtjevPravnaOsobaVo(j2eers);
			// PODACI KLIJENTA
			PodaciKlijentaVo pod_kli = new PodaciKlijentaVo(j2eers);
			povrat_vo.setPodaciKlijenta(pod_kli);
			// POSLOVNI ODNOS BANKA
			PoslovniOdnosBankaVo po_banka = new PoslovniOdnosBankaVo(j2eers);
			povrat_vo.setPoslovneOdnoseBanka(po_banka);
			// ZADUZENOST KOD BANIKE
			pstmt.close();
			pstmt = getConnection().getPreparedStatement("SELECT * FROM dbo.bi_fn_po_partija_krediti(?,?)");
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
			pstmt.setMaxRows(0);
			j2eers = (transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			povrat_vo.setZaduzenostKodBanke(new ZaduzenostKodBankeRs(j2eers));
			// POVEZANE OSOBE
			pstmt = getConnection().getPreparedStatement("SELECT * FROM dbo.fn_po_zah_povezane_osobe(?,?)");
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
			pstmt.setMaxRows(0);
			j2eers = (transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			povrat_vo.setPovezaneOsobe(new PovezanaOsobaRs(j2eers));
			// BONITET
			BonitetJdbc bonitet_dao = new BonitetJdbc();
			BonitetBilancaVo bonitet_vo = new BonitetBilancaVo(
			bonitet_dao.daoCitajBilancu(new PravnaOsobaVo(value)));
			povrat_vo.setBonitet(bonitet_vo);
			return povrat_vo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public ZahtjevPravnaOsobaVo daoPripremiOsnovnePodatke(ZahtjevPravnaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		// citaj zadnji zahtjev iz baze
		sql.append("SELECT top (1) * FROM view_po_zah_podaci_klijenta ");
		sql.append("where maticni_broj = ? ");
		sql.append("order by broj_zahtjeva desc ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			ZahtjevPravnaOsobaVo povrat_vo = new ZahtjevPravnaOsobaVo(j2eers);
			pstmt = getConnection().getPreparedStatement("SELECT * FROM dbo.fn_po_zah_pravna_osoba_podaci(?,?,?)");
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setDate(2, value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
			pstmt.setObject(3, value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			povrat_vo.append(j2eers);
			return povrat_vo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public ZahtjevPravnaOsobaVo daoCitajOsnovnePodatkeZahtjeva(ZahtjevPravnaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_po_zah_osnovni_podaci(?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			ZahtjevPravnaOsobaVo j2eers = new ZahtjevPravnaOsobaVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public ZahtjevPravnaOsobaVo daoPripremiPrijedlogOdluke(ZahtjevPravnaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select isnull(polje1,'')+isnull(polje2,'')+isnull(polje3,'')+isnull(polje4,'') as prijedlog_odluke from dbo.fn_po_zah_referat_prijedlog_odluke(?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			ZahtjevPravnaOsobaVo j2eers = new ZahtjevPravnaOsobaVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public ZahtjevPravnaOsobaVo daoDupliciranjeZahtjeva(ZahtjevPravnaOsobaVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		String sp_name = "stp_po_zah_zahtjev_dupliciranje";
		int count = 0;
		AS2User _user = (AS2User) value.getProperty(AS2Constants.USER_OBJ);
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(++count, value.getBrojZahtjeva());
			if (_user.getAsLong("id_osobe") == 0)
				cs.setNull(++count, Types.FLOAT);
			else
				cs.setObject(++count, _user.getProperty("id_osobe"));
			cs.registerOutParameter(++count, java.sql.Types.INTEGER);
			cs.execute();
			int status = cs.getInt(count);
			cs.close();
			if (status == 0)
				throw new AS2DataAccessException("11013");
			value.setBrojZahtjeva(status + "");// vraća novi broj zahtjeva nazad
			return value;
		} catch (AS2Exception e) {
			throw e;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public ZahtjevPravnaOsobaVo daoCitajPrijedlogOdobrenja(ZahtjevPravnaOsobaVo value)  {
		ZahtjevPravnaOsobaVo j2eers = null;
		J2EESqlBuilder sql = new J2EESqlBuilder();
		try {
			sql.append("select * from dbo.fn_tbl_po_zah_zahtjev_odobrenje(?)");
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			j2eers = new ZahtjevPravnaOsobaVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			return j2eers;
		}
	}

	public ZahtjevPravnaOsobaVo daoCitajZahtjevZaProcjenuRizika(RizikVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_po_zah_rizik_podaci(?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			ZahtjevPravnaOsobaVo j2eers = new ZahtjevPravnaOsobaVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public ZahtjevPravnaOsobaRs daoPronadiZahtjeveZaKlijenta(ZahtjevPravnaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT broj_zahtjeva, datum_rizika, opis_zahtjeva, iznos, ocjena, rizik ");
		sql.appendln("from po_zah_rizik ");
		sql.appendln("where jmbg_mb = ? and isnull(broj_zahtjeva,0) not in (?)");
		sql.appendln("ORDER BY datum_rizika ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setObject(2, value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			ZahtjevPravnaOsobaRs j2eers = new ZahtjevPravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public boolean daoFindIfExists(String broj_zahtjeva)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT 1 FROM po_zah_zahtjev where broj_zahtjeva = '"
				+ broj_zahtjeva + "'");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			int broj_slogova = j2eers.size();
			if (broj_slogova > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoZaprimateljZahtjeva(String broj_zahtjeva)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT convert(decimal(18,0),zaprimatelj) as zaprimatelj FROM po_zah_zahtjev where broj_zahtjeva = '"
				+ broj_zahtjeva + "'");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers.getAsString("zaprimatelj");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public boolean daoFindIfExistsZaKlijenta(String broj_zahtjeva, String maticni_broj, String oib)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT 1 FROM po_zah_podaci_klijenta where broj_zahtjeva = '"
				+ broj_zahtjeva
				+ "'"
				+ " and maticni_broj = '"
				+ maticni_broj
				+ "'");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			int broj_slogova = j2eers.size();
			if (broj_slogova > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}