package hr.adriacomsoftware.app.server.or.eksternalizacija.da.jdbc;

import hr.adriacomsoftware.app.common.or.eksternalizacija.dto.OrEksternalizacijaRs;
import hr.adriacomsoftware.app.common.or.eksternalizacija.dto.OrEksternalizacijaVo;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class OrEksternalizacijaJdbc extends OrJdbc {
	public OrEksternalizacijaJdbc() {
		setTableName("or_eksternalizacija");
	}
    public OrEksternalizacijaRs daoListaEksternalizacija(OrEksternalizacijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_or_eksternalizacija_pogled ");
        sql.appLike("AND", "naziv", value.get("ime_prezime")); 
        sql.appEqual("AND", "maticni_broj", value.getJmbg());
        sql.appEqual("AND", "oib", value.getOib());
        sql.append(" ORDER BY datum desc ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        OrEksternalizacijaRs j2eers = new OrEksternalizacijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}