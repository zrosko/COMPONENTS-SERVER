package hr.adriacomsoftware.app.server.or.da.jdbc;

import hr.adriacomsoftware.app.common.or.dto.OrDogadajRs;
import hr.adriacomsoftware.app.common.or.dto.OrDogadajVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class OrDogadajJdbc extends OrJdbc {
	public OrDogadajJdbc() {
		setTableName("or_dogadaj");
	}
    public OrDogadajRs daoListaDogadaja(OrDogadajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_or_dogadaj_pogled  ");
        sql.append(" ORDER BY datum_nastanka desc ");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        OrDogadajRs j2eers = new OrDogadajRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OrDogadajRs daoPronadiDogadaje(OrDogadajVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_or_dogadaj_pogled  ");
        sql.appLike("AND", "naziv_dogadaja", value.getNazivDogadaja()); 
        sql.appEqual("AND", "kategorija", value.getKategorija());
        sql.appEqual("AND", "uzrok", value.getUzrok());
        sql.appEqual("AND", "organizacijska_jedinica_dogadaja", value.getOrganizacijskaJedinicaDogadaja());
        sql.appEqual("AND", "djelatnik", value.getDjelatnik());
        sql.appEqual("AND", "status_dogadaja", value.getStatusDogadaja());
        sql.appEqual("AND", "ocjena_visina_rizika", value.getOcjenaVisinaRizika());
        sql.appendln("ORDER BY id_dogadaja");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        OrDogadajRs j2eers = new OrDogadajRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}