/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author HP
 *
 */
public final class RelativeStrengthIndex implements IStatistics {
	
	private static final Double ganoRSI0_30 = 0.107142857d;

	private static final Double ganoRSI30_70 = 0.660714286d;

	private static final Double ganoRSI70_100 = 0.232142857d;
	
	private static final Double perdioRSI0_30 = 0.164835165d;

	private static final Double perdioRSI30_70 = 0.714285714d;

	private static final Double perdioRSI70_100 = 0.120879121d;



	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getWinStatistics(java.lang.Object)
	 */
	//=IF(AND(Q6>=0,Q6<30),"RSI(0-30)",IF(AND(Q6>=30,Q6<70),"RSI(30-70)",IF(AND(Q6>=70,Q6<100),"RSI(70-100)","Clasificarme")))
	@Override
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((double)valor >=0 && (double)valor < 30){
			winStatistics = ganoRSI0_30;
		}else if ((double)valor >=30 && (double)valor < 70){
			winStatistics = ganoRSI30_70;
		}else if ((double)valor >=70 && (double)valor < 100){
			winStatistics = ganoRSI70_100;
		}
		return winStatistics;
	}

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getLostStatistics(java.lang.Object)
	 */
	//=IF(AND(Q6>=0,Q6<30),"RSI(0-30)",IF(AND(Q6>=30,Q6<70),"RSI(30-70)",IF(AND(Q6>=70,Q6<100),"RSI(70-100)","Clasificarme")))
	@Override
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((double)valor >=0 && (double)valor < 30){
			lostStatistics = perdioRSI0_30;
		}else if ((double)valor >=30 && (double)valor < 70){
			lostStatistics = perdioRSI30_70;
		}else if ((double)valor >=70 && (double)valor < 100){
			lostStatistics = perdioRSI70_100;
		}
		return lostStatistics;
	}

}
