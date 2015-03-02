package hr.adriacomsoftware.app.server.dionice.facade;

import hr.adriacomsoftware.app.common.dionice.facade.DioniceFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.server.dionice.da.jdbc.DioSifrarnikJdbc;
import hr.adriacomsoftware.app.server.dionice.da.jdbc.DionicaJdbc;
import hr.adriacomsoftware.app.server.dionice.da.jdbc.UpisnicaJdbc;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class DioniceFacadeServer extends AS2FacadeServerLayer
		implements DioniceFacade {

	private static DioniceFacadeServer _instance = null;
	public static DioniceFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new DioniceFacadeServer();
		}
		return _instance;
	}
	private DioniceFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
	//DOKAPITALIZACIJA
	public AS2Record citajUpisnicu(AS2Record value) throws Exception {
		UpisnicaJdbc dao = new UpisnicaJdbc();
		return dao.daoLoadUpisnica(value);
	}
	public AS2RecordList procitajSveUpisniceKruga(AS2Record value) throws Exception {
		UpisnicaJdbc dao = new UpisnicaJdbc();
		return dao.daoFindUpisnice(value);
	}
	public AS2Record dodajUpisnicu(AS2Record value) throws Exception {
		UpisnicaJdbc dao = new UpisnicaJdbc();
		dao.daoCreate(value);
		return value;
	}
	public AS2Record azurirajUpisnicu(AS2Record value) throws Exception {
		UpisnicaJdbc dao = new UpisnicaJdbc();
		dao.daoStore(value);
		return value;
	}
	public AS2Record brisiUpisnicu(AS2Record value) throws Exception {
		UpisnicaJdbc dao = new UpisnicaJdbc();
		value.set("ispravno", "0");
		dao.daoStore(value);
		return value;
	}

	public AS2Record citajPodatkeDionicara(AS2Record value) throws Exception {
		UpisnicaJdbc dao = new UpisnicaJdbc();
		return dao.daoFindDionicarByOib(value);
	}
	//DIONICE
	public OsnovniRs dioniceRekapitulacija1(OsnovniVo value) throws Exception {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoQuerySpDatum("bi_dionice_rekapitulacija_1", value);
    }
    public OsnovniRs dioniceRekapitulacija2(OsnovniVo value) throws Exception {
        BankarskiJdbc dao = new BankarskiJdbc();
        return dao.daoQuerySpDatum("bi_dionice_rekapitulacija_2", value);
    }
    public OsnovniRs dioniceStanjeNaDan(OsnovniVo value) throws Exception {
        DionicaJdbc dao = new DionicaJdbc();
        return new OsnovniRs(dao.daoFindListuDionicaraNaDan(value));
    }
    public OsnovniRs dioniceUsporednoStanje(OsnovniVo value) throws Exception {
        DionicaJdbc dao = new DionicaJdbc();
        return dao.daoUsporednoStanje(value);
    } 
    public OsnovniRs citajDionice(OsnovniVo value) throws Exception {
        DionicaJdbc dao = new DionicaJdbc();
        OsnovniRs rs = new OsnovniRs(dao.daoListaDionica(value));
		return rs;
    }
    public OsnovniRs pronadiDionice(OsnovniVo value) throws Exception {
        DionicaJdbc dao = new DionicaJdbc();
        OsnovniRs rs = new OsnovniRs(dao.daoPronadiDionice(value));
		return rs;
    }
  //ŠIFRARNIK
  	public AS2RecordList procitajSifrarnik(AS2Record value) throws Exception {
  		DioSifrarnikJdbc dao = new DioSifrarnikJdbc();
  		return dao.daoFindSifrarnik(value, false);
  	}
}
