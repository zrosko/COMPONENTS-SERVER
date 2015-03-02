package hr.adriacomsoftware.app.server.ovrha.facade;

import hr.adriacomsoftware.app.common.ovrha.dto.OvrhaRs;
import hr.adriacomsoftware.app.common.ovrha.dto.OvrhaVo;
import hr.adriacomsoftware.app.common.ovrha.facade.OvrhaFacade;
import hr.adriacomsoftware.app.server.ovrha.da.jdbc.OvrhaJdbc;
import hr.adriacomsoftware.app.server.ovrha.da.jdbc.RacunJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.common.validations.AS2ValidatorService;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.session.AS2SessionFactory;


public final class OvrhaFacadeServer extends AS2FacadeServerLayer
		implements OvrhaFacade {

	private static OvrhaFacadeServer _instance = null;
	public static OvrhaFacadeServer getInstance() {
		if (_instance == null){
			_instance = new OvrhaFacadeServer();
		}
		return _instance;
	}
	private OvrhaFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
    public OvrhaRs listajSveOvrhe(OvrhaVo value)  {
        OvrhaJdbc dao = new OvrhaJdbc();
        return dao.daoLoadAll(value);
    }
    public OvrhaRs citajSveRacuneOvrhe(OvrhaVo value)  {
        RacunJdbc dao = new RacunJdbc();
        return dao.daoFind(value);
    }
    public OvrhaVo dodajOvrhu(OvrhaVo value)  {
        checkMandatory(value);
        OvrhaJdbc dao = new OvrhaJdbc();
        RacunJdbc dao_racun = new RacunJdbc();
        AS2User _user = AS2SessionFactory.getInstance().getCurrentUser();
        if (value.getKorisnik().equals("")){
            value.setKorisnik(_user.get("korisnik"));
            value.setOperaterUnosa(_user.get("bsa_korisnik"));
            value.setVrijemeUnosa(AS2Date.getTodayAsCalendar());
        }
        if(value.getIdOvrhe().equals("")){
            value.setBrojPaketa(dao.daoBrojPaketa(value));
            dao.daoCreate(value);
            value.setIdOvrhe(dao.daoZadnjiIdOvrhe(value));
        }else
            value = azurirajOvrhu(value);
        if (provjeriPoljaRacuna(value)==true && value.getTip().equals("osnova"))
            dao_racun.daoCreate(value);
        return value;
    }
    public OvrhaVo azurirajOvrhu(OvrhaVo value)  {
        checkMandatory(value);
        OvrhaJdbc dao = new OvrhaJdbc();
        RacunJdbc dao_racun = new RacunJdbc();
        AS2User _user = AS2SessionFactory.getInstance().getCurrentUser();
        value.setOperaterPosljednjeIzmjene(_user.get("bsa_korisnik"));
        value.setDatumPosljednjeIzmjene(AS2Date.getTodayAsCalendar());
        dao.daoStore(value);
        if(!value.getIdRacuna().equals(""))
            dao_racun.daoStore(value);
        return value;
    }
    public OvrhaVo brisiOvrhu(OvrhaVo value)  {
        OvrhaJdbc dao = new OvrhaJdbc();
        OvrhaVo vo = new OvrhaVo();
        vo.setIdOvrhe(value.getIdOvrhe());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public OvrhaVo brisiRacun(OvrhaVo value)  {
        RacunJdbc dao = new RacunJdbc();
        OvrhaVo vo = new OvrhaVo();
        vo.setIdRacuna(value.getIdRacuna());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public OvrhaVo zadnjiRedniBrojKorisnika(OvrhaVo value)  {
        OvrhaJdbc dao = new OvrhaJdbc();
        AS2User _user = AS2SessionFactory.getInstance().getCurrentUser();
        value.setKorisnik(_user.get("korisnik"));
        value.setRedniBroj(dao.daoZadnjiRedniBroj(value));
        return value;
    }
    public OvrhaRs exportExcel(OvrhaVo value)  {
        OvrhaJdbc dao = new OvrhaJdbc();
        return dao.daoExportExcel(value);
    }
    public static void checkMandatory(OvrhaVo value)  {
        if(value.getTip().equals("mjera"))
            AS2ValidatorService.getInstance().checkMandatory("OvrhaFacadeServer.mjera", value);
        else if(value.getTip().equals("dodatna")){
            AS2ValidatorService.getInstance().checkMandatory("OvrhaFacadeServer.dodatna", value);
        }else{
            if(value.getSifraPravomocnosti().equals("0"))
                AS2ValidatorService.getInstance().checkMandatory("OvrhaFacadeServer.ovrhaPravomocnost", value);
            else if (provjeriPoljaRacuna(value) == true)
                AS2ValidatorService.getInstance().checkMandatory("OvrhaFacadeServer.ovrha", value);
            else
                AS2ValidatorService.getInstance().checkMandatory("OvrhaFacadeServer.ovrhaOsnovno", value);
        }
    }
    public static boolean provjeriPoljaRacuna(OvrhaVo value)  {
        boolean povrat = true;
        if( value.getSifraVrsteDuga().equals("") &&
			value.getVisinaKamatneStope().equals("") &&
			value.getOznakaStraneVal().equals("") &&
			value.getOstatakGlavnogDugaKn().equals("") &&
			value.getOstatakGlavnogDugaVal().equals("") &&
			value.getOstatakKamateKn().equals("") &&
			value.getOstatakKamateVal().equals("") &&
			value.getRazinaBlokade().equals("") &&
			value.getRacunOdobrenja().equals("") &&
			value.getModelPbo().equals("") &&
			value.getPbo().equals("") &&
			value.getSifraIzdavateljaOsnove().equals("") &&
			value.getMjestoIzdavateljaOsnove().equals("") &&
			value.getSifraVrsteIsplate().equals("") &&
			value.getSifraPravomocnosti().equals("") &&
			value.getIznosZaplijenjenihSredstavaGlavnica().equals("") &&
			value.getIznosZaplijenjenihSredstavaKamata().equals("") )
            return false;
        return povrat;
    }
    public OvrhaRs pronadiOvrhe(OvrhaVo value)  {
        OvrhaJdbc dao = new OvrhaJdbc();
        if(value.get("@@tip").equals("izvjestaj"))
            return dao.daoPronadiOvrheIzvjestaj(value);
        else if(value.get("@@tip").equals("izvjestaj_prilog2"))
            return dao.daoPronadiOvrheIzvjestajPrilog2(value);
        else
            return dao.daoPronadiOvrhe(value);
    }
    public OvrhaVo izracunajBrojPaketa(OvrhaVo value)  {
        OvrhaJdbc dao = new OvrhaJdbc();
        dao.daoStpIzracunajBrojPaketa(value);
        return value;
    }
}
