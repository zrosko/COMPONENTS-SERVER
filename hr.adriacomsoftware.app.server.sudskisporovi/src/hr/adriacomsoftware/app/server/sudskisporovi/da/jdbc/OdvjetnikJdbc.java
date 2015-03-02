package hr.adriacomsoftware.app.server.sudskisporovi.da.jdbc;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.services.J2EEOdvjetnikJDBCService;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public class OdvjetnikJdbc extends BankarskiJdbc {
    public OdvjetnikJdbc() {
        setTableName("*");
    }
	public J2EEConnectionJDBC getConnection(){
		try {
			return (J2EEConnectionJDBC) J2EEOdvjetnikJDBCService.getInstance().getConnection();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
    public OsnovniRs daoPronadiSudove(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("SELECT naziv, naziv as ime, Adresa1 as Street,  ");
        sql.appendln("LEFT(Adresa2,5) as Zip,RIGHT(Adresa2,len(Adresa2)-6) as City ");
        sql.appendln("FROM Sudovi ");
        sql.appLike("AND", "naziv", value.getNaziv()); //naziv suda
        sql.appendln("ORDER BY naziv");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoPronadiBiljeznike(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT FirstName + ' ' + LastName + '-' + City AS naziv, ");
        sql.appendln("FirstName + ' ' + LastName as ime, Street,Zip,City FROM Biljeznici ");
        sql.appendln("WHERE  FirstName + ' ' + LastName + '-' + City LIKE ");
        sql.appendln("'%"+value.getNaziv()+"%'");
        sql.appendln(" ORDER BY City, FirstName, LastName");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoPronadiSudoveJavneBiljeznikeInstitucije(OsnovniVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln(" SELECT naziv, ime, street, zip, city ");
        sql.appendln(" FROM OLTP_PROD.dbo.view_odvjetnik_lista_sud_jb ");
        sql.appendln(" WHERE naziv LIKE ");
        sql.appendln(" '%"+value.getNaziv()+"%'");
        sql.appendln(" ORDER BY city, naziv ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}