package hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc;

import hr.adriacomsoftware.app.common.rizik.rejting.dto.RejtingRs;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.RejtingVo;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.SkoringVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.RIZIKJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class RejtingJdbc extends RIZIKJdbc {
    
    public RejtingJdbc() {
        setTableName("kr_entitet_rejting");
    }
    public RejtingRs daoListaRejtingaPravnihOsoba(RejtingVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        if(value.get("@izbor_a").equals("A"))
            sql.appendln("SELECT * FROM view_kr_rejting_pogled_log  ");
        else
            sql.appendln("SELECT * FROM view_kr_rejting_pogled  ");
        sql.appLike("AND", "naziv", value.getImePrezime()); //ime prezime znaci naziv
        sql.appEqualNoQuote("AND", "maticni_broj", value.getJmbg()); //jmbg se korisni u modelu a znaci MB
        sql.appEqualNoQuote("AND", "oib", value.getOib());
        sql.append(" ORDER BY naziv ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        RejtingRs j2eers = new RejtingRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoAzurirajRejtingPravneOsobe(SkoringVo value)  {        
    	J2EESqlBuilder sp = new J2EESqlBuilder(); 
		sp.append("{call ");
		sp.append(" stp_po_rejting_azuriranje ");
		sp.append(" (?,?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1,value.getAsSqlDate(SkoringVo.KR_SKORING__DATUM));
			cs.setObject(2, value.getMaticniBroj());
			cs.setObject(3, value.getOib());
			cs.setObject(4, value.getIdSkoringa());
			cs.setObject(5, value.getPokazatelj());
			cs.setObject(6, value.getOcjena());
			cs.setObject(7, value.getVrijednostPokazatelja());
			cs.setObject(8, value.getIzmjenaKrozAplikaciju());
			cs.execute();
			cs.close(); 
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}