package hr.adriacomsoftware.app.server.gradani.da.jdbc;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class OsobaJdbc extends BankarskiJdbc {
    
    public OsobaJdbc() {
        setTableName("bi_gr_osoba"); 
    }
    public boolean daoFindIfExists(OsobaVo value)  {
    	 try{
    		J2EESqlBuilder sql = new J2EESqlBuilder("SELECT jmbg FROM bi_gr_osoba WHERE (jmbg = ?) ");
 	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setString(1, value.getJmbg());
	        pstmt.setMaxRows(1);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        if (j2eers.size()>0)
	            return true;
	        return false; 
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsobaRs daoFind(OsobaVo value, int maxRows)  {
    	 try{
    		J2EESqlBuilder sql = new J2EESqlBuilder();
    		sql.append("SELECT CONVERT(decimal(15,0),jmbg)as jmbg, rtrim(ime)+' '+rtrim(prezime) as ime_prezime, mjesto, ulica, postanski_broj, telefon FROM dbo.bi_gr_osoba ");
 	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(maxRows);
	        OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers; 
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsobaRs daoSearch(OsobaVo value, int maxRows)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT CONVERT(decimal(15,0),jmbg)as jmbg, rtrim(ime)+' '+rtrim(prezime) as ime_prezime, mjesto, ulica, postanski_broj, telefon FROM dbo.bi_gr_osoba ");
        sql.appEqual("AND", "jmbg", value.get("jmbg"));
		sql.appLike("AND", "(rtrim(ime)+' '+rtrim(prezime))", value.get("ime_prezime"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(maxRows);
	        OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsobaRs daoPodaciKlijenta(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("SELECT * FROM bi_gr_osoba ");
        if (value.getJmbg().length() > 0)
            sql.appEqualNoQuote("AND", "jmbg", value.getJmbg());
        else if (value.getOib().length() > 0)
            sql.appEqualNoQuote("AND", "oib", value.getOib());
        else if (value.get("ime_prezime").length() > 0)
            sql.appLike("AND", "(rtrim(ime)+' '+rtrim(prezime))", value.get("ime_prezime"));
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(1);
	        OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsobaRs daoFindPodatkeKlijenta(OsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * ");
        sql.appendln("FROM bi_gr_osoba ");
        sql.appLike("AND", "(rtrim(ime)+' '+rtrim(prezime))", value.get("ime_prezime"));
        sql.appEqualNoQuote("AND", "jmbg", value.getJmbg()); 
        sql.appEqualNoQuote("AND", "oib", value.getOib());
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiSamoZaPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.appendln("ORDER BY ime");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(1);
	        OsobaRs j2eers = new OsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoPrometOsobe(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.appendln("select * from dbo.fn_tbl_gr_promet(?,?,?) ");
        sql.append(" ORDER BY broj_partije, datum_knjizenja, broj_konta");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setDate(2,value.getAsSqlDate("datum_od"));
			pstmt.setDate(3,value.getAsSqlDate("datum_do"));
			pstmt.setMaxRows(10000);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}