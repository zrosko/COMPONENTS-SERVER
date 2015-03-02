package hr.adriacomsoftware.app.server.pravneosobe.krediti.facade;

import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.pravneosobe.krediti.facade.KreditniProgramFacade;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PartijaGradanaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.KreditniProgramJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PartijaPravnihOsobaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.exceptions.AS2BusinessLogicException;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class KreditniProgramFacadeServer extends AS2FacadeServerLayer
	implements KreditniProgramFacade {

	private static KreditniProgramFacadeServer _instance = null;
	public static KreditniProgramFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new KreditniProgramFacadeServer();
		}
		return _instance;
	}
	private KreditniProgramFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public OsnovniVo azurirajPartijuZaKreditniProgram(OsnovniVo value)  {
        KreditniProgramJdbc dao = new KreditniProgramJdbc();
        PartijaGradanaJdbc dao_po_partija = new PartijaGradanaJdbc();
        PartijaPravnihOsobaJdbc dao_gr_partija = new PartijaPravnihOsobaJdbc();
	    if (!dao_po_partija.daoFindIfExists(value))
	        if (!dao_gr_partija.daoFindIfExists(value))
	            throw new AS2BusinessLogicException("10018");
	    value.set("vrsta_programa","OB");
		dao.daoStore(value);
		value.set("brojac_partija", dao.daoFindCountExists(value));
		return value;
    }
    public OsnovniRs procitajSvePartijeZaKreditniProgram(OsnovniVo value)  {
        KreditniProgramJdbc dao = new KreditniProgramJdbc();
        OsnovniRs rs = new OsnovniRs(dao.daoFind(value));
		return rs;
    }
    public OsnovniVo dodajPartijuZaKreditniProgram(OsnovniVo value)  {
        KreditniProgramJdbc dao = new KreditniProgramJdbc();
        PartijaGradanaJdbc dao_po_partija = new PartijaGradanaJdbc();
        PartijaPravnihOsobaJdbc dao_gr_partija = new PartijaPravnihOsobaJdbc();
	    if (!dao_po_partija.daoFindIfExists(value))
	        if (!dao_gr_partija.daoFindIfExists(value))
	            throw new AS2BusinessLogicException("10018");
	    value.set("vrsta_programa","OB");
		dao.daoCreate(value);
		value.set("brojac_partija", dao.daoFindCountExists(value));
		return value;
    }
    public OsnovniVo brisiPartijuZaKreditniProgram(OsnovniVo value)  {
        KreditniProgramJdbc dao = new KreditniProgramJdbc();
		dao.daoRemove(value);
		return value;
    }    
}
