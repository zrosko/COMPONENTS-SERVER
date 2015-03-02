package hr.adriacomsoftware.app.server.gk.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.jb.BankaConstants;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.sql.PreparedStatement;


public final class GlavnaKnjigaJdbc extends BankarskiJdbc implements BankaConstants {
    public static final String MAX_BROJ_STAVKE_SQL = "SELECT max(abs(redni_broj_stavke)) as max_broj_stavke FROM bi_glavna_knjiga";

    public GlavnaKnjigaJdbc() {
        setTableName("bi_glavna_knjiga");
    } 
    /**
     * 		sql.append("select * from bi_view_glavna_knjiga_pogled ");
		    sql.append("WHERE (organizacijska_jedinica IN (SELECT organizacijska_jedinica ");
		    sql.append("FROM bi_organizacijska_jedinica WHERE (organizacijska_jedinica = ?)))");
     *
     */
    public OsnovniRs daoPronadiStavke(OsnovniVo value, boolean pretrazivanje ){
    	J2EESqlBuilder sql = new J2EESqlBuilder();	
		sql.append("select * from bi_view_glavna_knjiga_pogled ");
        /*ograničavanje za podružnice POČETAK*/
        String profitni_centar = AS2SessionFactory.getInstance().getCurrentUser().getProfitCenter();            
        if(!profitni_centar.equals(PROFITNI_CENTAR_SVE))//CENTRALA VIDI SVA IZVJEŠĆA
            sql.appEqualNoQuote("AND", "profitni_centar", profitni_centar); 
        /*ograničavanje za podružnice KRAJ*/
        if(pretrazivanje){
	        sql.appEqualNoQuote("AND", "broj_partije", value.getBrojPartije()); 
	        sql.appEqualNoQuote("AND", "maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
        }
        sql.appendln(" ORDER BY datum_knjizenja desc");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(10000);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public OsnovniRs daoZaduzenostBezPartije(OsnovniVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder(); 
		sql.appendln("select * from fn_tbs_po_ostalo_zaduzenje(?,?) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.get("maticni_broj"));
			pstmt.setDate(2,value.getAsSqlDate(J2EEDataDictionary.DATUM));
			pstmt.setMaxRows(0);
			OsnovniRs j2eers = new OsnovniRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
	        return j2eers;
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}