package com.wireless.soft.indices.cfd.business.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

import com.wireless.soft.indices.cfd.collections.CompanyRanking;

/**
 * Entity encargado del almacenar el ultimo historico por compania para poder
 * calcular el RSI
 * 
 * @author Francisco
 *
 */
@NamedQueries(value = {
		@NamedQuery(name = "findHistoricalDataByCompany", query = "SELECT h FROM HistoricalDataCompany h WHERE h.company = :companyId ORDER BY h.id desc "),
		@NamedQuery(name = "deleteHistoricalData", query = "DELETE FROM HistoricalDataCompany "),
		@NamedQuery(name = "findHistoricalDataByCompanyAndDate", query = "SELECT h FROM HistoricalDataCompany h WHERE h.company = :companyId AND fechaDataHistorica >= :dateBegin ORDER BY h.id desc ") })

@NamedNativeQueries({ @NamedNativeQuery(name = "findFirstValueHistoricalDataByCompany", query = "SELECT	*\r\n"
		+ "FROM		indexyahoocfd.HST_HISTORICAL_DATA_COMPANY_TO_RSI\r\n" + "WHERE	scn_codigo = :companyId \r\n"
		+ "ORDER by  HST_date desc limit 1 ", resultClass = HistoricalDataCompany.class),
		@NamedNativeQuery(name = "findLastValueHistoricalDataByCompany", query = "SELECT	*\r\n"
				+ "FROM		indexyahoocfd.HST_HISTORICAL_DATA_COMPANY_TO_RSI\r\n"
				+ "WHERE	scn_codigo = :companyId \r\n"
				+ "ORDER by  HST_date asc limit 1 ", resultClass = HistoricalDataCompany.class),
		@NamedNativeQuery(name = "findTopFiveToMomentumFactor", query = "SELECT	*\r\n"
				+ "FROM		indexyahoocfd.HST_HISTORICAL_DATA_COMPANY_TO_RSI\r\n"
				+ "WHERE	scn_codigo = :companyId \r\n"
				+ "ORDER by  HST_date desc limit 5 ", resultClass = HistoricalDataCompany.class),
		@NamedNativeQuery(name = "findAllLastHistoricalData", query = "SELECT	ch.*\r\n" + 
				"FROM		indexyahoocfd.hst_historical_data_company_to_rsi ch		join  (SELECT max(chi.hst_codigo) as hst_codigo  FROM	indexyahoocfd.hst_historical_data_company_to_rsi chi GROUP BY chi.SCN_codigo ) as maxCompanyHistory   \r\n" + 
				"																ON maxCompanyHistory.hst_codigo = ch.hst_codigo ", resultClass = HistoricalDataCompany.class)
		

		

})

@Entity
@Table(name = "indexyahoocfd.HST_HISTORICAL_DATA_COMPANY_TO_RSI")
public class HistoricalDataCompany implements Serializable, Comparable<HistoricalDataCompany> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7474154609673494939L;

	// ////////////////////////////////////////////////////////////////////////
	// Constantes de la clase
	// ////////////////////////////////////////////////////////////////////////
	/** */
	public static final String FIND_HISTORICAL_DATA_BYCOMPANY = "findHistoricalDataByCompany";
	/** */
	public static final String FIND_FIRST_HISTORICAL_DATA_BYCOMPANY = "findFirstValueHistoricalDataByCompany";
	/** */
	public static final String FIND_LAST_HISTORICAL_DATA_BYCOMPANY = "findLastValueHistoricalDataByCompany";
	/** */
	public static final String FIND_TOP_FIVE_TO_MOMENTUM_FACTOR = "findTopFiveToMomentumFactor";
	/** */
	public static final String DELETE_HISTORICAL_DATA = "deleteHistoricalData";
	/** */
	public static final String FIND_HISTORICAL_DATA_BY_COMPANY_DATE = "findHistoricalDataByCompanyAndDate";
	
	public static final String FIND_ALL_LAST_HISTORICAL_DATA = "findAllLastHistoricalData";
	
	

	// ////////////////////////////////////////////////////////////////////////
	// Atributos de la clase
	// ////////////////////////////////////////////////////////////////////////
	/** Identificador del registro */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "HST_CODIGO")
	private Long id;

	/** Informacion de la Compania */
	@Column(name = "SCN_CODIGO", nullable = false)
	private Long company;

	@Column(name = "HST_DATE", nullable = false)
	/** Fecha Creacion */
	private Calendar fechaDataHistorica;

	@Column(name = "HST_STOCK_PRICE_CLOSE", nullable = false)
	private String stockPriceClose;

	@Column(name = "HST_STOCK_PRICE_HIGH", nullable = false)
	private String stockPriceHigh;

	@Column(name = "HST_STOCK_PRICE_LOW", nullable = false)
	private String stockPriceLow;

	@Column(name = "HST_STOCK_PRICE_OPEN", nullable = false)
	private String stockPriceOpen;

	@Column(name = "HST_STOCK_VOLUME", nullable = false)
	private String stockVolume;

	@Column(name = "HST_FECHA_CREACION", nullable = false)
	/** Fecha Creacion */
	private Calendar fechaCreacion;

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
	 * @param id
	 *            the id to set
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
	 * @param company
	 *            the company to set
	 */
	public void setCompany(Long company) {
		this.company = company;
	}

	/**
	 * @return the fechaDataHistorica
	 */
	public Calendar getFechaDataHistorica() {
		return fechaDataHistorica;
	}

	/**
	 * @param fechaDataHistorica
	 *            the fechaDataHistorica to set
	 */
	public void setFechaDataHistorica(Calendar fechaDataHistorica) {
		this.fechaDataHistorica = fechaDataHistorica;
	}

	/**
	 * @return the stockPriceClose
	 */
	public String getStockPriceClose() {
		return stockPriceClose;
	}

	/**
	 * @param stockPriceClose
	 *            the stockPriceClose to set
	 */
	public void setStockPriceClose(String stockPriceClose) {
		this.stockPriceClose = stockPriceClose;
	}

	/**
	 * @return the stockPriceHigh
	 */
	public String getStockPriceHigh() {
		return stockPriceHigh;
	}

	/**
	 * @param stockPriceHigh
	 *            the stockPriceHigh to set
	 */
	public void setStockPriceHigh(String stockPriceHigh) {
		this.stockPriceHigh = stockPriceHigh;
	}

	/**
	 * @return the stockPriceLow
	 */
	public String getStockPriceLow() {
		return stockPriceLow;
	}

	/**
	 * @param stockPriceLow
	 *            the stockPriceLow to set
	 */
	public void setStockPriceLow(String stockPriceLow) {
		this.stockPriceLow = stockPriceLow;
	}

	/**
	 * @return the stockPriceOpen
	 */
	public String getStockPriceOpen() {
		return stockPriceOpen;
	}

	/**
	 * @param stockPriceOpen
	 *            the stockPriceOpen to set
	 */
	public void setStockPriceOpen(String stockPriceOpen) {
		this.stockPriceOpen = stockPriceOpen;
	}

	/**
	 * @return the stockVolume
	 */
	public String getStockVolume() {
		return stockVolume;
	}

	/**
	 * @param stockVolume
	 *            the stockVolume to set
	 */
	public void setStockVolume(String stockVolume) {
		this.stockVolume = stockVolume;
	}

	/**
	 * @return the fechaCreacion
	 */
	public Calendar getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            the fechaCreacion to set
	 */
	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Override
	public String toString() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));			
		StringBuilder s = new StringBuilder();
		s.append("\n id [" + this.id + "]");
		s.append(" company [" + this.company + "]");
		s.append(" stockPriceClose [" + this.stockPriceClose + "]");
		s.append(" fechaCreacion [" + simpleDateFormat.format( new Date( this.fechaCreacion.getTimeInMillis()))  + "]");

		return s.toString();
	}

	/**
	 * @param compareCR
	 * @return Compara Ponderado y volumen
	 */
	@Override
	public int compareTo(HistoricalDataCompany compareHistoricalDataCompany) {

		if (this == compareHistoricalDataCompany) {
			return 0;
		}

		int value1 = 0;
		try {


			value1 = (int)(compareHistoricalDataCompany.getId() - this.getId());

			return value1*-1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;

	}

}
