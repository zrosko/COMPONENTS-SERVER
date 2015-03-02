package hr.adriacomsoftware.app.server.pravneosobe.povezaneosobe.facade;

import hr.adriacomsoftware.app.common.pravneosobe.povezaneosobe.dto.KlijentPovezanaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.povezaneosobe.dto.KlijentPovezanaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.povezaneosobe.facade.KlijentPovezaneOsobeFacade;
import hr.adriacomsoftware.app.server.pravneosobe.povezaneosobe.da.jdbc.KlijentPovezanaOsobaJdbc;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class KlijentPovezaneOsobeFacadeServer extends AS2FacadeServerLayer
		implements KlijentPovezaneOsobeFacade {

	private static KlijentPovezaneOsobeFacadeServer _instance = null;
	public static KlijentPovezaneOsobeFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new KlijentPovezaneOsobeFacadeServer();
		}
		return _instance;
	}
	private KlijentPovezaneOsobeFacadeServer(){
	}
    public KlijentPovezanaOsobaVo azurirajPovezanuOsobu(KlijentPovezanaOsobaVo value)  {
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
		dao.daoStore(value);
		return value;
    }
    public KlijentPovezanaOsobaRs procitajSvePovezaneOsobe(KlijentPovezanaOsobaVo value)  {
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
        KlijentPovezanaOsobaRs rs;
        if(value.exists(KlijentPovezanaOsobaVo.KLIJENT_PO_POVEZANA_OSOBA__SLOG)){
            rs = new KlijentPovezanaOsobaRs(dao.daoFindPoVrstiSloga(value));
        }else{
            rs = new KlijentPovezanaOsobaRs(dao.daoFind(value));
        }
		return rs;
    }
    public KlijentPovezanaOsobaRs procitajSvePovezaneOsobePOVIJEST(KlijentPovezanaOsobaVo value)  {
        /* TODO citati BSA, PRANJE NOVCA, ZAHTJEVE
         **/
        return null;
    }
    public KlijentPovezanaOsobaVo dodajPovezanuOsobu(KlijentPovezanaOsobaVo value)  {
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public KlijentPovezanaOsobaVo dodajPovezaneOsobe(KlijentPovezanaOsobaRs value)  {
        /* Dodavanje više povezani osoba (npr. sa PDF izjava o povezanosti).
         **/
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
        /** Briši postojeće */
        if(value.size()>0){
            dao.daoRemoveUcitanePovezaneOsobe(value.getRowAt(0));
            dao.daoCreateMany(value);
        }
		return new KlijentPovezanaOsobaVo();
    }
    public KlijentPovezanaOsobaVo dodajPovezaneOsobeIzPRN(KlijentPovezanaOsobaVo value)  {
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
	    if(value.exists("klijent_maticni_broj")){
	        value.set("ucitano","3");
	        dao.daoRemoveUcitanePovezaneOsobe(value);
	        dao.daoDodaPovezaneOsobeIzPRN(value);
        }
		return new KlijentPovezanaOsobaVo();
    }
    public KlijentPovezanaOsobaVo brisiPovezanuOsobu(KlijentPovezanaOsobaVo value)  {
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public KlijentPovezanaOsobaRs listaPovezanihOsoba(KlijentPovezanaOsobaVo value)  {
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
        KlijentPovezanaOsobaRs rs = new KlijentPovezanaOsobaRs(dao.daoFindListaVezanihOsoba(value));
		return rs;
    }
    public KlijentPovezanaOsobaRs usporedbaSaBSAPovezanimOsobama(KlijentPovezanaOsobaVo value)  {
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
        KlijentPovezanaOsobaRs rs = dao.daoFindUsporedbaSaBSA(value);
		return rs;
    }
    public KlijentPovezanaOsobaVo brisiVisePovezanihOsoba(KlijentPovezanaOsobaRs value)  {
        KlijentPovezanaOsobaJdbc dao = new KlijentPovezanaOsobaJdbc();
		dao.daoRemoveMany(value);
		return new KlijentPovezanaOsobaVo();
    }   
}
