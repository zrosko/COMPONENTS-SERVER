package hr.adriacomsoftware.app.server.pranjenovca.gr.da.jdbc;

import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnPoliticarVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrnPoliticarJdbc extends BankarskiJdbc {

	public PrnPoliticarJdbc() {
		setTableName("prn_gr_upitnik_politicar");
	}

	public PrnPoliticarVo daoLoad(PrnPoliticarVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from prn_gr_upitnik_politicar WHERE id_upitnika = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getIdUpitnika());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			PrnPoliticarVo j2eers = new PrnPoliticarVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}