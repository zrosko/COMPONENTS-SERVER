package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class NaplataKlijentJdbc extends OLTPJdbc {
    public NaplataKlijentJdbc() {
        setTableName("naplata_klijent");
    }
    public NaplataKlijentRs daoFind(NaplataKlijentVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if(value.exists("@@find"))
			sql.append("select *,'"+value.get("@@find")+"' as filter_");
		else 
			sql.append("select * ");
		sql.append(" FROM view_naplata_klijent ");
		if(value.exists("@@find")){
			String _find = value.get("@@find");				
	        if (_find.equals("otpisani_po")){
	        	sql.appEqual("AND", "otpisan ", "DA");
	        	sql.appEqual("AND", "vrsta", "PRAVNA"); 
	        }else if (_find.equals("otpisani_obrt")){
	        	sql.appEqual("AND", "otpisan", "DA");
	        	sql.appIn("AND", "vrsta", "'OBRT','UDRUGA'");
	        }else if (_find.equals("otpisani_gr")){
	        	sql.appEqual("AND", "otpisan", "DA");
	        	sql.appEqual("AND", "vrsta", "GRADANIN"); 
	        }else if (_find.equals("neotpisani_po")){
	        	sql.appEqual("AND", "otpisan", "NE");
	        	sql.appEqual("AND", "vrsta", "PRAVNA"); 
	        }else if (_find.equals("neotpisani_obrt")){
	        	sql.appEqual("AND", "otpisan", "NE");
	        	sql.appIn("AND", "vrsta", "'OBRT','UDRUGA'");
	        }else if (_find.equals("neotpisani_gr")){
	        	sql.appEqual("AND", "otpisan", "NE");
	        	sql.appEqual("AND", "vrsta", "GRADANIN"); 
	        }else if (_find.equals("svi_aktivni")){
	        	sql.appEqual("AND", "aktivnost", "DA"); 
	        }
         
		}else{
            sql.appEqualNoQuote("AND", "oib", value.getOib()); 
            sql.appEqualNoQuote("OR", "jmbg", value.getOib());
            sql.appEqualNoQuote("OR", "maticni_broj", value.getOib());
	        sql.appLike("AND", "naziv", value.getNaziv()); 
	        sql.appEqual("AND", "rizicna_skupina", value.getRizicnaSkupina()); 
	        sql.appEqual("AND", "status_klijenta", value.getStatusKlijenta());
	        sql.appGreatherOrEqual("AND", "broj_dana_kasnjenja", value.getBrojDanaKasnjenja());
	        sql.appIn("AND", "aktivnost", value.getAktivnost()); 
	        sql.appIn("AND", "otpisan", value.getOtpisan()); 
	        sql.appIn("AND", "vrsta", value.getVrsta()); 
		}
		sql.append(" order by datum_pocetak desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public NaplataKlijentRs daoFindFromTransakcija(NaplataKlijentVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select naziv, ulica, CONVERT(decimal(13, 0), oib) AS oib,maticni_broj," );
		sql.append(" CONVERT(decimal(13, 0), jmbg) AS jmbg,");
		sql.append(" CONVERT(varchar(10), datum_pocetak, 104)AS datum_pocetak_, postanski_broj, mjesto, broj_dana_kasnjenja,dospjeli_dug, izlozenost_bruto,");
		sql.append(" null as prazno1, null as prazno2,datum_pocetak, vrsta, status_klijenta, aktivnost, otpisan, rizicna_skupina, napomena, 'TXN' as izvor");
		sql.append(" from dbo.fn_tbl_naplata_filer_novih_klijenata(?,?,?,?,?,?,?)");
		sql.append(" order by broj_dana_kasnjenja desc, naziv");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate("datum_pocetak"));
			pstmt.setObject(2,value.getBrojDanaKasnjenja());
			pstmt.setObject(3,value.getRizicnaSkupina());
			pstmt.setObject(4,value.getProperty("vrsta_transakcije"));
			pstmt.setDouble(5,value.getAsDoubleObject("izlozenost_bruto").doubleValue());
			pstmt.setDouble(6,value.getAsDoubleObject("dospjeli_dug").doubleValue());
			pstmt.setObject(7,value.getProperty("pravne_gradani"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public boolean daoFindIfExists(NaplataKlijentVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT 1 FROM naplata_klijent ");
		sql.append(" where oib = ? or jmbg = ? or maticni_broj = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("oib"));
			pstmt.setObject(2,value.getProperty("jmbg"));
			pstmt.setObject(3,value.getProperty("maticni_broj"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
	        int broj_slogova = j2eers.size();
	        if(broj_slogova > 0)
	            return true;
	        else
	            return false;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public NaplataKlijentRs daoLoadSSP(NaplataKlijentVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * " +
				   " from fn_gr_naplata_pogled(0) " +
				   " where partija_zatvorena = 'NE' and datum_izlaska is null " +
				   " order by partija_zatvorena desc, status_partije desc, " +
				   " datum_ulaska desc, datum_izlaska desc, naziv ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
}
    public NaplataKlijentRs daoFindSSP(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from fn_gr_naplata_pogled("+value.getAsLong("broj_partije")+") ");
        if(value.get("_refresh").equals("true")){
        	sql.appEqualNoQuote("AND", "broj_partije", value.get("broj_partije")); 
        	sql.appEqual("AND", "datum_ssp", value.get("datum_ssp")); 
        }
        else{
			if(value.get("oib").length()>0){
				sql.appEqual("AND", "(oib", value.get("oib")); 
				sql.appEqual("OR", "jmbg", value.get("oib")); 
				sql.append(")");
			}
			sql.appLike("AND", "naziv", value.get("naziv")); 
			sql.appInNoQuote("AND", value.get("broj_partije"),"broj_partije,broj_partije_vezane,broj_partije_izuzeti_priljevi");
			sql.appGreatherOrEqual("AND", "datum_ssp", value.get("datum_ssp_od"));
			sql.appLessOrEqual("AND", "datum_ssp", value.get("datum_ssp_do"));
			sql.appEqual("AND", "datum_ssp", value.get("datum_ssp"));
			sql.appGreatherOrEqual("AND", "datum_izlaska", value.get("datum_izlaska_od"));
			sql.appLessOrEqual("AND", "datum_izlaska", value.get("datum_izlaska_do"));
			if(!value.get("broj_ovrhe_prefiks").equals(" "))
				sql.appEqual("AND", "broj_ovrhe_prefiks", value.get("broj_ovrhe_prefiks")); 
			sql.appEqual("AND", "broj_ovrhe_broj", value.get("broj_ovrhe_broj")); 
			sql.appLike("AND", "broj_ovrhe_godina", value.get("broj_ovrhe_godina")); 
			
			sql.append(" order by partija_zatvorena desc, status_partije desc, " +
					   " datum_ulaska desc, datum_izlaska desc ");
        }
        try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	  public NaplataRs daoIzvjestaji(NaplataVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_gr_naplata_ssp_facade");
		sp.append(" (?,?,?,?,?,?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(3,value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(4,value.get("@@report_selected"));
			cs.setObject(5,value.get("status_predmeta"));
			cs.setObject(6,value.get("javni_biljeznik"));
			cs.setObject(7,value.get("sud"));
			cs.setObject(8,value.get("referent"));
			cs.setObject(9,value.get("broj_partije"));
			cs.setDate(10,value.getAsSqlDate("datum_ssp"));
			cs.setObject(11,value.get("koristi_alt_adresu"));
			cs.setDate(12,value.getAsSqlDate("datum_izdavanja"));
			NaplataRs j2eers = new NaplataRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 
	  public NaplataRs daoIzvjestajReferat(NaplataVo value)  {
			J2EESqlBuilder sql = new J2EESqlBuilder();
			sql.append(" select * from oltp_prod.dbo.fn_tbs_gr_naplata_referat("+value.get("broj_partije")+",'"+value.get("datum_ssp")+"') " +
					   " order by rb,vrsta,datum_dogadaja,id_biljeske ");
			try{
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setMaxRows(0);
				AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
				pstmt.close();
				return new NaplataRs(j2eers);
			} catch (Exception e) {
				throw new AS2DataAccessException(e);
			}
		}
    public NaplataKlijentRs daoFindGradaniRocnost(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from BI_PROD.dbo.view_gr_krediti_rocnost " );
		sql.append(" where maticni_broj in (select jmbg FROM view_naplata_klijent)");
		sql.appendWhere();
        sql.appPrefix();
        sql.append("AND godina = DATEPART(year,'");
        sql.append(value.get("datum"));
        sql.append("')");            
        sql.append("AND mjesec = DATEPART(month,'");
        sql.append(value.get("datum"));
        sql.append("')");            
        sql.append("and dan = DATEPART(day,'");
        sql.append(value.get("datum"));
        sql.append("')");
		sql.append(" ORDER BY preko_365, od_181_do_365, od_151_do_180, od_121_do_150, od_91_do_120, od_61_do_90, od_31_do_60, do_30_dana");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 
    public NaplataKlijentRs daoFindPravneOsobeRocnost(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from BI_PROD.dbo.bi_view_izvjestaj_po_rocnost " );
		sql.append(" where maticni_broj in (select maticni_broj FROM view_naplata_klijent)");
		sql.appendWhere();
        sql.appPrefix();
        sql.append("AND godina = DATEPART(year,'");
        sql.append(value.get("datum"));
        sql.append("')");            
        sql.append("AND mjesec = DATEPART(month,'");
        sql.append(value.get("datum"));
        sql.append("')");            
        sql.append("and dan = DATEPART(day,'");
        sql.append(value.get("datum"));
        sql.append("')");
		sql.append(" ORDER BY preko_365, od_181_do_365, od_151_do_180, od_121_do_150, od_91_do_120, od_61_do_90, od_31_do_60, do_30_dana");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 
    public NaplataKlijentRs daoFindPravneOsobeZaduzenost(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from RIZIK_PROD.dbo.view_kr_transakcija_naplata ");
		sql.append(" where oib in (select oib FROM view_naplata_klijent " +
				"where vrsta not in ('gradanin'))");
        sql.appendWhere();
        sql.appPrefix();
		sql.appEqual("AND", "datum", value.getDateAsStringOrCurrenDate("datum")); 
		sql.append(" ORDER BY oib, broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 
    public NaplataKlijentRs daoFindGradaniStanjePoRacunima(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln(" select * " +
				" from bi_prod.dbo.fn_tbs_gr_osoba_partije_racuni_stanja(?) " +
				" order by prezime,ime,broj_partije,broj_konta ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1,value.getAsSqlDate("datum"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public NaplataKlijentRs daoFindPravneOsobeStanjePoRacunima(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln(" select * " +
				" from bi_prod.dbo.fn_tbs_po_pravna_osoba_partije_racuni_stanja(?) " +
				" order by prezime,ime,broj_partije,broj_konta ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1,value.getAsSqlDate("datum"));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public NaplataKlijentRs daoFindGradaniZaduzenost(NaplataKlijentVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select * from RIZIK_PROD.dbo.view_kr_transakcija_naplata ");
		sql.append(" where oib in (select oib FROM view_naplata_klijent " +
				"where vrsta = 'gradanin')");
        sql.appendWhere();
        sql.appPrefix();
		sql.appEqual("AND", "datum", value.getDateAsStringOrCurrenDate("datum")); 
		sql.append(" ORDER BY oib, broj_partije");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 
 }