package hr.adriacomsoftware.app.server.pravneosobe.da.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PravnaOsobaJdbc extends BankarskiJdbc {
    private String SQL_FIND_IF_EXIST = "SELECT maticni_broj FROM bi_po_pravna_osoba WHERE (maticni_broj = ?) ";
    public PravnaOsobaJdbc() {
        setTableName("bi_po_pravna_osoba");
    }    
    public PravnaOsobaRs daoPronadiPravneOsobe(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT maticni_broj, naziv, mjesto, telefon, godina_pocetka_poslovanja, ocjena_ukupna, ");
		sql.append("CONVERT(char(15), CONVERT(decimal(15, 0), oib)) AS oib ");
        sql.appendln("FROM bi_po_pravna_osoba ");
        //zr.14.4.2010. samo obrnike
        if(value.get("@obrtnici").equals("1")){
            sql = new J2EESqlBuilder();
            sql.append("select * from view_po_obrtnici "); 
        }
        sql.appLike("AND", "naziv", value.getImePrezime()); //ime prezime znaci naziv
        sql.appEqualNoQuote("AND", "maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
        sql.appEqualNoQuote("AND", "oib", value.getOib());
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiSamoZaPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.appendln("ORDER BY naziv");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        PravnaOsobaRs as2_rs = new PravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PravnaOsobaRs daoListaPravnihOsoba(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT maticni_broj, naziv, mjesto, telefon, godina_pocetka_poslovanja, ocjena_ukupna, ");
		sql.append("CONVERT(char(15), CONVERT(decimal(15, 0), oib)) AS oib ");
        sql.appendln("FROM bi_po_pravna_osoba  ");
        //zr.14.4.2010. samo obrnike
        if(value.get("@obrtnici").equals("1")){
            sql = new J2EESqlBuilder();
            sql.append("select top 20 * from view_po_obrtnici "); 
        }
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiSamoZaPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.appendln(" ORDER BY naziv");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        PravnaOsobaRs as2_rs = new PravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public boolean daoFindIfExists(PravnaOsobaVo value)  {
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(SQL_FIND_IF_EXIST);
	        pstmt.setString(1, value.getMaticniBroj());
	        pstmt.setMaxRows(1);
	        AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        if (as2_rs.size()>0)
	            return true;
	        return false;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
	    }
    }
    public PravnaOsobaRs daoFindPodatkeKlijenta(PravnaOsobaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * ");
        sql.appendln("FROM bi_po_pravna_osoba ");
        //zr.14.4.2010. samo obrnike
        if(value.get("@obrtnici").equals("1")){
        	sql = new J2EESqlBuilder();
            sql.append("select * from view_po_obrtnici "); 
        }
        sql.appLike("AND", "naziv", value.getImePrezime()); //ime prezime znaci naziv
        sql.appEqualNoQuote("AND", "maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
        sql.appEqualNoQuote("AND", "oib", value.getOib());
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiSamoZaPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.appendln("ORDER BY naziv");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(1);
	        PravnaOsobaRs as2_rs = new PravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
	    }
    }
    public OsnovniRs daoPrometPravneOsobe(OsnovniVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.appendln("select *,CMDB_PROD.dbo.fn_scl_isms_imovina_povjerljivost(1398) as isms_povjerljivost from bi_view_po_glavna_knjiga ");
		sql.append(" where maticni_broj = ? and datum_knjizenja between ? and ?");
        sql.append(" ORDER BY broj_partije, datum_knjizenja, broj_konta");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getMaticniBroj());
			pstmt.setDate(2,value.getAsSqlDate("datum_od"));
			pstmt.setDate(3,value.getAsSqlDate("datum_do"));
			pstmt.setMaxRows(10000);
			OsnovniRs as2_rs = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}