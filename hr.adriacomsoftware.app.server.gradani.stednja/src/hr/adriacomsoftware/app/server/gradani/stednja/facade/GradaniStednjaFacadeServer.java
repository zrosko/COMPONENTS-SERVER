package hr.adriacomsoftware.app.server.gradani.stednja.facade;

import hr.adriacomsoftware.app.common.gradani.dto.OsobaRs;
import hr.adriacomsoftware.app.common.gradani.stednja.dto.StednjaVo;
import hr.adriacomsoftware.app.common.gradani.stednja.facade.GradaniStednjaFacade;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniRs;
import hr.adriacomsoftware.app.common.jb.dto.OsnovniVo;
import hr.adriacomsoftware.app.common.jb.dto.PartijaRs;
import hr.adriacomsoftware.app.server.gradani.da.jdbc.PartijaGradanaJdbc;
import hr.adriacomsoftware.app.server.gradani.stednja.da.jdbc.GradaniObavijestJdbc;
import hr.adriacomsoftware.app.server.gradani.stednja.da.jdbc.PartijaStednjaGradanaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.exceptions.AS2BusinessLogicException;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class GradaniStednjaFacadeServer extends AS2FacadeServerLayer
		implements GradaniStednjaFacade{

	private static GradaniStednjaFacadeServer _instance = null;
	public static GradaniStednjaFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new GradaniStednjaFacadeServer();
		}
		return _instance;
	}
	private GradaniStednjaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public OsobaRs pronadiSveOsobe(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc(); 
        return dao.daoPronadiOsobe(value,true);
    }
	public OsobaRs procitajSveOsobe()  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc(); 
        return dao.daoPronadiOsobe(new OsnovniVo(),false);
	 }
    public OsnovniVo azurirajPartijuZaObavijest(OsnovniVo value)  {
        GradaniObavijestJdbc dao = new GradaniObavijestJdbc();
        PartijaGradanaJdbc dao_partija = new PartijaGradanaJdbc();
        if (!dao_partija.daoFindIfExists(value))
	        throw new AS2BusinessLogicException("10018");
		dao.daoStore(value);
		return value;
    }
    public OsnovniRs procitajSvePartijeZaObavijest(OsnovniVo value)  {
        GradaniObavijestJdbc dao = new GradaniObavijestJdbc();
        OsnovniRs rs = new OsnovniRs(dao.daoFind(value));
 		return rs;
    }
    public OsnovniVo dodajPartijuZaObavijest(OsnovniVo value)  {
        GradaniObavijestJdbc dao = new GradaniObavijestJdbc();
        PartijaGradanaJdbc dao_partija = new PartijaGradanaJdbc();
	    if (!dao_partija.daoFindIfExists(value))
	        throw new AS2BusinessLogicException("10018");
		dao.daoCreate(value);
		return value;
    }
    public OsnovniVo brisiPartijuZaObavijest(OsnovniVo value)  {
        GradaniObavijestJdbc dao = new GradaniObavijestJdbc();
		dao.daoRemove(value);
		return value;
    }
    public OsnovniRs izvjestajObavijestiDoznaka(OsnovniVo value)  {
        GradaniObavijestJdbc dao = new GradaniObavijestJdbc();
		return new OsnovniRs(dao.daoListaObavijesti(value));
    }
    public OsnovniRs listaNegativnihPartija(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc(); 
        return dao.daoListaNegativnihPartija(value);
    }
    public OsnovniRs listaPrometaPogresnaValuta(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc(); 
        return dao.daoListaPrometaPogresnaValuta(value);
    }
    public PartijaRs listaPartijaNerezidenata(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc(); 
        return dao.daoPronadiNerezidente(value);
    }
    public PartijaRs listaTecajnihRazlika(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc(); 
        return dao.daoListaTecajnihRazlika(value);
    }
    public PartijaRs listaStednjaFiksnaKamata(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc(); 
        return dao.daoStednjaFiksnaKamata(value);
    }
    public OsnovniRs izvjestajObavijestiMirovine(OsnovniVo value)  {
        GradaniObavijestJdbc dao = new GradaniObavijestJdbc();
		return new OsnovniRs(dao.daoListaMirovine(value));
    }
    public OsnovniRs brojPartijaDepozitaNaDan(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoQueryFnDatum(value,"bi_fn_gr_stednja_broj_partija"); 
    }
    public OsnovniRs rekapitulacijaPripisaKamataDevize(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoQueryFnDatumOdDatumDo(value,"bi_fn_gr_stednja_devize_pripis_kamate_rekapitulacija");
    }
    public OsnovniRs rekapitulacijaPripisaKamataKune(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoQueryFnDatumOdDatumDo(value,"bi_fn_gr_stednja_kune_pripis_kamate_rekapitulacija");
    }
    public OsnovniRs priljevOdljev(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoQueryFnDatumOdDatumDo(value,"bi_fn_gr_stednja_izvjestaj_likvidnost_priljev_odljev");
    }
    public OsnovniRs izvjestajPoredakStednje(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoPoredakStednja(value);
    }
    public OsnovniRs izvjestajKunskaStednja(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        OsnovniRs res = dao.daoKunskaStednja(value);
        res.addResultSet("kunska_stednja_detalji",res);
        return res;
    }
    public OsnovniRs izvjestajKunskaStednjaBrojPartija(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        OsnovniRs res = dao.daoKunskaStednjaBrojPartija(value);
    	res.addResultSet("kunska_stednja_br_partija_detalji",res);
        return res;  	
    }
    public OsnovniRs izvjestajDeviznaStednja(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        OsnovniRs res = dao.daoDeviznaStednja(value);
    	res.addResultSet("devizna_stednja_detalji",res);
    	return res;  
    }
    public OsnovniRs izvjestajDeviznaStednjaBrojPartija(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        OsnovniRs res = dao.daoDeviznaStednjaBrojPartija(value);
    	res.addResultSet("devizna_stednja_br_partija_detalji",res);
    	return res;  
    }
    public OsnovniRs izvjestajOrocenaStednja(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        OsnovniRs res = dao.daoOrocenaStednja(value,1);
        res.addResultSet("orocena_stednja_detalji1",res);
        OsnovniRs res2 = dao.daoOrocenaStednja(value,2);
    	res.addResultSet("orocena_stednja_detalji2",res2);
    	return res;  
    }
    public OsnovniRs izvjestajOrocenaStednjaPartije(StednjaVo value)  {
		PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
		String org = value.getOrganizacijskaJedinica();
		String prof = value.getProfitniCentar();
		String ozn = value.getOznakaValute();
		String roc = value.getRocnost();
		String vk = value.getValutnaKlauzula();
		return dao.daoOrocenaStednjaPartije(value,org,prof,ozn,roc,vk);
}
    public OsnovniRs izvjestajTransakcijePreko105000(StednjaVo value)  {
        return new OsnovniRs();
    }
    public OsnovniRs izvjestajPregledPartijaKonto924061(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoPregledPartijaKonto924061(value);
    }
    public OsnovniRs izvjestajNeregularnoZatvorenePartije(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoNeregularnoZatvorenePartije(value);
    } 
    public OsnovniRs izvjestajRekapitulacijaPoKontima(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoRekapitulacijaPoKontima(value);
    }
    public OsnovniRs izvjestajRekapitulacijaPoValutama(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoRekapitulacijaPoValutama(value);
    }
    public OsnovniRs izvjestajDeviznaStednjaPoIntervalima(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoDeviznaStednjaPoIntervalima(value);
    }
    public OsnovniRs izvjestajNajvecih20(OsnovniVo value)  {
        PartijaStednjaGradanaJdbc dao = new PartijaStednjaGradanaJdbc();
        return dao.daoNajvecih20(value);
    }
}
