package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.garancije.da.jdbc;

import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.garancije.dto.GarancijaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.garancije.dto.GarancijaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;
import java.util.Calendar;

public final class PoZahGarancijaJdbc extends BankarskiJdbc {
    public PoZahGarancijaJdbc() {
        setTableName("po_zah_garancija");
    }
    public int daoFindNextBrojGarancije(GarancijaVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT max(redni_broj) as redni_broj "+
		" FROM po_zah_garancija where YEAR(datum_izdavanja) = ? and isnull(ispravno,1) = 1");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setInt(1, value.getDatumIzdavanja().get(Calendar.YEAR));
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers.getAsInt(GarancijaVo.PO_ZAH_GARANCIJA__REDNI_BROJ)+1;//povećaj za 1
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public GarancijaRs daoPronadiGarancije(GarancijaVo value, boolean pretrazivanje)  {
		J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.appendln("select * from view_po_zah_garancije_pogled ");
        sql.append(" where datum_izdavanja between ? and ? ");
        sql.appendWhere();
        sql.appPrefix();
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        if(pretrazivanje){
            sql.appLike("AND", "broj_garancije", value.getBrojGarancije()); 
            sql.appEqual("AND", "sifra_valute", value.getSifraValute());
            sql.appEqual("AND", "tip", value.getTip());
            sql.appEqual("AND", "vrsta", value.getVrsta());
	        sql.appLike("AND", "naziv", value.getNaziv()); 
	        sql.appEqualNoQuote("AND", "maticni_broj", value.getMaticniBroj()); 
	        sql.appEqualNoQuote("AND", "oib", value.getOib());
	        sql.appEqualNoQuote("AND", "operater_unosa", value.getOperaterUnosa());
	        sql.appEqualNoQuote("AND", "organizacijska_jedinica", value.getOrganizacijskaJedinica());
        }
        sql.appendln(" ORDER BY datum_izdavanja");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			//koristi se datum_unosa i datum_promjene kao pomocno polje
			pstmt.setDate(1,value.getAsSqlDate("datum_unosa"));
			pstmt.setDate(2,value.getAsSqlDate("datum_promjene"));
			pstmt.setMaxRows(0);
			GarancijaRs j2eers = new GarancijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public GarancijaRs daoProcitajGarancije(GarancijaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.appendln("select * from view_po_zah_garancije_pogled ");
        sql.append(" where isnull(ispravno,1) = 1 ");
        sql.appendWhere();
        sql.appPrefix();
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.appendln(" ORDER BY datum_izdavanja");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			GarancijaRs j2eers = new GarancijaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 }