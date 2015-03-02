package hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc;

import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaAdresaOsobeVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class PrnCrnaListaAdresaJdbc extends BankarskiJdbc {

	public PrnCrnaListaAdresaJdbc() {
		setTableName("prn_crna_lista_adresa");
	}

	public String daoZadnjiBrojAdrese(CrnaListaAdresaOsobeVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT 'a'+convert(varchar(15),max(convert(int,"
				+ "substring(id_adrese,2,len(id_adrese)))+1)) as max_id_adrese "
				+ "FROM prn_crna_lista_adresa " + "WHERE id_liste = 5 ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers.get("max_id_adrese");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}