package hr.adriacomsoftware.app.server.pravneosobe.da.as400.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectAs400Jdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class IzvjestajAs400Jdbc extends J2EEDataAccessObjectAs400Jdbc {
    public IzvjestajAs400Jdbc() {
        setTableName("??");
    }
    /* ZBOG dublih redova dodano DISTINCT - 7.3.2013.*/
    public OsnovniRs daoListaPrometNaDan26017(OsnovniVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        J2EESqlBuilder sql2 = new J2EESqlBuilder();
        J2EESqlBuilder sql3 = new J2EESqlBuilder();
        sql.appendln("SELECT DISTINCT BSADB.CLIENT.CLIENTNO as maticni_broj,BSADB.SUBLEDGER.DEPARTNO as organizacijska_jedinica,CNAME as naziv,");
        sql.appendln("BSADB.SUBLEDGER.CACCOUNT as broj_konta,");
        sql.appendln("case when BSADB.SUBLEDGER.partno<>0 then ");
        sql.appendln("substr(BSADB.SUBLEDGER.partno,1,9)||substr( 911-mod((mod(mod((mod(mod((mod(mod((mod");
        sql.appendln("(mod((mod(mod((mod(mod((mod(mod((mod(mod((mod(10+substr(BSADB.SUBLEDGER.partno,1,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(BSADB.SUBLEDGER.partno,2,1)-1,10)+1)*2,11)+substr(BSADB.SUBLEDGER.partno,3,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(BSADB.SUBLEDGER.partno,4,1)-1,10)+1)*2,11)+substr(BSADB.SUBLEDGER.partno,5,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(BSADB.SUBLEDGER.partno,6,1)-1,10)+1)*2,11)+substr(BSADB.SUBLEDGER.partno,7,1)-1,10)+1)*2,11)");                           
        sql.appendln("+substr(BSADB.SUBLEDGER.partno,8,1)-1,10)+1)*2,11)+substr(BSADB.SUBLEDGER.partno,9,1)-1,10)+1)*2,11),3,1) else '         0' end as broj_partije,");                                    
        sql.appendln("CASE  WHEN  debcre = -1 THEN AMOUNT1/100 ELSE 0 END AS potrazuje, ");
        sql.appendln("CASE  WHEN  debcre = 1 THEN AMOUNT1/100 ELSE 0 END  AS duguje, ");
        sql.appendln("DATEOFACC as datum_knjizenja, BSADB.SORTOFITEM.DESCRITEM as opis_prometa, ");
        sql.appendln("INTDOCNO as interni_broj_dokumenta, ");
        sql.appendln("DOCUMNO as broj_dokumenta,OPERENTRY as operater_unosa, BSADB.CLIENTACC.RISK as ocjena_ukupna, ");  //BSADB.CLIENT.RISK as ocjena_ukupna
        sql.appendln("BSADB.ITEMDESCR.PARTNO as platitelj, ");
        sql.appendln("CASE WHEN BSADB.ITEMDESCR.REASON1 ='' then BSADB.ITEMDESCR.ITEMDESC else BSADB.ITEMDESCR.REASON1||BSADB.ITEMDESCR.REASON2||BSADB.ITEMDESCR.REASON3 end as temelj_uplate, ");
        sql2.appendln(sql.toString());//SQL2
        sql2.appendln(" 0 as sifra_protuvalute, BSADB.SORTOFITEM.SORTOFIT as vrsta_prometa, BSADB.SUBLEDGER.subitemno  as redni_broj_stavke");//30.09.2013.
        sql.appendln("BSADB.CCONTRACT.currnum2 as sifra_protuvalute,BSADB.SORTOFITEM.SORTOFIT as vrsta_prometa, BSADB.SUBLEDGER.subitemno as redni_broj_stavke");//1.2.2013.//30.09.2013.
        sql.appendln("FROM BSADB.SUBLEDGER " +
   			 		 "left join BSADB.CLIENT on BSADB.SUBLEDGER.CLIENTNO=BSADB.CLIENT.CLIENTNO " +
   			 		 "left join BSADB.CCONTRACT on BSADB.SUBLEDGER.PARTNO=BSADB.CCONTRACT.PARTNO " +
   			 		 "left join BSADB.SORTOFITEM on BSADB.SUBLEDGER.SORTOFIT=BSADB.SORTOFITEM.SORTOFIT " +
   			 		 "left join BSADB.ITEMDESCR ON BSADB.SUBLEDGER.SUBITEMNO = BSADB.ITEMDESCR.SUBITEMNO " +
   			 		 "left join BSADB.CLIENTACC on BSADB.SUBLEDGER.PARTNO = BSADB.CLIENTACC.PARTNO ");
        sql2.appendln("FROM BSADB.SUBLEDGER " +
        			  "left join BSADB.CLIENT on BSADB.SUBLEDGER.CLIENTNO=BSADB.CLIENT.CLIENTNO " +
        			  "left join BSADB.SORTOFITEM on BSADB.SUBLEDGER.SORTOFIT=BSADB.SORTOFITEM.SORTOFIT " +
        			  "left join BSADB.ITEMDESCR ON BSADB.SUBLEDGER.SUBITEMNO = BSADB.ITEMDESCR.SUBITEMNO " +
        			  "left join BSADB.CLIENTACC on BSADB.SUBLEDGER.PARTNO = BSADB.CLIENTACC.PARTNO   ");
        if(value.get(OsnovniVo.IZBOR_NAZIV).equals(OsnovniVo.IZBOR_A)){
            sql3.appendln("WHERE DATEOFACC BETWEEN '"+value.getDDMMYYYY("datum_od")+"'"); 
            sql3.appendln("AND '"+value.getDDMMYYYY("datum_do")+"'"); 
            sql3.appendln("AND DOCUMNO LIKE 'NKS-%' ");
        }else if(value.get(OsnovniVo.IZBOR_NAZIV).equals(OsnovniVo.IZBOR_B)){
            sql3.appendln("WHERE DATEOFACC BETWEEN '"+value.getDDMMYYYY("datum_od")+"'"); 
            sql3.appendln("AND '"+value.getDDMMYYYY("datum_do")+"'"); 
        }else if(value.get(OsnovniVo.IZBOR_NAZIV).equals(OsnovniVo.IZBOR_C)){//NOVO BEGIN MB
            sql3.appendln("WHERE DATEOFACC BETWEEN '"+value.getDDMMYYYY("datum_od")+"'"); 
            sql3.appendln(" AND '"+value.getDDMMYYYY("datum_do")+"'"); 
            if(value.get("maticni_broj").length()>0)
            	sql3.appendln(" AND BSADB.CLIENT.CLIENTNO = '"+value.getMaticniBroj()+"'"); //NOVO END
        }else {
          sql3.appendln("WHERE DATEOFACC='"+value.getDDMMYYYY("datum")+"'");              
        }
        if(value.get("@@report_selected").equals("promet_na_dan_26017_b_c"))
        	sql3.appendln(" AND (BSADB.CLIENTACC.RISK LIKE 'B%' OR BSADB.CLIENTACC.RISK LIKE 'C%') ");
         sql.appendln(sql3.toString());
        sql2.appendln(sql3.toString());//SQL2
        sql.appendln(" AND '"+value.getDDMMYYYY("datum")+"'");
        sql.appendln(" BETWEEN BSADB.CCONTRACT.DATEFROM AND BSADB.CCONTRACT.DATETO "); 
        sql3 = new J2EESqlBuilder(); 
        sql3.appendln(" AND CACCOUNT IN ('26017','26117','2608') ");
        sql.appendln(sql3.toString());
        sql2.appendln(sql3.toString());//SQL2
        sql2.appendln(" AND BSADB.SUBLEDGER.PARTNO=999999999 ");//SQL2
        sql3 = new J2EESqlBuilder(); 
        sql3.appendln(sql.toString());
        sql3.appendln(" UNION ALL ");
        sql3.appendln(sql2.toString());
        sql3.appendln(" order by 1,4,7 ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql3.toString());
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        j2eers.addValue("isms_povjerljivost", "1398 - Povjerljivo");
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}