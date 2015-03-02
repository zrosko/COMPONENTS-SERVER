package hr.adriacomsoftware.app.server.gradani.da.jdbc;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.gradani.dto.VezanaOsobaRs;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class VezanaOsobaJdbc extends J2EEDataAccessObjectJdbc {

	public VezanaOsobaJdbc() {
		setTableName("bi_gr_vezana_osoba");
	}

	public VezanaOsobaRs daoFindSveVezaneOsobe(OsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder("SELECT * FROM dbo.fn_tbs_gr_osoba_vezane_osobe(?)");
		try {
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("jmbg"));
			pstmt.setMaxRows(0);
			VezanaOsobaRs j2eers = new VezanaOsobaRs(
					transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}