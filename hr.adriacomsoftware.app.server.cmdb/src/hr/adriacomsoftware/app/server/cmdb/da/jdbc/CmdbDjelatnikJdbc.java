package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbDjelatnikVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class CmdbDjelatnikJdbc extends CMDBJdbc {
	public CmdbDjelatnikJdbc() {
		setTableName("cmdb_djelatnik");
	}

	public AS2RecordList daoFind(CmdbDjelatnikVo value, boolean pretrazivanje) {
		return daoFind(value, pretrazivanje, false);
	}

	public AS2RecordList daoFindBezOvlasti(CmdbDjelatnikVo value,
			boolean pretrazivanje) {
		return daoFind(value, pretrazivanje, false);
	}

	public AS2RecordList daoFind(CmdbDjelatnikVo value, boolean pretrazivanje,
			boolean ovlasti) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_cmdb_djelatnik_pogled ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		if (ovlasti)
			sql = odrediRazinuOvlasti(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		if (pretrazivanje) {
			sql.appLike("AND", "ime_prezime", value.get("ime_prezime"));
			sql.appEqualNoQuote("AND", "jmbg", value.getJmbg());
		}
		sql.appendln("ORDER BY ime_prezime");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoGetOrgJedinicaRada(String value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select org_jedinica_rada from cmdb_djelatnik ");
		sql.appEqualNoQuote("AND", "jmbg", value);
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(
					sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers.get("org_jedinica_rada");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}