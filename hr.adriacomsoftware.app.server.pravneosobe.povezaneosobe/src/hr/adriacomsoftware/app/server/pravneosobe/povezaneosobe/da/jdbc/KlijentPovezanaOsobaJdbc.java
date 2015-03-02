package hr.adriacomsoftware.app.server.pravneosobe.povezaneosobe.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.povezaneosobe.dto.KlijentPovezanaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.povezaneosobe.dto.KlijentPovezanaOsobaVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class KlijentPovezanaOsobaJdbc extends J2EEDataAccessObjectJdbc {
    public KlijentPovezanaOsobaJdbc() {
        setTableName("klijent_po_povezana_osoba");
    }
    public AS2RecordList daoFind(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
	    sql.append("SELECT PO.tip_veze,");
		sql.append("klijent_naziv,klijent_maticni_broj,datum_od,datum_do,");
		sql.append("CONVERT(char(15), CONVERT(decimal(15, 0), klijent_oib)) AS klijent_oib, ");
		sql.append("CONVERT(char(15), CONVERT(decimal(15, 0), jmbg_mb)) AS jmbg_mb, ime_prezime_naziv,  ");
		sql.append("postotak_udjela, tekuca_godina,prethodna_godina,vlasnik, iznos_zaduzenosti, funkcija,id_povezane_osobe, naziv_veze,");
		sql.append("CONVERT(char(15), CONVERT(decimal(15, 0), oib)) AS oib, broj_zahtjeva, strucna_sprema, zvanje, TV.opis as _tip_veze FROM ");
		sql.append(getTableName()+" PO LEFT OUTER JOIN bi_po_tip_veze as TV ON PO.tip_veze = TV.tip_veze where klijent_maticni_broj = ? ");
		sql.append(" ORDER BY vlasnik DESC, postotak_udjela, ime_prezime_naziv");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("klijent_maticni_broj"));
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
	    sql.append("SELECT * FROM fn_klijent_po_povezane_osobe(?,?) ");
	    if(value.exists("@print"))	
	        sql.append("ORDER by slog, ime_prezime_naziv ");
	    else
	        sql.append("ORDER by postotak_udjela desc, ime_prezime_naziv ");
	    try{
		    PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("klijent_maticni_broj"));
			if(value.exists("@print"))
			    pstmt.setObject(2,null);
			else
			    pstmt.setObject(2,value.get("slog"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 
	public void daoRemoveUcitanePovezaneOsobe(AS2Record value) 	{
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("delete FROM klijent_po_povezana_osoba WHERE klijent_maticni_broj = ? ");
		 //ucitano (0-unos, 1-BSA,2-PDF, 3-SSPN)
		sql.append(" AND isnull(ucitano,0) = ?");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get("klijent_maticni_broj"));
	        pstmt.setObject(2, value.get("ucitano"));
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
	public KlijentPovezanaOsobaRs daoFindListaVezanihOsoba(KlijentPovezanaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		//zr. problem kod prikaza povezanih osoba "drop down" koji imaju jmbg sa 12 znamenaka
		sql.append( "SELECT DISTINCT convert(char(15),convert(decimal(15,0),C.jmbg_mb)) as id, ");
		sql.append( "rtrim(convert(char(15),convert(decimal(15,0),C.jmbg_mb)))+'-'+rtrim(C.ime_prezime_naziv) as name ");
		sql.append(" FROM dbo.klijent_po_povezana_osoba AS C WHERE C.klijent_maticni_broj = ? ");	
		if(value.get("slog").equals("300"))
		    sql.append(" AND C.slog IN (100,200) ");
		else if(value.get("slog").equals("400"))
		    sql.append(" AND C.slog IN (100,200,300) ");
		else if(value.get("slog").equals("500"))
		    sql.append(" AND slog IN (400) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("maticni_broj"));
			pstmt.setMaxRows(0);
			KlijentPovezanaOsobaRs j2eers = new KlijentPovezanaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
   public void daoDodaPovezaneOsobeIzPRN(KlijentPovezanaOsobaVo value) 	{
	   	J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_klijent_vezane_osobe_prn");
		sp.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(1, value.getKlijentMaticniBroj());
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
   public KlijentPovezanaOsobaRs daoFindUsporedbaSaBSA(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
	    sql.append("SELECT * FROM fn_tbl_po_povezana_osoba_usporedba(?) ");
	    try{
		    PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("klijent_maticni_broj"));
			pstmt.setMaxRows(0);
			KlijentPovezanaOsobaRs j2eers = new KlijentPovezanaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
 }