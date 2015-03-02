package hr.adriacomsoftware.app.server.mp.gk.facade;


import hr.adriacomsoftware.app.common.mp.gk.dto.MpGkKnjizenjaRs;
import hr.adriacomsoftware.app.common.mp.gk.dto.MpGkKnjizenjaVo;
import hr.adriacomsoftware.app.common.mp.gk.facade.MpGkFacade;
import hr.adriacomsoftware.app.server.mp.gk.da.jdbc.MpGkKnjizenjaJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class MpGkFacadeServer extends AS2FacadeServerLayer 
		implements MpGkFacade {

	private static MpGkFacadeServer _instance = null;
	public static MpGkFacadeServer getInstance() {
		if (_instance == null){
			_instance = new MpGkFacadeServer();
		}
		return _instance;
	}
	private MpGkFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
	public MpGkKnjizenjaVo brisiMpGkKnjizenja(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setIspravno("0");
		AS2Record res = dao.daoStore(value);
		return new MpGkKnjizenjaVo(res);
	}
	public MpGkKnjizenjaVo dodajMpGkKnjizenja(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setOrganizacijskaJedinica("13000");
		value.setIspravno("2");
		AS2Record res = dao.daoCreate(value);
		return new MpGkKnjizenjaVo(res);
	}
	public MpGkKnjizenjaVo azurirajMpGkKnjizenja(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		value.setVrijemeZadnjeIzmjene(AS2Date.getTodayAsCalendar());
		value.setIspravno("2");
		value.setOrganizacijskaJedinica("13000");
		AS2Record res = dao.daoStore(value);
		return new MpGkKnjizenjaVo(res);
	}
	public MpGkKnjizenjaRs procitajSveMpGkKnjizenja(MpGkKnjizenjaVo value)  {
		//MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		return new MpGkKnjizenjaRs(/*dao.daoFindListajPromete(value)*/);
	}
	public AS2RecordList listajSveMpGkKnjizenja()  {
		//MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		//return dao.daoFindListu(MpGkKnjizenjaJdbc.SQL_LISTA);
		return new AS2RecordList();
	}
	public MpGkKnjizenjaRs pretraziMpGkKnjizenja(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		if(value.get("@novo").equals("true")){
			AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
			value.setOperaterZadnjeIzmjene(user.get("korisnik"));
			value.setIspravno("2");
		}else
			value.setIspravno("1");
		return new MpGkKnjizenjaRs(dao.daoFindPronadiKnjizenja(value));
	}
	public MpGkKnjizenjaRs procitajSveMpGkKnjizenjaGrupno(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		return new MpGkKnjizenjaRs(dao.daoFindListajKnjizenja(value));
	}
	public MpGkKnjizenjaRs pretraziMpGkKnjizenjaGrupno(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		return new MpGkKnjizenjaRs(dao.daoFindPronadiKnjizenja(value));
	}
	public void proknjiziMpGkKnjizenja(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setOperaterZadnjeIzmjene(user.get("korisnik"));
		MpGkKnjizenjaRs rs = dao.daoFindPronadiPrometeKratki(value);
		dao.daoStoreMany(rs);
	}
	public MpGkKnjizenjaVo validate(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		if(value.get("@validate").equals("vrsta_iznosa")){
			AS2Record res = dao.daoProvjeriVrstuIznosa(value);
			return new MpGkKnjizenjaVo(res);
		}else
			return value;
	}
	public MpGkKnjizenjaRs izvjestaji(MpGkKnjizenjaVo value)  {
		MpGkKnjizenjaJdbc dao = new MpGkKnjizenjaJdbc();
		return dao.daoIzvjestaji(value);
	}
 }
