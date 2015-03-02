package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.facade;

import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.BankaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.BankaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.facade.OsnovniPodaciFacade;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahBankaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class OsnovniPodaciFacadeServer extends AS2FacadeServerLayer 
		implements OsnovniPodaciFacade {

	private static OsnovniPodaciFacadeServer _instance = null;
	public static OsnovniPodaciFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new OsnovniPodaciFacadeServer();
		}
		return _instance;
	}
	private OsnovniPodaciFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public BankaVo azurirajOsnovnePodatke(BankaVo value)  {
        PoZahBankaJdbc dao = new PoZahBankaJdbc();
		dao.daoStore(value);
		return value;
    }
    public BankaRs procitajSveOsnovnePodatke(BankaVo value)  {
        PoZahBankaJdbc dao = new PoZahBankaJdbc();
        BankaRs rs = new BankaRs(dao.daoFind(value));
		return rs;
    }
    public BankaVo dodajOsnovnePodatke(BankaVo value)  {
        PoZahBankaJdbc dao = new PoZahBankaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public BankaVo brisiOsnovnePodatke(BankaVo value)  {
        PoZahBankaJdbc dao = new PoZahBankaJdbc();
		dao.daoRemove(value);
		return value;
    }    
}
