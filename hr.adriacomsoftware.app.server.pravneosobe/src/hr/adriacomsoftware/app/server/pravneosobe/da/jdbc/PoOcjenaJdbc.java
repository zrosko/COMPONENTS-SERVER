package hr.adriacomsoftware.app.server.pravneosobe.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.rizik.dto.ZahOcjenaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class PoOcjenaJdbc extends BankarskiJdbc {
    public PoOcjenaJdbc() {
        setTableName("po_zah_ocjena");
    }
    public void daoSpPripremiOcjenePravnaOsoba(ZahOcjenaVo value)  {
    	J2EESqlBuilder sp = new J2EESqlBuilder();
	    if(value.getProperty("datum")==null)
	        value.set("datum", AS2Date.getTodayAsCalendar());
		sp.append("{call ");
		sp.append("stp_po_skoring_pravna_osoba_2014 ");
		sp.append(" (?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			int counter = 1;
			cs.setString(counter++,value.getMaticniBroj());
			cs.setString(counter++,value.getOib());
			cs.setDate(counter++,value.getAsSqlDate("datum"));
			int trenutni_izracun = 0;
			if(value.get("@@vrsta").equals("NOVO"))
			    trenutni_izracun = 1;
			cs.setInt(counter++,trenutni_izracun);
	        cs.setObject(counter++,value.getProperty("tip_entiteta"));
	        cs.setObject(counter++,value.getProperty("vrsta_klijenta"));
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
			
	}
    public AS2RecordList daoFindPravnaOsoba(AS2Record value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
    	sql.append("select * from fn_tbl_po_skoring_pravna_osoba(?,?,?,?) order by vrsta, pokazatelj");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("maticni_broj"));
			pstmt.setObject(2,value.getProperty("oib"));
			pstmt.setObject(3,value.getProperty("vrsta"));
			pstmt.setDate(4,value.getAsSqlDate("datum"));
			pstmt.setMaxRows(0);
			AS2RecordList as2rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }