package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PoslovniOdnosBankaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PoslovniOdnosBankaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahPoslovniOdnosBankaJdbc extends PoZahJdbc {
    public PoZahPoslovniOdnosBankaJdbc() {
        setTableName("po_zah_poslovni_odnos_banka");
    }
    public PoslovniOdnosBankaVo daoCitajPOVIJEST(ZahtjevPravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder("SELECT P.* FROM dbo.fn_po_zah_pravna_osoba_podaci(?,?,?) as P");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());  
	        if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
	            pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        else
	            pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
	        //9.11.2009. promjena gore. 
	        //pstmt.setDate(2,value.getSqlDateFromCalendarAsItIs(value, ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        pstmt.setObject(3,value.getBrojZahtjeva());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close(); 
	        //POSLOVNI ODNOS BANKA
	        PoslovniOdnosBankaVo povrat_vo = new PoslovniOdnosBankaVo(j2eers);
	        return povrat_vo;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PoslovniOdnosBankaVo daoLoad(ZahtjevPravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT broj_zahtjeva, pocetak_poslovnog_odnosa, postotak_prometa, broj_djelatnika_placa,");
        sql.append("depoziti_trazitelja, stednja_vlasnika, broj_dionica_banke,udio_vlasnistva_banke, jamstveni_kapital, ");
        sql.append("rizicna_skupina, poseban_odnos, opis_posebnog_odnosa, komentar_poslovnog_odnosa,");
        sql.append("CONVERT(char(18), CONVERT(decimal(18, 0), broj_ziro_racuna)) AS broj_ziro_racuna, broj_dionica_banke_po, ");
        sql.append("izlozenost_prema_jam_kapitalu_po, izlozenost_prema_jam_kapitalu, iznos_izlozenosti, iznos_izlozenosti_po, ");
        sql.append("iznos_izlozenosti_neto, iznos_izlozenosti_neto_po ");
        sql.append("FROM po_zah_poslovni_odnos_banka WHERE broj_zahtjeva = ?");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getBrojZahtjeva());
	        pstmt.setMaxRows(1);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close(); 
	        //POSLOVNI ODNOS BANKA
	        PoslovniOdnosBankaVo povrat_vo = new PoslovniOdnosBankaVo(j2eers);
	        return povrat_vo;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PoslovniOdnosBankaRs daoCitajStednjuPovezanihOsoba(ZahtjevPravnaOsobaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder("SELECT * FROM dbo.fn_po_zah_stednja_povezanih_osoba(?,?) where iznos>0 ");
    	try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
	            pstmt.setDate(1,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        else
	            pstmt.setDate(1,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
	        pstmt.setObject(2,value.getBrojZahtjeva());
	        pstmt.setMaxRows(0);
	        PoslovniOdnosBankaRs j2eers = new PoslovniOdnosBankaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close(); 
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PoslovniOdnosBankaRs daoCitajDionicePovezanihOsoba(ZahtjevPravnaOsobaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder("SELECT * FROM fn_tbl_po_zah_dionice_povezanih_osoba(?,?) where broj_dionica>0 ");
    	try{
    		PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
	            pstmt.setDate(1,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        else
	            pstmt.setDate(1,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));
	        pstmt.setObject(2,value.getBrojZahtjeva());
	        pstmt.setMaxRows(0);
	        PoslovniOdnosBankaRs j2eers = new PoslovniOdnosBankaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close(); 
	        return j2eers;
    	} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }