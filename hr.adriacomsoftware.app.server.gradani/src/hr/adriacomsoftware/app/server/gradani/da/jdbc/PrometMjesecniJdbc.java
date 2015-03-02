package hr.adriacomsoftware.app.server.gradani.da.jdbc;

import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrometMjesecniJdbc extends BankarskiJdbc {
    public static final String MAX_BROJ_STAVKE_SQL = "SELECT isnull(max(broj_stavke),50314958) as max_broj_stavke  FROM bi_gr_promet_mjesecni ";
    //mjesecni promet ima ove vrste
    public final static int VRSTA_A = 11;
    public final static int VRSTA_B = 12;
    public final static int VRSTA_C = 13;
    
    public PrometMjesecniJdbc() { 
        setTableName("bi_gr_promet_mjesecni");
    } 
    public void daoBrisiStarePodatke() {
        //podaci stariji od 15 dana nisu potrebni u tabeli
    	try{
	    	PreparedStatement pstmt = getConnection().getPreparedStatement("DELETE FROM bi_gr_promet_mjesecni WHERE datum_prometa < DATEADD(dd, -15, GETDATE())");
	        pstmt.execute();
	        pstmt.close();
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public int daoMaxBrojStavke(int vrsta) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln(MAX_BROJ_STAVKE_SQL);
        if (vrsta == VRSTA_A){
            sql.appendln("WHERE (broj_partije BETWEEN 7000000000 AND 7399999999 ");
            sql.appendln("OR broj_partije BETWEEN 3000000000 AND 3099999999 ");
            sql.appendln("OR broj_partije BETWEEN 3300000000 AND 3499999999 ");
            sql.appendln("OR broj_partije BETWEEN 4800000000 AND 4899999999 ");
            sql.appendln("OR broj_partije BETWEEN 3200000000 AND 3299999999 ");
            sql.appendln("OR broj_partije BETWEEN 3100000000 AND 3199999999 ");
            sql.appendln("OR broj_partije BETWEEN 3600000000 AND 3699999999) ");
        }else if (vrsta == VRSTA_B){
            sql.appendln("WHERE (broj_partije < 3000000000)  ");
        }else if (vrsta == VRSTA_C){
            sql.appendln("WHERE (broj_partije BETWEEN 4000000000 AND 4799999999 ");
            sql.appendln("OR broj_partije BETWEEN 4900000000 AND 4999999999) ");
	    }else{
	       //za novu logiku ne treba gledati broj partije 
	    }
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
	        pstmt.close();
	        return j2eers.getAsInt("max_broj_stavke",50314958);
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}