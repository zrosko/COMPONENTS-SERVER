package hr.adriacomsoftware.app.server.or.eksternalizacija.facade;

import hr.adriacomsoftware.app.common.or.eksternalizacija.dto.OrEksternalizacijaRs;
import hr.adriacomsoftware.app.common.or.eksternalizacija.dto.OrEksternalizacijaVo;
import hr.adriacomsoftware.app.common.or.eksternalizacija.facade.OrEksternalizacijaFacade;
import hr.adriacomsoftware.app.server.or.eksternalizacija.da.jdbc.OrEksternalizacijaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class OrEksternalizacijaFacadeServer extends AS2FacadeServerLayer
		implements OrEksternalizacijaFacade {

	private static OrEksternalizacijaFacadeServer _instance = null;

	public static OrEksternalizacijaFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new OrEksternalizacijaFacadeServer();
		}
		return _instance;
	}

	private OrEksternalizacijaFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	public OrEksternalizacijaVo brisiOrEksternalizacija(
			OrEksternalizacijaVo value)  {
		OrEksternalizacijaJdbc dao = new OrEksternalizacijaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public OrEksternalizacijaVo dodajOrEksternalizacija(
			OrEksternalizacijaVo value)  {
		OrEksternalizacijaJdbc dao = new OrEksternalizacijaJdbc();
		AS2Record res = dao.daoCreate(value);
		return new OrEksternalizacijaVo(res);
	}

	public OrEksternalizacijaVo azurirajOrEksternalizacija(
			OrEksternalizacijaVo value)  {
		OrEksternalizacijaJdbc dao = new OrEksternalizacijaJdbc();
		AS2Record res = dao.daoStore(value);
		return new OrEksternalizacijaVo(res);
	}

	public OrEksternalizacijaRs procitajSveOrEksternalizacija(
			OrEksternalizacijaVo value)  {
		OrEksternalizacijaJdbc dao = new OrEksternalizacijaJdbc();
		return new OrEksternalizacijaRs(dao.daoListaEksternalizacija(value));
	}

	public AS2RecordList listajSveOrEksternalizacija()  {
		//OrEksternalizacijaJdbc dao = new OrEksternalizacijaJdbc();
		// return dao.daoFindListu(OrEksternalizacijaJdbc.SQL_LISTA);
		return new AS2RecordList();
	}

	public OrEksternalizacijaRs pretraziOrEksternalizacija(
			OrEksternalizacijaVo value)  {
		OrEksternalizacijaJdbc dao = new OrEksternalizacijaJdbc();
		return new OrEksternalizacijaRs(dao.daoListaEksternalizacija(value));
	}
}
