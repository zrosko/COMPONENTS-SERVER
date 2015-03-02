package hr.adriacomsoftware.app.server.helpdesk.da.jdbc;

import hr.adriacomsoftware.app.common.helpdesk.dto.HelpDeskPozivVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class HdPozivJdbc extends CMDBJdbc {
	public HdPozivJdbc() {
		setTableName("hd_poziv");
	}

	public AS2RecordList daoFind(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		// zbog sporosti dodano je top 1000
		sql.append("select top 1000 * from dbo.view_hd_poziv_pogled ");
		sql.appendWhere();
		sql.appPrefix();
		sql.append("where broj_ is not null "); // dodano da ne bi status 0
												// preuzeo
		/* Ograničavanje za sigurnosne razine (1,2,3,5) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciDjelatnika(sql);
		sql.append("OR status=0 "); // da svi korisnici mogu vidjeti obavijesti
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY id_poziva DESC");
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

	public AS2Record daoFindLast(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.view_hd_poziv_pogled ");
		sql.appendWhere();
		sql.appPrefix();
		sql.append("where broj_ is not null "); // dodano da ne bi status 0
		/* Ograničavanje za sigurnosne razine (1,2,3,5) POČETAK */
		sql = odrediRazinuOvlastiUkljucujuciDjelatnika(sql);
		sql.append("OR status=0 "); // da svi korisnici mogu vidjeti obavijesti
		/* ograničavanje za sigurnosne razine KRAJ */
		sql.appendln("ORDER BY id_poziva DESC");
		// System.out.println(sql);
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(1);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers.getRowAt(0);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoSearch(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select distinct P.* "
				+ "from view_hd_poziv_pogled as P left outer join "
				+ "(SELECT id_poziva, komentar FROM view_hd_komentar_pogled) as k "
				+ "on P.broj_=k.id_poziva ");
		sql.append("where datum_poziva >= ? and (datum_poziva-1) <= ? ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appLike("AND", "naziv", value.get("naziv"));
		if (value.get("opis_sto").length() > 0) {
			sql.appLike("AND (", "opis_sto", value.get("opis_sto"));
			sql.appLike("OR", "opis_greske", value.get("opis_sto"));
			sql.appLike("OR", "opis_kako", value.get("opis_sto"));
			sql.appLike("OR", "opis_sada", value.get("opis_sto"));
			sql.appLike("OR", "opis_tko", value.get("opis_sto"));
			sql.appLike("OR", "komentar", value.get("opis_sto"));
			sql.appLike("OR", "vas_znak", value.get("opis_sto"));
			sql.appLike("OR", "korisnicko_ime", value.get("opis_sto"));
			sql.appLike("OR", "opis_izvrsenih_promjena", value.get("opis_sto"));
			sql.appLike("OR", "naziv", value.get("opis_sto"));
			sql.append(" ) ");
		}
		if (value.get("nezaduzeno").equals("DA")) {
			if (value.get("and_or").equals("and")) {
				sql.appLike("AND", "ime_prezime", value.get("ime_prezime"));
				sql.append(" AND ISNULL(osoba_rasporeda,0) = 0 ");
			} else { // and_or='or'
				if (value.get("ime_prezime").length() > 0) {
					sql.appLike("AND", "(ime_prezime", value.get("ime_prezime"));
					sql.append(" OR ISNULL(osoba_rasporeda,0) = 0 )");

				} else
					sql.append(" AND ISNULL(osoba_rasporeda,0) = 0 ");
			}
		} else { // nezaduzeno='NE'
			if (value.get("and_or").equals("and")) {
				sql.appLike("AND", "ime_prezime", value.get("ime_prezime"));
				sql.appLike("AND", "osoba_rasporeda_",
						value.get("osoba_rasporeda_"));
			} else { // and_or='or'
				if (value.get("ime_prezime").length() > 0) {
					sql.appLike("AND", "(ime_prezime", value.get("ime_prezime"));
					sql.appLike("OR", "osoba_rasporeda_",
							value.get("osoba_rasporeda_"));
					sql.append(")");
				} else
					sql.appLike("AND", "osoba_rasporeda_",
							value.get("osoba_rasporeda_"));
			}
		}
		sql.appLike("AND", "organizacijska_jedinica",
				value.get("organizacijska_jedinica"));
		sql.appIn("AND", "P.id_poziva", value.get("id_poziva"));
		sql.appLike("AND", "P.vas_znak", value.get("vas_znak"));
		if (!value.get("status").equals("0"))
			sql.appEqual("AND", "status", value.get("status"));
		if (!value.get("prioritet").equals("X"))
			sql.appEqual("AND", "prioritet", value.get("prioritet"));
		if (!value.get("vrsta").equals("0") && !value.get("vrsta").equals("A")
				&& !value.get("vrsta").equals("B")
				&& !value.get("vrsta").equals("C"))
			sql.appEqual("AND", "vrsta", value.get("vrsta"));
		else if (value.get("vrsta").equals("A"))
			sql.appLike("AND", "predmet_za", "BANKA");
		else if (value.get("vrsta").equals("B"))
			sql.appLike("AND", "predmet_za", "BANKSOFT");
		else if (value.get("vrsta").equals("C"))
			sql.appLike("AND", "predmet_za", "OSTALO");
		if (!value.get("vrsta").equals("9")) // da svi korisnici mogu vidjeti obavijesti
			sql = odrediRazinuOvlastiUkljucujuciDjelatnika(sql);
		sql.appLike("AND", "build_id", value.get("build_id"));
		if (!value.get("predmet_za").startsWith("SVI"))
			sql.appEqual("AND", "predmet_za", value.get("predmet_za"));
		sql.appendln("ORDER BY id_poziva DESC");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1, value.getAsSqlDate(HelpDeskPozivVo.HD_POZIV__DATUM_POZIVA));
			pstmt.setDate(2, value.getAsSqlDate(HelpDeskPozivVo.HD_POZIV__DATUM_GRESKE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoSearch1(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select broj_,datum_poziva__ as datum_poziva,tip_,zahtjev,opis_,status_,"
				+ "ime_prezime as osoba_prijave,opis_sto_,opis_tko,opis_sada,"
				+ "opis_kako,datum_zatvaranja_,datum_rjesenja_,broj_komentara_,"
				+ "broj_privitaka,datum_poziva as datum_poziva1 ");
		sql.append("from view_hd_poziv_pogled ");
		sql.append("where datum_poziva >= ? and (datum_poziva-1) <= ? ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appLike("AND", "naziv", value.get("naziv"));
		sql.appLike("AND", "opis_sto", value.get("opis_sto"));
		sql.appLike("OR", "opis_greske", value.get("opis_sto"));
		if (value.get("ime_prezime").length() > 0
				&& value.get("osoba_rasporeda_").length() > 0) {
			sql.appLike("AND", "(ime_prezime", value.get("ime_prezime"));
			if (!value.get("nezaduzeno").equals("DA"))
				sql.appLike("OR", "osoba_rasporeda_",
						value.get("osoba_rasporeda_"));
			sql.append(")");
		} else {
			sql.appLike("AND", "ime_prezime", value.get("ime_prezime"));
			if (!value.get("nezaduzeno").equals("DA"))
				sql.appLike("AND", "osoba_rasporeda_",
						value.get("osoba_rasporeda_"));
		}
		sql.appLike("AND", "organizacijska_jedinica",
				value.get("organizacijska_jedinica"));
		sql.appEqual("AND", "id_poziva", value.get("id_poziva"));
		sql.appLike("AND", "vas_znak", value.get("vas_znak"));
		if (!value.get("status").equals("0"))
			sql.appEqual("AND", "status", value.get("status"));
		if (!value.get("prioritet").equals("X"))
			sql.appEqual("AND", "prioritet", value.get("prioritet"));
		if (!value.get("vrsta").equals("0") && !value.get("vrsta").equals("A")
				&& !value.get("vrsta").equals("B")
				&& !value.get("vrsta").equals("C"))
			sql.appEqual("AND", "vrsta", value.get("vrsta"));
		else if (value.get("vrsta").equals("A"))
			sql.appLike("AND", "predmet_za", "BANKA");
		else if (value.get("vrsta").equals("B"))
			sql.appLike("AND", "predmet_za", "BANKSOFT");
		else if (value.get("vrsta").equals("C"))
			sql.appLike("AND", "predmet_za", "OSTALO");
		if (value.get("nezaduzeno").equals("DA"))
			sql.append(" and isnull(osoba_rasporeda,0) = 0 ");
		if (!value.get("vrsta").equals("9")) // da svi korisnici mogu vidjeti obavijesti
			sql = odrediRazinuOvlastiUkljucujuciDjelatnika(sql);
		sql.appLike("AND", "build_id", value.get("build_id"));
		if (!value.get("predmet_za").startsWith("SVI"))
			sql.appEqual("AND", "predmet_za", value.get("predmet_za"));
		sql.appendln("ORDER BY id_poziva DESC");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setDate(1,
					value.getAsSqlDate(HelpDeskPozivVo.HD_POZIV__DATUM_POZIVA));
			pstmt.setDate(2,
					value.getAsSqlDate(HelpDeskPozivVo.HD_POZIV__DATUM_GRESKE));
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return j2eers;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

	public AS2RecordList daoProcitajVlastitePozive(HelpDeskPozivVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("select * from dbo.view_hd_poziv_pogled ");
		if (value.get("@@filter").equals("1"))
			sql.append("where osoba_prijave=" + value.get("@@id_osobe")
					+ " AND status=1 ");
		else if (value.get("@@filter").equals("2"))
			sql.append("where osoba_prijave=" + value.get("@@id_osobe")
					+ " AND status=2 ");
		else if (value.get("@@filter").equals("3"))
			sql.append("where osoba_prijave=" + value.get("@@id_osobe")
					+ " AND status=3 ");
		else if (value.get("@@filter").equals("4"))
			sql.append("where osoba_rasporeda=" + value.get("@@id_osobe")
					+ " AND status=1 ");
		else if (value.get("@@filter").equals("5"))
			sql.append("where osoba_rasporeda=" + value.get("@@id_osobe")
					+ " AND status=2 ");
		else if (value.get("@@filter").equals("6"))
			sql.append("where osoba_rasporeda=" + value.get("@@id_osobe")
					+ " AND status=3 ");
		else
			sql.append("where osoba_rasporeda=" + value.get("@@id_osobe")
					+ " OR osoba_prijave=" + value.get("@@id_osobe") + " ");
		sql.appendln("ORDER BY id_poziva DESC");
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

	public void daoUpdateDateAsDefault(HelpDeskPozivVo value)  {
		String naziv = value.get("datum_naziv");
		String sql = " update hd_poziv " + " set " + naziv + " = DEFAULT "
				+ " where id_poziva = ? ";
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1, value.get("id_poziva"));
			pstmt.setMaxRows(0);
			pstmt.execute();
			pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}

}