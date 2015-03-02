package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.ZAHDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.RizikRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.RizikVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class PoZahRizikJdbc extends PoZahJdbc {
    public PoZahRizikJdbc() {
        setTableName("po_zah_rizik");
    }
    //nema zahtjeva vec id_rizika
    public RizikVo daoLoadRizik(RizikVo value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT convert(varchar(20),CONVERT(decimal(18),R.jmbg_mb)) AS jmbg_mb_, ");
        sql.append("CONVERT(decimal(18),R.jmbg_mb) AS maticni_broj, ");
        sql.append("CONVERT(decimal(18),R.oib) AS oib_, R.* ");
        sql.append("FROM dbo.po_zah_rizik R " );
        sql.append("WHERE R.id_rizika = ? ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getIdRizika());
	        pstmt.setMaxRows(1);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close(); 
	        //POSLOVNI ODNOS BANKA
	        RizikVo povrat_vo = new RizikVo(j2eers);
	        povrat_vo.setDatumProcjeneRizika(povrat_vo.getDatumRizika());
	        return povrat_vo;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public String daoZadnjiBrojRizika(RizikVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder("SELECT isnull(max(id_rizika),0) as id_rizika FROM po_zah_rizik "); 
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
	        return j2eers.get("id_rizika");
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }  
    public RizikVo daoFindFinancijskePodatke(RizikVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT * ");
        sql.append("from fn_tbl_po_zah_rizik_bonitet_podaci(?,?,?) ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_PROCJENE_RIZIKA));
	        pstmt.setObject(3,value.getBrojZahtjeva());
	        pstmt.setMaxRows(1);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close(); 
	        RizikVo povrat_vo = new RizikVo(j2eers);
	        return povrat_vo;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public RizikVo daoFindPoslovniOdnosZaProcjenu(RizikVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT ");
        sql.append("BI_PROD.dbo.fn_scl_po_krediti_klijent_dospjeli_dug_BASELII(?,?) as dospjeli_dug,");
        sql.append("BI_PROD.dbo.fn_scl_po_krediti_max_broj_dana_kasnjenja_BASELII(?,?) as max_broj_dana_kasnjenja, ");
        sql.append("dbo.fn_scl_zah_odnos_zaduzenost_osiguranje(?) as odnos_zaduzenosti_osiguranja ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getMaticniBroj());
	        pstmt.setDate(2,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_PROCJENE_RIZIKA));
	        pstmt.setObject(3,value.getMaticniBroj());
	        pstmt.setDate(4,value.getAsSqlDate(ZAHDataDictionary.PO_ZAH_ZAHTJEV__DATUM_PROCJENE_RIZIKA));
	        pstmt.setObject(5,value.getBrojZahtjeva());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close(); 
	        RizikVo povrat_vo = new RizikVo(j2eers);
	        return povrat_vo;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public RizikVo daoFindProcjenaRizika(RizikVo value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT * ");
        sql.append("from fn_tbl_po_rizik_misljenje (?) ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getIdRizika()); 
	        pstmt.setMaxRows(1);
	        AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close(); 
	        RizikVo povrat_vo = new RizikVo(j2eers);
	        return povrat_vo;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public RizikRs daoFindProcjenaRizikaLista(RizikVo value, boolean pretrazivanje)  {
        /* Samo polja potrebna za unos i izmjenu po_zah_rizik tabele. */
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT id_rizika, status_procjene, broj_zahtjeva ,CONVERT(char(10), datum_rizika, 104) as datum_rizika_");
        sql.append(",CONVERT(decimal(18),jmbg_mb) AS jmbg_mb,naziv,operater,iznos");
        sql.append(",ocjena,rizik,opis_zahtjeva,CONVERT(decimal(18),oib) as oib,datum_rizika,id_rizika");          
        sql.append(",CONVERT(decimal(18),jmbg_mb) AS maticni_broj FROM po_zah_rizik ");
        if(pretrazivanje){
            sql.appEqual("AND", "broj_zahtjeva", value.getBrojZahtjeva()); 
		    sql.appLike("AND", "naziv", value.getImePrezime()); 
		    sql.appLike("AND","status_procjene",value.getStatusProcjene());
		    sql.appLike("AND","rizik",value.getRizik());
		    sql.appEqualNoQuote("AND", "jmbg_mb", value.getJmbgMb()); 
		    sql.appEqualNoQuote("AND", "oib", value.getOib());
        }  
        sql.appEqual("AND", "isnull(ispravno,1)", "1"); 
        sql.append(" order by datum_rizika desc ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        RizikRs j2eers = new RizikRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close(); 
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public boolean daoFindIfExists(String broj_zahtjeva)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT 1 FROM po_zah_rizik where broj_zahtjeva = '" +broj_zahtjeva+"'");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
	        int broj_slogova = j2eers.size();
	        if(broj_slogova > 0)
	            return true;
	        else
	            return false;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
	public RizikVo daoDupliciranjeMisljenja(RizikVo value)  {
		String sp_name = "stp_po_zah_rizik_dupliciranje";
		J2EESqlBuilder sp = new J2EESqlBuilder();
		int count=0;
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?) }");
		try {
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(++count, value.getIdRizika());
			cs.setObject(++count, value.getOperater());
			cs.registerOutParameter(++count, java.sql.Types.INTEGER);
			cs.execute();
			int status = cs.getInt(count);
			cs.close();
			if (status == 0)
				throw new AS2DataAccessException("14502");
			value.setIdRizika(status + "");// vraća novi id rizika nazad na klijent
			return value;
		} catch (AS2DataAccessException e) {
			throw e;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public String daoFindIdRizika(String broj_zahtjeva)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT id_rizika FROM po_zah_rizik where broj_zahtjeva = '" +broj_zahtjeva+"'");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
	        int broj_slogova = j2eers.size();
	        if(broj_slogova > 0)
	            return j2eers.getRowAt(0).get("id_rizika");
	        else
	            return null;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
  }