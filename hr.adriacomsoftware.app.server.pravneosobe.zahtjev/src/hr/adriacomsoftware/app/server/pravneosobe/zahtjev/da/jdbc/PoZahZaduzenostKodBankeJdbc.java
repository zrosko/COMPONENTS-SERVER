package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZaduzenostKodBankeVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahZaduzenostKodBankeJdbc extends PoZahJdbc {
    public PoZahZaduzenostKodBankeJdbc() {
        setTableName("po_zah_zaduzenost_kod_banke");
    }
    public AS2RecordList daoFind(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT  * from fn_po_zah_zaduzenost_kod_banke(?) ORDER BY vrsta_proizvoda");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public AS2RecordList daoFindPovezanaOsoba(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT  * from fn_po_zah_zaduzenost_kod_banke_povezane_osobe(?,?) ORDER BY jmbg_mb, vrsta_proizvoda");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setObject(2,value.get(ZAHDataDictionary.PO_ZAH_ZADUZENOST_KOD_BANKE__JMBG_MB));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		 }
	}
    public AS2RecordList daoFindPovezanaOsobaUkupno(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT  * from fn_po_zah_zaduzenost_kod_banke_povezane_osobe_ukupno (?) ORDER BY jmbg_mb, vrsta_proizvoda");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		 }
	}
    
    public void daoRemoveUcitaneZaduzenosti(ZaduzenostKodBankeVo value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_zaduzenost_kod_banke WHERE broj_zahtjeva = ? ");
		sql.append(" and jmbg_mb is null AND isnull(ucitano,0) = 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		 }
	}
    public void daoRemoveUcitaneZaduzenostiPovezaneOsobe(ZaduzenostKodBankeVo value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_zaduzenost_kod_banke WHERE broj_zahtjeva = ? and jmbg_mb = ? ");
		sql.append(" AND isnull(ucitano,0) = 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setObject(2, value.get(ZAHDataDictionary.PO_ZAH_ZADUZENOST_KOD_BANKE__JMBG_MB));
	        pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		 }
	}
    public void daoRemoveSveZaduzenostiPovezaneOsobe(ZaduzenostKodBankeVo value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_zaduzenost_kod_banke WHERE broj_zahtjeva = ? and jmbg_mb = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
			pstmt.setObject(2, value.get(ZAHDataDictionary.PO_ZAH_ZADUZENOST_KOD_BANKE__JMBG_MB));
	        pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		 }
	}
    
    public void daoRemoveSveZaduzenostiPovezanihOsoba(ZaduzenostKodBankeVo value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_zaduzenost_kod_banke WHERE broj_zahtjeva = ? ");
		sql.append(" AND isnull(ucitano,0) = 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		 }
			
	}
    public PartijaRs daoListaSvihKreditaGradana(ZaduzenostKodBankeVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT distinct * ");
        sql.appendln("FROM BI_PROD.dbo.fn_tbl_gr_partija_krediti(?,?,?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getJmbgMb());
	        pstmt.setObject(2,value.getOib());
	        if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
	            pstmt.setDate(3,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        else
	            pstmt.setDate(3,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		 }
    }
    public PartijaRs daoListaSvihTekucihGradana(ZaduzenostKodBankeVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT distinct * ");
        sql.appendln("FROM BI_PROD.dbo.fn_tbl_gr_partija_krediti_okvirni(?,?,?)"); //ZR.14.10.2010. odobreni_iznos <> 0
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getJmbgMb());
	        pstmt.setObject(2,value.getOib());
	        if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
	            pstmt.setDate(3,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        else
	            pstmt.setDate(3,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		 }
    }
 }