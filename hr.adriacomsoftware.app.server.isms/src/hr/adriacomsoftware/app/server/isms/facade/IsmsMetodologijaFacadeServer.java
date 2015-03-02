package hr.adriacomsoftware.app.server.isms.facade;

import hr.adriacomsoftware.app.common.isms.dto.IsmsImovinaVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsKategorijaImovineRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsKategorijaImovineVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsKontrolaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsKontrolaVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsLokacijaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsLokacijaVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsPrijetnjaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsPrijetnjaVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRanjivostRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRanjivostVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsVo;
import hr.adriacomsoftware.app.common.isms.dto.IsmsZgradaRs;
import hr.adriacomsoftware.app.common.isms.dto.IsmsZgradaVo;
import hr.adriacomsoftware.app.common.isms.facade.IsmsMetodologijaFacade;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsKategorijaImovineJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsKontrolaJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsLokacijaJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsMetodologijaJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsPrijetnjaJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsRanjivostJdbc;
import hr.adriacomsoftware.app.server.isms.da.jdbc.IsmsZgradaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class IsmsMetodologijaFacadeServer extends AS2FacadeServerLayer
		implements IsmsMetodologijaFacade {

	private static IsmsMetodologijaFacadeServer _instance = null;
	public static IsmsMetodologijaFacadeServer getInstance(){
		if (_instance == null){
			_instance = new IsmsMetodologijaFacadeServer();
		}
		return _instance;
	}
	private IsmsMetodologijaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public IsmsVo azurirajMetodologiju(IsmsVo value)  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        dao.setTableName(value.get(IsmsVo.ISMS__TABLE));
        dao.daoStore(value);
        return new IsmsVo();
    }
    public IsmsRs procitajSveMetodologije(IsmsVo value)  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        if(value.get(IsmsVo.ISMS__TABLE).length()==0)
            value.set(IsmsVo.ISMS__TABLE, "isms_povjerljivost");
        dao.setTableName(value.get(IsmsVo.ISMS__TABLE));
        return new IsmsRs(dao.daoFind(value));
    }
    public IsmsVo dodajMetodologiju(IsmsVo value)  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        dao.setTableName(value.get(IsmsVo.ISMS__TABLE));
        dao.daoCreate(value);
        return new IsmsVo();
    }
    public IsmsVo brisiMetodologiju(IsmsVo value)  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        dao.setTableName(value.get(IsmsVo.ISMS__TABLE));
        dao.daoRemove(value);
        return new IsmsVo();
    }
    public AS2RecordList listajSveKategorijeImovine()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_KATEGORIJA_IMOVINE,null);
    }
    public AS2RecordList listajSveKategorijeImovineKratko()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_KATEGORIJA_IMOVINE_KRATKO,null);
    }
    public AS2RecordList listajSveZgrade()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_ZGRADE," ORDER BY id_zgrade");
    }
    public AS2RecordList listajSvaRanjivosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_RANJIVOSTI,null);
    }
    public AS2RecordList listajSvePrijetnje()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_PRIJETNJE,null);
    }
    public AS2RecordList listajSveKontrole()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_KONTROLE," ORDER BY oznaka");
    }
    public AS2RecordList listajSvePovjerljivosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_POVJERLJIVOST,null);
    }
    public AS2RecordList listajSveCjelovitosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_CJELOVITOST,null);
    }
    public AS2RecordList listajSveRaspolozivosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_RASPOLOZIVOST,null);
    }
    public AS2RecordList listajSveVaznosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_VAZNOST,null);
    }
    //prijetnje
    public IsmsPrijetnjaRs citajSvePrijetnje(IsmsPrijetnjaVo value)  {
        IsmsPrijetnjaJdbc dao = new IsmsPrijetnjaJdbc();
        IsmsPrijetnjaRs rs = new IsmsPrijetnjaRs(dao.daoListaPrijetnji(value));
		return rs;
    }
    public IsmsPrijetnjaRs pronadiSvePrijetnje(IsmsPrijetnjaVo value)  {
        IsmsPrijetnjaJdbc dao = new IsmsPrijetnjaJdbc();
        IsmsPrijetnjaRs rs = new IsmsPrijetnjaRs(dao.daoPronadiPrijetnje(value));
		return rs;
    }
    public IsmsPrijetnjaVo dodajPrijetnju(IsmsPrijetnjaVo value)  {
        IsmsPrijetnjaJdbc dao = new IsmsPrijetnjaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsPrijetnjaVo citajPrijetnju(IsmsPrijetnjaVo value)  {
        IsmsPrijetnjaJdbc dao = new IsmsPrijetnjaJdbc();
		dao.daoLoad(value);
		return value;
    }
    public IsmsPrijetnjaVo duplicirajPrijetnju(IsmsPrijetnjaVo value)  {
        IsmsPrijetnjaJdbc dao = new IsmsPrijetnjaJdbc();
        value.setIdPrijetnje(null);
		dao.daoCreate(value);
		return value;
    }
    public IsmsPrijetnjaVo brisiPrijetnju(IsmsPrijetnjaVo value)  {
        IsmsPrijetnjaJdbc dao = new IsmsPrijetnjaJdbc();
        IsmsPrijetnjaVo vo = new IsmsPrijetnjaVo();
        vo.setIdPrijetnje(value.getIdPrijetnje());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public IsmsPrijetnjaVo azurirajPrijetnju(IsmsPrijetnjaVo value)  {
        IsmsPrijetnjaJdbc dao = new IsmsPrijetnjaJdbc();
		dao.daoStore(value);
		return value;
    }
    public AS2RecordList listajSveVrstePrijetnje()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_PRIJETNJA_VRSTA,null);
    }
    public AS2RecordList listajSveVjerojatnostiPrijetnje()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_VJEROJATNOST_PRIJETNJE,null);
    }
    //ranjivosti
    public IsmsRanjivostRs citajSveRanjivosti(IsmsRanjivostVo value)  {
        IsmsRanjivostJdbc dao = new IsmsRanjivostJdbc();
        IsmsRanjivostRs rs = new IsmsRanjivostRs(dao.daoListaRanjivosti(value));
		return rs;
    }
    public IsmsRanjivostRs pronadiSveRanjivosti(IsmsRanjivostVo value)  {
        IsmsRanjivostJdbc dao = new IsmsRanjivostJdbc();
        IsmsRanjivostRs rs = new IsmsRanjivostRs(dao.daoPronadiRanjivosti(value));
		return rs;
    }
    public IsmsRanjivostVo dodajRanjivost(IsmsRanjivostVo value)  {
        IsmsRanjivostJdbc dao = new IsmsRanjivostJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsRanjivostVo citajRanjivost(IsmsRanjivostVo value)  {
        IsmsRanjivostJdbc dao = new IsmsRanjivostJdbc();
		dao.daoLoad(value);
		return value;
    }
    public IsmsRanjivostVo duplicirajRanjivost(IsmsRanjivostVo value)  {
        IsmsRanjivostJdbc dao = new IsmsRanjivostJdbc();
        value.setIdRanjivosti(null);
		dao.daoCreate(value);
		return value;
    }
    public IsmsRanjivostVo brisiRanjivost(IsmsRanjivostVo value)  {
        IsmsRanjivostJdbc dao = new IsmsRanjivostJdbc();
        IsmsRanjivostVo vo = new IsmsRanjivostVo();
        vo.setIdRanjivosti(value.getIdRanjivosti());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public IsmsRanjivostVo azurirajRanjivost(IsmsRanjivostVo value)  {
        IsmsRanjivostJdbc dao = new IsmsRanjivostJdbc();
		dao.daoStore(value);
		return value;
    }
    public AS2RecordList listajSveVrsteRanjivosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_RANJIVOST_VRSTA,null);
    }
    public AS2RecordList listajSvePosljedicaRanjivosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_POSLJEDICA_RANJIVOSTI,null);
    }
    //zgrada
    public IsmsZgradaRs procitajSveZgrade(IsmsZgradaVo value)  {
        IsmsZgradaJdbc dao = new IsmsZgradaJdbc();
        IsmsZgradaRs rs = new IsmsZgradaRs(dao.daoFind(value));
		return rs;
    }
    public IsmsZgradaVo azurirajZgradu(IsmsZgradaVo value)  {
        System.out.println(value);
        IsmsZgradaJdbc dao = new IsmsZgradaJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsZgradaVo dodajZgradu(IsmsZgradaVo value)  {
        IsmsZgradaJdbc dao = new IsmsZgradaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsZgradaVo brisiZgradu(IsmsZgradaVo value)  {
        IsmsZgradaJdbc dao = new IsmsZgradaJdbc();
		dao.daoRemove(value);
		return value;
    }
    //lokacija
    public IsmsLokacijaRs procitajSveLokacije(IsmsLokacijaVo value)  {
        IsmsLokacijaJdbc dao = new IsmsLokacijaJdbc();
        IsmsLokacijaRs rs = new IsmsLokacijaRs(dao.daoFind(value));
		return rs;
    }
    public IsmsLokacijaVo azurirajLokaciju(IsmsLokacijaVo value)  {
        IsmsLokacijaJdbc dao = new IsmsLokacijaJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsLokacijaVo dodajLokaciju(IsmsLokacijaVo value)  {
        IsmsLokacijaJdbc dao = new IsmsLokacijaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsLokacijaVo brisiLokaciju(IsmsLokacijaVo value)  {
        IsmsLokacijaJdbc dao = new IsmsLokacijaJdbc();
		dao.daoRemove(value);
		return value;
    }
    public AS2RecordList listajSveOdgovornosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_ODGOVORNOST,null);
    }
    public AS2RecordList listajSveDokazivosti()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_DOKAZIVOST,null);
    }
    public AS2RecordList listajSveVjerojatnostiOtkrivanja()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_VJEROJATNOST_OTKRIVANJA,null);
    }
    public AS2RecordList listajSveRanjivostiVrsta(IsmsRanjivostVo value)  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        String _where = "where vrsta_ranjivosti = '"+value.getVrstaRanjivosti()+"' ";
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_RANJIVOSTI+_where,null);
    }
    public AS2RecordList listajSvePrijetnjeVrsta(IsmsPrijetnjaVo value)  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        String _where = "where vrsta_prijetnje = '"+value.getVrstaPrijetnje()+"' ";
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_PRIJETNJE+_where,null);
    }
    public AS2RecordList listajSveKategorijeImovineVrsta(IsmsImovinaVo value)  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        String _where = " and id_kategorije like '"+value.get("kategorija_imovine")+"%' ";
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_KATEGORIJA_IMOVINE+_where,null);
    }
    public AS2RecordList listajSveLokacijeVrsta(IsmsLokacijaVo value)  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        String _where = "where L.id_zgrade = '"+value.getIdZgrade()+"' ";
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_LOKACIJE+_where,"ORDER BY L.id_zgrade, L.kat, L.oznaka_sobe");
    }
    public AS2RecordList listajSveLokacije()  {
        IsmsMetodologijaJdbc dao = new IsmsMetodologijaJdbc();
        return dao.daoFindListu(IsmsMetodologijaJdbc.SQL_LOKACIJE,"ORDER BY L.id_zgrade, L.kat, L.oznaka_sobe");
    }
    public IsmsKontrolaRs citajSveKontrole(IsmsKontrolaVo value)  {
        IsmsKontrolaJdbc dao = new IsmsKontrolaJdbc();
        IsmsKontrolaRs rs = new IsmsKontrolaRs(dao.daoListaKontrola(value));
		return rs;
    }
    public IsmsKontrolaRs pronadiSveKontrole(IsmsKontrolaVo value)  {
        IsmsKontrolaJdbc dao = new IsmsKontrolaJdbc();
        IsmsKontrolaRs rs = new IsmsKontrolaRs(dao.daoPronadiKontrole(value));
		return rs;
    }
    public IsmsKontrolaVo dodajKontrolu(IsmsKontrolaVo value)  {
        IsmsKontrolaJdbc dao = new IsmsKontrolaJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsKontrolaVo citajKontrolu(IsmsKontrolaVo value)  {
        IsmsKontrolaJdbc dao = new IsmsKontrolaJdbc();
		value = new IsmsKontrolaVo(dao.daoLoad(value));
		return value;
    }
    public IsmsKontrolaVo duplicirajKontrolu(IsmsKontrolaVo value)  {
        IsmsKontrolaJdbc dao = new IsmsKontrolaJdbc();
        value.setIdKontrole(null);
		dao.daoCreate(value);
		return value;
    }
    public IsmsKontrolaVo brisiKontrolu(IsmsKontrolaVo value)  {
        IsmsKontrolaJdbc dao = new IsmsKontrolaJdbc();
        IsmsKontrolaVo vo = new IsmsKontrolaVo();
        vo.setIdKontrole(value.getIdKontrole());
        vo.setIspravnoNE();
		dao.daoStore(vo);
		return value;
    }
    public IsmsKontrolaVo azurirajKontrolu(IsmsKontrolaVo value)  {
        IsmsKontrolaJdbc dao = new IsmsKontrolaJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsKontrolaRs citajSveKontroleStatus(IsmsKontrolaVo value)  {
        IsmsKontrolaJdbc dao = new IsmsKontrolaJdbc();
        IsmsKontrolaRs rs = new IsmsKontrolaRs(dao.daoListaKontrolaStatus(value));
		return rs;
    }
    //kategorije imovine
    public IsmsKategorijaImovineRs procitajSveKategorije(IsmsKategorijaImovineVo value)  {
        IsmsKategorijaImovineJdbc dao = new IsmsKategorijaImovineJdbc();
        return new IsmsKategorijaImovineRs(dao.daoFind(value));
    }
    public IsmsKategorijaImovineVo azurirajKategoriju(IsmsKategorijaImovineVo value)  {
        IsmsKategorijaImovineJdbc dao = new IsmsKategorijaImovineJdbc();
		dao.daoStore(value);
		return value;
    }
    public IsmsKategorijaImovineVo dodajKategoriju(IsmsKategorijaImovineVo value)  {
        IsmsKategorijaImovineJdbc dao = new IsmsKategorijaImovineJdbc();
		dao.daoCreate(value);
		return value;
    }
    public IsmsKategorijaImovineVo brisiKategoriju(IsmsKategorijaImovineVo value)  {
        IsmsKategorijaImovineJdbc dao = new IsmsKategorijaImovineJdbc();
		dao.daoRemove(value);
		return value;
    }
}
