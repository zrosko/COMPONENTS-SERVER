package hr.adriacomsoftware.app.server.gk.da.as400.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectAs400Jdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class GlavnaKnjigaAs400Jdbc extends J2EEDataAccessObjectAs400Jdbc {
    public GlavnaKnjigaAs400Jdbc() {
        setTableName("bsadb.subledgerTEST"); // produkcija  
    }
    /**
     * @redni_broj_stavke u BI bazi
     * @@redovi glavne knjige iz produkcije veći od zadnjeg broja stavke u BI bazi
     */
    public OsnovniRs daoETLGlavnaKnjiga(OsnovniVo value) {
        //pronadi zadnji broj stavke u BI bazi
        //citaj podatke iz BSA
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT subitemno as redni_broj_stavke,");
        sql.appendln("clientno as maticni_broj,");
        sql.appendln("departno as organizacijska_jedinica,");
        sql.appendln("CURRNUM1 as valuta_racuna,");
        //početak module 11 broj partije 7.6. problem nije bilo case naredbe
//            sql.appendln("substr(partno,1,9)||substr( 911-mod((mod(mod((mod(mod((mod(mod((mod");
//            sql.appendln("(mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(10+substr(partno,1,1)-1,10)+1)*2,11)");                           
//            sql.appendln("+substr(partno,2,1)-1,10)+1)*2,11)+substr(partno,3,1)-1,10)+1)*2,11)");                           
//            sql.appendln("+substr(partno,4,1)-1,10)+1)*2,11)+substr(partno,5,1)-1,10)+1)*2,11)");                           
//            sql.appendln("+substr(partno,6,1)-1,10)+1)*2,11)+substr(partno,7,1)-1,10)+1)*2,11)");                           
//            sql.appendln("+substr(partno,8,1)-1,10)+1)*2,11)+substr(partno,9,1)-1,10)+1)*2,11),3,1) as broj_partije,"); 
        //kraj staro
        sql.appendln("case when partno<>0 then ");
        sql.appendln("substr(partno,1,9)||substr( 911-mod((mod(mod((mod(mod((mod(mod((mod");
        sql.appendln("(mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(10+substr(partno,1,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,2,1)-1,10)+1)*2,11)+substr(partno,3,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,4,1)-1,10)+1)*2,11)+substr(partno,5,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,6,1)-1,10)+1)*2,11)+substr(partno,7,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(partno,8,1)-1,10)+1)*2,11)+substr(partno,9,1)-1,10)+1)*2,11),3,1) else '         0' end as broj_partije,"); 
        //kraj moduel 11 broj partije
        sql.appendln("AMOUNT1/100 as iznos_valuta,");
        sql.appendln("CURRNUM2 as protuvaluta_racuna,");
        sql.appendln("AMOUNT2/100 as iznos_protuvaluta,");
        sql.appendln("DATEOFACC as datum_knjizenja,");
        sql.appendln("MATURITY as datum_valutiranja,");
        sql.appendln("INTDOCNO as interni_broj_dokumenta,");
        sql.appendln("DATEOFDOC as datum_dokumenta,");
        sql.appendln("SORTOFIT as vrsta_prometa,");
        sql.appendln("DEBCRE as duguje_potrazuje,");
        sql.appendln("DOCUMNO as broj_dokumenta,");
        sql.appendln("CACCOUNT as broj_konta,");
        sql.appendln("DEPARTNOE as organizacijska_jedinica_unosa,");
        //sql.appendln("timeentry as vrijeme_unosa,");  //greska kod insereta         
        sql.appendln("OPERENTRY as operater_unosa ");
        sql.appendln("FROM bsadb.subledger ");
        sql.appendln("WHERE abs(subitemno) >'"+value.get("max_broj_stavke")+"'"); 
        sql.appendln("ORDER BY subitemno ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(2000);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }   
}