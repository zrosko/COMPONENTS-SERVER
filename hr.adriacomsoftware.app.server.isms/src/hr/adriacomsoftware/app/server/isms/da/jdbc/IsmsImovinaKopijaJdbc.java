package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaKopijaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class IsmsImovinaKopijaJdbc extends CMDBJdbc {
    public IsmsImovinaKopijaJdbc() {
        setTableName("isms_imovina_kopija");
    }
    public AS2RecordList daoFind(AS2Record aFields) {
		String sql = "select CONVERT(char(10), datum_kopiranja,104) as datum_kopiranja_,organizacijska_jedinica,"+
		"id_kopije, datum_kopiranja, id_lokacije, convert(decimal(18,0),jmbg) as jmbg,id_imovine FROM "+
		getTableName()+" where id_imovine = ? ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,aFields.get(IsmsImovinaKopijaVo.ISMS_IMOVINA_KOPIJA__ID_IMOVINE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}