package hr.adriacomsoftware.app.server.obrasci.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.TESTObrasciJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class VrstaJdbc extends TESTObrasciJdbc {

	public VrstaJdbc() {
		setTableName("dok_vrsta");
	}

	public AS2Record daoFindVrstuDokumentaPremaNazivu(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		int counter = 1;
		PreparedStatement pstmt;
		try{
			if (value.getAsObject("verzija") != null) {
				sql.append("SELECT top 1 * FROM test.dbo.dok_vrsta WHERE naziv = ? AND verzija = ?");
				pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setObject(counter++, value.getAsObject("naziv"));
				pstmt.setObject(counter++, value.getAsObject("verzija"));
			} else {
				sql.append("SELECT top 1 * FROM test.dbo.dok_vrsta WHERE naziv = ? AND verzija is null");
				pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setObject(counter++, value.getAsObject("naziv"));
			}
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			AS2Record j2eevo = new AS2Record(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}
