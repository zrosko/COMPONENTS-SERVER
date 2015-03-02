package hr.adriacomsoftware.app.server.pravneosobe.platnipromet.facade;

import hr.adriacomsoftware.app.common.jb.dto.IzvjestajRs;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.NaplataMjenicaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.NaplataMjenicaVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.platnipromet.facade.PlatniPrometPravnihOsobaFacade;
import hr.adriacomsoftware.app.server.pravneosobe.platnipromet.da.jdbc.NaplataMjenicaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.platnipromet.da.jdbc.PartijaPlatnogPrometaPravnihOsobaJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class PlatniPrometPravnihOsobaFacadeServer  extends AS2FacadeServerLayer
		implements PlatniPrometPravnihOsobaFacade {
    
    private static PlatniPrometPravnihOsobaFacadeServer _instance = null;

    public static PlatniPrometPravnihOsobaFacadeServer getInstance() {
        if (_instance == null) {
            _instance = new PlatniPrometPravnihOsobaFacadeServer();
        }
        return _instance;
    }

    private PlatniPrometPravnihOsobaFacadeServer() {
    	AS2Context.setSingletonReference(this);
    }

    public PravnaOsobaRs procitajSvePravneOsobePlatniPromet()  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc(); 
        return dao.daoPronadiPravneOsobe(new OsnovniVo(),false);
    }

    public PravnaOsobaRs pronadiSvePravneOsobePlatniPromet(OsnovniVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc(); 
        return dao.daoPronadiPravneOsobe(value,true);
    }

    public IzvjestajRs pregledPrometaPoPartiji(IzvjestajVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return new IzvjestajRs(dao.daoPregledPrometa(value));
    }

    public IzvjestajRs poslovniRacun(IzvjestajVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return new IzvjestajRs(dao.daoPoslovniRacun(value));
    }

    public IzvjestajRs poslovniRacunMjesecno(IzvjestajVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return new IzvjestajRs(dao.daoPoslovniRacunMjesecno(value));
    }

    public OsnovniRs izvjestajZatezneKamate(OsnovniVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return new OsnovniRs(dao.daoZatezneKamate(value));
    }
    public OsnovniRs izvjestajMjesecnaRekapitulacija(OsnovniVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return new OsnovniRs(dao.daoMjesecnaRekapitulacija(value));
    }

    public IzvjestajRs prosjecnoStanjePartijaPoMaticnomBroju(IzvjestajVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return dao.daoDnevnoProsjecnoStanjePoMaticnomBroju(value);
    }

    public IzvjestajRs prosjecnoStanjePartije(IzvjestajVo value)  {
        return null;
    }

    public IzvjestajRs poslovniRacunZaPartiju(IzvjestajVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return new IzvjestajRs(dao.daoPoslovniRacunZaPartiju(value));
    }
    public IzvjestajRs poslovniRacun500000(IzvjestajVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return new IzvjestajRs(dao.daoPoslovniRacun500000(value));
    }

    public IzvjestajRs poslovniRacuniNaknada(IzvjestajVo value)  {
        PartijaPlatnogPrometaPravnihOsobaJdbc dao = new PartijaPlatnogPrometaPravnihOsobaJdbc();
        return new IzvjestajRs(dao.daoPoslovniRacuniNaknada(value));
    }

	public NaplataMjenicaVo dodajMjenicu(NaplataMjenicaVo value)  {
		NaplataMjenicaJdbc dao = new NaplataMjenicaJdbc();
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		dao.daoCreate(value);
		return value;
	}

	public NaplataMjenicaVo azurirajMjenicu(NaplataMjenicaVo value)  {
		NaplataMjenicaJdbc dao = new NaplataMjenicaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent_zadnje_izmjene",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		dao.daoStore(value);
		return value;
	}

	public NaplataMjenicaRs procitajSveMjenice(NaplataMjenicaVo value)  {
		NaplataMjenicaJdbc dao = new NaplataMjenicaJdbc();
		return dao.daoLoad(value,false);
	}

	public NaplataMjenicaRs pronadiMjenice(NaplataMjenicaVo value)  {
		return null;
	}
	public NaplataMjenicaVo brisiMjenicu(NaplataMjenicaVo value)  {
		NaplataMjenicaJdbc dao = new NaplataMjenicaJdbc();
		dao.daoRemove(value);
		return value;
	}

	public NaplataMjenicaVo brisiViseMjenica(NaplataMjenicaRs value)  {
		NaplataMjenicaJdbc dao = new NaplataMjenicaJdbc();
		dao.daoRemoveMany(value);
		return new NaplataMjenicaVo();
	}

	public NaplataMjenicaRs izvjestaji(NaplataMjenicaVo value)  {
		NaplataMjenicaJdbc dao = new NaplataMjenicaJdbc();
		return dao.daoIzvjestaji(value);
	}

 }