package hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc;

import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.RIZIKJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

public final class TipEntitetaPokazateljJdbc extends RIZIKJdbc {
    
    public TipEntitetaPokazateljJdbc() {
        setTableName("kr_tip_entiteta_pokazatelj_log");
    }
    public void daoRemoveTipPokazatelja(PokazateljVo value) {
        J2EESqlBuilder sql = new J2EESqlBuilder(); 
        sql.appendln("DELETE FROM kr_tip_entiteta_pokazatelj_log  ");
        sql.append(" where pokazatelj = ? and vrijedi_od = ? and vrijedi_do = ? ");
        try{
	        PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
	        pstmt.setObject(1,value.getPokazatelj());
	        pstmt.setDate(2,value.getAsSqlDate(PokazateljVo.KR_POKAZATELJ__VRIJEDI_OD));
	        pstmt.setDate(3,value.getAsSqlDate(PokazateljVo.KR_POKAZATELJ__VRIJEDI_DO));
	        pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}