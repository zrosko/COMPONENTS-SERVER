package hr.adriacomsoftware.app.server.obrasci.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.TESTObrasciJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PoljeJdbc extends TESTObrasciJdbc {

	public PoljeJdbc() {
		setTableName("dok_polje");
	}

	public AS2RecordList daoFindDropDownPolja(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_obrasci_combo_box(?,?) order by id_polja");
		int counter = 1;
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++, value.get("naziv"));
			pstmt.setObject(counter++, value.get("verzija"));
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return loc_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}
