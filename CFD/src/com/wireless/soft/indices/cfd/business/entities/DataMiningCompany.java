package com.wireless.soft.indices.cfd.business.entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Francisco 2016Oct27
 *
 */
//@NamedNativeQueries({ 
//	@NamedNativeQuery(name = "findDataMiningCompanyByIteracion", query = " SELECT	dmc.DMC_CODIGO as id, DMC_CODIGO_GRP_ITERACION as idIteracion, DMC_FECHA_CREACION as fechaCreacion, SCN_CODIGO as company, DMC_IS_PRICE_BETWEEN_HIGH_LOW as isPriceBetweenHighLow, DMC_TENDENCIA as tendencia,  DMC_PRICE_PERCENTAGE_INCREMENT as pricePercentageIncrement, DMC_NOTA_PONDERADA as notaPonderada,  DMC_PRICE_EARNING_RATIO as priceEarningRatio, DMC_RELATIVE_STRENGTH_INDEX as relativeStrengthIndex,  DMC_STOCK_PRICE as stockPrice, DMC_LAST_DIGIT as lastDigitStockPrice, DMC_DIFF_MAX_MIN as diffMaxMin, DMC_PERCENTAGE_INCREMENT as percentageIncrement, DMC_IS_STOCK_PRICE_MAYOR_MEDIA as isTockPriceMayorMedia, DMC_STOCK_PRICE_CLOSE as stockPriceClose,  DMC_YTD_PLATAFORMA as YTDPlataforma  FROM 	indexyahoocfd.dmc_data_mining_company dmc WHERE	dmc.DMC_CODIGO_GRP_ITERACION = :companyId  ", resultClass = DataMiningCompany.class)
//})
@NamedQueries(value = {				
		@NamedQuery(name = "findAllDataMiningCompany", query = "SELECT s FROM DataMiningCompany s ORDER BY s.id"),
		@NamedQuery(name = "findDataMiningCompanyByIteracionAndCompany", query = "SELECT s FROM DataMiningCompany s WHERE s.company.id = :companyId AND s.idIteracion = :iteracion "),
		@NamedQuery(name = "findLastTwoDataMiningCompanyByCompany", query = "SELECT s FROM DataMiningCompany s WHERE s.company.id = :companyId AND idIteracion <= :idIteracion ORDER BY s.id DESC "),
		@NamedQuery(name = "findDataMiningCompanyByIteracion", query = "SELECT s FROM DataMiningCompany s WHERE s.idIteracion = :iteracion01 ")
					})


@Entity
@Table(name = "indexyahoocfd.dmc_data_mining_company")
public class DataMiningCompany implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3233207038913993231L;
	
	/** */
    public static final String FIND_ALL_DATAMINING_COMPANY = "findAllDataMiningCompany";
    
    public static final String FIND_DATAMINING_COMPANY_BY_ID_ITERACION = "findDataMiningCompanyByIteracionAndCompany";
    
    
    public static final String FIND_LAST_TWO_DATAMINING_COMPANY_BY_ID = "findLastTwoDataMiningCompanyByCompany";
    
    public static final String FIND_DATAMINING_BY_ID_ITERACION = "findDataMiningCompanyByIteracion";

	
	// ////////////////////////////////////////////////////////////////////////
    // Atributos de la clase
    // ////////////////////////////////////////////////////////////////////////
	/**Identificador del registro*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "DMC_CODIGO")
    private Long id;

    /** Identificador de la iteracion */
    @Column(name = "DMC_CODIGO_GRP_ITERACION", nullable = false)
    private Long idIteracion;
    
    @Column(name = "DMC_FECHA_CREACION", nullable = true)
    /** Fecha Creacion */
    private Calendar fechaCreacion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "SCN_CODIGO", nullable = true)
    /** */
    private Company company;

    
    @Column(name = "DMC_IS_PRICE_BETWEEN_HIGH_LOW", nullable = true)
    private Boolean  isPriceBetweenHighLow;
    
    /*
     * 	(0) - alza
		(1)	- baja
		(2)	Alza
		(3)	Baja
     */
    @Column(name = "DMC_TENDENCIA", nullable = true)
    private Integer tendencia;
    
    @Column(name = "DMC_PRICE_PERCENTAGE_INCREMENT", nullable = true)
    private String pricePercentageIncrement;
    
    @Column(name = "DMC_NOTA_PONDERADA", nullable = true)
    private String notaPonderada;
    
    
    @Column(name = "DMC_PRICE_EARNING_RATIO", nullable = true)
    private String priceEarningRatio;
    
    
    @Column(name = "DMC_RELATIVE_STRENGTH_INDEX", nullable = true)
    private String relativeStrengthIndex;
        
    
    @Column(name = "DMC_STOCK_PRICE", nullable = true)
    private String stockPrice;
    
    
    @Column(name = "DMC_LAST_DIGIT", nullable = true)
    private Integer lastDigitStockPrice;
    
    
    @Column(name = "DMC_DIFF_MAX_MIN", nullable = true)
    private String diffMaxMin;
    
    
    @Column(name = "DMC_PERCENTAGE_INCREMENT", nullable = true)
    private String percentageIncrement; 
    
    
    @Column(name = "DMC_IS_STOCK_PRICE_MAYOR_MEDIA", nullable = true)
    private Boolean isTockPriceMayorMedia; 
    
    
    // Determina el valor de la accion al final del dia, 
    // en la logica del sistema nos permite saber si la
    //accion gano o perdio valor, comparando con el campo 'dmc_stock_price'
    @Column(name = "DMC_STOCK_PRICE_CLOSE", nullable = true)
    private String stockPriceClose;
    
    /*
     * Variable que evalua el YTD 
     * ver Algoritmo del metodo "getYearToDateReturn"
     */
    @Column(name = "DMC_YTD_PLATAFORMA", nullable = true)
    private String YTDPlataforma;
    
    
    @Column(name = "DMC_MOMENTUM_FACTOR", nullable = true)
    private Integer momentumFactor;
    
    @Column(name = "DMC_PROBABILIDAD_WIN", nullable = true)
    private String probabilidadWin;
    
    @Column(name = "DMC_PROBABILIDAD_LOST", nullable = true)
    private String probabilidadLost;
    
    @Column(name = "DMC_BANDERA_INCREMENTO", nullable = true)
    private Boolean banderaIncremento;
    
    
    @Column(name = "DMC_ACCELERATION", nullable = true)
    private String acceleration;
    
    
    @Column(name = "DMC_IS_REACTION_MODE", nullable = true)
    private Boolean reactionMode;
    
    
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
	 * @return the idIteracion
	 */
	public Long getIdIteracion() {
		return idIteracion;
	}


	/**
	 * @param idIteracion the idIteracion to set
	 */
	public void setIdIteracion(Long idIteracion) {
		this.idIteracion = idIteracion;
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
	 * @return the isPriceBetweenHighLow
	 */
	public Boolean getIsPriceBetweenHighLow() {
		return isPriceBetweenHighLow;
	}


	/**
	 * @param isPriceBetweenHighLow the isPriceBetweenHighLow to set
	 */
	public void setIsPriceBetweenHighLow(Boolean isPriceBetweenHighLow) {
		this.isPriceBetweenHighLow = isPriceBetweenHighLow;
	}


	/**
	 * @return the tendencia
	 */
	public Integer getTendencia() {
		return tendencia;
	}


	/**
	 * @param tendencia the tendencia to set
	 */
	public void setTendencia(Integer tendencia) {
		this.tendencia = tendencia;
	}


	/**
	 * @return the pricePercentageIncrement
	 */
	public String getPricePercentageIncrement() {
		return pricePercentageIncrement;
	}


	/**
	 * @param pricePercentageIncrement the pricePercentageIncrement to set
	 */
	public void setPricePercentageIncrement(String pricePercentageIncrement) {
		this.pricePercentageIncrement = pricePercentageIncrement;
	}


	/**
	 * @return the notaPonderada
	 */
	public String getNotaPonderada() {
		return notaPonderada;
	}


	/**
	 * @param notaPonderada the notaPonderada to set
	 */
	public void setNotaPonderada(String notaPonderada) {
		this.notaPonderada = notaPonderada;
	}


	/**
	 * @return the priceEarningRatio
	 */
	public String getPriceEarningRatio() {
		return priceEarningRatio;
	}


	/**
	 * @param priceEarningRatio the priceEarningRatio to set
	 */
	public void setPriceEarningRatio(String priceEarningRatio) {
		this.priceEarningRatio = priceEarningRatio;
	}


	/**
	 * @return the relativeStrengthIndex
	 */
	public String getRelativeStrengthIndex() {
		return relativeStrengthIndex;
	}


	/**
	 * @param relativeStrengthIndex the relativeStrengthIndex to set
	 */
	public void setRelativeStrengthIndex(String relativeStrengthIndex) {
		this.relativeStrengthIndex = relativeStrengthIndex;
	}


	/**
	 * @return the stockPrice
	 */
	public String getStockPrice() {
		return stockPrice;
	}


	/**
	 * @param stockPrice the stockPrice to set
	 */
	public void setStockPrice(String stockPrice) {
		this.stockPrice = stockPrice;
	}


	/**
	 * @return the lastDigitStockPrice
	 */
	public Integer getLastDigitStockPrice() {
		return lastDigitStockPrice;
	}


	/**
	 * @param lastDigitStockPrice the lastDigitStockPrice to set
	 */
	public void setLastDigitStockPrice(Integer lastDigitStockPrice) {
		this.lastDigitStockPrice = lastDigitStockPrice;
	}


	/**
	 * @return the diffMaxMin
	 */
	public String getDiffMaxMin() {
		return diffMaxMin;
	}


	/**
	 * @param diffMaxMin the diffMaxMin to set
	 */
	public void setDiffMaxMin(String diffMaxMin) {
		this.diffMaxMin = diffMaxMin;
	}


	/**
	 * @return the percentageIncrement
	 */
	public String getPercentageIncrement() {
		return percentageIncrement;
	}


	/**
	 * @param percentageIncrement the percentageIncrement to set
	 */
	public void setPercentageIncrement(String percentageIncrement) {
		this.percentageIncrement = percentageIncrement;
	}


	/**
	 * @return the isTockPriceMayorMedia
	 */
	public Boolean getIsTockPriceMayorMedia() {
		return isTockPriceMayorMedia;
	}


	/**
	 * @param isTockPriceMayorMedia the isTockPriceMayorMedia to set
	 */
	public void setIsTockPriceMayorMedia(Boolean isTockPriceMayorMedia) {
		this.isTockPriceMayorMedia = isTockPriceMayorMedia;
	}


	/**
	 * @return the stockPriceClose
	 */
	public String getStockPriceClose() {
		return stockPriceClose;
	}


	/**
	 * @param stockPriceClose the stockPriceClose to set
	 */
	public void setStockPriceClose(String stockPriceClose) {
		this.stockPriceClose = stockPriceClose;
	} 

	

	/**
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}


	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}


	/**
	 * @return the yTDPlataforma
	 */
	public String getYTDPlataforma() {
		return YTDPlataforma;
	}


	/**
	 * @param yTDPlataforma the yTDPlataforma to set
	 */
	public void setYTDPlataforma(String yTDPlataforma) {
		YTDPlataforma = yTDPlataforma;
	}


	public Integer getMomentumFactor() {
		return momentumFactor;
	}


	public void setMomentumFactor(Integer momentumFactor) {
		this.momentumFactor = momentumFactor;
	}


	/**
	 * @return the probabilidadWin
	 */
	public String getProbabilidadWin() {
		return probabilidadWin;
	}


	/**
	 * @param probabilidadWin the probabilidadWin to set
	 */
	public void setProbabilidadWin(String probabilidadWin) {
		this.probabilidadWin = probabilidadWin;
	}


	/**
	 * @return the probabilidadLost
	 */
	public String getProbabilidadLost() {
		return probabilidadLost;
	}


	/**
	 * @param probabilidadLost the probabilidadLost to set
	 */
	public void setProbabilidadLost(String probabilidadLost) {
		this.probabilidadLost = probabilidadLost;
	}


	/**
	 * @return the banderaIncremento
	 */
	public Boolean getBanderaIncremento() {
		return banderaIncremento;
	}


	/**
	 * @param banderaIncremento the banderaIncremento to set
	 */
	public void setBanderaIncremento(Boolean banderaIncremento) {
		this.banderaIncremento = banderaIncremento;
	}


	/**
	 * @return the acceleration
	 */
	public String getAcceleration() {
		return acceleration;
	}


	/**
	 * @param acceleration the acceleration to set
	 */
	public void setAcceleration(String acceleration) {
		this.acceleration = acceleration;
	}


	/**
	 * @return the reactionMode
	 */
	public Boolean getReactionMode() {
		return reactionMode;
	}


	/**
	 * @param reactionMode the reactionMode to set
	 */
	public void setReactionMode(Boolean reactionMode) {
		this.reactionMode = reactionMode;
	}


	@Override
    public String toString() {
	StringBuffer s = new StringBuffer();
	s.append(" lastDigitStockPrice [" + this.lastDigitStockPrice + "]");
	s.append(" relativeStrengthIndex [" + this.relativeStrengthIndex + "]");
	s.append(" stockPrice [" + this.stockPrice + "]");
	if (null != this.getCompany()){
		s.append(" \n company [" + this.getCompany().toString() + "]");
	}
	

	return s.toString();
    }
    
    

}

