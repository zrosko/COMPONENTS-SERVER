package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsRanjivostRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRanjivostVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class IsmsRanjivostJdbc extends CMDBJdbc {
    public IsmsRanjivostJdbc() {
        setTableName("isms_ranjivost");
    }
    public IsmsRanjivostRs daoPronadiRanjivosti(IsmsRanjivostVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM isms_ranjivost  ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsRanjivostRs j2eers = new IsmsRanjivostRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IsmsRanjivostRs daoListaRanjivosti(IsmsRanjivostVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_ranjivost_pogled order by vrsta_ranjivosti, naziv");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsRanjivostRs j2eers = new IsmsRanjivostRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}