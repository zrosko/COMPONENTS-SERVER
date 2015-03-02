package hr.adriacomsoftware.app.server.pravneosobe.devize.facade;

import hr.adriacomsoftware.app.common.jb.dto.IzvjestajRs;
import hr.adriacomsoftware.app.common.jb.dto.IzvjestajVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaVo;
import hr.adriacomsoftware.app.common.jb.dto.RacunPartijeRs;
import hr.adriacomsoftware.app.common.pravneosobe.devize.facade.DevizePravnihOsobaFacade;
import hr.adriacomsoftware.app.common.pravneosobe.dto.IzvodRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.IzvodVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.IzvodiPravihOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.devize.da.jdbc.PartijaDepozitaPravnihOsobaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class DevizePravnihOsobaFacadeServer extends AS2FacadeServerLayer
		implements DevizePravnihOsobaFacade {
    
    private static DevizePravnihOsobaFacadeServer _instance = null;

    public static DevizePravnihOsobaFacadeServer getInstance() {
        if (_instance == null) {
            _instance = new DevizePravnihOsobaFacadeServer();
        }
        return _instance;
    }

    private DevizePravnihOsobaFacadeServer() {
    	AS2Context.setSingletonReference(this);
    }
    public PravnaOsobaRs pronadiSvePravneOsobeDevize(OsnovniVo value)  {
       PartijaDepozitaPravnihOsobaJdbc dao = new PartijaDepozitaPravnihOsobaJdbc(); 
       return dao.daoPronadiPravneOsobe(value,true);
     }
 	public PravnaOsobaRs procitajSvePravneOsobeDevize()  {
 	   PartijaDepozitaPravnihOsobaJdbc dao = new PartijaDepozitaPravnihOsobaJdbc(); 
       return dao.daoPronadiPravneOsobe(new OsnovniVo(),false);
	 }
    public IzvodRs izvjestajIzvodiDeviza(IzvodVo value)  {
    	IzvodiPravihOsobaJdbc dao = new IzvodiPravihOsobaJdbc();
        return new IzvodRs(dao.daoListaIzvodaDevizni(value));
    }

    public IzvodRs procitajSveObradeDeviznihIzvoda(IzvodVo value)  {
    	IzvodiPravihOsobaJdbc dao = new IzvodiPravihOsobaJdbc();
        return new IzvodRs(dao.daoListaObradaDeviznihIzvoda(value));
    }

    public RacunPartijeRs izvjestajStanjePartije(PartijaVo value)  {
        PartijaDepozitaPravnihOsobaJdbc dao = new PartijaDepozitaPravnihOsobaJdbc();
        return new RacunPartijeRs(dao.daoStanjePartijePoValutama(value));
    }

    public RacunPartijeRs izvjestajStanjeValute(PartijaVo value)  {
        PartijaDepozitaPravnihOsobaJdbc dao = new PartijaDepozitaPravnihOsobaJdbc();
        return new RacunPartijeRs(dao.daoStanjePoValutama(value));
    }

    public IzvjestajRs priljevOdljev(IzvjestajVo value)  {
        PartijaDepozitaPravnihOsobaJdbc dao = new PartijaDepozitaPravnihOsobaJdbc(); 
        return dao.daoPriljevOdljev(value);
     }

    public IzvjestajRs izvjestajDevizneTecajneRazlike(IzvjestajVo value)  {
        PartijaDepozitaPravnihOsobaJdbc dao = new PartijaDepozitaPravnihOsobaJdbc(); 
        return dao.daoTecajneRazlike(value);
    }

    public IzvjestajRs izvjestajOroceniDevizniDepoziti(IzvjestajVo value)  {
        PartijaDepozitaPravnihOsobaJdbc dao = new PartijaDepozitaPravnihOsobaJdbc(); 
        return dao.daoOroceniDevizniDepoziti(value);
    }

    public IzvjestajRs izvjestajOroceniKunskiDepoziti(IzvjestajVo value)  {
        PartijaDepozitaPravnihOsobaJdbc dao = new PartijaDepozitaPravnihOsobaJdbc(); 
        return dao.daoOroceniKunskiDepoziti(value);
    }
 }