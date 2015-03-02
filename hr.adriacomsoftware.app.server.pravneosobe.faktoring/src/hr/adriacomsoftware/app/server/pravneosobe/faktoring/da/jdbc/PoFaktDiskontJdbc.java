package hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.DiskontRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.DiskontVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoFaktDiskontJdbc extends OLTPJdbc {
	public PoFaktDiskontJdbc() {
		setTableName("po_fakt_diskont");
	}

	public DiskontRs daoFind(DiskontVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from po_fakt_diskont ");
		sql.appendln("ORDER BY 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			DiskontRs j2eers = new DiskontRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}