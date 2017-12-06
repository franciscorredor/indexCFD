/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author HP
 *
 */
public final class LastDigit implements IStatistics {
	
	private static final Double gano0_cero = 0.178571429d;
	private static final Double gano1_uno = 0.125d;
	private static final Double gano2_dos = 0.125d;
	private static final Double gano3_tres = 0.035714286d;
	private static final Double gano4_cuatro = 0.071428571d;
	private static final Double gano5_cinco = 0.089285714d;
	private static final Double gano6_seis = 0.053571429d;
	private static final Double gano7_siete = 0.089285714d;
	private static final Double gano8_ocho = 0.178571429d;
	private static final Double gano9_nueve = 0.053571429d;
	
	private static final Double perdio0_cero = 0.142857143d;
	private static final Double perdio1_uno = 0.120879121d;
	private static final Double perdio2_dos = 0.087912088d;
	private static final Double perdio3_tres = 0.054945055d;
	private static final Double perdio4_cuatro = 0.10989011d;
	private static final Double perdio5_cinco = 0.087912088d;
	private static final Double perdio6_seis = 0.098901099d;
	private static final Double perdio7_siete = 0.032967033d;
	private static final Double perdio8_ocho = 0.10989011d;
	private static final Double perdio9_nueve = 0.153846154d;


	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getWinStatistics(java.lang.Object)
	 */
	@Override
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((int)valor ==0 ){
			winStatistics = gano0_cero;
		}else if ((int)valor ==1 ){
			winStatistics = gano1_uno;
		}else if ((int)valor ==2 ){
			winStatistics = gano2_dos;
		}else if ((int)valor ==3 ){
			winStatistics = gano3_tres;
		}else if ((int)valor ==4 ){
			winStatistics = gano4_cuatro;
		}else if ((int)valor ==5 ){
			winStatistics = gano5_cinco;
		}else if ((int)valor ==6 ){
			winStatistics = gano6_seis;
		}else if ((int)valor ==7 ){
			winStatistics = gano7_siete;
		}else if ((int)valor ==8 ){
			winStatistics = gano8_ocho;
		}else if ((int)valor ==9 ){
			winStatistics = gano9_nueve;
		}
		return winStatistics;
	}

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getLostStatistics(java.lang.Object)
	 */
	@Override
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((int)valor ==0 ){
			lostStatistics = perdio0_cero;
		}else if ((int)valor ==1 ){
			lostStatistics = perdio1_uno;
		}else if ((int)valor ==2 ){
			lostStatistics = perdio2_dos;
		}else if ((int)valor ==3 ){
			lostStatistics = perdio3_tres;
		}else if ((int)valor ==4 ){
			lostStatistics = perdio4_cuatro;
		}else if ((int)valor ==5 ){
			lostStatistics = perdio5_cinco;
		}else if ((int)valor ==6 ){
			lostStatistics = perdio6_seis;
		}else if ((int)valor ==7 ){
			lostStatistics = perdio7_siete;
		}else if ((int)valor ==8 ){
			lostStatistics = perdio8_ocho;
		}else if ((int)valor ==9 ){
			lostStatistics = perdio9_nueve;
		}
		return lostStatistics;
	}

}
