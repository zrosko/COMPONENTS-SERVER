package hr.adriacomsoftware.app.server.evidencijadolazaka.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public class PlaceSifrarnikJdbc extends OLTPJdbc {
	public PlaceSifrarnikJdbc () {
		setTableName("place_sifrarnik");
	}

	public AS2RecordList daoFindSifrarnik(AS2Record value, boolean pretrazivanje) {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from view_place_sifrarnik ");
		sql.append(" order by vrsta, rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}