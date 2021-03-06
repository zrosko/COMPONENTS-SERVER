package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentOtplatniPlanRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentOtplatniPlanVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class NaplataKlijentOtplatniPlanJdbc extends OLTPJdbc {
	public NaplataKlijentOtplatniPlanJdbc() {
		setTableName("naplata_klijent_otplatni_plan");
	}

	public NaplataKlijentOtplatniPlanRs daoLoad(NaplataKlijentOtplatniPlanVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * ");
		sql.append(" from view_naplata_klijent_otplatni_plan ");
		sql.append(" where id_biljeske = " + value.getIdBiljeske() + " ");
		sql.append(" order by datum_uplate_planirani");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataKlijentOtplatniPlanRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE naplata_klijent_otplatni_plan SET ispravno = 0 "
				+ "WHERE id_plana = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getProperty("id_plana"));
			pstmt.setMaxRows(0);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoIzradiOtplatniPlan(NaplataKlijentOtplatniPlanVo value)	 {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_gr_naplata_otplatni_plan");
		sp.append(" (?,?,?,?,?,?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, value.getAsSqlDate("datum_nagodbe"));
			cs.setDate(2, value.getAsSqlDate("datum_uplate"));
			cs.setObject(3, value.get("stanje_na_dan_nagodbe"));
			cs.setObject(4, value.get("iznos_rate"));
			cs.setObject(5, value.get("broj_rata"));
			cs.setObject(6, value.get("periodika"));
			cs.setObject(7, value.get("vrijeme_unosa_izmjene"));
			cs.setObject(8, value.get("referent"));
			cs.setObject(9, value.get("id_biljeske"));
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}