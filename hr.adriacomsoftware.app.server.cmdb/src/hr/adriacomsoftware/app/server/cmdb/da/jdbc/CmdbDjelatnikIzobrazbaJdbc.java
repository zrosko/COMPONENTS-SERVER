package hr.adriacomsoftware.app.server.cmdb.da.jdbc;

import hr.adriacomsoftware.app.common.cmdb.dto.DjelatnikIzobrazbaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class CmdbDjelatnikIzobrazbaJdbc extends CMDBJdbc {
    public CmdbDjelatnikIzobrazbaJdbc() {
        setTableName("cmdb_djelatnik_izobrazba");
    }
    public AS2RecordList daoFind(DjelatnikIzobrazbaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT RTRIM(D.ime)+' '+RTRIM(D.prezime) AS ime_prezime,I.ocjena,");
		sql.append("I.opis,CONVERT(decimal(13, 0), I.jmbg) AS jmbg, I.id_izobrazbe ");
        sql.append("FROM cmdb_djelatnik_izobrazba AS I LEFT OUTER JOIN "); 
        sql.append("cmdb_djelatnik AS D ON D.jmbg = I.jmbg ");
        sql.append("WHERE id_izobrazbe = ? ");
        sql.append("order by id_izobrazbe");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setString(1,value.getIdIzobrazbe());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}