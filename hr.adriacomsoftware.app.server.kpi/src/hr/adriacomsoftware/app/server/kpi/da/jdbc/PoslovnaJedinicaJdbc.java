package hr.adriacomsoftware.app.server.kpi.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.KPIJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PoslovnaJedinicaJdbc extends KPIJdbc {
	public PoslovnaJedinicaJdbc() {
		setTableName("rdg_poslovna_jedinica");
	}

	public AS2RecordList daoFindPoslovneJedinice(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if(value.get("profitni_centar").equals("0"))
			sql.append("SELECT * from rdg_poslovna_jedinica order by naziv");
		else
			sql.append("SELECT * from rdg_poslovna_jedinica where profitni_centar = ? order by naziv");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			if(!value.get("profitni_centar").equals("0"))
				pstmt.setObject(1, value.get("profitni_centar"));
			pstmt.setMaxRows(0);
			AS2RecordList as2rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

}