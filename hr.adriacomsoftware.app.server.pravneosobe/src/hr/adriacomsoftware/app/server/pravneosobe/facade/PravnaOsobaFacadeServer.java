package hr.adriacomsoftware.app.server.pravneosobe.facade;


import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.Bon2UpitRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.Bon2UpitVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.dto.PravnaOsobaVo;
import hr.adriacomsoftware.app.common.pravneosobe.dto.VezanaOsobaRs;
import hr.adriacomsoftware.app.common.pravneosobe.facade.PravnaOsobaFacade;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.DobavljacRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.DobavljacVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KupacRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.dto.KupacVo;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.rizik.dto.ZahOcjenaRs;
import hr.adriacomsoftware.app.common.pravneosobe.zahtjev.rizik.dto.ZahOcjenaVo;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.JrrJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.ObracunNaknadeJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PartijaPravnihOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PoDobavljacJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PoKupacJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PoOcjenaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.PravnaOsobaJdbc;
import hr.adriacomsoftware.app.server.pravneosobe.da.jdbc.VezanaOsobaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class PravnaOsobaFacadeServer extends AS2FacadeServerLayer
		implements PravnaOsobaFacade {

	private static PravnaOsobaFacadeServer _instance = null;
	public static PravnaOsobaFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new PravnaOsobaFacadeServer();
		}
		return _instance;
	}
	private PravnaOsobaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public PravnaOsobaRs citajSvePravneOsobe(PravnaOsobaVo value)  {
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        return dao.daoListaPravnihOsoba(value);
    }
    public PravnaOsobaRs pronadiSvePravneOsobeJrr(PravnaOsobaVo value)  {
        JrrJdbc dao = new JrrJdbc();
        return dao.daoPronadiPravneOsobe(value);
    }
    public PravnaOsobaRs pronadiSvePartijePravneOsobeJrr(PravnaOsobaVo value)  {
        JrrJdbc dao = new JrrJdbc();
        return dao.daoPronadiPartijePravneOsobe(value);
    }
    public PravnaOsobaVo citajPravnuOsobuJrr(PravnaOsobaVo value)  {
        JrrJdbc dao = new JrrJdbc();
        return new PravnaOsobaVo(dao.daoLoad(value));        
    }
    public PravnaOsobaRs citajSvePravneOsobeJrr(PravnaOsobaVo value)  {
        JrrJdbc dao = new JrrJdbc();
        return dao.daoListaPravnihOsoba(value);
    }
    public PravnaOsobaRs pronadiPravnuOsobuJrrSimple(PravnaOsobaVo value)  {
        JrrJdbc dao = new JrrJdbc();
        return dao.daoPronadiPravnuOsobuJrrSimple(value);
    }
    public PravnaOsobaRs pronadiSvePravneOsobe(PravnaOsobaVo value)  {
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        return dao.daoPronadiPravneOsobe(value);
    }
    public PravnaOsobaVo citajPravnuOsobu(PravnaOsobaVo value)  {
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        return new PravnaOsobaVo(dao.daoLoad(value));        
    }
    public PartijaRs citajSaldaSvihPartija(PravnaOsobaVo value)  {
        PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
        return dao.daoSaldoSvihPartijaPravneOsobe(value);
    }
    public PartijaRs citajSvePartijeKredita(PravnaOsobaVo value)  {
        PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
        return dao.daoListaSvihKredita(value);
    }
    public PartijaRs citajSveUgovore(PravnaOsobaVo value)  {
        PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
        return dao.daoListaSvihUgovora(value);
    }
    public PartijaRs citajSveDepozite(PravnaOsobaVo value)  {
        PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
        return dao.daoListaSvihDepozita(value);
    }
    public Bon2UpitRs citajSveBon2Podatke(Bon2UpitVo value)  {
        PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
        return dao.daoListaSvihBon2Podataka((value));
     }
    public PartijaRs citajBonitet(PravnaOsobaVo value)  {
        PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
        return dao.daoBonitetPravneOsobe(value);
    }
    public PartijaRs citajSvePartijePravneOsobe(PravnaOsobaVo value)  {
        PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
        return dao.daoFindListaOtvorenihPartijaPravneOsobe(value);
    }
    public VezanaOsobaRs citajSvePovezaneOsobeOsobe(PravnaOsobaVo value)  {
        VezanaOsobaJdbc dao = new VezanaOsobaJdbc();
        return dao.daoFindSveVezaneOsobe(value);
    }
    public void upisiObracunNaknada(AS2RecordList rs)  {
        ObracunNaknadeJdbc dao = new ObracunNaknadeJdbc();
        dao.daoCreateMany(rs);
    }
    public PravnaOsobaRs citajPodatkeKlijenta(PravnaOsobaVo value)  {
        PravnaOsobaRs j2eers = new PravnaOsobaRs();
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        
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
    public ZahOcjenaRs pripremaSkoringOcjena(ZahOcjenaVo value)  {
        PoOcjenaJdbc dao = new PoOcjenaJdbc();
        dao.daoSpPripremiOcjenePravnaOsoba(value);
        ZahOcjenaRs rs = new ZahOcjenaRs(dao.daoFindPravnaOsoba(value));
        return rs;
    }
    public ZahOcjenaRs procitajSkoringPravneOsobe(ZahOcjenaVo value)  {
        PoOcjenaJdbc dao = new PoOcjenaJdbc();
        ZahOcjenaRs rs = new ZahOcjenaRs(dao.daoFindPravnaOsoba(value));
		return rs;
    }
    public PartijaRs citajSvePartijeIzlozenosti(PravnaOsobaVo value)  {
        PartijaPravnihOsobaJdbc dao = new PartijaPravnihOsobaJdbc();
        return dao.daoListaSvihIzlozenosti(value);
    }
    /******* K U P C I ******************/
    public KupacVo azurirajKupca(KupacVo value)  {
        PoKupacJdbc dao = new PoKupacJdbc();
        dao.daoStore(value);
        return value;
    }
    public KupacRs procitajSveKupce(KupacVo value)  {
        PoKupacJdbc dao = new PoKupacJdbc();
        KupacRs rs = new KupacRs(dao.daoFind(value));
        return rs;
    }
    public KupacVo dodajKupca(KupacVo value)  {
        PoKupacJdbc dao = new PoKupacJdbc();
        dao.daoCreate(value);
        return value;
    }
    public KupacVo brisiKupca(KupacVo value)  {
        PoKupacJdbc dao = new PoKupacJdbc();
        dao.daoRemove(value);
        return value;
    }
    public KupacVo brisiSveKupce(KupacVo value)  {
        PoKupacJdbc dao = new PoKupacJdbc();
        dao.daoRemoveSveKupceZaKlijenta(value);
        return value;
    }
    public KupacVo brisiViseKupaca(KupacRs value)  {
        PoKupacJdbc dao = new PoKupacJdbc();
        dao.daoRemoveMany(value);
        return new KupacVo();
    }
    public KupacVo dodajViseKupaca(KupacRs value)  {
        PoKupacJdbc dao = new PoKupacJdbc();
        dao.daoCreateMany(value);
        return new KupacVo();
    }
    /******* D O B A V LJ A C I ******************/
    public DobavljacVo azurirajDobavljaca(DobavljacVo value)  {
        PoDobavljacJdbc dao = new PoDobavljacJdbc();
        dao.daoStore(value);
        return value;
    }
    public DobavljacRs procitajSveDobavljace(DobavljacVo value)  {
        PoDobavljacJdbc dao = new PoDobavljacJdbc();
        DobavljacRs rs = new DobavljacRs(dao.daoFind(value));
        return rs;
    }
    public DobavljacVo dodajDobavljaca(DobavljacVo value)  {
        PoDobavljacJdbc dao = new PoDobavljacJdbc();
        dao.daoCreate(value);
        return value;
    }
    public DobavljacVo brisiDobavljaca(DobavljacVo value)  {
        PoDobavljacJdbc dao = new PoDobavljacJdbc();
        dao.daoRemove(value);
        return value;
    }
    public DobavljacVo brisiSveDobavljace(DobavljacVo value)  {
        PoDobavljacJdbc dao = new PoDobavljacJdbc();
        dao.daoRemoveSveDobavljaceZaKlijenta(value);
        return value;
    }
    public DobavljacVo brisiViseDobavljaca(DobavljacRs value)  {
        PoDobavljacJdbc dao = new PoDobavljacJdbc();
        dao.daoRemoveMany(value);
        return new DobavljacVo();
    }
    public DobavljacVo dodajViseDobavljaca(DobavljacRs value)  {
        PoDobavljacJdbc dao = new PoDobavljacJdbc();
        dao.daoCreateMany(value);
        return new DobavljacVo();
    }
    public OsnovniRs citajPrometePravneOsobe(OsnovniVo value)  {
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        return dao.daoPrometPravneOsobe(value);
    }
    public PravnaOsobaRs citajPravneOsobePremaNadleznosti(PravnaOsobaVo value) {
        PravnaOsobaJdbc dao = new PravnaOsobaJdbc();
        return dao.daoPronadiPremaNadleznosti(value);
    }
}
