package hr.adriacomsoftware.app.server.karticno.gr.facade;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.karticno.dto.McardSifrarnikRs;
import hr.adriacomsoftware.app.common.karticno.dto.McardSifrarnikVo;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrPokazateljRs;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrPokazateljVo;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevOcjenaRs;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevOcjenaVo;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevPrivitakRs;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevPrivitakVo;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevRs;
import hr.adriacomsoftware.app.common.karticno.gr.dto.McardGrZahtjevVo;
import hr.adriacomsoftware.app.common.karticno.gr.facade.KarticnoFacade;
import hr.adriacomsoftware.app.server.karticno.da.jdbc.McardGrSifrarnikJdbc;
import hr.adriacomsoftware.app.server.karticno.gr.da.jdbc.McardGrPokazateljJdbc;
import hr.adriacomsoftware.app.server.karticno.gr.da.jdbc.McardGrZahtjevJdbc;
import hr.adriacomsoftware.app.server.karticno.gr.da.jdbc.McardGrZahtjevOcjenaJdbc;
import hr.adriacomsoftware.app.server.karticno.gr.da.jdbc.McardGrZahtjevPrivitakJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.transaction.AS2Transaction;


public final class KarticnoFacadeServer extends AS2FacadeServerLayer
		implements KarticnoFacade {
	private static KarticnoFacadeServer _instance = null;

	public static KarticnoFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new KarticnoFacadeServer();
		}
		return _instance;
	}

	private KarticnoFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	public McardGrZahtjevVo brisiMcardGrZahtjev(McardGrZahtjevVo value)	 {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		McardGrZahtjevVo vo = new McardGrZahtjevVo();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		vo.setOperaterIzmjene(user.get("korisnik"));
		vo.setVrijemeIzmjene(AS2Date.getTodayAsCalendar());
		vo.setIspravno("0");
		vo.setBrojZahtjeva(value.getBrojZahtjeva());
		dao.daoStore(vo);
		return value;
	}

	public McardGrZahtjevVo dodajMcardGrZahtjev(McardGrZahtjevVo value) {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterIzmjene(user.get("korisnik"));
		value.setVrijemeIzmjene(AS2Date.getTodayAsCalendar());
		value.setZaprimatelj(user.get("korisnik"));
		value.setDatumZaprimanja(AS2Date.getTodayAsCalendar());
		if(value.getStatusZahtjeva().length()<1)
			value.setStatusZahtjeva("U tijeku");
		dao.daoCreate(value);
		AS2Transaction.commit();
		AS2Transaction.begin();
		value.set("broj_zahtjeva",dao.daoZadnjiBrojZahtjeva(value));
		return new McardGrZahtjevVo(value);
	}

	public McardGrZahtjevVo azurirajMcardGrZahtjev(McardGrZahtjevVo value) {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterIzmjene(user.get("korisnik"));
		value.setVrijemeIzmjene(AS2Date.getTodayAsCalendar());
		
		if(value.getOsiguranoDepozitom().equalsIgnoreCase("da")){
			double preporuceni_odobreni_iznos = value.getAsDouble(McardGrZahtjevVo.MCARD_GR_ZAHTJEV__STANJE_DEPOZITA);
			preporuceni_odobreni_iznos = preporuceni_odobreni_iznos/11*10;
			value.setPreporuceniOdobrenIznos(preporuceni_odobreni_iznos+"");
		}
	    if (value.getVrstaKartice().equalsIgnoreCase("CHARGE")){
	    	//nije potreban skoring - brišemo ga
			McardGrZahtjevOcjenaVo upit_vo = new McardGrZahtjevOcjenaVo();
			upit_vo.setBrojZahtjeva(value.getBrojZahtjeva());
			McardGrZahtjevOcjenaJdbc dao_skoring = new McardGrZahtjevOcjenaJdbc();
			dao_skoring.daoRemoveSveOcjeneZaZahtjev(upit_vo);
	    }
		if(value.getStatusZahtjeva().length()<1)
			value.setStatusZahtjeva("U tijeku");
		dao.daoStore(value);
		//povrat 
		McardGrZahtjevVo zahtjev = dao.daoLoadZahtjev(value);
		McardGrZahtjevVo odluka = dao.daoLoadZahtjevOdluka(value);
		zahtjev.append(odluka);
		return zahtjev;
	}

	public McardGrZahtjevRs procitajSveMcardGrZahtjev(McardGrZahtjevVo value) {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		return new McardGrZahtjevRs(dao.daoFindZahtjeve(value,false));
	}
	
	public McardGrZahtjevVo procitajMcardGrZahtjev(McardGrZahtjevVo value) {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		//povrat 
		McardGrZahtjevVo zahtjev = dao.daoLoadZahtjev(value);
		McardGrZahtjevVo odluka = dao.daoLoadZahtjevOdluka(value);
		zahtjev.append(odluka);
		return zahtjev;
	}
	
	public AS2RecordList listajSveMcardGrZahtjev()  {
		return new AS2RecordList();
	}

	public McardGrZahtjevRs pretraziMcardGrZahtjev(McardGrZahtjevVo value) {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		return new McardGrZahtjevRs(dao.daoFindZahtjeve(value,true));
	}
    public McardGrZahtjevVo ucitajPodatkeZaMcardGrZahtjev(McardGrZahtjevVo value) {
    	McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		return dao.daoLoadPodatkeKlijenta(value);
    }

	public McardGrZahtjevRs procitajSifre(McardGrZahtjevVo value) {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		return dao.daoProcitajSifre(value);
	}
	
	public McardGrPokazateljVo brisiMcardGrPokazatelj(McardGrPokazateljVo value)  {
		McardGrPokazateljJdbc dao = new McardGrPokazateljJdbc();
		dao.daoRemove(value);
		return value;
	}

	public McardGrPokazateljVo dodajMcardGrPokazatelj(McardGrPokazateljVo value)  {
		McardGrPokazateljJdbc dao = new McardGrPokazateljJdbc();
		AS2Record res = dao.daoCreate(value);
		return new McardGrPokazateljVo(res);
	}

	public McardGrPokazateljVo azurirajMcardGrPokazatelj(McardGrPokazateljVo value)  {
		McardGrPokazateljJdbc dao = new McardGrPokazateljJdbc();
		AS2Record res = dao.daoStore(value);
		return new McardGrPokazateljVo(res);
	}

	public McardGrPokazateljRs procitajSveMcardGrPokazatelj(McardGrPokazateljVo value)  {
		McardGrPokazateljJdbc dao = new McardGrPokazateljJdbc();
		return new McardGrPokazateljRs(dao.daoFind(value));
	}

	public AS2RecordList listajSveMcardGrPokazatelj()  {
		return new AS2RecordList();
	}

	public McardGrPokazateljRs pretraziMcardGrPokazatelj(McardGrPokazateljVo value)  {
		McardGrPokazateljJdbc dao = new McardGrPokazateljJdbc();
		return new McardGrPokazateljRs(dao.daoFind(value));
	}
	public McardGrZahtjevOcjenaVo brisiMcardGrZahtjevOcjena(McardGrZahtjevOcjenaVo value)  {
		McardGrZahtjevOcjenaJdbc dao = new McardGrZahtjevOcjenaJdbc();
		dao.daoRemove(value);
		return value;
	}
	public McardGrZahtjevOcjenaVo brisiMcardGrZahtjevSveOcjene(McardGrZahtjevOcjenaVo value)  {
		McardGrZahtjevOcjenaJdbc dao = new McardGrZahtjevOcjenaJdbc();
		dao.daoRemoveSveOcjeneZaZahtjev(value);
		return value;
	}
	public McardGrZahtjevOcjenaVo dodajMcardGrZahtjevOcjena(McardGrZahtjevOcjenaVo value)  {
		McardGrZahtjevOcjenaJdbc dao = new McardGrZahtjevOcjenaJdbc();
		AS2Record res = dao.daoCreate(value);
		res = dao.daoLoad(res);
		return new McardGrZahtjevOcjenaVo(res);
	}
	public McardGrZahtjevOcjenaVo azurirajMcardGrZahtjevOcjena(McardGrZahtjevOcjenaVo value)  {
		McardGrZahtjevOcjenaJdbc dao = new McardGrZahtjevOcjenaJdbc();
		AS2Record res = dao.daoStore(value);
		res = dao.daoLoad(res);
		return new McardGrZahtjevOcjenaVo(res);
	}
	public McardGrZahtjevOcjenaRs procitajSveMcardGrZahtjevOcjena(McardGrZahtjevOcjenaVo value)  {
		McardGrZahtjevOcjenaJdbc dao = new McardGrZahtjevOcjenaJdbc();
		return new McardGrZahtjevOcjenaRs(dao.daoFind(value));
	}
	public AS2RecordList listajSveMcardGrZahtjevOcjena()  {
		return new AS2RecordList();
	}
	public McardGrZahtjevOcjenaRs pretraziMcardGrZahtjevOcjena(McardGrZahtjevOcjenaVo value)  {
		McardGrZahtjevOcjenaJdbc dao = new McardGrZahtjevOcjenaJdbc();
		return new McardGrZahtjevOcjenaRs(dao.daoFind(value));
	}
    public McardGrZahtjevOcjenaRs pripremaOcjena(McardGrZahtjevOcjenaVo value)  {
    	McardGrZahtjevVo vo_zah = new McardGrZahtjevVo();
    	vo_zah.setBrojZahtjeva(value.getBrojZahtjeva());
    	vo_zah.setHrokRateKredita(value.get("hrok_rate_kredita"));
    	McardGrZahtjevJdbc dao_zah = new McardGrZahtjevJdbc();
    	dao_zah.daoStore(vo_zah);
    	McardGrZahtjevOcjenaJdbc dao = new McardGrZahtjevOcjenaJdbc();
        dao.daoSpPripremiOcjene(value);
        McardGrZahtjevOcjenaRs rs = new McardGrZahtjevOcjenaRs(dao.daoFind(value));
        return rs;
    }
  
	public McardGrZahtjevRs izvjestaji(McardGrZahtjevVo value)  {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
		return dao.daoIzvjestaji(value);
	}
    public McardGrZahtjevPrivitakRs procitajSvePrivitke(McardGrZahtjevPrivitakVo value)  {
    	McardGrZahtjevPrivitakJdbc dao = new McardGrZahtjevPrivitakJdbc();
    	McardGrZahtjevPrivitakRs rs = new McardGrZahtjevPrivitakRs(dao.daoFind(value));
		return rs;
    }
    public McardGrZahtjevPrivitakVo dodajPrivitak(McardGrZahtjevPrivitakVo value)  {
    	McardGrZahtjevPrivitakJdbc dao = new McardGrZahtjevPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public McardGrZahtjevPrivitakVo citajPrivitak(McardGrZahtjevPrivitakVo value)  {
    	McardGrZahtjevPrivitakJdbc dao = new McardGrZahtjevPrivitakJdbc();
        return new McardGrZahtjevPrivitakVo(dao.daoLoad(value));
    }
    public McardGrZahtjevPrivitakVo brisiPrivitak(McardGrZahtjevPrivitakVo value)  {
    	McardGrZahtjevPrivitakJdbc dao = new McardGrZahtjevPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
    public OsnovniRs procitajSvePartijeStednje(OsnovniVo value)	 {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
        return dao.daoFindSvePartijeStednje(value);
	}
	public OsnovniRs procitajSvePartijeMcard(OsnovniVo value)	 {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
        return dao.daoFindSvePartijeMcard(value);
	}
	public OsnovniRs procitajPartijeTekuci(OsnovniVo value)	 {
		McardGrZahtjevJdbc dao = new McardGrZahtjevJdbc();
        return dao.daoFindPartijaTekuci(value);
	}
	public McardSifrarnikRs procitajMcardGrSifrarnik(McardSifrarnikVo value)  {
		McardGrSifrarnikJdbc dao = new McardGrSifrarnikJdbc();
		return new McardSifrarnikRs(dao.daoFind(value));
	}
}
