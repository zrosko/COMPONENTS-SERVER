package hr.adriacomsoftware.app.server.pranjenovca.transakcija.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.pranjenovca.transakcija.dto.PrnTransakcijaRs;
import hr.adriacomsoftware.app.common.pranjenovca.transakcija.dto.PrnTransakcijaVo;
import hr.adriacomsoftware.app.common.pranjenovca.transakcija.facade.PranjeNovcaTransakcijaFacade;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.server.etl.facade.ETLFacadeServer;
import hr.adriacomsoftware.app.server.pranjenovca.transakcija.da.jdbc.PrnBiljeskaJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.transakcija.da.jdbc.PrnTransakcijaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class PranjeNovcaTransakcijaFacadeServer extends AS2FacadeServerLayer
	implements PranjeNovcaTransakcijaFacade {

	private static PranjeNovcaTransakcijaFacadeServer _instance = null; 
	public static PranjeNovcaTransakcijaFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new PranjeNovcaTransakcijaFacadeServer();
		}
		return _instance;
	}
	private PranjeNovcaTransakcijaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public PrnTransakcijaRs procitajSveTransakcijePravnihOsoba(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        return dao.daoFindTransakcije(value, "PO");
    }
    public PrnTransakcijaRs pronadiTransakcijePravnihOsoba(PravnaOsobaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        return dao.daoSearchTransakcije(value, "PO");
    }
    public PrnTransakcijaRs procitajSveTransakcijeGradana(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        if(value.getProperty("@@URED")==null)
            return dao.daoFindTransakcije(value, "GR");
        else
            return dao.daoFindTransakcijeZaSlanjeUredu(value, "GR");
    }
    public PrnTransakcijaRs pronadiTransakcijeGradana(OsobaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        if(value.getProperty("@@URED")==null)
            return dao.daoSearchTransakcije(value, "GR"); 
        else
            return dao.daoSearchTransakcijeZaSlanjeUredu(value, "GR");
    }
    public PrnTransakcijaRs filterTransakcijeGradana(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        if(value.getProperty("@@URED")==null)
            return dao.daoFilterTransakcije(value, "GR");
        else
            return dao.daoFilterTransakcijeZaSlanjeUredu(value, "GR");
    }
    public PrnTransakcijaRs filterTransakcijePravnihOsoba(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        if(value.getProperty("@@URED")==null)
            return dao.daoFilterTransakcije(value, "PO");
        else
            return dao.daoFilterTransakcijeZaSlanjeUredu(value, "PO");
    }
    public PrnTransakcijaRs pronadiNoveTransakcijeGradana(PrnTransakcijaVo value)  {
        //--(1)--napravi ETL prepis iz BSA baze
        ETLFacadeServer.getInstance().jobPrometMjesecniETL(null);
        PrnTransakcijaJdbc dao_trn = new PrnTransakcijaJdbc();        
        //--(2) napravi upit u prepisane podatke i pronadi bez/gotovinske transakcije vece od maksimalnog broja stavke
        //--obradi podatke za upotrebu u aplikaciji za pranje novca
        dao_trn.daoPronadiNoveTransakcijeGradana(value);
        return null;
    }
    public PrnTransakcijaVo brisiTransakciju(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        dao.daoRemove(value);
        return value;
    }
    public PrnTransakcijaRs pronadiNoveTransakcijePravnihOsoba(PrnTransakcijaVo value)  {
        return null;
    }
    public PrnTransakcijaVo citajTransakciju(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        value = dao.daoLoad(value);
        return value;
    }
    public PrnTransakcijaVo azurirajTransakciju(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        if(value.getProperty("@@URED")==null){        
            dao.daoStore(value);
        }else {
            if(value.getStanjeIsporucenostiUredu().equals("X")){
                dao.daoAzurirajStatus(value);
            }
        }
        return value;
    }
    public PrnTransakcijaVo dodajTransakciju(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        dao.daoCreate(value);
        return value;
    }
    public PrnTransakcijaRs procitajSveObradeTransakcijaGradana(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        return new PrnTransakcijaRs(dao.daoListaObradaTransakcijaGradana(value));

    }
    public PrnTransakcijaRs procitajSveStavkeTransakcije(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        return new PrnTransakcijaRs(dao.daoSveStavkeTransakcije(value,"GR"));
    }
    public PrnTransakcijaRs izvjestajTransakcijeZaRazdobljeGradana(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        PrnTransakcijaRs rezultat = dao.daoTransakcijeListaStavke(value,"GR");
        return rezultat;
    }
    public PrnTransakcijaRs izvjestajTransakcijePreko105000Gradana(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        return dao.daoTransakcijePreko105000Staro(value,value.getVrstaTransakcije());
    }
    //PRAVNE OSOBE
    public PrnTransakcijaRs izvjestajTransakcijeZaRazdobljePravnihOsoba(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        PrnTransakcijaRs rezultat = dao.daoTransakcijeListaStavke(value,"PO");
        return rezultat;
    }
    public PrnTransakcijaRs dubinskaAnalizaTransakcijeFizickeOsobe(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        return dao.daoDubinskaAnalizaTransakcijeFizickeOsobe(value);	
    }
    public PrnTransakcijaRs dubinskaAnalizaTransakcijePravneOsobe(PrnTransakcijaVo value)  {
        PrnTransakcijaJdbc dao = new PrnTransakcijaJdbc();
        return dao.daoDubinskaAnalizaTransakcijePravneOsobe(value);
    }
    public PrnTransakcijaVo citajBiljesku(PrnTransakcijaVo value)  {
        PrnBiljeskaJdbc dao = new PrnBiljeskaJdbc();
        value = dao.daoCitajBiljesku(value);
        return value;
    }
    public PrnTransakcijaVo azurirajBiljesku(PrnTransakcijaVo value)  {
        PrnBiljeskaJdbc dao = new PrnBiljeskaJdbc();
        if (value.get("biljeska").equals(""))
            dao.daoUkloniBiljesku(value);
        else
            dao.daoStore(value);
        return value;
    }
    public PrnTransakcijaVo dodajBiljesku(PrnTransakcijaVo value)  {
        PrnBiljeskaJdbc dao = new PrnBiljeskaJdbc();
        dao.daoCreate(value);
        return value;
    }
}
