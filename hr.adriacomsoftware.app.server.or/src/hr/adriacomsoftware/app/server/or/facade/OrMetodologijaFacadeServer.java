package hr.adriacomsoftware.app.server.or.facade;

import hr.adriacomsoftware.app.common.or.dto.OrKategorijaDogadajaRs;
import hr.adriacomsoftware.app.common.or.dto.OrKategorijaDogadajaVo;
import hr.adriacomsoftware.app.common.or.dto.OrKategorijaGubitkaRs;
import hr.adriacomsoftware.app.common.or.dto.OrKategorijaGubitkaVo;
import hr.adriacomsoftware.app.common.or.dto.OrKontniPlanRs;
import hr.adriacomsoftware.app.common.or.dto.OrKontniPlanVo;
import hr.adriacomsoftware.app.common.or.dto.OrKontrolaRs;
import hr.adriacomsoftware.app.common.or.dto.OrKontrolaVo;
import hr.adriacomsoftware.app.common.or.dto.OrOcjenaRs;
import hr.adriacomsoftware.app.common.or.dto.OrOcjenaVo;
import hr.adriacomsoftware.app.common.or.dto.OrPoslovnaFunkcijaRs;
import hr.adriacomsoftware.app.common.or.dto.OrPoslovnaFunkcijaVo;
import hr.adriacomsoftware.app.common.or.dto.OrPoslovnaLinijaRs;
import hr.adriacomsoftware.app.common.or.dto.OrPoslovnaLinijaVo;
import hr.adriacomsoftware.app.common.or.dto.OrPrijetnjaRs;
import hr.adriacomsoftware.app.common.or.dto.OrPrijetnjaVo;
import hr.adriacomsoftware.app.common.or.dto.OrUzrokDogadajaRs;
import hr.adriacomsoftware.app.common.or.dto.OrUzrokDogadajaVo;
import hr.adriacomsoftware.app.common.or.facade.OrMetodologijaFacade;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrKategorijaDogadajaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrKategorijaGubitkaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrKontniPlanJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrKontrolaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrMetodologijaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrOcjenaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrPoslovnaFunkcijaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrPoslovnaLinijaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrPrijetnjaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrUzrokDogadajaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class OrMetodologijaFacadeServer extends AS2FacadeServerLayer
		implements OrMetodologijaFacade {

	private static OrMetodologijaFacadeServer _instance = null;

	public static OrMetodologijaFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new OrMetodologijaFacadeServer();
		}
		return _instance;
	}

	private OrMetodologijaFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	public AS2RecordList listajSveProizvode()  {
		OrMetodologijaJdbc dao = new OrMetodologijaJdbc();
		return dao.daoFindListu(OrMetodologijaJdbc.SQL_PROIZVOD.toString(), null);
	}
	public AS2RecordList listajSvePoslovneLinije()  {
		OrMetodologijaJdbc dao = new OrMetodologijaJdbc();
		return dao.daoFindListu(OrMetodologijaJdbc.SQL_OR_LINIJE.toString(), null);
	}
	public AS2RecordList listajSvePoslovneFunkcije()  {
		OrMetodologijaJdbc dao = new OrMetodologijaJdbc();
		return dao.daoFindListu(OrMetodologijaJdbc.SQL_OR_FUNKCIJE.toString(), null);
	}
	public OrPoslovnaLinijaRs procitajSvePoslovneLinije(OrPoslovnaLinijaVo value)
			 {
		OrPoslovnaLinijaJdbc dao = new OrPoslovnaLinijaJdbc();
		OrPoslovnaLinijaRs rs = new OrPoslovnaLinijaRs(dao.daoFind(value));
		return rs;
	}

	public OrPoslovnaLinijaVo azurirajPoslovnuLiniju(OrPoslovnaLinijaVo value)
			 {
		OrPoslovnaLinijaJdbc dao = new OrPoslovnaLinijaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrPoslovnaLinijaVo dodajPoslovnuLiniju(OrPoslovnaLinijaVo value)
			 {
		OrPoslovnaLinijaJdbc dao = new OrPoslovnaLinijaJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrPoslovnaLinijaVo brisiPoslovnuLiniju(OrPoslovnaLinijaVo value)
			 {
		OrPoslovnaLinijaJdbc dao = new OrPoslovnaLinijaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public OrKategorijaDogadajaRs procitajSveKategorijaDogadaja(
			AS2Record value)  {
		OrKategorijaDogadajaJdbc dao = new OrKategorijaDogadajaJdbc();
		return new OrKategorijaDogadajaRs(dao.daoFind(value));
	}
	public OrKategorijaDogadajaRs procitajSveKategorijaDogadaja(
			OrKategorijaDogadajaVo value)  {
		OrKategorijaDogadajaJdbc dao = new OrKategorijaDogadajaJdbc();
		return new OrKategorijaDogadajaRs(dao.daoFind(value));
	}

	public OrKategorijaDogadajaVo azurirajKategorijaDogadaja(
			OrKategorijaDogadajaVo value)  {
		OrKategorijaDogadajaJdbc dao = new OrKategorijaDogadajaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrKategorijaDogadajaVo dodajKategorijaDogadaja(
			OrKategorijaDogadajaVo value)  {
		OrKategorijaDogadajaJdbc dao = new OrKategorijaDogadajaJdbc();
		dao.daoCreate(value);
		return value;
	}
	public AS2Record dodajKategorijaDogadaja(
			AS2Record value)  {
		OrKategorijaDogadajaJdbc dao = new OrKategorijaDogadajaJdbc();
		dao.daoCreate(value);
		return value;
	}
	public OrKategorijaDogadajaVo brisiKategorijaDogadaja(
			OrKategorijaDogadajaVo value)  {
		OrKategorijaDogadajaJdbc dao = new OrKategorijaDogadajaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public AS2RecordList listajSveKategorijaDogadaja()  {
		OrKategorijaDogadajaJdbc dao = new OrKategorijaDogadajaJdbc();
		return dao.daoFindListu(OrKategorijaDogadajaJdbc.sql.toString());
	}

	public OrKategorijaDogadajaRs pretraziKategorijaDogadaja(OrKategorijaDogadajaVo value) 
			 {
		return null;
	}

	public AS2RecordList listajSveKategorijaGubitka()  {
		OrKategorijaGubitkaJdbc dao = new OrKategorijaGubitkaJdbc();
		return dao.daoFindListu(OrKategorijaGubitkaJdbc.sql.toString());
	}

	public OrKategorijaGubitkaRs pretraziKategorijaGubitka(
			OrKategorijaGubitkaVo value)  {

		return null;
	}

	public OrKategorijaGubitkaRs procitajSveKategorijaGubitka(OrKategorijaGubitkaVo value)  {
		OrKategorijaGubitkaJdbc dao = new OrKategorijaGubitkaJdbc();
		return new OrKategorijaGubitkaRs(dao.daoFind(value));
	}

	public OrKategorijaGubitkaVo azurirajKategorijaGubitka(OrKategorijaGubitkaVo value)  {
		OrKategorijaGubitkaJdbc dao = new OrKategorijaGubitkaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrKategorijaGubitkaVo dodajKategorijaGubitka(
			OrKategorijaGubitkaVo value)  {
		OrKategorijaGubitkaJdbc dao = new OrKategorijaGubitkaJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrKategorijaGubitkaVo brisiKategorijaGubitka(
			OrKategorijaGubitkaVo value)  {
		OrKategorijaGubitkaJdbc dao = new OrKategorijaGubitkaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public AS2RecordList listajSveKontrola() {

		return null;
	}

	public OrKontrolaRs pretraziKontrola(OrKontrolaVo value)
			 {

		return null;
	}

	public OrKontrolaRs procitajSveKontrola(OrKontrolaVo value)
			 {

		return null;
	}

	public OrKontrolaVo azurirajKontrola(OrKontrolaVo value)
			 {
		OrKontrolaJdbc dao = new OrKontrolaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrKontrolaVo dodajKontrola(OrKontrolaVo value)  {
		OrKontrolaJdbc dao = new OrKontrolaJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrKontrolaVo brisiKontrola(OrKontrolaVo value)  {
		OrKontrolaJdbc dao = new OrKontrolaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public AS2RecordList listajSveOcjena(OrOcjenaVo value)  {
		OrMetodologijaJdbc dao = new OrMetodologijaJdbc();
		AS2RecordList rs = new AS2RecordList();
		if(value.getVrsta().equals(OrOcjenaVo.OCJENA__IZNOS))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA_IZNOS.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__UCINAK))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA_UCINAK.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__UCESTALOST))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA_UCESTALOST.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__VISINA_RIZIKA))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA_VISINA_RIZIKA.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__STATUS_OPERATIVNOG_RIZIKA))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA_STATUS_OPERATIVNOG_RIZIKA.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__STATUS_DOGADAJA))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA_STATUS_DOGADAJA.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__VISINA_GUBITKA))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA_VISINA_GUBITKA.toString(),null);
		//eksternalizacija
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__EKST_EKONOM_FINANCIJSKA))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA__EKST_EKONOM_FINANCIJSKA.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__EKST_ZAPOSLENICI))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA__EKST_ZAPOSLENICI.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__EKST_RIZIK))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA__EKST_RIZIK.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__EKST_USLUGA))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA__EKST_USLUGA.toString(),null);
		else if(value.getVrsta().equals(OrOcjenaVo.OCJENA__EKST_UGOVOR))
			rs = dao.daoFindListu(OrMetodologijaJdbc.SQL_OCJENA__EKST_UGOVOR.toString(),null);
        return rs;
	}

	public OrOcjenaRs pretraziOcjena(OrOcjenaVo value)  {

		return null;
	}

	public OrOcjenaRs procitajSveOcjena(OrOcjenaVo value)  {
		OrOcjenaJdbc dao = new OrOcjenaJdbc();
		return new OrOcjenaRs(dao.daoFind(value));
	}

	public OrOcjenaVo azurirajOcjena(OrOcjenaVo value)  {
		OrOcjenaJdbc dao = new OrOcjenaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrOcjenaVo dodajOcjena(OrOcjenaVo value)  {
		OrOcjenaJdbc dao = new OrOcjenaJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrOcjenaVo brisiOcjena(OrOcjenaVo value)  {
		OrOcjenaJdbc dao = new OrOcjenaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public AS2RecordList listajSveUzrokDogadaja()  {
		OrUzrokDogadajaJdbc dao = new OrUzrokDogadajaJdbc();
		return dao.daoFindListu(OrUzrokDogadajaJdbc.sql.toString());
	}

	public AS2RecordList listajSvePrijetnja()  {
		OrPrijetnjaJdbc dao = new OrPrijetnjaJdbc();
		return dao.daoFindListu(OrPrijetnjaJdbc.sql.toString());
	}

	public OrPrijetnjaRs pretraziPrijetnja(OrPrijetnjaVo value)	 {
		OrPrijetnjaJdbc dao = new OrPrijetnjaJdbc();
		return new OrPrijetnjaRs(dao.daoFind(value));
	}

	public OrPrijetnjaRs procitajSvePrijetnja(OrPrijetnjaVo value) {
		OrPrijetnjaJdbc dao = new OrPrijetnjaJdbc();
		return new OrPrijetnjaRs(dao.daoFind(value));
	}

	public OrPrijetnjaVo azurirajPrijetnja(OrPrijetnjaVo value)	 {
		OrPrijetnjaJdbc dao = new OrPrijetnjaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrPrijetnjaVo dodajPrijetnja(OrPrijetnjaVo value) {
		OrPrijetnjaJdbc dao = new OrPrijetnjaJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrPrijetnjaVo brisiPrijetnja(OrPrijetnjaVo value) {
		OrPrijetnjaJdbc dao = new OrPrijetnjaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public OrUzrokDogadajaRs pretraziUzrokDogadaja(OrUzrokDogadajaVo value)	 {
		OrUzrokDogadajaJdbc dao = new OrUzrokDogadajaJdbc();
		return new OrUzrokDogadajaRs(dao.daoFind(value));
	}

	public OrUzrokDogadajaRs procitajSveUzrokDogadaja(OrUzrokDogadajaVo value)	 {
		OrUzrokDogadajaJdbc dao = new OrUzrokDogadajaJdbc();
		return new OrUzrokDogadajaRs(dao.daoFind(value));
	}

	public OrUzrokDogadajaVo azurirajUzrokDogadaja(OrUzrokDogadajaVo value)	 {
		OrUzrokDogadajaJdbc dao = new OrUzrokDogadajaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrUzrokDogadajaVo dodajUzrokDogadaja(OrUzrokDogadajaVo value)	 {
		OrUzrokDogadajaJdbc dao = new OrUzrokDogadajaJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrUzrokDogadajaVo brisiUzrokDogadaja(OrUzrokDogadajaVo value)	 {
		OrUzrokDogadajaJdbc dao = new OrUzrokDogadajaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public OrPoslovnaFunkcijaRs procitajSvePoslovneFunkcije(OrPoslovnaFunkcijaVo value)  {
		OrPoslovnaFunkcijaJdbc dao = new OrPoslovnaFunkcijaJdbc();
		return new OrPoslovnaFunkcijaRs(dao.daoFind(value));
	}

	public OrPoslovnaFunkcijaVo azurirajPoslovnuFunkciju(OrPoslovnaFunkcijaVo value)  {
		OrPoslovnaFunkcijaJdbc dao = new OrPoslovnaFunkcijaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrPoslovnaFunkcijaVo dodajPoslovnuFunkciju(OrPoslovnaFunkcijaVo value)  {
		OrPoslovnaFunkcijaJdbc dao = new OrPoslovnaFunkcijaJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrPoslovnaFunkcijaVo brisiPoslovnuFunkciju(OrPoslovnaFunkcijaVo value)  {
		OrPoslovnaFunkcijaJdbc dao = new OrPoslovnaFunkcijaJdbc();
		dao.daoRemove(value);
		return value;
	}

/************* FACADE SERVER  or_kontni_plan ************/

public OrKontniPlanVo brisiOrKontniPlan(OrKontniPlanVo value)  {
	OrKontniPlanJdbc dao = new OrKontniPlanJdbc();
	dao.daoRemove(value);
	return value;
}
public OrKontniPlanVo dodajOrKontniPlan(OrKontniPlanVo value)  {
	OrKontniPlanJdbc dao = new OrKontniPlanJdbc();
	AS2Record res = dao.daoCreate(value);
	return new OrKontniPlanVo(res);
}
public OrKontniPlanVo azurirajOrKontniPlan(OrKontniPlanVo value)  {
	OrKontniPlanJdbc dao = new OrKontniPlanJdbc();
	AS2Record res = dao.daoStore(value);
	return new OrKontniPlanVo(res);
}
public OrKontniPlanRs procitajSveOrKontniPlan(OrKontniPlanVo value)  {
	OrKontniPlanJdbc dao = new OrKontniPlanJdbc();
	OrKontniPlanRs rs =  new OrKontniPlanRs(dao.daoFind(value));
	return rs;
}
public AS2RecordList listajSveOrKontniPlan()  {
	OrKontniPlanJdbc dao = new OrKontniPlanJdbc();
	return dao.daoFindListuOrderBy(OrKontniPlanJdbc.sql.toString(),"id");
}
public OrKontniPlanRs pretraziOrKontniPlan(OrKontniPlanVo value)  {
	OrKontniPlanJdbc dao = new OrKontniPlanJdbc();
	return new OrKontniPlanRs(dao.daoFind(value));
}

}