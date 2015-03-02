package hr.adriacomsoftware.app.server.jb.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.dto.OsobaVo;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.common.jb.facade.BankaFacade;
import hr.adriacomsoftware.app.server.jb.da.jdbc.BankaJdbc;
import hr.adriacomsoftware.app.server.jb.da.jdbc.HborJdbc;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.security.user.AS2User;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class BankaFacadeServer extends AS2FacadeServerLayer
		implements BankaFacade {

	private static BankaFacadeServer _instance = null;
	public static BankaFacadeServer getInstance()
	{
		if (_instance == null){
			_instance = new BankaFacadeServer();
		}
		return _instance;
	}
	private BankaFacadeServer(){
	    AS2Context.setSingletonReference(this);
	}
    public AS2RecordList listajSvePoslovnice()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.POSLOVNICA_SQL);
    }
    public AS2RecordList listajSveValute()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.VALUTA_SQL);
    }
    public AS2RecordList listajSveOpcine()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListuOrderById(BankarskiJdbc.OPCINA_SQL);
    }
    public AS2RecordList listajSvaMjesta()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.MJESTO_SQL);
    }
    public AS2RecordList listajSveZupanije()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListuOrderById(BankarskiJdbc.ZUPANIJA_SQL);
    }
    public AS2RecordList listajSveDrzave(OsnovniVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListuOrderById(BankarskiJdbc.DRZAVA_SQL);
    }
    public AS2RecordList listajSveBanke()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.BANKA_SQL);
    }
    public AS2RecordList listajSveBankeKratka()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.BANKA_KRATKA_SQL);
    }
    public AS2RecordList listajSveRadnike()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.RADNIK_SQL);
    }
    public AS2RecordList listajSveRadnikeKorisnike()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.DJELATNIK_DOMENA_SQL);
    }
    public AS2RecordList listajSveDjelatnike()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT RTRIM(ime) + ' ' + RTRIM(prezime) AS ime_prezime, ");
        sql.append("RTRIM(email) AS email, org_jedinica_rada, ugovorno_mjesto ");
        sql.append("FROM CMDB_PROD.dbo.cmdb_djelatnik " );
        sql.append("WHERE (org_jedinica_rada BETWEEN 15000 AND 16999) ");
        sql.append("OR (org_jedinica_rada BETWEEN 10030 AND 10040) ");
        sql.append("OR (org_jedinica_rada in (40970,40160,40190,40910,40920,40940)) ");
        sql.append("ORDER BY org_jedinica_rada");
        return dao.daoExecuteQuery(sql.toString());
    }
    public AS2RecordList listajSveDjelatnikeBanke()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoExecuteQuery(BankarskiJdbc.DJELATNIK_BANKA_SQL);
    }
    public AS2RecordList listajSveKorisnikeBanke()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoExecuteQuery(BankarskiJdbc.KORISNIK_BANKA_SQL);
    }
    public AS2RecordList listajSveDjelatnikeBankeIT()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoExecuteQuery(BankarskiJdbc.DJELATNIK_BANKA_IT_SQL);
    }
    public AS2RecordList listajSveDjelatnikeJmbg()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoExecuteQuery(BankarskiJdbc.DJELATNIK_ZAHTJEVI_SQL);
    }
    public AS2RecordList listajSveVrsteTrajnihNaloga()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.TRAJNI_NALOZI_SQL);
    }
    //liste
    public AS2RecordList izvjestajTecajnaLista(OsnovniVo value)  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoFindTecajnuListu(value);
    }
    public AS2RecordList izvjestajOrganizacijskeJedinice()  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoFindListu(BankaJdbc.POSLOVNICA_SQL);
    }
    public AS2RecordList izvjestajTipoviRacuna()  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoFindListu(BankaJdbc.TIP_RACUNA_SQL);
    }
    public AS2RecordList izvjestajBanke()  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoFindListu(BankaJdbc.BANKE_SQL);
    }
    public AS2RecordList izvjestajKontniPlan()  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoFindListu(BankaJdbc.KONTNI_PLAN_SQL);
    }
    public AS2RecordList izvjestajVrstePrometaRacuna()  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoFindListu(BankaJdbc.VRSTA_PROMETA_RACUNA_SQL);
    }
    public OsnovniRs izvjestajKreditiValutneKlauzule(OsnovniVo value)  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoCallStoredProcedure("bi_jb_krediti_valutne_klauzule", value);
    }    
    public OsnovniRs izvjestajObrazac4(OsnovniVo value)  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoFindObrazac4(value);
    }
    public OsnovniRs izvjestajObrazac1(OsnovniVo value)  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoFindObrazac1(value);
    }
    public AS2RecordList izvjestajBrutoBilanca(OsnovniVo value)  {
        BankaJdbc dao = new BankaJdbc();
        return dao.daoBrutoBilanca(value);
    }
    public AS2RecordList listajSveDjelatnosti()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListuOrderById(BankarskiJdbc.DJELATNOSTI_SQL);
    }
    public AS2RecordList listajSveDjelatnostiKratko()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListuOrderById(BankarskiJdbc.DJELATNOSTI_KRATKO_SQL);
    }
    public AS2RecordList listajSveTipovePovezanihOsoba()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListuOrderById(BankarskiJdbc.POVEZANE_OSOBE_SQL);
    }
    public AS2RecordList listajSvaRadnaMjesta()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoExecuteQuery(BankarskiJdbc.RADNO_MJESTO_SQL);
    }
    public OsobaRs pronadiOsobu(OsobaVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoPronadiOsobu(value);
    }
    public OsobaVo pronadiOsobuJmbgMb(OsobaVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoPronadiOsobuJmbgMb(value);
    }
    public OsobaVo pronadiPravnuFizickuOsobuOib(OsobaVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoPronadiPravnuFizickuOsobuOib(value);
    }
    public OsobaVo pronadiOsobuJmbg(OsobaVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoPronadiOsobuJmbg(value);
    }
    public OsobaVo pronadiOsobuOib(OsobaVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoPronadiOsobuOib(value);
    }
    public OsobaVo pronadiPravnuFizickuOsobuBrojPartije(OsobaVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoPronadiPravnuFizickuOsobuBrojPartije(value);
    }
    public AS2RecordList listajSveCrneListe()  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoFindListu(BankarskiJdbc.CRNE_LISTE_SQL);
    }
    public AS2RecordList listajSveUloge(OsnovniVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoSimpleQuery(BankarskiJdbc.ALL_ROLES);
    }
    public AS2RecordList listajSveUlogeKomponente(OsnovniVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoSimpleQuery(BankarskiJdbc.ALL_ROLES_COMPONENTS);
    }
    public PartijaRs listajSvePartijeZaOib(OsobaVo value)  {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoListaOtvorenihPartijaPravneFizicke(value);
    }
    public OsnovniVo dodajHborPartiju(OsnovniVo value)  {
        HborJdbc dao = new HborJdbc();
		AS2User user = (AS2User)value.getProperty(AS2Constants.USER_OBJ);
		value.setCalendarAsDateString(AS2Date.getTodayAsCalendar(), "vrijeme_zadnje_izmjene");
		value.set("operater_zadnje_izmjene",user.get("korisnik"));
        dao.daoCreate(value);
        return value;
    }
	public OsnovniVo provjeriHborPartiju(OsnovniVo value)  {
		HborJdbc dao = new HborJdbc();
		OsnovniVo res = dao.daoProvjeriPartiju(value);
		return res;
	}
	public OsnovniRs procitajUnesenePartije(OsnovniVo value)  {
		HborJdbc dao = new HborJdbc();
		return dao.daoLoadAll(value);
	}
	public AS2RecordList procitajSveOsobe(AS2Record value)  {
		BankarskiJdbc dao = new BankarskiJdbc();
		return dao.daoListaPravnihFizickihOsoba(value);
	}
	public AS2RecordList listajDnevniRed(OsnovniVo value)  {
		AS2RecordList rs = new AS2RecordList();
		javaxt.io.Directory folder = new javaxt.io.Directory(value.get("url"));
		if (folder.exists()) {			
			AS2Record vo;
			for (Object file : folder.getChildren()) {
					vo = new AS2Record();
					if(file instanceof javaxt.io.File)
						vo.set("fileName", ((javaxt.io.File)file).getName());
					else{
						vo.set("fileName", ((javaxt.io.Directory)file).getName());
					}			
					vo.set("datum", value.get("datum"));
					rs.addRow(vo);
			}
			for (final javaxt.io.File file : folder.getFiles()) {
				//if (!file.isDirectory()) {
					vo = new AS2Record();
					vo.set("fileName", file.getName());
					vo.set("datum", value.get("datum"));
					rs.addRow(vo);
				//}

			}
		}
		return rs;
	}
 }
