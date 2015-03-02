package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class NaplataGrSspPovijestJdbc extends CMDBJdbc {
    public NaplataGrSspPovijestJdbc() {
        setTableName("j2ee_audit");
    }

    public NaplataGrSspRs daoProcitajPovijestPolja(NaplataGrSspVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select isnull(OldValue,'') as vrijednost_stara,isnull(NewValue,'') as vrijednost_nova, " +
				   " bi_prod.dbo.fnFormatDate(UpdateDate,'dd.MM.yyyy hh:mi:ss') as datum_izmjene,UserName as korisnik " +
				   " from CMDB_PROD.dbo.j2ee_audit " +
				   " where TableName='"+value.get("TableName")+"' and PrimaryKeyValue = '"+value.get("PrimaryKeyValue")+"' " +
				   " and FieldName='"+value.get("FieldName")+"' " +
				   " and isnull(OldValue,'') <> isnull(NewValue,'') " +
				   " order by UpdateDate desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			NaplataGrSspRs j2eers = new  NaplataGrSspRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return new NaplataGrSspRs(j2eers);			
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    
 }