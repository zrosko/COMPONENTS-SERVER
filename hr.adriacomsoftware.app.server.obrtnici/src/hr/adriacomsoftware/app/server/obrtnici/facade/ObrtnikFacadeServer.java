package hr.adriacomsoftware.app.server.obrtnici.facade;


import hr.adriacomsoftware.app.common.obrtnici.facade.ObrtnikFacade;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PravnaOsobaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class ObrtnikFacadeServer extends AS2FacadeServerLayer 
		implements ObrtnikFacade {

	private static ObrtnikFacadeServer _instance = null;
	public static ObrtnikFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new ObrtnikFacadeServer();
		}
		return _instance;
	}
	private ObrtnikFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public PravnaOsobaRs citajSveObrtnike(PravnaOsobaVo value)  {
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        value.set("@obrtnici","1");//bilo sto u polju daje znak da cita samo obrtnike
        return dao.daoListaPravnihOsoba(value);
    }
    public PravnaOsobaRs pronadiSveObrtnike(PravnaOsobaVo value)  {
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        value.set("@obrtnici","1");//bilo sto u polju daje znak da cita samo obrtnike
        return dao.daoPronadiPravneOsobe(value);
    }
    public PravnaOsobaVo citajObrtnika(PravnaOsobaVo value)  {
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        return new PravnaOsobaVo(dao.daoLoad(value));        
    }
}

