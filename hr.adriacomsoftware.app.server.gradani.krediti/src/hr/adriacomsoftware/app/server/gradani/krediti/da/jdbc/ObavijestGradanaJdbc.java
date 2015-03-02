package hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.J2EEDataDictionary;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.ObavijestRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.ObavijestVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class ObavijestGradanaJdbc extends BankarskiJdbc {
	public ObavijestGradanaJdbc() {
		setTableName("bi_gr_krediti_izvjestaj_pocetak_otplate");
	}

	public ObavijestRs daoListaObradaObavijesti(ObavijestVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT CONVERT(char(10), datum_pocetak, 104) AS datum_od, ");
		sql.appendln("CONVERT(char(10), datum_kraj, 104) AS datum_do, ");
		sql.appendln("count(*) as ukupno_obavijesti ");
		sql.appendln("FROM bi_view_gr_krediti_pocetak_otplate ");
		/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.append(" GROUP BY datum_pocetak, datum_kraj ");
		sql.append(" ORDER BY datum_pocetak DESC,datum_kraj DESC ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			ObavijestRs j2eers = new ObavijestRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public ObavijestRs daoListaObavijesti(ObavijestVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();

		if (value.getAkcija().equals("Priprema")) {
			daoSpGrKreditiObavijesti("bi_gr_krediti_pocetak_otplate", value);
			return new ObavijestRs();
		} else {
			sql.appendln("select * from bi_view_gr_krediti_pocetak_otplate ");
			sql.appendWhere();
			sql.appPrefix();
			sql.append(" WHERE datum_pocetak >= ? AND datum_kraj <= ? ");
			sql.appEqual("AND", "broj_partije", value.getBrojPartije());
			// sql.appEqual("AND", "vrsta_obavijesti",
			// value.getVrstaObavijesti());
			sql.appLike("AND", "ime_prezime", value.getImePrezime());
			sql.appEqual("AND", "jmbg", value.getJmbg());
			if (value.getIskljucenePartije().length() > 0)
				sql.appendln("AND (broj_partije NOT IN ("
						+ value.getIskljucenePartije() + "))");
			/* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
			sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
			/* ograničavanje za sigurnosne razine KRAJ */
			sql.append(" ORDER BY ime_prezime, broj_partije"); // vrsta_obavijesti
		}
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			pstmt.setMaxRows(0);
			ObavijestRs j2eers = new ObavijestRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	private void daoSpGrKreditiObavijesti(String sp_name,ObavijestVo input_value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, input_value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
			cs.setDate(2, input_value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
			cs.setString(3, input_value.getIskljucenePartije());
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoIskljuciUkljuciObavijesti(ObavijestVo value,	String ukljuciIskljuci)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.getIskljucenePartije().length() > 0) {
			sql.appendln("UPDATE bi_gr_krediti_izvjestaj_pocetak_otplate ");
			sql.append("SET ispravno = ");
			sql.append(ukljuciIskljuci);
			sql.append(" WHERE broj_partije IN (");
			sql.append(value.getIskljucenePartije());
			sql.append(") AND datum_pocetak = ? AND datum_kraj = ?");
			try{
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setDate(1, value.getAsSqlDate(J2EEDataDictionary.DATUM_OD));
				pstmt.setDate(2, value.getAsSqlDate(J2EEDataDictionary.DATUM_DO));
				pstmt.setMaxRows(0);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (Exception e) {
				throw new AS2DataAccessException(e);
			}
		}
	}
}