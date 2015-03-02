package hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc;

import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljOcjenaRs;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljOcjenaVo;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.RIZIKJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;
import java.sql.Types;

public final class PokazateljOcjenaJdbc extends RIZIKJdbc {
    public PokazateljOcjenaJdbc() {
        setTableName("kr_pokazatelj_ocjena_log");
    }
    public PokazateljOcjenaRs daoFindZaPokazatelj(PokazateljVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("SELECT * FROM kr_pokazatelj_ocjena_log  ");
        sql.append(" where pokazatelj = ? ");
        sql.append(" ORDER BY ocjena ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getPokazatelj());
	        pstmt.setMaxRows(0);
	        PokazateljOcjenaRs j2eers = new PokazateljOcjenaRs(transformResultSet(pstmt.executeQuery()));
	        pstmt.close();
	        return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public void daoStoreVrijednosti(PokazateljOcjenaVo value)    {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.append("update kr_pokazatelj_ocjena_log set vrijednost_od = ?, vrijednost_do = ? ");
        sql.append("WHERE pokazatelj_ocjena = ? ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        if(value.getVrijednostOd().length()>0)
	            pstmt.setObject(1, value.getVrijednostOd());
	        else
	            pstmt.setNull(1,Types.DECIMAL);
	        if(value.getVrijednostDo().length()>0)
	            pstmt.setObject(2, value.getVrijednostDo());
	        else
	            pstmt.setNull(2,Types.DECIMAL);
	        pstmt.setObject(3, value.getPokazateljOcjena());
	        int updateCounter = pstmt.executeUpdate();
	        pstmt.close();
	        if (updateCounter != 1)
	            throw new AS2Exception(" 162");
        } catch (AS2Exception e) {
        	throw e;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}