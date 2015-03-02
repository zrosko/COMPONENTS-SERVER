package hr.adriacomsoftware.app.server.services.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.datasources.J2EEDefaultJDBCService;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.Types;


public class BankarskiJdbc extends J2EEDataAccessObjectJdbc implements BankaConstants {
    public static final String BANKA_KRATKA_SQL = "select distinct vbdi AS id, RTRIM(naziv)+'-'+convert(char(20),vbdi) AS name from bi_view_banka_kratka_lista WHERE (ISNULL(vbdi, 0) > 0) ";
    public static final String BANKA_SQL = "select distinct vbdi AS id, RTRIM(naziv)+'-'+convert(char(20),vbdi) AS name from bi_banka WHERE (ISNULL(vbdi, 0) > 0)";
    public static final String POSLOVNICA_SQL = "select organizacijska_jedinica AS id, rtrim(convert(char(20),organizacijska_jedinica))+'-'+rtrim(naziv) AS name from bi_organizacijska_jedinica ";
    public static final String VALUTA_SQL = "select sifra_valute AS id, rtrim(oznaka_valute)+'-'+convert(char(20),sifra_valute) AS name from bi_valuta";
    public static final String OPCINA_SQL = "select sifra_opcine AS id, rtrim(naziv)+'-'+convert(char(20),sifra_opcine) AS name from bi_opcina where sifra_zupanije in (13,15,17)";
    public static final String MJESTO_SQL = "select M.sifra_mjesta AS id, "+
    	"RTRIM(M.naziv)+'-'+RTRIM(O.naziv)+'-'+CONVERT(char(29),M.sifra_opcine)AS name FROM bi_mjesto AS M INNER JOIN bi_opcina AS O ON M.sifra_opcine = O.sifra_opcine AND O.sifra_zupanije in (13,15,17)";
    public static final String ZUPANIJA_SQL = "select sifra_zupanije AS id, convert(char(2),sifra_zupanije)+'-'+rtrim(naziv) AS name from bi_zupanija";
    public static final String DRZAVA_SQL = "select drzava AS id, rtrim(drzava)+'-'+rtrim(naziv) AS name from bi_drzava";
    public static final String RADNIK_SQL = "select id_radnika AS id, rtrim(ime)+' '+rtrim(prezime)+'-'+convert(char(20),id_radnika) AS name from bi_radnik";
    public static final String TRAJNI_NALOZI_SQL =
        "SELECT vrsta_trajnog_naloga AS id, RTRIM(CONVERT(char(20), vrsta_trajnog_naloga)) + '-' + RTRIM(opis) AS name FROM bi_vrsta_trajnog_naloga ";
    public static final String DJELATNOSTI_KRATKO_SQL = "SELECT rtrim(sifra_djelatnosti) AS id, rtrim(sifra_djelatnosti)+'-'+rtrim(naziv_djelatnosti) AS name from bi_sifra_djelatnosti "
        +"WHERE(nkd_grupa NOT IN (SELECT sifra_djelatnosti FROM bi_sifra_djelatnosti))";
    public static final String DJELATNOSTI_SQL = "SELECT rtrim(sifra_djelatnosti) AS id, rtrim(sifra_djelatnosti)+'-'+rtrim(naziv_djelatnosti) AS name from bi_sifra_djelatnosti "
        +"WHERE(nkd_grupa IN (SELECT sifra_djelatnosti FROM bi_sifra_djelatnosti))";
    public static final String DJELATNIK_ZAHTJEVI_SQL = "select convert(char(15),convert(decimal(15,0),jmbg)) AS id, rtrim(ime)+' '+rtrim(prezime) AS name from CMDB_PROD.dbo.cmdb_djelatnik "
            +"WHERE (org_jedinica_rada BETWEEN 15000 AND 16999) OR (org_jedinica_rada in (40970,40160,40190,40910,40920,40940,40980)) ORDER BY ime, prezime";
    public static final String DJELATNIK_BANKA_SQL = "select convert(char(15),convert(decimal(15,0),jmbg)) AS id, rtrim(ime)+' '+rtrim(prezime)AS name from CMDB_PROD.dbo.view_cmdb_djelatnik_pogled "
        +" ORDER BY ime";
    public static final String KORISNIK_BANKA_SQL = "select oznaka_radnika AS id, rtrim(ime)+' '+rtrim(prezime)AS name from CMDB_PROD.dbo.view_cmdb_djelatnik_pogled "
        +" ORDER BY ime";
    public static final String DJELATNIK_BANKA_IT_SQL = "select convert(char(15),convert(decimal(15,0),jmbg)) AS id, rtrim(ime)+' '+rtrim(prezime)AS name from CMDB_PROD.dbo.view_cmdb_djelatnik_pogled "
        +" where email like 'lmilutin%' or email like 'jbujas%' or email like 'dbabic%' or email like 'operateri%' ORDER BY ime";
    public static final String DJELATNIK_DOMENA_SQL = "select oznaka_radnika AS id, rtrim(ime)+' '+rtrim(prezime)AS name from CMDB_PROD.dbo.view_cmdb_djelatnik_pogled ";
    public static final String POVEZANE_OSOBE_SQL = "select tip_veze AS id, convert(char(4),tip_veze)+'-'+rtrim(opis) AS name from bi_po_tip_veze";
    public static final String RADNO_MJESTO_SQL = "SELECT RTRIM(oznaka_radnog_mjesta) AS id, RTRIM(oznaka_radnog_mjesta) + '-' + RTRIM(naziv) AS name FROM CMDB_PROD.dbo.cmdb_radno_mjesto";
    public static final String CRNE_LISTE_SQL = "SELECT id_liste AS id, RTRIM(naziv) AS name FROM BI_PROD.dbo.prn_crna_lista ";
    public static final String ALL_ROLES = "SELECT  * from cmdb_prod.dbo.view_cmdb_imovina_softver_uloge  " +
    		"order by jedinstvena_oznaka,uloga ";
    public static final String ALL_ROLES_COMPONENTS = "SELECT a.*, c.komponenta "+
    		"FROM cmdb_prod.dbo.view_cmdb_imovina_softver_uloge as a left outer join "+
    		"cmdb_prod.dbo.rbac_komponenta as c on a.jedinstvena_oznaka = c.aplikacija ";
    
    public BankarskiJdbc() {
        setTableName("");
    }
    public AS2RecordList daoFindListuOrderById(String sql) {
        String cacheKey = "GenericJdbc.daoFindListuOrderById";
        AS2RecordList cache_rs = J2EEDefaultJDBCService.getInstance().getFromCache(cacheKey);
        if (cache_rs != null)
            return cache_rs; //return from cache
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql+" order by id");
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        //add to cache
	        J2EEDefaultJDBCService.getInstance().addToCache(cacheKey, j2eers);
	        return j2eers; 
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public AS2RecordList daoFindListuOrderBy(String sql,String order_by)  {
        String cacheKey = "GenericJdbc.daoFindListu"+sql;
        AS2RecordList cache_rs = J2EEDefaultJDBCService.getInstance().getFromCache(cacheKey);
        if (cache_rs != null)
            return cache_rs; //return from cache
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql+" order by "+order_by);
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        //add to cache
	        J2EEDefaultJDBCService.getInstance().addToCache(cacheKey, j2eers);
        return j2eers;
    } catch (Exception e) {
		throw new AS2Exception(e);
	}
        
    }
    public AS2RecordList daoFindListu(String sql)  {
    	return daoFindListuOrderBy(sql,"name");
    }
    public OsnovniRs daoQuerySpDatum(String sp_name, OsnovniVo value) {
        StringBuffer sp = new StringBuffer();         
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public OsnovniRs daoQuerySpDatumOdDatumDo(OsnovniVo value, String sp_name) {
        StringBuffer sp = new StringBuffer();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public void daoCallSpDatum(OsnovniVo input_value,String sp_name) {
		StringBuffer sp = new StringBuffer();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,input_value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.execute();
			cs.close();
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
	}
    public void daoCallSpDatumOdDatumDo(OsnovniVo value,String sp_name) {
		StringBuffer sp = new StringBuffer();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.execute();
			cs.close();	
		} catch (Exception e) {
			throw new AS2Exception(e);
		}
	}
    public void daoCallSpDatumOdDatumDoBrojPartije(OsnovniVo value,String sp_name) 	{
		StringBuffer sp = new StringBuffer();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setObject(3, value.getBrojPartije());
			cs.execute();
			cs.close();
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
	}
    public void daoCallSpDatumOdDatumDoProfitniCentar(OsnovniVo value,String sp_name)  {
 		StringBuffer sp = new StringBuffer();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			if(value.getAsInt("profitni_centar")==0)
			    cs.setNull(3,Types.INTEGER);
			else
				cs.setObject(3, value.getProfitniCentar());
			cs.execute();
			cs.close();
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
 	}
    public boolean daoFindIfExists(String sql)  {
        try {
            PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
            pstmt.setMaxRows(0);
            /*AS2RecordList as2_rs =*/ transformResultSet(pstmt.executeQuery());
            pstmt.close();
            //if (as2_rs.size()>0)
                return true;
            //return false; 
        } catch (Exception e) {
            return false;
        }
    }
    public OsnovniRs daoQueryFnDatumOdDatumDo(OsnovniVo value, String fn_name)  {
        StringBuffer fn = new StringBuffer();
		fn.append("select * from dbo.");
		fn.append(fn_name);
		fn.append(" (?,?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(fn.toString());
			pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public OsnovniRs daoQueryFnDatum(OsnovniVo value, String fn_name)  {
        StringBuffer fn = new StringBuffer();
		fn.append("select * from dbo.");
		fn.append(fn_name);
		fn.append(" (?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(fn.toString());
			pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public J2EESqlBuilder odrediRazinuOvlastiSamoZaPodrucje(J2EESqlBuilder sql){ 
        //za kredite se posebno rade izvjesca po podrucju pripadnosti (maticni broj - sifra opcine)
        //dok se organizacijska jedinica ne uzima u obzir
       String _podrucje = AS2SessionFactory.getInstance().getCurrentUser().getPodrucje();
       //String _profitni_centar = J2EESessionService.getInstance().getCurrentUser().getProfitCenter();
       String razina_ovlasti = AS2SessionFactory.getInstance().getCurrentUser().getSecurityLevel();
       if(razina_ovlasti!=null){
	       if(razina_ovlasti.equals(RAZINA_OVLASTI_PODRUCJE)){	
	           if(_podrucje.equals(PODRUCJE_SIBENIK)||
	        	  _podrucje.equals(PODRUCJE_SPLIT)||
	              _podrucje.equals(PODRUCJE_ZAGREB)){	               
	              sql.appEqualNoQuote("AND", "profitni_centar",_podrucje);
	           }else{
	               //tamo gdje nema označenog profitnog centra tj. sve općine osim ZG županije, 
	               //Vodice (Murter, Tisno, Pirovac, Tribunj) je NULL 
	               sql.appEqualNoQuote("AND", "isnull(profitni_centar,"+_podrucje+")",_podrucje);  
	           }
	       }
       }
       return sql;
    }
    public J2EESqlBuilder odrediRazinuOvlastiUkljucujuciPodrucje(J2EESqlBuilder sql){ 
        //za kredite se posebno rade izvjesca po podrucju pripadnosti (maticni broj - sifra opcine)
       //String _profitni_centar = J2EESessionService.getInstance().getCurrentUser().getProfitCenter();
       String _podrucje = AS2SessionFactory.getInstance().getCurrentUser().getPodrucje();
       String razina_ovlasti = AS2SessionFactory.getInstance().getCurrentUser().getSecurityLevel();
       if(razina_ovlasti!=null){
	       if(razina_ovlasti.equals(RAZINA_OVLASTI_PODRUCJE)){	           
	           if(_podrucje.equals(PODRUCJE_SIBENIK)||
	        	  _podrucje.equals(PODRUCJE_SPLIT)||
	              _podrucje.equals(PODRUCJE_ZAGREB)){	               
	 	              sql.appEqualNoQuote("AND", "profitni_centar",_podrucje);
	 	           }else {
	 	               //tamo gdje nema označenog profitnog centra tj. sve općine osim ZG županije, 
	 	               //Vodice (Murter, Tisno, Pirovac, Tribunj) je NULL 
	 	               sql.appEqualNoQuote("AND", "isnull(profitni_centar,"+_podrucje+")",_podrucje);  
	 	           } 
	       }else{
	           return odrediRazinuOvlasti(sql);
	       }
       }
       return sql;
    }
    public J2EESqlBuilder odrediRazinuOvlastiUkljucujuciDjelatnika(J2EESqlBuilder sql) {
        String razina_ovlasti = AS2SessionFactory.getInstance().getCurrentUser().getSecurityLevel();
        String jmbg = AS2SessionFactory.getInstance().getCurrentUser().get("id_osobe");
        String korisnik = AS2SessionFactory.getInstance().getCurrentUser().get("korisnik");
        if (razina_ovlasti != null) {
            if (razina_ovlasti.equals(RAZINA_OVLASTI_DJELATNIK)) {
                if (korisnik.equals("aklaric") || korisnik.equals("rmandic") || korisnik.equals("tkrncevic")) {
                    String nizKorisnikaJmbg[] = { "112959383327", "112970383304", "1108963383303", "9999999999999" };
                    sql.appIn("AND", "(jmbg", nizKorisnikaJmbg);
                    sql.appIn("OR", "osoba_rasporeda", nizKorisnikaJmbg);
                    sql.appendln(") ");
                } else {
                    sql.appEqualNoQuote("AND", "(jmbg", jmbg);
                    sql.appEqualNoQuote("OR", "osoba_rasporeda", jmbg);
                    // gledanje tuđih zahtjeva ukoliko nam je to dopusteno u tabeli hd_dopustene_osobe
                    sql.appendln("or jmbg in( select osoba from hd_dopustene_osobe where dopustena_osoba = "+jmbg+") ");
                    // gledanje pojedinih tuđih zahtjeva ukoliko nam je to dopusteno u tabeli hd_dopusteni_zahtjevi
                    sql.appendln("or broj_ in( select id_zahtjeva from hd_dopusteni_zahtjevi where dopustena_osoba = "+jmbg+") ");
                    sql.appendln(") ");
                }
            } else {
            	String uloga = AS2SessionFactory.getInstance().getCurrentUser().get("role_id");
            	if(uloga.equals("9")){ //direktor direkcije
    	            String org_jedinica = AS2SessionFactory.getInstance().getCurrentUser().getUserBranch();            
 	               	sql.appendln("AND organizacijska_jedinica in (SELECT organizacijska_jedinica ");
 	               	sql.append("FROM [BI_PROD].[dbo].[bi_organizacijska_jedinica] " );
 	               	sql.append("WHERE nadredena_org_jedinica="+org_jedinica+") "); 
 	               	sql.append("OR  organizacijska_jedinica in (SELECT organizacijska_jedinica  ");
 	               	sql.append("FROM [BI_PROD].[dbo].[bi_organizacijska_jedinica]  ");
 	               	sql.append("WHERE nadredena_org_jedinica  in ( ");
 	               	sql.append("SELECT organizacijska_jedinica ");
 	               	sql.append("FROM [BI_PROD].[dbo].[bi_organizacijska_jedinica] ");
 	               	sql.append("WHERE nadredena_org_jedinica="+ org_jedinica+")) ");
 	               	sql.appEqualNoQuote("OR", "organizacijska_jedinica", org_jedinica); 
            	}else
            		return odrediRazinuOvlasti(sql);
            }
        }
        return sql;
    }
    public J2EESqlBuilder odrediRazinuOvlasti(J2EESqlBuilder sql){ 
	    /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
	    String razina_ovlasti = AS2SessionFactory.getInstance().getCurrentUser().getSecurityLevel();
	    if(razina_ovlasti==null)
	        razina_ovlasti = RAZINA_OVLASTI_SVE;
	    if(!razina_ovlasti.equals(RAZINA_OVLASTI_SVE)){
	        if(razina_ovlasti.equals(RAZINA_OVLASTI_PROFITNI_CENTAR)){//nadredene org jedinice (profitni centri)
	            String profitni_centar = AS2SessionFactory.getInstance().getCurrentUser().getProfitCenter();
                sql.appEqualNoQuote("AND", "BI_PROD.dbo.bi_fn_banka_profitni_centar(organizacijska_jedinica)", profitni_centar); 
	        }else if(razina_ovlasti.equals(RAZINA_OVLASTI_POSLOVNICA)){//prva razina poslovnice
	            String org_jedinica = AS2SessionFactory.getInstance().getCurrentUser().getUserBranch();            
	               sql.appEqualNoQuote("AND", "organizacijska_jedinica", org_jedinica); 
	        }
	    }
	    return sql;
	    /*ograničavanje za sigurnosne razine KRAJ*/ 
    }
    //pronadi fizičku ili pravnu osobu
    public OsobaRs daoPronadiOsobu(OsobaVo value)  {
        OsobaRs j2eers = null;
        J2EESqlBuilder sql = new J2EESqlBuilder();
			//broj partije
		try{
			if(value.exists("broj_partije")){
				sql.append("SELECT TOP 1 ime_prezime_klijenta as ime_prezime_naziv,convert(decimal(15,0),oib) as oib, ");
				sql.append("convert(decimal(15,0),jmbg) as jmbg_mb,convert(decimal(15,0),broj_partije) as broj_partije, telefon, drzava, datum_rodenja_, ime_oca_majke " +
						   "FROM bi_osoba_view ");
				sql.appEqualNoQuote("AND", "broj_partije", value.get("broj_partije"));
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setMaxRows(0);
				j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
				return j2eers;
			}
			//fizičke osobe 																						
			sql.append(" select TOP 50 convert(decimal(15,0),o.jmbg) as jmbg_mb, convert(decimal(15,0),o.oib) as oib,isnull(rtrim(ime),'')+' '+isnull(rtrim(prezime),'') as ime_prezime_naziv, " +
					   " ulica, mjesto, postanski_broj,drzava, telefon, datum_rodenja, ime_oca as ime_oca_majke,convert(decimal(15,0),broj_partije) as broj_partije /*tekuci*/ " +
					   " from dbo.bi_gr_osoba o left join dbo.bi_gr_partija p on o.oib=p.oib and racun_aktivni=1000 and datum_zatvaranja is null ");
			sql.appLike("AND", "rtrim(ime) + ' ' +rtrim(prezime) ", value.get("ime_prezime_naziv"));
	        sql.appEqualNoQuote("AND", "o.jmbg", value.get("jmbg_mb"));
	        sql.appEqualNoQuote("AND", "o.oib", value.get("oib"));
	        sql.append(" ORDER BY ime ");
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
			//pravne osobe
			if(value.get("jmbg_mb").length()>0 && j2eers.size()>0){
			    //jmbg je prevelik za maticni_broj (int) - događa se greška ako nastavi 
				pstmt.close();
				return j2eers;
			}
			if(value.get("jmbg_mb").length()>12)//ako je jmbg a nema ga u bazi
			    return j2eers;
			sql = new J2EESqlBuilder();
			sql.append("select TOP 50 convert(decimal(15,0),maticni_broj) as jmbg_mb, convert(decimal(15,0),oib) as oib, rtrim(naziv) as ime_prezime_naziv, "+
			        "ulica, mjesto, postanski_broj,drzava from dbo.bi_po_pravna_osoba ");
			sql.appLike("AND", "naziv", value.get("ime_prezime_naziv"));
	        sql.appEqualNoQuote("AND", "maticni_broj", value.get("jmbg_mb"));
	        sql.appEqualNoQuote("AND", "oib", value.get("oib"));
	        sql.append(" ORDER BY naziv ");
	        PreparedStatement pstmt1 = getConnection().getPreparedStatement(sql.toString());
			pstmt1.setMaxRows(0);
			j2eers.addRows(new OsobaRs(transformResultSet(pstmt1.executeQuery())));
			//kraj
			pstmt.close();
			pstmt1.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public OsobaVo daoPronadiOsobuJmbg(OsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
		//fizičke osobe
		sql.append("select CASE CONVERT(char(13), LEN(CONVERT(decimal(15, 0), jmbg))) "+
		        "WHEN '12' THEN '0' + CONVERT(char(12), (CONVERT(char(13), CONVERT(decimal(15, 0), jmbg)))) ELSE CONVERT(char(13),"+
		        "(CONVERT(decimal(15, 0), jmbg))) END AS jmbg_mb,"+
		        "ime, prezime, ulica, mjesto, postanski_broj, drzava, " +
		        "CONVERT(decimal(15, 0),oib) as oib, datum_rodenja " + //dodana linija 26.11.2009. Maja
				"from dbo.bi_gr_osoba where jmbg = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("jmbg_mb"));
			pstmt.setMaxRows(1);
	        OsobaVo j2eers = new OsobaVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public OsobaVo daoPronadiOsobuJmbgMb(OsobaVo value)  {        
        J2EESqlBuilder sql = new J2EESqlBuilder();
        OsobaVo j2eers = daoPronadiOsobuJmbg(value);
        if(j2eers.get("jmbg_mb").length()>0)
            return j2eers;
		//fizičke osobe
		sql.append("SELECT maticni_broj, maticni_broj as jmbg_mb, convert(decimal(15,0),oib) as oib, naziv as ime, naziv, ulica, postanski_broj, mjesto, drzava "+
				"from dbo.bi_po_pravna_osoba where maticni_broj = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("jmbg_mb"));
			pstmt.setMaxRows(1);
	        j2eers = new OsobaVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public OsobaVo daoPronadiOsobuOib(OsobaVo value)  {        
        J2EESqlBuilder sql = new J2EESqlBuilder();
		//fizičke osobe
		sql.append("select TOP 50 CASE CONVERT(char(13), LEN(CONVERT(decimal(15, 0), jmbg))) WHEN '12' " +
				   "THEN '0' + CONVERT(char(12), (CONVERT(char(13), CONVERT(decimal(15,0), jmbg)))) " +
				   "ELSE CONVERT(char(13), (CONVERT(decimal(15, 0), jmbg))) END AS jmbg_mb, " +
				   //"select TOP 50 convert(decimal(15,0),jmbg) as jmbg_mb, " +
				   "ime, prezime, ulica, mjesto, postanski_broj, drzava,convert(decimal(15,0),oib) as oib," +
				   "datum_rodenja " + 
				   "from dbo.bi_gr_osoba where oib = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("oib"));
			pstmt.setMaxRows(1);
			OsobaVo j2eers = new OsobaVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public OsobaVo daoPronadiPravnuFizickuOsobuOib(OsobaVo value)  {       
        J2EESqlBuilder sql = new J2EESqlBuilder();
        OsobaVo j2eers = daoPronadiOsobuOib(value);
        if(j2eers.get("oib").length()>0)
            return j2eers;
		//fizičke osobe
		sql.append("SELECT TOP 50 maticni_broj, maticni_broj as jmbg_mb, convert(decimal(15,0),oib) as oib, naziv as ime, naziv, ulica, postanski_broj, mjesto, drzava "+
				"from dbo.bi_po_pravna_osoba where oib = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("oib"));
			pstmt.setMaxRows(1);
	        j2eers = new OsobaVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		 } catch (Exception e) {
				throw new AS2Exception(e);
		 }
    }
    public OsobaVo daoPronadiPravnuFizickuOsobuBrojPartije(OsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select TOP 150 convert(varchar(10),convert(decimal(10,0),P.broj_partije)) as broj_partije, " +
				   "convert(varchar(15),convert(decimal(15,0),O.jmbg)) as maticni_broj, " +
				   "convert(varchar(15),convert(decimal(15,0),O.oib)) as oib, " +
				   "rtrim(O.ime)+' '+rtrim(O.prezime) as naziv, O.ulica, O.mjesto,O.postanski_broj " +
				   "from bi_gr_partija as P " +
				   "left outer join bi_gr_osoba as O on P.jmbg=O.jmbg where broj_partije=? " +
				   "union all " +
				   "select convert(varchar(10),convert(decimal(10,0),P.broj_partije)) as broj_partije, " +
				   "convert(varchar(15),convert(decimal(15,0),O.maticni_broj)) as maticni_broj, " +
				   "convert(varchar(15),convert(decimal(15,0),O.oib)) as oib, " +
				   "O.naziv,O.ulica, O.mjesto,O.postanski_broj " +
				   "from bi_po_partija as P left outer join bi_po_pravna_osoba as O " +
				   "on P.maticni_broj=O.maticni_broj where broj_partije=? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("broj_partije"));
			pstmt.setObject(2,value.get("broj_partije"));
			pstmt.setMaxRows(1);
			OsobaVo j2eers = new OsobaVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public OsnovniRs daoSimpleQuery(String sql)  {
		String cacheKey = "daoFindAllRoles";				
		AS2RecordList j2eers = J2EEDefaultJDBCService.getInstance().getFromCache(cacheKey); 
		if(j2eers!=null)
			return new OsnovniRs(j2eers); //return from cache
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OsnovniRs rs = new OsnovniRs( transformResultSet(pstmt.executeQuery()));
			pstmt.close();	
			//add to cache
			J2EEDefaultJDBCService.getInstance().addToCache(cacheKey, rs);
			return rs;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
	}
    public PartijaRs daoListaOtvorenihPartijaPravneFizicke(OsobaVo value)  {        
        J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT convert(decimal(15,0),P.broj_partije) as broj_partije_  " +
				   "FROM bi_po_partija P left join bi_po_pravna_osoba O on O.maticni_broj = P.maticni_broj " +
				   "WHERE P.datum_zatvaranja is null and O.oib = ? " );
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("oib"));
			pstmt.setMaxRows(0);
			PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
			//drugi dio
			sql = new J2EESqlBuilder();
			sql.append("SELECT convert(decimal(15,0),P.broj_partije) as broj_partije_ " +
					   "FROM bi_gr_partija P left join bi_gr_osoba O on O.jmbg = P.jmbg " +
					   "WHERE P.datum_zatvaranja is null and O.oib = ? " );
			PreparedStatement pstmt1 = getConnection().getPreparedStatement(sql.toString());
			pstmt1.setObject(1,value.get("oib"));
			pstmt1.setMaxRows(0);
			j2eers.addRows(new PartijaRs(transformResultSet(pstmt1.executeQuery())));
			//kraj
			pstmt.close();
			pstmt1.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
    public AS2RecordList daoListaPravnihFizickihOsoba(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select top 50 * FROM BI_PROD.dbo.view_bi_sve_osobe ");		
		sql.appLike("AND", "oib_", value.get("oib_"));
		sql.appLike("AND", "jmbg_mb_", value.get("jmbg_mb_"));
		sql.appLike("AND", "naziv", value.get("naziv"));
		sql.appLike("AND", "vrsta", value.get("vrsta"));	
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2Exception(e);
		}
    }
}