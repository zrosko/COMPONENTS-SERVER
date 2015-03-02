package hr.adriacomsoftware.app.server.gradani.kreditnizahtjev.facade;

import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.KreditniZahtjevOsobaRs;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.KreditniZahtjevOsobaVo;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.KreditniZahtjevRs;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.KreditniZahtjevVo;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.MjesecneObvezeRs;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.MjesecneObvezeVo;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.PrihodiClanovaRs;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.dto.PrihodiClanovaVo;
import hr.adriacomsoftware.app.common.gradani.kreditnizahtjev.facade.KreditniZahtjevGradanaFacade;
import hr.adriacomsoftware.app.server.gradani.kreditnizahtjev.da.jdbc.KreditniZahtjevJdbc;
import hr.adriacomsoftware.app.server.gradani.kreditnizahtjev.da.jdbc.KreditniZahtjevOsobaJdbc;
import hr.adriacomsoftware.app.server.gradani.kreditnizahtjev.da.jdbc.MjesecneObvezeJdbc;
import hr.adriacomsoftware.app.server.gradani.kreditnizahtjev.da.jdbc.PrihodiClanovaJdbc;
import hr.as2.inf.common.core.AS2Constants;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class KreditniZahtjevGradanaFacadeServer extends AS2FacadeServerLayer
		implements KreditniZahtjevGradanaFacade {

	private static KreditniZahtjevGradanaFacadeServer _instance = null;
	public static KreditniZahtjevGradanaFacadeServer getInstance()	{
		if (_instance == null){
			_instance = new KreditniZahtjevGradanaFacadeServer();
		}
		return _instance;
	}
	private KreditniZahtjevGradanaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public KreditniZahtjevVo dodajKreditniZahtjev(KreditniZahtjevVo value) throws Exception {
        //dao
        KreditniZahtjevJdbc dao_kz = new KreditniZahtjevJdbc();
        KreditniZahtjevOsobaJdbc dao_osoba = new KreditniZahtjevOsobaJdbc();
//        MjesecneObvezeJdbc dao_mo = new MjesecneObvezeJdbc();
//        PrihodiClanovaJdbc dao_pc = new PrihodiClanovaJdbc();
        //podaci
        value.setTrenutnoVrijeme(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV__SIFRA_ZAHTJEVA);
        value.setStatusKredita(KreditniZahtjevVo.STATUS_KREDITA_ZAHTJEV); //pocenti status
        KreditniZahtjevOsobaVo osoba_vo = value.getKreditniZahtjevOsoba();//dohvati podatke o osobi
        value.set("datum_unosa", AS2Date.getCurrentDate());
        dao_kz.daoCreate(value); //unos u bazu
        //citaj id kreditnog zahtjeva (kreira ga sistem)
        KreditniZahtjevVo zahtjev_vo = new KreditniZahtjevVo();
        zahtjev_vo.setSifraZahtjeva(value.getSifraZahtjeva());
        zahtjev_vo = new KreditniZahtjevVo(dao_kz.daoLoadSifra(zahtjev_vo));
        value.setIdZahtjeva(zahtjev_vo.getIdZahtjeva()); //koristi kasnije za PC i MO
        osoba_vo.setIdZahtjeva(zahtjev_vo.getIdZahtjeva());//ubaci vezu sa kreditnim zahtjevom
        osoba_vo.setTipPunomoci(KreditniZahtjevOsobaVo.JB_GR_KREDITNI_ZAHTJEV_OSOBA__TIP_PUNOMOCI_DUZNIK);
        dao_osoba.daoCreate(osoba_vo);//ubaci osobu
        KreditniZahtjevOsobaVo osoba_finder = new KreditniZahtjevOsobaVo();
        osoba_finder.setIdZahtjeva(zahtjev_vo.getIdZahtjeva());
        KreditniZahtjevOsobaRs osoba_rs = new KreditniZahtjevOsobaRs(dao_osoba.daoFind(osoba_finder));//ubaci osobu
        osoba_vo = (KreditniZahtjevOsobaVo)osoba_rs.getRowAt(0);
        dodajMjesecneObvezePrihodeClanova(value, osoba_vo);
        return new KreditniZahtjevVo(); //probolem sa kljucem
    }
    public KreditniZahtjevVo dodajKreditniZahtjevJamacSuduznik(KreditniZahtjevVo value) throws Exception {
        KreditniZahtjevOsobaJdbc dao_osoba = new KreditniZahtjevOsobaJdbc();
//        MjesecneObvezeJdbc dao_mo = new MjesecneObvezeJdbc();
//        PrihodiClanovaJdbc dao_pc = new PrihodiClanovaJdbc();
        //podati
        KreditniZahtjevOsobaVo osoba_vo = value.getKreditniZahtjevOsoba();//dohvati podatke o osobi
        osoba_vo.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa kreditnim zahtjevom
        dao_osoba.daoCreate(osoba_vo);//ubaci osobu
        KreditniZahtjevOsobaVo osoba_finder = new KreditniZahtjevOsobaVo();
        osoba_finder.setIdZahtjeva(value.getIdZahtjeva());
        osoba_finder.setJmbg(osoba_vo.getJmbg());
        KreditniZahtjevOsobaRs osoba_rs = new KreditniZahtjevOsobaRs(dao_osoba.daoFindJmbg(osoba_finder));//ubaci osobu
        osoba_vo = (KreditniZahtjevOsobaVo)osoba_rs.getRowAt(0);
        //dodat prihode clanova i mjesecne obveze
        dodajMjesecneObvezePrihodeClanova(value, osoba_vo);
 
        return new KreditniZahtjevVo(); //probolem sa kljucem
    }
    private void dodajMjesecneObvezePrihodeClanova(KreditniZahtjevVo value, KreditniZahtjevOsobaVo osoba_vo)throws Exception {
        MjesecneObvezeJdbc dao_mo = new MjesecneObvezeJdbc();
        PrihodiClanovaJdbc dao_pc = new PrihodiClanovaJdbc();

        MjesecneObvezeVo mo1 = value.getMjesecneObveze1();//dohvati mjesecne obveze
        MjesecneObvezeVo mo2 = value.getMjesecneObveze2();//dohvati mjesecne obveze
        MjesecneObvezeVo mo3 = value.getMjesecneObveze3();//dohvati mjesecne obveze
        MjesecneObvezeVo mo4 = value.getMjesecneObveze4();//dohvati mjesecne obveze
        MjesecneObvezeVo mo5 = value.getMjesecneObveze5();//dohvati mjesecne obveze
        
        PrihodiClanovaVo pc1 = value.getPrihodiClanova1();//dohvati prihode clanova
        PrihodiClanovaVo pc2 = value.getPrihodiClanova2();//dohvati prihode clanova
        PrihodiClanovaVo pc3 = value.getPrihodiClanova3();//dohvati prihode clanova
        PrihodiClanovaVo pc4 = value.getPrihodiClanova4();//dohvati prihode clanova      
        mo1.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        mo1.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        mo2.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        mo2.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        mo3.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        mo3.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        mo4.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        mo4.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        mo5.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        mo5.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        
        pc1.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        pc1.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        pc2.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        pc2.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        pc3.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        pc3.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        pc4.setIdZahtjeva(value.getIdZahtjeva());//ubaci vezu sa osobom
        pc4.setIdOsobe(osoba_vo.getIdOsobe());//ubaci vezu sa osobom
        if(mo1.getVrstaObveze().length()>0)
            mo1 = new MjesecneObvezeVo(dao_mo.daoCreate(mo1));//ubaci mjesecne obveze
        if(mo2.getVrstaObveze().length()>0)
            mo2 = new MjesecneObvezeVo(dao_mo.daoCreate(mo2));//ubaci mjesecne obveze
        if(mo3.getVrstaObveze().length()>0)
            mo3 = new MjesecneObvezeVo(dao_mo.daoCreate(mo3));//ubaci mjesecne obveze
        if(mo4.getVrstaObveze().length()>0)
            mo4 = new MjesecneObvezeVo(dao_mo.daoCreate(mo4));//ubaci mjesecne obveze
        if(mo5.getVrstaObveze().length()>0)
            mo5 = new MjesecneObvezeVo(dao_mo.daoCreate(mo5));//ubaci mjesecne obveze
        
        if(pc1.getJmbg().length()>0)
            pc1 = new PrihodiClanovaVo(dao_pc.daoCreate(pc1));//ubaci prihode clanova
        if(pc2.getJmbg().length()>0)
            pc2 = new PrihodiClanovaVo(dao_pc.daoCreate(pc2));//ubaci prihode clanova
        if(pc3.getJmbg().length()>0)
            pc3 = new PrihodiClanovaVo(dao_pc.daoCreate(pc3));//ubaci prihode clanova
        if(pc4.getJmbg().length()>0)
            pc4 = new PrihodiClanovaVo(dao_pc.daoCreate(pc4));//ubaci prihode clanova
    }
    public void azurirajPrijedlog(KreditniZahtjevVo value) throws Exception {
        KreditniZahtjevJdbc dao = new KreditniZahtjevJdbc();
        if(!value.getStatusKredita().equals(KreditniZahtjevVo.STATUS_KREDITA_ZAHTJEV))
            throw new AS2Exception("999");
        dao.daoStore(value);
    }
    public void azurirajKreditniZahtjev(KreditniZahtjevVo value) throws Exception {
        KreditniZahtjevJdbc dao = new KreditniZahtjevJdbc();
        KreditniZahtjevOsobaJdbc dao_osoba = new KreditniZahtjevOsobaJdbc();
        MjesecneObvezeJdbc dao_mo = new MjesecneObvezeJdbc();
        PrihodiClanovaJdbc dao_pc = new PrihodiClanovaJdbc();
        dao.daoStore(value);
        KreditniZahtjevOsobaVo osoba_vo = value.getKreditniZahtjevOsoba();//dohvati podatke o osobi
        dao_osoba.daoStore(osoba_vo);
        //mjesečne obaveze brisi
        MjesecneObvezeVo mo = new MjesecneObvezeVo();
        mo.setIdZahtjeva(value.getIdZahtjeva());
        mo.setIdOsobe(osoba_vo.getIdOsobe());
        dao_mo.daoRemoveMo(mo);
        //prihodi clanova brisi
        PrihodiClanovaVo pc = new PrihodiClanovaVo();
        pc.setIdZahtjeva(value.getIdZahtjeva());
        pc.setIdOsobe(osoba_vo.getIdOsobe());
        dao_pc.daoRemovePc(pc);
        //dodaj novo
        dodajMjesecneObvezePrihodeClanova(value, osoba_vo);        
    }
    public void azurirajKreditniZahtjevJamacSuduznik(KreditniZahtjevVo value) throws Exception {
        KreditniZahtjevOsobaJdbc dao = new KreditniZahtjevOsobaJdbc();
        MjesecneObvezeJdbc dao_mo = new MjesecneObvezeJdbc();
        PrihodiClanovaJdbc dao_pc = new PrihodiClanovaJdbc();

        KreditniZahtjevOsobaVo osoba_vo = value.getKreditniZahtjevOsoba();//dohvati podatke o osobi
        dao.daoStore(osoba_vo);
        //mjesečne obaveze brisi
        MjesecneObvezeVo mo = new MjesecneObvezeVo();
        mo.setIdZahtjeva(value.getIdZahtjeva());
        mo.setIdOsobe(osoba_vo.getIdOsobe());
        dao_mo.daoRemoveMo(mo);
        //prihodi clanova brisi
        PrihodiClanovaVo pc = new PrihodiClanovaVo();
        pc.setIdZahtjeva(value.getIdZahtjeva());
        pc.setIdOsobe(osoba_vo.getIdOsobe());
        dao_pc.daoRemovePc(pc);
        //dodaj novo
        dodajMjesecneObvezePrihodeClanova(value, osoba_vo); 
    }
    public void brisiKreditniZahtjevJamacSuduznik(KreditniZahtjevOsobaVo value) throws Exception {
        KreditniZahtjevOsobaJdbc dao = new KreditniZahtjevOsobaJdbc();
        KreditniZahtjevOsobaVo kz_vo = new KreditniZahtjevOsobaVo();
        kz_vo.setIdZahtjeva(value.getIdZahtjeva());
        kz_vo.setIdOsobe(value.getIdOsobe());
        kz_vo.setValidIndToNotValid();
        dao.daoStore(kz_vo);
    }
    public void brisiKreditniZahtjev(KreditniZahtjevVo value) throws Exception {
        KreditniZahtjevJdbc dao = new KreditniZahtjevJdbc();
//        KreditniZahtjevOsobaJdbc dao_osoba = new KreditniZahtjevOsobaJdbc();
        KreditniZahtjevVo kz_vo = new KreditniZahtjevVo();
        kz_vo.setIdZahtjeva(value.getIdZahtjeva());
        kz_vo.setValidIndToNotValid();
        dao.daoStore(kz_vo);
        //osobu brisati (logički)
        KreditniZahtjevOsobaVo kz_osoba = new KreditniZahtjevOsobaVo();
        kz_osoba.setIdZahtjeva(value.getIdZahtjeva());
        kz_osoba.setIdOsobe(value.get(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV_OSOBA__ID_OSOBE));
        kz_osoba.setValidIndToNotValid();
        dao.daoStore(kz_osoba);
    }
    public KreditniZahtjevRs procitajSveKreditneZahtjeve() throws Exception {
        KreditniZahtjevJdbc dao = new KreditniZahtjevJdbc();
	    AS2Record criteria = new AS2Record();
	    criteria.set(AS2Constants.ORDER_BY_CLAUSE, "order by datum_zaprimanja");
	    KreditniZahtjevRs set = new KreditniZahtjevRs(dao.daoFindFromView(criteria, "bi_view_gradani_zahtjevi_kredita"));
	    return set;

    }
    public KreditniZahtjevVo citajKreditniZahtjev(KreditniZahtjevVo value) throws Exception {

        KreditniZahtjevJdbc dao_kz = new KreditniZahtjevJdbc();
        KreditniZahtjevOsobaJdbc dao_osoba = new KreditniZahtjevOsobaJdbc();
        MjesecneObvezeJdbc dao_mo = new MjesecneObvezeJdbc();
        PrihodiClanovaJdbc dao_pc = new PrihodiClanovaJdbc();
        //citaj zahtjev
        KreditniZahtjevVo zahtjev_vo = new KreditniZahtjevVo(dao_kz.daoLoad(value));
        //citaj ososbu
        KreditniZahtjevOsobaVo osoba_finder = new KreditniZahtjevOsobaVo();
        osoba_finder.setIdZahtjeva(value.get(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV_OSOBA__ID_ZAHTJEVA));
        osoba_finder.setIdOsobe(value.get(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV_OSOBA__ID_OSOBE));
        KreditniZahtjevOsobaVo osoba_vo = new KreditniZahtjevOsobaVo(dao_osoba.daoLoad(osoba_finder));
        zahtjev_vo.setKreditniZahtjevOsoba(osoba_vo);
        //mjesecne obveze
        MjesecneObvezeVo mo_vo = new MjesecneObvezeVo();
        mo_vo.setIdZahtjeva(osoba_vo.getIdZahtjeva());
        mo_vo.setIdOsobe(osoba_vo.getIdOsobe());
        value.set(AS2Constants.FIND_CRITERIA,mo_vo);
        MjesecneObvezeRs mo_rs = new MjesecneObvezeRs(dao_mo.daoFind(value));
        //prihodi clanova
        PrihodiClanovaVo pc_vo = new PrihodiClanovaVo();
        pc_vo.setIdZahtjeva(osoba_vo.getIdZahtjeva());
        pc_vo.setIdOsobe(osoba_vo.getIdOsobe());
        value.set(AS2Constants.FIND_CRITERIA,pc_vo);
        PrihodiClanovaRs pc_rs = new PrihodiClanovaRs(dao_pc.daoFind(value));
        zahtjev_vo.setMjesecneObveze(mo_rs);
        zahtjev_vo.setPrihodiClanova(pc_rs);
        return zahtjev_vo;
    }
    public KreditniZahtjevVo citajKreditniZahtjevSuduznik(KreditniZahtjevVo value) throws Exception {
        KreditniZahtjevOsobaJdbc dao_osoba = new KreditniZahtjevOsobaJdbc();
        MjesecneObvezeJdbc dao_mo = new MjesecneObvezeJdbc();
        PrihodiClanovaJdbc dao_pc = new PrihodiClanovaJdbc();
        //citaj ososbu
        KreditniZahtjevOsobaVo osoba_finder = new KreditniZahtjevOsobaVo();
        osoba_finder.setIdZahtjeva(value.get(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV_OSOBA__ID_ZAHTJEVA));
        osoba_finder.setIdOsobe(value.get(JBDataDictionary.JB_GR_KREDITNI_ZAHTJEV_OSOBA__ID_OSOBE));
        KreditniZahtjevOsobaVo osoba_vo = new KreditniZahtjevOsobaVo(dao_osoba.daoLoad(osoba_finder));
        System.out.println(osoba_vo.getJmbg());
        KreditniZahtjevVo zahtjev_vo = new KreditniZahtjevVo();
        zahtjev_vo.setIdZahtjeva(osoba_vo.getIdZahtjeva());
        zahtjev_vo.setKreditniZahtjevOsoba(osoba_vo);
        //mjesecne obveze
        MjesecneObvezeVo mo_vo = new MjesecneObvezeVo();
        mo_vo.setIdZahtjeva(osoba_vo.getIdZahtjeva());
        mo_vo.setIdOsobe(osoba_vo.getIdOsobe());
        value.set(AS2Constants.FIND_CRITERIA,mo_vo);
        MjesecneObvezeRs mo_rs = new MjesecneObvezeRs(dao_mo.daoFind(value));
        //prihodi clanova
        PrihodiClanovaVo pc_vo = new PrihodiClanovaVo();
        pc_vo.setIdZahtjeva(osoba_vo.getIdZahtjeva());
        pc_vo.setIdOsobe(osoba_vo.getIdOsobe());
        value.set(AS2Constants.FIND_CRITERIA,pc_vo);
        PrihodiClanovaRs pc_rs = new PrihodiClanovaRs(dao_pc.daoFind(value));
        zahtjev_vo.setMjesecneObveze(mo_rs);
        zahtjev_vo.setPrihodiClanova(pc_rs);
        return zahtjev_vo;
    }
}
