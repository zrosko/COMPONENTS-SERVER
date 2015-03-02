package hr.adriacomsoftware.app.server.karticno.gr.da.jdbc;

import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevOcjenaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class McardGrZahtjevOcjenaJdbc extends OLTPJdbc {
	public McardGrZahtjevOcjenaJdbc() {
		setTableName("mcard_gr_zahtjev_ocjena");
	}
    public AS2RecordList daoFind(AS2Record value) 	{
    	J2EESqlBuilder sql = new J2EESqlBuilder();
    	sql.append("select * from dbo.fn_tbl_mcard_gr_zahtjev_skoring(?,?) order by vrsta, pokazatelj");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("broj_zahtjeva"));
			pstmt.setObject(2,value.getProperty("vrsta"));
			pstmt.setMaxRows(0);
			AS2RecordList mmrs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return mmrs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoSpPripremiOcjene(McardGrZahtjevOcjenaVo value)  {
    	J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_mcard_gr_zahtjev_skoring ");
		sp.append(" (?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
	        cs.setString(1,value.getBrojZahtjeva());
	        cs.setDate(2,value.getAsSqlDate("datum_ocjene"));			
	        cs.setObject(3,value.getProperty("tip_entiteta"));
	        cs.setObject(4,value.getProperty("vrsta_klijenta"));
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoRemoveSveOcjeneZaZahtjev(McardGrZahtjevOcjenaVo value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM mcard_gr_zahtjev_ocjena WHERE broj_zahtjeva = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getBrojZahtjeva());
	        int updateCounter = pstmt.executeUpdate();
	        if(updateCounter>9){//imamo samo 9 indikatora
	        	pstmt.close(); //TODO postaviti i u J2EEDataAccessObject klasu
	        	throw new AS2Exception(" 162");
	        }
			pstmt.close();
		} catch (AS2Exception e) {
			throw e;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}