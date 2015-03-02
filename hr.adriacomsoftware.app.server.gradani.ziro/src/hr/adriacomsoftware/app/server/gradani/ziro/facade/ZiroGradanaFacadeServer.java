package hr.adriacomsoftware.app.server.gradani.ziro.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.ziro.facade.ZiroGradanaFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.server.gradani.ziro.da.jdbc.PartijaZiroGradanaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class ZiroGradanaFacadeServer extends AS2FacadeServerLayer
		implements ZiroGradanaFacade {

	private static ZiroGradanaFacadeServer _instance = null;
	public static ZiroGradanaFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new ZiroGradanaFacadeServer();
		}
		return _instance;
	}
	private ZiroGradanaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	public OsobaRs procitajSveOsobe()  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
        return dao.daoPronadiOsobe(new OsnovniVo(), false);
	 }
    public OsobaRs pronadiSveOsobe(OsnovniVo value)  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
        return dao.daoPronadiOsobe(value,true);
    }
    public PartijaRs izvjestajKamateNaknade(OsnovniVo value)  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
		return dao.daoKamateNaknade(value);
    }
    public PartijaRs izvjestajPartijaPoKriteriju(OsnovniVo value)  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
		return dao.daoListaPartijaKriterij(value);
    }
    public PartijaRs izvjestajPartijeBezPrometa(OsnovniVo value)  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
		return dao.daoListaPartijaBezPrometa(value);
    }
    public PartijaRs izvjestajZiroSaPrometom(OsnovniVo value)  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
		return dao.daoZiroSaPrometom(value);
    }
    public PartijaRs izvjestajZiroKamatneStope(OsnovniVo value)  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
		return dao.daoZiroKamatneStope(value);
    }
    public PartijaRs izvjestajZiroBlokirani(OsnovniVo value)  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
		return dao.daoZiroBlokirani(value);
    }
    public PartijaRs registarZiroRacuna(OsnovniVo value)  {
        PartijaZiroGradanaJdbc dao = new PartijaZiroGradanaJdbc(); 
	    return dao.daoRegistarRacuna(value);
    }
}
