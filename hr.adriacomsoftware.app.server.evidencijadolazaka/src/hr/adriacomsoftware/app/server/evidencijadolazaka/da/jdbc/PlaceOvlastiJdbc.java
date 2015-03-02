package hr.adriacomsoftware.app.server.evidencijadolazaka.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PlaceOvlastiJdbc extends OLTPJdbc {

	public PlaceOvlastiJdbc() {
		setTableName("place_ovlasti");
	}
	   public AS2RecordList daoFindOvlasti(AS2Record value) {
	    	J2EESqlBuilder sql = new J2EESqlBuilder();
			sql.append("select * from dbo.place_ovlasti order by id_spica_oj");
			try{
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setMaxRows(0);
				AS2RecordList as2_rs = transformResultSet(pstmt.executeQuery());
				pstmt.close();
				return as2_rs;
		   } catch (Exception e) {
				throw new AS2DataAccessException(e);
		   }
		}
	    public AS2Record daoLoadOvlasti(AS2Record value) {
	    	J2EESqlBuilder sql = new J2EESqlBuilder();		
			sql.append("select * from dbo.place_ovlasti "
					+ " where id_ovlasti = ? "
					+ " order by id_spica_oj ");
			try{
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setObject(1, value.get("id_ovlasti"));
				pstmt.setMaxRows(0);
				AS2RecordList as2_rs = transformResultSetOneRow(pstmt.executeQuery());
				AS2Record as2_vo = new AS2Record(as2_rs);
				pstmt.close();
				return as2_vo;
		    } catch (Exception e) {
				throw new AS2DataAccessException(e);
			}
		}
}