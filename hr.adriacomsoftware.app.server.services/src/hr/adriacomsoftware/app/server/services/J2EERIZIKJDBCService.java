package hr.adriacomsoftware.app.server.services;

import hr.as2.inf.common.core.AS2Context;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.datasources.J2EEPersistentService;

public class J2EERIZIKJDBCService extends J2EEPersistentService {
	static private J2EERIZIKJDBCService _instance = null;
public J2EERIZIKJDBCService() {
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
public static J2EERIZIKJDBCService getInstance(){
	if(_instance==null)
		_instance = new J2EERIZIKJDBCService();
	return _instance;
}
}
