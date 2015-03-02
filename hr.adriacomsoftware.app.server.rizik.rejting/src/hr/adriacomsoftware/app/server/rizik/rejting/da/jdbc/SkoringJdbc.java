package hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc;

import hr.adriacomsoftware.app.common.rizik.rejting.dto.SkoringRs;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.SkoringVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.RIZIKJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class SkoringJdbc extends RIZIKJdbc {
    
    public SkoringJdbc() {
        setTableName("kr_skoring");
    }
    public SkoringRs daoFindSkoringPravneOsobe(SkoringVo value) 	{
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.append("select * from fn_tbl_po_skoring_ponderiranje(?,?,?,?,?,?) order by vrsta, pokazatelj");
		if(value.exists("@@LOG"))
		    sql.append("select * from fn_tbl_po_skoring_ponderiranje_log(?,?,?,?,?,?) order by vrsta, pokazatelj");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("maticni_broj"));
			pstmt.setObject(2,value.getProperty("oib"));
			pstmt.setObject(3,value.getProperty("vrsta"));
			pstmt.setDate(4,value.getAsSqlDate("datum"));
			pstmt.setObject(5,null);
			pstmt.setObject(6,null);
			pstmt.setMaxRows(0);
			SkoringRs j2eers = new SkoringRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoNoviSkoringPravneOsobe(SkoringVo value)    {
    	J2EESqlBuilder sp = new J2EESqlBuilder();        
        sp.append("{call stp_po_rejting_pravna_osoba (?,?,?,?,?) }");
        try{
	        CallableStatement cs = getConnection().getCallableStatement(sp.toString());
	        cs.setObject(1,value.getProperty("maticni_broj"));
	        cs.setObject(2,value.getProperty("oib"));
	        cs.setDate(3,value.getAsSqlDate("datum"));
	        cs.setObject(4,value.getProperty("tip_entiteta"));
	        cs.setObject(5,value.getProperty("vrsta_klijenta"));
	        cs.execute();
	        cs.close();
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoSpAzurirajPondere(SkoringVo value)  {
//        	StringBuffer sp = new StringBuffer();
//          sp.append("{call ");
//          sp.append("stp_po_rejting_pravna_osoba_izmjena ");
//          sp.append(" (?,?,?) }");
//          CallableStatement cs = getConnection().getCallableStatement(sp.toString());
//          cs.setObject(1,value.getMaticniBroj());
//          cs.setObject(2,value.getVrsta());
//          cs.setDate(3,value.getSqlDateFromCalendarAsEndOfDay(value, "datum"));
//          cs.execute();
//          cs.close();
    }
    public String daoFnPripremJednuOcjenu(SkoringVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("select RIZIK_PROD.dbo.fn_scl_kr_po_pokazatelj_2014(?,?,?,?) as ocjena ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getProperty("pokazatelj"));
	        pstmt.setObject(2,value.getProperty("grupa"));
	        pstmt.setObject(3,value.getProperty("vrijednost_pokazatelja"));
	        pstmt.setDate(4,value.getAsSqlDate("datum_ocjene"));
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers.get("ocjena");
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoMjesecniSkoringPravneOsobe(SkoringVo value, int vrsta_pokazatelja)    {
    	J2EESqlBuilder sp = new J2EESqlBuilder();        
        sp.append("{call stp_po_rejting_pravna_osoba_mjesecno (?,?,?,?,?,?) }");
        try{
	        CallableStatement cs = getConnection().getCallableStatement(sp.toString());
	        cs.setObject(1,value.getProperty("maticni_broj"));
	        cs.setObject(2,value.getProperty("oib"));
	        cs.setDate(3,value.getAsSqlDate("datum_obrade"));
	        cs.setObject(4,value.getProperty("tip_entiteta"));
	        cs.setObject(5,value.getProperty("vrsta_klijenta"));
	        cs.setInt(6,vrsta_pokazatelja);//1-opći,...9-rejtting
	        cs.execute();
	        cs.close();
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}