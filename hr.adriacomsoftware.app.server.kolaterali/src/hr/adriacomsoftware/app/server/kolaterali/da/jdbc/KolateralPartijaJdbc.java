package hr.adriacomsoftware.app.server.kolaterali.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.KOLDataDictionary;
import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralPartijaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class KolateralPartijaJdbc extends J2EEDataAccessObjectJdbc {

	public KolateralPartijaJdbc() {
		setTableName("kol_kolateral_partija");
	}

	public AS2RecordList daoFind(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM dbo.fn_po_kol_partije_kolaterala(?) ");
		sql.append("ORDER BY datum_upisa desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get(KOLDataDictionary.KOL_KOLATERAL__ID_KOLATERALA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public boolean daoFindIfExists(KolateralPartijaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT broj_partije FROM kol_kolateral_partija ");
		sql.append(" WHERE (broj_partije = ? and id_kolaterala = ? )");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getBrojPartije());
			pstmt.setObject(2, value.getIdKolaterala());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			if (j2eers.size() > 0)
				return true;
			return false;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoRemoveUcitaneZaduzenosti(KolateralPartijaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM kol_kolateral_partija WHERE id_kolaterala = ? ");
		sql.append(" AND isnull(ucitano,0) = 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}