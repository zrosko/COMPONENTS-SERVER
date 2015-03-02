package hr.adriacomsoftware.app.server.pravneosobe.krediti.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.OpomenaRs;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.dto.OpomenaVo;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectJdbc;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;

public final class OpomenaPravnihOsobaJdbc extends J2EEDataAccessObjectJdbc {
	private static boolean program_radi = false;

	public OpomenaPravnihOsobaJdbc() {
		setTableName("bi_po_krediti_izvjestaj_opomene");
	}

	public OpomenaRs daoListaObradaOpomena(OpomenaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.appendln("select CONVERT(char(10), datum_obrade, 104) AS datum_obrade, ");
		sql.appendln("CONVERT(char(10), zadani_datum, 104) AS zadani_datum, ");
		sql.appendln("count(*) as ukupno_opomena, datum_obrade as datum_obrade_, ");
		sql.appendln("zadani_datum as zadani_datum_  ");
		sql.appendln("from bi_view_po_krediti_opomene where vrsta_opomene not in ('O')");
		sql.append(" GROUP BY CONVERT(char(10), datum_obrade, 104),datum_obrade,zadani_datum ");
		sql.append(" ORDER BY datum_obrade_ DESC,zadani_datum_ DESC ");
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
			if (program_radi || RocnostKreditaPravihOsobaJdbc.program_radi)						
				throw new AS2Exception("10010");
			program_radi = true;
			daoSpPoKreditiOpomene(value);
			program_radi = false;
			return new OpomenaRs();
		} else {
			sql.appendln("select * from bi_view_po_krediti_opomene ");
			sql.appIn("IN", "vrsta_opomene", value.getVrstaOpomene());
			sql.appLike("AND", "naziv", value.getNazivPravneOsobe());
			sql.appEqual("AND", "maticni_broj", value.getMaticniBroj());
			sql.appEqual("AND", "broj_partije", value.getBrojPartije());

			if (value.getIskljucenePartije().length() > 0)
				sql.appendln("AND (broj_partije NOT IN ("
						+ value.getIskljucenePartije() + "))");
			sql.append(" AND zadani_datum = ? ");
			sql.append(" ORDER BY vrsta_opomene,naziv,broj_partije");
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

	public void daoIskljuciUkljuciOpomene(OpomenaVo value,String ukljuciIskljuci)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.getIskljucenePartije().length() > 0) {
			sql.appendln("UPDATE bi_po_krediti_izvjestaj_opomene ");
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

	private int daoSpPoKreditiOpomene(OpomenaVo input_value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("bi_po_krediti_opomene ");
		sp.append(" (?,?,?,?) }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.setDate(1, input_value.getAsSqlDate(JBDataDictionary.BI_OPOMENE__ZADANI_DATUM));
			cs.setString(2, input_value	.get(OpomenaVo.BI_OPOMENA_VO__PONAVLJANJE_OBRADE));
			cs.setString(3, input_value.getIskljucenePartije());
			cs.registerOutParameter(4, java.sql.Types.INTEGER);
			cs.execute();
			int status = cs.getInt(4);
			cs.close();
			if (status == 100)
				throw new AS2DataAccessException("10011");
			else if (status == 200)
				throw new AS2DataAccessException("10012");
			return status;
		} catch (AS2DataAccessException e) {
			throw e;
		} catch (Exception e) {
			AS2DataAccessException ex = new AS2DataAccessException("151");
			ex.addCauseException(e);
			throw ex;
		}
	}
}