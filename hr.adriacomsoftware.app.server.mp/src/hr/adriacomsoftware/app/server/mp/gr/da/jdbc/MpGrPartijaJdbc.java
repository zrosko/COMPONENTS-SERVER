package hr.adriacomsoftware.app.server.mp.gr.da.jdbc;

import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrPartijaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public class MpGrPartijaJdbc extends TESTJdbc {
	public MpGrPartijaJdbc() {
		setTableName("mp_gr_partija");
	}

	public MpGrPartijaVo daoProvjeriPartiju(MpGrPartijaVo value){
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " + " from fn_tbs_mp_gr_partija_provjera(?,?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getBrojPartije());
			pstmt.setObject(2, null);
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			MpGrPartijaVo j2eevo = new MpGrPartijaVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}