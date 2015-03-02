package hr.adriacomsoftware.app.server.isms.da.jdbc;

import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaKorisnikVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;

import java.sql.PreparedStatement;


public final class IsmsImovinaKorisnikJdbc extends CMDBJdbc {
    public IsmsImovinaKorisnikJdbc() {
        setTableName("isms_imovina_korisnik");
    }
    public AS2RecordList daoFind(AS2Record aFields) {
		String sql = "select id_procesa, organizacijska_jedinica,"+
		"id_korisnika, id_imovine FROM "+
		getTableName()+" where id_imovine = ? ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,aFields.get(IsmsImovinaKorisnikVo.ISMS_IMOVINA_KORISNIK__ID_IMOVINE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}