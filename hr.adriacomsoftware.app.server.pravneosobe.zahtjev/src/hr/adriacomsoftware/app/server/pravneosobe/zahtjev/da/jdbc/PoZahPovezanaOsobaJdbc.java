package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PovezanaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.PovezanaOsobaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoZahPovezanaOsobaJdbc extends PoZahJdbc {
    public PoZahPovezanaOsobaJdbc() {
        setTableName("po_zah_povezana_osoba");
    }
    public AS2RecordList daoFind(AS2Record aFields)  {
        return daoFind(aFields, false);
	}
    public AS2RecordList daoFind(AS2Record value, boolean vlasnici) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (vlasnici)
		    sql.append("SELECT ");
		else
		    sql.append("SELECT PO.tip_veze,");
		sql.append(" CONVERT(char(15), CONVERT(decimal(15, 0), jmbg_mb)) AS jmbg_mb, ime_prezime_naziv,  ");
		sql.append("postotak_udjela, tekuca_godina,prethodna_godina,vlasnik, iznos_zaduzenosti, funkcija,id_povezane_osobe, naziv_veze,");
		sql.append("CONVERT(char(15), CONVERT(decimal(15, 0), oib)) AS oib, broj_zahtjeva, strucna_sprema, zvanje, TV.opis as _tip_veze FROM ");
		sql.append(getTableName()+" PO LEFT OUTER JOIN BI_PROD.dbo.bi_po_tip_veze as TV ON PO.tip_veze = TV.tip_veze where broj_zahtjeva = ? ");
		sql.append("and ((PO.slog in (100,200,300,400)and isnull(PO.postotak_udjela,0)>= 20) or (PO.slog in (500,600,700,800))) ");
		if(vlasnici)
		    sql.append(" AND isnull(vlasnik,'NE') = 'DA' ");
		sql.append(" ORDER BY vlasnik DESC, postotak_udjela, ime_prezime_naziv");
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
    public AS2RecordList daoFindPoVrstiSloga(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
	    sql.append("SELECT * FROM fn_po_zah_zahtjev_povezane_osobe(?,?) ");
	    if(value.exists("@print"))	
	        sql.append("ORDER by slog, ime_prezime_naziv ");
	    else
	        sql.append("ORDER by postotak_udjela desc, ime_prezime_naziv ");
	    try{
	    	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(ZAHDataDictionary.PO_ZAH_POVEZANA_OSOBA__BROJ_ZAHTJEVA));
			if(value.exists("@print"))
			    pstmt.setObject(2,null);
			else
			    pstmt.setObject(2,value.get(ZAHDataDictionary.PO_ZAH_POVEZANA_OSOBA__SLOG));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public PovezanaOsobaRs daoFindPostojecePovezaneOsobe(PovezanaOsobaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT DISTINCT tip_veze, CONVERT(char(15), CONVERT(decimal(15, 0), jmbg_mb)) AS jmbg_mb, ime_prezime_naziv,  "+
			"postotak_udjela,iznos_zaduzenosti FROM dbo.fn_po_zah_povezane_osobe(?,?) where postotak_udjela <= 0");
		try{	
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setObject(2,null);
			pstmt.setMaxRows(0);
			PovezanaOsobaRs j2eers = new PovezanaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
            return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PovezanaOsobaRs daoFindPostojecePovezaneOsobeVlasnici(PovezanaOsobaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT DISTINCT tip_veze, CONVERT(char(15), CONVERT(decimal(15, 0), jmbg_mb)) AS jmbg_mb, ime_prezime_naziv,  "+
		"postotak_udjela,iznos_zaduzenosti FROM dbo.fn_po_zah_povezane_osobe_vlasnici(?,?)");			
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setObject(2,null);
			pstmt.setMaxRows(0);
			PovezanaOsobaRs j2eers = new PovezanaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PovezanaOsobaRs daoFindPostojecePovezaneOsobeZahtjev(PovezanaOsobaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_po_zah_povezane_osobe_zahtjev(?,?,?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setObject(2,value.getBrojZahtjeva());
			pstmt.setString(3,"NE");
			pstmt.setMaxRows(0);
			PovezanaOsobaRs j2eers = new PovezanaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PovezanaOsobaRs daoFindPostojecePovezaneOsobeVlasniciZahtjev(PovezanaOsobaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_po_zah_povezane_osobe_zahtjev(?,?,?)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setObject(2,value.getBrojZahtjeva());
			pstmt.setString(3,"DA");
			pstmt.setMaxRows(0);
			PovezanaOsobaRs j2eers = new PovezanaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
	public void daoRemoveUcitanePovezaneOsobe(PovezanaOsobaVo value, boolean vlasnici) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM po_zah_povezana_osoba WHERE broj_zahtjeva = ? ");
		if(vlasnici)
		    sql.append(" AND isnull(vlasnik,'NE') = 'DA' ");
		else
		    sql.append(" AND isnull(vlasnik,'NE') = 'NE' ");
		 //ucitano (0-unos, 1-bsa, 2-OLTP, 3-zahtjev, 4-PDF)
		sql.append(" AND isnull(ucitano,0) = ?");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__BROJ_ZAHTJEVA));
	        pstmt.setObject(2, value.get(ZAHDataDictionary.PO_ZAH_POVEZANA_OSOBA__UCITANO));
	        pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	/**
	 * Čita povezane osobe za potrebe listi povezanosti npr. za drop-down izbornik.
	 * @300 - čita 100,200 za - 3. Obitelji od 1. i 2.
	 * @400 - Čita 100,200,300 za - 4. Udio 1.2.3. u drugim poduzećima.
	 * @500 - Čita 400 za - 5. Osobe iz drugih poduzeća (4.).
	 */   
	public PovezanaOsobaRs daoFindListaVezanihOsoba(PovezanaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		//z.r. 26-7-2010. Problem je bio u prikazu povezanih osoba na UI. Kod odabira jmbg je bio bez voedeće nule
		//a kod čitanja sa vodećom nulom.
		sql.append( "SELECT DISTINCT convert(char(15),convert(decimal(15,0),C.jmbg_mb)) as id, ");
		sql.append( "rtrim(convert(char(15),convert(decimal(15,0),C.jmbg_mb)))+'-'+rtrim(C.ime_prezime_naziv) as name ");
		sql.append(" FROM dbo.po_zah_povezana_osoba AS C WHERE C.broj_zahtjeva = ? ");	
		if(value.get("slog").equals("300"))
		    sql.append(" AND C.slog IN (100,200) ");
		else if(value.get("slog").equals("400"))
		    sql.append(" AND C.slog IN (100,200,300) ");
		else if(value.get("slog").equals("500"))
		    sql.append(" AND slog IN (400) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getBrojZahtjeva());
			pstmt.setMaxRows(0);
			PovezanaOsobaRs j2eers = new PovezanaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
   public PovezanaOsobaRs daoFindPostojecePovezaneOsobePO(PovezanaOsobaVo value)  {
	   	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_po_zah_povezane_osobe_PO(?)  ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setMaxRows(0);
			PovezanaOsobaRs j2eers = new PovezanaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
	   	} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }