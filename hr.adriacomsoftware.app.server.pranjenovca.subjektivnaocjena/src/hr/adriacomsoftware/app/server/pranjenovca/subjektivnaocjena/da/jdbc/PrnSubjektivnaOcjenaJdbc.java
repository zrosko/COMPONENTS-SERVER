package hr.adriacomsoftware.app.server.pranjenovca.subjektivnaocjena.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrnSubjektivnaOcjenaJdbc extends J2EEDataAccessObjectJdbc {
	public PrnSubjektivnaOcjenaJdbc() {
		setTableName("prn_subjektivna_ocjena_BRISI");
	}

	public AS2RecordList daoFindOcjene(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_subjektivna_ocjena order by jmbg_mb,grupa,vrijedi_od ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2Record daoLoadOcjena(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_subjektivna_ocjena WHERE id_ocjene = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("id_ocjene"));
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			AS2Record j2eers = new AS2Record(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}
