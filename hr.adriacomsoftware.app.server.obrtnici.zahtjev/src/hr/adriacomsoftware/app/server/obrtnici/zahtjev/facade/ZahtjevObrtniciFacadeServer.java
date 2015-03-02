package hr.adriacomsoftware.app.server.obrtnici.zahtjev.facade;

import hr.adriacomsoftware.app.common.obrtnici.zahtjev.facade.ZahtjevObrtniciFacade;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.ZahtjevPravnaOsobaVo;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.da.jdbc.PoZahZahtjevJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class ZahtjevObrtniciFacadeServer extends AS2FacadeServerLayer
		implements ZahtjevObrtniciFacade {

	private static ZahtjevObrtniciFacadeServer _instance = null;
	public static ZahtjevObrtniciFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new ZahtjevObrtniciFacadeServer();
		}
		return _instance;
	}
	private ZahtjevObrtniciFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public ZahtjevPravnaOsobaRs pronadiSveZahtjeve(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        value.set("@obrtnici","1");//bilo sto u polju daje znak da cita samo obrtnike
        return dao.daoPronadiZahtjeve(value, true);
    }
    public ZahtjevPravnaOsobaRs citajSveZahtjeve(ZahtjevPravnaOsobaVo value)  {
        PoZahZahtjevJdbc dao = new PoZahZahtjevJdbc();
        value.set("@obrtnici","1");//bilo sto u polju daje znak da cita samo obrtnike
        return dao.daoPronadiZahtjeve(value, false);
    }
 }
