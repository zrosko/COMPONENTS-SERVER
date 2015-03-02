package hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.FakturaRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.FakturaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PoFaktSpecifikacijaJdbc extends OLTPJdbc {
    public PoFaktSpecifikacijaJdbc() {
        setTableName("po_fakt_specifikacija");
    }
    public FakturaRs daoPronadiSpecifikacije(FakturaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.appendln("select * from view_po_fakt_specifikacija_pogled ");
		sql.append(" where datum_specifikacije between ? and ? ");
        sql.appendWhere();
        sql.appPrefix();
        sql.appLike("AND", "id_spec", value.getIdSpec()); 
        if(value.getRegres().equals("1")){
            sql.appEqual("AND", "regres", value.getRegres());
        }
        sql.appEqual("AND", "broj_ugovora", value.getBrojUgovora());
        sql.appEqual("AND", "broj_zahtjeva", value.getBrojZahtjeva());
	    sql.appLike("AND", "naziv", value.getNaziv()); 
	    sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj()); 
	    sql.appEqualNoQuote("AND", "oib", value.getOib());
        sql.appEqualNoQuote("AND", "zaprimatelj", value.getZaprimatelj());
        sql.appEqualNoQuote("AND", "organizacijska_jedinica", value.getOrganizacijskaJedinica());
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.appendln(" ORDER BY datum_specifikacije desc");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1,value.getAsSqlDate("datum_specifikacije"));
			pstmt.setDate(2,value.getAsSqlDate("datum_specifikacije_do"));
			pstmt.setMaxRows(0);
			FakturaRs j2eers = new FakturaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}	        
    }
    public FakturaRs daoProcitajSveSpecifikacije(FakturaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.appendln("select * from view_po_fakt_specifikacija_pogled ");
        sql.appendln(" ORDER BY id_spec");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			FakturaRs j2eers = new FakturaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }