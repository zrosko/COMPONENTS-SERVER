package hr.adriacomsoftware.app.server.rizik.rezervacije.facade;

import hr.adriacomsoftware.app.common.rizik.rezervacije.dto.RezervacijaRs;
import hr.adriacomsoftware.app.common.rizik.rezervacije.dto.RezervacijaVo;
import hr.adriacomsoftware.app.common.rizik.rezervacije.facade.RezervacijeFacade;
import hr.adriacomsoftware.app.server.rizik.rezervacije.da.jdbc.RezervacijeJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class RezervacijeFacadeServer extends AS2FacadeServerLayer
		implements RezervacijeFacade {

	private static RezervacijeFacadeServer _instance = null;

	public static RezervacijeFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new RezervacijeFacadeServer();
		}
		return _instance;
	}

	private RezervacijeFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	public RezervacijaRs procitajRezervacije(RezervacijaVo value)  {
		RezervacijeJdbc dao = new RezervacijeJdbc();
		return dao.daoListaRezervacija(value);
	}

	public RezervacijaRs pronadiRezervacije(RezervacijaVo value)  {
		RezervacijeJdbc dao = new RezervacijeJdbc();
		return dao.daoListaRezervacija(value);
	}

}
