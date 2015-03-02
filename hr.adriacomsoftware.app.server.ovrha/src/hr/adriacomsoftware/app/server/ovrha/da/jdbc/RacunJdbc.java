package hr.adriacomsoftware.app.server.ovrha.da.jdbc;

import hr.adriacomsoftware.app.common.ovrha.dto.OvrhaRs;
import hr.adriacomsoftware.app.common.ovrha.dto.OvrhaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class RacunJdbc extends OLTPJdbc {

	public RacunJdbc() {
		setTableName("pons_racun");
	}

	public OvrhaRs daoFind(OvrhaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_pons_racun where id_ovrhe = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("id_ovrhe"));
			pstmt.setMaxRows(0);
			OvrhaRs j2eers = new OvrhaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}