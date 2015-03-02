package hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.KupacFaktoringaRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.KupacFaktoringaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoFaktKupacJdbc extends OLTPJdbc {
	public PoFaktKupacJdbc() {
		setTableName("po_fakt_kupac");
	}

	public KupacFaktoringaRs daoProcitajSveKupce(KupacFaktoringaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from view_po_fakt_kupci_faktoringa_pogled ");
		sql.appendln(" ORDER BY naziv_kupca");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			KupacFaktoringaRs j2eers = new KupacFaktoringaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}