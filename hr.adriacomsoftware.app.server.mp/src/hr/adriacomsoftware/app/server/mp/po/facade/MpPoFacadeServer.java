package hr.adriacomsoftware.app.server.mp.po.facade;


import hr.adriacomsoftware.app.common.mp.po.dto.MpPoOsobaRs;
import hr.adriacomsoftware.app.common.mp.po.dto.MpPoOsobaVo;
import hr.adriacomsoftware.app.common.mp.po.dto.MpPoPartijaRs;
import hr.adriacomsoftware.app.common.mp.po.dto.MpPoPartijaVo;
import hr.adriacomsoftware.app.common.mp.po.dto.MpPoPrometRs;
import hr.adriacomsoftware.app.common.mp.po.dto.MpPoPrometVo;
import hr.adriacomsoftware.app.common.mp.po.facade.MpPoFacade;
import hr.adriacomsoftware.app.server.mp.po.da.jdbc.MpPoOsobaJdbc;
import hr.adriacomsoftware.app.server.mp.po.da.jdbc.MpPoPartijaJdbc;
import hr.adriacomsoftware.app.server.mp.po.da.jdbc.MpPoPrometJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class MpPoFacadeServer extends AS2FacadeServerLayer 
		implements MpPoFacade {

	private static MpPoFacadeServer _instance = null;
	public static MpPoFacadeServer getInstance() {
		if (_instance == null){
			_instance = new MpPoFacadeServer();
		}
		return _instance;
	}
	private MpPoFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
	public MpPoOsobaVo brisiMpPoOsoba(MpPoOsobaVo value)  {
		MpPoOsobaJdbc dao = new MpPoOsobaJdbc();
		dao.daoRemove(value);
		return value;
	}
	public MpPoOsobaVo dodajMpPoOsoba(MpPoOsobaVo value)  {
		MpPoOsobaJdbc dao = new MpPoOsobaJdbc();
		AS2Record res = dao.daoCreate(value);
		return new MpPoOsobaVo(res);
	}
	public MpPoOsobaVo azurirajMpPoOsoba(MpPoOsobaVo value)  {
		MpPoOsobaJdbc dao = new MpPoOsobaJdbc();
		AS2Record res = dao.daoStore(value);
		return new MpPoOsobaVo(res);
	}
	public MpPoOsobaRs procitajSveMpPoOsoba(MpPoOsobaVo value)  {
		MpPoOsobaJdbc dao = new MpPoOsobaJdbc();
		return new MpPoOsobaRs(dao.daoFind(value));
	}
	public AS2RecordList listajSveMpPoOsoba()  {
		return new AS2RecordList();
	}
	public MpPoOsobaRs pretraziMpPoOsoba(MpPoOsobaVo value)  {
		MpPoOsobaJdbc dao = new MpPoOsobaJdbc();
		return new MpPoOsobaRs(dao.daoFind(value));
	}
	public MpPoPartijaVo brisiMpPoPartija(MpPoPartijaVo value)  {
		MpPoPartijaJdbc dao = new MpPoPartijaJdbc();
		dao.daoRemove(value);
		return value;
	}
	public MpPoPartijaVo dodajMpPoPartija(MpPoPartijaVo value)  {
		MpPoPartijaJdbc dao = new MpPoPartijaJdbc();
		AS2Record res = dao.daoCreate(value);
		return new MpPoPartijaVo(res);
	}
	public MpPoPartijaVo azurirajMpPoPartija(MpPoPartijaVo value)  {
		MpPoPartijaJdbc dao = new MpPoPartijaJdbc();
		AS2Record res = dao.daoStore(value);
		return new MpPoPartijaVo(res);
	}
	public MpPoPartijaRs procitajSveMpPoPartija(MpPoPartijaVo value)  {
		MpPoPartijaJdbc dao = new MpPoPartijaJdbc();
		return new MpPoPartijaRs(dao.daoFind(value));
	}
	public AS2RecordList listajSveMpPoPartija()  {
		return new AS2RecordList();
	}
	public MpPoPartijaRs pretraziMpPoPartija(MpPoPartijaVo value)  {
		MpPoPartijaJdbc dao = new MpPoPartijaJdbc();
		return new MpPoPartijaRs(dao.daoFind(value));
	}
	public MpPoPartijaVo provjeriMpPoPartija(MpPoPartijaVo value)  {
		MpPoPartijaJdbc dao = new MpPoPartijaJdbc();
		AS2Record res = dao.daoProvjeriPartiju(value);
		return new MpPoPartijaVo(res);
	}
	public MpPoPrometVo brisiMpPoPromet(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setIspravno("0");
		AS2Record res = dao.daoStore(value);
		return new MpPoPrometVo(res);
	}
	public MpPoPrometVo dodajMpPoPromet(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setOrganizacijskaJedinica("14040");
		value.setIspravno("2");
		AS2Record res = dao.daoCreate(value);
		return new MpPoPrometVo(res);
	}
	public MpPoPrometVo azurirajMpPoPromet(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setIspravno("2");
		value.setOrganizacijskaJedinica("14040");
		AS2Record res = dao.daoStore(value);
		return new MpPoPrometVo(res);
	}
	public MpPoPrometRs procitajSveMpPoPromet(MpPoPrometVo value)  {
		return new MpPoPrometRs(/*dao.daoFindListajPromete(value)*/);
	}
	public AS2RecordList listajSveMpPoPromet()  {
		return new AS2RecordList();
	}
	public MpPoPrometRs pretraziMpPoPromet(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		if(value.get("@novo").equals("true")){
			AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
			value.setOperaterZadnjeIzmjene(user.get("korisnik"));
			value.setIspravno("2");
		}else
			value.setIspravno("1");
		return new MpPoPrometRs(dao.daoFindPronadiPromete(value));
	}
	public MpPoPrometRs procitajSveMpPoPrometGrupno(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		return new MpPoPrometRs(dao.daoFindListajKnjizenja(value));
	}
	public MpPoPrometRs pretraziMpPoPrometGrupno(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		return new MpPoPrometRs(dao.daoFindPronadiKnjizenja(value));
	}
	public void proknjiziMpPoPromet(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		MpPoPrometRs rs = dao.daoFindPronadiPrometeKratki(value);
		dao.daoStoreMany(rs);
	}
	public MpPoPrometVo validate(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		if(value.get("@validate").equals("vrsta_iznosa")){
			AS2Record res = dao.daoProvjeriVrstuIznosa(value);
			return new MpPoPrometVo(res);
		}else
			return value;
	}
	public MpPoPrometRs izvjestaji(MpPoPrometVo value)  {
		MpPoPrometJdbc dao = new MpPoPrometJdbc();
		return dao.daoIzvjestaji(value);
	}
 }
