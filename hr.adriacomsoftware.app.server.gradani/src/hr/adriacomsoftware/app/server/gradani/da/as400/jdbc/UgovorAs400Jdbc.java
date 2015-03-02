package hr.adriacomsoftware.app.server.gradani.da.as400.jdbc;

import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectAs400Jdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class UgovorAs400Jdbc extends J2EEDataAccessObjectAs400Jdbc {
    public static final String MAX_BROJ_STAVKE_SQL = "SELECT max(fintranno) as max_broj_stavke FROM bsadb.fintran ";
        
    public UgovorAs400Jdbc() {
        setTableName("bsadb.fintranTEST"); // produkcija  ??
    }

    public AS2RecordList daoLoad(String broj_partije) {
        //pronadi zadnji broj stavke u BI bazi
        //citaj podatke iz BSA
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * from bsadb.CONTRACT ");
        sql.appendln("where ( partno BETWEEN 320000707 AND 320000707 ) and year(dateto)> 2050 order by dateto desc"); 
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(1000);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers; 
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 
}