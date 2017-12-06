package com.wireless.soft.indices.cfd.business.entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author 	Francisco Corredor
 * @since  	23Dec2015
 * @version	1.0  
 * entity de la tabla indexyahoocfd.iyc_fundamental_company_history
 * http://www.investopedia.com/terms/e/eps.asp
 * http://www.investopedia.com/terms/p/pegratio.asp
 */

@NamedQueries(value = {				
		@NamedQuery(name = "findAllFundamentalHistory", query = "SELECT s FROM FundamentalHistoryCompany s ORDER BY s.id"),
		@NamedQuery(name = "findFundamentalHistoryByCompany", query = "SELECT s FROM QuoteHistoryCompany s WHERE s.company = :company ORDER BY s.id desc ")
					})
@NamedNativeQueries({
	@NamedNativeQuery(name = "findLastIteracionFundamentalHistoryByCompany", query = "SELECT	0 as id, FCH_CODIGO, SCN_CODIGO as company, SCN_CODIGO, FCH_FECHA_CREACION as fechaCreacion, FCH_FECHA_CREACION, PERatio, Bid, Ask, EBITDA, PriceSales, PriceEPSEstimateCurrentYear, PriceEPSEstimateNextYear, PEGRatio, MarketCapitalization, MarketCapRealtime "+
																			"	FROM		indexyahoocfd.iyc_fundamental_company_history fundamentalhisto0_ "+
																			"	WHERE	SCN_CODIGO = :company "+
																			//"	AND		FCH_FECHA_CREACION between  DATE_SUB(NOW(), INTERVAL 1 DAY)  AND NOW() "+  
																			"	ORDER	by FCH_FECHA_CREACION desc "+
																			"	LIMIT 1 ", resultClass = FundamentalHistoryCompany.class)
 })
@Entity
@Table(name = "indexyahoocfd.iyc_fundamental_company_history")
public class FundamentalHistoryCompany implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7256036862497192370L;
	
	// ////////////////////////////////////////////////////////////////////////
    // Constantes de la clase
    // ////////////////////////////////////////////////////////////////////////

    /** */
    public static final String FIND_ALL_FUNDAMENTAL_HISTORY = "findAllFundamentalHistory";
    /** */
    public static final String FIND_FUNDAMENTAL_HISTORY_BYCOMPANY = "findFundamentalHistoryByCompany";
    
    public static final String FIND_LAST_FUNDAMENTAL_ITERACION_BYCOMPANY = "findLastIteracionFundamentalHistoryByCompany";


	
	
	
	 // ////////////////////////////////////////////////////////////////////////
    // Atributos de la clase
    // ////////////////////////////////////////////////////////////////////////
	/**Identificador del registro*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "FCH_CODIGO")
    private Long id;

    /** Informacion de la Compania */
    @Column(name = "SCN_CODIGO", nullable = false)
    private Long company;
    
    @Column(name = "FCH_FECHA_CREACION", nullable = false)
    /** Fecha Creacion */
    private Calendar fechaCreacion;
    
    @Column(name = "PERatio", nullable = true)
    private String pERatio;
    
    @Column(name = "Bid", nullable = true)
    private String bid;
    
    @Column(name = "Ask", nullable = true)
    private String ask;
    
    @Column(name = "EBITDA", nullable = true)
    private String ebitda;
    
    @Column(name = "PriceSales", nullable = true)
    private String priceSales;
    
    @Column(name = "PriceEPSEstimateCurrentYear", nullable = true)
    private String priceEPSEstimateCurrentYear;
    
    @Column(name = "PriceEPSEstimateNextYear", nullable = true)
    private String priceEPSEstimateNextYear;
    
    @Column(name = "PEGRatio", nullable = true)
    private String PEGRatio;
    
    @Column(name = "MarketCapitalization", nullable = true)
    private String MarketCapitalization;
    
    @Column(name = "MarketCapRealtime", nullable = true)
	private String MarketCapRealtime;
    

    
    // ////////////////////////////////////////////////////////////////////////
    // Getter/Setter de la clase
    // ////////////////////////////////////////////////////////////////////////
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the company
	 */
	public Long getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Long company) {
		this.company = company;
	}

	/**
	 * @return the fechaCreacion
	 */
	public Calendar getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion the fechaCreacion to set
	 */
	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return the pERatio
	 */
	public String getpERatio() {
		return pERatio;
	}

	/**
	 * @param pERatio the pERatio to set
	 */
	public void setpERatio(String pERatio) {
		this.pERatio = pERatio;
	}

	/**
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}

	/**
	 * @param bid the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}

	/**
	 * @return the ask
	 */
	public String getAsk() {
		return ask;
	}

	/**
	 * @param ask the ask to set
	 */
	public void setAsk(String ask) {
		this.ask = ask;
	}

	/**
	 * @return the ebitda
	 */
	public String getEbitda() {
		return ebitda;
	}

	/**
	 * @param ebitda the ebitda to set
	 */
	public void setEbitda(String ebitda) {
		this.ebitda = ebitda;
	}

	/**
	 * @return the priceSales
	 */
	public String getPriceSales() {
		return priceSales;
	}

	/**
	 * @param priceSales the priceSales to set
	 */
	public void setPriceSales(String priceSales) {
		this.priceSales = priceSales;
	}

	/**
	 * @return the priceEPSEstimateCurrentYear
	 */
	public String getPriceEPSEstimateCurrentYear() {
		return priceEPSEstimateCurrentYear;
	}

	/**
	 * @param priceEPSEstimateCurrentYear the priceEPSEstimateCurrentYear to set
	 */
	public void setPriceEPSEstimateCurrentYear(String priceEPSEstimateCurrentYear) {
		this.priceEPSEstimateCurrentYear = priceEPSEstimateCurrentYear;
	}

	/**
	 * @return the priceEPSEstimateNextYear
	 */
	public String getPriceEPSEstimateNextYear() {
		return priceEPSEstimateNextYear;
	}

	/**
	 * @param priceEPSEstimateNextYear the priceEPSEstimateNextYear to set
	 */
	public void setPriceEPSEstimateNextYear(String priceEPSEstimateNextYear) {
		this.priceEPSEstimateNextYear = priceEPSEstimateNextYear;
	}
    
	/**
	 * @return the pEGRatio
	 */
	public String getPEGRatio() {
		return PEGRatio;
	}

	/**
	 * @param pEGRatio the pEGRatio to set
	 */
	public void setPEGRatio(String pEGRatio) {
		PEGRatio = pEGRatio;
	}

	/**
	 * @return the marketCapitalization
	 */
	public String getMarketCapitalization() {
		return MarketCapitalization;
	}

	/**
	 * @param marketCapitalization the marketCapitalization to set
	 */
	public void setMarketCapitalization(String marketCapitalization) {
		MarketCapitalization = marketCapitalization;
	}

	/**
	 * @return the marketCapRealtime
	 */
	public String getMarketCapRealtime() {
		return MarketCapRealtime;
	}

	/**
	 * @param marketCapRealtime the marketCapRealtime to set
	 */
	public void setMarketCapRealtime(String marketCapRealtime) {
		MarketCapRealtime = marketCapRealtime;
	}

	@Override
    public String toString() {
	StringBuffer s = new StringBuffer();
	s.append("\n id [" + this.id + "]");
	s.append(" company [" + this.company + "]");
	s.append(" pERatio [" + this.pERatio + "]");
	s.append(" ask [" + this.ask + "]");
	s.append(" bid [" + this.bid + "]");
	s.append(" ebitda [" + this.ebitda + "]");
	s.append(" priceSales [" + this.priceSales + "]");
	s.append(" priceEPSEstimateCurrentYear [" + this.priceEPSEstimateCurrentYear + "]");
	s.append(" priceEPSEstimateNextYear [" + this.priceEPSEstimateNextYear + "]");
	s.append(" PEGRatio [" + this.PEGRatio + "]");
	s.append(" MarketCapitalization [" + this.MarketCapitalization + "]");
	s.append(" MarketCapRealtime [" + this.MarketCapRealtime + "]");

	return s.toString();
    }

}
