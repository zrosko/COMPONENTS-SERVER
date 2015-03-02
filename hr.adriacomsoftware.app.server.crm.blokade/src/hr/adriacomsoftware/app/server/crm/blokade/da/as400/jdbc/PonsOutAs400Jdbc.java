package hr.adriacomsoftware.app.server.crm.blokade.da.as400.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectAs400Jdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PonsOutAs400Jdbc extends J2EEDataAccessObjectAs400Jdbc {
    public PonsOutAs400Jdbc() {
        setTableName("BSADB.PONS_OUT"); // produkcija  
    }
    public AS2RecordList daoListajPoruke(OsnovniVo value) {
            J2EESqlBuilder sql = new J2EESqlBuilder(); 
            sql.appendln("SELECT *");
            sql.appendln("FROM BSADB.PONS_OUT ");
            sql.appendln("WHERE PONSDATE >='"+value.getDDMMYYYY("datum")+"'"); 
            sql.appendln(" AND MSGID = '"+value.get("msgid")+"'");
            sql.appendln("ORDER BY MSGID ");
            try{
	            PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	            pstmt.setMaxRows(2000);
	            AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	            pstmt.close();
	            return j2eers;    
            } catch (Exception e) {
    			throw new AS2DataAccessException(e);
    		}
    }
}