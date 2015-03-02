package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.adriacomsoftware.app.common.or.dto.OrNadoknadaRs;
import hr.adriacomsoftware.app.common.or.dto.OrNadoknadaVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class OrNadoknadaJdbc extends OrJdbc {
	public OrNadoknadaJdbc() {
		setTableName("or_nadoknada");
	}
    public OrNadoknadaRs daoListaNadoknada(OrNadoknadaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_or_nadoknada_pogled where id_dogadaja = ? ");
        sql.append(" ORDER BY id_nadoknade ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.getIdDogadaja());
	        pstmt.setMaxRows(0);
	        OrNadoknadaRs j2eers = new OrNadoknadaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}