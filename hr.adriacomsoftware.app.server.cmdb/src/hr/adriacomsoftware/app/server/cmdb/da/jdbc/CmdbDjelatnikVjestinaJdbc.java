package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.DjelatnikVjestinaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class CmdbDjelatnikVjestinaJdbc extends CMDBJdbc {
	public CmdbDjelatnikVjestinaJdbc() {
		setTableName("cmdb_djelatnik_vjestina");
	}

	public AS2RecordList daoFind(DjelatnikVjestinaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT RTRIM(D.ime)+' '+RTRIM(D.prezime) AS ime_prezime,V.ocjena,");
		sql.append("V.opis,CONVERT(decimal(13, 0), V.jmbg) AS jmbg, V.id_vjestine ");
		sql.append("FROM cmdb_djelatnik_vjestina AS V LEFT OUTER JOIN ");
		sql.append("cmdb_djelatnik AS D ON D.jmbg = V.jmbg ");
		sql.append("WHERE id_vjestine = ? ");
		sql.append("order by id_vjestine");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setString(1, value.getIdVjestine());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}