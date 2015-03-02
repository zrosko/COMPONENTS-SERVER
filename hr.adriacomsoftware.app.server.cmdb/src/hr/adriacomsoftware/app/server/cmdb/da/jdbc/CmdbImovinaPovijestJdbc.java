package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class CmdbImovinaPovijestJdbc extends CMDBJdbc {
    public CmdbImovinaPovijestJdbc() {
        setTableName("cmdb_imovina_povijest");
    }
    public CmdbImovinaRs daoPronadiPovijestImovine(CmdbImovinaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_cmdb_imovina_povijest " +
        		     "where id_imovine = " + value.getIdImovine());
        sql.appendln(" ORDER BY datum_akcije");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        CmdbImovinaRs j2eers = new CmdbImovinaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}