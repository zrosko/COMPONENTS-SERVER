package hr.adriacomsoftware.app.server.kolaterali.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.KOLDataDictionary;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralRs;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class KolateralJdbc extends BankarskiJdbc {

	public KolateralJdbc() {
		setTableName("kol_kolateral");
	}

	public KolateralRs daoPronadiKolaterale(KolateralVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM view_kol_kolaterali_pretrazivanje WHERE isnull(datum_dospijeca,getdate()) >= ? and isnull(datum_dospijeca,getdate()) <= ? ");
		sql.append(" and isnull(vrijedi_od,getdate()) >= ? and isnull(vrijedi_do,getdate()) <= ? ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appEqualNoQuote("AND", "maticni_broj_jmbg",
				value.getMaticniBrojJmbg());
		sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
		sql.appEqualNoQuote("AND", "oib", value.getOib());
		sql.appEqual("AND", "vrsta", value.getVrsta());
		sql.appLike("AND", "vlasnik", value.getVlasnik());
		sql.appLike("AND", "naziv_osobe", value.getNaziv());
		sql.appLike("AND", "lokacija", value.getLokacija());
		sql.appLike("AND", "katastarska_cestica", value.getKatastarskaCestica());
		sql.appLike("AND", "katastarska_opcina", value.getKatastarskaOpcina());
		sql.appLike("AND", "broj_zk_uloska", value.getBrojZkUloska());
		sql.appEqual("AND", "valuta_procjene", value.getValutaProcjene());
		sql.appLike("AND", "procjenitelj", value.getProcjenitelj());
		sql.appLike("AND", "zalog_broj_zaloga", value.getZalogBrojZaloga());
		sql.appLike("AND", "zalog_broj_ovjere", value.getZalogBrojOvjere());
		sql.appLike("AND", "zalog_sud", value.getZalogSud());
		sql.appLike("AND", "zalog_javni_biljeznik",
				value.getZalogJavniBiljeznik());
		sql.appLike("AND", "napomena", value.getNapomena());
		sql.appGreatherOrEqualNoQuote("AND",
				" isnull(procjenjena_vrijednost,0) ",
				value.getProcjenjenaVrijednost());
		sql.appLessOrEqualNoQuote("AND", " isnull(procjenjena_vrijednost,0) ",
				value.getPovrsinaZemljista());
		sql.appendln(" ORDER BY vrsta, naziv");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value
					.getAsSqlDate(KOLDataDictionary.KOL_KOLATERAL__DATUM_PROCJENE));
			pstmt.setDate(2, value
					.getAsSqlDate(KOLDataDictionary.KOL_KOLATERAL__DATUM_DOSPIJECA));
			pstmt.setDate(3,
					value.getAsSqlDate(KOLDataDictionary.KOL_KOLATERAL__VRIJEDI_OD));
			pstmt.setDate(4,
					value.getAsSqlDate(KOLDataDictionary.KOL_KOLATERAL__VRIJEDI_DO));
			pstmt.setMaxRows(0);
			KolateralRs j2eers = new KolateralRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public KolateralRs daoListaKolaterala(KolateralVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM view_kol_kolaterali_pogled ");
		sql.appendln(" ORDER BY id_kolaterala DESC ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			KolateralRs j2eers = new KolateralRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoLoad(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM view_kol_kolaterali_pogled where id_kolaterala = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(KOLDataDictionary.KOL_KOLATERAL__ID_KOLATERALA));
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public KolateralRs daoListaKolateralaZaOsobu(KolateralVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM " + getTableName()
				+ " WHERE maticni_broj_jmbg = ? ");
		sql.append(" AND procjenjena_vrijednost > 0 ");
		sql.appendln(" ORDER BY vrsta, procjenjena_vrijednost ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setMaxRows(0);
			KolateralRs j2eers = new KolateralRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoFinVrsteKolaterala()  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT vrsta as id, rtrim(vrsta)+'-'+ltrim(naziv) as name FROM kol_kolateral_vrsta ");
		sql.appendln("ORDER BY vrsta ");
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
}