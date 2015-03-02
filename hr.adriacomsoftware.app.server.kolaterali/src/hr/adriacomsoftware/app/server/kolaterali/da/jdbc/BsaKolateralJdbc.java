package hr.adriacomsoftware.app.server.kolaterali.da.jdbc;

import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralRs;
import hr.adriacomsoftware.app.common.kolaterali.dto.KolateralVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KolateralPonudeniRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostKodBankeVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class BsaKolateralJdbc extends BankarskiJdbc {

	public BsaKolateralJdbc() {
		setTableName("bi_kolateral");
	}

	public KolateralRs daoListaKolateralaZaOsobu(KolateralVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT * FROM view_kol_bsa_kolaterali WHERE maticni_broj_jmbg = ? ");
		sql.append(" AND isnull(procjenjena_vrijednost,0) > 0 ");
		sql.appendln(" ORDER BY vrsta, procjenjena_vrijednost ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBrojJmbg());
			pstmt.setMaxRows(0);
			KolateralRs j2eers = new KolateralRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	// čita postojeće kolaterale u BSA
	public KolateralPonudeniRs daoListaKolateralaZaPartiju(ZaduzenostKodBankeVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT V.naziv as vrsta_osiguranja, K.* FROM "
				+ getTableName() + " AS K LEFT OUTER JOIN ");
		sql.appendln(" kol_kolateral_vrsta AS V ON V.vrsta = K.vrsta ");
		sql.appendln(" WHERE K.id_kolaterala in ( ");
		sql.append(" select id_kolaterala FROM dbo.bi_kolateral_partija where broj_partije = ?) ");
		sql.appendln(" ORDER BY K.vrsta, K.procjenjena_vrijednost ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getBrojPartije());
			pstmt.setMaxRows(0);
			KolateralPonudeniRs j2eers = new KolateralPonudeniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}