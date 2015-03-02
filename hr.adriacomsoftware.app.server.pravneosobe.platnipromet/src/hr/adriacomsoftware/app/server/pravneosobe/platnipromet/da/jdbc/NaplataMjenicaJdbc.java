package hr.adriacomsoftware.app.server.pravneosobe.platnipromet.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.pravneosobe.dto.NaplataMjenicaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.NaplataMjenicaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class NaplataMjenicaJdbc extends OLTPJdbc implements BankaConstants{
    public NaplataMjenicaJdbc() {
        setTableName("po_naplata_mjenica");
    }
    public NaplataMjenicaRs daoLoad(NaplataMjenicaVo value, boolean pretrazivanje)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_po_naplata_mjenica ");
        sql.appGreatherOrEqual("AND", "datum_dospijeca", value.get("datum_od1"));
        sql.appLessOrEqual("AND", "datum_dospijeca", value.get("datum_do1"));
        sql.appendln("ORDER BY datum_dospijeca,datum_zaprimanja,vrijeme_zadnje_izmjene ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			NaplataMjenicaRs j2eers = new NaplataMjenicaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE dbo.po_naplata_mjenica SET ispravno = 0 " +
				   "WHERE id_mjenice = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_mjenice"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public NaplataMjenicaRs daoIzvjestaji(NaplataMjenicaVo value)  {
	    J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_gr_naplata_mjenica_facade");
		sp.append(" (?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setDate(3,value.getAsSqlDate(J2EEDataDictionary.DATUM));
			cs.setObject(4,value.get("@@report_selected"));
			NaplataMjenicaRs j2eers = new NaplataMjenicaRs(transformResultSet(cs.executeQuery()));
			cs.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	} 
}