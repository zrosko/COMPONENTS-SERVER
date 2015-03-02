package hr.adriacomsoftware.app.server.pravneosobe.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class JrrJdbc extends J2EEDataAccessObjectJdbc {
    
    public JrrJdbc() {
        setTableName("bi_po_hnb_jrr");
    }
    
    public PravnaOsobaRs daoPronadiPravneOsobe(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT J.maticni_broj, RTRIM(J.naziv) AS naziv, dbo.bi_fn_banka_vbdi_naziv(vbdi) as banka, ");
        sql.appendln("CONVERT(decimal(10, 0), J.broj_partije) AS broj_partije, CONVERT(char(10), J.datum_otvaranja, 104) AS datum_otvaranja,");
        sql.appendln("CONVERT(char(10), J.datum_zatvaranja, 104) AS datum_zatvaranja ");
        sql.appendln("FROM bi_po_hnb_jrr J");
        sql.appLike("AND", "J.naziv", value.getImePrezime()); //ime prezime znaci naziv
        sql.appEqualNoQuote("AND", "J.maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
        sql.appEqualNoQuote("AND", "oib", value.getOib());
        sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
        sql.appendln("ORDER BY J.naziv");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        PravnaOsobaRs j2eers = new PravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PravnaOsobaRs daoListaPravnihOsoba(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT J.maticni_broj, RTRIM(J.naziv) AS naziv, dbo.bi_fn_banka_vbdi_naziv(vbdi) as banka, ");
        sql.appendln("CONVERT(decimal(10, 0), J.broj_partije) AS broj_partije, CONVERT(char(10), J.datum_otvaranja, 104) AS datum_otvaranja,");
        sql.appendln("CONVERT(char(10), J.datum_zatvaranja, 104) AS datum_zatvaranja ");
        sql.appendln("FROM bi_po_hnb_jrr J");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(1000);
	        PravnaOsobaRs j2eers = new PravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PravnaOsobaRs daoPronadiPartijePravneOsobe(PravnaOsobaVo value)  {
        String _broj_jrr_partija;
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        //5 partija na izlistu ako nije navedeno drugaćije
        _broj_jrr_partija = value.getAsString("broj_jrr_partija","5");
        sql.appendln("SELECT TOP "+_broj_jrr_partija+" ");
        sql.appendln("J.maticni_broj, RTRIM(J.naziv) AS naziv, dbo.bi_fn_banka_vbdi_naziv(vbdi) as banka, ");
        sql.appendln("CONVERT(decimal(10, 0), J.broj_partije) AS broj_partije, CONVERT(char(10), J.datum_otvaranja, 104) AS datum_otvaranja,");
        sql.appendln("CONVERT(char(10), J.datum_zatvaranja, 104) AS datum_zatvaranja ");
        sql.appendln("FROM bi_po_hnb_jrr J  ");
        sql.appEqualNoQuote("AND", "J.maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
        sql.appendln(" and datum_zatvaranja is null");
        sql.appendln(" and datum_otvaranja <= ?");            
        sql.appendln("ORDER BY J.naziv");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        if(value.get(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE).length()==0)
	            pstmt.setDate(1,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_ZAPRIMANJA));
	        else
	            pstmt.setDate(1,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_OBRADE));	
	        pstmt.setMaxRows(1000);
	        PravnaOsobaRs j2eers = new PravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PravnaOsobaRs daoPronadiPravnuOsobuJrrSimple(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select distinct cast(cast(maticni_broj as bigint) as varchar) as maticni_broj, " +
        			 "cast(cast(oib as bigint) as varchar) as oib, naziv, ulica, postanski_broj, mjesto " +
        			 "from dbo.bi_po_hnb_jrr " +
        		     "where jmbg=0 and datum_zatvaranja is null and broj_partije <> 0 and glavni_racun=1 ");
        if(value.getProperty("as2_smartgwt")!=null){
        	 sql.appendln("and ('000'+cast(cast(maticni_broj as bigint) as varchar) like '%"+value.get("maticni_broj")+"%' and " +
        		     "'000'+cast(cast(oib as bigint) as varchar) like '%"+value.get("oib")+"%' and " +
        		     "naziv like '%"+value.get("naziv")+"%' ) ");
        }else{
        	 sql.appendln("and ('000'+cast(cast(maticni_broj as bigint) as varchar) like '%"+value.get("value")+"%' or " +
        		     "'000'+cast(cast(oib as bigint) as varchar) like '%"+value.get("value")+"%' or " +
        		     "naziv like '%"+value.get("value")+"%' ) ");
        		     
        }
        sql.appendln("ORDER BY naziv ");
        try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(100);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new PravnaOsobaRs(j2eers);
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}