package hr.adriacomsoftware.app.server.kolaterali.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.KOLDataDictionary;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralPolicaVo;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralRs;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class KolateralPolicaJdbc extends BankarskiJdbc {

	public KolateralPolicaJdbc() {
		setTableName("kol_kolateral_polica");
	}

	public AS2RecordList daoFind(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.append("SELECT komentar,osiguravajuca_kuca, broj_police, osigurana_svota, CONVERT(char(10), "
				+ "datum_dospijeca_police, 104) AS datum_dospijeca, CONVERT(decimal(10, 0), broj_partije) AS broj_partije, "
				+ "datum_dospijeca_police, datum_izdavanja_police, valuta_osigurane_svote, datum_dospijeca_premije, id_police, id_kolaterala FROM "
				+ getTableName()
				+ " where id_kolaterala = ? ORDER BY datum_dospijeca_police");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(KOLDataDictionary.KOL_KOLATERAL__ID_KOLATERALA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public KolateralRs daoPronadiPolice(KolateralPolicaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM view_kol_police_pogled  WHERE datum_dospijeca_police >= ? and datum_dospijeca_police <= ? ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appLike("AND", "naziv", value.getNaziv());
		sql.appLike("AND", "broj_police", value.getBrojPolice());
		sql.appLike("AND", "osiguravajuca_kuca", value.getOsiguravajucaKuca());
		sql.appendln(" ORDER BY osiguravajuca_kuca");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1,value.getAsSqlDate(KOLDataDictionary.KOL_KOLATERAL_POLICA__DATUM_DOSPIJECA_POLICE));
			pstmt.setDate(2,value.getAsSqlDate(KOLDataDictionary.KOL_KOLATERAL_POLICA__DATUM_DOSPIJECA_PREMIJE));
			pstmt.setMaxRows(0);
			KolateralRs j2eers = new KolateralRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public KolateralRs daoListaPolica(KolateralVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM view_kol_police_pogled ");
		sql.appendln(" ORDER BY NAZIV ");
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
}