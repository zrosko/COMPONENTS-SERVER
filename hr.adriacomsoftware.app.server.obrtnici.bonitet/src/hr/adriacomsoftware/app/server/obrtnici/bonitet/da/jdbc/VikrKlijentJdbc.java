package hr.adriacomsoftware.app.server.obrtnici.bonitet.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.obrasci.dto.VikrStavkaVo;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class VikrKlijentJdbc extends PoZahJdbc {
    public VikrKlijentJdbc() {
        setTableName("po_vikr_klijent");
    }
    public boolean daoFindIfExists(VikrStavkaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT 1 FROM po_vikr_klijent ");
        sql.append(" WHERE datum_stanja = ? AND (maticni_broj = ? )" );// OR oib = ?
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(VikrStavkaVo.PO_VIKR_KLIJENT__DATUM_STANJA));
	        pstmt.setObject(2, value.getMaticniBroj());
	        pstmt.setMaxRows(1);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        if (j2eers.size()>0)
	            return true;
	        return false; 
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public String daoZadnjiIdVikrKlijenta(VikrStavkaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT isnull(max(id_vikr_klijenta),0) as id_vikr_klijenta FROM po_vikr_klijent ");
        sql.append("WHERE  (maticni_broj = ? )" );// OR oib = ?
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getMaticniBroj());
	        pstmt.setMaxRows(1);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers.get("id_vikr_klijenta");
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}