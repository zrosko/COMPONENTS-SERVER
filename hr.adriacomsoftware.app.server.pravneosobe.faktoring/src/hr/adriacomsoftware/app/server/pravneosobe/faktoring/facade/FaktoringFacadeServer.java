package hr.adriacomsoftware.app.server.pravneosobe.faktoring.facade;

import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.DiskontRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.DiskontVo;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.FakturaRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.FakturaVo;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.KupacFaktoringaRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.KupacFaktoringaVo;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.KupacLimitRs;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.dto.KupacLimitVo;
import hr.adriacomsoftware.app.common.pravneosobe.faktoring.facade.FaktoringFacade;
import hr.adriacomsoftware.app.server.gk.da.jdbc.TemeljnicaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc.PoFaktDiskontJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc.PoFaktFakturaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc.PoFaktKupacJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc.PoFaktKupacLimitJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.faktoring.da.jdbc.PoFaktSpecifikacijaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;
import hr.as2.inf.server.session.AS2SessionFactory;


public final class FaktoringFacadeServer extends AS2FacadeServerLayer
		implements FaktoringFacade {
    
    private static FaktoringFacadeServer _instance = null;

    public static FaktoringFacadeServer getInstance() {
        if (_instance == null) {
            _instance = new FaktoringFacadeServer();
        }
        return _instance;
    }

    private FaktoringFacadeServer() {
    	AS2Context.setSingletonReference(this);
    }
	public AS2RecordList temeljnicaFaktoring(AS2Record value)  {
	    TemeljnicaJdbc dao = new TemeljnicaJdbc();
	    return dao.daoTemeljnica(value,"FAKTORING");
	 }

    public FakturaRs procitajSveSpecifikacije(FakturaVo value)  {
        PoFaktSpecifikacijaJdbc dao = new PoFaktSpecifikacijaJdbc();
        return  dao.daoProcitajSveSpecifikacije(value);
    }

    public FakturaRs pronadiSpecifikacije(FakturaVo value)  {
        PoFaktSpecifikacijaJdbc dao = new PoFaktSpecifikacijaJdbc();
        return  dao.daoPronadiSpecifikacije(value);
    }

    public FakturaVo azurirajSpecifikaciju(FakturaVo value)  {
        PoFaktSpecifikacijaJdbc dao = new PoFaktSpecifikacijaJdbc();        
        dao.daoStore(value);
        return value;
    }

    public FakturaVo dodajSpecifikaciju(FakturaVo value)  {
        PoFaktSpecifikacijaJdbc dao = new PoFaktSpecifikacijaJdbc();
        value.setOperaterUnosa(AS2SessionFactory.getInstance().getCurrentUser().get("id_osobe"));
        dao.daoCreate(value);
        return value;
    }

    public FakturaVo brisiSpecifikaciju(FakturaVo value)  {
        PoFaktSpecifikacijaJdbc dao = new PoFaktSpecifikacijaJdbc();
        value.setIspravnoNE();
        dao.daoStore(value);
        return value;
    }

    public FakturaRs procitajSveFakture(FakturaVo value)  {
        PoFaktFakturaJdbc dao = new PoFaktFakturaJdbc();
        return  dao.daoProcitajSveFakture(value);
    }

    public FakturaVo azurirajFakturu(FakturaVo value)  {
        PoFaktFakturaJdbc dao = new PoFaktFakturaJdbc();
        dao.daoStore(value);
        return value;
    }

    public FakturaVo dodajFakturu(FakturaVo value)  {
        PoFaktFakturaJdbc dao = new PoFaktFakturaJdbc();
        dao.daoCreate(value);
        return value;
    }

    public FakturaVo brisiFakturu(FakturaVo value)  {
        PoFaktFakturaJdbc dao = new PoFaktFakturaJdbc();
        dao.daoRemove(value);
        return value;
    }

    public KupacFaktoringaRs procitajSveKupceFaktoringa(KupacFaktoringaVo value)  {
        PoFaktKupacJdbc dao = new PoFaktKupacJdbc();
        return dao.daoProcitajSveKupce(value);

    }

    public KupacFaktoringaVo azurirajKupcaFaktoringa(KupacFaktoringaVo value)  {
        PoFaktKupacJdbc dao = new PoFaktKupacJdbc();
        dao.daoStore(value);
        return value;
    }

    public KupacFaktoringaVo dodajKupcaFaktoringa(KupacFaktoringaVo value)  {
        PoFaktKupacJdbc dao = new PoFaktKupacJdbc();
        dao.daoCreate(value);
        return value;
    }

    public KupacFaktoringaVo brisiKupcaFaktoringa(KupacFaktoringaVo value)  {
        PoFaktKupacJdbc dao = new PoFaktKupacJdbc();
        dao.daoRemove(value);
        return value;
    }

    public DiskontRs procitajSveDiskonte(DiskontVo value)  {
        PoFaktDiskontJdbc dao = new PoFaktDiskontJdbc();
        return dao.daoFind(value);
    }

    public DiskontVo azurirajDiskont(DiskontVo value)  {
        PoFaktDiskontJdbc dao = new PoFaktDiskontJdbc();
        dao.daoStore(value);
        return value;
    }

    public DiskontVo dodajDiskont(DiskontVo value)  {
        PoFaktDiskontJdbc dao = new PoFaktDiskontJdbc();
        dao.daoCreate(value);
        return value;
    }

    public DiskontVo brisiDiskont(DiskontVo value)  {
        PoFaktDiskontJdbc dao = new PoFaktDiskontJdbc();
        dao.daoRemove(value);
        return value;
    }

    public KupacLimitRs procitajSveLimiteKupca(KupacLimitVo value)  {
        PoFaktKupacLimitJdbc dao = new PoFaktKupacLimitJdbc();
        return dao.daoFind(value);
    }

    public KupacLimitVo azurirajLimitKupca(KupacLimitVo value)  {
        PoFaktKupacLimitJdbc dao = new PoFaktKupacLimitJdbc();
        dao.daoStore(value);
        return value;
    }

    public KupacLimitVo dodajLimitKupca(KupacLimitVo value)  {
        PoFaktKupacLimitJdbc dao = new PoFaktKupacLimitJdbc();
        dao.daoCreate(value);
        return value;
    }

    public KupacLimitVo brisiLimitKupca(KupacLimitVo value)  {
        PoFaktKupacLimitJdbc dao = new PoFaktKupacLimitJdbc();
        dao.daoRemove(value);
        return value;
    }
 }