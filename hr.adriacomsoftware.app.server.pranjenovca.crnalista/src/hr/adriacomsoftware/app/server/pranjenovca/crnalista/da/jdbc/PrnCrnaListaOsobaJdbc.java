package hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaOsobaVo;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaRs;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaVo;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnFizickaOsobaRs;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.sql.PreparedStatement;


public final class PrnCrnaListaOsobaJdbc extends BankarskiJdbc {

	public PrnCrnaListaOsobaJdbc() {
		setTableName("prn_crna_lista_osoba");
	}

	public void daoSpremiOsobu(CrnaListaOsobaVo value)  {
		daoCreate(value);
		PrnCrnaListaAdresaJdbc dao_adrese = new PrnCrnaListaAdresaJdbc();
		dao_adrese.daoCreateMany(value.getAdrese());
		PrnCrnaListaDokumentJdbc dao_dokument = new PrnCrnaListaDokumentJdbc();
		dao_dokument.daoCreateMany(value.getDokumente());
		PrnCrnaListaOsobaAliasJdbc dao_alias = new PrnCrnaListaOsobaAliasJdbc();
		dao_alias.daoCreateMany(value.getAliase());
	}

	public CrnaListaRs daoFindOsoba(CrnaListaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_crna_lista_osoba ");
		AS2User _user = AS2SessionFactory.getInstance().getCurrentUser();
		// administrator ima role_id broj 1 i samo on moze gledati listu 5 -
		// Lista klijenata Ureda (USPN)
		if (!_user.get("role_id").equals("1")) {
			sql.append(" WHERE id_liste not in(5) ");
		}
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			CrnaListaRs j2eers = new CrnaListaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public CrnaListaRs daoSearchOsoba(CrnaListaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from view_prn_crna_lista_osoba ");
		if (value.get("prezime").length() > 0
				&& value.get("id_osobe").length() <= 0) {
			sql.append(" WHERE id_osobe in (select id_osobe from dbo.fn_tbl_prn_crna_lista_osobe_pretrazivanje('");
			sql.append(value.get("prezime"));
			sql.append("'))");
		} else if (value.get("id_osobe").length() > 0) {
			sql.appEqual("AND", "id_osobe", value.get("id_osobe"));
		} else {
			return new CrnaListaRs();
		}
		AS2User _user = AS2SessionFactory.getInstance().getCurrentUser();
		// administrator ima role_id broj 1 i samo on moze gledati listu 5 -
		// Lista klijenata Ureda (USPN)
		if (!_user.get("role_id").equals("1")) {
			sql.append(" AND id_liste not in(5) ");
		}
		sql.append(" order by prezime ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(200);
			CrnaListaRs j2eers = new CrnaListaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoZadnjiBrojOsobe(CrnaListaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT 'u'+convert(varchar(15),max(convert(int,"
				+ "substring(id_osobe,2,len(id_osobe)))+1)) as max_id_osobe "
				+ "FROM view_prn_crna_lista_osoba " + "WHERE id_liste = 5 ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers.get("max_id_osobe");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public String daoId(CrnaListaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("SELECT id " + "FROM view_prn_crna_lista_osoba "
				+ "WHERE id_liste = " + value.get("id_liste")
				+ " AND id_osobe ='" + value.get("id_osobe") + "' ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			return j2eers.get("id");
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoListajAdreseOsobe(CrnaListaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select CMDB_PROD.dbo.fn_scl_isms_imovina_povjerljivost(482) as isms_povjerljivost, A.* from prn_crna_lista_adresa A ");
		sql.appEqual("AND", "id_osobe", value.get("id_osobe"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(200);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoListajDokumenteOsobe(CrnaListaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select CMDB_PROD.dbo.fn_scl_isms_imovina_povjerljivost(482) as isms_povjerljivost, D.* from prn_crna_lista_dokument D ");
		sql.appEqual("AND", "id_osobe", value.get("id_osobe"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(200);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnFizickaOsobaRs daoDubinskaAnalizaOsobe(OsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbl_prn_gr_procjena_rizika_stranke(?,?,?) ");
		sql.append("order by jmbg_mb,redni_broj, vrsta_ocjena, ocjena");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			String ime_prezime = value.get("ime") + " " + value.get("prezime");
			pstmt.setObject(1, ime_prezime);
			pstmt.setObject(2, value.getJmbg());
			pstmt.setObject(3, value.getOib());
			pstmt.setMaxRows(0);
			PrnFizickaOsobaRs j2eers = new PrnFizickaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnPravnaOsobaRs daoDubinskaAnalizaPravneOsobe(PravnaOsobaVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbl_prn_po_procjena_rizika_stranke(?,?,?) ");
		sql.append("order by jmbg_mb,redni_broj, vrsta_ocjena, ocjena");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.getNaziv());
			pstmt.setObject(2, value.getMaticniBroj());
			pstmt.setObject(3, value.getOib());
			pstmt.setMaxRows(0);
			PrnPravnaOsobaRs j2eers = new PrnPravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnPravnaOsobaRs daoDubinskaAnalizaSvihPravnihOsoba(PravnaOsobaVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbl_prn_po_procjena_rizika_stranke_batch(?,?) ");
		sql.append("order by jmbg_mb,redni_broj, vrsta_ocjena, ocjena");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("kategorija_rizika"));
			pstmt.setObject(2, value.get("prikazi_detalje"));
			pstmt.setMaxRows(0);
			PrnPravnaOsobaRs j2eers = new PrnPravnaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public PrnFizickaOsobaRs daoDubinskaAnalizaSvihOsoba(OsobaVo value)	 {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from fn_tbl_prn_gr_procjena_rizika_stranke_batch(?,?) ");
		sql.append("order by jmbg_mb,redni_broj, vrsta_ocjena, ocjena");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("kategorija_rizika"));
			pstmt.setObject(2, value.getProperty("prikazi_detalje"));
			pstmt.setMaxRows(0);
			PrnFizickaOsobaRs j2eers = new PrnFizickaOsobaRs(transformResultSet(pstmt.executeQuery()));
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
}