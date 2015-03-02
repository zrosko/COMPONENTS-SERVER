package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.adriacomsoftware.app.common.or.dto.OrGubitakRs;
import hr.adriacomsoftware.app.common.or.dto.OrGubitakVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class OrGubitakJdbc extends OrJdbc {
	public OrGubitakJdbc() {
		setTableName("or_gubitak");
	}
    public OrGubitakRs daoListaGubitaka(OrGubitakVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_or_gubitak_pogled where id_dogadaja = ? ");
        sql.append(" ORDER BY id_gubitka ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getIdDogadaja());
	        pstmt.setMaxRows(0);
	        OrGubitakRs j2eers = new OrGubitakRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}