package com.wireless.soft.indices.cfd.business.adm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import com.wireless.soft.indices.cfd.business.entities.BloombergIndex;
import com.wireless.soft.indices.cfd.business.entities.Company;
import com.wireless.soft.indices.cfd.business.entities.DataMiningCompany;
import com.wireless.soft.indices.cfd.business.entities.FundamentalHistoryCompany;
import com.wireless.soft.indices.cfd.business.entities.HistoricalDataCompany;
import com.wireless.soft.indices.cfd.business.entities.QuoteHistoryCompany;
import com.wireless.soft.indices.cfd.deserializable.json.object.ReturnYahooFinanceQuoteObject;
import com.wireless.soft.indices.cfd.deserializable.json.object.ReturnYahooFinanceQuoteObject.Query.Results.Quote;
import com.wireless.soft.indices.cfd.exception.BusinessException;
import com.wireless.soft.indices.cfd.util.UtilSession;

/**
 * @author Francisco Corredor
 * @version 1.0
 * @since 2015Dec01 Clase encargada de gestionar la consulta a la BD
 *
 */
public class AdminEntity {

	// ////////////////////////////////////////////////////////////////////////
	// Logger de la clase
	// ////////////////////////////////////////////////////////////////////////
	private static Logger _logger = Logger.getLogger(AdminEntity.class);

	// ////////////////////////////////////////////////////////////////////////
	// Atributos de la clase
	// ////////////////////////////////////////////////////////////////////////
	/** */
	private EntityManagerFactory emf;
	/** */
	private EntityManager em;
	/** */
	private EntityTransaction tx;

	private static final AdminEntity INSTANCE = new AdminEntity();

	// ////////////////////////////////////////////////////////////////////////
	// Constructor de la clase
	// ////////////////////////////////////////////////////////////////////////
	/**
	 * Constructor de la clase
	 */
	private AdminEntity() {
		super();

		this.emf = Persistence.createEntityManagerFactory("entityManager");
		this.em = this.emf.createEntityManager();
		this.tx = this.em.getTransaction();
	}

	public static AdminEntity getInstance() {
		return INSTANCE;
	}

	public void free() {
		try {
			this.em.clear();
		} catch (Exception e) {
		}
		try {
			this.em.close();
		} catch (Exception e) {
		}
		try {
			this.emf.close();
		} catch (Exception e) {
		}

		this.emf = Persistence.createEntityManagerFactory("entityManager");
		this.em = this.emf.createEntityManager();
		this.tx = this.em.getTransaction();
	}

	// ////////////////////////////////////////////////////////////////////////
	// Metodos de negocio
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * @return
	 * @throws BusinessException
	 */
	public List<Company> getCompanies() throws BusinessException {
		List<Company> lstCompanies = new ArrayList<Company>();

		try {
			if (!tx.isActive()) {
				this.tx.begin();
			}
			em.joinTransaction();
			List<Object> list = UtilSession.getObjectsByNamedQuery(this.em, Company.FIND_COMPANIES, null, null);

			for (Object object : list) {
				Company vnt = (Company) object;
				lstCompanies.add(vnt);
			}

			em.clear();

		} catch (Exception ex) {
			String s = "Error al consultar las companias";
			_logger.error(s, ex);
			throw new BusinessException(s, ex);
		}

		return lstCompanies;

	}

	/**
	 * @param ri
	 * @param cmp
	 */
	public void persistirCompaniesQuotes(ReturnYahooFinanceQuoteObject rf, Company cmp) {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		if (null != rf && null != rf.getQuery() && null != rf.getQuery().getResults()
				&& null != rf.getQuery().getResults().getQuote()) {

			Quote q = rf.getQuery().getResults().getQuote();
			if (null != q && null != q.getVolume()) {
				try {

					QuoteHistoryCompany qhc = new QuoteHistoryCompany();
					qhc.setCompany(cmp.getId());
					qhc.setFechaCreacion(Calendar.getInstance());
					// qhc.setName( f.getName() );
					// qhc.setSymbol(q.getSymbol());
					// qhc.setTs(f.getTs());
					// qhc.setType(f.getType());
					// qhc.setUtctime(f.getUtctime());
					qhc.setVolume(q.getVolume());
					// qhc.setSyntaxis_change(f.getChange());
					qhc.setChg_percent(q.getChange());
					qhc.setDay_high(q.getDaysHigh());
					qhc.setDay_low(q.getDaysLow());
					// qhc.setIssuer_name(f.getIssuer_name());
					// qhc.setIssuer_name_lang(f.getIssuer_name_lang());
					qhc.setYear_high(q.getYearHigh());
					qhc.setYear_low(q.getYearLow());
					// qhc.setPrice(q.getLastTradePriceOnly());
					qhc.setPrice(q.getAsk()); // Se modifica ahora trae de MSN
					em.persist(qhc);
					this.em.flush();
					// _logger.info("PErsistio.." + qhc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		this.tx.commit();
		em.clear();
	}

	/**
	 * @param cmp
	 * @return
	 * @throws Exception
	 */
	public List<Object> getCompIdxQuote(Company cmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("company", cmp.getId());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				QuoteHistoryCompany.FIND_QUOTEHISTORY_BYCOMPANY, param, 5);

		em.clear();

		return listIdxCompany;

	}
	
	/**
	 * @param cmp
	 * @return
	 * @throws Exception
	 */
	//Get info to take the Awesome Oscillator
	public List<Object> getCompIdxAOQuote(Company cmp) throws Exception {

		
		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("company", cmp.getId());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				QuoteHistoryCompany.FIND_AO_QUOTEHISTORY_BYCOMPANY, param, 34);

		em.clear();

		return listIdxCompany;

	}

	/**
	 * @param cmp
	 * @return
	 * @throws Exception
	 */
	public List<QuoteHistoryCompany> getAllLastPriceHistory() throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		List<QuoteHistoryCompany> qhcReturn = null;
		qhcReturn = new ArrayList<QuoteHistoryCompany>();
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				QuoteHistoryCompany.FIND_ALL_LAST_PRICE_HISTORY, null, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				qhcReturn.add((QuoteHistoryCompany) object);
			}
		}

		em.clear();
		return qhcReturn;

	}

	// TODO
	// para saber si una compa�ia subio su maximo high del dia realizar la
	// consulta del dia y si encontro unpunto donde
	// incremento el tope con repecto al minimo, realiar un break;

	/**
	 * @param cmp
	 * @return
	 * @throws Exception
	 *             Obtiene el primer record de una compa�ia
	 */
	public QuoteHistoryCompany getFirstRecordDay(Company cmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		QuoteHistoryCompany qhcReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("company", cmp.getId());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				QuoteHistoryCompany.FIND_FIRSTITERACION_BYCOMPANY, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				qhcReturn = (QuoteHistoryCompany) object;
				break;
			}
		}

		em.clear();

		return qhcReturn;

	}

	/**
	 * @param cmp
	 * @return
	 * @throws Exception
	 *             Obtiene el primer record de una compania
	 */
	public Company getCompanyById(Company cmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		Company cReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("companyId", cmp.getId());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em, Company.FIND_COMPANY_BY_ID, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				cReturn = (Company) object;
				break;
			}
		}

		em.clear();

		return cReturn;

	}

	/**
	 * @param cmp
	 * @return
	 * @throws Exception
	 *             Obtiene el primer record de una compa�ia
	 */
	public Company getCompanyBySymbol(Company cmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		Company cReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("cmpSymbol", cmp.getGoogleSymbol());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em, Company.FIND_COMPANY_BY_SYMBOL, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				cReturn = (Company) object;
				break;
			}
		}

		em.clear();

		return cReturn;

	}

	/**
	 * @param cmp
	 * @return
	 * @throws Exception
	 *             Obtiene el primer record de una compania
	 */

	public DataMiningCompany getDMCompanyByCmpAndIteracion(DataMiningCompany dmCmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		DataMiningCompany dmcReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("companyId", dmCmp.getCompany().getId());
		param.put("iteracion", dmCmp.getIdIteracion());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				DataMiningCompany.FIND_DATAMINING_COMPANY_BY_ID_ITERACION, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				dmcReturn = (DataMiningCompany) object;
				break;
			}
		}

		em.clear();

		return dmcReturn;

	}

	/**
	 * @param dmCmp
	 * @return
	 * @throws Exception
	 */
	public DataMiningCompany getPenultimateCompanyByCmp(DataMiningCompany dmCmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		DataMiningCompany dmcReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("companyId", dmCmp.getCompany().getId());
		param.put("idIteracion", dmCmp.getIdIteracion());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				DataMiningCompany.FIND_LAST_TWO_DATAMINING_COMPANY_BY_ID, param, null);
		if (null != listIdxCompany && listIdxCompany.size() >= 2) {
			// for (Object object : listIdxCompany) {
			dmcReturn = (DataMiningCompany) listIdxCompany.get(1);
			// }
		}

		em.clear();
		
		return dmcReturn;

	}

	/**
	 * @param dmCmp
	 * @return
	 * @throws Exception
	 *             Obtiene lista de DataMining
	 */
	public List<DataMiningCompany> getDMCompanyByIteracion(DataMiningCompany dmCmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		List<DataMiningCompany> lstDataMC = new ArrayList<DataMiningCompany>();

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("iteracion01", dmCmp.getIdIteracion());
		List<Object> listDMC = UtilSession.getObjectsByNamedQuery(em, DataMiningCompany.FIND_DATAMINING_BY_ID_ITERACION,
				param, null);

		if (null != listDMC && listDMC.size() > 0) {
			for (Object object : listDMC) {
				DataMiningCompany dmc = (DataMiningCompany) object;
				lstDataMC.add(dmc);
			}
		}

		em.clear();
		
		return lstDataMC;
	}

	/**
	 * @param rf
	 * @param cmp
	 */
	public void persistirCompaniesFundamental(ReturnYahooFinanceQuoteObject rf, Company cmp) {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		if (null != rf && null != rf.getQuery() && null != rf.getQuery().getResults()
				&& null != rf.getQuery().getResults().getQuote()) {

			Quote q = rf.getQuery().getResults().getQuote();
			if (null != q && null != q.getPERatio()) {
				try {

					FundamentalHistoryCompany fhc = new FundamentalHistoryCompany();
					fhc.setCompany(cmp.getId());
					fhc.setFechaCreacion(Calendar.getInstance());
					fhc.setpERatio(q.getPERatio());
					fhc.setAsk(q.getAsk());
					fhc.setBid(q.getBid());
					fhc.setEbitda(q.getEBITDA());
					fhc.setPriceEPSEstimateCurrentYear(q.getPriceEPSEstimateCurrentYear());
					fhc.setPriceEPSEstimateNextYear(q.getPriceEPSEstimateNextYear());
					fhc.setPriceSales(q.getPriceSales());
					fhc.setMarketCapitalization(q.getMarketCapitalization());
					fhc.setMarketCapRealtime(q.getMarketCapRealtime());
					fhc.setPEGRatio(q.getPEGRatio());

					em.persist(fhc);
					this.em.flush();
					// _logger.info("Persistio.." + fhc.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		this.tx.commit();
		em.clear();
		
	}

	/**
	 * @param dmc
	 */
	public void persistirDataMiningCompany(DataMiningCompany dmc) {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		if (null != dmc && null != dmc.getCompany() && null != dmc.getCompany().getId()) {

			try {
				Company cmp = em.find(Company.class, dmc.getCompany().getId());
				dmc.setCompany(cmp);
				em.persist(dmc);
				this.em.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		this.tx.commit();
		em.clear();
		
	}

	/**
	 * @param dmc
	 */
	public void updateDataMiningCompany(DataMiningCompany dmc) {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		if (null != dmc && null != dmc.getCompany() && null != dmc.getCompany().getId()) {

			try {
				em.merge(dmc);
				this.em.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		this.tx.commit();
		em.clear();
		
	}

	/**
	 * Retorna el ultimo registro del analisis fundamental
	 * 
	 * @param cmp
	 * @return
	 * @throws Exception
	 */
	public FundamentalHistoryCompany getLastFundamentalRecord(Company cmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		FundamentalHistoryCompany fhcReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("company", cmp.getId());
		List<Object> listFtlCompany = UtilSession.getObjectsByNamedQuery(em,
				FundamentalHistoryCompany.FIND_LAST_FUNDAMENTAL_ITERACION_BYCOMPANY, param, null);
		if (null != listFtlCompany && listFtlCompany.size() > 0) {
			for (Object object : listFtlCompany) {
				fhcReturn = (FundamentalHistoryCompany) object;
				break;
			}
		}

		em.clear();
		

		return fhcReturn;

	}

	/**
	 * Obtine el URL Bloomberg para obtener los indicadores
	 * 
	 * @param cmp
	 * @return
	 * @throws Exception
	 */
	public BloombergIndex getBloombergIndex(Company cmp) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		BloombergIndex bidxReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("company", cmp.getId());
		List<Object> listFtlCompany = UtilSession.getObjectsByNamedQuery(em,
				BloombergIndex.FIND_BLOOMBERG_URL_BYCOMPANY, param, null);
		if (null != listFtlCompany && listFtlCompany.size() > 0) {
			for (Object object : listFtlCompany) {
				bidxReturn = (BloombergIndex) object;
				break;
			}
		}

		em.clear();
		

		return bidxReturn;

	}

	/**
	 * @param lstDataToPersistir
	 */
	public void persistirDataHistoricaByCompany(List<HistoricalDataCompany> lstDataToPersistir) {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		if (null != lstDataToPersistir && !lstDataToPersistir.isEmpty()) {

			for (HistoricalDataCompany historicalDataCompany : lstDataToPersistir) {
				em.persist(historicalDataCompany);
				this.em.flush();
			}

		}

		this.tx.commit();

		em.clear();
		
	}

	/**
	 * @param lstDataToPersistir
	 * @throws Exception
	 */
	public void deleteDataHistorica() throws Exception {
		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		UtilSession.executeUpdateByNamedQuery(em, HistoricalDataCompany.DELETE_HISTORICAL_DATA, null);

		this.em.flush();

		this.tx.commit();

		em.clear();
		

	}

	/**
	 * @param lstDataToPersistir
	 * @throws Exception
	 */
	public void updateMomentumFactorByCompany(Long idCompany, Integer momentumFactor) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("company", idCompany);
		param.put("momentumFactor", momentumFactor);
		UtilSession.executeUpdateByNativeQuery(em,
				"UPDATE indexyahoocfd.dmc_data_mining_company SET DMC_MOMENTUM_FACTOR = :momentumFactor WHERE scn_codigo = :company",
				param);

		this.em.flush();

		this.tx.commit();

		em.clear();
		

	}

	/**
	 * @param hdc
	 * @return
	 * @throws Exception
	 */
	public List<HistoricalDataCompany> getHistoricalDataCompanyByCompany(HistoricalDataCompany hdc) throws Exception {
		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();
		List<HistoricalDataCompany> listHdcReturn = null;
		listHdcReturn = new ArrayList<HistoricalDataCompany>();

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("companyId", hdc.getCompany());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				HistoricalDataCompany.FIND_HISTORICAL_DATA_BYCOMPANY, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				listHdcReturn.add((HistoricalDataCompany) object);

			}
		}

		em.clear();
		

		return listHdcReturn;

	}

	/**
	 * @param hdc
	 * @return
	 * @throws Exception
	 */
	public List<HistoricalDataCompany> getHistoricalDataCompanyByCompanyDateBegin(HistoricalDataCompany hdc)
			throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		List<HistoricalDataCompany> listHdcReturn = null;
		listHdcReturn = new ArrayList<HistoricalDataCompany>();

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("companyId", hdc.getCompany());
		param.put("dateBegin", hdc.getFechaDataHistorica());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				HistoricalDataCompany.FIND_HISTORICAL_DATA_BY_COMPANY_DATE, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				listHdcReturn.add((HistoricalDataCompany) object);

			}
		}

		em.clear();
		
		return listHdcReturn;

	}

	/**
	 * @param hdc
	 * @return
	 * @throws Exception
	 */
	public HistoricalDataCompany getFirstHistoricalDataCompanyByCompany(HistoricalDataCompany hdc) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		HistoricalDataCompany hdcReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("companyId", hdc.getCompany());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				HistoricalDataCompany.FIND_FIRST_HISTORICAL_DATA_BYCOMPANY, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				hdcReturn = (HistoricalDataCompany) object;
				break;
			}
		}

		em.clear();
		

		return hdcReturn;

	}

	/**
	 * @param hdc
	 * @return
	 * @throws Exception
	 */
	public HistoricalDataCompany getLastHistoricalDataCompanyByCompany(HistoricalDataCompany hdc) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		HistoricalDataCompany hdcReturn = null;

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("companyId", hdc.getCompany());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				HistoricalDataCompany.FIND_LAST_HISTORICAL_DATA_BYCOMPANY, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				hdcReturn = (HistoricalDataCompany) object;
				break;
			}
		}

		em.clear();
		

		return hdcReturn;

	}

	/**
	 * @param hdc
	 * @return
	 * @throws Exception
	 */
	public List<HistoricalDataCompany> getTopFiveToMomentumFactor(HistoricalDataCompany hdc) throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		List<HistoricalDataCompany> listHdcReturn = null;
		listHdcReturn = new ArrayList<HistoricalDataCompany>();

		Hashtable<String, Object> param = new Hashtable<String, Object>();
		param.put("companyId", hdc.getCompany());
		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				HistoricalDataCompany.FIND_TOP_FIVE_TO_MOMENTUM_FACTOR, param, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				listHdcReturn.add((HistoricalDataCompany) object);

			}
		}

		em.clear();
		

		return listHdcReturn;

	}

	/**
	 * Obtiene la ultima data de High, low and close por cada compania.
	 * 
	 * @param hdc
	 * @return
	 * @throws Exception
	 */
	public Map<Long, HistoricalDataCompany> getAllLastHistoricalData() throws Exception {

		if (!tx.isActive()) {
			this.tx.begin();
		}
		em.joinTransaction();

		Map<Long, HistoricalDataCompany> listHdcReturn = null;
		listHdcReturn = new HashMap<Long, HistoricalDataCompany>();

		List<Object> listIdxCompany = UtilSession.getObjectsByNamedQuery(em,
				HistoricalDataCompany.FIND_ALL_LAST_HISTORICAL_DATA, null, null);
		if (null != listIdxCompany && listIdxCompany.size() > 0) {
			for (Object object : listIdxCompany) {
				HistoricalDataCompany historicalDataCompany = (HistoricalDataCompany) object;

				listHdcReturn.put(historicalDataCompany.getCompany(), historicalDataCompany);

			}
		}

		em.clear();
		

		return listHdcReturn;

	}

}
