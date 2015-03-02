package hr.adriacomsoftware.app.server.helpdesk.da.jdbc;

import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class HdPozivStatusJdbc extends CMDBJdbc {
	public HdPozivStatusJdbc() {
		setTableName("hd_poziv_status");
	}

	public String daoJmbgEmail(String value) {
		String sql = "select email " + "from cmdb_djelatnik where jmbg = "
				+ value + " ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			HelpDeskPozivVo j2eers = new HelpDeskPozivVo(loc_rs);
			pstmt.close();
			return j2eers.get("email");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}