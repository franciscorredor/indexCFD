package com.wireless.soft.indices.cfd.business.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;



/**
 * @author 	Francisco Corredor
 * @since  	01Dec2015
 * @version	2.0, 06Nov2017  
 */
@NamedNativeQueries({ 
	@NamedNativeQuery(name = "findCompanies", query = " SELECT	com.scn_cid as cid , com.SCN_GOOGLE_SYMBOL as googleSymbol, com.SCN_CODIGO as id, com.SCN_NAME as name, sci.SCI_URL_INDEX as urlIndex, com.SCN_MSN_QUOTE as urlQuote \n"+
														" FROM		indexyahoocfd.iyc_stack_company_index sci  	INNER JOIN indexyahoocfd.iyc_stock_companies com   	on com.SCN_CODIGO = sci.SCN_CODIGO and com.SCN_MSN_QUOTE is not null and com.SCN_MSN_QUOTE <> 'noMSN' \n"+ 
														" inner join indexyahoocfd.iyc_stack_company_quotes scq	on scq.scn_codigo = com.scn_codigo \n"+
														//" WHERE com.scn_cid not in (676, 918, 917, 139, 157) \n"+
														//" WHERE com.scn_CODIGO not in (139,145,174,189,190,194,195,196,205,206,225,227,235,240,241,242,243,246,247,249,259,271,272,283,285,315,316,318,326,327,328,330,337,340,343,347,348,376,377,401,402,407,408,412,415,416,417,419,422,432,433,435,437,439,445,452,453,458,461,466,476,486,489,494,521,540,593,610,635,640,661,670,683,698,699,714,732,734,754,801,821,823,827,837,855,867,905,917) \n"+
														" ORDER by com.SCN_CODIGO", resultClass = Company.class),
	@NamedNativeQuery(name = "findCompanyById", query = "SELECT	com.scn_cid as cid , com.SCN_GOOGLE_SYMBOL as googleSymbol, com.SCN_CODIGO as id, com.SCN_NAME as name, sci.SCI_URL_INDEX as urlIndex, com.SCN_MSN_QUOTE as urlQuote FROM		indexyahoocfd.iyc_stack_company_index sci  INNER JOIN  indexyahoocfd.iyc_stock_companies com   on com.SCN_CODIGO = sci.SCN_CODIGO inner join indexyahoocfd.iyc_stack_company_quotes scq	on scq.scn_codigo = com.scn_codigo WHERE  com.SCN_CODIGO = :companyId  ", resultClass = Company.class),
	@NamedNativeQuery(name = "findCompanyBySymbol", query = " SELECT	com.scn_cid as cid , com.SCN_GOOGLE_SYMBOL as googleSymbol, com.SCN_CODIGO as id, com.SCN_NAME as name, ch.symbol as urlIndex, com.SCN_MSN_QUOTE as urlQuote  \n"+
															" FROM		indexyahoocfd.iyc_quote_company_history ch  	INNER JOIN indexyahoocfd.iyc_stock_companies com   	on com.SCN_CODIGO = ch.SCN_CODIGO \n"+ 
															//" WHERE	ch.symbol = :cmpSymbol \n"+
															" WHERE	com.SCN_GOOGLE_SYMBOL = :cmpSymbol \n"+
															" ORDER by com.SCN_CODIGO LIMIT 1 ", resultClass = Company.class),
})



@Entity
@Table(name = "indexyahoocfd.Company")
public class Company  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -91323491401835022L;
	//private static String MSN_URL_QUOTE = "https://www.msn.com/en-us/money/stockdetails/analysis/";
	private static String MSN_URL_QUOTE = "https://www.msn.com/en-us/money/stockdetails/";
	
	
	// ////////////////////////////////////////////////////////////////////////
    // Nombre de los queries nombrados
    // ////////////////////////////////////////////////////////////////////////
    /** */
    public static final String FIND_COMPANIES = "findCompanies";

    /** */
    public static final String FIND_COMPANY_BY_ID = "findCompanyById";
    
    /** */
    public static final String FIND_COMPANY_BY_SYMBOL = "findCompanyBySymbol";

    // ////////////////////////////////////////////////////////////////////////
    // Campos del backing bean
    // ////////////////////////////////////////////////////////////////////////
    @Id
    private Long id;
    /** */
    private String name;
    /** */
    private String urlIndex;
    
    private String urlQuote;
    
    private String googleSymbol;
    
    private String cid;
    
    
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
	 * @return the urlIndex
	 */
	public String getUrlIndex() {
		return urlIndex;
	}
	/**
	 * @param urlIndex the urlIndex to set
	 */
	public void setUrlIndex(String urlIndex) {
		this.urlIndex = urlIndex;
	}
	
	public String getUrlQuote() {
		return urlQuote==null?urlQuote:this.MSN_URL_QUOTE+urlQuote;
	}
	public void setUrlQuote(String urlQuote) {
		this.urlQuote = urlQuote;
	}
	
	/**
	 * @return the googleSymbol
	 */
	public String getGoogleSymbol() {
		return googleSymbol;
	}
	/**
	 * @param googleSymbol the googleSymbol to set
	 */
	public void setGoogleSymbol(String googleSymbol) {
		this.googleSymbol = googleSymbol;
	}
	
	
	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}
	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}
	
	@Override
    public String toString() {
	StringBuffer s = new StringBuffer();
	s.append(" name [" + this.name + "]");
	s.append(" \n urlQuote [" + this.urlQuote + "\n]");
	s.append(" googleSymbol [" + this.googleSymbol + "]");
	s.append(" cid [" + this.cid + "]");
	
	return s.toString();
    }
	

}

