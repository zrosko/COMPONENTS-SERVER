package hr.adriacomsoftware.app.server.pravneosobe.bonitet.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;
import java.util.GregorianCalendar;

public final class BonitetJdbc extends J2EEDataAccessObjectJdbc {
	public BonitetJdbc() {
		setTableName("");
	}

	public AS2RecordList daoCitajBilancu(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * ");
		sql.appendln("FROM dbo.bi_fn_po_bonitet_bilanca(?,?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setInt(2, value.getDatum().get(GregorianCalendar.YEAR) - 1);
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoCitajRacunDobitiGubitka(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * ");
		sql.appendln("FROM dbo.bi_fn_po_bonitet_racun_dobiti_gubitka(?,?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setInt(2, value.getDatum().get(GregorianCalendar.YEAR) - 1);
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoCitajStanjaZaReferat(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * ");
		sql.appendln("FROM po_zah_bonitet WHERE broj_zahtjeva = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoCitajStanjaZaReferatBONITET(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_bonitet_referat(?,?,?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setDate(2, value.getAsSqlDate("datum_obrade"));
			pstmt.setObject(3,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoCitajStatistickiZaReferat(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * ");
		sql.appendln("FROM po_zah_bonitet_statisticko WHERE broj_zahtjeva = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoCitajStatistickiZaReferatBONITET(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM dbo.bi_fn_po_bonitet_referat_statisticko_NOVO(?,?,?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBroj());
			pstmt.setDate(2, value.getAsSqlDate("datum_obrade"));
			pstmt.setObject(3, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoCitajBonitetZaBSA(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * ");
		sql.appendln("FROM RIZIK_PROD.dbo.fn_tbl_bonitet_izvoz_bsa(?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}