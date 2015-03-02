package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class IsmsImovinaJdbc extends CMDBJdbc {
    public IsmsImovinaJdbc() {
        setTableName("isms_imovina");
    }
    public IsmsImovinaRs daoPronadiImovinu(IsmsImovinaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_imovina_pogled  ");
        if(value.getIdImovine().length()>0){//trazi samo JEDNU imovinu
            sql.appEqual("AND", "id_imovine", value.getIdImovine());
        }else{//VISE redova
            sql.appLike("AND", "naziv", value.getNaziv()); 
            if(value.getIdKategorije().length()==0){
                sql.appLikeRight("AND", "id_kategorije", value.get("kategorija_imovine")); 
            }else
                sql.appLikeRight("AND", "id_kategorije", value.getIdKategorije()); 
            sql.appEqualNoQuote("AND", "vlasnik", value.getVlasnik());
            sql.appEqualNoQuote("AND", "odgovorna_osoba", value.getOdgovornaOsoba());
            sql.appLikeRight("AND", "id_zgrade", value.getIdZgrade());
            sql.appLikeRight("AND", "id_procesa", value.getIdProcesa());
            sql.appEqualNoQuote("AND", "povjerljivost", value.getPovjerljivost());
            sql.appEqualNoQuote("AND", "cjelovitost", value.getCjelovitost());
            sql.appEqualNoQuote("AND", "raspolozivost", value.getRaspolozivost());
            sql.appEqualNoQuote("AND", "vaznost", value.getVaznost());
            sql.appEqual("AND", "potvrda_vrijednosti", value.getPotvrdaVrijednosti());
            sql.appEqual("AND", "regulator_paznja", value.getRegulatorPaznja());
            /* Ograničavanje za sigurnosne razine NE POSTOJI kod pretraživanja. 
             * Potreba da se prikaže imovina po precesima i org. jedinicama zahtjeva fleksibilnost. */
            //sql.appendln("ORDER BY prioritet_procesa desc, razina, id_procesa, id_kategorije");
            sql.append(" ORDER BY id_procesa, id_kategorije ");
        }//VISE redova
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsImovinaRs j2eers = new IsmsImovinaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public IsmsImovinaRs daoListaImovine(IsmsImovinaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM view_isms_imovina_pogled ");
        /* Ograničavanje sigurnosne razine POČETAK. 
         * Korisnik vidi imovinu sovje i svih podredenih org. jedinica. */
        String profitni_centar = AS2SessionFactory.getInstance().getCurrentUser().get("org_jedinica");
        sql.append(" WHERE organizacijska_jedinica IN ( SELECT organizacijska_jedinica FROM dbo.fn_tbl_isms_org_jedinice_podredene(");
        sql.append(profitni_centar);
        sql.append("))");
        /*ograničavanje za sigurnosne razine KRAJ*/
        sql.append(" ORDER BY id_procesa, id_kategorije ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        IsmsImovinaRs j2eers = new IsmsImovinaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoStoreVlasnika(IsmsImovinaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("update isms_imovina set vlasnik = ? where vlasnik = ? ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get("novi_vlasnik"));
	        pstmt.setObject(2, value.get(IsmsImovinaVo.ISMS_IMOVINA__VLASNIK));
	        pstmt.executeUpdate();
	        pstmt.close();
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoStoreOrgJedinicu(IsmsImovinaVo value) {
		StringBuffer sp = new StringBuffer();
		sp.append("{call isms_org_jedinica_azuriraj ");
		sp.append(" (?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setObject(1,value.get(IsmsImovinaVo.ISMS_IMOVINA__VLASNIK));
			cs.setObject(2,value.get("novi_vlasnik"));
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
    public void daoStoreKategoriju(IsmsImovinaVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("update isms_imovina set id_kategorije = ? where id_kategorije = ? ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1, value.get("novi_id_kategorije"));
	        pstmt.setObject(2, value.get(IsmsImovinaVo.ISMS_IMOVINA__ID_KATEGORIJE));
	        pstmt.executeUpdate();
	        pstmt.close();
	    } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}