package hr.adriacomsoftware.app.server.gradani.da.as400.jdbc;

import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectAs400Jdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class RacunPartijeAs400Jdbc extends J2EEDataAccessObjectAs400Jdbc {
      
    public RacunPartijeAs400Jdbc() {
        setTableName("bsadb.accbalance"); 
    }

    public AS2RecordList daoLoad(String broj_partije) {
        J2EESqlBuilder sql = sqlBrojPartije(); 
        sql.appendln("acclass as broj_racuna,");
        sql.appendln("case when year(dateofbal) < 1753 OR year(dateofbal) = 3000 then null else dateofbal end  as datum_zadnjeg_prometa,");
        sql.appendln("(creamount1/100) - (debamount1/100) as stanje,");
        sql.appendln("case when year(timechange) < 1753 OR year(timechange) = 3000 then null else timechange end  as vrijeme_zadnje_izmjene ");
        sql.appendln("from bsadb.accbalance ");
        sql.appendln("where partno = '"+broj_partije+"'"); //10 znakova
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(100);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers; 
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 
}