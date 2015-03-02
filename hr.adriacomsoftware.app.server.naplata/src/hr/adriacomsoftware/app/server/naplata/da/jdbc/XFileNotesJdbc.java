package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.ODVJETNIKJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class XFileNotesJdbc extends ODVJETNIKJdbc {
    
    public XFileNotesJdbc() {
        setTableName("XFileNotes");
    }

    public NaplataVo daoLoad(NaplataVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.XFileNotes " +
				   "WHERE FileNoteID = ? and Deleted = 0 ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getFileNoteID());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			NaplataVo j2eers = new NaplataVo(loc_rs);
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public NaplataRs daoLoadAll(NaplataVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT BI_PROD.dbo.fnFormatDate(CreatedDate, 'dd.MM.yyyy HH:mi:ss') as datum, " +
        		   "CreatedBy as kreirao, Notes as biljeska, * " +
        		   "FROM XFileNotes WHERE Deleted=0 and FileID = ? ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getFileID());
	        pstmt.setMaxRows(0);
	        NaplataRs j2eers = new NaplataRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers; 
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoCreate(NaplataVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		String datum = value.get("createddate").substring(0,10);
		String vrijeme = value.get("createddate").substring(11,19);
		sql.append("INSERT INTO XFileNotes " +
				   "SELECT ?, ?, '', '"+datum+"', '"+vrijeme+"', ?, 0, '', 0, 0, 0, ?,	?, ?, ?, '' ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getFileID());
			pstmt.setObject(2,value.getCreatedBy());
			pstmt.setObject(3,value.getNotes());
			pstmt.setObject(4,value.getCreatedBy());
			pstmt.setTimestamp(5,value.getAsSqlTimestamp(NaplataVo.XFILES__CREATEDDATE));
			pstmt.setObject(6,value.getCreatedBy());
			pstmt.setTimestamp(7,value.getAsSqlTimestamp(NaplataVo.XFILES__CREATEDDATE));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
}
    public void daoStore(NaplataVo value)  {
		String datum = value.get("createddate").substring(0,10);
		String vrijeme = value.get("createddate").substring(11,19);
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE dbo.XFileNotes SET Notes = ?, ModifiedBy = ?, " +
				   "ModifiedDate = getdate(),CreatedDate = ?,NoteDate = '"+
				   datum + "', NoteTime = '"+ vrijeme + "'  WHERE FileNoteID = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getNotes());
			pstmt.setObject(2,value.getModifiedBy());
			pstmt.setTimestamp(3,value.getAsSqlTimestamp(NaplataVo.XFILES__CREATEDDATE));
			pstmt.setObject(4,value.getFileNoteID());			
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE dbo.XFileNotes SET Deleted = 1, ModifiedBy = ?, " +
				   "ModifiedDate = getdate() WHERE FileNoteID = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("modifiedby"));
			pstmt.setObject(2,value.getProperty("filenoteid"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }

    /*
    public String daoZadnjiRedniBroj(PdvVo value) throws J2EEDataAccessException {
        J2EETrace.trace(J2EETrace.I, getTableName()+ " daoZadnjiRedniBroj begin");
        J2EEConnectionJDBC co = null;
        PdvVo j2eers = null;

        try {
            co = getConnection();
			Connection jco = co.getJdbcConnection();
			String sql = "if exists( SELECT (isnull(redni_broj,0)+1) as max_redni_broj " +
									"FROM   bi_pdv_unos_podataka WHERE organizacijska_jedinica=?) " +
									"begin " +
									"SELECT (isnull(redni_broj,0)+1) as max_redni_broj " +
									"FROM   bi_pdv_unos_podataka WHERE organizacijska_jedinica=? end " +
									"else select 1 as max_redni_broj ";
			PreparedStatement pstmt = jco.prepareStatement(sql);
			pstmt.setObject(1,value.getOrganizacijskaJedinica());
			pstmt.setObject(2,value.getOrganizacijskaJedinica());
			pstmt.setMaxRows(1);
			ResultSet rs = pstmt.executeQuery();
			J2EEResultSet loc_rs = J2EEResultSetUtility.getOneRowAsJ2EEResultSetFromJDBCResultSet(rs);
			j2eers = new PdvVo(loc_rs);
			pstmt.close();
            J2EETrace.trace(J2EETrace.I, getTableName()+ " daoZadnjiRedniBroj end");
            return j2eers.get("max_redni_broj");
        } catch (J2EEDataAccessException e) {
		    throw e;
        } catch (Exception e) {
            J2EETrace.trace(J2EETrace.E, e);
            J2EEDataAccessException ex = new J2EEDataAccessException("151");
            ex.addCauseException(e);
            throw ex;
        }
    }  
    public PdvRs daoFind(PdvVo value) throws J2EEDataAccessException {
        J2EETrace.trace(J2EETrace.I, getTableName()+ " daoFind begin");
		J2EEConnectionJDBC co = null;
		PdvRs j2eers = null;

		try	{
			co = getConnection();
			Connection jco = co.getJdbcConnection();
			J2EESqlBuilder sql = new J2EESqlBuilder();
			if(value.get("izvjestaj").equals("r1_razlike")){
			    sql.append("select *, '" + value.getSqlDateFromCalendarAsItIs(value,"datum_od") +"' as datum_od, '"
			    + value.getSqlDateFromCalendarAsItIs(value,"datum_do") +"' as datum_do ");
			    sql.append("from dbo.bi_pdv_unos_podataka ");
				sql.append("where ispravno in(1,0) and datum >= ? and datum <= ? ");
			}else{
			    sql.append("select * from dbo.fn_tbl_pdv_racun_lista() ");
			    sql.append("where isnull(ispravno,0) <> 1 and datum >= ? and datum <= ? ");
			}
			sql.appendWhere();
	        sql.appPrefix();
	        sql.appEqual("AND", "broj_partije", value.get("broj_partije"));
	        sql.appEqual("AND", "maticni_broj", value.get("maticni_broj"));
	        sql.appEqual("AND", "organizacijska_jedinica", value.get("organizacijska_jedinica"));
			sql.appLike("AND", "oznaka", value.get("oznaka"));
			 sql.append("order by redni_broj");
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1,value.getSqlDateFromCalendarAsItIs(value,"datum_od"));
			pstmt.setDate(2,value.getSqlDateFromCalendarAsItIs(value,"datum_do"));
			pstmt.setMaxRows(0);
            ResultSet rs = pstmt.executeQuery();
            j2eers = new PdvRs(J2EEResultSetUtility.getJ2EEResultSetFromJDBCResultSet(rs));
            pstmt.close();
            return j2eers; 
		}
		catch (Exception e)		{
			J2EETrace.trace(J2EETrace.E, e);
			J2EEDataAccessException ex = new J2EEDataAccessException("151");
			ex.addCauseException(e);
			throw ex;
		}
    }*/
}