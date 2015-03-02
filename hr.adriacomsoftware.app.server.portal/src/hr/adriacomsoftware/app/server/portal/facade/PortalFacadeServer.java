package hr.adriacomsoftware.app.server.portal.facade;

import hr.adriacomsoftware.app.server.portal.da.jdbc.PortalObavijestiJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.security.authorization.da.jdbc.J2EERBACKorisnikJdbc;

import org.jsoup.Jsoup;


public final class PortalFacadeServer {

	private static PortalFacadeServer _instance = new PortalFacadeServer();

	public static PortalFacadeServer getInstance() {
		return _instance;
	}

	private PortalFacadeServer() {
		AS2Context.setSingletonReference(this);
	}
	
	private String getUserName(AS2Record value){
		AS2User user = (AS2User) value.getAsObject(AS2Constants.USER_OBJ);
		return user.getUserName();
	}
	
    public AS2RecordList procitajAplikacije(AS2User value)  {
    	J2EERBACKorisnikJdbc _dao = new J2EERBACKorisnikJdbc();
    	return _dao.daoFindApplications(value);
    }
	
	//OBAVIJESTI
	public AS2RecordList procitajSveObavijesti(AS2Record value)  {
		PortalObavijestiJdbc dao = new PortalObavijestiJdbc();
		return dao.daoFindObavijesti(value);
	}
	public AS2Record dodajObavijest(AS2Record value)  {
		PortalObavijestiJdbc dao = new PortalObavijestiJdbc();
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
		value.set("korisnik", getUserName(value));
		value.set("obavijest",Jsoup.parse(value.get("obavijest_html")).text());
		return dao.daoLoadObavijest(dao.daoCreate(value));
	}
	public AS2Record azurirajObavijest(AS2Record value)  {
		PortalObavijestiJdbc dao = new PortalObavijestiJdbc();
		value.set("vrijeme_izmjene", AS2Date.getCurrentDateTimeAsString());
		value.set("korisnik", getUserName(value));
		value.set("obavijest",Jsoup.parse(value.get("obavijest_html")).text());
		return dao.daoLoadObavijest(dao.daoStore(value));
	}
	
	public AS2Record brisiObavijest(AS2Record value)  {
		PortalObavijestiJdbc dao = new PortalObavijestiJdbc();
		value.set("ispravno", "0");
		return dao.daoLoadObavijest(dao.daoStore(value));
	}
}
