package hr.adriacomsoftware.app.server.gradani.tekuci.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.tekuci.dto.CekRs;
import hr.adriacomsoftware.app.common.gradani.tekuci.dto.CekVo;
import hr.adriacomsoftware.app.common.gradani.tekuci.facade.TekuciGradanaFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.server.gradani.tekuci.da.jdbc.CekUnosJdbc;
import hr.adriacomsoftware.app.server.gradani.tekuci.da.jdbc.PartijaTekuciGradanaJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class TekuciGradanaFacadeServer extends AS2FacadeServerLayer
		implements TekuciGradanaFacade {

	private static TekuciGradanaFacadeServer _instance = null;
	public static TekuciGradanaFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new TekuciGradanaFacadeServer();
		}
		return _instance;
	}
	private TekuciGradanaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public OsobaRs pronadiSveOsobe(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
        return dao.daoPronadiOsobe(value,true);
    }
	public OsobaRs procitajSveOsobe()  {
	    PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
        return dao.daoPronadiOsobe(new OsnovniVo(),false);
	 }
    public PartijaRs izvjestajPartijeBezPrometa(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
		return dao.daoListaPartijaBezPrometa(value);
    }
    public PartijaRs izvjestajNedozvoljenoPrekoracenje(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
		return dao.daoListaPartijaNedozvoljeno(value);
    }
    public PartijaRs izvjestajPartijaPoKriteriju(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
		return dao.daoListaPartijaKriterij(value);
    }
    public PartijaRs izvjestajKamateNaknade(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
		return dao.daoKamateNaknade(value);
    }
    public PartijaRs izvjestajTrajniNalozi(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
	    return dao.daoListaTrajnihNaloga(value);
    }
    public PartijaRs izvjestajRocnostNedozvoljenogPrekoracenja(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
        return dao.daoListaRocnostNedozvoljenogPrekoracenjaPregled(value); 
    }
    public PartijaRs izvjestajNaplataRezervacija(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
	    return dao.daoNaplataRezervacija(value);
    }
    public PartijaRs izvjestajCrnaLista(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
	    return dao.daoCrnaLista(value);
    }
    public PartijaRs izvjestajDnevneOpomene(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
	    return dao.daoDnevneOpomene(value);
    }
    public PartijaRs registarTekucihRacuna(OsnovniVo value)  {
        PartijaTekuciGradanaJdbc dao = new PartijaTekuciGradanaJdbc(); 
	    return dao.daoRegistarRacuna(value);
    }
	public CekVo dodajCek(CekVo value)  {
		CekUnosJdbc dao = new CekUnosJdbc();
		AS2User _user = (AS2User)value.getAsObject(AS2Constants.USER_OBJ);
		value.setOperaterUnosa(_user.get("korisnik"));
		value.setVrijemeUnosa(AS2Date.getTodayAsCalendar());
		value.set("akcija", "I"); //insert
		dao.daoValidacija(value);
		dao.daoCreate(value);
		return value;
	}
	public CekVo azurirajCek(CekVo value)  {
		CekUnosJdbc dao = new CekUnosJdbc();
		value.set("akcija", "U"); //update
		dao.daoValidacija(value);
		dao.daoStore(value);
		return value;
	}
	public CekVo brisiCek(CekVo value)  {
		CekUnosJdbc dao = new CekUnosJdbc();
		dao.daoRemove(value);
		return value;
	}
	public CekRs procitajSveCekove(CekVo value)  {
		CekUnosJdbc dao = new CekUnosJdbc();
		return dao.daoFind(value);
	}
	public CekVo brisiViseCekova(CekRs value)  {
		CekUnosJdbc dao = new CekUnosJdbc();
		dao.daoRemoveMany(value);
		return new CekVo();
	}
	public CekRs procitajSveNeiskoristeCekoveZaPartiju(CekVo value)  {
		CekUnosJdbc dao = new CekUnosJdbc();
		return dao.daoFindNeiskoristeni(value);
	}
}
