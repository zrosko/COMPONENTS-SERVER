package hr.adriacomsoftware.app.server.pranjenovca.po.facade;

import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPoPovezanaOsobaRs;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPoPovezanaOsobaVo;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPoRacunRs;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPoRacunVo;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPravnaOsobaRs;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPravnaOsobaVo;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPrivitakRs;
import hr.adriacomsoftware.app.common.pranjenovca.po.dto.PrnPrivitakVo;
import hr.adriacomsoftware.app.common.pranjenovca.po.facade.PranjeNovcaFacade;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc.PrnCrnaListaOsobaJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.po.da.jdbc.PrnPoPovezanaOsobaJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.po.da.jdbc.PrnPoPrivitakJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.po.da.jdbc.PrnPoRacunJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.po.da.jdbc.PrnPoUpitnikJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PartijaPravnihOsobaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.i18n.AS2DataDictionary;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class PranjeNovcaFacadeServer extends AS2FacadeServerLayer
		implements PranjeNovcaFacade {

	private static PranjeNovcaFacadeServer _instance = null; 
	public static PranjeNovcaFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new PranjeNovcaFacadeServer();
		}
		return _instance;
	}
	private PranjeNovcaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public PravnaOsobaRs procitajSveOsobe(PravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        return dao.daoFindPravnaOsoba(value);
    }
    public PravnaOsobaRs pronadiOsobe(PravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        return dao.daoSearchPravnaOsoba(value);
    }
    public PravnaOsobaRs izvjestajiPravneOsobe(PravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        return dao.daoIzvjestajiPravneOsobe(value);
    }
    public PrnPoPovezanaOsobaVo azurirajPovezanuOsobu(PrnPoPovezanaOsobaVo value)  {
        System.out.println(value);
        PrnPoPovezanaOsobaJdbc dao = new PrnPoPovezanaOsobaJdbc();
		dao.daoStore(value);
		return value;
    }
    public PrnPoPovezanaOsobaRs procitajSvePovezaneOsobe(PrnPoPovezanaOsobaVo value)  {
        PrnPoPovezanaOsobaJdbc dao = new PrnPoPovezanaOsobaJdbc();
        PrnPoPovezanaOsobaRs rs = new PrnPoPovezanaOsobaRs(dao.daoFind(value));
		return rs;
    }    
    public PrnPoPovezanaOsobaVo dodajPovezanuOsobu(PrnPoPovezanaOsobaVo value)  {
        PrnPoPovezanaOsobaJdbc dao = new PrnPoPovezanaOsobaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public PrnPoPovezanaOsobaVo brisiPovezanuOsobu(PrnPoPovezanaOsobaVo value)  {
        PrnPoPovezanaOsobaJdbc dao = new PrnPoPovezanaOsobaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public PrnPoPovezanaOsobaRs procitajSvePovezaneOsobePOVIJEST(PrnPoPovezanaOsobaVo value)  {
        return null;
    }
    public PrnPoRacunVo citajRacun(PrnPoRacunVo value)  {
        PrnPoRacunJdbc dao = new PrnPoRacunJdbc();
        return dao.daoLoad(value);
    }
    public PrnPoRacunVo azurirajRacun(PrnPoRacunVo value)  {
        PrnPoRacunJdbc dao = new PrnPoRacunJdbc();
        if (value.get("gotovinska_transakcija").matches("NE") && value.get("prosjecni_iznos").length()>0) {
            value.set("prosjecni_iznos",0);
            value.set("prosjecni_iznos_valuta",0);
            value.set("gotovinska_transakcija_svrha","");
        }
        if (value.get("prosjecni_iznos").length()==0) {
            value.set("prosjecni_iznos",0);
            value.set("prosjecni_iznos_valuta",0);
        }
        dao.daoStore(value);
        return value;
    }
    public PrnPoRacunVo dodajRacun(PrnPoRacunVo value)  {
        PrnPoRacunJdbc dao = new PrnPoRacunJdbc();
        dao.daoCreate(value);
        return value;
    }
    public PrnPoRacunRs listaRacuna(PrnPoRacunVo value)  {
        PrnPoRacunJdbc dao = new PrnPoRacunJdbc();
        return new PrnPoRacunRs(dao.daoListaRacuna(value));
    }
    public PrnPravnaOsobaRs procitajSveUpitnike(PrnPravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        return new PrnPravnaOsobaRs(dao.daoFind(value));
    }
    public PrnPravnaOsobaVo citajUpitnik(PrnPravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        return dao.daoLoad(value);
    }
    public PrnPravnaOsobaRs pronadiUpitnike(PrnPravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        return new PrnPravnaOsobaRs(dao.daoSearch(value));
    }
    public PrnPravnaOsobaVo azurirajZavrsnePodatkeUpitnika(PrnPravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        value.setOsobaPromjene(value.getKorisnik());
        value.setZadnjaPromjenaVrijeme(value.getAsCalendar(AS2DataDictionary.ZADNJA_PROMJENA_VRIJEME));
        dao.daoStore(value);
        return value;
    }
    public PrnPravnaOsobaVo azurirajUpitnik(PrnPravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        value.setOsobaPromjene(value.getKorisnik());
        value.setZadnjaPromjenaVrijeme(value.getAsCalendar(AS2DataDictionary.ZADNJA_PROMJENA_VRIJEME));
        dao.daoStore(value);
        return value;
    }
    public PrnPravnaOsobaVo dodajUpitnik(PrnPravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        value.setDatumUnosa(AS2Date.getTodayAsCalendar());
        value.setOsobaUnosa(value.getKorisnik());
        value.setOsobaPromjene(value.getKorisnik());
        PartijaPravnihOsobaJdbc dao_partija = new PartijaPravnihOsobaJdbc();
        if (!dao_partija.daoFindPartijaPravneOsobe(new PravnaOsobaVo(value))){
            if(value.getBrojPartije().length()>0)
                throw new AS2Exception("12001");
            value.delete("broj_partije");
        }
        dao.daoCreate(value);
        value.setIdUpitnika(dao.daoZadnjiBrojUpitnika());
        return value;
    }
    public PrnPravnaOsobaVo brisiUpitnik(PrnPravnaOsobaVo value)  {
        PrnPoUpitnikJdbc dao = new PrnPoUpitnikJdbc();
        value.setOsobaPromjene(value.getKorisnik());
        value.setIspravno("0");
        dao.daoStore(value);
        return value;
    }
    public PrnPrivitakRs procitajSvePrivitke(PrnPrivitakVo value)  {
        PrnPoPrivitakJdbc dao = new PrnPoPrivitakJdbc();
        PrnPrivitakRs rs = new PrnPrivitakRs(dao.daoFind(value));
		return rs;
    }
    public PrnPrivitakVo dodajPrivitak(PrnPrivitakVo value)  {
        PrnPoPrivitakJdbc dao = new PrnPoPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public PrnPrivitakVo citajPrivitak(PrnPrivitakVo value)  {
        PrnPoPrivitakJdbc dao = new PrnPoPrivitakJdbc();
        return new PrnPrivitakVo(dao.daoLoad(value));
    }
    public PrnPrivitakVo brisiPrivitak(PrnPrivitakVo value)  {
        PrnPoPrivitakJdbc dao = new PrnPoPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
    public PrnPravnaOsobaRs dubinskaAnalizaPravneOsobe(PravnaOsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoDubinskaAnalizaPravneOsobe(value);
    }
    public PrnPravnaOsobaRs dubinskaAnalizaSvihPravnihOsoba(PravnaOsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoDubinskaAnalizaSvihPravnihOsoba(value);
    }
}
