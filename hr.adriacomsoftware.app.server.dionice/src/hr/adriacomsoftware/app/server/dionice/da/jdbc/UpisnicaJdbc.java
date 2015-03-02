package hr.adriacomsoftware.app.server.dionice.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;

import java.sql.PreparedStatement;


public final class UpisnicaJdbc extends OLTPJdbc {

	public UpisnicaJdbc() {
		setTableName("dio_upisnica");
	}

	public AS2RecordList daoLoadUpisnica(AS2Record value) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from view_dio_upisnica where id_upisnica = ? ");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setObject(1, value.getAsObject("id_upisnica"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2Exception(e);
		}
	}

	public AS2RecordList daoFindUpisnice(AS2Record value) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from view_dio_upisnica order by broj_zaprimanja desc ");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2Exception(e);
		}
	}

	public AS2RecordList daoFindDionicarByOib(AS2Record value) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from view_dio_upisnica_oib where oib = ? ");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setObject(1, value.get("oib"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2Exception(e);
		}
	}
}