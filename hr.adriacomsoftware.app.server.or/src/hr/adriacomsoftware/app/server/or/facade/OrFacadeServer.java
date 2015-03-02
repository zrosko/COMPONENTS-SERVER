package hr.adriacomsoftware.app.server.or.facade;

import hr.adriacomsoftware.app.common.or.dto.OrDogadajRs;
import hr.adriacomsoftware.app.common.or.dto.OrDogadajVo;
import hr.adriacomsoftware.app.common.or.dto.OrGubitakRs;
import hr.adriacomsoftware.app.common.or.dto.OrGubitakVo;
import hr.adriacomsoftware.app.common.or.dto.OrNadoknadaRs;
import hr.adriacomsoftware.app.common.or.dto.OrNadoknadaVo;
import hr.adriacomsoftware.app.common.or.dto.OrPoduzetaMjeraRs;
import hr.adriacomsoftware.app.common.or.dto.OrPoduzetaMjeraVo;
import hr.adriacomsoftware.app.common.or.facade.OrFacade;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrDogadajJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrGubitakJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrNadoknadaJdbc;
import hr.adriacomsoftware.app.server.or.da.jdbc.OrPoduzetaMjeraJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class OrFacadeServer extends AS2FacadeServerLayer
		implements OrFacade {

	private static OrFacadeServer _instance = null; 

	public static OrFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new OrFacadeServer();
		}
		return _instance;
	}

	private OrFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	public OrDogadajRs pronadiDogadaje(OrDogadajVo value)  {
		OrDogadajJdbc dao = new OrDogadajJdbc();
		OrDogadajRs rs = new OrDogadajRs(dao.daoPronadiDogadaje(value));
		return rs;
	}

	public OrDogadajRs procitajSveDogadaje(OrDogadajVo value)
			 {
		OrDogadajJdbc dao = new OrDogadajJdbc();
		OrDogadajRs rs = new OrDogadajRs(dao.daoListaDogadaja(value));
		return rs;
	}

	public OrDogadajVo azurirajDogadaj(OrDogadajVo value)  {		
		AS2User _user = (AS2User) value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterPromjene(_user.get("korisnik"));
		value.setVrijemePromjene(AS2Date.getTodayAsCalendar());		
		OrDogadajJdbc dao = new OrDogadajJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrDogadajVo dodajDogadaj(OrDogadajVo value)  {
		AS2User _user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterUnosa(_user.get("korisnik"));
		value.setVrijemeUnosa(AS2Date.getTodayAsCalendar());
		value.setOrganizacijskaJedinicaUnosa(_user.get("org_jedinica"));
		OrDogadajJdbc dao = new OrDogadajJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrDogadajVo brisiDogadaj(OrDogadajVo value)  {
		OrDogadajJdbc dao = new OrDogadajJdbc();
		dao.daoRemove(value);
		return value;
	}

	public OrGubitakRs pronadiGubitake(OrGubitakVo value)  {
		OrGubitakJdbc dao = new OrGubitakJdbc();
		OrGubitakRs rs = new OrGubitakRs(dao.daoFind(value));
		return rs;
	}

	public OrGubitakRs procitajSveGubitake(OrGubitakVo value)  {
		OrGubitakJdbc dao = new OrGubitakJdbc();
		return dao.daoListaGubitaka(value);
	}

	public OrGubitakVo azurirajGubitak(OrGubitakVo value)  {
		OrGubitakJdbc dao = new OrGubitakJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrGubitakVo dodajGubitak(OrGubitakVo value)  {
		OrGubitakJdbc dao = new OrGubitakJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrGubitakVo brisiGubitak(OrGubitakVo value)  {
		OrGubitakJdbc dao = new OrGubitakJdbc();
		dao.daoRemove(value);
		return value;
	}

	public OrNadoknadaRs pronadiNadoknada(OrNadoknadaVo value)
			 {
		OrNadoknadaJdbc dao = new OrNadoknadaJdbc();
		OrNadoknadaRs rs = new OrNadoknadaRs(dao.daoFind(value));
		return rs;
	}

	public OrNadoknadaRs procitajSveNadoknada(OrNadoknadaVo value)
			 {
		OrNadoknadaJdbc dao = new OrNadoknadaJdbc();
		OrNadoknadaRs rs = new OrNadoknadaRs(dao.daoListaNadoknada(value));
		return rs;
	}

	public OrNadoknadaVo azurirajNadoknada(OrNadoknadaVo value)
			 {
		OrNadoknadaJdbc dao = new OrNadoknadaJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrNadoknadaVo dodajNadoknada(OrNadoknadaVo value)
			 {
		OrNadoknadaJdbc dao = new OrNadoknadaJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrNadoknadaVo brisiNadoknada(OrNadoknadaVo value)
			 {
		OrNadoknadaJdbc dao = new OrNadoknadaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public OrPoduzetaMjeraRs pronadiPoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	public OrPoduzetaMjeraRs procitajSvePoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		OrPoduzetaMjeraJdbc dao = new OrPoduzetaMjeraJdbc();
		OrPoduzetaMjeraRs rs = new OrPoduzetaMjeraRs(dao.daoListaPodzetihMjera(value));
		return rs;
	}

	public OrPoduzetaMjeraVo azurirajPoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		OrPoduzetaMjeraJdbc dao = new OrPoduzetaMjeraJdbc();
		dao.daoStore(value);
		return value;
	}

	public OrPoduzetaMjeraVo dodajPoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		OrPoduzetaMjeraJdbc dao = new OrPoduzetaMjeraJdbc();
		dao.daoCreate(value);
		return value;
	}

	public OrPoduzetaMjeraVo brisiPoduzetaMjera(OrPoduzetaMjeraVo value)
			 {
		OrPoduzetaMjeraJdbc dao = new OrPoduzetaMjeraJdbc();
		dao.daoRemove(value);
		return value;
	}
}
