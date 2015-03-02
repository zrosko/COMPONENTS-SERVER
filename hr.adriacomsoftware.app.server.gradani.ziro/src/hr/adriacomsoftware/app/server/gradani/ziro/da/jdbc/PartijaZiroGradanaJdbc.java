package hr.adriacomsoftware.app.server.gradani.ziro.da.jdbc;

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


public final class PartijaZiroGradanaJdbc extends BankarskiJdbc implements
		BankaConstants {
	public PartijaZiroGradanaJdbc() {
		setTableName("");
	}

	public OsobaRs daoPronadiOsobe(OsnovniVo value, boolean pretrazivanje)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_gr_ziro_pogled ");
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

	public PartijaRs daoKamateNaknade(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		short count = 1;
		sql.append("select * FROM dbo.bi_fn_gr_ziro_kamate_naknade(?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		// zr. 22.2.2012.sql.append(" order by organizacijska_jedinica");
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

	public PartijaRs daoListaPartijaBezPrometa(OsnovniVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		short count = 1;
		sql.append("select * FROM dbo.bi_fn_gr_ziro_bez_prometa(?,?,?,?,?) ");
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

	public PartijaRs daoListaPartijaKriterij(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM bi_view_gr_ziro_stanje ");
		sql.appGreatherOrEqual("AND", "stanje_partije",
				value.get(JBDataDictionary.BI_PARTIJA__STANJE_PARTIJE_OD));
		sql.appLessOrEqual("AND", "stanje_partije",
				value.get(JBDataDictionary.BI_PARTIJA__STANJE_PARTIJE_DO));
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY " + value.getOrderBy());
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet( pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoZiroSaPrometom(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_ziro_sa_prometom_100_7(?,?) ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet( pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PartijaRs daoZiroKamatneStope(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_ziro_partije_kamatne_stope(?) ");
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

	public PartijaRs daoZiroBlokirani(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_gr_ziro_blokirani(?) ");
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

	public PartijaRs daoRegistarRacuna(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * FROM dbo.bi_view_gr_ziro_registar_racuna ");
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