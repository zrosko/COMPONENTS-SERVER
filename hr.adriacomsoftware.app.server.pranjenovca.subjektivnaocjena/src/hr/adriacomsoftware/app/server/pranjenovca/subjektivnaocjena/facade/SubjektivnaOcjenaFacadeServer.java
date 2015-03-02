package hr.adriacomsoftware.app.server.pranjenovca.subjektivnaocjena.facade;

import hr.adriacomsoftware.app.server.pranjenovca.subjektivnaocjena.da.jdbc.PrnSubjektivnaOcjenaJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class SubjektivnaOcjenaFacadeServer extends AS2FacadeServerLayer {

	private static SubjektivnaOcjenaFacadeServer _instance = null;

	public static SubjektivnaOcjenaFacadeServer getInstance() {
		if (_instance == null) {
			_instance = new SubjektivnaOcjenaFacadeServer();
		}
		return _instance;
	}

	private SubjektivnaOcjenaFacadeServer() {
		AS2Context.setSingletonReference(this);
	}

	// fizička osoba
	public AS2RecordList procitajSveOcjene(AS2Record value)  {
		PrnSubjektivnaOcjenaJdbc dao = new PrnSubjektivnaOcjenaJdbc();
		return dao.daoFindOcjene(value);
	}

	public AS2Record azurirajSubjektivnuOcjenu(AS2Record value) {
		PrnSubjektivnaOcjenaJdbc dao = new PrnSubjektivnaOcjenaJdbc();
		return dao.daoLoadOcjena(dao.daoStore(value));
	}

	public AS2Record dodajSubjektivnuOcjenu(AS2Record value)  {
		PrnSubjektivnaOcjenaJdbc dao = new PrnSubjektivnaOcjenaJdbc();
		return dao.daoLoadOcjena(dao.daoCreate(value));
	}

	public AS2Record brisiSubjektivnuOcjenu(AS2Record value)  {
		PrnSubjektivnaOcjenaJdbc dao = new PrnSubjektivnaOcjenaJdbc();
		dao.daoRemove(value);
		return value;
	}

}
