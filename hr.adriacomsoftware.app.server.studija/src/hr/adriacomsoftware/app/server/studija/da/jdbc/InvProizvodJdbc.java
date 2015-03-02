package hr.adriacomsoftware.app.server.studija.da.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;



public class InvProizvodJdbc extends InvJdbc{

	public InvProizvodJdbc(){
		setTableName("inv_proizvod");
	}
	 public AS2RecordList daoFindListu(AS2Record value)  {    	
	    	J2EESqlBuilder sql = new J2EESqlBuilder();
	        sql.append(" order by id");//TODO
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

}
