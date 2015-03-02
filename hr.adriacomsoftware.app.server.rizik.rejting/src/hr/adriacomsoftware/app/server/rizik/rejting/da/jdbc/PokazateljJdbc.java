package hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc;

import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljRs;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.RIZIKJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;
import java.util.Calendar;
//29.07.2014. zrosko _log dodatak.

public final class PokazateljJdbc extends RIZIKJdbc {
    
    public PokazateljJdbc() {
        setTableName("kr_pokazatelj_log");
    }
    public PokazateljRs daoFindZaRazdoblje(PokazateljVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM kr_pokazatelj_log  ");
        sql.append(" ORDER BY vrsta,pokazatelj ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        PokazateljRs j2eers = new PokazateljRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PokazateljVo daoLoad(PokazateljVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM kr_pokazatelj_log  ");
        sql.append(" where pokazatelj = ? ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getPokazatelj());
	        pstmt.setMaxRows(0);
	        PokazateljVo j2eers = new PokazateljVo(transformResultSetOneRow(pstmt.executeQuery()));
	        sql = new J2EESqlBuilder();
	        sql.append("select tip_entiteta from kr_tip_entiteta_pokazatelj_log where pokazatelj = ?");
	        pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getPokazatelj());
	        pstmt.setMaxRows(0);
	        PokazateljRs pok_rs = new PokazateljRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        j2eers.setTipEntiteta(pok_rs.getValueList("tip_entiteta"));
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PokazateljRs daoIzracunajPokazatelje(PokazateljVo value)  {
        PokazateljRs j2eers = null;
        J2EESqlBuilder sql = new J2EESqlBuilder();
        try{
	        if(value.exists("@@broj_defaulta")){
	            sql.appendln("SELECT * FROM fn_tbl_kr_po_skoring_upit_financijski(?,?) ");
	            sql.append(" ORDER BY naziv_osobe "); 
	            PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	            Calendar _cal1 = value.getAsCalendar("datum_od");
	            int _godina1 = _cal1.get(Calendar.YEAR);
	            Calendar _cal2 = value.getAsCalendar("datum_do");
	            int _godina2 = _cal2.get(Calendar.YEAR);
	            pstmt.setInt(1,_godina1);
	            pstmt.setInt(2,_godina2);
	            pstmt.setMaxRows(0);
	            j2eers = new PokazateljRs(transformResultSet(pstmt.executeQuery()));
	            pstmt.close();
	        }else{
	            sql.appendln("SELECT * FROM fn_tbl_kr_po_skoring_upit(?,?,?,?) ");
	            sql.append(" ORDER BY maticni_broj,vrsta, pokazatelj ");                
	            PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	            pstmt.setObject(1,value.getProperty("maticni_broj"));
	            pstmt.setObject(2,value.getProperty("oib"));
	            pstmt.setObject(3,value.getProperty("godina"));
	            pstmt.setObject(4,value.getProperty("vrsta"));
	            pstmt.setMaxRows(0);
	            j2eers = new PokazateljRs(transformResultSet(pstmt.executeQuery())); 
	            pstmt.close();
	        }            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PokazateljRs daoIzracunajRanoUpozorenje(PokazateljVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("select * from dbo.fn_tbl_kr_rano_upozorenje(?) ");
        sql.append(" ORDER BY naziv "); 
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate("datum"));
	        pstmt.setMaxRows(0);
	        PokazateljRs j2eers = new PokazateljRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close(); 
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}