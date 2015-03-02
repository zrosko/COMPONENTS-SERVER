package hr.adriacomsoftware.app.server.crm.kontakt.facade;

import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktAktivnostRs;
import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktAktivnostVo;
import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktPredmetRs;
import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktPredmetVo;
import hr.adriacomsoftware.app.common.crm.kontakt.dto.KontaktPrivitakVo;
import hr.adriacomsoftware.app.common.crm.kontakt.facade.CrmKontaktFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc.KontaktAktivnostJdbc;
import hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc.KontaktKlijentJdbc;
import hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc.KontaktPredmetJdbc;
import hr.adriacomsoftware.app.server.crm.kontakt.da.jdbc.KontaktPrivitakJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.transaction.AS2Transaction;


public final class CrmKontaktFacadeServer extends AS2FacadeServerLayer
		implements CrmKontaktFacade {

	private static CrmKontaktFacadeServer _instance = null;
	public static CrmKontaktFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new CrmKontaktFacadeServer();
		}
		return _instance;
	}
	private CrmKontaktFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	/************* FACADE SERVER  kontakt_klijent ************/
	
	public AS2RecordList procitajSveKontaktPredmet(AS2Record value)  {
		KontaktKlijentJdbc dao = new KontaktKlijentJdbc();
		return dao.daoFindListuKlijenata(value);
	}

	/************* FACADE SERVER  kontakt_predmet ************/
	
	public KontaktPredmetVo brisiKontaktPredmet(KontaktPredmetVo value)  {
		KontaktPredmetJdbc dao = new KontaktPredmetJdbc();
		dao.daoRemove(value);
		return value;
	}
	public KontaktPredmetVo dodajKontaktPredmet(KontaktPredmetVo value)  {
		KontaktPredmetJdbc dao = new KontaktPredmetJdbc();
		AS2Record res = dao.daoCreate(value);
		return new KontaktPredmetVo(res);
	}
	public KontaktPredmetVo azurirajKontaktPredmet(KontaktPredmetVo value)  {
		KontaktPredmetJdbc dao = new KontaktPredmetJdbc();
		AS2Record res = dao.daoStore(value);
		return new KontaktPredmetVo(res);
	}
	public KontaktPredmetRs procitajSveKontaktPredmet(KontaktPredmetVo value)  {
		KontaktPredmetJdbc dao = new KontaktPredmetJdbc();
		return new KontaktPredmetRs(dao.daoFind(value));
	}
	public AS2RecordList listajSveKontaktPredmet()  {
		return new AS2RecordList();
	}
	public KontaktPredmetRs pretraziKontaktPredmet(KontaktPredmetVo value)  {
		KontaktPredmetJdbc dao = new KontaktPredmetJdbc();
		return new KontaktPredmetRs(dao.daoFindListuPredmeta(value));
////		BlokadaPoJdbc dao = new BlokadaPoJdbc();
//		return new KontaktPredmetRs(dao.daoFindListuBlokadaPo(value));
	}

	/************* FACADE SERVER  kontakt_aktivnost ************/

	public KontaktAktivnostVo brisiKontaktAktivnost(KontaktAktivnostVo value)  {
		KontaktAktivnostJdbc dao = new KontaktAktivnostJdbc();
		dao.daoRemove(value);
		return value;
	}
	public KontaktAktivnostVo dodajKontaktAktivnost(KontaktAktivnostVo value)  {
		KontaktAktivnostJdbc dao = new KontaktAktivnostJdbc();
		AS2User user = (AS2User) value.getAsObject(AS2Constants.USER_OBJ);
		value.set("korisnik", user.get("korisnik"));
		value.setCalendarAsDateString(AS2Date.getTodayAsCalendar(),"id_temp");
		dao.daoCreate(value);
		AS2Transaction.commit();
		AS2Transaction.begin();
		value.set("id_aktivnosti", dao.daoZadnjiIdAktivnosti(value));
		if (value.get("@@privitak_dodaj").equals("true")) {
			dodajPrivitak(new KontaktPrivitakVo(value));
			value.set("@@privitak_dodaj", "false");
		}
		return value;
	}
	public KontaktAktivnostVo azurirajKontaktAktivnost(KontaktAktivnostVo value)  {
		KontaktAktivnostJdbc dao = new KontaktAktivnostJdbc();
		AS2User user = (AS2User)value.getAsObject(AS2Constants.USER_OBJ);
        value.set("korisnik",user.get("korisnik"));
		dao.daoStore(value);
		if(value.get("@@privitak_dodaj").equals("true")){
			dodajPrivitak(new KontaktPrivitakVo(value));
			value.set("@@privitak_dodaj","false");
		}else if(value.get("@@privitak_ukloni").equals("true")){
    		brisiPrivitak(new KontaktPrivitakVo(value));
    		value.set("@@privitak_ukloni","false");
		}
		return value;
	}
	public KontaktAktivnostRs procitajSveKontaktAktivnost(KontaktAktivnostVo value)  {
		KontaktAktivnostJdbc dao = new KontaktAktivnostJdbc();
		return new KontaktAktivnostRs(dao.daoFindListuAktivnosti(value));
	}
	public AS2RecordList listajSveKontaktAktivnost()  {
		return new AS2RecordList();
	}
	public KontaktAktivnostRs pretraziKontaktAktivnost(KontaktAktivnostVo value)  {
		KontaktAktivnostJdbc dao = new KontaktAktivnostJdbc();
		return new KontaktAktivnostRs(dao.daoFindListuAktivnosti(value));
	}

	/************* FACADE SERVER  kontakt_privitak ************/

	public KontaktPrivitakVo dodajPrivitak(KontaktPrivitakVo value)  {
		KontaktPrivitakJdbc dao = new KontaktPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public KontaktPrivitakVo citajPrivitak(KontaktPrivitakVo value)  {
    	KontaktPrivitakJdbc dao = new KontaktPrivitakJdbc();
        return new KontaktPrivitakVo(dao.daoLoad(value));
    }
    public KontaktPrivitakVo brisiPrivitak(KontaktPrivitakVo value)  {
    	KontaktPrivitakJdbc dao = new KontaktPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
	
	/************* FACADE SERVER  zajednicko ************/
	
	public OsnovniRs procitajCrmKontaktSifre(OsnovniVo value) {
		KontaktPredmetJdbc dao = new KontaktPredmetJdbc();
		return dao.daoProcitajSifre(value);
	}
	public OsnovniRs izvjestaji(OsnovniVo value)  {
		KontaktPredmetJdbc dao = new KontaktPredmetJdbc();
		return dao.daoIzvjestaji(value);
	}
	
}
