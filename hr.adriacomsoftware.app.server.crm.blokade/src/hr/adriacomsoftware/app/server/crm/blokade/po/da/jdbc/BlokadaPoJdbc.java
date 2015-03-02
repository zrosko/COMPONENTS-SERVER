package hr.adriacomsoftware.app.server.crm.blokade.po.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.CRMJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class BlokadaPoJdbc extends CRMJdbc {
    public static final String LISTA_BLOKADA_PO_SQL = "select * from dbo.view_po_status_potrazivanja_201 order by datum_blokade desc";
    
    public BlokadaPoJdbc() {
        setTableName("");
    }
    public AS2RecordList daoFindListuBlokadaPo(AS2Record value) {
    	try{
	    	PreparedStatement pstmt = getConnection().getPreparedStatement(LISTA_BLOKADA_PO_SQL);
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers;
    	} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}