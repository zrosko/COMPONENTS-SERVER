package hr.adriacomsoftware.app.server.naplata.da.jdbc;


import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspPrivitakVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class NaplataGrSspPrivitakJdbc extends OLTPJdbc {
    public NaplataGrSspPrivitakJdbc() {
        setTableName("naplata_gr_ssp_privitak");
    }
    public void daoRemove(NaplataGrSspPrivitakVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE naplata_gr_ssp_privitak SET ispravno = 0 " +
				   "WHERE id_privitka = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_privitka"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

 }