package hr.adriacomsoftware.app.server.karticno.da.jdbc;

import hr.adriacomsoftware.app.common.karticno.dto.McardSifrarnikRs;
import hr.adriacomsoftware.app.common.karticno.dto.McardSifrarnikVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class McardGrSifrarnikJdbc extends OLTPJdbc {
	public McardGrSifrarnikJdbc () {
		setTableName("mcard_gr_sifarnik");
	}
    public McardSifrarnikRs daoFindSifrarnik(McardSifrarnikVo value, boolean pretrazivanje) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * from view_mcard_gr_sifarnik ");
        /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
        sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
        /*ograničavanje za sigurnosne razine KRAJ*/
		sql.append(" order by vrsta, rb ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			McardSifrarnikRs j2eers = new McardSifrarnikRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}