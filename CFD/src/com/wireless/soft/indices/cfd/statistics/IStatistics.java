/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author Francisco Corredor
 * @version 1.0
 * @since 20161030
 * 
	Interfaz encargada de unificar el calculo de la optencion de
		las estadiscticas de cada unan de las variables
 */
public interface IStatistics {
	
	/*
	 * Variables de Datamining "Variacion Unidad Precio"
	 * REF: C:\franco\Dropbox\etx\2016\Oct\Week04\20161024
	 */
	public static final Double ganoVariacionPrecio = 0.380952381d;
	public static final Double perdioVariacionPrecio = 0.619047619d;
	
	/**
	 * @param valor
	 * @return
	 * Obtiene las estadisticas de gano
	 * Implementa funcionalidad para que dado un valor, retorne la estadistica
	 */
	public  abstract Double getWinStatistics(Object valor);
	
	
	/**
	 * @param valor
	 * @return
	 * Obtiene las estadisticas de perdio
	 * Implementa funcionalidad para que dado un valor, retorne la estadistica
	 */
	public  abstract Double getLostStatistics(Object valor);

}
