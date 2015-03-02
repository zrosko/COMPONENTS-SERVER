package hr.adriacomsoftware.app.server.obrasci.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.TESTJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

/************* DAO_JDBC GrSifarnikJdbc ************/

public class DokumentSifrarnikJdbc extends TESTJdbc {
	public DokumentSifrarnikJdbc() {
		setTableName("dok_dokument_sifrarnik");
	}
	public AS2RecordList daoFindSifrarnik(AS2Record value, boolean pretrazivanje) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from view_dok_dokument_sifrarnik ");
		sql.append(" where aplikacija in ('sve','" + value.get("aplikacija")
				+ "')   ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" order by vrsta, rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}