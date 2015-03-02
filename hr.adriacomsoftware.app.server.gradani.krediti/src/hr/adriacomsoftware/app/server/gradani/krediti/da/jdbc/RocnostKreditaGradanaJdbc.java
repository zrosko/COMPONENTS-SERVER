package hr.adriacomsoftware.app.server.gradani.krediti.da.jdbc;

import hr.adriacomsoftware.app.common.datadictionary.JBDataDictionary;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.RocnostRs;
import hr.adriacomsoftware.app.common.gradani.krediti.dto.RocnostVo;
import hr.adriacomsoftware.app.server.services.da.jdbc.BankarskiJdbc;
import hr.as2.inf.common.exceptions.AS2DataAccessException;
import hr.as2.inf.common.exceptions.AS2Exception;
import hr.as2.inf.server.da.jdbc.J2EESqlBuilder;

import java.sql.PreparedStatement;


public final class RocnostKreditaGradanaJdbc extends BankarskiJdbc {
    public static boolean _program_radi = false;
    public RocnostKreditaGradanaJdbc() {
        setTableName("bi_izvjestaj_gr_rocnost");
    }
    public RocnostRs daoListaRocnosti(RocnostVo value)  {
        try {
        	RocnostKreditaGradanaJdbc._program_radi = true;
            J2EESqlBuilder sql = new J2EESqlBuilder();
            if (value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA_KAMATE))
                sql.appendln("select * from view_gr_krediti_rocnost ");
            else
                sql.appendln("select * from view_gr_krediti_rocnost_glavnica_kamate ");
            sql.appendWhere();
            sql.appPrefix();
            sql.append("WHERE godina = DATEPART(year,'");
            sql.append(value.get(JBDataDictionary.BI_ROCNOST__DATUM));
            sql.append("')");
            sql.append("AND mjesec = DATEPART(month,'");
            sql.append(value.get(JBDataDictionary.BI_ROCNOST__DATUM));
            sql.append("')");
            sql.append("and dan = DATEPART(day,'");
            sql.append(value.get(JBDataDictionary.BI_ROCNOST__DATUM));
            sql.append("')");
            String _profitni_centar = value.getProfitniCentar();
            String _profitni_centar_pocetak = _profitni_centar;
            String _profitni_centar_kraj = _profitni_centar;
            if(_profitni_centar.length()<1){
            	_profitni_centar_pocetak = "0";
            	_profitni_centar_kraj = "99000";
            } else if(_profitni_centar.startsWith("99000")){
            	_profitni_centar_pocetak = "0";
            	_profitni_centar_kraj = "99000";
            }
            sql.appGreatherOrEqual("AND","isnull(profitni_centar,0)", _profitni_centar_pocetak);
            sql.appLessOrEqual("AND", "isnull(profitni_centar,0)", _profitni_centar_kraj);
            if (!value.getGlavnicaKamate().equals(RocnostVo.BI_VRSTA_ROCNOSTI_GLAVNICA_KAMATE))
                sql.appEqual("AND", "glavnica_kamate", value.getGlavnicaKamate());
            /* Ograničavanje za sigurnosne razine (1,2,3) POČETAK */
            sql = odrediRazinuOvlastiUkljucujuciPodrucje(sql);
            /* ograničavanje za sigurnosne razine KRAJ */
            // sql.append(" ORDER BY naziv,broj_partije ");
            sql.append(" ORDER BY preko_365, od_181_do_365, od_151_do_180, od_121_do_150, od_91_do_120, od_61_do_90, od_31_do_60, do_30_dana");
            PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
            pstmt.setMaxRows(0);
            RocnostRs j2eers = new RocnostRs(transformResultSet(pstmt.executeQuery()));
            pstmt.close();
            if (value.getAkcija().equals(RocnostVo.IZRACUNAJ))
                _program_radi = false;
            return j2eers;
        } catch (AS2Exception e) {
            throw e;
        } catch (Exception e) {
			if (value.getAkcija().equals(RocnostVo.IZRACUNAJ))
				_program_radi = false;
            AS2DataAccessException ex = new AS2DataAccessException("151");
            ex.addCauseException(e);
            throw ex;
        }
    }
    public RocnostRs daoRocnostZaKomitenta(RocnostVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("select * from dbo.fn_tbl_gr_kredit_rocnost_oib(?,?,?,?,?) order by maticni_broj,broj_partije,broj_konta");
        try{    
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
            pstmt.setDate(1, value.getAsSqlDate("datum"));
            pstmt.setString(2, "O");
            pstmt.setString(3, "V");
            pstmt.setObject(4, value.getJmbg());
            pstmt.setString(5, "GR");
            pstmt.setMaxRows(0);
            RocnostRs j2eers = new RocnostRs(transformResultSet(pstmt.executeQuery()));
            pstmt.close();
            return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
    public RocnostRs daoRocnostSintetikaGlavnaKnjiga(RocnostVo value)  {
        J2EESqlBuilder sql = new J2EESqlBuilder();
        sql.appendln("select broj_konta, SUM(iznos_valuta*duguje_potrazuje) as iznos ");
        sql.appendln("from bi_prod.dbo.bi_glavna_knjiga_bsa ");
        sql.appendln("where datum_valutiranja <= ? AND broj_konta in ( ");
        sql.appendln("SELECT DISTINCT broj_konta ");
        sql.appendln("FROM BI_PROD.dbo.bi_view_gr_prometi_kredita_sve ");
        if (value.getGlavnicaKamate().equals("G")) {
            sql.appendln("WHERE (dospjela_potrazivanja = '1') ");
            sql.appendln("AND (broj_konta LIKE '5%')) ");
            sql.appendln("GROUP BY broj_konta having ");
            sql.appendln("SUM(iznos_valuta*duguje_potrazuje)<>0 ");
        } else if (value.getGlavnicaKamate().equals("K")) {
            sql.appendln("WHERE (dospjela_potrazivanja = '1') ");
            sql.appendln("AND (broj_konta LIKE '1%' OR broj_konta LIKE '9%')) ");
            sql.appendln("GROUP BY broj_konta having ");
            sql.appendln("SUM(iznos_valuta*duguje_potrazuje)<>0 ");
        } else {
            sql.appendln("WHERE (dospjela_potrazivanja = '1') ");
            sql.appendln("AND (broj_konta LIKE '1%' OR broj_konta LIKE '5%' OR broj_konta LIKE '9%')) ");
            sql.appendln("GROUP BY broj_konta having ");
            sql.appendln("SUM(iznos_valuta*duguje_potrazuje)<>0 ");
        }
        sql.appendln(" union all ");
        sql.appendln("select 'Ukupno:' as broj_konta, SUM(iznos_valuta*duguje_potrazuje) as iznos ");
        sql.appendln("from bi_prod.dbo.bi_glavna_knjiga_bsa ");
        sql.appendln("where datum_valutiranja <= ? AND broj_konta in ( ");
        sql.appendln("SELECT DISTINCT broj_konta ");
        sql.appendln("FROM BI_PROD.dbo.bi_view_gr_prometi_kredita_sve ");
        if (value.getGlavnicaKamate().equals("G")) {
            sql.appendln("WHERE (dospjela_potrazivanja = '1') ");
            sql.appendln("AND (broj_konta LIKE '5%')) ");
        } else if (value.getGlavnicaKamate().equals("K")) {
            sql.appendln("WHERE (dospjela_potrazivanja = '1') ");
            sql.appendln("AND (broj_konta LIKE '1%' OR broj_konta LIKE '9%')) ");
        } else {
            sql.appendln("WHERE (dospjela_potrazivanja = '1') ");
            sql.appendln("AND (broj_konta LIKE '1%' OR broj_konta LIKE '5%' OR broj_konta LIKE '9%')) ");
        }
        try{
        	PreparedStatement pstmt = getConnection().getPreparedStatement(sql.toString());
            pstmt.setDate(1, value.getAsSqlDate(JBDataDictionary.BI_ROCNOST__DATUM));
            pstmt.setDate(2, value.getAsSqlDate(JBDataDictionary.BI_ROCNOST__DATUM));
            pstmt.setMaxRows(0);
            RocnostRs j2eers = new RocnostRs(transformResultSet(pstmt.executeQuery()));
            pstmt.close();
            return j2eers;
        } catch (Exception e) {
			throw new AS2DataAccessException(e);
		}
    }
}