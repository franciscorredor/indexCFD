package com.wireless.soft.indices.cfd.deserializable.json.object;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"Hp",
"Lp",
"Op",
"P",
"T",
"V"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesObj implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7839478087134085032L;
	//High
	@JsonProperty("Hp")
	private Double Hp;
	
	//Low
	@JsonProperty("Lp")
	private Double Lp;
	
	//Open
	@JsonProperty("Op")
	private Double Op;
	
	//Close
	@JsonProperty("P")
	private Double P;
	
	//time
	@JsonProperty("T")
	private Integer T;
	
	//Volume
	@JsonProperty("V")
	private Integer V;
	/**
	 * @return the hp
	 */
	public Double getHp() {
		return Hp;
	}
	/**
	 * @param hp the hp to set
	 */
	public void setHp(Double hp) {
		Hp = hp;
	}
	/**
	 * @return the lp
	 */
	public Double getLp() {
		return Lp;
	}
	/**
	 * @param lp the lp to set
	 */
	public void setLp(Double lp) {
		Lp = lp;
	}
	/**
	 * @return the op
	 */
	public Double getOp() {
		return Op;
	}
	/**
	 * @param op the op to set
	 */
	public void setOp(Double op) {
		Op = op;
	}
	/**
	 * @return the p
	 */
	public Double getP() {
		return P;
	}
	/**
	 * @param p the p to set
	 */
	public void setP(Double p) {
		P = p;
	}
	/**
	 * @return the t
	 */
	public Integer getT() {
		return T;
	}
	/**
	 * @param t the t to set
	 */
	public void setT(Integer t) {
		T = t;
	}
	/**
	 * @return the v
	 */
	public Integer getV() {
		return V;
	}
	/**
	 * @param v the v to set
	 */
	public void setV(Integer v) {
		V = v;
	}
	
	

}
