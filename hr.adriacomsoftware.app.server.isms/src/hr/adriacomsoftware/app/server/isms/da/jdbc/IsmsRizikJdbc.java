package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRizikVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.sql.PreparedStatement;


public final class IsmsRizikJdbc extends CMDBJdbc {
    public IsmsRizikJdbc() {
        setTableName("isms_rizik");
    }
    /* Pročitaj sve rizike za jednu informacijsku imovinu. */
    public AS2RecordList daoFind(AS2Record aFields) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendWhere();
        sql.appPrefix();
		sql.append("select prijetnja_naziv as prijetnja_, ranjivost_naziv as ranjivost_, vjerojatnost_rizika as vjerojatnost_, utjecaj_rizika as utjecaj_, R.* FROM view_isms_rizik_pogled R where id_imovine = ? ");
		sql.append(" order by razina_rizika desc");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,aFields.get(IsmsRizikVo.ISMS_RIZIK__ID_IMOVINE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    /* Pretraži sve rizike. */
    public IsmsRizikRs daoPronadiRizike(IsmsRizikVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_rizik_pogled  ");
        sql.appLike("AND", "naziv", value.getNaziv()); 
        if(value.getRazinaRizikaOd().length()==0)
            sql.appGreatherOrEqualNoQuote("AND", "razina_rizika_", "0"); 
        else
            sql.appGreatherOrEqualNoQuote("AND", "razina_rizika_", value.getRazinaRizikaOd());
        if(value.getRazinaRizikaDo().length()==0)
            sql.appLessOrEqualNoQuote("AND", "razina_rizika_", "125"); 
        else
            sql.appLessOrEqualNoQuote("AND", "razina_rizika_", value.getRazinaRizikaDo());
        sql.appEqualNoQuote("AND", "vrsta_prijetnje", value.get("vrsta_prijetnje")); 
        sql.appEqualNoQuote("AND", "id_prijetnje", value.getIdPrijetnje());
        sql.appEqualNoQuote("AND", "vrsta_ranjivosti", value.get("vrsta_ranjivosti")); 
        sql.appEqualNoQuote("AND", "id_ranjivosti", value.getIdRanjivosti()); 
        sql.appEqualNoQuote("AND", "nacin_smanjenja_rizika", value.getNacinSmanjenjaRizika()); 
        sql.appEqualNoQuote("AND", "vjerojatnost_rizika", value.getVjerojatnostRizika()); 
        sql.appEqualNoQuote("AND", "utjecaj_rizika", value.getUtjecajRizika());
        sql.appEqualNoQuote("AND", "vjerojatnost_otkrivanja", value.getVjerojatnostOtkrivanja());
        sql.appLike("AND", "naziv_imovine", value.getNazivImovine());
        sql.appEqualNoQuote("AND", "vrijednost_imovine", value.getVrijednostImovine());
        if(value.getIdKategorije().length()==0){
            sql.appLikeRight("AND", "id_kategorije", value.get("kategorija_imovine")); 
        }else
            sql.appLikeRight("AND", "id_kategorije", value.getIdKategorije());            
        sql.appEqualNoQuote("AND", "vlasnik", value.getVlasnik());
        sql.appLikeRight("AND", "id_procesa", value.getIdProcesa());
        /* Ograničavanje za sigurnosne razine NE POSTOJI kod pretraživanja. 
         * Potreba da se prikaže imovina po precesima i org. jedinicama zahtjeva fleksibilnost. */
        sql.appendln(" ORDER BY razina_rizika desc ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsRizikRs j2eers = new IsmsRizikRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    /* Pročitaj sve rizike za organizacijsku jedinicu korisnika i sve podređene org. jedinice. */
    public IsmsRizikRs daoListaRizika(IsmsRizikVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_rizik_pogled ");
        /* Ograničavanje sigurnosne razine POČETAK. 
         * Korisnik vidi imovinu sovje i svih podredenih org. jedinica. */
        String profitni_centar = AS2SessionFactory.getInstance().getCurrentUser().get("org_jedinica");
        sql.append(" WHERE organizacijska_jedinica IN ( SELECT organizacijska_jedinica FROM BI_PROD.dbo.bi_fn_banka_org_jedinice_podredene(");
        sql.append(profitni_centar);
        sql.append("))");
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.append(" ORDER BY id_kategorije ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsRizikRs j2eers = new IsmsRizikRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}