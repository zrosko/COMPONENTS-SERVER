package hr.adriacomsoftware.app.server.etl.job;

import hr.adriacomsoftware.app.server.etl.facade.ETLFacadeServer;
import hr.as2.inf.common.types.AS2Date;
import hr.as2.inf.server.transaction.AS2Transaction;

public class ETLJob implements Runnable {
    private static ETLJob _instance = null;
    
    public static ETLJob getInstance() {
        if (_instance == null)
            _instance = new ETLJob();
        return _instance;
    }
    private ETLJob() {
        try {
            Thread etl_thread = new Thread(this);
            etl_thread.setPriority(Thread.MIN_PRIORITY);
            etl_thread.start();
        } catch (Exception e) {
            System.out.println("ETLJob default constructor exception - "+ e);
        }
    }

    public void run() {
        Thread t = Thread.currentThread();
        long startTime=0, endTime=0;
        
        while (true) {
            try {
                System.out.println("ETLJob START thread " + t.getName() + " started at: "+AS2Date.getCurrentDateAsString());
                startTime = System.currentTimeMillis();
//                J2EETransaction.begin(); //BEGIN txn
//                System.out.println("ETLJob Glavna knjiga - transaction begin: ");
//                ETLFacadeServer.getInstance().jobGlavnaKnjigaETL(null);
//                J2EETransaction.commit(); //COMMIT txn
//                System.out.println("ETLJob Glavna knjiga - transaction commit: ");
                
                AS2Transaction.begin(); //BEGIN txn
                System.out.println("ETLJob Glavna knjiga bsa - transaction begin: ");
                ETLFacadeServer.getInstance().jobGlavnaKnjigaBsaETL(null);
                AS2Transaction.commit(); //COMMIT txn
                System.out.println("ETLJob Glavna knjiga bsa - transaction commit: ");

                endTime = System.currentTimeMillis();
                Thread.sleep(300000); //5 minuta 300000
            }catch(Exception e){
                AS2Transaction.rollback(); //COMMIT txn
                System.out.println("ETLJob - transaction rollback: "+e.toString());
                e.printStackTrace();                
            } 
            System.out.println("ETLJob thread " + t.getName() + " finished for: " + (endTime - startTime) + " milliseconds");
            System.out.println("ETLJob END thread " + t.getName() + " finished at: "+AS2Date.getCurrentDateAsString());
        }
    }
    public static void main(String[] args) {
        ETLJob.getInstance();
    }
}