package hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.OpomenaRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.OpomenaVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class OpomenaGradanaJdbc extends J2EEDataAccessObjectJdbc {
	public OpomenaGradanaJdbc() {
		setTableName("bi_gr_krediti_izvjestaj_opomene");
	}

	public OpomenaRs daoListaObradaOpomena(OpomenaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("SELECT CONVERT(char(10), datum_obrade, 104) AS datum_obrade, ");
		sql.appendln("CONVERT(char(10), zadani_datum, 104) AS zadani_datum, ");
		sql.appendln("count(*) as ukupno_opomena ");
		sql.appendln("FROM bi_view_gr_krediti_opomene ");
		sql.appendln("WHERE (vrsta_opomene = 'D')  ");
		sql.append(" GROUP BY datum_obrade,zadani_datum ");
		sql.append(" ORDER BY datum_obrade DESC,zadani_datum DESC ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OpomenaRs j2eers = new OpomenaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OpomenaRs daoListaOpomena(OpomenaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.getAkcija().equals("Priprema")) {
			daoSpGrKreditiOpomene("bi_gr_krediti_opomene", value);
			return new OpomenaRs();
		} else {
			sql.appendln("select * from bi_view_gr_krediti_opomene ");
			sql.appGreatherOrEqual(null, "broj_partije",
					value.getBrojPartijeOd());
			sql.appLessOrEqual("AND", "broj_partije", value.getBrojPartijeDo());
			sql.appGreatherOrEqual(null, "broj_neplacenih_rata",
					value.getBrojRataOd());
			sql.appLessOrEqual("AND", "broj_neplacenih_rata",
					value.getBrojRataDo());
			sql.appEqual("AND", "vrsta_opomene", value.getVrstaOpomene());
			sql.appLike("AND", "ime_prezime", value.getImePrezime());
			sql.appEqual("AND", "jmbg", value.getJmbg());
			sql.appIn("AND", "broj_opomene", value.getBrojOpomene());
			if (value.getIskljucenePartije().length() > 0)
				sql.appendln("AND (broj_partije NOT IN ("
						+ value.getIskljucenePartije() + "))");
			sql.append(" AND zadani_datum = ? ");
			sql.append(" ORDER BY ime_prezime, broj_partije, vrsta_opomene");
		}
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1,value.getAsSqlDate(JBDataDictionary.BI_OPOMENE__ZADANI_DATUM));
			pstmt.setMaxRows(0);
			OpomenaRs j2eers = new OpomenaRs(transformResultSet(pstmt.executeQuery()));			
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	private void daoSpGrKreditiOpomene(String sp_name, OpomenaVo input_value) {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append(sp_name);
		sp.append(" (?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, input_value.getAsSqlDate(JBDataDictionary.BI_OPOMENE__ZADANI_DATUM));
			cs.setString(2, input_value.getIskljucenePartije());
			cs.setInt(3, 0);
			cs.setInt(4, 999);
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoIskljuciUkljuciOpomene(OpomenaVo value, String ukljuciIskljuci)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.getIskljucenePartije().length() > 0) {
			sql.appendln("UPDATE bi_gr_krediti_izvjestaj_opomene ");
			sql.append("SET ispravno = ");
			sql.append(ukljuciIskljuci);
			sql.append(" WHERE broj_partije IN (");
			sql.append(value.getIskljucenePartije());
			sql.append(") AND zadani_datum = ?");
			try{
				PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
				pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_OPOMENE__ZADANI_DATUM));
				pstmt.setMaxRows(0);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (Exception e) {
				throw new AS2DataAccessException(e);
			}
		}
	}
}