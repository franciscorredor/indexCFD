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
 * @since  	01Dec2015
 * @version	1.0  
 * entity de la tabla indexyahoocfd.iyc_quote_company_history
 */

/*
 * Obtener top 7 de compa�ias con mejor rentabilidad en el mercado! 
 SELECT	x.name, x.diferencia
FROM		(
		SELECT	comp.SCN_NAME as name, max(chMax.price) as max, min(chMin.price) as min, (max(chMax.price) - min(chMin.price)) as diferencia
		FROM		indexyahoocfd.iyc_stock_companies  comp inner join   indexyahoocfd.iyc_quote_company_history chMax   on  chMax.SCN_CODIGO = comp.SCN_CODIGO
												inner join   indexyahoocfd.iyc_quote_company_history chMin   on  chMin.SCN_CODIGO = comp.SCN_CODIGO
		WHERE	chMax.QHC_FECHA_CREACION between  DATE_SUB(NOW(), INTERVAL 1 DAY)  AND NOW()
		AND	     chMin.QHC_FECHA_CREACION between  DATE_SUB(NOW(), INTERVAL 1 DAY)  AND NOW()
		GROUP by  comp.SCN_CODIGO
		) x
ORDER by diferencia desc
limit 7  --> Importante, validar que el precio este en subido, con esta consulta, puede ser que el precio halla caido!  

 */
@NamedQueries(value = {				
		@NamedQuery(name = "findAllQuoteHistory", query = "SELECT s FROM QuoteHistoryCompany s ORDER BY s.id"),
		@NamedQuery(name = "findQuoteHistoryByCompany", query = "SELECT s FROM QuoteHistoryCompany s WHERE s.company = :company AND s.volume is not NULL ORDER BY s.id desc LIMIT 5 ")
					})
@NamedNativeQueries({
	//TODO --> encontrar la primera iteracion para saber si a superado el high del dia y del anio, para dar un ponderado
	@NamedNativeQuery(name = "findFirstIteracionHistoryByCompany", query = "SELECT	0 as id, qch_codigo, SCN_CODIGO as company, SCN_CODIGO, QHC_FECHA_CREACION as fechaCreacion, QHC_FECHA_CREACION, name, symbol, ts, type, utctime, volume, syntaxis_change, chg_percent, day_high, day_low, issuer_name, issuer_name_lang, year_high, year_low, price "+
																			"	FROM		indexyahoocfd.iyc_quote_company_history quotehisto0_ "+
																			"	WHERE	SCN_CODIGO = :company and volume is not null and price is not null"+
																			"	AND		QHC_FECHA_CREACION between  DATE_SUB(NOW(), INTERVAL 1 DAY)  AND NOW() "+  
																			"	ORDER	by QHC_FECHA_CREACION desc "+
																			"	LIMIT 1 ", resultClass = QuoteHistoryCompany.class)	
 })
@Entity
@Table(name = "indexyahoocfd.iyc_quote_company_history")
public class QuoteHistoryCompany implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4861537089116018142L;
	
    // ////////////////////////////////////////////////////////////////////////
    // Constantes de la clase
    // ////////////////////////////////////////////////////////////////////////

    /** */
    public static final String FIND_ALL_QUOTEHISTORY = "findAllQuoteHistory";
    /** */
    public static final String FIND_QUOTEHISTORY_BYCOMPANY = "findQuoteHistoryByCompany";
    /** */
    public static final String FIND_FIRSTITERACION_BYCOMPANY = "findFirstIteracionHistoryByCompany";
    

    
    // ////////////////////////////////////////////////////////////////////////
    // Atributos de la clase
    // ////////////////////////////////////////////////////////////////////////
	/**Identificador del registro*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "qch_codigo")
    private Long id;

    /** Informacion de la Compania */
    //@OneToOne(targetEntity = Company.class, fetch = FetchType.EAGER)
    //@JoinColumn(name = "SCN_CODIGO", nullable = false)
    //private Company company;
    @Column(name = "SCN_CODIGO", nullable = false)
    private Long company;
    
    @Column(name = "QHC_FECHA_CREACION", nullable = false)
    /** Fecha Creacion */
    private Calendar fechaCreacion;
    
    @Column(name = "name", nullable = true)
    private String name;
    
    @Column(name = "symbol", nullable = true)
    private String symbol;
    
    @Column(name = "ts", nullable = true)
    private String ts;
    
    @Column(name = "type", nullable = true)
    private String type;
    
    @Column(name = "utctime", nullable = true)
    private String utctime;
    
    @Column(name = "volume", nullable = true)
    private String volume;
    
    @Column(name = "syntaxis_change", nullable = true)
    private String syntaxis_change;
    
    @Column(name = "chg_percent", nullable = true)
    private String chg_percent; 
    
    @Column(name = "day_high", nullable = true)
    private String day_high;
    
    @Column(name = "day_low", nullable = true)
    private String day_low;
    
    @Column(name = "issuer_name", nullable = true)
    private String issuer_name;
    
    @Column(name = "issuer_name_lang", nullable = true)
    private String issuer_name_lang;
    
    @Column(name = "year_high", nullable = true)
    private String year_high;
    
    @Column(name = "year_low", nullable = true)
    private String year_low;
    
    @Column(name = "price", nullable = true)
    private String price;
    
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the ts
	 */
	public String getTs() {
		return ts;
	}

	/**
	 * @param ts the ts to set
	 */
	public void setTs(String ts) {
		this.ts = ts;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the utctime
	 */
	public String getUtctime() {
		return utctime;
	}

	/**
	 * @param utctime the utctime to set
	 */
	public void setUtctime(String utctime) {
		this.utctime = utctime;
	}

	/**
	 * @return the volume
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(String volume) {
		this.volume = volume;
	}

	/**
	 * @return the change
	 */
	public String getSyntaxis_change() {
		return syntaxis_change;
	}

	/**
	 * @param change the change to set
	 */
	public void setSyntaxis_change(String syntaxis_change) {
		this.syntaxis_change = syntaxis_change;
	}

	/**
	 * @return the chg_percent
	 */
	public String getChg_percent() {
		return chg_percent;
	}

	/**
	 * @param chg_percent the chg_percent to set
	 */
	public void setChg_percent(String chg_percent) {
		this.chg_percent = chg_percent;
	}

	/**
	 * @return the day_high
	 */
	public String getDay_high() {
		return day_high;
	}

	/**
	 * @param day_high the day_high to set
	 */
	public void setDay_high(String day_high) {
		this.day_high = day_high;
	}

	/**
	 * @return the day_low
	 */
	public String getDay_low() {
		return day_low;
	}

	/**
	 * @param day_low the day_low to set
	 */
	public void setDay_low(String day_low) {
		this.day_low = day_low;
	}

	/**
	 * @return the issuer_name
	 */
	public String getIssuer_name() {
		return issuer_name;
	}

	/**
	 * @param issuer_name the issuer_name to set
	 */
	public void setIssuer_name(String issuer_name) {
		this.issuer_name = issuer_name;
	}

	/**
	 * @return the issuer_name_lang
	 */
	public String getIssuer_name_lang() {
		return issuer_name_lang;
	}

	/**
	 * @param issuer_name_lang the issuer_name_lang to set
	 */
	public void setIssuer_name_lang(String issuer_name_lang) {
		this.issuer_name_lang = issuer_name_lang;
	}

	/**
	 * @return the year_high
	 */
	public String getYear_high() {
		return year_high;
	}

	/**
	 * @param year_high the year_high to set
	 */
	public void setYear_high(String year_high) {
		this.year_high = year_high;
	}

	/**
	 * @return the year_low
	 */
	public String getYear_low() {
		return year_low;
	}

	/**
	 * @param year_low the year_low to set
	 */
	public void setYear_low(String year_low) {
		this.year_low = year_low;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	
	
	@Override
    public String toString() {
	StringBuffer s = new StringBuffer();
	s.append("\n name [" + this.name + "]");
	s.append(" price [" + this.price + "]");
	s.append("\n day_high [" + this.day_high + "]");
	s.append(" day_low [" + this.day_low + "]");
	s.append(" year_high [" + this.year_high + "]");
	s.append(" year_low [" + this.year_low + "]");

	return s.toString();
    }
	

}
