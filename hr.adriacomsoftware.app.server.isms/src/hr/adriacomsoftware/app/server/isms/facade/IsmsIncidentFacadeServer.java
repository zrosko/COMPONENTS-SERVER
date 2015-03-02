package hr.adriacomsoftware.app.server.isms.facade;

import hr.adriacomsoftware.app.common.isms.dto.IsmsIncidentRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsIncidentVo;
import hr.adriacomsoftware.app.common.isms.facade.IsmsIncidentFacade;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsIncidentJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class IsmsIncidentFacadeServer extends AS2FacadeServerLayer		
		implements IsmsIncidentFacade {

	private static IsmsIncidentFacadeServer _instance = null;
	public static IsmsIncidentFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new IsmsIncidentFacadeServer();
		}
		return _instance;
	}
	private IsmsIncidentFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	//incident
    public IsmsIncidentRs procitajSveIncidente(IsmsIncidentVo value)  {
        IsmsIncidentJdbc dao = new IsmsIncidentJdbc();
        IsmsIncidentRs rs = new IsmsIncidentRs(dao.daoFind(value));
		return rs;
    }
    public IsmsIncidentVo azurirajIncident(IsmsIncidentVo value)  {
        IsmsIncidentJdbc dao = new IsmsIncidentJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsIncidentVo dodajIncident(IsmsIncidentVo value)  {
        AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
        String jmbg = user.get("id_osobe");
        value.setOsobaPrijave(jmbg);
        IsmsIncidentJdbc dao = new IsmsIncidentJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsIncidentVo brisiIncident(IsmsIncidentVo value)  {
        IsmsIncidentJdbc dao = new IsmsIncidentJdbc();
        IsmsIncidentVo vo = new IsmsIncidentVo();
        vo.setIdIncidenta(value.getIdIncidenta());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
}
