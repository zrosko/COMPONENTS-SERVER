package hr.adriacomsoftware.app.server.kalkulatori.da.jdbc;

import hr.adriacomsoftware.app.common.kalkulatori.dto.FinancijskiKalkulatorVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class KalkulatoriJdbc extends OLTPJdbc {    
    public KalkulatoriJdbc() {
        setTableName("");
    }
    public String daoIzracunajZateznuKamatu(FinancijskiKalkulatorVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select dbo.fn_scl_fin_kamata_zakon(?,?,?,?) as kamatna_stopa");
        try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			int counter = 1;
			pstmt.setDate(counter++,value.getAsSqlDate(FinancijskiKalkulatorVo.DATUM_OD)); 
	        pstmt.setDate(counter++,value.getAsSqlDate(FinancijskiKalkulatorVo.DATUM_DO)); 
			pstmt.setObject(counter++,value.getGlavnica());
			pstmt.setObject(counter++,value.getKamatnjak());
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			FinancijskiKalkulatorVo j2eers = new FinancijskiKalkulatorVo(loc_rs);
			pstmt.close();
	        return j2eers.get("kamatna_stopa");
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }  
    public AS2RecordList daoIzracunajkOtplatniPlanEks(FinancijskiKalkulatorVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * from oltp_prod.dbo.fn_tbl_fin_kalkulator_otplatni_plan_eks(?,?,?,?,?,?,?,?,?,?,?) order by razdoblje");
		try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			int counter = 1;
			pstmt.setObject(counter++,value.getAsDouble(FinancijskiKalkulatorVo.OTPLATNI_PLAN__IZNOS_KREDITA)); 
			pstmt.setObject(counter++,value.getAsDouble(FinancijskiKalkulatorVo.KAMATNA_STOPA)); 
			pstmt.setObject(counter++,value.getAsDouble(FinancijskiKalkulatorVo.OTPLATNI_PLAN__TROSAK_KREDITA)); 
			pstmt.setObject(counter++,value.getAsDouble(FinancijskiKalkulatorVo.OTPLATNI_PLAN__POSTOTAK_NAKNADE)); 
			pstmt.setObject(counter++,value.getAsDouble(FinancijskiKalkulatorVo.OTPLATNI_PLAN__BROJ_GODINA)); 
			pstmt.setObject(counter++,value.getSifraValute()); 
			pstmt.setObject(counter++,value.getSifraProtuvalute()); 
	        pstmt.setDate(counter++,value.getAsSqlDate(FinancijskiKalkulatorVo.OTPLATNI_PLAN__DATUM_TECAJA)); 
	        pstmt.setObject(counter++,value.getAsDouble(FinancijskiKalkulatorVo.OTPLATNI_PLAN__TECAJ));
	        pstmt.setObject(counter++,value.getAsDouble(FinancijskiKalkulatorVo.OTPLATNI_PLAN__BROJ_RATA_POCEKA));
	        pstmt.setObject(counter++,value.getProperty(FinancijskiKalkulatorVo.OTPLATNI_PLAN__VRSTA_TECAJA)); 
			pstmt.setMaxRows(10000);//da ne dode do MEMORY limita
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());			
			pstmt.close();
	        return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }  
    public AS2RecordList daoIzracunajOtplatniPlan(FinancijskiKalkulatorVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select * from dbo.fn_tbl_fin_anuiteti(?,?,?,?,?,?,?,?) ");
        try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			int counter=0;
			pstmt.setObject(++counter,value.getIznosKredita());
			pstmt.setObject(++counter,value.getKamatnaStopa()); 
			pstmt.setObject(++counter,value.get("period_kamatne_stope"));
			pstmt.setObject(++counter,value.get("rok_otplate"));
			pstmt.setObject(++counter,value.get("period_otplate"));
			pstmt.setObject(++counter,value.get("nacin_otplate"));
			pstmt.setObject(++counter,value.get("vrsta_obracuna"));
			pstmt.setObject(++counter,value.get("anuiteti_vrsta"));
			pstmt.setMaxRows(10000);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    } 
    public AS2RecordList daoIzracunajKamatu(FinancijskiKalkulatorVo value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select * from dbo.fn_tbl_fin_obracun_kamate(?,?,?,?,?,?,?,?,?) ");
        try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        int counter=0;
			pstmt.setObject(++counter,value.getProperty("vrsta_obracuna"));
			pstmt.setObject(++counter,value.getProperty("br_dokumenta")); 
			pstmt.setObject(++counter,value.getAsDouble("glavnica"));
			pstmt.setDate(++counter,value.getAsSqlDate("datum_pocetak")); 
			pstmt.setDate(++counter,value.getAsSqlDate("datum_kraj")); 
			pstmt.setObject(++counter,value.getAsDouble("kamatna_stopa"));
			pstmt.setObject(++counter,value.getProperty("rok_otplate_jedinica"));
			pstmt.setObject(++counter,value.getAsDouble("iznos_za_obracun_prethodni"));
			pstmt.setObject(++counter,value.getAsDouble("kamata_prethodna"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
	        return as2_rs;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}