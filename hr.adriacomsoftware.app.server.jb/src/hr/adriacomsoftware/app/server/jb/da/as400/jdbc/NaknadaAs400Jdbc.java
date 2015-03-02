package hr.adriacomsoftware.app.server.jb.da.as400.jdbc;

import hr.as2.inf.common.data.AS2Record;
import hr.as2.inf.common.data.AS2RecordList;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.connections.jdbc.J2EEConnectionJDBC;
import hr.as2.inf.server.da.jdbc.J2EEDataAccessObjectAs400Jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
/*
	----- Original Message ----- From: "Jasna Birkic" <jasna.birkic@banksoft.hr>
	To: "Damir Gojčeta" <damir.gojceta@jadranska-banka.hr>
	Sent: Tuesday, February 19, 2008 2:51 PM
	Subject: RE: Interface za knjiženje naknada u BSA
	Poštovani,
	Evo upute za evidenciju naknada (tehnički opis). Ovo su upute samo za
	Pravne osobe, molimo
	Vas ukoliko želite evidentirati naknade i za građane da se konzultirate s
	našim šalterskim odjelom(Ines Gojak, Domagoj Mažić)
	Tabela koju treba puniti je CHARGEEVID s kolonama:
	|---------------+---------------------------+----------------|
	| PARTNO        |Partija komitenta          |INTEGER   9     |
	|---------------+---------------------------+----------------|
	| DATEEVID      |Datum evidentiranja naknade|DATE            |
	|---------------+---------------------------+----------------|
	| ITEMEVID      |Broj stavaka za evidenciju |SMALLINT  4     |
	|---------------+---------------------------+----------------|
	| IDCHDEF       |ID naknade iz CHARGEDEF    |SMALLINT  4     |
	|---------------+---------------------------+----------------|
	| STATUS        |Status naknade             |SMALLINT  4     |
	|---------------+---------------------------+----------------|
	| OPERENTRY     |Operator unosa             |CHARACTER 4     |
	|---------------+---------------------------+----------------|
	| TIMEENTRY     |Vrijeme unosa              |TIMESTAMP       |
	|---------------+---------------------------+----------------|
	| CHARGENO      | Brojač                    |INTEGER   9     |
	|---------------+---------------------------+----------------|
	
	PARTNO - prvih 9 znamenaka partije, tj. partija bez kontrolne znamenke
	
	DATEEVID mora biti datum unutar mjeseca za koji će se obračunati unesena
	naknada.
	
	ITEMEVID je u biti količina za koliko puta će se jedna te ista stavka
	obračunati (npr. tarifa po stranicama i bilo je 5stranica -> itemevid=5)
	
	IDCHDEF je oznaka tarife naknade.Svaka tarifa je definirana sa
	jedinstvenim brojem, ukoliko želite evidentirati naknade za neke tarife,
	molimo
	Vas da nas kontaktirate, na način da nam napišete tarifu i tarifni stavak
	a mi ćemo Vama poslati odgovarajući idchdef.
	
	STATUS je =0 kad se naknada evidentira, =1 kad je obračunato(nakon
	puštanja programa za obračun naknade), =9 stornirana
	
	OPERENTRY - za operater unosa bi bilo dobro postaviti nešto po ćemu će se
	prepoznati da ste vi punili tabelu, tako da u modulu Platnog prometa možete
	pregledati sve naknade unesene na takav način. (npr. već postoji za sve što
	ide automatski -> operentry=AUTO)
	
	TIMEENTRY = CURRENT TIMESTAMP
	
	CHARGENO = MAX(CHARGENO) +1, može biti i nula, bitan je samo kad se ide
	stornirati evidentirana naknada preko opcije u platnom prometu
	
	
	Primjer za evidenciju naknade za partiju 1120000456, želimo joj
	evidenitrati naknadu koja se nalazi pod tarifnim stavkom 1.1.2. Održavanje
	internet bankarstva.
	Toj tarifi odgovara npr. idchdef =234. Naknadu evidentiramo za 19.02.2008
	,itemevid=1
	
	INSERT INTO BSADB.CHARGEEVID VALUES
	(112000045, 19.02.2008',  1, 234, 0, 'INTB', CURRENT TIMESTAMP, 0)
	
	Srdačan pozdrav,
	
	Jasna Birkić
	jasna.birkic@banksoft.hr
	
	Banksoft - racunalne usluge u bankarstvu
	
	tel. +385 1 4664 666,
	
	fax +385 1 4664 744
	http://www.banksoft.hr
 */

public final class NaknadaAs400Jdbc extends J2EEDataAccessObjectAs400Jdbc {
    public NaknadaAs400Jdbc() {
        setTableName("bsadb.CHARGEEVID"); // produkcija  
    }
	public int daoKnjiziNaknadeZaOpomene(AS2RecordList valueList)  {
	    AS2Record vo;
	    int rowCounter=0;
		Iterator<AS2Record> E = valueList.getRows().iterator();
		while(E.hasNext()){
			vo = E.next(); 
			daoKnjiziJednuNaknaduZaOpomenu(vo);
			rowCounter++;
		}
		return rowCounter;
	}
    public void daoKnjiziJednuNaknaduZaOpomenu(AS2Record value)  {
        J2EEConnectionJDBC co = null;
        try{
			co = getConnection();
			Connection jco = co.getJdbcConnection();
			PreparedStatement pstmt = jco
					.prepareStatement("INSERT INTO BSTDB.CHARGEEVID (PARTNO,DATEEVID,ITEMEVID,IDCHDEF,STATUS,OPERENTRY,TIMEENTRY,CHARGENO) VALUES (?,?,?,?,?,?,?,?) ");
			pstmt.setObject(1, value.get("broj_partije").substring(0, 9));
			pstmt.setObject(2, AS2Date.getCurrentTime());
			pstmt.setObject(3, "1");
			pstmt.setObject(4, "40");
			pstmt.setObject(5, "0");
			pstmt.setObject(6, "999");
			pstmt.setObject(7, AS2Date.getCurrentTime());
			pstmt.setObject(8, "0");
			pstmt.executeUpdate(); 
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    } 
	public AS2RecordList daoListaNaknada(AS2Record value) {
		J2EEConnectionJDBC co = null;
		AS2RecordList rs = null; 
		try{
			co = getConnection();
			Connection jco = co.getJdbcConnection();
			String sql;
			PreparedStatement pstmt;
			sql = "select * from chargeevid"; //$NON-NLS-1$
			pstmt = jco.prepareStatement(sql);
			pstmt.setMaxRows(co.getMaxRows());
			rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public AS2RecordList daoCitajNaknadu(AS2Record value) {
		J2EEConnectionJDBC co = null;
		AS2RecordList rs = null; 
		try{
			co = getConnection();
			Connection jco = co.getJdbcConnection();
			String sql;
			PreparedStatement pstmt;
			sql = "select * from chargeevid where PARTNO = ? "; //$NON-NLS-1$
			pstmt = jco.prepareStatement(sql);
			pstmt.setObject(1,value.get("broj_partije").substring(0,9));
			pstmt.setMaxRows(co.getMaxRows());
			rs = transformResultSet(pstmt.executeQuery());
			pstmt.close();
			return rs;
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
	public int daoBrisiNaknadeZaOpomene(AS2RecordList valueList)  {
	    AS2Record vo;
	    int rowCounter=0;
		Iterator<AS2Record> E = valueList.getRows().iterator();
		while(E.hasNext()){
			vo = E.next(); 
			daoBrisiJednuNaknaduZaOpomenu(vo);
			rowCounter++;
		}
		return rowCounter;
	}
	public void daoBrisiJednuNaknaduZaOpomenu(AS2Record value)  {
		J2EEConnectionJDBC co = null;
		AS2Record vo = null;
		int status = -1;
		long chargeno = -1;// TODO viditi za jednu partiju
		try{
			co = getConnection();
			Connection jco = co.getJdbcConnection();
			String sql;
			PreparedStatement pstmt;
			sql = "SELECT * FROM chargeevid WHERE chargeno = ?"; //$NON-NLS-1$
			pstmt = jco.prepareStatement(sql);
			pstmt.setLong(1, value.getAsLong("chargeno")); //$NON-NLS-1$
			pstmt.setMaxRows(co.getMaxRows());
			AS2RecordList j2eeRS = transformResultSetOneRow(pstmt.executeQuery());
			pstmt.close();
			vo = new AS2Record();
			vo.setProperties(j2eeRS.getProperties());
			chargeno = vo.getAsLong("chargeno"); //$NON-NLS-1$
			status = vo.getAsInt("status"); //$NON-NLS-1$
			if (chargeno == 0) {
				// naknada ne postoji
				vo.set("finish", "Naknada ne postoji"); //$NON-NLS-1$//
			} else if (status == 9) { // ako je status = 9 naknada je vec stornirana
				vo.set("finish", "Naknada je već stornirana"); //$NON-NLS-1$//$NON-NLS-2$
			} else { // storniraj naknadu
				sql = "UPDATE chargeevid SET status = 9 WHERE chargeno = ?"; //$NON-NLS-1$
				pstmt = jco.prepareStatement(sql);
				pstmt.setLong(1, chargeno);
				pstmt.setMaxRows(co.getMaxRows());
				pstmt.executeUpdate();
				pstmt.close();
				vo.set("finish", "Naknada uspješno stornirana"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		} catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
	}
		/* method body code version 0.01 */
}