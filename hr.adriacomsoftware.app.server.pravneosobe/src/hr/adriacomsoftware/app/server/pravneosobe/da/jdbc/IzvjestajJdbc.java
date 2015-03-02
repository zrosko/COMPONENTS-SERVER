package hr.adriacomsoftware.app.server.pravneosobe.da.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class IzvjestajJdbc extends J2EEDataAccessObjectJdbc {
	public IzvjestajJdbc() {
		setTableName("??");
	}

	public OsnovniRs daoListaPrometNaDan26017(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from bi_view_po_glavna_knjiga ");
		sql.append("where (datum_knjizenja='");
		sql.append(value.get("datum").substring(0, 10));
		sql.append("') AND (potrazuje<>0 OR DUGUJE <>0) AND broj_konta='26017' ");
		sql.appendln("ORDER BY maticni_broj,broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}