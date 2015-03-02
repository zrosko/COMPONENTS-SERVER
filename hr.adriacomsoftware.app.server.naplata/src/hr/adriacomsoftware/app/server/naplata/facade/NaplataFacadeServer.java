package hr.adriacomsoftware.app.server.naplata.facade;


import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentBiljeskaRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentBiljeskaVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentKontaktRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentKontaktVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentOtplatniPlanRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentOtplatniPlanVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentStatusRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentStatusVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataKlijentVo;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataRs;
import hr.adriacomsoftware.app.common.naplata.dto.NaplataVo;
import hr.adriacomsoftware.app.common.naplata.facade.NaplataFacade;
import hr.adriacomsoftware.app.common.rizik.izlozenost.dto.KrTransakcijaRs;
import hr.adriacomsoftware.app.common.rizik.izlozenost.dto.KrTransakcijaVo;
import hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc.PartijaKreditaGradanaJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataKlijentBiljeskaJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataKlijentJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataKlijentKontaktJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataKlijentOtplatniPlanJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataKlijentPrivitakJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataKlijentStatusJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.NaplataPovijestJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.XFileNotesJdbc;
import hr.adriacomsoftware.app.server.naplata.da.jdbc.XFilesJdbc;
import hr.adriacomsoftware.app.server.rizik.izlozenost.da.jdbc.KrTransakcijaJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.security.user.AS2UserFactory;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.common.types.AS2String;
import hr.as2.inf.common.validations.AS2ValidatorService;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.transaction.AS2Transaction;


public final class NaplataFacadeServer extends AS2FacadeServerLayer 
		implements NaplataFacade {

	private static NaplataFacadeServer _instance = null;
	public static NaplataFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new NaplataFacadeServer();
		}
		return _instance;
	}
	private NaplataFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
    public NaplataRs listajSveSpise(NaplataVo value)  {
        XFilesJdbc dao = new XFilesJdbc();
        return dao.daoFindAll(value);
    }
    public NaplataRs pronadiSpise(NaplataVo value)  {
        XFilesJdbc dao = new XFilesJdbc();
        return dao.daoFind(value);
    }
    public NaplataRs citajSpis(NaplataVo value)  {
        XFilesJdbc dao = new XFilesJdbc();
        NaplataRs res = dao.daoLoad(value);
        XFileNotesJdbc dao_biljeske = new XFileNotesJdbc();
        res.addResultSet("odvjetnik_ispis_spisa_biljeske", dao_biljeske.daoLoadAll(value));
        return res;
    }
    public NaplataRs listajSveBiljeske(NaplataVo value)  {
        XFileNotesJdbc dao = new XFileNotesJdbc();
        return dao.daoLoadAll(value);
    }
    public NaplataVo citajBiljesku(NaplataVo value)  {
        XFileNotesJdbc dao = new XFileNotesJdbc();
        return dao.daoLoad(value);
    }
    public NaplataVo dodajBiljesku(NaplataVo value)  {
        XFileNotesJdbc dao = new XFileNotesJdbc();
        dao.daoCreate(value);
        return value;
    }
    public NaplataVo azurirajBiljesku(NaplataVo value)  {
        XFileNotesJdbc dao = new XFileNotesJdbc();
        AS2User user = AS2UserFactory.getInstance().getCurrentUser();
        value.setModifiedBy(user.get("korisnik"));
        dao.daoStore(value);
        return value;
    }
    public NaplataVo brisiBiljesku(NaplataVo value)  {
        XFileNotesJdbc dao = new XFileNotesJdbc();
        AS2User user = AS2UserFactory.getInstance().getCurrentUser();
        value.setModifiedBy(user.get("korisnik"));
        dao.daoRemove(value);
        return value;
    }
    public NaplataVo brisiViseBiljeski(NaplataRs value)  {
        XFileNotesJdbc dao = new XFileNotesJdbc();
		dao.daoRemoveMany(value);
		return new NaplataVo();
    }
	public NaplataKlijentRs pronadiKlijenteNaplate(NaplataKlijentVo value) {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		if(!value.exists("@@filter"))
			return dao.daoFind(value);
		else
			return dao.daoFindFromTransakcija(value);
	}
	public NaplataKlijentRs procitajKlijenteNaplate(NaplataKlijentVo value)  {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFind(value);
	}
	public NaplataKlijentRs filtrirajKlijente(NaplataKlijentVo value)  {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFind(value);
	}
	public NaplataKlijentVo dodajKlijentaNaplate(NaplataKlijentVo value) {
        AS2ValidatorService.getInstance().checkMandatory("NaplataFacadeServer.dodajKlijentaNaplate", value); //$NON-NLS-1$
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		if(!dao.daoFindIfExists(value))
			dao.daoCreate(value);
		else
			throw new AS2Exception("15500");
		return value;
	}
	public NaplataKlijentVo azurirajKlijentaNaplate(NaplataKlijentVo value)	 {
        AS2ValidatorService.getInstance().checkMandatory("NaplataFacadeServer.dodajKlijentaNaplate", value); //$NON-NLS-1$
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		dao.daoStore(value);
		return value;
	}
	public NaplataKlijentKontaktVo azurirajKontakt(NaplataKlijentKontaktVo value) {
		NaplataKlijentKontaktJdbc dao = new NaplataKlijentKontaktJdbc();
		dao.daoStore(value);
		return value;
	}
	public NaplataKlijentKontaktRs procitajSveKontakte(NaplataKlijentVo value)  {
		NaplataKlijentKontaktJdbc dao = new NaplataKlijentKontaktJdbc();
		return dao.daoFind(value);
	}
	public NaplataKlijentKontaktVo dodajKontakt(NaplataKlijentKontaktVo value) {
		NaplataKlijentKontaktJdbc dao = new NaplataKlijentKontaktJdbc();
		dao.daoCreate(value);
		return value;
	}
	public NaplataKlijentKontaktVo brisiKontakt(NaplataKlijentKontaktVo value) {
		NaplataKlijentKontaktJdbc dao = new NaplataKlijentKontaktJdbc();
		dao.daoRemove(value);
		return value;
	}
	public NaplataKlijentKontaktVo brisiViseKontakta(NaplataKlijentKontaktRs value)  {
		NaplataKlijentKontaktJdbc dao = new NaplataKlijentKontaktJdbc();
		dao.daoRemoveMany(value);
		return new NaplataKlijentKontaktVo();
	}
	public NaplataKlijentBiljeskaRs procitajSveBiljeske(NaplataKlijentVo value)  {
		NaplataKlijentBiljeskaJdbc dao = new NaplataKlijentBiljeskaJdbc();
		return dao.daoFind(value);
	}
	public NaplataKlijentBiljeskaVo dodajBiljesku(NaplataKlijentBiljeskaVo value) {
		NaplataKlijentBiljeskaJdbc dao = new NaplataKlijentBiljeskaJdbc();
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setCalendarAsDateString(AS2Date.getTodayAsCalendar(),"id_temp");
		dao.daoCreate(value);
		AS2Transaction.commit();
		AS2Transaction.begin();
		value.set("id_biljeske",dao.daoZadnjiIdBiljeske(value));
		if (value.get("@@privitak_dodaj").equals("true")) {
			dodajPrivitak(new NaplataVo(value));
			value.set("@@privitak_dodaj", "false");
		}
		return value;
	}
	public NaplataKlijentBiljeskaVo azurirajBiljesku(NaplataKlijentBiljeskaVo value)  {
		NaplataKlijentBiljeskaJdbc dao = new NaplataKlijentBiljeskaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		dao.daoStore(value);
		if(value.get("@@privitak_dodaj").equals("true")){
			dodajPrivitak(new NaplataVo(value));
			value.set("@@privitak_dodaj","false");
		}else if(value.get("@@privitak_ukloni").equals("true")){
    		brisiPrivitak(new NaplataVo(value));
    		value.set("@@privitak_ukloni","false");
		}
		return value;
	}
	public NaplataKlijentBiljeskaVo brisiBiljesku(NaplataKlijentBiljeskaVo value) {
		NaplataKlijentBiljeskaJdbc dao = new NaplataKlijentBiljeskaJdbc();
		dao.daoRemove(value);
		return value;
	}
	public NaplataKlijentBiljeskaVo brisiViseBiljeskiKlijenta(NaplataKlijentBiljeskaRs value)  {
		NaplataKlijentBiljeskaJdbc dao = new NaplataKlijentBiljeskaJdbc();
		dao.daoRemoveMany(value);
		return new NaplataKlijentBiljeskaVo();
	}
	public NaplataKlijentVo dodajNoveKlijente(AS2RecordList value)	 {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		dao.daoCreateMany(value);
		return new NaplataKlijentVo();
	}
	public void brisiViseKlijenata(AS2RecordList value)  {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		value.addValue("ispravno", "0");//logički brisi
		dao.daoStoreMany(value);		
	}
	public NaplataKlijentStatusVo azurirajStatus(NaplataKlijentStatusVo value)	 {
		NaplataKlijentStatusJdbc dao = new NaplataKlijentStatusJdbc();
		dao.daoStore(value);
		return value;
	}
	public NaplataKlijentStatusRs procitajSveStatuse(NaplataKlijentVo value) {
		NaplataKlijentStatusJdbc dao = new NaplataKlijentStatusJdbc();
		return dao.daoFind(value);
	}
	public NaplataKlijentStatusVo dodajStatus(NaplataKlijentStatusVo value)	 {
		NaplataKlijentStatusJdbc dao = new NaplataKlijentStatusJdbc();
		dao.daoCreate(value);
		return value;
	}
	public NaplataKlijentStatusVo brisiStatus(NaplataKlijentStatusVo value)	 {
		NaplataKlijentStatusJdbc dao = new NaplataKlijentStatusJdbc();
		dao.daoRemove(value);
		return value;
	}
	public NaplataKlijentStatusVo brisiViseStatusaKlijenta(	NaplataKlijentStatusRs value)  {
		NaplataKlijentStatusJdbc dao = new NaplataKlijentStatusJdbc();
		dao.daoRemoveMany(value);
		return new NaplataKlijentStatusVo();
	}
	public NaplataKlijentRs izvjesceKlijentaNaplate(NaplataKlijentVo value)	 {
		NaplataKlijentRs _spis = new NaplataKlijentRs(); //povrat više res. setova, u rs se nalaze osnovi podaci (1 red)  
		NaplataKlijentJdbc dao_klijent = new NaplataKlijentJdbc();
		NaplataKlijentStatusJdbc dao_status = new NaplataKlijentStatusJdbc();
		NaplataKlijentVo _row = new NaplataKlijentVo(dao_klijent.daoLoad(value));
		_spis.addRow(_row);
		
		if(AS2String.ifExists(value.getOpcijeIspisa(), "izvjesce_napomena")){
			if(_row.getNapomena().length()>0)
				_spis.addResultSet("izvjesce_napomena", _spis);
		}
		if(AS2String.ifExists(value.getOpcijeIspisa(), "izvjesce_opis")){
			NaplataKlijentStatusRs rsJ2EEResultSet =  dao_status.daoFindZadnjiOpis(value);
			_spis.addResultSet("izvjesce_opis", rsJ2EEResultSet);
        }
        if(AS2String.ifExists(value.getOpcijeIspisa(), "izvjesce_zakljucak")){
			NaplataKlijentStatusRs rsJ2EEResultSet =  dao_status.daoFindZadnjiZakljucak(value);
			_spis.addResultSet("izvjesce_zakljucak", rsJ2EEResultSet);
        }
        if(AS2String.ifExists(value.getOpcijeIspisa(), "izvjesce_zaduzenost")){
	        KrTransakcijaJdbc dao = new KrTransakcijaJdbc();
	        KrTransakcijaRs rsJ2EEResultSet =  dao.daoZaduzenostKlijentaNaDan(new KrTransakcijaVo(value));
	        _spis.addResultSet("izvjesce_zaduzenost", rsJ2EEResultSet);
        }
		return _spis;
	}
	public NaplataKlijentRs izvjesceNaplateSkupno(NaplataKlijentVo value)  {
		NaplataKlijentBiljeskaJdbc dao = new NaplataKlijentBiljeskaJdbc();
		return new NaplataKlijentRs(dao.daoFindIzvjesceSkupno(value));
	}
	public AS2RecordList prometProlaznaKontaKreditaGradana(OsnovniVo value)	 {
		PartijaKreditaGradanaJdbc dao = new PartijaKreditaGradanaJdbc();
		AS2RecordList filter = dao.daoExecuteQuery("select distinct jmbg from OLTP_PROD.dbo.naplata_klijent where vrsta like 'gr%' and isnull(ispravno,1)=1");
		OsnovniRs j2eers = dao.daoPrometProlazniRacuni(value);
		return j2eers.doSearch("jmbg", "=", filter); 	
	}
	public NaplataKlijentRs procitajKlijenteNaplateSSP(NaplataKlijentVo value)  {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoLoadSSP(value);
	}
	public NaplataKlijentRs pronadiKlijenteNaplateSSP(NaplataKlijentVo value)  {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFindSSP(value);
	}
	public NaplataRs procitajPovijestPolja(NaplataVo value)  {
		NaplataPovijestJdbc dao = new NaplataPovijestJdbc();
		return dao.daoProcitajPovijestPolja(value);
	}
	public NaplataRs izvjestaji(NaplataVo value)  {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoIzvjestaji(value);
	}
	public NaplataRs izvjestajReferat(NaplataVo value)  {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoIzvjestajReferat(value);
	}
	public NaplataVo dodajPrivitak(NaplataVo value)  {
        NaplataKlijentPrivitakJdbc dao = new NaplataKlijentPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public NaplataVo citajPrivitak(NaplataVo value)  {
    	NaplataKlijentPrivitakJdbc dao = new NaplataKlijentPrivitakJdbc();
        return new NaplataVo(dao.daoLoad(value));
    }
    public NaplataVo brisiPrivitak(NaplataVo value)  {
    	NaplataKlijentPrivitakJdbc dao = new NaplataKlijentPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
	public NaplataKlijentOtplatniPlanVo azurirajOtplatniPlan(NaplataKlijentOtplatniPlanVo value)  {
		NaplataKlijentOtplatniPlanJdbc dao = new NaplataKlijentOtplatniPlanJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
        value.setVrijemeUnosaIzmjene(AS2Date.getTodayAsCalendar());
		dao.daoStore(value);
		return value;
	}
	public NaplataKlijentOtplatniPlanRs procitajOtplatniPlan(NaplataKlijentOtplatniPlanVo value)  {
		NaplataKlijentOtplatniPlanJdbc dao = new NaplataKlijentOtplatniPlanJdbc();
		return dao.daoLoad(value);
	}
	public NaplataKlijentOtplatniPlanVo dodajOtplatniPlan(NaplataKlijentOtplatniPlanVo value)  {
		NaplataKlijentOtplatniPlanJdbc dao = new NaplataKlijentOtplatniPlanJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
		value.setVrijemeUnosaIzmjene(AS2Date.getTodayAsCalendar());
		dao.daoCreate(value);
		return value;
	}
	public NaplataKlijentOtplatniPlanVo brisiOtplatniPlan(NaplataKlijentOtplatniPlanVo value)  {
		NaplataKlijentOtplatniPlanJdbc dao = new NaplataKlijentOtplatniPlanJdbc();
		dao.daoRemove(value);
		return value;
	}
	public NaplataKlijentOtplatniPlanVo brisiViseOtplatnihPlanova(NaplataKlijentOtplatniPlanRs value)  {
		NaplataKlijentOtplatniPlanJdbc dao = new NaplataKlijentOtplatniPlanJdbc();
		dao.daoRemoveMany(value);
		return new NaplataKlijentOtplatniPlanVo();
	}
	public NaplataKlijentOtplatniPlanVo izradiOtplatniPlan(NaplataKlijentOtplatniPlanVo value)  {
		NaplataKlijentOtplatniPlanJdbc dao = new NaplataKlijentOtplatniPlanJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        value.set("referent",user.get("korisnik"));
		value.setVrijemeUnosaIzmjene(AS2Date.getTodayAsCalendar());
		azurirajBiljesku(new NaplataKlijentBiljeskaVo(value));
		dao.daoIzradiOtplatniPlan(value);
		return value;
	}
	public NaplataKlijentBiljeskaRs procitajMogucePotpisnikeNagodbe(NaplataKlijentBiljeskaVo value)  {
		NaplataKlijentBiljeskaJdbc dao = new NaplataKlijentBiljeskaJdbc();
		return dao.daoProcitajMogucePotpisnikeNagodbe(value);
	}
	public NaplataKlijentBiljeskaRs procitajSifre(NaplataKlijentBiljeskaVo value) {
		NaplataKlijentBiljeskaJdbc dao = new NaplataKlijentBiljeskaJdbc();
		return dao.daoProcitajSifre(value);
	}
	public NaplataKlijentRs izvjesceRocnostGradani(NaplataKlijentVo value) {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFindGradaniRocnost(value);
	}
	public NaplataKlijentRs izvjesceRocnostPravneOsobe(NaplataKlijentVo value) {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFindPravneOsobeRocnost(value);
	}
	public NaplataKlijentRs izvjesceZaduzenostiGradani(NaplataKlijentVo value) {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFindGradaniZaduzenost(value);
	}
	public NaplataKlijentRs izvjesceZaduzenostiPravneOsobe(NaplataKlijentVo value) {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFindPravneOsobeZaduzenost(value);
	}
	public NaplataKlijentRs izvjesceGradaniStanjePoRacunima(NaplataKlijentVo value) {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFindGradaniStanjePoRacunima(value);
	}
	public NaplataKlijentRs izvjescePravneOsobeStanjePoRacunima(NaplataKlijentVo value)  {
		NaplataKlijentJdbc dao = new NaplataKlijentJdbc();
		return dao.daoFindPravneOsobeStanjePoRacunima(value);
	}
 }
