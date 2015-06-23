package hr.adriacomsoftware.app.server.pravneosobe.krediti.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.RocnostRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.RocnostVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.types.AS2String;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class RocnostKreditaPravihOsobaJdbc extends BankarskiJdbc {
    public static boolean program_radi = false;
    
    public RocnostKreditaPravihOsobaJdbc() {
        setTableName("bi_izvjestaj_po_rocnost");
    }
    public RocnostRs daoListaRocnostiKonto(RocnostVo value)  {
        J2EESqlBuilder sql = daoListaRocnosti(value, false);
        sql.appGreatherOrEqual("AND","broj_partije", value.getBrojPartije());
        sql.appLessOrEqual("AND", "broj_partije", value.getBrojPartijeDo());
        sql.appIn("AND", "maticni_broj", value.getMaticniBroj());
        sql.appLike("AND", "naziv", value.getNaziv()); 
        sql.appGreather("AND", "ukupno_red", "0"); 
        if(!value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA_KAMATE))
            sql.appEqual("AND", "glavnica_kamate", value.getGlavnicaKamate());
        sql.appIn("AND", "vrsta_proizvoda", value.get("vrsta_proizvoda"));
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        RocnostRs j2eers = new RocnostRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public RocnostRs daoListaRocnostiKontoOnline(RocnostVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT * FROM dbo.bi_fn_po_kredit_rocnost_glavna_arhiva(?,?,?,?,?) ");
        sql.appIn("AND", "vrsta_proizvoda", value.get("vrsta_proizvoda"));
        sql.append("ORDER BY maticni_broj, broj_partije, broj_konta");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_ROCNOST__DATUM));
	        pstmt.setString(2,value.get(JBDataDictionary.BI_ROCNOST__GLAVNICA_KAMATE));
	        pstmt.setString(3,value.getValutaProtuvaluta());
	        pstmt.setObject(4,value.getBrojPartije());
	        pstmt.setObject(5,value.getBrojPartijeDo());
	        pstmt.setMaxRows(0);
	        RocnostRs j2eers = new RocnostRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public RocnostRs daoListaRocnostiKontoMaticniBrojOnline(RocnostVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT * FROM dbo.bi_fn_po_kredit_rocnost_maticni_broj_arhiva(?,?,?,?,?) ");
        sql.appIn("AND", "vrsta_proizvoda", value.get("vrsta_proizvoda"));
        sql.append("ORDER BY maticni_broj, broj_partije, broj_konta");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        int counter = 1;
	        pstmt.setDate(counter++, value.getAsSqlDate(JBDataDictionary.BI_ROCNOST__DATUM));
	        pstmt.setString(counter++,value.get(JBDataDictionary.BI_ROCNOST__GLAVNICA_KAMATE));
	        pstmt.setString(counter++,"V");
	        pstmt.setObject(counter++,value.getMaticniBroj());
	        pstmt.setString(counter++,"PO");
	        pstmt.setMaxRows(0);
	        RocnostRs as_rs = new RocnostRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return as_rs;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public RocnostRs daoListaRocnostiKomitent(RocnostVo value)  {
        J2EESqlBuilder sql=new J2EESqlBuilder();
        if(value.get(RocnostVo.BI_VRSTA_ROCNOSTI_GRAFIKA).equals(RocnostVo.BI_VRSTA_ROCNOSTI_GRAFIKA_NAZIV)){
            sql.appendln("select * from bi_view_izvjestaj_po_rocnost_grafika ");
            sql.appEqual("AND", "maticni_broj", value.getMaticniBroj());
            sql.append(" order by maticni_broj,godina,mjesec,dan");
        }else{
            sql = daoListaRocnosti(value, true);
            sql.appendln("group by maticni_broj, naziv, ocjena_ukupna, organizacijska_jedinica, godina, mjesec, dan, profitni_centar, nkd_grupa," +
               "ovrha,opomena,blokada,isms_povjerljivost,zadani_datum,glavnica_kamate,broj_partije ");
            if(value.get(RocnostVo.BI_VRSTA_ROCNOSTI_SORT).equals(RocnostVo.BI_VRSTA_ROCNOSTI_SORT_NAZIV)||
               value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA_KAMATE_ODVOJENO))
                sql.append(" ORDER BY naziv");
            else if(value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_PARTIJA)){
                sql.append(" order by maticni_broj, broj_partije");
            }else
                sql.append(" ORDER BY preko_365, od_181_do_365, od_151_do_180, od_121_do_150, od_91_do_120, od_61_do_90, od_31_do_60, do_30_dana");
        }   
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        RocnostRs j2eers = new RocnostRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    private java.sql.Timestamp daoCallExecuteStoredProcedure(String sp_name, RocnostVo input_value)  {
    	J2EESqlBuilder sp = new J2EESqlBuilder();
	       sp.append("{call ");
	       sp.append(sp_name);
	       sp.append(" (?,?,?) }");
	       try{
	    	   CallableStatement cs = getConnection().getCallableStatement(sp.toString());
		       cs.setDate(1,input_value.getAsSqlDate(JBDataDictionary.BI_ROCNOST__DATUM));
		       cs.setString(2,input_value.getAsString(JBDataDictionary.BI_ROCNOST__GLAVNICA_KAMATE));
		       cs.setString(3,input_value.getAkcija());
		       cs.execute();
		       cs.close();
		       return null;
	      } catch (Exception e) {
				throw new AS2DataAccessException(e);
	      }
    }
    private J2EESqlBuilder daoListaRocnosti(RocnostVo value, boolean ukupno)  {
        try { 
            if(program_radi && value.getAkcija().equals(RocnostVo.IZRACUNAJ))
                throw new AS2Exception("10010");
            if(value.getAkcija().equals(RocnostVo.IZRACUNAJ)){
                program_radi = true;
                //Napravi prepis iz BSA koristeci Pentaho ETL
                daoStartSQLJob("ETL_PROD_JOB_GK_Glavna_Knjiga_Arhiva_ONLINE");
                //31.10.2011.J2EEPentahoService.getInstance().startSingleJOB("PROD - JOB - GK Glavna Knjiga Arhiva ONLINE");
            }
            boolean kamate_glavnica = false;
            String original_glavnica_kamate = value.getGlavnicaKamate();
            if(value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_PARTIJA)||
               value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA_KAMATE)||
               value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA_KAMATE_ODVOJENO)){
                kamate_glavnica= true;
            }
            //ako postoji izvjestaj za zadani datum, ne radi se novi, vec se samo prikazuje
            if(kamate_glavnica){
                value.setGlavnicaKamate(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA);
                /*identifikator = */daoCallExecuteStoredProcedure("bi_po_krediti_rocnost_arhiva", value);
                value.setGlavnicaKamate(RocnostVo.BI_VRSTA_ROCNOSTI_KAMATE);//K
                /*identifikator = */daoCallExecuteStoredProcedure("bi_po_krediti_rocnost_arhiva", value);
                //sacuvaj inicijalnu vrijednost
                value.setGlavnicaKamate(original_glavnica_kamate);//Original
            }else{ 
                //samo glavnica ili samo kamate
                /*identifikator = */daoCallExecuteStoredProcedure("bi_po_krediti_rocnost_arhiva", value);
            }
            J2EESqlBuilder sql = new J2EESqlBuilder();
            if(!ukupno){
                sql.appendln("select * from bi_izvjestaj_po_rocnost");
            }else {
                sql.appendln("select maticni_broj, naziv, SUM(preko_365) AS preko_365, SUM(od_181_do_365) AS od_181_do_365, SUM(od_151_do_180) AS od_151_do_180, " +
	               "SUM(od_121_do_150) AS od_121_do_150, SUM(od_91_do_120) AS od_91_do_120, SUM(od_61_do_90) AS od_61_do_90, " +
	               "SUM(od_31_do_60) AS od_31_do_60, SUM(do_30_dana) AS do_30_dana, SUM(ukupno_red) AS ukupno_red, organizacijska_jedinica," +
	               "ovrha,opomena,blokada, godina, mjesec, dan, profitni_centar, ocjena_ukupna, isms_povjerljivost, " +
	               "nkd_grupa,MAX(broj_dana_kasnjenja) AS broj_dana_kasnjenja,zadani_datum,glavnica_kamate, broj_partije ");
                if(value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_KAMATE)){
                	sql.appendln(" from bi_view_izvjestaj_po_rocnost_kamate");
                }else if(value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA)){
                    sql.appendln(" from bi_view_izvjestaj_po_rocnost_glavnica");
                }else if(value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA_KAMATE_ODVOJENO)){
                    sql.appendln(" from bi_view_izvjestaj_po_rocnost_komitent");
                }else if(value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_PARTIJA)){
                    sql.appendln(" from bi_view_izvjestaj_po_rocnost_partija");
                }else{ 
                    sql.appendln(" from bi_view_izvjestaj_po_rocnost");
                }
            }
            sql.appendWhere();
            sql.appPrefix();
            sql.append("WHERE godina = DATEPART(year,'");
            sql.append(value.get(JBDataDictionary.BI_ROCNOST__DATUM));
            sql.append("')");            
            sql.append("AND mjesec = DATEPART(month,'");
            sql.append(value.get(JBDataDictionary.BI_ROCNOST__DATUM));
            sql.append("')");            
            sql.append("and dan = DATEPART(day,'");
            sql.append(value.get(JBDataDictionary.BI_ROCNOST__DATUM));
            sql.append("')");
            String _profitni_centar = value.getProfitniCentar();
            String _profitni_centar_pocetak = _profitni_centar;
            String _profitni_centar_kraj = _profitni_centar;
            if(_profitni_centar.length()<1){
                _profitni_centar_pocetak = "0";
                _profitni_centar_kraj = "99000";
            } else if(_profitni_centar.startsWith("99000")){
                _profitni_centar_pocetak = "0";
                _profitni_centar_kraj = "99000";
            }
            sql.appGreatherOrEqual("AND","isnull(profitni_centar,0)", _profitni_centar_pocetak);
            sql.appLessOrEqual("AND", "isnull(profitni_centar,0)", _profitni_centar_kraj);
            sql.appGreatherOrEqual("AND","organizacijska_jedinica", value.getOrganizacijskaJedinicaOd());
            sql.appLessOrEqual("AND", "organizacijska_jedinica", value.getOrganizacijskaJedinicaDo());
            sql.appIn("AND", "maticni_broj", value.getMaticniBroj());
            if(value.get("izbor_ocjene").indexOf(",")>0) //ovdje se uzima lista sa zarezom npr. a,b,c
                sql.appIn("AND", "ocjena_ukupna", AS2String.splitString(value.get("izbor_ocjene")));
            else //ovdje uzima npr. b%, korisnik unese b a LIKE doda b1,b2,b3
                sql.appLike("AND", "ocjena_ukupna", value.get("izbor_ocjene"));
            sql.appIn("AND", "vrsta_proizvoda", value.get("vrsta_proizvoda"));
            /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
            sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
            /*ograničavanje za sigurnosne razine KRAJ*/
            if(value.getAkcija().equals(RocnostVo.IZRACUNAJ))
                program_radi = false;
            return sql;
        } catch (AS2Exception e) {
                if(!e.getErrorCode().equals("10010"))     
                	program_radi = false;
                throw e;
        } catch (Exception e) {
            if(value.getAkcija().equals(RocnostVo.IZRACUNAJ))
                program_radi = false;
            AS2DataAccessException ex = new AS2DataAccessException("151");
            ex.addCauseException(e);
            throw ex;
        }
    }
    public RocnostRs daoListaRocnostiNadleznost(RocnostVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("select * from OLTP_PROD.dbo.fn_tbs_krediti_rocnost_nadleznost(?,?) ");
        sql.append("ORDER BY preko_365, od_181_do_365, od_151_do_180, od_121_do_150, od_91_do_120, od_61_do_90, od_31_do_60, do_30_dana");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        RocnostRs j2eers = new RocnostRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_ROCNOST__DATUM));
            pstmt.setString(2,value.get("@profitni_centar"));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}
