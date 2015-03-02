package hr.adriacomsoftware.app.server.mp.po.da.jdbc;

import hr.adriacomsoftware.app.common.mp.po.dto.MpPoPartijaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;


public final class MpPoPartijaJdbc extends TESTJdbc {
	public MpPoPartijaJdbc() {
		setTableName("mp_po_partija");
	}

	public MpPoPartijaVo daoProvjeriPartiju(MpPoPartijaVo value) {
		J2EEConnectionJDBC co = null;
		MpPoPartijaVo j2eevo = null;
		co = getConnection();
		Connection jco = co.getJdbcConnection();
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " + " from fn_tbs_mp_po_partija_provjera(?,?) ");
		try{
			PreparedStatement pstmt = jco.prepareStatement(sql.toString());
			pstmt.setObject(1, value.getBrojPartije());
			pstmt.setObject(2, null);
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			j2eevo = new MpPoPartijaVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}