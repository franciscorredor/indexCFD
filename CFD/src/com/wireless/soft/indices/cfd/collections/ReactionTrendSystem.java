package com.wireless.soft.indices.cfd.collections;

public class ReactionTrendSystem {
	
	private Boolean actualPriceBetweenBuy;
	
	private Boolean actualPriceBetweenSell;
	
	private Boolean actualPriceUpHBOP;
	
	private Boolean actualPriceDownLBOP;
	
	private Boolean actualPriceBetweenHBOPLBOP;
	
	private Double actualPrice;
	
	private Double xPrima;
	private Double b_1;
	private Double s_1;
	private Double hbop;
	private Double lbop;
	private Double b_1_up;
	private Double s_1_down;
	
	public ReactionTrendSystem() {
		this.actualPriceBetweenBuy = false;
		this.actualPriceBetweenSell = false;
		this.actualPriceUpHBOP = false;
		this.actualPriceDownLBOP = false;
		this.actualPriceBetweenHBOPLBOP = false;
		this.actualPrice = 0d;
		this.xPrima = 0d;
		this.b_1 = 0d;
		this.s_1 = 0d;
		this.hbop = 0d;
		this.lbop = 0d;
		this.b_1_up = 0d;
		this.s_1_down = 0d;
	}

	/**
	 * @return the actualPriceBetweenBuy
	 */
	public Boolean getActualPriceBetweenBuy() {
		return actualPriceBetweenBuy;
	}

	/**
	 * @param actualPriceBetweenBuy the actualPriceBetweenBuy to set
	 */
	public void setActualPriceBetweenBuy(Boolean actualPriceBetweenBuy) {
		this.actualPriceBetweenBuy = actualPriceBetweenBuy;
	}

	/**
	 * @return the actualPriceBetweenSell
	 */
	public Boolean getActualPriceBetweenSell() {
		return actualPriceBetweenSell;
	}

	/**
	 * @param actualPriceBetweenSell the actualPriceBetweenSell to set
	 */
	public void setActualPriceBetweenSell(Boolean actualPriceBetweenSell) {
		this.actualPriceBetweenSell = actualPriceBetweenSell;
	}

	/**
	 * @return the actualPriceUpHBOP
	 */
	public Boolean getActualPriceUpHBOP() {
		return actualPriceUpHBOP;
	}

	/**
	 * @param actualPriceUpHBOP the actualPriceUpHBOP to set
	 */
	public void setActualPriceUpHBOP(Boolean actualPriceUpHBOP) {
		this.actualPriceUpHBOP = actualPriceUpHBOP;
	}

	/**
	 * @return the actualPriceDownLBOP
	 */
	public Boolean getActualPriceDownLBOP() {
		return actualPriceDownLBOP;
	}

	/**
	 * @param actualPriceDownLBOP the actualPriceDownLBOP to set
	 */
	public void setActualPriceDownLBOP(Boolean actualPriceDownLBOP) {
		this.actualPriceDownLBOP = actualPriceDownLBOP;
	}

	/**
	 * @return the actualPriceBetweenHBOPLBOP
	 */
	public Boolean getActualPriceBetweenHBOPLBOP() {
		return actualPriceBetweenHBOPLBOP;
	}

	/**
	 * @param actualPriceBetweenHBOPLBOP the actualPriceBetweenHBOPLBOP to set
	 */
	public void setActualPriceBetweenHBOPLBOP(Boolean actualPriceBetweenHBOPLBOP) {
		this.actualPriceBetweenHBOPLBOP = actualPriceBetweenHBOPLBOP;
	}

	/**
	 * @return the actualPrice
	 */
	public Double getActualPrice() {
		return actualPrice;
	}

	/**
	 * @param actualPrice the actualPrice to set
	 */
	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}

	/**
	 * @return the xPrima
	 */
	public Double getxPrima() {
		return xPrima;
	}

	/**
	 * @param xPrima the xPrima to set
	 */
	public void setxPrima(Double xPrima) {
		this.xPrima = xPrima;
	}

	/**
	 * @return the b_1
	 */
	public Double getB_1() {
		return b_1;
	}

	/**
	 * @param b_1 the b_1 to set
	 */
	public void setB_1(Double b_1) {
		this.b_1 = b_1;
	}

	/**
	 * @return the s_1
	 */
	public Double getS_1() {
		return s_1;
	}

	/**
	 * @param s_1 the s_1 to set
	 */
	public void setS_1(Double s_1) {
		this.s_1 = s_1;
	}

	/**
	 * @return the hbop
	 */
	public Double getHbop() {
		return hbop;
	}

	/**
	 * @param hbop the hbop to set
	 */
	public void setHbop(Double hbop) {
		this.hbop = hbop;
	}

	/**
	 * @return the lbop
	 */
	public Double getLbop() {
		return lbop;
	}

	/**
	 * @param lbop the lbop to set
	 */
	public void setLbop(Double lbop) {
		this.lbop = lbop;
	}

	/**
	 * @return the b_1_up
	 */
	public Double getB_1_up() {
		return b_1_up;
	}

	/**
	 * @param b_1_up the b_1_up to set
	 */
	public void setB_1_up(Double b_1_up) {
		this.b_1_up = b_1_up;
	}

	/**
	 * @return the s_1_down
	 */
	public Double getS_1_down() {
		return s_1_down;
	}

	/**
	 * @param s_1_down the s_1_down to set
	 */
	public void setS_1_down(Double s_1_down) {
		this.s_1_down = s_1_down;
	}


}
