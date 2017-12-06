/**
 * 
 */
package com.wireless.soft.indices.cfd.statistics;

/**
 * @author FrancisCorredor 
 * @since Nov 1, 2016
 * @version 2016
 * @git 
 */
public final class PercentageIncremento implements IStatistics {

	private static final Double ganoPInc0_1 = 0.084745763d;
	private static final Double ganoPInc1_2 = 0.474576271d;
	private static final Double ganoPInc2_3 = 0.237288136d;
	private static final Double ganoPInc3_4 = 0.13559322d;
	private static final Double ganoPInc4_5 = 0.033898305d;
	private static final Double ganoPInc5_6 = 0.06779661d;
	private static final Double ganoPInc6mas = 0.033898305d;
	
	private static final Double perdioPInc0_1 = 0.053191489d;
	private static final Double perdioPInc1_2 = 0.553191489d;
	private static final Double perdioPInc2_3 = 0.265957447d;
	private static final Double perdioPInc3_4 = 0.074468085d;
	private static final Double perdioPInc4_5 = 0.063829787d;
	private static final Double perdioPInc5_6 = 0.021276596d;
	private static final Double perdioPInc6mas = 0.010638298d;

	
	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getWinStatistics(java.lang.Object)
	 */
	@Override
	//=IF(AND(S6>0,S6<=1),"%inc(0-1)",IF(AND(S6>1,S6<=2),"%inc(1-2)",IF(AND(S6>2,S6<=3),"%inc(2-3)",IF(AND(S6>3,S6<=4),"%inc(3-4)",
	//		IF(AND(S6>4,S6<=5),"%inc(4-5)",IF(AND(S6>5,S6<=6),"%inc(5-6)",IF(S6>6,"%inc(6mas)","Clasificarme")))))))
	public Double getWinStatistics(Object valor) {
		Double winStatistics = null;
		if ((double)valor >0 && (double)valor <= 1){
			winStatistics = ganoPInc0_1;
		}else if ((double)valor >1 && (double)valor <= 2){
			winStatistics = ganoPInc1_2;
		}else if ((double)valor >2 && (double)valor <= 3){
			winStatistics = ganoPInc2_3;
		}else if ((double)valor >3 && (double)valor <= 4){
			winStatistics = ganoPInc3_4;
		}else if ((double)valor >4 && (double)valor <= 5){
			winStatistics = ganoPInc4_5;
		}else if ((double)valor >5 && (double)valor <= 6){
			winStatistics = ganoPInc5_6;
		}else if ((double)valor >6){
			winStatistics = ganoPInc6mas;
		}
		return winStatistics;
	}

	/* (non-Javadoc)
	 * @see com.wireless.soft.indices.cfd.statistics.IStatistics#getLostStatistics(java.lang.Object)
	 */
	@Override
	//=IF(AND(S6>0,S6<=1),"%inc(0-1)",IF(AND(S6>1,S6<=2),"%inc(1-2)",IF(AND(S6>2,S6<=3),"%inc(2-3)",IF(AND(S6>3,S6<=4),"%inc(3-4)",
		//		IF(AND(S6>4,S6<=5),"%inc(4-5)",IF(AND(S6>5,S6<=6),"%inc(5-6)",IF(S6>6,"%inc(6mas)","Clasificarme")))))))
	public Double getLostStatistics(Object valor) {
		Double lostStatistics = null;
		if ((double)valor >0 && (double)valor <= 1){
			lostStatistics = perdioPInc0_1;
		}else if ((double)valor >1 && (double)valor <= 2){
			lostStatistics = perdioPInc1_2;
		}else if ((double)valor >2 && (double)valor <= 3){
			lostStatistics = perdioPInc2_3;
		}else if ((double)valor >3 && (double)valor <= 4){
			lostStatistics = perdioPInc3_4;
		}else if ((double)valor >4 && (double)valor <= 5){
			lostStatistics = perdioPInc4_5;
		}else if ((double)valor >5 && (double)valor <= 6){
			lostStatistics = perdioPInc5_6;
		}else if ((double)valor >6){
			lostStatistics = perdioPInc6mas;
		}
		return lostStatistics;
	}

}
