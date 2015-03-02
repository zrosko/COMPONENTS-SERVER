package hr.adriacomsoftware.app.server.gradani.naplata.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.OpomenaRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.OpomenaVo;
import hr.adriacomsoftware.app.common.gradani.naplata.facade.NaplataGradaniFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc.OpomenaGradanaJdbc;
import hr.adriacomsoftware.app.server.gradani.naplata.da.jdbc.NaplataGradaniJdbc;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class NaplataGradaniFacadeServer  extends AS2FacadeServerLayer
		implements NaplataGradaniFacade {
    
    private static NaplataGradaniFacadeServer _instance = null;
    public static NaplataGradaniFacadeServer getInstance() {
        if (_instance == null) {
            _instance = new NaplataGradaniFacadeServer();
        }
        return _instance;
    }
    private NaplataGradaniFacadeServer() {
    	AS2Context.setSingletonReference(this);
    }
	public OpomenaRs izvjestajOpomeneKredita(OpomenaVo value)  {
	    OpomenaGradanaJdbc dao = new OpomenaGradanaJdbc();
	    return new OpomenaRs(dao.daoListaOpomena(value));
	 }   
    public OsnovniRs pregledUgovora(OsnovniVo value)  {
        NaplataGradaniJdbc dao = new NaplataGradaniJdbc(); 
        return dao.daoPregledUgovora(value);
    }
    public OpomenaRs iskljuciOpomene(OpomenaVo value)  {
        OpomenaGradanaJdbc dao = new OpomenaGradanaJdbc();
        dao.daoIskljuciUkljuciOpomene(value,OpomenaVo.VALID_IND_NE);
        return new OpomenaRs(); 
    }
    public OpomenaRs ukljuciOpomene(OpomenaVo value)  {
        OpomenaGradanaJdbc dao = new OpomenaGradanaJdbc();
        dao.daoIskljuciUkljuciOpomene(value,OpomenaVo.VALID_IND_DA);
        return new OpomenaRs(); 
    }
    public OsnovniRs pregledPrometa(OsnovniVo value)  {
        return null;
    }
    public OsnovniRs pregledBoniteta(OsnovniVo value)  {
        return null;
    }
    public OsnovniRs pregledStanja(OsnovniVo value)  {
        return null;
    }
    public OsnovniRs pregledVezanihOsoba(OsnovniVo value)  {
        return null;
    }
    public OsnovniRs pregledRezervacija(OsnovniVo value)  {
        return null;
    }
    public OsobaRs procitajSveGradaneNaplata()  {
	    BankarskiJdbc dao = new BankarskiJdbc();
	    AS2Record criteria = new AS2Record();
	    criteria.set(AS2Constants.ORDER_BY_CLAUSE, "order by broj_opomene desc");
	    OsobaRs set = new OsobaRs(dao.daoFindFromView(criteria, "bi_view_gr_naplata_pogled"));
	    return set;
    }
    public OsobaRs pronadiSveGradaneNaplata(OsnovniVo value)  {
        NaplataGradaniJdbc dao = new NaplataGradaniJdbc(); 
        return dao.daoPronadiOsobe(value);
    }
 }