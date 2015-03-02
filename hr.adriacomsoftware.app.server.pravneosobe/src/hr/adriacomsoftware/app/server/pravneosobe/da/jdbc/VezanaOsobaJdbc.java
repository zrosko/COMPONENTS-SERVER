package hr.adriacomsoftware.app.server.pravneosobe.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.VezanaOsobaRs;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class VezanaOsobaJdbc extends J2EEDataAccessObjectJdbc {
    
    public VezanaOsobaJdbc() {
        setTableName("bi_po_vezana_osoba");
    }
    
    public VezanaOsobaRs daoFindSveVezaneOsobe(PravnaOsobaVo value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder("SELECT * FROM dbo.fn_tbs_po_osoba_vezane_osobe(?)");
    	try{
	    	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	    	pstmt.setObject(1,value.get("maticni_broj"));
	    	pstmt.setMaxRows(0);
	        AS2RecordList as2rs = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return new VezanaOsobaRs(as2rs);
    	} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }    
}