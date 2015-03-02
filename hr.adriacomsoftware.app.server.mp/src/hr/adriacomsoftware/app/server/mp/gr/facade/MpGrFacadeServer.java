package hr.adriacomsoftware.app.server.mp.gr.facade;


import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrOsobaRs;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrOsobaVo;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrPartijaRs;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrPartijaVo;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrPrometRs;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrPrometVo;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrVezanaOsobaRs;
import hr.adriacomsoftware.app.common.mp.gr.dto.MpGrVezanaOsobaVo;
import hr.adriacomsoftware.app.common.mp.gr.facade.MpGrFacade;
import hr.adriacomsoftware.app.server.mp.gr.da.jdbc.MpGrOsobaJdbc;
import hr.adriacomsoftware.app.server.mp.gr.da.jdbc.MpGrPartijaJdbc;
import hr.adriacomsoftware.app.server.mp.gr.da.jdbc.MpGrPrometJdbc;
import hr.adriacomsoftware.app.server.mp.gr.da.jdbc.MpGrVezanaOsobaJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class MpGrFacadeServer extends AS2FacadeServerLayer 
		implements MpGrFacade {

	private static MpGrFacadeServer _instance = null;
	public static MpGrFacadeServer getInstance() {
		if (_instance == null){
			_instance = new MpGrFacadeServer();
		}
		return _instance;
	}
	private MpGrFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
	public MpGrOsobaVo brisiMpGrOsoba(MpGrOsobaVo value)  {
		MpGrOsobaJdbc dao = new MpGrOsobaJdbc();
		dao.daoRemove(value);
		return value;
	}
	public MpGrOsobaVo dodajMpGrOsoba(MpGrOsobaVo value)  {
		MpGrOsobaJdbc dao = new MpGrOsobaJdbc();
		AS2Record res = dao.daoCreate(value);
		return new MpGrOsobaVo(res);
	}
	public MpGrOsobaVo azurirajMpGrOsoba(MpGrOsobaVo value)  {
		MpGrOsobaJdbc dao = new MpGrOsobaJdbc();
		AS2Record res = dao.daoStore(value);
		return new MpGrOsobaVo(res);
	}
	public MpGrOsobaRs procitajSveMpGrOsoba(MpGrOsobaVo value)  {
		MpGrOsobaJdbc dao = new MpGrOsobaJdbc();
		return new MpGrOsobaRs(dao.daoFind(value));
	}
	public AS2RecordList listajSveMpGrOsoba()  {
		//MpGrOsobaJdbc dao = new MpGrOsobaJdbc();
		//return dao.daoFindListu(MpGrOsobaJdbc.SQL_LISTA);
		return new AS2RecordList();
	}
	public MpGrOsobaRs pretraziMpGrOsoba(MpGrOsobaVo value)  {
		MpGrOsobaJdbc dao = new MpGrOsobaJdbc();
		return new MpGrOsobaRs(dao.daoFind(value));
	}
	public MpGrPartijaVo brisiMpGrPartija(MpGrPartijaVo value)  {
		MpGrPartijaJdbc dao = new MpGrPartijaJdbc();
		dao.daoRemove(value);
		return value;
	}
	public MpGrPartijaVo dodajMpGrPartija(MpGrPartijaVo value)  {
		MpGrPartijaJdbc dao = new MpGrPartijaJdbc();
		AS2Record res = dao.daoCreate(value);
		return new MpGrPartijaVo(res);
	}
	public MpGrPartijaVo azurirajMpGrPartija(MpGrPartijaVo value)  {
		MpGrPartijaJdbc dao = new MpGrPartijaJdbc();
		AS2Record res = dao.daoStore(value);
		return new MpGrPartijaVo(res);
	}
	public MpGrPartijaRs procitajSveMpGrPartija(MpGrPartijaVo value)  {
		MpGrPartijaJdbc dao = new MpGrPartijaJdbc();
		return new MpGrPartijaRs(dao.daoFind(value));
	}
	public AS2RecordList listajSveMpGrPartija()  {
		//MpGrPartijaJdbc dao = new MpGrPartijaJdbc();
		//return dao.daoFindListu(MpGrPartijaJdbc.SQL_LISTA);
		return new AS2RecordList();
	}
	public MpGrPartijaRs pretraziMpGrPartija(MpGrPartijaVo value)  {
		MpGrPartijaJdbc dao = new MpGrPartijaJdbc();
		return new MpGrPartijaRs(dao.daoFind(value));
	}
	public MpGrPartijaVo provjeriMpGrPartija(MpGrPartijaVo value)  {
		MpGrPartijaJdbc dao = new MpGrPartijaJdbc();
		AS2Record res = dao.daoProvjeriPartiju(value);
		return new MpGrPartijaVo(res);
	}
	public MpGrVezanaOsobaVo brisiMpGrVezanaOsoba(MpGrVezanaOsobaVo value)  {
		MpGrVezanaOsobaJdbc dao = new MpGrVezanaOsobaJdbc();
		dao.daoRemove(value);
		return value;
	}
	public MpGrVezanaOsobaVo dodajMpGrVezanaOsoba(MpGrVezanaOsobaVo value)  {
		MpGrVezanaOsobaJdbc dao = new MpGrVezanaOsobaJdbc();
		AS2Record res = dao.daoCreate(value);
		return new MpGrVezanaOsobaVo(res);
	}
	public MpGrVezanaOsobaVo azurirajMpGrVezanaOsoba(MpGrVezanaOsobaVo value)  {
		MpGrVezanaOsobaJdbc dao = new MpGrVezanaOsobaJdbc();
		AS2Record res = dao.daoStore(value);
		return new MpGrVezanaOsobaVo(res);
	}
	public MpGrVezanaOsobaRs procitajSveMpGrVezanaOsoba(MpGrVezanaOsobaVo value)  {
		MpGrVezanaOsobaJdbc dao = new MpGrVezanaOsobaJdbc();
		return new MpGrVezanaOsobaRs(dao.daoFind(value));
	}
	public AS2RecordList listajSveMpGrVezanaOsoba()  {
		//MpGrVezanaOsobaJdbc dao = new MpGrVezanaOsobaJdbc();
		//return dao.daoFindListu(MpGrVezanaOsobaJdbc.SQL_LISTA);
		return new AS2RecordList();
	}
	public MpGrVezanaOsobaRs pretraziMpGrVezanaOsoba(MpGrVezanaOsobaVo value)  {
		MpGrVezanaOsobaJdbc dao = new MpGrVezanaOsobaJdbc();
		return new MpGrVezanaOsobaRs(dao.daoFind(value));
	}
	public MpGrPrometVo brisiMpGrPromet(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setIspravno("0");
		AS2Record res = dao.daoStore(value);
		return new MpGrPrometVo(res);
	}
	public MpGrPrometVo dodajMpGrPromet(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setOrganizacijskaJedinica("14040");
		value.setIspravno("2");
		AS2Record res = dao.daoCreate(value);
		return new MpGrPrometVo(res);
	}
	public MpGrPrometVo azurirajMpGrPromet(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setIspravno("2");
		value.setOrganizacijskaJedinica("14040");
		AS2Record res = dao.daoStore(value);
		return new MpGrPrometVo(res);
	}
	public MpGrPrometRs procitajSveMpGrPromet(MpGrPrometVo value)  {
		//MpGrPrometJdbc dao = new MpGrPrometJdbc();
		return new MpGrPrometRs(/*dao.daoFindListajPromete(value)*/);
	}
	public AS2RecordList listajSveMpGrPromet()  {
		//MpGrPrometJdbc dao = new MpGrPrometJdbc();
		//return dao.daoFindListu(MpGrPrometJdbc.SQL_LISTA);
		return new AS2RecordList();
	}
	public MpGrPrometRs pretraziMpGrPromet(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		if(value.get("@novo").equals("true")){
			AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
			value.setOperaterZadnjeIzmjene(user.get("korisnik"));
			value.setIspravno("2");
		}else
			value.setIspravno("1");
		return new MpGrPrometRs(dao.daoFindPronadiPromete(value));
	}
	public MpGrPrometRs procitajSveMpGrPrometGrupno(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		return new MpGrPrometRs(dao.daoFindListajKnjizenja(value));
	}
	public MpGrPrometRs pretraziMpGrPrometGrupno(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		return new MpGrPrometRs(dao.daoFindPronadiKnjizenja(value));
	}
	public void proknjiziMpGrPromet(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		dao.daoKnjizenje(value);
	}
	public MpGrPrometVo validate(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		if(value.get("@validate").equals("vrsta_iznosa")){
			AS2Record res = dao.daoProvjeriVrstuIznosa(value);
			return new MpGrPrometVo(res);
		}else
			return value;
	}
	public MpGrPrometRs izvjestaji(MpGrPrometVo value)  {
		MpGrPrometJdbc dao = new MpGrPrometJdbc();
		return dao.daoIzvjestaji(value);
	}
 }
