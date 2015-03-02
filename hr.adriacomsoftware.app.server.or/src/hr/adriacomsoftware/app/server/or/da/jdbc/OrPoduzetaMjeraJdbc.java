package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.adriacomsoftware.app.common.or.dto.OrPoduzetaMjeraRs;
import hr.adriacomsoftware.app.common.or.dto.OrPoduzetaMjeraVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class OrPoduzetaMjeraJdbc extends OrJdbc {
	public OrPoduzetaMjeraJdbc() {
		setTableName("or_poduzeta_mjera");
	}
    public OrPoduzetaMjeraRs daoListaPodzetihMjera(OrPoduzetaMjeraVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_or_mjere_pogled where id_dogadaja = ? ");
        sql.append(" ORDER BY id_mjere ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getIdDogadaja());
	        pstmt.setMaxRows(0);
	        OrPoduzetaMjeraRs j2eers = new OrPoduzetaMjeraRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}