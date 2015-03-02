package hr.adriacomsoftware.app.server.gradani.kreditnizahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.KreditniZahtjevOsobaRs;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.KreditniZahtjevOsobaVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class KreditniZahtjevOsobaJdbc extends J2EEDataAccessObjectJdbc {
	public KreditniZahtjevOsobaJdbc() {
		setTableName("jb_gr_kreditni_zahtjev_osoba");
	}

	public KreditniZahtjevOsobaRs daoFind(KreditniZahtjevOsobaVo value){
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM jb_gr_kreditni_zahtjev_osoba WHERE id_zahtjeva = ?");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setBigDecimal(counter,value.getAsBigDecimal(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV__ID_ZAHTJEVA));
			counter++;
			pstmt.setMaxRows(0);
			KreditniZahtjevOsobaRs j2eers = new KreditniZahtjevOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public KreditniZahtjevOsobaRs daoFindJmbg(KreditniZahtjevOsobaVo value){
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM jb_gr_kreditni_zahtjev_osoba WHERE id_zahtjeva = ? and jmbg = ?");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setBigDecimal(
					counter,
					value.getAsBigDecimal(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV__ID_ZAHTJEVA));
			counter++;
			pstmt.setString(counter, value.getJmbg());
			pstmt.setMaxRows(0);
			KreditniZahtjevOsobaRs j2eers = new KreditniZahtjevOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}