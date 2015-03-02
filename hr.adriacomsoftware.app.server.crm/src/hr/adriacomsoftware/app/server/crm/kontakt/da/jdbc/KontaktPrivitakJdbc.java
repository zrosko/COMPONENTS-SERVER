package hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc;

import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktPrivitakVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class KontaktPrivitakJdbc extends KontaktJdbc {

	public KontaktPrivitakJdbc() {
		setTableName("kontakt_privitak");
	}

    public void daoRemove(KontaktPrivitakVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE kontakt_privitak SET ispravno = 0 " +
				   "WHERE id_privitka = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getAsObject("id_privitka"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}        
    }
}