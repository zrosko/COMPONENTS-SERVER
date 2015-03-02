package hr.adriacomsoftware.app.server.pranjenovca.gr.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnFizickaOsobaRs;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnFizickaOsobaVo;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnGrRacunRs;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnGrRacunVo;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnPoliticarVo;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnPrivitakRs;
import hr.adriacomsoftware.app.common.pranjenovca.gr.dto.PrnPrivitakVo;
import hr.adriacomsoftware.app.common.pranjenovca.gr.facade.PranjeNovcaFacade;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PartijaGradanaJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc.PrnCrnaListaDrzavaJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc.PrnCrnaListaOsobaJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.gr.da.jdbc.PrnGrPrivitakJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.gr.da.jdbc.PrnGrRacunJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.gr.da.jdbc.PrnGrUpitnikJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.gr.da.jdbc.PrnPoliticarJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.i18n.AS2DataDictionary;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.common.validations.AS2ValidatorService;
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
    public OsobaRs procitajSveOsobe(OsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        return dao.daoFindOsoba(value);
    }
    public OsobaRs pronadiOsobe(OsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        return dao.daoSearchOsoba(value);
    }
    public OsobaRs izvjestajiFizickeOsobe(OsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        return dao.daoIzvjestajiFizickeOsobe(value);
    }
    public PrnGrRacunVo citajRacun(PrnGrRacunVo value)  {
        PrnGrRacunJdbc dao = new PrnGrRacunJdbc();
        return dao.daoLoad(value);
    }
    public PrnGrRacunVo azurirajRacun(PrnGrRacunVo value)  {
        PrnGrRacunJdbc dao = new PrnGrRacunJdbc();
        if (value.get("racun_u_ime_drugoga").matches("NE")) {
            if(value.get("racun_u_ime_drugoga_opis").length()>0)
                value.set("racun_u_ime_drugoga_opis","");
            if(value.get("dokaz_ovlastenosti_zastupanja").length()>0)
                value.set("dokaz_ovlastenosti_zastupanja","");
        }
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
    public PrnGrRacunVo dodajRacun(PrnGrRacunVo value)  {
        PrnGrRacunJdbc dao = new PrnGrRacunJdbc();
        dao.daoCreate(value);
        return value;
    }
    public PrnGrRacunRs listaRacuna(PrnGrRacunVo value)  {
        PrnGrRacunJdbc dao = new PrnGrRacunJdbc();
        return new PrnGrRacunRs(dao.daoListaRacuna(value));
    }
    public PrnPoliticarVo citajPoliticara(PrnPoliticarVo value)  {
        PrnPoliticarJdbc dao = new PrnPoliticarJdbc();
        return dao.daoLoad(value);
    }
    public PrnPoliticarVo azurirajPoliticara(PrnPoliticarVo value)  {
        PrnPoliticarJdbc dao = new PrnPoliticarJdbc();
        dao.daoStore(value);
        return value;
    }
    public PrnPoliticarVo dodajPoliticara(PrnPoliticarVo value)  {
        PrnPoliticarJdbc dao = new PrnPoliticarJdbc();
        dao.daoCreate(value);
        return value;
    }
    public PrnFizickaOsobaRs procitajSveUpitnike(PrnFizickaOsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        return new PrnFizickaOsobaRs(dao.daoFind(value));
    }
    public PrnFizickaOsobaVo citajUpitnik(PrnFizickaOsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        return dao.daoLoad(value);
    }
    public PrnFizickaOsobaRs pronadiUpitnike(PrnFizickaOsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        return new PrnFizickaOsobaRs(dao.daoSearch(value));
    }
    public PrnFizickaOsobaVo azurirajZavrsnePodatkeUpitnika(PrnFizickaOsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        value.setOsobaPromjene(value.getKorisnik());
        value.setZadnjaPromjenaVrijeme(value.getAsCalendar(AS2DataDictionary.ZADNJA_PROMJENA_VRIJEME));
        dao.daoStore(value);
        return value;
    }
    public PrnFizickaOsobaVo azurirajUpitnik(PrnFizickaOsobaVo value)  {
        AS2ValidatorService.getInstance().checkMandatory("PranjeNovcaFacadeServer.upitnik", value); //$NON-NLS-1$
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        value.setOsobaPromjene(value.getKorisnik());
        value.setZadnjaPromjenaVrijeme(value.getAsCalendar(AS2DataDictionary.ZADNJA_PROMJENA_VRIJEME));
        dao.daoStore(value);
        return value;
    }
    public PrnFizickaOsobaVo dodajUpitnik(PrnFizickaOsobaVo value)  {
        AS2ValidatorService.getInstance().checkMandatory("PranjeNovcaFacadeServer.upitnik", value); //$NON-NLS-1$
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        value.setDatumUnosa(AS2Date.getTodayAsCalendar());
        value.setOsobaUnosa(value.getKorisnik());
        value.setOsobaPromjene(value.getKorisnik());
        PartijaGradanaJdbc dao_partija = new PartijaGradanaJdbc();
        if (!dao_partija.daoFindPartijePovezanihOsoba(new OsobaVo(value))){
            if(value.getBrojPartije().length()>0)
                throw new AS2Exception("12001");
            value.delete("broj_partije");
        }
        dao.daoCreate(value);
        value.setIdUpitnika(dao.daoZadnjiBrojUpitnika());
        return value;
    }
    public PrnFizickaOsobaVo brisiUpitnik(PrnFizickaOsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        value.setOsobaPromjene(value.getKorisnik());
        value.setIspravno("0");
        dao.daoStore(value);
        return value;
    }
    public PrnFizickaOsobaVo ucitajUpitnik(PrnFizickaOsobaVo value)  {
        PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
        return dao.daoUcitaj(value);
    }
    public PrnPrivitakRs procitajSvePrivitke(PrnPrivitakVo value)  {
        PrnGrPrivitakJdbc dao = new PrnGrPrivitakJdbc();
        PrnPrivitakRs rs = new PrnPrivitakRs(dao.daoFind(value));
		return rs;
    }
    public PrnPrivitakVo dodajPrivitak(PrnPrivitakVo value)  {
        PrnGrPrivitakJdbc dao = new PrnGrPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public PrnPrivitakVo citajPrivitak(PrnPrivitakVo value)  {
        PrnGrPrivitakJdbc dao = new PrnGrPrivitakJdbc();
        return new PrnPrivitakVo(dao.daoLoad(value));
    }
    public PrnPrivitakVo brisiPrivitak(PrnPrivitakVo value)  {
        PrnGrPrivitakJdbc dao = new PrnGrPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
    public PrnFizickaOsobaRs dubinskaAnalizaOsobe(OsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoDubinskaAnalizaOsobe(value);
    }
    public PrnFizickaOsobaRs dubinskaAnalizaSvihOsoba(OsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoDubinskaAnalizaSvihOsoba(value);
    }
	public AS2RecordList izvjestajSwiftTransakcijeDrzavaCrnaLista(OsnovniVo value) {
	    PrnCrnaListaDrzavaJdbc dao = new PrnCrnaListaDrzavaJdbc();
	    return dao.daoSwiftTransakcije(value);
	}
	public OsnovniRs procitajPrnSifre(OsnovniVo value) {
		PrnGrUpitnikJdbc dao = new PrnGrUpitnikJdbc();
		return dao.daoProcitajSifre(value);
	}
}
