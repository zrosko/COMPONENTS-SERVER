package hr.adriacomsoftware.app.server.ovrha.da.jdbc;

import hr.adriacomsoftware.app.common.ovrha.dto.OvrhaRs;
import hr.adriacomsoftware.app.common.ovrha.dto.OvrhaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.OLTPJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;


public final class OvrhaJdbc extends OLTPJdbc {

	public OvrhaJdbc() {
		setTableName("pons_ovrha");
	}

	public OvrhaRs daoLoadAll(OvrhaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT * FROM view_pons_ovrhe_pogled ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OvrhaRs j2eers = new OvrhaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoZadnjiRedniBroj(OvrhaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select isnull(max(redni_broj),0)+1 as max_redni_broj "
				+ "from pons_ovrha where korisnik like ? and tip like ? "
				+ "and (ISNULL(ispravno, 1) = 1)");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("korisnik"));
			pstmt.setObject(2, value.get("tip"));
			pstmt.setMaxRows(1);
			OvrhaVo j2eers = new OvrhaVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers.get("max_redni_broj");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoZadnjiIdOvrhe(OvrhaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select isnull(max(id_ovrhe),0) as max_id_ovrhe "
				+ "from pons_ovrha where redni_broj = ? "
				+ "and (ISNULL(ispravno, 1) = 1) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("redni_broj"));
			pstmt.setMaxRows(1);
			OvrhaVo j2eers = new OvrhaVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers.get("max_id_ovrhe");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoBrojPaketa(OvrhaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.getTip().equals("osnova")) {
			sql.append(" select isnull((select top(1) broj_paketa from pons_ovrha "
					+ " where jmbg = ? and isnull(ispravno,1)=1 and tip='osnova'), "
					+ " (select max(broj_paketa)+1 from pons_ovrha "
					+ " where isnull(ispravno,1)=1))as max_broj_paketa");
		} else if (value.getTip().equals("dodatna")) {
			sql.append(" select top(1) broj_paketa as max_broj_paketa from pons_ovrha "
					+ " where isnull(ispravno,1)=1 and tip='osnova' and broj_paketa is not null "
					+ " and broj_osnove = ? ");
		}
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			if (value.getTip().equals("osnova"))
				pstmt.setObject(1, value.getJmbg());
			else if (value.getTip().equals("dodatna"))
				pstmt.setObject(1, value.getBrojOsnove());
			pstmt.setMaxRows(1);
			OvrhaVo j2eers = new OvrhaVo(transformResultSetOneRow(pstmt.executeQuery()));
			pstmt.close();
			return j2eers.get("max_broj_paketa");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OvrhaRs daoExportExcel(OvrhaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("exec stp_pons_excel_export @tip = '" + value.get("@tip")
				+ "' ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OvrhaRs j2eers = new OvrhaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OvrhaRs daoPronadiOvrhe(OvrhaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_pons_ovrhe_pogled ");
		sql.appEqualNoQuote("AND", "broj_paketa", value.getBrojPaketa());
		sql.appEqualNoQuote("AND", "redni_broj", value.getRedniBroj());
		sql.appEqual("AND", "jmbg", value.getJmbg());
		sql.appEqual("OR", "oib", value.getOib());
		sql.appEqual("AND", "racun_ovrsenika", value.getRacunOvrsenika());
		sql.appEqual("AND", "racun_ovrsenika", value.getRacunOvrsenika());
		sql.appLike("AND", "broj_osnove", value.getBrojOsnove());
		sql.appLike("AND", "korisnik_naziv", value.get("korisnik_naziv"));
		sql.appIn("AND", "tip", value.getTip());
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			// System.out.println(sql);
			OvrhaRs j2eers = new OvrhaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OvrhaRs daoPronadiOvrheIzvjestaj(OvrhaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		if (value.getBrojPaketa().equals(""))
			return new OvrhaRs();
		sql.append("select * from view_pons_ovrhe_izvjestaj ");
		sql.appEqualNoQuote("AND", "broj_paketa", value.getBrojPaketa());
		sql.append(" order by broj_paketa ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OvrhaRs j2eers = new OvrhaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public OvrhaRs daoPronadiOvrheIzvjestajPrilog2(OvrhaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" declare @a int set @a = 1 "
				+ " declare @aaaa varchar(2000) set @aaaa = ' select 1 as a ' "
				+ " while  @a<" + value.get("broj_konta") + " " + " begin "
				+ " set @aaaa = @aaaa+' union all select 1 as a ' "
				+ " set @a=@a+1 " + " end " + " exec(@aaaa) ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			OvrhaRs j2eers = new OvrhaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public void daoStpIzracunajBrojPaketa(OvrhaVo value)  {
		J2EESqlBuilder sp = new J2EESqlBuilder();
		sp.append("{call ");
		sp.append("stp_pons_dodjela_broja_paketa }");
		try{
			CallableStatement cs = getConnection().getCallableStatement(sp.toString());
			cs.execute();
			cs.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}