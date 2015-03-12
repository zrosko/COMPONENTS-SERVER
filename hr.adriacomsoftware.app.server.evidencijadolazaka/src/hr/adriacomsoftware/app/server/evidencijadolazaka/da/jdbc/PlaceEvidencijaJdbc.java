package hr.adriacomsoftware.app.server.evidencijadolazaka.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class PlaceEvidencijaJdbc extends OLTPJdbc {

    public PlaceEvidencijaJdbc() {
        setTableName("place_evidencija");
    }
    
    public AS2RecordList daoFindByOrgJed(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_place_evidencija_obracun(?,?,?,?) order by id_spica_oj,prezime,ime ");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++,value.getProperty("datum_od"));
			pstmt.setObject(counter++,value.getProperty("datum_do"));
			pstmt.setObject(counter++,value.getProperty("radnik_id"));
			pstmt.setObject(counter++,value.getProperty("id_spica_oj"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public AS2RecordList daoFindOneByOrgJed(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_place_evidencija_obracun(?,?,0,?) " +
					"where id_dnevne_evidencije  = ? ");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++,value.getProperty("datum_od"));
			pstmt.setObject(counter++,value.getProperty("datum_do"));
			pstmt.setObject(counter++,value.getProperty("id_spica_oj"));
			pstmt.setObject(counter++,value.getProperty("id_dnevne_evidencije"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public AS2RecordList daoFindPotvrde(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_place_evidencija_potvrda(?,?,?)");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++,value.getProperty("datum_od"));
			pstmt.setObject(counter++,value.getProperty("datum_do"));
			pstmt.setObject(counter++,value.getProperty("id_spica_oj"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public AS2RecordList daoFindMjecesno(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_place_evidencija_mjesecna(?,?) order by naziv ");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++,value.getProperty("mjesec"));
			pstmt.setObject(counter++,value.getProperty("godina"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public AS2RecordList daoFindGO(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbl_place_godisnji_odmor(?,?) order by prezime_ime");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			String dodatak = "1231";
			if(value.getProperty("godina").equals(""))
				dodatak="";
			pstmt.setObject(counter++,value.getProperty("godina")+dodatak);
			pstmt.setObject(counter++,value.getProperty("id_spica_oj"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public AS2RecordList daoFindGORadnik(AS2Record value)  {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.fn_tbs_place_godisnji_odmor_radnik_detalji(?,?) order by mj");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++,value.getProperty("godina"));
			pstmt.setObject(counter++,value.getProperty("radnik_id"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    
    public AS2RecordList daoIzvjestaji(AS2Record value)  {
		int counter = 1;
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_place_facade");
		sp.append(" (?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(counter++,value.getProperty("@@report_selected"));
			cs.setObject(counter++,value.getProperty("datum_od"));
			cs.setObject(counter++,value.getProperty("datum_do"));
			cs.setObject(counter++,value.getProperty("id_spica_oj"));
			cs.setObject(counter++,value.getProperty("mjesec"));
			cs.setObject(counter++,value.getProperty("godina"));
			AS2RecordList as2_rs = transformResultSet(cs.executeQuery());
			cs.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
	    }
	}  
    public AS2RecordList daoPokreniObradu(AS2Record value)  {
		int counter = 1;
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_place_facade_obrada");
		sp.append(" (?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(counter++,value.getProperty("@@process_selected"));
			cs.setObject(counter++,value.getProperty("datum_od"));
			cs.setObject(counter++,value.getProperty("datum_do"));
			cs.setObject(counter++,value.getProperty("id_spica_oj"));
			cs.setObject(counter++,value.getProperty("mjesec"));
			cs.setObject(counter++,value.getProperty("godina"));
			cs.setObject(counter++,value.getProperty("korisnik"));
			AS2RecordList as2_rs = transformResultSet(cs.executeQuery());
			cs.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 
    public AS2RecordList daoFindByRadnik(AS2Record value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder();
    	sql.append("select id_dnevne_evidencije, element_obracuna_id, datum, korisnik "
				 + " from dbo.view_place_evidencija "
				 + " where radnik_id = ? and datum >= cast(GETDATE() as DATE) ");
		int counter = 1;
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(counter++,value.getProperty("radnik_id"));
			pstmt.setMaxRows(0);
			AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public AS2RecordList daoAddBuduceEvidencije(AS2Record value) {
    	int counter = 1;
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_place_evidencija_radnik_unos_razdoblje");
		sp.append(" (?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(counter++,value.getProperty("radnik_id"));
			cs.setObject(counter++,value.getProperty("element_obracuna_id"));
			cs.setObject(counter++,value.getProperty("korisnik"));
			cs.setObject(counter++,value.getProperty("datum_od"));
			cs.setObject(counter++,value.getProperty("datum_do"));
			AS2RecordList as2_rs = transformResultSet(cs.executeQuery());
			cs.close();
			return as2_rs;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 
    
}