package hr.adriacomsoftware.app.server.gradani.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.gradani.dto.VezanaOsobaRs;
import hr.adriacomsoftware.app.common.gradani.facade.GradaniFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.OsobaJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PartijaGradanaJdbc;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.VezanaOsobaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class GradaniFacadeServer extends AS2FacadeServerLayer
		implements GradaniFacade {

	private static GradaniFacadeServer _instance = null;
	public static GradaniFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new GradaniFacadeServer();
		}
		return _instance;
	}
	private GradaniFacadeServer(){
		AS2Context.setSingletonReference(this);
	}

    public PartijaRs citajSvePartijeOsobe(OsobaVo value)  {
        PartijaGradanaJdbc dao = new PartijaGradanaJdbc();
        return dao.daoFindListaOtvorenihPartijaOsobe(value);
    }
    public VezanaOsobaRs citajSvePovezaneOsobeOsobe(OsobaVo value)  {
        VezanaOsobaJdbc dao = new VezanaOsobaJdbc();
        return dao.daoFindSveVezaneOsobe(value);
    }
    public OsobaRs citajPodatkeKlijenta(OsobaVo value)  {
        OsobaRs j2eers = new OsobaRs();
        OsobaJdbc dao = new OsobaJdbc();
        
        if(value.exists("@izbor_a")){
            j2eers = dao.daoFindPodatkeKlijenta(value);
        }
        if(value.exists("@izbor_b")){
            //podaci o računima
        }
        if(value.exists("@izbor_c")){
            //ostali podaci
        }
        return j2eers;
    }
    public OsnovniRs citajPrometeOsobe(OsnovniVo value)  {
        OsobaJdbc dao = new OsobaJdbc();
        return dao.daoPrometOsobe(value);
    }
}
