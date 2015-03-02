package hr.adriacomsoftware.app.server.naplata.da.jdbc;

import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciPracenjeRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrTekuciPracenjeVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.TESTNaplataJdbc;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;

/************* DAO_JDBC  naplata_gr_tekuci_pracenje ************/

public final class NaplataGrTekuciPracenjeJdbc extends TESTNaplataJdbc { 
	public NaplataGrTekuciPracenjeJdbc () {
		setTableName("naplata_gr_tekuci_pracenje");
	}
	
	public NaplataGrTekuciPracenjeVo daoLoad(NaplataGrTekuciPracenjeVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * FROM view_naplata_gr_tekuci_pracenje_sve ");
		sql.append(" where id_pracenja = ? " );
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getIdPracenja());
			pstmt.setMaxRows(0);
			AS2RecordList loc_rs = transformResultSetOneRow(pstmt.executeQuery());
			NaplataGrTekuciPracenjeVo j2eevo = new NaplataGrTekuciPracenjeVo(loc_rs);
			pstmt.close();
			return j2eevo;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public NaplataGrTekuciPracenjeRs daoFindAll(NaplataGrTekuciPracenjeVo value) {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * ");
		sql.append(" FROM view_naplata_gr_tekuci_pracenje ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrTekuciPracenjeRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public NaplataGrTekuciPracenjeRs daoFindAllArhiva(NaplataGrTekuciPracenjeVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * ");
		sql.append(" FROM view_naplata_gr_tekuci_pracenje_arhiva ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrTekuciPracenjeRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public NaplataGrTekuciPracenjeRs daoFindPostojece(NaplataGrTekuciPracenjeVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select * FROM view_naplata_gr_tekuci_pracenje_sve ");
		
		sql.appIn("AND", "broj_partije", value.get("broj_partije"));
		sql.appIn("AND", "jmbg", value.get("jmbg"));
		sql.appIn("AND", "oib", value.get("oib"));
		sql.appLike("AND", "naziv", value.get("naziv"));
		sql.appIn("AND", "case status_partije when 'Prva opomena' then 'Prva' " +
				 "when 'Druga opomena' then 'Druga' " +
				 "when 'Treća opomena' then 'Treca' " +
				 "when 'Dozvoljeno prekoračenje' then 'Dozvoljeno' " +
				 "when 'Pozitivni saldo' then 'Pozitivni' "+
				 "when 'Nedozvoljeno prekoračenje' " +
				 "then 'Nedozvoljeno' " +
				 "else status_partije  end", value.get("status_partije_cbox"));
		
		sql.appIn("AND", "isnull(blokirana_partija,'NE')", value.get("blokada_cbox"));
		sql.appNotIn("AND", "id_pracenja", value.get("@iskljucene"));
		
		sql.appGreatherOrEqualNoQuote("AND", "saldo_zadanog_datuma", value.get("saldo_od"));
		sql.appLessOrEqualNoQuote("AND", "saldo_zadanog_datuma", value.get("saldo_do"));
		sql.appGreatherOrEqualNoQuote("AND", "moguci_kredit", value.get("moguci_od"));
		sql.appLessOrEqualNoQuote("AND", "moguci_kredit", value.get("moguci_do"));
		sql.appGreatherOrEqualNoQuote("AND", "dozvoljeno_prekoracenje", value.get("dozvoljeno_od"));
		sql.appLessOrEqualNoQuote("AND", "dozvoljeno_prekoracenje", value.get("dozvoljeno_do"));
		sql.appGreatherOrEqualNoQuote("AND", "nedozvoljeno_prekoracenje", value.get("nedozvoljeno_od"));
		sql.appLessOrEqualNoQuote("AND", "nedozvoljeno_prekoracenje", value.get("nedozvoljeno_do"));
		
		sql.appLike("AND", "status_pracenja", value.get("status_pracenja"));
		
		
		if(!value.get("arhiva_cbox").equals("'arhiva_da'"))
			sql.appEqual("AND","isnull(ispravno,1)","1");
		
		if(value.get("promet_cbox").equals("PrometDa")){
			sql.append(" and datediff(dd,'"+value.get("datum_od")+"','"+value.get("datum_do")+"') < broj_dana_zadnje_uplate ");
		}
		
		if(!value.get("order_by_pretrazivanje").equals(""))
			sql.append(" order by " + value.get("order_by_pretrazivanje")); 
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrTekuciPracenjeRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public NaplataGrTekuciPracenjeRs daoFindNovo(NaplataGrTekuciPracenjeVo value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append(" select cast(r.broj_partije as bigint) as broj_partije, " +
				   " RIGHT('0'+cast(cast(jmbg as bigint) as varchar),13) as jmbg, " +
				   " cast(oib as bigint) as oib, " +
				   " ime_prezime,saldo_zadanog_datuma,moguci_kredit, " +
				   " dozvoljeno_prekoracenje,nedozvoljeno_prekoracenje,status_partije,vrsta_prekoracenja ");
		if(value.get("promet_cbox").equals("PrometDa")){
			sql.append(",iznos_permanentni_nalog,iznos_prijenos,iznos_uplata");
		}
		sql.append(" from naplata_gr_tekuci r ");
		if(value.get("promet_cbox").equals("PrometDa")){
			sql.append( "left join( "+
						"select pt.broj_partije, sum(pt.duguje_potrazuje*pt.iznos_valuta*(-1)) as iznos_permanentni_nalog "+
						"from   bi_prod.dbo.bi_gr_promet_tekuci pt  "+
						"where  broj_racuna in (1000,1010,1500) and duguje_potrazuje=-1  "+
						"and vrsta_prometa in (351/*509,5*/) and datum_prometa>='"+value.get("datum_od")+"' "+
						"group by pt.broj_partije "+
				")perm on perm.broj_partije=r.broj_partije "+
				"left join( "+
						"select pt.broj_partije, sum(pt.duguje_potrazuje*pt.iznos_valuta*(-1)) as iznos_prijenos "+
						"from   bi_prod.dbo.bi_gr_promet_tekuci pt  "+
						"where  broj_racuna in (1000,1010,1500) and duguje_potrazuje=-1  "+
						"and vrsta_prometa in (509) and datum_prometa>='"+value.get("datum_od")+"' "+
						"group by pt.broj_partije "+
				")prijenos on prijenos.broj_partije=r.broj_partije "+
				"left join( "+
						"select pt.broj_partije, sum(pt.duguje_potrazuje*pt.iznos_valuta*(-1)) as iznos_uplata "+
						"from   bi_prod.dbo.bi_gr_promet_tekuci pt  "+
						"where  broj_racuna in (1000,1010,1500) and duguje_potrazuje=-1  "+
						"and vrsta_prometa in (5) and datum_prometa>='"+value.get("datum_od")+"' "+
						"group by pt.broj_partije "+
				")uplata on uplata.broj_partije=r.broj_partije ");
		}
		sql.append(" where r.broj_partije not in (select broj_partije from dbo.naplata_gr_tekuci_pracenje where ISNULL(ispravno,1) = 1 ) " +
				   " and datum_zatvaranja is null ");
		sql.appendWhere();
		sql.appPrefix();
		sql.appIn("AND", "case r.status_partije when 'Prva opomena' then 'Prva' " +
						 "when 'Druga opomena' then 'Druga' " +
						 "when 'Treća opomena' then 'Treca' " +
						 "when 'Dozvoljeno prekoračenje' then 'Dozvoljeno' " +
						 "when 'Pozitivni saldo' then 'Pozitivni' "+
						 "when 'Nedozvoljeno prekoračenje' " +
						 "then 'Nedozvoljeno' end", value.get("status_partije_cbox"));
		
		if(value.get("promet_cbox").equals("PrometDa")){
			sql.append(" and datediff(dd,'"+value.get("datum_od")+"','"+value.get("datum_do")+"') < broj_dana_zadnje_uplate ");
		}
		sql.appGreatherOrEqualNoQuote("AND", "r.saldo_zadanog_datuma", value.get("saldo_od"));
		sql.appLessOrEqualNoQuote("AND", "r.saldo_zadanog_datuma", value.get("saldo_do"));
		sql.appGreatherOrEqualNoQuote("AND", "r.moguci_kredit", value.get("moguci_od"));
		sql.appLessOrEqualNoQuote("AND", "r.moguci_kredit", value.get("moguci_do"));
		sql.appGreatherOrEqualNoQuote("AND", "r.dozvoljeno_prekoracenje", value.get("dozvoljeno_od"));
		sql.appLessOrEqualNoQuote("AND", "r.dozvoljeno_prekoracenje", value.get("dozvoljeno_do"));
		sql.appGreatherOrEqualNoQuote("AND", "r.nedozvoljeno_prekoracenje", value.get("nedozvoljeno_od"));
		sql.appLessOrEqualNoQuote("AND", "r.nedozvoljeno_prekoracenje", value.get("nedozvoljeno_do"));
		sql.appIn("AND", "isnull(r.blokirana_partija,'NE')", value.get("blokada_cbox"));
		sql.appIn("AND", "r.broj_partije", value.get("broj_partije"));
		sql.appIn("AND", "r.jmbg", value.get("jmbg"));
		sql.appIn("AND", "r.oib", value.get("oib"));
		sql.appLike("AND", "r.ime_prezime", value.get("naziv"));
		if(!value.get("order_by").equals(""))
			sql.append(" order by " + value.get("order_by"));
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setMaxRows(0);
			AS2RecordList j2eers = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return new NaplataGrTekuciPracenjeRs(j2eers);
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public void daoRemove(AS2Record value)  {
		J2EESqlBuilder sql = new J2EESqlBuilder();
		sql.append("UPDATE naplata_gr_tekuci_pracenje SET ispravno = 0 " +
				   "WHERE id_pracenja = ? ");
		try{
			PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
			pstmt.setObject(1,value.getProperty("id_pracenja"));
			pstmt.setMaxRows(0);
	        pstmt.executeUpdate();
	        pstmt.close();
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}
