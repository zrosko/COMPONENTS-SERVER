package hr.adriacomsoftware.app.server.cmdb.facade;

import hr.adriacomsoftware.app.common.cmdb.dto.CmdbDjelatnikRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbDjelatnikVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbDobavljacRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbDobavljacVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaDodatakRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaDodatakVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaKorisnikRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaKorisnikVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaPrivitakRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaPrivitakVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaVezaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaVezaVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbImovinaVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbKvarRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbKvarVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbMrezniCvorRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbMrezniCvorVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbOrgJedinicaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbOrgJedinicaVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbPromjenaPrivitakRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbPromjenaPrivitakVo;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbPromjenaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.CmdbPromjenaVo;
import hr.adriacomsoftware.app.common.cmdb.dto.DjelatnikIzobrazbaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.DjelatnikIzobrazbaVo;
import hr.adriacomsoftware.app.common.cmdb.dto.DjelatnikVjestinaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.DjelatnikVjestinaVo;
import hr.adriacomsoftware.app.common.cmdb.dto.EmailVo;
import hr.adriacomsoftware.app.common.cmdb.dto.IzobrazbaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.IzobrazbaVo;
import hr.adriacomsoftware.app.common.cmdb.dto.VjestinaRs;
import hr.adriacomsoftware.app.common.cmdb.dto.VjestinaVo;
import hr.adriacomsoftware.app.common.cmdb.facade.CMDBFacade;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbDjelatnikIzobrazbaJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbDjelatnikJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbDjelatnikVjestinaJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbDobavljacJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbImovinaDodatakJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbImovinaJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbImovinaKorisnikJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbImovinaPovijestJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbImovinaPrivitakJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbImovinaVezaJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbIzobrazbaJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbKvarJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbMrezniCvorJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbOrgJedinicaJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbPromjenaJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbPromjenaPrivitakJdbc;
import hr.adriacomsoftware.app.server.cmdb.da.jdbc.CmdbVjestinaJdbc;
import hr.adriacomsoftware.app.server.services.da.jdbc.CMDBJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class CMDBFacadeServer extends AS2FacadeServerLayer
		implements CMDBFacade {

	private static CMDBFacadeServer _instance = null; 
	public static CMDBFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new CMDBFacadeServer(); 
		}
		return _instance;
	}
	private CMDBFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public CmdbDjelatnikRs procitajSveDjelatnike(CmdbDjelatnikVo value)  {
        CmdbDjelatnikJdbc dao = new CmdbDjelatnikJdbc();
        CmdbDjelatnikRs rs = new CmdbDjelatnikRs(dao.daoFind(value,false));
		return rs;
    }
    public CmdbDjelatnikRs pronadiDjelatnike(CmdbDjelatnikVo value)  {
        CmdbDjelatnikJdbc dao = new CmdbDjelatnikJdbc();
        CmdbDjelatnikRs rs = new CmdbDjelatnikRs(dao.daoFind(value,true));
		return rs;
    }
    public CmdbDjelatnikRs pronadiEmailDjelatnike(CmdbDjelatnikVo value)  {
        CmdbDjelatnikJdbc dao = new CmdbDjelatnikJdbc();
        CmdbDjelatnikRs rs = new CmdbDjelatnikRs(dao.daoFindBezOvlasti(value,true));
		return rs;
    }
    public CmdbDjelatnikVo azurirajDjelatnika(CmdbDjelatnikVo value)  {
        CmdbDjelatnikJdbc dao = new CmdbDjelatnikJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbDjelatnikVo dodajDjelatnika(CmdbDjelatnikVo value)  {
        CmdbDjelatnikJdbc dao = new CmdbDjelatnikJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbDjelatnikVo brisiDjelatnika(CmdbDjelatnikVo value)  {
        CmdbDjelatnikJdbc dao = new CmdbDjelatnikJdbc();
        CmdbDjelatnikVo vo = new CmdbDjelatnikVo();
        vo.setIdDjelatnika(value.getIdDjelatnika());
        vo.setIspravno("0");
		dao.daoStore(vo);
		return value;
    }
    public IzobrazbaRs procitajSveIzobrazbe(IzobrazbaVo value)  {
        CmdbIzobrazbaJdbc dao = new CmdbIzobrazbaJdbc();
        IzobrazbaRs rs = new IzobrazbaRs(dao.daoFind(value));
		return rs;
    }
    public IzobrazbaVo azurirajIzobrazbu(IzobrazbaVo value)  {
        CmdbIzobrazbaJdbc dao = new CmdbIzobrazbaJdbc();
		dao.daoStore(value);
		return value;
    }
    public IzobrazbaVo dodajIzobrazbu(IzobrazbaVo value)  {
        CmdbIzobrazbaJdbc dao = new CmdbIzobrazbaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IzobrazbaVo brisiIzobrazbu(IzobrazbaVo value)  {
        CmdbIzobrazbaJdbc dao = new CmdbIzobrazbaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public VjestinaRs procitajSveVjestine(VjestinaVo value)  {
        CmdbVjestinaJdbc dao = new CmdbVjestinaJdbc();
        VjestinaRs rs = new VjestinaRs(dao.daoFind(value));
		return rs;
    }
    public VjestinaVo azurirajVjestinu(VjestinaVo value)  {
        CmdbVjestinaJdbc dao = new CmdbVjestinaJdbc();
		dao.daoStore(value);
		return value;
    }
    public VjestinaVo dodajVjestinu(VjestinaVo value)  {
        CmdbVjestinaJdbc dao = new CmdbVjestinaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public VjestinaVo brisiVjestinu(VjestinaVo value)  {
        CmdbVjestinaJdbc dao = new CmdbVjestinaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public DjelatnikIzobrazbaRs procitajSveIzobrazbeDjelatnika(DjelatnikIzobrazbaVo value)  {
        CmdbDjelatnikIzobrazbaJdbc dao = new CmdbDjelatnikIzobrazbaJdbc();
        DjelatnikIzobrazbaRs rs = new DjelatnikIzobrazbaRs(dao.daoFind(value));
		return rs;
    }
    public DjelatnikIzobrazbaVo azurirajIzobrazbuDjelatnika(DjelatnikIzobrazbaVo value)  {
        CmdbDjelatnikIzobrazbaJdbc dao = new CmdbDjelatnikIzobrazbaJdbc();
        //System.out.println(value);
		dao.daoStore(value);
		return value;
    }
    public DjelatnikIzobrazbaVo dodajIzobrazbuDjelatika(DjelatnikIzobrazbaVo value)  {
        CmdbDjelatnikIzobrazbaJdbc dao = new CmdbDjelatnikIzobrazbaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public DjelatnikIzobrazbaVo brisiIzobrazbuDjelatnika(DjelatnikIzobrazbaVo value)  {
        CmdbDjelatnikIzobrazbaJdbc dao = new CmdbDjelatnikIzobrazbaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public DjelatnikVjestinaRs procitajSveVjestineDjelatnika(DjelatnikVjestinaVo value)  {
        CmdbDjelatnikVjestinaJdbc dao = new CmdbDjelatnikVjestinaJdbc();
        DjelatnikVjestinaRs rs = new DjelatnikVjestinaRs(dao.daoFind(value));
		return rs;
    }
    public DjelatnikVjestinaVo azurirajVjestinuDjelatnika(DjelatnikVjestinaVo value)  {
        CmdbDjelatnikVjestinaJdbc dao = new CmdbDjelatnikVjestinaJdbc();
		dao.daoStore(value);
		return value;
    }
    public DjelatnikVjestinaVo dodajVjestinuDjelatnika(DjelatnikVjestinaVo value)  {
        CmdbDjelatnikVjestinaJdbc dao = new CmdbDjelatnikVjestinaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public DjelatnikVjestinaVo brisiVjestinuDjelatnika(DjelatnikVjestinaVo value)  {
        CmdbDjelatnikVjestinaJdbc dao = new CmdbDjelatnikVjestinaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public EmailVo proslijediEmailom(EmailVo value)  {
        //System.out.println(value);
        return null;
    }
    public CmdbImovinaRs citajSvuImovinu(CmdbImovinaVo value)  {
        CmdbImovinaJdbc dao = new CmdbImovinaJdbc();
        CmdbImovinaRs rs = new CmdbImovinaRs(dao.daoListaImovine(value));
		return rs;
    }
    public CmdbImovinaRs pronadiSvuImovinu(CmdbImovinaVo value)  {
        CmdbImovinaJdbc dao = new CmdbImovinaJdbc();
        CmdbImovinaRs rs = new CmdbImovinaRs();
        if (value.get("@novo").equals("@novo")){
            if(value.get("pregled").equals("")) value.set("pregled",value.get("@@pregled"));
            rs = new CmdbImovinaRs(dao.daoPronadiImovinuNovo(value));
        }else //pretrazivanje
            rs = new CmdbImovinaRs(dao.daoPronadiImovinu(value));
		return rs;
    }
    public CmdbImovinaVo dodajImovinu(CmdbImovinaVo value)  {
        CmdbImovinaJdbc dao = new CmdbImovinaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbImovinaVo citajImovinu(CmdbImovinaVo value)  {
        CmdbImovinaJdbc dao = new CmdbImovinaJdbc();
		dao.daoLoad(value);
		return value;
    }
    public CmdbImovinaVo duplicirajImovinu(CmdbImovinaVo value)  {
        CmdbImovinaJdbc dao = new CmdbImovinaJdbc();
        value.setIdImovine(null);
		dao.daoCreate(value);
		return value;
    }
    public CmdbImovinaVo brisiImovinu(CmdbImovinaVo value)  {
        CmdbImovinaJdbc dao = new CmdbImovinaJdbc();
        CmdbImovinaVo vo = new CmdbImovinaVo();
        vo.setIdImovine(value.getIdImovine());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public CmdbImovinaVo azurirajImovinu(CmdbImovinaVo value)  {
        CmdbImovinaJdbc dao = new CmdbImovinaJdbc();
		dao.daoStore(value);
		return value;
    }
    //povijest imovine
    public CmdbImovinaRs citajPovijestImovine(CmdbImovinaVo value)  {
        CmdbImovinaPovijestJdbc dao = new CmdbImovinaPovijestJdbc();
        CmdbImovinaRs rs = new CmdbImovinaRs(dao.daoPronadiPovijestImovine(value));
//        System.out.println(rs);
        return rs;
    }
    public CmdbImovinaVo dodajUrediPovijestImovine(CmdbImovinaVo value)  {
        //System.out.println(value);
        CmdbImovinaPovijestJdbc dao = new CmdbImovinaPovijestJdbc();
        CmdbImovinaJdbc dao_imovina = new CmdbImovinaJdbc();
        if(value.getIdPovijesti().equals(""))
            dao.daoCreate(value);
        else
            dao.daoStore(value);
        if(!value.get("command").equals("B_akcija_uredi")&&!value.get("command").equals("B_akcija_novo"))
            dao_imovina.daoStore(value);
        return value;
    }
    
    
    //korisnik imovine
    public CmdbImovinaKorisnikRs procitajSveKorisnikeImovine(CmdbImovinaKorisnikVo value)  {
        CmdbImovinaKorisnikJdbc dao = new CmdbImovinaKorisnikJdbc();
        CmdbImovinaKorisnikRs rs = new CmdbImovinaKorisnikRs(dao.daoFind(value));
		return rs;
    }
    public CmdbImovinaKorisnikVo azurirajKorisnikaImovine(CmdbImovinaKorisnikVo value)  {
        CmdbImovinaKorisnikJdbc dao = new CmdbImovinaKorisnikJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbImovinaKorisnikVo dodajKorisnikaImovine(CmdbImovinaKorisnikVo value)  {
        CmdbImovinaKorisnikJdbc dao = new CmdbImovinaKorisnikJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbImovinaKorisnikVo brisiKorisnikaImovine(CmdbImovinaKorisnikVo value)  {
        CmdbImovinaKorisnikJdbc dao = new CmdbImovinaKorisnikJdbc();
		dao.daoRemove(value);
		return value;
    } 
    //dodatak imovine
    public CmdbImovinaDodatakRs procitajSveDodatkeImovine(CmdbImovinaDodatakVo value)  {
        CmdbImovinaDodatakJdbc dao = new CmdbImovinaDodatakJdbc();
        CmdbImovinaDodatakRs rs = new CmdbImovinaDodatakRs(dao.daoFind(value));
		return rs;
    }
    public CmdbImovinaDodatakVo azurirajDodatakImovine(CmdbImovinaDodatakVo value)  {
        CmdbImovinaDodatakJdbc dao = new CmdbImovinaDodatakJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbImovinaDodatakVo dodajDodatakImovine(CmdbImovinaDodatakVo value)  {
        CmdbImovinaDodatakJdbc dao = new CmdbImovinaDodatakJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbImovinaDodatakVo brisiDodatakImovine(CmdbImovinaDodatakVo value)  {
        CmdbImovinaDodatakJdbc dao = new CmdbImovinaDodatakJdbc();
		dao.daoRemove(value);
		return value;
    }
    //privitak imovine
    public CmdbImovinaPrivitakRs procitajSvePrivitkeImovine(CmdbImovinaPrivitakVo value)  {
        CmdbImovinaPrivitakJdbc dao = new CmdbImovinaPrivitakJdbc();
        CmdbImovinaPrivitakRs rs = new CmdbImovinaPrivitakRs(dao.daoFind(value));
		return rs;
    }
    public CmdbImovinaPrivitakVo dodajPrivitakImovine(CmdbImovinaPrivitakVo value)  {
        CmdbImovinaPrivitakJdbc dao = new CmdbImovinaPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public CmdbImovinaPrivitakVo citajPrivitakImovine(CmdbImovinaPrivitakVo value)  {
        CmdbImovinaPrivitakJdbc dao = new CmdbImovinaPrivitakJdbc();
        return new CmdbImovinaPrivitakVo(dao.daoLoad(value));
    }
    public CmdbImovinaPrivitakVo brisiPrivitakImovine(CmdbImovinaPrivitakVo value)  {
        CmdbImovinaPrivitakJdbc dao = new CmdbImovinaPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
    //privitak promjene
    public CmdbPromjenaPrivitakRs procitajSvePrivitkePromjene(CmdbPromjenaPrivitakVo value)  {
        CmdbPromjenaPrivitakJdbc dao = new CmdbPromjenaPrivitakJdbc();
        CmdbPromjenaPrivitakRs rs = new CmdbPromjenaPrivitakRs(dao.daoFind(value));
		return rs;
    }
    public CmdbPromjenaPrivitakVo dodajPrivitakPromjene(CmdbPromjenaPrivitakVo value)  {
        CmdbPromjenaPrivitakJdbc dao = new CmdbPromjenaPrivitakJdbc();
        dao.daoCreate(value);
        return value;
    }
    public CmdbPromjenaPrivitakVo citajPrivitakPromjene(CmdbPromjenaPrivitakVo value)  {
        CmdbPromjenaPrivitakJdbc dao = new CmdbPromjenaPrivitakJdbc();
        return new CmdbPromjenaPrivitakVo(dao.daoLoad(value));
    }
    public CmdbPromjenaPrivitakVo brisiPrivitakPromjene(CmdbPromjenaPrivitakVo value)  {
        CmdbPromjenaPrivitakJdbc dao = new CmdbPromjenaPrivitakJdbc();
        dao.daoRemove(value);
        return value;
    }
    //kvar imovine
    public CmdbKvarRs procitajSveKvaroveImovine(CmdbKvarVo value)  {
        CmdbKvarJdbc dao = new CmdbKvarJdbc();
        CmdbKvarRs rs = new CmdbKvarRs(dao.daoFind(value));
		return rs;
    }
    public CmdbKvarVo azurirajKvarImovine(CmdbKvarVo value)  {
        CmdbKvarJdbc dao = new CmdbKvarJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbKvarVo dodajKvarImovine(CmdbKvarVo value)  {
        CmdbKvarJdbc dao = new CmdbKvarJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbKvarVo brisiKvarImovine(CmdbKvarVo value)  {
        CmdbKvarJdbc dao = new CmdbKvarJdbc();
		dao.daoRemove(value);
		return value;
    }
    //promjena imovine
    public CmdbPromjenaRs procitajSvePromjeneImovine(CmdbPromjenaVo value)  {
        CmdbPromjenaJdbc dao = new CmdbPromjenaJdbc();
        CmdbPromjenaRs rs = new CmdbPromjenaRs(dao.daoFind(value));
		return rs;
    }
    public CmdbPromjenaRs procitajSvePromjene(CmdbPromjenaVo value)  {
        CmdbPromjenaJdbc dao = new CmdbPromjenaJdbc();
        CmdbPromjenaRs rs = new CmdbPromjenaRs(dao.daoListaPromjena(value));
		return rs;
    }
    public CmdbPromjenaVo azurirajPromjenuImovine(CmdbPromjenaVo value)  {
        CmdbPromjenaJdbc dao = new CmdbPromjenaJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbPromjenaVo dodajPromjenuImovine(CmdbPromjenaVo value)  {
        CmdbPromjenaJdbc dao = new CmdbPromjenaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbPromjenaVo brisiPromjenuImovine(CmdbPromjenaVo value)  {
        CmdbPromjenaJdbc dao = new CmdbPromjenaJdbc();
		dao.daoRemove(value);
		return value;
    }
    //veza imovine
    public CmdbImovinaVezaRs procitajSveVezeImovine(CmdbImovinaVezaVo value)  {
        CmdbImovinaVezaJdbc dao = new CmdbImovinaVezaJdbc();
        CmdbImovinaVezaRs rs = new CmdbImovinaVezaRs(dao.daoFind(value));
		return rs;
    }
    public CmdbImovinaVezaVo azurirajVezuImovine(CmdbImovinaVezaVo value)  {
        CmdbImovinaVezaJdbc dao = new CmdbImovinaVezaJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbImovinaVezaVo dodajVezuImovine(CmdbImovinaVezaVo value)  {
        CmdbImovinaVezaJdbc dao = new CmdbImovinaVezaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbImovinaVezaVo brisiVezuImovine(CmdbImovinaVezaVo value)  {
        CmdbImovinaVezaJdbc dao = new CmdbImovinaVezaJdbc();
		dao.daoRemove(value);
		return value;
    }
    //dobavljač 
    public CmdbDobavljacRs procitajSveDobavljace(CmdbDobavljacVo value)  {
        CmdbDobavljacJdbc dao = new CmdbDobavljacJdbc();
        CmdbDobavljacRs rs = new CmdbDobavljacRs(dao.daoFind(value));
		return rs;
    }
    public CmdbDobavljacVo azurirajDobavljaca(CmdbDobavljacVo value)  {
        CmdbDobavljacJdbc dao = new CmdbDobavljacJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbDobavljacVo dodajDobavljaca(CmdbDobavljacVo value)  {
        CmdbDobavljacJdbc dao = new CmdbDobavljacJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbDobavljacVo brisiDobavljaca(CmdbDobavljacVo value)  {
        CmdbDobavljacJdbc dao = new CmdbDobavljacJdbc();
		dao.daoRemove(value);
		return value;
    }
    //mrezni cvor 
    public CmdbMrezniCvorRs procitajSveDobavljace(CmdbMrezniCvorVo value)  {
        CmdbMrezniCvorJdbc dao = new CmdbMrezniCvorJdbc();
        CmdbMrezniCvorRs rs = new CmdbMrezniCvorRs(dao.daoFind(value));
		return rs;
    }
    public CmdbMrezniCvorVo azurirajDobavljaca(CmdbMrezniCvorVo value)  {
        CmdbMrezniCvorJdbc dao = new CmdbMrezniCvorJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbMrezniCvorVo dodajDobavljaca(CmdbMrezniCvorVo value)  {
        CmdbMrezniCvorJdbc dao = new CmdbMrezniCvorJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbMrezniCvorVo brisiDobavljaca(CmdbMrezniCvorVo value)  {
        CmdbMrezniCvorJdbc dao = new CmdbMrezniCvorJdbc();
		dao.daoRemove(value);
		return value;
    }
    public AS2RecordList listajSveKategorijeImovine()  {
       CMDBJdbc dao = new CMDBJdbc();
        return dao.daoFindListu(CMDBJdbc.SQL_KATEGORIJA_IMOVINE,null);
    }
    //Org.jedinica
    public CmdbOrgJedinicaVo azurirajOrgJedinicu(CmdbOrgJedinicaVo value)  {
        CmdbOrgJedinicaJdbc dao = new CmdbOrgJedinicaJdbc();
		dao.daoStore(value);
		return value;
    }
    public CmdbOrgJedinicaVo dodajOrgJedinicu(CmdbOrgJedinicaVo value)  {
        CmdbOrgJedinicaJdbc dao = new CmdbOrgJedinicaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public CmdbOrgJedinicaVo brisiOrgJedinicu(CmdbOrgJedinicaVo value)  {
        CmdbOrgJedinicaJdbc dao = new CmdbOrgJedinicaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public CmdbOrgJedinicaRs procitajSveOrgJedinice(CmdbOrgJedinicaVo value)  {
        CmdbOrgJedinicaJdbc dao = new CmdbOrgJedinicaJdbc();
        CmdbOrgJedinicaRs rs = new CmdbOrgJedinicaRs(dao.daoFind(value));
		return rs;
    }
    public AS2RecordList listajSvePoslovnice()  {
        CmdbOrgJedinicaJdbc dao = new CmdbOrgJedinicaJdbc();
        return dao.daoFindListu(CmdbOrgJedinicaJdbc.POSLOVNICA_SQL);
    }
    public AS2RecordList listajSveKorisnickeAplikacije()  {
    	CmdbImovinaJdbc dao = new CmdbImovinaJdbc();
        return dao.daoFindListu(CmdbImovinaJdbc.SOFTWARE_SQL);
    }
}
