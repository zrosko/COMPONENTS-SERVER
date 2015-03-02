package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsKontrolaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsKontrolaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class IsmsKontrolaJdbc extends CMDBJdbc {
    public IsmsKontrolaJdbc() {
        setTableName("isms_kontrola");
    }
    public IsmsKontrolaRs daoPronadiKontrole(IsmsKontrolaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_kontrola_pogled where ISNULL(rok_provedbe, getdate()) >= ? AND ISNULL(rok_provedbe, '29990101') <= ? ");
        sql.appendln("AND ISNULL(datum_provedbe, getdate()) >= ? AND ISNULL(datum_provedbe, '29990101') <= ? ");
        sql.appendWhere();
        sql.appPrefix();
		sql.appLike("AND", "naziv", value.getNaziv());
		sql.appLikeRight("AND", "oznaka", value.getOznaka());
		sql.appLike("AND", "opis", value.get("opis"));
        sql.appEqual("AND", "ocjena_djelotvornosti", value.getOcjenaDjelotvornosti());
        sql.appEqual("AND", "odgovorna_osoba", value.getOdgovornaOsoba());
        sql.appEqual("AND", "sukladnost", value.getSukladnost());
        if(!value.get("aktivno").equals("0A"))
            sql.appEqual("AND", "aktivno", value.getAktivno());
        if(!value.get("tip").equals("0U"))
            sql.appEqual("AND", "tip", value.getTip());
        if(!value.get("vrsta").equals("0PR"))
            sql.appEqual("AND", "vrsta", value.getVrsta());
        if(!value.get("razina_rizika").equals("00"))
            sql.appEqual("AND", "razina_rizika", value.getRazinaRizika());
        sql.appendln("order by oznaka, vrsta, tip ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1,value.getAsSqlDate(IsmsKontrolaVo.ISMS_KONTROLA__ROK_PROVEDBE));
			pstmt.setDate(2,value.getAsSqlDate(IsmsKontrolaVo.ISMS_KONTROLA__ROK_PROVEDBE_DO));
			pstmt.setDate(3,value.getAsSqlDate(IsmsKontrolaVo.ISMS_KONTROLA__DATUM_PROVEDBE));
			pstmt.setDate(4,value.getAsSqlDate(IsmsKontrolaVo.ISMS_KONTROLA__DATUM_PROVEDBE_DO));
	        pstmt.setMaxRows(0);
	        IsmsKontrolaRs j2eers = new IsmsKontrolaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IsmsKontrolaRs daoListaKontrola(IsmsKontrolaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_kontrola_pogled order by oznaka, vrsta, tip");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsKontrolaRs j2eers = new IsmsKontrolaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IsmsKontrolaRs daoListaKontrolaStatus(IsmsKontrolaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.append("SELECT * FROM dbo.fn_isms_kontrola_stanje('");
        //sql.appLikeRight("AND", "oznaka", value.getOznaka()); 
        sql.append(value.getOznaka()); 
        sql.append("') order by oznaka");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsKontrolaRs j2eers = new IsmsKontrolaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
     }
}