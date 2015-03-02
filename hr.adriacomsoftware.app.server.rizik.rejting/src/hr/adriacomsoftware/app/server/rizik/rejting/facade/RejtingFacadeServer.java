package hr.adriacomsoftware.app.server.rizik.rejting.facade;

import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljOcjenaRs;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljOcjenaVo;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljRs;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.PokazateljVo;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.RejtingRs;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.RejtingVo;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.SkoringRs;
import hr.adriacomsoftware.app.common.rizik.rejting.dto.SkoringVo;
import hr.adriacomsoftware.app.common.rizik.rejting.facade.RejtingFacade;
import hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc.PokazateljJdbc;
import hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc.PokazateljOcjenaJdbc;
import hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc.PokazateljVrstaJdbc;
import hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc.RejtingJdbc;
import hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc.SkoringJdbc;
import hr.adriacomsoftware.app.server.rizik.rejting.da.jdbc.TipEntitetaPokazateljJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;

import java.util.Iterator;


public final class RejtingFacadeServer extends AS2FacadeServerLayer 
		implements RejtingFacade {

	private static RejtingFacadeServer _instance = null;
	public static RejtingFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new RejtingFacadeServer();
		}
		return _instance;
	}
	private RejtingFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	//rejting
    public RejtingRs procitajRejtingPravnihOsoba(RejtingVo value)  {
        RejtingJdbc dao = new RejtingJdbc();
        return dao.daoListaRejtingaPravnihOsoba(value);
    }
    public RejtingRs pronadiRejtingPravnihOsoba(RejtingVo value)  {
        RejtingJdbc dao = new RejtingJdbc();
        return dao.daoListaRejtingaPravnihOsoba(value);
    }
    //skoring
    public SkoringRs pripremaOcjenaPravneOsobe(SkoringVo value)  {
        SkoringJdbc dao = new SkoringJdbc();
        dao.daoNoviSkoringPravneOsobe(value);
        if(value.getOsvjeziObradu().equals("1")){
        	dao.daoMjesecniSkoringPravneOsobe(value,1);
        	dao.daoMjesecniSkoringPravneOsobe(value,2);
        	dao.daoMjesecniSkoringPravneOsobe(value,3);
        	dao.daoMjesecniSkoringPravneOsobe(value,4);
        	dao.daoMjesecniSkoringPravneOsobe(value,5);
        	dao.daoMjesecniSkoringPravneOsobe(value,7);
        	dao.daoMjesecniSkoringPravneOsobe(value,9);
        }
        return dao.daoFindSkoringPravneOsobe(value);
    } 
    public SkoringRs procitajSkoringPravneOsobe(SkoringVo value)  {
        SkoringJdbc dao = new SkoringJdbc();
        return dao.daoFindSkoringPravneOsobe(value);
    }
    public SkoringVo azurirajSkoring(SkoringVo value)  {
        SkoringJdbc dao_skoring = new SkoringJdbc();
        RejtingJdbc dao_rejting = new RejtingJdbc();
        if(value.get("@@NOVO").equals("vrijednost_pokazatelja"))
            value.setOcjena(dao_skoring.daoFnPripremJednuOcjenu(value));
		dao_rejting.daoAzurirajRejtingPravneOsobe(value);
		return value;
    }
    public SkoringVo brisiSkoring(SkoringVo value)  {
        SkoringJdbc dao = new SkoringJdbc();
		dao.daoRemove(value);
	    dao.daoSpAzurirajPondere(value);
		return value;
    }
    public SkoringVo brisiViseSkoringa(SkoringRs value)  {
        SkoringJdbc dao = new SkoringJdbc();
		dao.daoRemoveMany(value);
		return new SkoringVo();
    }
    public PokazateljRs procitajSvePokazatelje(PokazateljVo value)  {
        PokazateljJdbc dao = new PokazateljJdbc();
        PokazateljRs rs = dao.daoFindZaRazdoblje(value);
        //prikupi podatke o tipu pokazatelja
        PokazateljRs new_rs = new PokazateljRs();
        Iterator<AS2Record> E = rs.getRows().iterator();
        while(E.hasNext()){
            new_rs.addRow(dao.daoLoad((PokazateljVo)E.next()));
        }
        return new_rs;
    }
    public PokazateljVo citajPokazatelj(PokazateljVo value)  {
        PokazateljJdbc dao = new PokazateljJdbc();
        return dao.daoLoad(value);
    }
    public PokazateljVo azurirajPokazatelj(PokazateljVo value)  {
        PokazateljJdbc dao = new PokazateljJdbc();
        dao.daoStore(value);
        TipEntitetaPokazateljJdbc dao2 = new TipEntitetaPokazateljJdbc();
        dao2.daoRemoveTipPokazatelja(value);
        PokazateljRs tip_pok_rs = value.pripremiTipovePokazatelja();  
        dao2.daoCreateMany(tip_pok_rs);
        return value;
    }
    public PokazateljRs izracunajPokazatelje(PokazateljVo value)  {
       PokazateljJdbc dao = new PokazateljJdbc();
       return dao.daoIzracunajPokazatelje(value);
    }
    public PokazateljRs procitajSveVrstePokazatelja(PokazateljVo value)  {
        PokazateljVrstaJdbc dao = new PokazateljVrstaJdbc();
        return new PokazateljRs(dao.daoFind(value));
    }
    public PokazateljVo azurirajVrstuPokazatelja(PokazateljVo value)  {
        PokazateljVrstaJdbc dao = new PokazateljVrstaJdbc();
        dao.daoStore(value);
        return value;
    }
    //ocjene
    public PokazateljOcjenaRs procitajSveOcjenePokazatelja(PokazateljVo value)  {
        PokazateljOcjenaJdbc dao = new PokazateljOcjenaJdbc();
        return new PokazateljOcjenaRs(dao.daoFindZaPokazatelj(value));
    }
    public PokazateljOcjenaVo azurirajOcjenuPokazatelja(PokazateljOcjenaVo value)  {
        PokazateljOcjenaJdbc dao = new PokazateljOcjenaJdbc();
        dao.daoStoreVrijednosti(value);
        return value;
    }
	public PokazateljRs izracunajRanoUpozorenje(PokazateljVo value)	 {
		PokazateljJdbc dao = new PokazateljJdbc();
	    return dao.daoIzracunajRanoUpozorenje(value);
	}
}
