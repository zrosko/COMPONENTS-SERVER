package hr.adriacomsoftware.app.server.jb.da.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class HborJdbc extends J2EEDataAccessObjectJdbc {
     public HborJdbc() {
        setTableName("bi_po_krediti_hbor_dodatno");
    }
 	public OsnovniVo daoProvjeriPartiju(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select  cast(broj_partije as bigint) broj_partije " +
				   " from dbo.bi_po_krediti_hbor_dodatno " +
				   " where broj_partije = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getBrojPartije());
			pstmt.setMaxRows(1);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			OsnovniVo j2eevo = new OsnovniVo(loc_rs);
			pstmt.close();
	        return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	 }
 	 public OsnovniRs daoLoadAll(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select broj_partije, broj_ugovora, broj_racuna_hbor, " +
				   " convert(varchar(10),datum_otvaranja,104) as datum_sklapanja_ugovora " +
				   //" ,vrijeme_zadnje_izmjene, operater_zadnje_izmjene " +
				   " from bi_po_krediti_hbor_dodatno " +
				   " order by vrijeme_zadnje_izmjene ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new OsnovniRs(j2eers);
	 	} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
 	}
}