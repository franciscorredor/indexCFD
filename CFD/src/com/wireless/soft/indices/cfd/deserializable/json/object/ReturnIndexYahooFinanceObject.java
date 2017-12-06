package com.wireless.soft.indices.cfd.deserializable.json.object;

import java.io.Serializable;

/**
 * Objeto POJO, que contiene la informacion de Yahoo finanzas
 * @author Francisco
 * REF:
 * 	http://jsonviewer.stack.hu/#http://finance.yahoo.com/webservice/v1/symbols/AMZN/quote?format=json
 *
 */
public class ReturnIndexYahooFinanceObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -530614290559982762L;

	// ////////////////////////////////////////////////////////////////////////
	// Atributos de la clase
	// ////////////////////////////////////////////////////////////////////////

	private List list;

	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}
	
	@Override
    public String toString() {
	StringBuffer s = new StringBuffer(this.getClass().getCanonicalName());
	if (this.list !=null){
		s.append("\n list [" + this.list.toString() + "]");
	}

	return s.toString();
    }

	public class List {

		private Meta meta;
		
		private Resources[] resources;

		public Meta getMeta() {
			return meta;
		}

		public void setMeta(Meta meta) {
			this.meta = meta;
		}
		
		public Resources[] getResources() {
			return resources;
		}

		public void setResources(Resources[] resources) {
			this.resources = resources;
		}

		@Override
	    public String toString() {
		StringBuffer s = new StringBuffer();
		if (this.meta !=null){
			s.append("\n meta [" + this.meta.toString() + "]");
		}
		if (null != getResources() && getResources().length > 0) {
		    for (int i = 0; i < getResources().length; i++) {
			s.append("\n resources{" + i + "} [" + this.getResources()[i].toString() + "]");
		    }

		} else {
		    s.append("  El arreglo no tiene resources  ");
		}

		return s.toString();
	    }
		


		public class Meta {

			private String type;
			
			private String start;

			private String count;

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getStart() {
				return start;
			}

			public void setStart(String start) {
				this.start = start;
			}

			public String getCount() {
				return count;
			}

			public void setCount(String count) {
				this.count = count;
			}
			
			@Override
		    public String toString() {
			StringBuffer s = new StringBuffer();
			s.append("\n type [" + this.type + "]");
			s.append(" start [" + this.start + "]");
			s.append(" count [" + this.count + "]");

			return s.toString();
		    }

		}
		
		public class Resources{
			
			
			private Resource resource;
			
			public Resource getResource() {
				return resource;
			}



			public void setResource(Resource resource) {
				this.resource = resource;
			}
			
			@Override
		    public String toString() {
			StringBuffer s = new StringBuffer();
			if (this.resource !=null){
				s.append("\n resource [" + this.resource.toString() + "]");
			}

			return s.toString();
		    }
			
			
			
		public class Resource{	

			private String classname;
			
			private Fields fields;

			public String getClassname() {
				return classname;
			}

			public void setClassname(String classname) {
				this.classname = classname;
			}
			
			

			
			public Fields getFields() {
				return fields;
			}

			public void setFields(Fields fields) {
				this.fields = fields;
			}

			
			public class Fields{
				
				private String name;
				private String price;
				private String symbol;
				private String ts;
				private String type;
				private String utctime;
				private String volume;
				
				//detail info
				private String change;
				private String chg_percent;
				private String day_high;
				private String day_low;
				private String issuer_name;
				private String issuer_name_lang;
				private String year_high;
				private String year_low;
				
				
				public String getName() {
					return name;
				}
				public void setName(String name) {
					this.name = name;
				}
				public String getPrice() {
					return price;
				}
				public void setPrice(String price) {
					this.price = price;
				}
				public String getSymbol() {
					return symbol;
				}
				public void setSymbol(String symbol) {
					this.symbol = symbol;
				}
				public String getTs() {
					return ts;
				}
				public void setTs(String ts) {
					this.ts = ts;
				}
				public String getType() {
					return type;
				}
				public void setType(String type) {
					this.type = type;
				}
				public String getUtctime() {
					return utctime;
				}
				public void setUtctime(String utctime) {
					this.utctime = utctime;
				}
				public String getVolume() {
					return volume;
				}
				public void setVolume(String volume) {
					this.volume = volume;
				}
				
				public String getChange() {
					return change;
				}
				public void setChange(String change) {
					this.change = change;
				}
				public String getChg_percent() {
					return chg_percent;
				}
				public void setChg_percent(String chg_percent) {
					this.chg_percent = chg_percent;
				}	
				public String getDay_high() {
					return day_high;
				}
				public void setDay_high(String day_high) {
					this.day_high = day_high;
				}
				public String getDay_low() {
					return day_low;
				}
				public void setDay_low(String day_low) {
					this.day_low = day_low;
				}
				public String getIssuer_name() {
					return issuer_name;
				}
				public void setIssuer_name(String issuer_name) {
					this.issuer_name = issuer_name;
				}
				public String getIssuer_name_lang() {
					return issuer_name_lang;
				}
				public void setIssuer_name_lang(String issuer_name_lang) {
					this.issuer_name_lang = issuer_name_lang;
				}
				public String getYear_high() {
					return year_high;
				}
				public void setYear_high(String year_high) {
					this.year_high = year_high;
				}
				public String getYear_low() {
					return year_low;
				}
				public void setYear_low(String year_low) {
					this.year_low = year_low;
				}
				
				@Override
			    public String toString() {
				StringBuffer s = new StringBuffer();
				s.append("\n name [" + this.name + "]");
				s.append("\n price [" + this.price + "]");
				s.append("\n symbol [" + this.symbol + "]");
				s.append("\n ts [" + this.ts + "]");
				s.append("\n type [" + this.type + "]");
				s.append("\n utctime [" + this.utctime + "]");
				s.append("\n volume [" + this.volume + "]");

				s.append("\n change [" + this.change + "]");
				s.append("\n chg_percent [" + this.chg_percent + "]");
				s.append("\n day_high [" + this.day_high + "]");
				s.append("\n day_low [" + this.day_low + "]");
				s.append("\n issuer_name [" + this.issuer_name + "]");
				s.append("\n issuer_name_lang [" + this.issuer_name_lang + "]");
				s.append("\n year_high [" + this.year_high + "]");
				s.append("\n year_low [" + this.year_low + "]");

			
				return s.toString();
			    }

				
				
			}
			
			@Override
		    public String toString() {
			StringBuffer s = new StringBuffer();
			s.append("\n classname [" + this.classname + "]");
			if (this.fields !=null){
				s.append("\n fields [" + this.fields.toString() + "]");
			}

			return s.toString();
		    }
		}



		
			
		}
		
		 

	}

}
