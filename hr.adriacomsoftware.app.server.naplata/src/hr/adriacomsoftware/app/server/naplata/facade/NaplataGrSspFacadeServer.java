package hr.adriacomsoftware.app.server.naplata.facade;


import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspOtplatniPlanRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspOtplatniPlanVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspPrivitakVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataGrSspVo;
import hr.adriacomsoftware.app.common.naplata.facade.NaplataGrSspFacade;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrSspJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrSspOtplatniPlanJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrSspPovijestJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataGrSspPrivitakJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.transaction.AS2Transaction;


public final class NaplataGrSspFacadeServer extends AS2FacadeServerLayer 
		implements NaplataGrSspFacade {

	private static NaplataGrSspFacadeServer _instance = null;
	public static NaplataGrSspFacadeServer getInstance() {
		if (_instance == null){
			_instance = new NaplataGrSspFacadeServer();
		}
		return _instance;
	}
	private NaplataGrSspFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
	public NaplataGrSspRs procitajSveBiljeske(NaplataGrSspVo value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		if(value.getVrsta().equals("TROŠKOVI OVRHE"))
			return dao.daoFindTroskoviOvrhe(value);
		else
			return dao.daoFindBiljeska(value);
	}
	public NaplataGrSspVo procitajBiljesku(NaplataGrSspVo value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		return dao.daoLoadBiljeska(value);
	}
	public NaplataGrSspVo dodajBiljesku(NaplataGrSspVo value) {
		if(value.getVrsta().equals("TROŠKOVI OVRHE")){
			NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
			AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
			value.set("referent",user.get("korisnik"));
			value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
			dao.daoCreate(value);
		}else{
			NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
			AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
			value.set("referent",user.get("korisnik"));
			value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
			value.setCalendarAsDateString(AS2Date.getTodayAsCalendar(),"id_temp");
			dao.daoCreate(value);
			AS2Transaction.commit();
			AS2Transaction.begin();
			value.set("id_biljeske",dao.daoZadnjiIdBiljeske(value));
			if (value.get("@@privitak_dodaj").equals("true")) {
				dodajPrivitak(new NaplataGrSspPrivitakVo(value));
				value.set("@@privitak_dodaj", "false");
			}
			if(value.getVrsta().equals("OVRHA")){
				NaplataGrSspJdbc dao_troskovi = new NaplataGrSspJdbc();
				value.setVrsta("TROŠKOVI OVRHE");
				value.set("@kratki","true");
				value.set("id_biljeske_veza",value.get("id_biljeske"));
				NaplataGrSspRs rs = dao_troskovi.daoFindTroskoviOvrhe(value);
				dao_troskovi.daoStoreMany(rs);
			}
		}
		return value;
	}
	public NaplataGrSspVo azurirajBiljesku(NaplataGrSspVo value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		dao.daoStore(value);
		if(value.get("@@privitak_dodaj").equals("true")){
			dodajPrivitak(new NaplataGrSspPrivitakVo(value));
			value.set("@@privitak_dodaj","false");
		}else if(value.get("@@privitak_ukloni").equals("true")){
    		brisiPrivitak(new NaplataGrSspPrivitakVo(value));
    		value.set("@@privitak_ukloni","false");
		}
		return value;
	}
	public NaplataGrSspVo brisiBiljesku(NaplataGrSspVo value) {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		dao.daoRemove(value);
		return value;
	}
	public NaplataGrSspVo brisiViseBiljeski(NaplataGrSspRs value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		dao.daoRemoveMany(value);
		return new NaplataGrSspVo();
	}

	public NaplataGrSspRs procitajPartijeNaplateSSP(NaplataGrSspVo value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		return dao.daoLoadSSP(value);
	}
	public NaplataGrSspRs pronadiPartijeNaplateSSP(NaplataGrSspVo value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		return dao.daoFindSSP(value);
	}
	public NaplataGrSspRs procitajPovijestPolja(NaplataGrSspVo value)  {
		NaplataGrSspPovijestJdbc dao = new NaplataGrSspPovijestJdbc();
		return dao.daoProcitajPovijestPolja(value);
	}
	public NaplataGrSspRs izvjestaji(NaplataGrSspVo value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		return dao.daoIzvjestaji(value);
	}
	public NaplataGrSspRs izvjestajReferat(NaplataGrSspVo value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		NaplataGrSspRs naplata_tekuci = dao.daoIzvjestajReferat(value);
		value.set("@@report_selected","ssp_plan_otplate"); 
		value.set("id_biljeske",value.get("id_biljeske_dogovora"));
		naplata_tekuci.addResultSet("ssp_plan_otplate_sub",dao.daoIzvjestaji(value));
		return naplata_tekuci;
	}
	public NaplataGrSspPrivitakVo dodajPrivitak(NaplataGrSspPrivitakVo value)  {
		NaplataGrSspPrivitakJdbc dao = new NaplataGrSspPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public NaplataGrSspPrivitakVo citajPrivitak(NaplataGrSspPrivitakVo value)  {
    	NaplataGrSspPrivitakJdbc dao = new NaplataGrSspPrivitakJdbc();
        return new NaplataGrSspPrivitakVo(dao.daoLoad(value));
    }
    public NaplataGrSspPrivitakVo brisiPrivitak(NaplataGrSspPrivitakVo value)  {
    	NaplataGrSspPrivitakJdbc dao = new NaplataGrSspPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
	public NaplataGrSspOtplatniPlanVo azurirajOtplatniPlan(NaplataGrSspOtplatniPlanVo value)  {
		NaplataGrSspOtplatniPlanJdbc dao = new NaplataGrSspOtplatniPlanJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeUnosaIzmjene(AS2Date.getTodayAsCalendar());
        if(value.getIdPlana().length()>0)
        	dao.daoStore(value);
		return value;
	}
	public NaplataGrSspOtplatniPlanRs procitajOtplatniPlan(NaplataGrSspOtplatniPlanVo value)  {
		NaplataGrSspOtplatniPlanJdbc dao = new NaplataGrSspOtplatniPlanJdbc();
		return dao.daoLoad(value);
	}
	public NaplataGrSspOtplatniPlanVo dodajOtplatniPlan(NaplataGrSspOtplatniPlanVo value)  {
		NaplataGrSspOtplatniPlanJdbc dao = new NaplataGrSspOtplatniPlanJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
		value.setVrijemeUnosaIzmjene(AS2Date.getTodayAsCalendar());
		dao.daoCreate(value);
		return value;
	}
	public NaplataGrSspOtplatniPlanVo brisiOtplatniPlan(NaplataGrSspOtplatniPlanVo value)  {
		NaplataGrSspOtplatniPlanJdbc dao = new NaplataGrSspOtplatniPlanJdbc();
		dao.daoRemove(value);
		return value;
	}
	public NaplataGrSspOtplatniPlanVo brisiViseOtplatnihPlanova(NaplataGrSspOtplatniPlanRs value)  {
		NaplataGrSspOtplatniPlanJdbc dao = new NaplataGrSspOtplatniPlanJdbc();
		dao.daoRemoveMany(value);
		return new NaplataGrSspOtplatniPlanVo();
	}
	public NaplataGrSspOtplatniPlanVo izradiOtplatniPlan(NaplataGrSspOtplatniPlanVo value)  {
		NaplataGrSspOtplatniPlanJdbc dao = new NaplataGrSspOtplatniPlanJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
		value.setVrijemeUnosaIzmjene(AS2Date.getTodayAsCalendar());
		azurirajBiljesku(new NaplataGrSspVo(value));
		dao.daoIzradiOtplatniPlan(value);
		return value;
	}
	public NaplataGrSspRs procitajMogucePotpisnikeNagodbe(NaplataGrSspVo value)  {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		return dao.daoProcitajMogucePotpisnikeNagodbe(value);
	}
	public NaplataGrSspRs procitajSifre(NaplataGrSspVo value) {
		NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
		return dao.daoProcitajSifre(value);
	}
	public NaplataGrSspVo pripremiDopis(NaplataGrSspVo value) {
        NaplataGrSspJdbc dao = new NaplataGrSspJdbc();
        return dao.daoPripremiDopis(value);
   }

 }
