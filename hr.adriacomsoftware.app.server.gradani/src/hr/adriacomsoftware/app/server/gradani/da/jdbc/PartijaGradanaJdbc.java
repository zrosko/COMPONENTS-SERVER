package hr.adriacomsoftware.app.server.gradani.da.jdbc;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PartijaGradanaJdbc extends J2EEDataAccessObjectJdbc {
    public PartijaGradanaJdbc() {
        setTableName("bi_po_partija");
    }
    public boolean daoFindIfExists(OsnovniVo value) {
        int counter = 1;
        J2EESqlBuilder sql = new J2EESqlBuilder("SELECT broj_partije FROM bi_gr_partija WHERE (broj_partije = ?) ");
        try{
            PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
            pstmt.setObject(counter, value.getBrojPartije());
            pstmt.setMaxRows(0);
            AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
            pstmt.close();
            if (j2eers.size()>0)
                return true;
            return false;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public PartijaRs daoFindListaOtvorenihPartijaOsobe(OsobaVo value) {
        //03.12.2009. Maja: dodane partije povezanih osoba
        //String sql ="SELECT *, convert(decimal(15,0),broj_partije) as broj_partije_ FROM bi_gr_partija WHERE datum_zatvaranja is null and jmbg = ? ";
    	J2EESqlBuilder sql = new J2EESqlBuilder("SELECT * FROM dbo.fn_tbs_prn_gr_partije_povezane_osobe(?) ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getJmbg());
	        pstmt.setMaxRows(0);
	        PartijaRs j2eers = new PartijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public boolean daoFindPartijaOsobe(OsobaVo value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder("SELECT broj_partije FROM bi_gr_partija WHERE jmbg = ? and broj_partije = ? ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getJmbg());
	        pstmt.setObject(2, value.getBrojPartije());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        if (j2eers.size()>0)
	            return true;
	        return false; 
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public boolean daoFindPartijePovezanihOsoba(OsobaVo value) {
    	J2EESqlBuilder sql = new J2EESqlBuilder("SELECT broj_partije FROM fn_tbs_prn_gr_partije_povezane_osobe(?) WHERE broj_partije = ? ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getJmbg());
	        pstmt.setObject(2, value.getBrojPartije());
	        pstmt.setMaxRows(0);
	        AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
	        pstmt.close();
	        if (j2eers.size()>0)
	            return true;
	        return false; 
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}