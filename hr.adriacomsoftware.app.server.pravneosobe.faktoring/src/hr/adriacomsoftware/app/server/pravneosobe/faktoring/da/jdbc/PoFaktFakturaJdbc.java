package hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.FakturaRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.FakturaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoFaktFakturaJdbc extends OLTPJdbc {
	public PoFaktFakturaJdbc() {
		setTableName("po_fakt_faktura");
	}

	public FakturaRs daoProcitajSveFakture(FakturaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from fn_tbl_fakt_fakture_pogled(?) ");
		sql.appendln(" ORDER BY sifra_valute, id_fakture");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdSpec());
			pstmt.setMaxRows(0);
			FakturaRs j2eers = new FakturaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}