package com.wireless.soft.indices.cfd.business.entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Francisco Corredor
 * @since 01Aug2016
 * Entity q contine la url de bloomberg para obtaner
 * los indicadores de YTD & 1YR
 */
@NamedQueries(value = {				
		@NamedQuery(name = "findBloombergURLByCompany", query = "SELECT b FROM BloombergIndex b WHERE b.company = :company ORDER BY b.id desc ")
					})
@Entity
@Table(name = "indexyahoocfd.iyc_bbg_indicator")
public class BloombergIndex implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5330731179304603320L;
	
	// ////////////////////////////////////////////////////////////////////////
    // Constantes de la clase
    // ////////////////////////////////////////////////////////////////////////
    /** */
    public static final String FIND_BLOOMBERG_URL_BYCOMPANY = "findBloombergURLByCompany";
  
    
	 // ////////////////////////////////////////////////////////////////////////
    // Atributos de la clase
    // ////////////////////////////////////////////////////////////////////////
	/**Identificador del registro*/
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "BBG_CODIGO")
    private Long id;

    /** Informacion de la Compania */
    @Column(name = "SCN_CODIGO", nullable = false)
    private Long company;
    
    @Column(name = "BBG_URL_YRTN", nullable = true)
	private String UrlBloomberg;
    
    
    @Column(name = "BBG_FECHA_CREACION", nullable = false)
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
	 * @return the urlBloomberg
	 */
	public String getUrlBloomberg() {
		return UrlBloomberg;
	}


	/**
	 * @param urlBloomberg the urlBloomberg to set
	 */
	public void setUrlBloomberg(String urlBloomberg) {
		UrlBloomberg = urlBloomberg;
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
	
	@Override
    public String toString() {
	StringBuffer s = new StringBuffer();
	s.append("\n id [" + this.id + "]");
	s.append(" company [" + this.company + "]");
	s.append(" UrlBloomberg [" + this.UrlBloomberg + "]");

	return s.toString();
    }


}
