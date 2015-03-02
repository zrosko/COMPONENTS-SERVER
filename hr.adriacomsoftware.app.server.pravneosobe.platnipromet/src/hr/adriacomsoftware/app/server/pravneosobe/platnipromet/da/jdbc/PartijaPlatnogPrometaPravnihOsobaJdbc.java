package hr.adriacomsoftware.app.server.pravneosobe.platnipromet.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajRs;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class PartijaPlatnogPrometaPravnihOsobaJdbc extends BankarskiJdbc implements BankaConstants{
    public PartijaPlatnogPrometaPravnihOsobaJdbc() {
        setTableName("");
    }
    public PravnaOsobaRs daoPronadiPravneOsobe(OsnovniVo value, boolean pretrazivanje)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM bi_view_pravne_osobe_platni_promet ");
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        if(pretrazivanje){
            sql.appLike("AND", "naziv_klijenta", value.getImePrezime()); //ime prezime znaci naziv
            sql.appEqualNoQuote("AND", "maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
            sql.appEqualNoQuote("AND", "oib", value.getOib());
            sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
        }
        sql.appendln("ORDER BY naziv_klijenta");
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
 	private void daoCallExecuteStoredProcedure(String sp_name, IzvjestajVo input_value)  {
 		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			//float ne prihvaca stored procedura
			//cs.setFloat(1,input_value.getPropertyAsFloatOrZero(JBDataDictionary.BI_PARTIJA__BROJ_PARTIJE));
			cs.setString(1,input_value.get(JBDataDictionary.BI_PARTIJA__BROJ_PARTIJE));
			cs.setDate(2,input_value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(3,input_value.getAsSqlDate( J2EEDataDictionary.DATUM_DO));
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

    public IzvjestajRs daoPregledPrometa(IzvjestajVo value)  {
        daoCallExecuteStoredProcedure("bi_po_poslovni_racun", value);
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("select * from bi_po_izvjestaj_poslovni_racun ");  
        sql.append("ORDER BY datum_knjizenja");   
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IzvjestajRs daoPoslovniRacun(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_po_poslovni_racun_dnevna_stanja(?) where broj_partije < 9999999999 order by tip_partije, broj_partije");
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
    public IzvjestajRs daoPoslovniRacunZaPartiju(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select * from bi_fn_po_poslovni_racun_dnevna_stanja_partija_glavni(?,?,?) order by tip_partije, broj_partije");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
	        pstmt.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
	        pstmt.setObject(3,value.getBrojPartije());
	        pstmt.setMaxRows(0);
	        IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IzvjestajRs daoPoslovniRacunMjesecno(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_po_poslovni_racun_mjesecno_saldo (?,?) ");
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.appendWhere(" broj_partije < 9999999999 order by tip_partije, broj_partije");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
	        pstmt.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
	        pstmt.setMaxRows(0);
	        IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoZatezneKamate(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_po_platni_promet_zatezna_kamata (?,?,?,?) ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
	        pstmt.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
	        pstmt.setBigDecimal(3,value.getAsBigDecimal(JBDataDictionary.BI_OTPLATNI_PLAN__IZNOS_KREDITA));
	        pstmt.setString(4,value.getPoFo());
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoMjesecnaRekapitulacija(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_po_poslovni_racun_mjesecna_rekapitulacija (?,?) ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
	        pstmt.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IzvjestajRs daoDnevnoProsjecnoStanjePoMaticnomBroju(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.fn_tbs_po_platni_promet_prosjek_stanja (?,?,?) ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
	        pstmt.setDate(3,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
	        pstmt.setMaxRows(0);
	        IzvjestajRs j2eers = new IzvjestajRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();            
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IzvjestajRs daoPoslovniRacun500000(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM dbo.bi_fn_po_poslovni_racuni_promet_preko_500000(?) " +
		 "ORDER BY broj_partije ");
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
    public IzvjestajRs daoPoslovniRacuniNaknada(IzvjestajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("select * from bi_fn_po_poslovni_racun_naknada(?) order by maticni_broj");
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
}