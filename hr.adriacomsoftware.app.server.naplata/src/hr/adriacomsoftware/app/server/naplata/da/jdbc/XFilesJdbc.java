package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataVo;
import hr.adriacomsoftware.app.server.sudskisporovi.da.jdbc.OdvjetnikJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class XFilesJdbc extends OdvjetnikJdbc {
    
    public XFilesJdbc() {
        setTableName("XFiles");
    }
    public NaplataRs daoFind(NaplataVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT F.NasZnak, F.Naziv, " +
        		   "RTRIM(isnull(C.LastName,'') + ' ' + isnull(C.FirstName,'')) AS stranka, " +
        		   "case when F.Active = 1 then 'Aktivan' " +
        		   "when F.Active = 0 then 'Zatvoren' end AS status, " +
        		   "F.CreatedBy as spis_kreirao, F.FileID " +
        		   "FROM XFiles  as F left outer join XContacts as C " +
        		   "on F.ContactID=C.ContactID where F.NasZnak like '"+value.get("j2ee_aplication")+"%' ");
        sql.appendWhere();
        sql.appPrefix();
        sql.appLike("AND", "jmbg", value.getJmbg()); 
        sql.appEqual("AND", "F.Active", value.getStatus());
        sql.appLike("AND", "F.NasZnak", value.getNasZnak());
        sql.appLike("AND", "F.Naziv", value.getNaziv()); 
        sql.appLike("AND", "rtrim(C.LastName+' '+C.FirstName)", value.getStranka());
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        NaplataRs j2eers = new NaplataRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers; 
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public NaplataRs daoFindAll(NaplataVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("SELECT F.NasZnak, F.Naziv, " +
        		   "RTRIM(isnull(C.LastName,'') + ' ' + isnull(C.FirstName,'')) AS stranka, " +
        		   "case when F.Active = 1 then 'Aktivan' " +
        		   "when F.Active = 0 then 'Zatvoren' end AS status, " +
        		   "F.CreatedBy as spis_kreirao, F.FileID " +
        		   "FROM XFiles  as F left outer join XContacts as C " +
        		   "on F.ContactID=C.ContactID where F.NasZnak like '"+value.get("j2ee_aplication")+"%' ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setMaxRows(0);
	        NaplataRs j2eers = new NaplataRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers; 
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    //izvještaj spisa
    //select * from bi_prod.dbo.fn_tbs_odvjetnik_ispis_spisa() where fileid=2705
    public NaplataRs daoLoad(NaplataVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("select * from bi_prod.dbo.fn_tbs_odvjetnik_ispis_spisa() where fileid=?");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getFileID());
	        pstmt.setMaxRows(0);
	        NaplataRs j2eers = new NaplataRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers; 
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}