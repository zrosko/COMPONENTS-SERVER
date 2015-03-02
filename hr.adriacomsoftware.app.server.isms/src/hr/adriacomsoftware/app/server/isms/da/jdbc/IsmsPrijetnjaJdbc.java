package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsPrijetnjaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsPrijetnjaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class IsmsPrijetnjaJdbc extends CMDBJdbc {
    public IsmsPrijetnjaJdbc() {
        setTableName("isms_prijetnja");
    }
    public IsmsPrijetnjaRs daoPronadiPrijetnje(IsmsPrijetnjaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM isms_prijetnja  ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsPrijetnjaRs j2eers = new IsmsPrijetnjaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IsmsPrijetnjaRs daoListaPrijetnji(IsmsPrijetnjaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_prijetnja_pogled order by vrsta_prijetnje, izvor, naziv");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsPrijetnjaRs j2eers = new IsmsPrijetnjaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}