package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class NaplataKlijentPrivitakJdbc extends OLTPJdbc {
	public NaplataKlijentPrivitakJdbc() {
		setTableName("naplata_klijent_privitak");
	}

	public void daoRemove(NaplataVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE naplata_klijent_privitak SET ispravno = 0 "
				+ "WHERE id_privitka = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getProperty("id_privitka"));
			pstmt.setMaxRows(0);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}