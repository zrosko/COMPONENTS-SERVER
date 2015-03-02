package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.rizik.dto.ZahOcjenaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class PoZahOcjenaJdbc extends PoZahJdbc {
    public PoZahOcjenaJdbc() {
        setTableName("po_zah_ocjena");
    }
    public AS2RecordList daoFind(AS2Record value) 	{
    	J2EESqlBuilder sql = new J2EESqlBuilder();
    	sql.append("select * from fn_tbl_po_skoring_zahtjev(?,?,?) order by vrsta, pokazatelj");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("maticni_broj"));
			pstmt.setObject(2,value.getProperty("broj_zahtjeva"));
			pstmt.setObject(3,value.getProperty("vrsta"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public String daoFnPripremJednuOcjenu(ZahOcjenaVo value)  {
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
    public void daoSpPripremiOcjene(ZahOcjenaVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_po_skoring_zahtjev_2014 ");
		sp.append(" (?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
	        cs.setObject(1,value.getProperty("maticni_broj"));
	        cs.setObject(2,value.getProperty("oib"));
	        cs.setDate(3,value.getAsSqlDate("datum"));			
			int trenutni_izracun = 0;
			if(value.get("@@vrsta").equals("NOVO"))
			    trenutni_izracun = 1;
			cs.setInt(4,trenutni_izracun);
	        cs.setObject(5,value.getProperty("tip_entiteta"));
	        cs.setObject(6,value.getProperty("vrsta_klijenta"));
	         cs.setString(7,value.getBrojZahtjeva());
			/*boolean res =*/ cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoSpAzurirajPondere(ZahOcjenaVo value)  {
        J2EESqlBuilder sp = new J2EESqlBuilder();
        sp.append("{call ");
        sp.append("stp_po_skoring_zahtjev_izmjena ");
        sp.append(" (?,?) }");
        try{
        	CallableStatement cs = getConnection().getCallableStatement(sp.toString());
	        cs.setObject(1,value.getBrojZahtjeva());
	        cs.setObject(2,value.getVrsta());
	        cs.execute();
	        cs.close();
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    //******* PRAVNA OSOBA ************************/
    public void daoSpPripremiOcjenePravnaOsoba(ZahOcjenaVo value)  {
    	J2EESqlBuilder sp = new J2EESqlBuilder();
	    if(value.getProperty("datum")==null)
	        value.set("datum", AS2Date.getTodayAsCalendar());
		sp.append("{call ");
		sp.append("stp_po_skoring_pravna_osoba_2014 ");
		sp.append(" (?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setString(1,value.getMaticniBroj());
			cs.setString(2,value.getOib());
			cs.setDate(3,value.getAsSqlDate("datum"));
			int trenutni_izracun = 0;
			if(value.get("@@vrsta").equals("NOVO"))
			    trenutni_izracun = 1;
			cs.setInt(4,trenutni_izracun);
	        cs.setObject(5,value.getProperty("tip_entiteta"));
	        cs.setObject(6,value.getProperty("vrsta_klijenta"));
			/*boolean res =*/ cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoSpAzurirajPonderePravnaOsoba(ZahOcjenaVo value)  {
        J2EESqlBuilder sp = new J2EESqlBuilder();
        sp.append("{call ");
        sp.append("stp_po_skoring_pravna_osoba_izmjena ");
        sp.append(" (?,?,?,?) }");
        try{
        	CallableStatement cs = getConnection().getCallableStatement(sp.toString());
	        cs.setObject(1,value.getMaticniBroj());
	        cs.setObject(2,value.getOib());
	        cs.setDate(3,value.getAsSqlDate("datum_ocjene"));
	        cs.setObject(4,value.getVrsta());
	        cs.execute();
	        cs.close();
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoFindPravnaOsoba(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbl_po_skoring_pravna_osoba(?,?,?,?) order by vrsta, pokazatelj");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("maticni_broj"));
			pstmt.setObject(2,value.getProperty("oib"));
			pstmt.setObject(3,value.getProperty("vrsta"));
			pstmt.setDate(4,value.getAsSqlDate("datum"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }