package hr.adriacomsoftware.app.server.rizik.izlozenost.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.rizik.izlozenost.dto.KrTransakcijaRs;
import hr.adriacomsoftware.app.common.rizik.izlozenost.dto.KrTransakcijaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.RIZIKJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class KrTransakcijaJdbc extends RIZIKJdbc {
    
    public KrTransakcijaJdbc() {
        setTableName("kr_transakcija");
    }
    public KrTransakcijaRs daoListaTransakcija(KrTransakcijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT top (100) * from rizik_prod.dbo.view_kr_transakcija_pogled ");
        sql.appEqual("and", "datum", value.getDateAsStringOrCurrenDate("datum").substring(0,10));
        sql.appendln("order by oib, broj_partije");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        KrTransakcijaRs j2eers = new KrTransakcijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public KrTransakcijaRs daoPronadiIzlozenosti(KrTransakcijaVo value, boolean pretrazivanje)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select * from rizik_prod.dbo.view_kr_transakcija_pogled  ");
        sql.appEqual("AND", "datum", value.getDateAsStringOrCurrenDate("datum")); 
        if(pretrazivanje){
            sql.appIn("AND", "vrsta_transakcije", value.get("@@VrstaTransakcije")); 
            sql.appEqual("AND", "oib", value.get("oib")); 
            sql.appEqual("AND", "jmbg", value.get("jmbg")); 
            sql.appEqual("AND", "maticni_broj", value.get("maticni_broj")); 
        }
        sql.appendln(" ORDER BY podvrsta_duga");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        KrTransakcijaRs j2eers = new KrTransakcijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
   
    public KrTransakcijaRs daoZaduzenostKlijentaNaDan(KrTransakcijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select * from view_kr_transakcija_aktiva ");
        if(value.getOib().length()>0){
            sql.appendln("where (oib=?) ");
        }else if(value.getJmbg().length()>0){
            sql.appendln("where (jmbg=?) ");
        }else if(value.getMaticniBroj().length()>0){
            sql.appendln("where (maticni_broj=?) ");
        }
        sql.appendWhere();
        sql.appPrefix();
        sql.appEqual("AND", "datum", value.getDateAsStringOrCurrenDate("datum")); 
        sql.appendln(" ORDER BY podvrsta_duga, vrsta_duga");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        if(value.getOib().length()>0){
	            pstmt.setObject(1, value.getOib());
	        }else if(value.getJmbg().length()>0){
	            pstmt.setObject(1, value.getJmbg());
	        }else if(value.getMaticniBroj().length()>0){
	            pstmt.setObject(1, value.getMaticniBroj());
	        }            
	        pstmt.setMaxRows(0);
	        KrTransakcijaRs j2eers = new KrTransakcijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    /*
     SELECT 'Ukupno klijent' as tip, sum(odobreni_iznos) as odobreni_iznos,
        sum(nedospjela_glavnica) as nedospjela_glavnica,
        sum(nedospjela_kamata) as nedospjela_kamata,
        sum(dospjela_glavnica) as dospjela_glavnica,
        sum(dospjela_kamata) as dospjela_kamata,
        sum(izlozenost_bruto) as izlozenost_bruto,
        sum(iznos_rezervacije) as iznos_rezervacije
        FROM  rizik_prod.dbo.view_kr_transakcija
        where (maticni_broj=3861619 or jmbg=3861619 or oib=3861619)
        and datum='20110503'
        
        
        union all
        
        SELECT  'Ukupno povezane osobe' as tip, sum(odobreni_iznos) as odobreni_iznos,
        sum(nedospjela_glavnica) as nedospjela_glavnica,
        sum(nedospjela_kamata) as nedospjela_kamata,
        sum(dospjela_glavnica) as dospjela_glavnica,
        sum(dospjela_kamata) as dospjela_kamata,
        sum(izlozenost_bruto) as izlozenost_bruto,
        sum(iznos_rezervacije) as iznos_rezervacije
        from rizik_prod.dbo.view_kr_transakcija
        where (maticni_broj in ( select case when jmbg_mb_veza is not null then jmbg_mb_veza
                        else isnull(jmbg_mb,0) end
                        from rizik_prod.dbo.kr_entitet_veza
                        where klijent_jmbg_mb=3861619 or klijent_oib=3861619 )
        or jmbg in ( select case when jmbg_mb_veza is not null then jmbg_mb_veza
                        else isnull(jmbg_mb,0) end
                        from rizik_prod.dbo.kr_entitet_veza
                        where klijent_jmbg_mb=3861619 or klijent_oib=3861619 ))
        and datum='20110503'
        order by tip
     */
    public KrTransakcijaRs daoZaduzenostUkupnoNaDan(KrTransakcijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT 'Ukupno klijent' as tip, sum(odobreni_iznos) as odobreni_iznos, ");
        sql.appendln("sum(nedospjela_glavnica) as nedospjela_glavnica, ");
        sql.appendln("sum(nedospjela_kamata) as nedospjela_kamata, ");
        sql.appendln("sum(dospjela_glavnica) as dospjela_glavnica, ");
        sql.appendln("sum(dospjela_kamata) as dospjela_kamata, ");
        sql.appendln("sum(izlozenost_bruto) as izlozenost_bruto, ");
        sql.appendln("sum(iznos_rezervacije) as iznos_rezervacije ");
        sql.appendln("FROM rizik_prod.dbo.view_kr_transakcija ");
        sql.appendln("where (maticni_broj=3861619 or jmbg=3861619 or oib=3861619)");
        sql.appendWhere();
        sql.appPrefix();
        sql.appEqual("and", "datum", value.getDateAsStringOrCurrenDate("datum")); 
        sql.appendln(" union all ");
        sql.appendln("SELECT  'Ukupno povezane osobe' as tip, sum(odobreni_iznos) as odobreni_iznos, ");
        sql.appendln("sum(nedospjela_glavnica) as nedospjela_glavnica, ");
        sql.appendln("sum(nedospjela_kamata) as nedospjela_kamata, ");
        sql.appendln("sum(dospjela_glavnica) as dospjela_glavnica, ");
        sql.appendln("sum(dospjela_kamata) as dospjela_kamata, ");
        sql.appendln("sum(izlozenost_bruto) as izlozenost_bruto, ");
        sql.appendln("sum(iznos_rezervacije) as iznos_rezervacije ");
        sql.appendln("FROM rizik_prod.dbo.view_kr_transakcija ");
        sql.appendln("where (maticni_broj in ( select case when jmbg_mb_veza is not null then jmbg_mb_veza ");
        sql.appendln("else isnull(jmbg_mb,0) end ");
        sql.appendln("from rizik_prod.dbo.kr_entitet_veza ");
        sql.appendln("where klijent_jmbg_mb=3861619 or klijent_oib=3861619 ) ");
        sql.appendln("or jmbg in ( select case when jmbg_mb_veza is not null then jmbg_mb_veza ");
        sql.appendln("else isnull(jmbg_mb,0) end");
        sql.appendln("from rizik_prod.dbo.kr_entitet_veza ");
        sql.appendln("where klijent_jmbg_mb=3861619 or klijent_oib=3861619 ))");
        sql.appEqual("and", "datum", value.getDateAsStringOrCurrenDate("datum")); 
        sql.appendln("");
        sql.appendln(" ORDER BY tip");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        KrTransakcijaRs j2eers = new KrTransakcijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    /*
     SELECT   * from rizik_prod.dbo.view_kr_transakcija
        where (maticni_broj in ( select case when jmbg_mb_veza is not null then jmbg_mb_veza
                else isnull(jmbg_mb,0) end
                from rizik_prod.dbo.kr_entitet_veza
                where klijent_jmbg_mb=3861619 or klijent_oib=3861619 )
        or jmbg in ( select case when jmbg_mb_veza is not null then jmbg_mb_veza
                else isnull(jmbg_mb,0) end
                from rizik_prod.dbo.kr_entitet_veza
                where klijent_jmbg_mb=3861619 or klijent_oib=3861619 ))
        and datum='20110503'
        order by podvrsta_duga,vrsta_duga
     */
    public KrTransakcijaRs daoZaduzenostPovezaneOsobeNaDan(KrTransakcijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT   * from rizik_prod.dbo.view_kr_transakcija ");
        sql.appendln("where (maticni_broj in ( select case when jmbg_mb_veza is not null then jmbg_mb_veza ");
        sql.appendln("else isnull(jmbg_mb,0) end ");
        sql.appendln("from rizik_prod.dbo.kr_entitet_veza ");
        sql.appendln("where klijent_jmbg_mb=3861619 or klijent_oib=3861619 ) ");
        sql.appendln("or jmbg in ( select case when jmbg_mb_veza is not null then jmbg_mb_veza ");
        sql.appendln("else isnull(jmbg_mb,0) end ");
        sql.appendln("from rizik_prod.dbo.kr_entitet_veza ");
        sql.appendln("where klijent_jmbg_mb=3861619 or klijent_oib=3861619 ))");
        sql.appendWhere();
        sql.appPrefix();
        sql.appEqual("and", "datum", value.getDateAsStringOrCurrenDate("datum")); 
        sql.appendln(" order by podvrsta_duga,vrsta_duga");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        KrTransakcijaRs j2eers = new KrTransakcijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public KrTransakcijaRs daoPragMaterijalnostiPO(KrTransakcijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * from rizik_prod.dbo.view_kr_transakcija ");
        sql.appEqual("and", "datum", value.getDateAsStringOrCurrenDate("datum").substring(0,10));
        sql.appendln("and rtrim(vrsta_transakcije) like 'PO%' ");
        sql.appendln("and prag_materijalnosti >= 90 ");
        sql.appendln("order by podvrsta_duga, maticni_broj");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        KrTransakcijaRs j2eers = new KrTransakcijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoJamstveniKapital(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("select *,CMDB_PROD.dbo.fn_scl_isms_imovina_povjerljivost(1415) as isms_povjerljivost ");
        sql.append("from RIZIK_prod.dbo.fn_tbl_jamstveni_kapital(?)");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    /**
     * Klijenti B i C
     */
    public KrTransakcijaRs daoZaduzenostKlijentaNaplate(KrTransakcijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select * from view_kr_transakcija_naplata ");
        sql.appEqual("AND", "datum", value.getDateAsStringOrCurrenDate("datum")); 
        sql.appendln("AND rizicna_skupina not in ('A') ");
        sql.appendln(" ORDER BY rizicna_skupina, broj_partije");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());            
	        pstmt.setMaxRows(0);
	        KrTransakcijaRs j2eers = new KrTransakcijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}