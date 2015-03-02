package hr.adriacomsoftware.app.server.helpdesk.da.jdbc;

import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class HdDopusteniZahtjevJdbc extends CMDBJdbc {
	public HdDopusteniZahtjevJdbc() {
		setTableName("hd_dopusteni_zahtjevi");
	}

	public AS2RecordList daoDopusteniZahtjevi(HelpDeskPozivVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select rtrim(dj.ime)+' '+rtrim(dj.prezime) as ime_prezime, dj.jmbg , dz.id_stavke "
				+ "from hd_dopusteni_zahtjevi as dz left outer join "
				+ "cmdb_djelatnik as dj on dz.dopustena_osoba=dj.jmbg "
				+ "where dz.id_zahtjeva=" + value.get("broj_") + " ");
		sql.appendln("ORDER BY ime_prezime ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public boolean daoFindIfExistsDopusteniZahtjev(HelpDeskPozivVo value)  {
		String sql = "SELECT 1 FROM hd_dopusteni_zahtjevi "
				+ "WHERE aplikacija like '" + value.get("aplikacija") + "' "
				+ "AND id_zahtjeva = " + value.get("id_zahtjeva") + " "
				+ "AND dopustena_osoba = " + value.get("dopustena_osoba") + " ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			int broj_slogova = j2eers.size();
			if (broj_slogova > 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoBrisiDopusteniZahtjevi(HelpDeskPozivVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("DELETE FROM hd_dopusteni_zahtjevi "
				+ "WHERE aplikacija like '" + value.get("aplikacija") + "' "
				+ "AND id_zahtjeva = " + value.get("id_zahtjeva") + " "
				+ "AND dopustena_osoba = " + value.get("dopustena_osoba") + " ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}