/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author HP
 *
 */
public final class PriceEarningRatio implements IStatistics {
	
	private static final Double ganoPERatio0_9 = 0.196428571d;
  
	private static final Double ganoPERatio9_14 = 0.428571429d;

	private static final Double ganoPERatio14_17 = 0.375d;

	private static final Double ganoPERatio17mas = 0d;

	private static final Double perdioPERatio0_9 = 0.230769231d;
	  
	private static final Double perdioPERatio9_14 = 0.362637363d;

	private static final Double perdioPERatio14_17 = 0.010989011d;

	private static final Double perdioPERatio17mas = 0.395604396d;

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getWinStatistics(java.lang.Object)
	 */
	//=IF(AND(L10>=0,L10<9),"PERatio(0-9)",IF(AND(L10>=9,L10<14),"PERatio(9-14)",IF(AND(L10>=14,L10<17),"PERatio(14-17)",IF(L10>=17,"PERatio(17mas)","Claificarme"))))
	@Override
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((double)valor >=0 && (double)valor < 9){
			winStatistics = ganoPERatio0_9;
		}else if ((double)valor >=9 && (double)valor < 14){
			winStatistics = ganoPERatio9_14;
		}else if ((double)valor >=14 && (double)valor < 17){
			winStatistics = ganoPERatio14_17;
		}else if ((double)valor >=17){
			winStatistics = ganoPERatio17mas;
		}
		return winStatistics;
	}

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getLostStatistics(java.lang.Object)
	 */
	//=IF(AND(L10>=0,L10<9),"PERatio(0-9)",IF(AND(L10>=9,L10<14),"PERatio(9-14)",IF(AND(L10>=14,L10<17),"PERatio(14-17)",IF(L10>=17,"PERatio(17mas)","Claificarme"))))
	@Override
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((double)valor >=0 && (double)valor < 9){
			lostStatistics = perdioPERatio0_9;
		}else if ((double)valor >=9 && (double)valor < 14){
			lostStatistics = perdioPERatio9_14;
		}else if ((double)valor >=14 && (double)valor < 17){
			lostStatistics = perdioPERatio14_17;
		}else if ((double)valor >=17){
			lostStatistics = perdioPERatio17mas;
		}
		return lostStatistics;
	}

}
