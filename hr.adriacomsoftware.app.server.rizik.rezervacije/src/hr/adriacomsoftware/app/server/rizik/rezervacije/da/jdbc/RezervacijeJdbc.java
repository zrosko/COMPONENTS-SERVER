package hr.adriacomsoftware.app.server.rizik.rezervacije.da.jdbc;

import hr.adriacomsoftware.app.common.rizik.rezervacije.dto.RezervacijaRs;
import hr.adriacomsoftware.app.common.rizik.rezervacije.dto.RezervacijaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class RezervacijeJdbc extends BankarskiJdbc {
    
    public RezervacijeJdbc() {
        setTableName("bi_rezervacije");
    }
    public RezervacijaRs daoListaRezervacija(RezervacijaVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("select * from rizik_prod.dbo.fn_tbs_rezervacije_pogled() order by broj_partije");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        RezervacijaRs j2eers = new RezervacijaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}