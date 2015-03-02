package hr.adriacomsoftware.app.server.pravneosobe.likvidnost.facade;

import hr.adriacomsoftware.app.common.pravneosobe.likvidnost.dto.LikvidnostRs;
import hr.adriacomsoftware.app.common.pravneosobe.likvidnost.dto.LikvidnostVo;
import hr.adriacomsoftware.app.common.pravneosobe.likvidnost.facade.LikvidnostFacade;
import hr.adriacomsoftware.app.server.pravneosobe.likvidnost.da.jdbc.LikvidnostJdbc;
import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.server.facade.AS2FacadeServerLayer;


public final class LikvidnostFacadeServer extends AS2FacadeServerLayer 
		implements LikvidnostFacade {
    
    private static LikvidnostFacadeServer _instance = null;

    public static LikvidnostFacadeServer getInstance() {
        if (_instance == null) {
            _instance = new LikvidnostFacadeServer();
        }
        return _instance;
    }

    private LikvidnostFacadeServer() {
    	AS2Context.setSingletonReference(this);
    }
    public LikvidnostVo dodajLikvidnost(LikvidnostVo value)  {
		LikvidnostJdbc dao = new LikvidnostJdbc();
		dao.daoCreate(value);
		return value;
     }
    public LikvidnostVo azurirajLikvidnost(LikvidnostVo value)  {
    	LikvidnostJdbc dao = new LikvidnostJdbc();
		dao.daoStore(value);
		return value;
     }
    public LikvidnostRs procitajSveLikvidnosti(LikvidnostVo value)  {
    	LikvidnostJdbc dao = new LikvidnostJdbc();
		return dao.daoLoad(value,false);
     }
    public LikvidnostRs pronadiLikvidnosti(LikvidnostVo value)  {
    	return null;
     }
    public LikvidnostVo brisiLikvidnost(LikvidnostVo value)  {
    	LikvidnostJdbc dao = new LikvidnostJdbc();
		dao.daoRemove(value);
		return value;
     }
    public LikvidnostVo brisiViseLikvidnosti(LikvidnostRs value)  {
    	LikvidnostJdbc dao = new LikvidnostJdbc();
		dao.daoRemoveMany(value);
		return new LikvidnostVo();
     }
 }