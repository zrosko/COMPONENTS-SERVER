package hr.adriacomsoftware.app.server.services;

import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.datasources.J2EEPersistentService;

public class AS2CRMPRODJDBCService extends J2EEPersistentService {
	static private AS2CRMPRODJDBCService _instance = null;
public AS2CRMPRODJDBCService() {
	super();
	AS2Context.setSingletonReference(this);
}
public void addToCache(String boFinder, AS2RecordList rs) throws AS2Exception {
    if(!rs.isValueLineHandlerUsed())
        _OLD_CACHE.put(boFinder, rs);
}
protected String getConnectionManagerType() {
	return "JDBC";
}
public static AS2CRMPRODJDBCService getInstance(){
	if(_instance==null)
		_instance = new AS2CRMPRODJDBCService();
	return _instance;
}
}
