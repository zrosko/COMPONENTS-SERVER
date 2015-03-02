package hr.adriacomsoftware.app.server.pravneosobe.devize.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajRs;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaVo;
import hr.adriacomsoftware.app.common.jb.dto.RacunPartijeRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class PartijaDepozitaPravnihOsobaJdbc extends BankarskiJdbc implements BankaConstants{
    public PartijaDepozitaPravnihOsobaJdbc() {
        setTableName("bi_po_partija");
    }
    public PravnaOsobaRs daoPronadiPravneOsobe(OsnovniVo value, boolean pretrazivanje)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM bi_view_pravne_osobe_devize ");
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        if(pretrazivanje){
            sql.appLike("AND", "naziv_klijenta", value.getImePrezime()); //ime prezime znaci naziv
            sql.appEqualNoQuote("AND", "maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
            sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
            sql.appEqualNoQuote("AND", "oib", value.getOib());
        }
        sql.appendln(" ORDER BY kraj_orocenja");
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
    public RacunPartijeRs daoStanjePartijePoValutama(PartijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("SELECT * from dbo.bi_fn_po_devize_stanje_po_valutama (?) ");  
        sql.appEqual("AND", "maticni_broj", value.getMaticniBroj()); 
        sql.appEqual("AND", "broj_partije", value.getBrojPartije()); 
        sql.appendln(" ORDER BY maticni_broj, naziv, broj_partije, valuta_racuna, oznaka_valute ");  
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate("datum")); 
	        pstmt.setMaxRows(0);
	        RacunPartijeRs j2eers = new RacunPartijeRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }

    public RacunPartijeRs daoStanjePoValutama(PartijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("SELECT * from dbo.bi_fn_po_devize_stanje_valute_na_dan (?) ");  
        sql.appEqual("AND", "maticni_broj", value.getMaticniBroj()); 
        sql.appendln(" ORDER BY maticni_broj, naziv, valuta_racuna, oznaka_valute "); 
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate("datum")); 
	        pstmt.setMaxRows(0);
	        RacunPartijeRs j2eers = new RacunPartijeRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
 
    public IzvjestajRs daoPriljevOdljev(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_dp_interni_priljev_odljev (?) ");
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlasti(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
	        pstmt.setMaxRows(0);
	        IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IzvjestajRs daoTecajneRazlike(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("select * from bi_fn_po_devize_tecajne_razlike(?) ");
        sql.appendln("ORDER BY broj_konta");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
	        pstmt.setMaxRows(0);
	        IzvjestajRs j2eers = new IzvjestajRs( transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }

    public IzvjestajRs daoOroceniDevizniDepoziti(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("SELECT * FROM dbo.bi_fn_po_depozit_devizni_oroceni(?) " +
        		   "ORDER BY maticni_broj,broj_partije");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
	        pstmt.setMaxRows(0);
	        IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IzvjestajRs daoOroceniKunskiDepoziti(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("SELECT * FROM dbo.bi_fn_po_depozit_kunski_oroceni(?) " +
        		   "ORDER BY maticni_broj, broj_partije");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
	        pstmt.setMaxRows(0);
	        IzvjestajRs j2eers = new IzvjestajRs( transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
}
}