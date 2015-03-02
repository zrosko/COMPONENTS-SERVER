package hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.KupacLimitRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.KupacLimitVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoFaktKupacLimitJdbc extends OLTPJdbc {
	public PoFaktKupacLimitJdbc() {
		setTableName("po_fakt_kupac_limit");
	}

	public KupacLimitRs daoFind(KupacLimitVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from po_fakt_kupac_limit where maticni_broj_kupca = ? ");
		sql.appendln("ORDER BY 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getMaticniBrojKupca());
			pstmt.setMaxRows(0);
			KupacLimitRs j2eers = new KupacLimitRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}