package hr.adriacomsoftware.app.server.gradani.da.as400.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectAs400Jdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;
import java.util.Iterator;


public final class PrometAs400Jdbc extends J2EEDataAccessObjectAs400Jdbc {
    public static final String MAX_BROJ_STAVKE_SQL = "SELECT max(fintranno) as max_broj_stavke FROM bsadb.fintran ";
    public final static int VRSTA_KREDITI_700000000_739999999 = 1;
    public final static int VRSTA_STEDNJA_DEVIZE_400000000_4499999999 = 2;
    public final static int VRSTA_STEDNJA_DEVIZE_490000000_499999999 = 3;
    public final static int VRSTA_ZIRO_310000000_319999999 = 4;
    public final static int VRSTA_TEKUCI_320000000_329999999 = 5;
    public final static int VRSTA_OSTALO_300000000 = 6;
    public final static int VRSTA_ZIRO_NEREZIDENTI_360000000_369999999 = 7;
    public final static int VRSTA_STEDNJA_KUNE_300000000_309999999 = 8;
    public final static int VRSTA_STEDNJA_KUNE_330000000_349999999 = 9;
    public final static int VRSTA_STEDNJA_KUNE_480000000_489999999 = 10;
    //mjesecni promet ima ove vrste
    public final static int VRSTA_A = 11;
    public final static int VRSTA_B = 12;
    public final static int VRSTA_C = 13;
        
    public PrometAs400Jdbc() {
        setTableName("bsadb.fintranTEST"); // produkcija  ??
    }
   
    public AS2RecordList daoETLPrometMjesecniSTARO(int max_broj_stavke, int vrsta)  {
        //citaj podatke iz BSA
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT case when (partno is not null and partno <> '' and partno <> 0  and partno > 99999999) ");
        sql.appendln("then substr(partno,1,9)||substr(");
        sql.appendln("911-mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(mod(( ");                           
        sql.appendln("mod(mod((mod(mod((mod( ");                           
        sql.appendln("10+substr(partno,1,1)-1,10)+1)*2,11) ");                           
        sql.appendln("+substr(partno,2,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,3,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,4,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,5,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,6,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,7,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,8,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,9,1)-1,10)+1)*2,11),3,1) ");
        if(vrsta == VRSTA_B){
            sql.appendln("when (partno is not null AND partno <> '' AND partno <> 0  AND partno < 99999999) then partno ");
            sql.appendln("when (partno is null OR partno <> '' OR partno = 0 ) then '0' ");
        }    
        sql.appendln(" end as broj_partije,");            
        sql.appendln("currnum1 as valuta_racuna,");
        sql.appendln("sortofit as vrsta_prometa,");
        sql.appendln("dacclass as broj_racunaa,");
        sql.appendln("cacclass as broj_racunab,");
        sql.appendln("currnum2 as protuvaluta_racuna,");
        sql.appendln("accdate as datum_prometa,");
        sql.appendln("duedate as datum_valutiranja,");
        sql.appendln("cashierno as broj_blagajne,");
        sql.appendln("docno as eksterni_broj_dokumenta,");
        sql.appendln("departnoe as organizacijska_jedinica_unosa,");
        sql.appendln("bankno as sifra_banke,");
        sql.appendln("fintranno as broj_stavke,");
        sql.appendln("cancelno as broj_stavke_storno,");
        sql.appendln("conectno as broj_prve_stavke_veza,");
        sql.appendln("findescr as opis_stavke,");
        sql.appendln("operentry as operater_zadnje_izmjene,");
        sql.appendln("timeentry as vrijeme_zadnje_izmjene, ");
        sql.append("status as status,");
        sql.append("extpartno as broj_partije_veza,");
        sql.append("chequeno as broj_ceka,");
        if (vrsta == VRSTA_A){
            sql.append("amount1/100 as iznos_valuta, ");
            sql.append("amount2/100 as iznos_protuvaluta ");
            sql.appendln("FROM bsadb.fintran "); 
            sql.appendln("WHERE (partno BETWEEN 700000000 AND 739999999 ");
            sql.appendln("OR partno BETWEEN 300000000 AND 309999999 ");
            sql.appendln("OR partno BETWEEN 330000000 AND 349999999 ");
            sql.appendln("OR partno BETWEEN 480000000 AND 489999999 ");
            sql.appendln("OR partno BETWEEN 320000000 AND 329999999 ");
            sql.appendln("OR partno BETWEEN 310000000 AND 319999999 ");
            sql.appendln("OR partno BETWEEN 360000000 AND 369999999) ");
         }else if (vrsta == VRSTA_B){
            sql.append("amount1/10**(c1.fractpart) as iznos_valuta, ");
            sql.append("amount2/10**(c2.fractpart) as iznos_protuvaluta "); 
            sql.appendln("FROM bsadb.fintran, bsadb.currency as c1, bsadb.currency as c2 ");
            sql.appendln("WHERE currnum1=c1.currnum and currnum2=c2.currnum ");
            sql.appendln("AND (partno < 300000000)  ");
         }else if (vrsta == VRSTA_C){
	            sql.append("amount1/10**(c1.fractpart) as iznos_valuta, ");
	            sql.append("amount2/10**(c2.fractpart) as iznos_protuvaluta "); 
	            sql.appendln("FROM bsadb.fintran, bsadb.currency as c1, bsadb.currency as c2 ");
	            sql.appendln("WHERE currnum1=c1.currnum and currnum2=c2.currnum ");
	            sql.appendln("AND (partno BETWEEN 400000000 AND 479999999 ");
	            sql.appendln("OR   partno BETWEEN 490000000 AND 499999999) ");
	         }             
        sql.appendln("AND fintranno >'"+max_broj_stavke+"' ");
        sql.appendln("ORDER BY fintranno ");
        try {
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(2000);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        //obrada koja daje duguje_potrazuje
		    AS2Record vo;
		    int broj_racuna = 0;
			Iterator<AS2Record> E = j2eers.iteratorRows();
			while(E.hasNext()){
				vo = (AS2Record)E.next(); 
				broj_racuna = vo.getAsInt("broj_racunab",0);
				if(broj_racuna>0 && broj_racuna<7989){
				    vo.set("duguje_potrazuje","-1");
				    vo.set("broj_racuna2",vo.get("broj_racunaa"));
				    vo.set("broj_racuna",vo.get("broj_racunab"));
				}else{
				    vo.set("duguje_potrazuje","1");
				    vo.set("broj_racuna",vo.get("broj_racunaa"));
				    vo.set("broj_racuna2",vo.get("broj_racunab"));    			    
				}
				vo.delete("broj_racunab");
			    vo.delete("broj_racunaa");
			}
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoETLPromet(OsnovniVo value, int vrsta)  {
        //pronadi zadnji broj stavke u BI bazi
        //citaj podatke iz BSA
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT case when (partno is not null and partno <> '' and partno <> 0  and partno > 99999999) ");
        sql.appendln("then substr(partno,1,9)||substr(");
        sql.appendln("911-mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(mod(( ");                           
        sql.appendln("mod(mod((mod(mod((mod( ");                           
        sql.appendln("10+substr(partno,1,1)-1,10)+1)*2,11) ");                           
        sql.appendln("+substr(partno,2,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,3,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,4,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,5,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,6,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,7,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,8,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,9,1)-1,10)+1)*2,11),3,1) ");
        if(vrsta == VRSTA_OSTALO_300000000){
            sql.appendln("when (partno is not null AND partno <> '' AND partno <> 0  AND partno < 99999999) then partno ");
            sql.appendln("when (partno is null OR partno <> '' OR partno = 0 ) then '0' ");
        }
        sql.appendln(" end as broj_partije,");
        
        sql.appendln("currnum1 as valuta_racuna,");
        sql.appendln("sortofit as vrsta_prometa,");
        sql.appendln("dacclass as broj_racunaa,");
        sql.appendln("cacclass as broj_racunab,");
        sql.appendln("currnum2 as protuvaluta_racuna,");
        sql.appendln("accdate as datum_prometa,");
        sql.appendln("duedate as datum_valutiranja,");
        sql.appendln("cashierno as broj_blagajne,");
        sql.appendln("docno as eksterni_broj_dokumenta,");
        sql.appendln("departnoe as organizacijska_jedinica_unosa,");
        sql.appendln("bankno as sifra_banke,");
        sql.appendln("fintranno as broj_stavke,");
        sql.appendln("cancelno as broj_stavke_storno,");
        sql.appendln("conectno as broj_prve_stavke_veza,");
        sql.appendln("findescr as opis_stavke,");
        sql.appendln("operentry as operater_zadnje_izmjene,");
        sql.appendln("timeentry as vrijeme_zadnje_izmjene, ");
        
        if(vrsta == VRSTA_OSTALO_300000000){
            sql.append("status as status,");
            sql.append("extpartno as broj_partije_veza,");
        }else if(vrsta == VRSTA_TEKUCI_320000000_329999999){
            sql.append("chequeno as broj_ceka,");
            sql.append("extpartno as broj_partije_veza,");
        }
        
        if (vrsta == VRSTA_STEDNJA_DEVIZE_490000000_499999999 ||
	              vrsta == VRSTA_STEDNJA_DEVIZE_400000000_4499999999 ||
	              vrsta == VRSTA_OSTALO_300000000){
            sql.append("amount1/10**(c1.fractpart) as iznos_valuta, ");
            sql.append("amount2/10**(c2.fractpart) as iznos_protuvaluta "); 
            sql.appendln("FROM bsadb.fintran, bsadb.currency as c1, bsadb.currency as c2 ");
        }else{
            sql.append("amount1/100 as iznos_valuta, ");
            sql.append("amount2/100 as iznos_protuvaluta ");
            sql.appendln("FROM bsadb.fintran ");
        }
                    
        if(vrsta == VRSTA_KREDITI_700000000_739999999){
            sql.appendln("WHERE partno BETWEEN 700000000 AND 739999999 ");
        }else if (vrsta == VRSTA_STEDNJA_DEVIZE_490000000_499999999){
            sql.appendln("WHERE (partno BETWEEN 490000000 AND 499999999) and currnum1=c1.currnum and currnum2=c2.currnum ");
        }else if (vrsta == VRSTA_STEDNJA_DEVIZE_400000000_4499999999){
            sql.appendln("WHERE (partno BETWEEN 400000000 AND 479999999) and currnum1=c1.currnum and currnum2=c2.currnum ");
        }else if (vrsta == VRSTA_ZIRO_310000000_319999999){
            sql.appendln("WHERE (partno BETWEEN 310000000 AND 319999999) ");
        }else if (vrsta == VRSTA_TEKUCI_320000000_329999999){
            sql.appendln("WHERE partno BETWEEN 320000000 AND 329999999 and accdate >'31.12.2007' ");
        }else if (vrsta == VRSTA_OSTALO_300000000){
            sql.appendln("WHERE partno < 300000000 and currnum1=c1.currnum and currnum2=c2.currnum ");
        }else if (vrsta == VRSTA_ZIRO_NEREZIDENTI_360000000_369999999){
            sql.appendln("WHERE partno BETWEEN 360000000 AND 369999999 ");
        }else if (vrsta == VRSTA_STEDNJA_KUNE_300000000_309999999){
            sql.appendln("WHERE (partno BETWEEN 300000000 AND 309999999) ");
        }else if (vrsta == VRSTA_STEDNJA_KUNE_330000000_349999999){
            sql.appendln("WHERE (partno BETWEEN 330000000 AND 349999999) ");
        }else if (vrsta == VRSTA_STEDNJA_KUNE_480000000_489999999){
            sql.appendln("WHERE (partno BETWEEN 480000000 AND 489999999)");
        }
        sql.appendln("AND fintranno >'"+value.get("max_broj_stavke")+"' ");
        sql.appendln("ORDER BY fintranno ");
        try {
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(5000);
	        AS2RecordList j2eers = transformResultSet( pstmt.executeQuery());
	        pstmt.close();
	        //obrada koja daje duguje_potrazuje
		    AS2Record vo;
		    int broj_racuna = 0;
			Iterator<AS2Record> E = j2eers.getRows().iterator();
			while(E.hasNext()){
				vo = E.next(); 
				broj_racuna = vo.getAsInt("broj_racunab",0);
				if(broj_racuna>0 && broj_racuna<7989){
				    vo.set("duguje_potrazuje","-1");
				    vo.set("broj_racuna2",vo.get("broj_racunaa"));
				    vo.set("broj_racuna",vo.get("broj_racunab"));
				}else{
				    vo.set("duguje_potrazuje","1");
				    vo.set("broj_racuna",vo.get("broj_racunaa"));
				    vo.set("broj_racuna2",vo.get("broj_racunab"));    			    
				}
				vo.delete("broj_racunab");
			    vo.delete("broj_racunaa");
			}
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    /**
     * Čitaj sve transakcije u FINTRAN za pranje novca (preko 105.000,00).
     */
    public AS2RecordList daoTransakcijePreko105000(String max_broj_stavke)  {
        //pronadi zadnji broj stavke u BI bazi
        //citaj podatke iz BSA
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT case when partno<>0 then ");
        sql.appendln("substr(partno,1,9)||substr( 911-mod((mod(mod((mod(mod((mod(mod((mod");
        sql.appendln("(mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(10+substr(partno,1,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,2,1)-1,10)+1)*2,11)+substr(partno,3,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,4,1)-1,10)+1)*2,11)+substr(partno,5,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,6,1)-1,10)+1)*2,11)+substr(partno,7,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,8,1)-1,10)+1)*2,11)+substr(partno,9,1)-1,10)+1)*2,11),3,1) else '         0' end as broj_partije,"); 
        //kraj moduel 11 broj partije
        sql.appendln("currnum1 as valuta_transakcije, ");
        sql.appendln("sortofit as vrsta_prometa,");
        sql.appendln("amount1/100 as iznos_valuta,");
        sql.appendln("currnum2 as protuvaluta_transakcije, ");
        sql.appendln("amount2/100 as iznos_protuvaluta,");
        sql.appendln("accdate as datum_prometa,");
        sql.appendln("duedate as datum_valutiranja,");
        sql.appendln("docno as eksterni_broj_dokumenta,");
        //sql.appendln("DEBCRE as duguje_potrazuje,");
        sql.appendln("departnoe as organizacijska_jedinica_unosa,");
        sql.appendln("bankno as sifra_banke,");
        sql.appendln("fintranno as broj_stavke,");            
        sql.appendln("operentry as operater_zadnje_izmjene,");
        sql.appendln("timeentry as vrijeme_zadnje_izmjene ");
        sql.appendln("FROM bsadb.fintran ");
        sql.appendln("WHERE fintranno >'"+max_broj_stavke+"'"); 
        sql.appendln(" AND amount1/100 >= 105000  AND partno > 0 "); 
        sql.appendln("ORDER BY fintranno ");
        try {
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(1000);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers;     
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoETLPrometMjesecni(int max_broj_stavke)  {
        //citaj podatke iz BSA
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("SELECT char(case ");
        sql.appendln("when (partno is not null AND partno <> '' AND partno <> 0  AND partno < 99999999) then char(partno) ");
        sql.appendln("when (partno is not null and partno <> '' and partno <> 0  and partno > 99999999) then ");
        sql.appendln("substr(partno,1,9)||substr(");
        sql.appendln("911-mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(mod(( ");                           
        sql.appendln("mod(mod((mod(mod((mod( ");                           
        sql.appendln("10+substr(partno,1,1)-1,10)+1)*2,11) ");                           
        sql.appendln("+substr(partno,2,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,3,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,4,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,5,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,6,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,7,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,8,1)-1,10)+1)*2,11)"); 
        sql.appendln("+substr(partno,9,1)-1,10)+1)*2,11),3,1) ");
        sql.appendln("when (partno is null OR partno <> '' OR partno = 0 ) then '0' ");
        sql.appendln(" end) as broj_partije,");            
        sql.appendln("currnum1 as valuta_racuna,");
        sql.appendln("sortofit as vrsta_prometa,");
        sql.appendln("dacclass as broj_racunaa,");
        sql.appendln("cacclass as broj_racunab,");
        sql.appendln("currnum2 as protuvaluta_racuna,");
        sql.appendln("accdate as datum_prometa,");
        sql.appendln("duedate as datum_valutiranja,");
        sql.appendln("cashierno as broj_blagajne,");
        sql.appendln("docno as eksterni_broj_dokumenta,");
        sql.appendln("departnoe as organizacijska_jedinica_unosa,");
        sql.appendln("bankno as sifra_banke,");
        sql.appendln("fintranno as broj_stavke,");
        sql.appendln("cancelno as broj_stavke_storno,");
        sql.appendln("conectno as broj_prve_stavke_veza,");
        sql.appendln("findescr as opis_stavke,");
        sql.appendln("operentry as operater_zadnje_izmjene,");
        sql.appendln("timeentry as vrijeme_zadnje_izmjene, ");
        sql.append("status as status,");
        sql.append("extpartno as broj_partije_veza,");
        sql.append("chequeno as broj_ceka,");            	       
        sql.append("amount1/10**(c1.fractpart) as iznos_valuta, ");
        sql.append("amount2/10**(c2.fractpart) as iznos_protuvaluta "); 
        sql.appendln("FROM bsadb.fintran, bsadb.currency as c1, bsadb.currency as c2 ");
        sql.appendln("WHERE currnum1=c1.currnum and currnum2=c2.currnum ");
        sql.appendln("AND fintranno >'"+max_broj_stavke+"' ");
        sql.appendln("ORDER BY fintranno ");
        try {
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(2000);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        //obrada koja daje duguje_potrazuje
		    AS2Record vo;
		    int broj_racuna = 0;
			Iterator<AS2Record> E = j2eers.getRows().iterator();
			while(E.hasNext()){
				vo = E.next(); 
				broj_racuna = vo.getAsInt("broj_racunab",0);
				if(broj_racuna>0 && broj_racuna<7989){
				    vo.set("duguje_potrazuje","-1");
				    vo.set("broj_racuna2",vo.get("broj_racunaa"));
				    vo.set("broj_racuna",vo.get("broj_racunab"));
				}else{
				    vo.set("duguje_potrazuje","1");
				    vo.set("broj_racuna",vo.get("broj_racunaa"));
				    vo.set("broj_racuna2",vo.get("broj_racunab"));    			    
				}
				vo.delete("broj_racunab");
			    vo.delete("broj_racunaa");
			}
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoFindPrometTekuci(AS2Record value)  {
        J2EESqlBuilder sql = sqlBrojPartije(); 
        sql.appendln("currnum1 as valuta_racuna,");
        sql.appendln("sortofit as vrsta_prometa,");
        sql.appendln("amount1/100 as iznos_valuta,");
        sql.appendln("currnum2 as protuvaluta_racuna, ");
        sql.appendln("amount2/100 as iznos_protuvaluta,");
        sql.appendln("accdate as datum_prometa,");
        sql.appendln("duedate as datum_valutiranja,");
        sql.appendln("docno as eksterni_broj_dokumenta,");
        sql.appendln("departnoe as organizacijska_jedinica_unosa,");
        sql.appendln("bankno as sifra_banke,");
        sql.appendln("fintranno as broj_stavke,");            
        sql.appendln("operentry as operater_zadnje_izmjene,");
        sql.appendln("timeentry as vrijeme_zadnje_izmjene ");
        sql.appendln("FROM bsadb.fintran ");
        sql.appendln("WHERE partno ='"+value.get("broj_partije")+"' "); 
        sql.appendln("AND accdate ='"+value.getDDMMYYYY("datum_prometa")+"'");  
        sql.appendln("ORDER BY fintranno ");
        try {
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(1000);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}