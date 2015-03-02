package hr.adriacomsoftware.app.server.pravneosobe.zahtjev.garancije.facade;

import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.garancije.dto.GarancijaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.garancije.dto.GarancijaVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.garancije.facade.GarancijeFacade;
import hr.adriacomsoftware.app.server.pravneosobe.zahtjev.garancije.da.jdbc.PoZahGarancijaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.common.validations.AS2ValidatorService;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.session.AS2SessionFactory;

import java.util.Calendar;


public final class GarancijeFacadeServer extends AS2FacadeServerLayer
		implements GarancijeFacade
{

	private static GarancijeFacadeServer _instance = null;
	public static GarancijeFacadeServer getInstance() {
		if (_instance == null){
			_instance = new GarancijeFacadeServer();
		}
		return _instance;
	}
	private GarancijeFacadeServer(){
		AS2Context.setSingletonReference(this);		
	}
    public GarancijaRs procitajSveGarancije(GarancijaVo value)  {
        PoZahGarancijaJdbc dao = new PoZahGarancijaJdbc();
        return dao.daoProcitajGarancije(value);
    }
    public GarancijaRs pronadiGarancije(GarancijaVo value)  {
        PoZahGarancijaJdbc dao = new PoZahGarancijaJdbc();
        return dao.daoPronadiGarancije(value, true);
    }
    public GarancijaVo azurirajGaranciju(GarancijaVo value)  {
        AS2ValidatorService.getInstance().checkMandatory("GarancijeFacadeServer.dodajGaranciju", value); //$NON-NLS-1$
        PoZahGarancijaJdbc dao = new PoZahGarancijaJdbc();
        value.setDatumPromjene(AS2Date.getTodayAsCalendar());
        value.setOperaterPromjene(AS2SessionFactory.getInstance().getCurrentUser().get("id_osobe"));
        dao.daoStore(value);
        return value;
    }
    public GarancijaVo dodajGaranciju(GarancijaVo value)  {
        AS2ValidatorService.getInstance().checkMandatory("GarancijeFacadeServer.dodajGaranciju", value); //$NON-NLS-1$
        PoZahGarancijaJdbc dao = new PoZahGarancijaJdbc();
        int _redniBroj = dao.daoFindNextBrojGarancije(value);
        StringBuffer _brojGarancije = new StringBuffer();
        _brojGarancije.append(_redniBroj);
        _brojGarancije.append("/");
        _brojGarancije.append(value.getOrganizacijskaJedinica());
        _brojGarancije.append("/");
        _brojGarancije.append(value.getDatumIzdavanja().get(Calendar.YEAR)); 
        value.setBrojGarancije(_brojGarancije.toString());
        value.setRedniBroj(_redniBroj);
        dao.daoCreate(value);
        return value;
    }
    public GarancijaVo brisiGaranciju(GarancijaVo value)  {
        PoZahGarancijaJdbc dao = new PoZahGarancijaJdbc();
        GarancijaVo _vo = new GarancijaVo();
        _vo.setIdGarancije(value.getIdGarancije());
        _vo.setIspravnoNE();
        dao.daoStore(_vo);
        return _vo;
    }
}
