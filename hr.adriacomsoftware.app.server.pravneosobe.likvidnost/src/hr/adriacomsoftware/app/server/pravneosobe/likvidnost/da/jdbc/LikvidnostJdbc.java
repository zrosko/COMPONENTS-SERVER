package hr.adriacomsoftware.app.server.pravneosobe.likvidnost.da.jdbc;

import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.pravneosobe.likvidnost.dto.LikvidnostRs;
import hr.adriacomsoftware.app.common.pravneosobe.likvidnost.dto.LikvidnostVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class LikvidnostJdbc extends OLTPJdbc implements BankaConstants{
    public LikvidnostJdbc() {
        setTableName("banka_krediti_depoziti_likvidnosti");
    }
    public LikvidnostRs daoLoad(LikvidnostVo value, boolean pretrazivanje) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_po_likvidnost ");
        /*if(pretrazivanje){
            sql.appLike("AND", "naziv_klijenta", value.getImePrezime()); //ime prezime znaci naziv
            sql.appEqualNoQuote("AND", "maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
            sql.appEqualNoQuote("AND", "oib", value.getOib());
            sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije());
        }*/
        sql.appGreatherOrEqual("AND", "isnull(rok,getdate())", value.get("datum_od1"));
        sql.appLessOrEqual("AND", "rok", value.get("datum_do1"));
        sql.appendln("ORDER BY rok desc");
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			LikvidnostRs j2eers = new LikvidnostRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoRemove(AS2Record value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE dbo.banka_krediti_depoziti_likvidnosti SET ispravno = 0 " +
				   "WHERE id = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }   
}