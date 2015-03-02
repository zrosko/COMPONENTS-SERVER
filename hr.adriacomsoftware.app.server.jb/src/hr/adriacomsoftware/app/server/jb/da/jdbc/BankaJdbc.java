package hr.adriacomsoftware.app.server.jb.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class BankaJdbc extends J2EEDataAccessObjectJdbc {
    public static final String BANKE_SQL = "select * from bi_banka order by naziv";
    public static final String POSLOVNICA_SQL = "select * from bi_organizacijska_jedinica order by naziv";
    public static final String KONTNI_PLAN_SQL = "select * from bi_view_jb_kontni_plan order by broj_konta";
    public static final String TIP_RACUNA_SQL = "select * from bi_tip_racuna order by tip_racuna";
    public static final String VRSTA_PROMETA_RACUNA_SQL ="SELECT * FROM bi_vrsta_prometa_racuna ORDER BY vrsta_prometa";
    public static final String TECAJNA_LISTA_SQL = "SELECT TL.*,VAL.oznaka_valute "+
                     "FROM dbo.bi_tecajna_lista AS TL LEFT OUTER JOIN " +
                     "dbo.bi_valuta AS VAL ON TL.sifra_valute = VAL.sifra_valute ";
    
    public BankaJdbc() {
        setTableName("");
    }
    public AS2RecordList daoFindListu(String sql)  {
    	try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql);
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers;
    	} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoFindTecajnuListu(OsnovniVo value)  {
        if(value.get("datum").length()==0)
            value.set("datum", AS2Date.getCurrentDateTimeAsString());
        J2EESqlBuilder sql = new J2EESqlBuilder();
        if(value.get("upit").equals("tecajna_lista_kratka")){
        	sql.append("select * from fn_tbl_banka_tecajna_lista(?) ");
        	sql.append("ORDER BY oznaka,sifra_valute");
        }else{
            sql.append(TECAJNA_LISTA_SQL);
            sql.append("where TL.pocetni_datum <= '");
            sql.append(value.get("datum").substring(0,10));
            sql.append("' AND TL.zavrsni_datum >= '");
            sql.append(value.get("datum").substring(0,10));
            sql.appendln("' ORDER BY VAL.oznaka_valute");
        }
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        if(value.get("upit").equals("tecajna_lista_kratka")){
	        	pstmt.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
	        }
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }

    public OsnovniRs daoCallStoredProcedure(String sp_name, OsnovniVo value)  {
        StringBuffer sp = new StringBuffer();	
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM));
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoFindObrazac4(OsnovniVo value)  {
        if(value.get("datum").length()==0)
            value.set("datum", AS2Date.getCurrentDateTimeAsString());
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("select * from dbo.bi_fn_hnb_obrazac4 ('");
        sql.append(value.get("datum").substring(0,10));
        sql.append("')");
        sql.appendln(" ORDER BY vrsta_transakcije, redni_broj");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        //u rs ubaci datum transakcije
	        j2eers.set("datum", value.get("datum"));
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoFindObrazac1(OsnovniVo value)  {
        if(value.get("datum").length()==0)
            value.set("datum", AS2Date.getCurrentDateTimeAsString());
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("select * from dbo.bi_fn_hnb_obrazac1 ('");
        sql.append(value.get("datum").substring(0,10));
        sql.append("')");
        sql.appendln(" ORDER BY vrsta_transakcije, redni_broj");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        //u rs ubaci datum transakcije
	        j2eers.set("datum", value.get("datum"));
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoBrutoBilanca(OsnovniVo value)  {
        if(value.get("datum").length()==0)
            value.set("datum", AS2Date.getCurrentDateTimeAsString());
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("select * from dbo.bi_fn_banka_bilanca ('");
        sql.append(value.get("datum").substring(0,10));
        sql.append("')");
        sql.appendln(" ORDER BY broj_konta");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}