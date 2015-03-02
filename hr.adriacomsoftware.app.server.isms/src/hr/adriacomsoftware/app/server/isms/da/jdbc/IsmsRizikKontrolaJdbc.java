package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikKontrolaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikKontrolaVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.sql.PreparedStatement;


public final class IsmsRizikKontrolaJdbc extends CMDBJdbc {
    public IsmsRizikKontrolaJdbc() {
        setTableName("isms_rizik_kontrola");
    }
    /* Procitaj sve mjere za rizik. */
    public AS2RecordList daoFind(AS2Record value) {
		String sql = "select * FROM "+
		" view_isms_rizik_kontrola where id_rizika = ? ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get(IsmsRizikKontrolaVo.ISMS_RIZIK_KONTROLA__ID_RIZIKA));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    /* Pretraži sve mjere za smanjivanje rizika. */
    public IsmsRizikKontrolaRs daoPronadiMjere(IsmsRizikVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_smanjivanje_rizika_pogled  ");
        sql.appLike("AND", "rizik", value.getNaziv()); 
        sql.appLike("AND", "naziv_kontrole", value.getNazivKontrole()); 
        sql.appLike("AND", "oznaka_kontrole", value.getOznakaKontrole()); 
        if(value.getRazinaRizikaOd().length()==0)
            sql.appGreatherOrEqualNoQuote("AND", "razina_rizika_prioritet", "0"); 
        else
            sql.appGreatherOrEqualNoQuote("AND", "razina_rizika_prioritet", value.getRazinaRizikaOd());
        if(value.getRazinaRizikaDo().length()==0)
            sql.appLessOrEqualNoQuote("AND", "razina_rizika_prioritet", "125"); 
        else
            sql.appLessOrEqualNoQuote("AND", "razina_rizika_prioritet", value.getRazinaRizikaDo());
        sql.appEqualNoQuote("AND", "status", value.getStatusKontrole()); 
        sql.appEqualNoQuote("AND", "odgovorna_osoba", value.getVlasnik());            

        /* Ograničavanje za sigurnosne razine NE POSTOJI kod pretraživanja.*/
        sql.appendln(" ORDER BY razina_rizika_prioritet desc");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsRizikKontrolaRs j2eers = new IsmsRizikKontrolaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    /* Pročitaj sve mjere za organizacijsku jedinicu korisnika i sve podređene org. jedinice. */
    public IsmsRizikKontrolaRs daoListaMjera(IsmsRizikVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_smanjivanje_rizika_pogled ");
        /* Ograničavanje sigurnosne razine POČETAK. 
         * Korisnik vidi imovinu sovje i svih podredenih org. jedinica. */
        String profitni_centar = AS2SessionFactory.getInstance().getCurrentUser().get("org_jedinica");
        sql.append(" WHERE organizacijska_jedinica IN ( SELECT organizacijska_jedinica FROM BI_PROD.dbo.bi_fn_banka_org_jedinice_podredene(");
        sql.append(profitni_centar);
        sql.append("))");
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.append(" ORDER BY razina_rizika_prioritet desc ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsRizikKontrolaRs j2eers = new IsmsRizikKontrolaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}