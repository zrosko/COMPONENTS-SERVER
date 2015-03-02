package hr.adriacomsoftware.app.server.pranjenovca.crnalista.facade;

import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaAdresaOsobeVo;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaDokumentOsobeVo;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaOsobaVo;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaRs;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.dto.CrnaListaVo;
import hr.adriacomsoftware.app.common.pranjenovca.crnalista.facade.CrnaListaFacade;
import hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc.PrnCrnaListaAdresaJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc.PrnCrnaListaDokumentJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc.PrnCrnaListaDrzavaJdbc;
import hr.adriacomsoftware.app.server.pranjenovca.crnalista.da.jdbc.PrnCrnaListaOsobaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class CrnaListaFacadeServer extends AS2FacadeServerLayer
		implements CrnaListaFacade {

	private static CrnaListaFacadeServer _instance = null; 
	public static CrnaListaFacadeServer getInstance() {
		if (_instance == null){
			_instance = new CrnaListaFacadeServer();
		}
		return _instance;
	}
	private CrnaListaFacadeServer(){
		AS2Context.setSingletonReference(this);
	}
    public CrnaListaRs pronadiOsobeNaCrnimListama(CrnaListaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoSearchOsoba(value);
    }
    public CrnaListaRs procitajSveOsobeCrnihListi(CrnaListaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoFindOsoba(value);
    }
    public AS2RecordList procitajSveAdreseOsobeNaCrnojListi(CrnaListaOsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoListajAdreseOsobe(value);
    }
    public AS2RecordList procitajSveDokumenteOsobeNaCrnojListi(CrnaListaOsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoListajDokumenteOsobe(value);
    }
    public AS2RecordList procitajOsobuNaCrnojListi(CrnaListaOsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        return dao.daoSearchOsoba(new CrnaListaVo(value));
    }
    public AS2RecordList procitajDrzaveNaCrnimListama(CrnaListaVo value)  {
        PrnCrnaListaDrzavaJdbc dao = new PrnCrnaListaDrzavaJdbc();
        return dao.daoListajDrave(value);
    }
    public AS2RecordList procitajRizicneDjelatnosti(CrnaListaVo value)  {
        PrnCrnaListaDrzavaJdbc dao = new PrnCrnaListaDrzavaJdbc();
        return dao.daoListajDjelatnosti(value);
    }
    public CrnaListaOsobaVo azurirajOsobu(CrnaListaOsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
        dao.daoStore(value);
        return value;
    }
    public CrnaListaOsobaVo dodajOsobu(CrnaListaOsobaVo value)  {
        PrnCrnaListaOsobaJdbc dao = new PrnCrnaListaOsobaJdbc();
		value.setIdOsobe(dao.daoZadnjiBrojOsobe(value));
		value.setVrsta(value.getVrsta1());
        dao.daoCreate(value);
		value.setId(dao.daoId(value));
        return value;
    }
    public CrnaListaAdresaOsobeVo azurirajAdresu(CrnaListaAdresaOsobeVo value)  {
        PrnCrnaListaAdresaJdbc dao = new PrnCrnaListaAdresaJdbc();
        dao.daoStore(value);
        return value;
    }
    public CrnaListaAdresaOsobeVo dodajAdresu(CrnaListaAdresaOsobeVo value)  {
        PrnCrnaListaAdresaJdbc dao = new PrnCrnaListaAdresaJdbc();
		value.setIdAdrese(dao.daoZadnjiBrojAdrese(value));
        dao.daoCreate(value);
        return value;
    }
    public CrnaListaAdresaOsobeVo brisiAdresu(CrnaListaAdresaOsobeVo value)  {
        PrnCrnaListaAdresaJdbc dao = new PrnCrnaListaAdresaJdbc();
        dao.daoRemove(value);
        return value;
    }
    public CrnaListaDokumentOsobeVo azurirajDokument(CrnaListaDokumentOsobeVo value)  {
        PrnCrnaListaDokumentJdbc dao = new PrnCrnaListaDokumentJdbc();
        dao.daoStore(value);
        return value;
    }
    public CrnaListaDokumentOsobeVo dodajDokument(CrnaListaDokumentOsobeVo value)  {
        PrnCrnaListaDokumentJdbc dao = new PrnCrnaListaDokumentJdbc();
		value.setIdDokumenta(dao.daoZadnjiBrojDokumenta(value));
        dao.daoCreate(value);
        return value;
    }
    public CrnaListaDokumentOsobeVo brisiDokument(CrnaListaDokumentOsobeVo value)  {
        PrnCrnaListaDokumentJdbc dao = new PrnCrnaListaDokumentJdbc();
        dao.daoRemove(value);
        return value;
    }
}
