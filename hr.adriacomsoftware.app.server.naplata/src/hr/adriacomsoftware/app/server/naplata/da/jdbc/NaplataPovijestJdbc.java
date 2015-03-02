package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class NaplataPovijestJdbc extends CMDBJdbc {
	public NaplataPovijestJdbc() {
		setTableName("j2ee_audit");
	}

	public NaplataRs daoProcitajPovijestPolja(NaplataVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select isnull(OldValue,'') as vrijednost_stara,isnull(NewValue,'') as vrijednost_nova, "
				+ " bi_prod.dbo.fnFormatDate(UpdateDate,'dd.MM.yyyy hh:mi:ss') as datum_izmjene,UserName as korisnik "
				+ " from CMDB_PROD.dbo.j2ee_audit "
				+ " where TableName='"
				+ value.get("TableName")
				+ "' and PrimaryKeyValue = '"
				+ value.get("PrimaryKeyValue")
				+ "' "
				+ " and FieldName='"
				+ value.get("FieldName")
				+ "' "
				+ " and isnull(OldValue,'') <> isnull(NewValue,'') "
				+ " order by UpdateDate desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

}