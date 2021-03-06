package com.wireless.soft.indices.cfd.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.wireless.soft.indices.cfd.business.adm.AdminEntity;
import com.wireless.soft.indices.cfd.business.entities.Company;
import com.wireless.soft.indices.cfd.business.entities.DataMiningCompany;
import com.wireless.soft.indices.cfd.business.entities.FundamentalHistoryCompany;
import com.wireless.soft.indices.cfd.business.entities.HistoricalDataCompany;
import com.wireless.soft.indices.cfd.business.entities.QuoteHistoryCompany;
import com.wireless.soft.indices.cfd.collections.CompanyRanking;
import com.wireless.soft.indices.cfd.collections.ReactionTrendSystem;
import com.wireless.soft.indices.cfd.collections.RelativeStrengthIndexData;
import com.wireless.soft.indices.cfd.deserializable.json.object.ReturnYahooFinanceQuoteObject;
import com.wireless.soft.indices.cfd.deserializable.json.object.SeriesObj;
import com.wireless.soft.indices.cfd.deserializable.json.object.StockMarketData;
import com.wireless.soft.indices.cfd.exception.BusinessException;
import com.wireless.soft.indices.cfd.statistics.DiffMaxMin;
import com.wireless.soft.indices.cfd.statistics.IStatistics;
import com.wireless.soft.indices.cfd.statistics.LastDigit;
import com.wireless.soft.indices.cfd.statistics.NotaPonderada;
import com.wireless.soft.indices.cfd.statistics.PercentageIncremento;
import com.wireless.soft.indices.cfd.statistics.PrecioAccion;
import com.wireless.soft.indices.cfd.statistics.PriceBetweenHighLow;
import com.wireless.soft.indices.cfd.statistics.PriceEarningRatio;
import com.wireless.soft.indices.cfd.statistics.PricePercentageIncrement;
import com.wireless.soft.indices.cfd.statistics.RelativeStrengthIndex;
import com.wireless.soft.indices.cfd.statistics.StockMayorMedia;
import com.wireless.soft.indices.cfd.statistics.TendenciaTresMeses;
import com.wireless.soft.indices.cfd.util.UtilGeneral;

/**
 * Clase principal encargada de obtener los indices de diferentes compaias
 * 
 * @author Francisco Corredor https://sites.google.com/site/gson/gson-user-guide
 *         TODO Conseguir BD en la nube y realizar lgica de negocio en CLOUD
 */

enum Evalua {
	ONE, THREE
}

enum TENDENCIA {
	minusalza, minusbaja, ALZA, BAJA, NO_EVALUADA;
}

/*
 * En base de datos: 
 * CEROS --> 0 
 * MF1 --> 1 
 * MF2 --> 2 
 * MF3 --> 3 
 * SHORT --> 4 
 * LONG  --> 5
 */
enum MOMENTUM_FACTOR {
	CEROS, MF1, MF2, MF3, SHORT, LONG;
}

public class ObtenerMarketIndex {

	// ////////////////////////////////////////////////////////////////////////
	// Atributos de la clase
	// ////////////////////////////////////////////////////////////////////////
	private final Gson gson = this.createGson();
	/** */
	private AdminEntity admEnt = null;
	
	private static int WAIT_TIME = 3500;

	private static int diasIntentos = -1;

	private int variableGlobalIntentos = 0;
	
	private List<Company> cmpGlobal = null;

	/*
	 * El sistema esta generando un out of memory porque esta creando varias
	 * instancias de los objetos por el loop, la idea es reutilizar los objetos para
	 * que la JVM, no los tenga que crear de nuevo y verificar el garbage collector,
	 */
	private static List<CompanyRanking> cr;
	private static ReturnYahooFinanceQuoteObject ri;

	// ////////////////////////////////////////////////////////////////////////
	// Logger de la clase
	// ////////////////////////////////////////////////////////////////////////
	private static Logger _logger = Logger.getLogger(ObtenerMarketIndex.class);

	public ObtenerMarketIndex() {
		admEnt = AdminEntity.getInstance();
	}
	
	public void freeAdminEntity() {
		admEnt.free();
	}

	/**
	 * @param args
	 *            args[0] --> Persistir o consultar (1/0) args[1] --> Numero de
	 *            iteraciones anteriores a ver args[2] --> Porcentaje [1-100],
	 *            indicador del TOP de las mejores compa_ias a imprimir, dependiendo
	 *            del porcentaje Samples: java -jar indicesToCFD.jar 1 2 10 java
	 *            -jar indicesToCFD.jar 0 java -jar indicesToCFD.jar 3 1 ===>
	 *            imprime chart cada minuto de la compania 1
	 */
	public static void main(String[] args) {

		diasIntentos = -1;

		/*
		 * if (isValidTime()){
		 * _logger.info("Tiempo evaluacion termino, contactar a herbert.andes@gmail.com"
		 * ); System.exit(0); }
		 */

		if (null == args || args.length < 1) {
			_logger.info("Debe especificar un argumento");
			return;
		} else {
			_logger.info("ini Proceso " + new Date());
		}

		Integer argumento2 = null;
		Integer cortePorcentajePonderado = null;
		PropertyConfigurator.configure("log4j.properties");
		// Valida si hay un 2do argumento
		// i. # Iteraciones antes
		// ii. Id compania
		if (null != args && args.length > 1 && null != args[1]) {
			try {
				argumento2 = Integer.parseInt(args[1]);
			} catch (Exception e) {
				argumento2 = null;
			}
		}
		// Valida si hay un 3er argumento (# Corte pocentaje ponderado)
		if (null != args && args.length > 2 && null != args[2]) {
			try {
				cortePorcentajePonderado = Integer.parseInt(args[2]);
			} catch (Exception e) {
				cortePorcentajePonderado = null;
			}
		}

		// Inicializa la clase principal.
		ObtenerMarketIndex omi = new ObtenerMarketIndex();
		// if (args[0].equals("test")) {
		// Company cmp = new Company();
		// cmp.setId(609l);
		// omi.obtenerReturnIndex(cmp);
		// }

		/*
		 * if (args[0].equals("test")) { try { //ReturnHistoricaldataYahooFinance tr =
		 * omi.executeYahooIndexHistoricaldata(
		 * "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22YHOO%22%20and%20startDate%20%3D%20%222016-07-01%22%20and%20endDate%20%3D%20%222016-08-04%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback="
		 * ); //System.out.print( tr.toString() ); } catch (IOException e) {
		 * e.printStackTrace(); } }
		 */

		try {
			String accion = args[0];
			switch (accion) {
			case "0":
				_logger.info("Inicio run Many Times");
				runManytimes(omi, argumento2, cortePorcentajePonderado);
				break;
			case "1":
				_logger.info("Imprime el indicador OBV");
				_logger.info("Precio accion menor = & % volumen mayor a cero!" + "Time:" + new Date());
				omi.printOBV(argumento2, cortePorcentajePonderado, Evalua.ONE);
				_logger.info("Precio accion mayor & % volumen mayor a cero!" + "Time:" + new Date());
				omi.printOBV(argumento2, cortePorcentajePonderado, Evalua.THREE);
				break;
			case "2":
				_logger.info("Indica si el mecado esta en Bull o Bear");
				omi.printIndicadorMercado();
				break;
			case "3":
				_logger.info("Print Chart Company UP&Down Price");
				omi.printChartCompany(argumento2);
				break;
			case "4":
				_logger.info("Persistir cada 10 minutos informacion de la companias");
				omi.persisteVariasIteraciones();
				break;
			case "5":
				_logger.info("test Call PE Ratio");
				omi.printPERatio();
				break;
			case "6":
				_logger.info("Do nothing");
				break;
			case "7":
				_logger.info("Call relativeStrengthIndex: --> [" + args[1] + "]");
				// El identificador 0 es que no tiene iteracion y no debe almacenar ningun tipo
				// de informacion para el Datamining
				omi.relativeStrengthIndexFromGoogle(args[1], true, "0");
				break;
			case "8":
				_logger.info("Persiste info de las companies, consultando de yahoo [Go short]");
				omi.printPERatio();
				_logger.info("Precio accion menor = & % volumen mayor a cero!" + "Time:" + new Date());
				omi.printOBVGoShort(argumento2, cortePorcentajePonderado, Evalua.ONE);
				_logger.info("Precio accion mayor & % volumen mayor a cero!" + "Time:" + new Date());
				omi.printOBVGoShort(argumento2, cortePorcentajePonderado, Evalua.THREE);
				break;
			case "9":
				_logger.info("Obtener indicador YTD (Regla de tres con respecto al inicio del year) --> [" + args[1]
						+ "] | " + omi.getYearToDateReturn(Long.parseLong(args[1])));
				break;
			case "10":
				_logger.info(
						"Obtener historico de n companias, definiendo el dia en q empieza la iteracion sample:java -jar indicesToCFD.jar 10 JUP.L;NEO.PA;APC.DE;HSX.L -1 ");
				omi.relativeStrengthIndexArray(args[1], args[2]);
				break;
			case "11":
				_logger.info(
						" Obtener tendencia de la compania en n meses (0) - alza 	(1)	- baja		(2)	Alza		(3)	Baja ");
				_logger.info(" " + omi.getTendenciaGoogle(Long.parseLong(args[1]), Integer.parseInt(args[2])));
				break;
			case "12":
				_logger.info(" Evaluar data mining statistical modeling ");
				omi.getStatisticalModeling(Long.parseLong(args[1]));
				break;
			case "13":
				_logger.info(" Obtener historico diario para obtener el RSI ");
				omi.persistirHistoricoToRSI();
				break;
			case "14":
				_logger.info(" Print Momentum Factor ");
				omi.printMomentumFactor();
				break;
			case "15":
				_logger.info(" Print SINGLE BOS -REACTION vs TREND mode- ");
				omi.printBOS(argumento2, true);
				break;
			case "16":
				_logger.info(" Print ALL BOS -REACTION vs TREND mode- by company not ID, separate by ; ");
				omi.printBOSForEachCompany(args[1]);
				break;
			case "17":
				_logger.info(" Print reaction Mode - ");
				String[] hlca = args[1].split(":");
				UtilGeneral.isReactionMode(Double.parseDouble(hlca[0]), Double.parseDouble(hlca[1]),
						Double.parseDouble(hlca[2]), Double.parseDouble(hlca[3]), true);
				break;
			case "18":
				_logger.info(" Print possible Buy or Sell ");
				String[] cmps = args.length < 2 ? null : args[1].split(":");
				UtilGeneral.printPosibleBuyOrSell(cmps);
				break;
			case "19":
				_logger.info(" printLastReactionModeByCompany ");
				long idCompany 	= Long.parseLong( args[1] );
				double price	= Double.parseDouble(args[2]);
				omi.printLastReactionModeByCompany(idCompany, price);
				break;
			case "20":
				_logger.info(" print Awesome Oscillator " + omi.getAwesomeOscillator(args[1]));
				break;
				
				

			default:
				_logger.info(" No realiza acci_n");
				break;
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (BusinessException be) {
			be.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		_logger.info("fin Proceso " + new Date());

		System.exit(0);
	}

	/**
	 * @param url
	 * @return el Quote de yahooIndex
	 * @throws IOException
	 * @throws Exception
	 */
	private ReturnYahooFinanceQuoteObject executeYahooIndexQuoteHTML(String url) throws IOException {

		ReturnYahooFinanceQuoteObject.Query.Results.Quote q = new ReturnYahooFinanceQuoteObject().new Query().new Results().new Quote();
		ReturnYahooFinanceQuoteObject.Query.Results r = new ReturnYahooFinanceQuoteObject().new Query().new Results();
		ReturnYahooFinanceQuoteObject.Query qry = new ReturnYahooFinanceQuoteObject().new Query();
		ReturnYahooFinanceQuoteObject y = new ReturnYahooFinanceQuoteObject();
		// url =
		// "https://www.msn.com/en-us/money/stockdetails/analysis/fi-160.1.BNP.PAR";
		// url =
		// "https://www.msn.com/en-us/money/stockdetails/analysis/fi-125.1.KOEBF.GREY";

		try {
			// -----------------------------------------------------------
			Document doc;
			doc = Jsoup.connect(url).timeout(10000).get();

			Elements newsHeadlines = doc.select("p.truncated-string");
			Elements newsHeadlines2 = doc.select("div.precurrentvalue");

			/*
			 * String[] strSplit = newsHeadlines.text().split(" "); q.setBid(strSplit[0]);
			 * q.setAsk(strSplit[0]); q.setPERatio(strSplit[15]);
			 * 
			 * url = "https://uk.finance.yahoo.com/quote/BNP.PA/news?p=BNP.PA";
			 * 
			 * Elements newsHeadlines = doc
			 * //.select("span[src~=Trsdu(0.3s) Fw(b) Fz(36px) Mb(-4px) D(ib)]" );
			 * //.select("img[src~=(?i)\\.(png|jpe?g)]");
			 * //.select("span[class~=(Trsdu)+]"); .select("span[class~=(Trsdu)+]");
			 */
			// _logger.info(" --> : " + newsHeadlines.text());
			// _logger.info(" --> : " + newsHeadlines2.text());

			String peratio = null;
			String marketCap = null;
			String volume = null;
			String dayRange = null;

			if (newsHeadlines != null) {
				try {
					peratio = newsHeadlines.text().substring(newsHeadlines.text().indexOf("P/E Ratio (EPS)") + 16,
							newsHeadlines.text().indexOf("(", newsHeadlines.text().indexOf("P/E Ratio (EPS)") + 16));
				} catch (Exception e) {
				}
				try {
					marketCap = newsHeadlines.text().substring(newsHeadlines.text().indexOf("Market Cap.") + 12,
							newsHeadlines.text().indexOf("Dividend Rate (Yield)"));
				} catch (Exception e) {
				}
				try {
					volume = newsHeadlines.text().substring(newsHeadlines.text().indexOf("Volume (Avg)") + 13,
							newsHeadlines.text().indexOf("(", newsHeadlines.text().indexOf("Volume (Avg)") + 13));
				} catch (Exception e) {
				}
				try {
					dayRange = newsHeadlines.text().substring(newsHeadlines.text().indexOf("Day's Range") + 12,
							newsHeadlines.text().indexOf("52Wk Range"));
				} catch (Exception e) {
				}
			}

			// _logger.info("peratio: " + peratio == null ? null : peratio.trim());
			q.setBid(newsHeadlines2.text() == null ? null : newsHeadlines2.text().trim().replaceAll(",", ""));
			q.setAsk(newsHeadlines2.text() == null ? null : newsHeadlines2.text().trim().replaceAll(",", ""));
			q.setPERatio(peratio);
			q.setMarketCapitalization(marketCap);
			q.setVolume(volume);
			if (dayRange != null && dayRange.split("-").length > 0) {
				q.setDaysLow(dayRange.split("-")[0].replaceAll(",", ""));
				q.setDaysHigh(dayRange.split("-")[1].replaceAll(",", ""));
			}

			r.setQuote(q);
			qry.setResults(r);
			y.setQuery(qry);

			return y;

		} catch (FileNotFoundException e) {
			_logger.info("Error al leer [" + url + "](FileNotFoundException)",e);
		} catch (IOException e) {
			_logger.info("Error al leer [" + url + "](IOException)",e);
		} catch (Exception e) {
			_logger.info("Error al leer [" + url + "](Exception)",e);
		}

		return null;

	}

	/**
	 * @throws Exception
	 */
	private void printOBV(Integer numIteracionAntes, Integer cortePorcentajePonderado, Evalua ev) {
		cr = new LinkedList<CompanyRanking>();
		// List<Double> lstMediaSearch = new ArrayList<Double>();
		try {
			/*
			 * 0 --> Accion disminuye, volumen disminuye 1 --> Accion disminuye, volumen
			 * aumenta 2 --> Accion aumenta, volumen disminuye 3 --> Accion aumenta, volumen
			 * aumenta
			 */
			if (cmpGlobal == null) {
				cmpGlobal = admEnt.getCompanies();
			}
			int contadorLiberaRecursos = 0;
			for (Company cmp : cmpGlobal) {
				List<Object> listIdxCompany = admEnt.getCompIdxQuote(cmp);
				Object tmp[] = listIdxCompany.toArray();
				if (null != tmp && tmp.length > 1) {
					// TODO --> Realizar comparacion con el primer regitro del dia
					QuoteHistoryCompany qhcBefore = (QuoteHistoryCompany) tmp[numIteracionAntes == null ? 1
							: numIteracionAntes];
					QuoteHistoryCompany qhcNow = (QuoteHistoryCompany) tmp[0];

					/*
					 * Double valueNowVolume = Double.valueOf(qhcNow.getVolume());
					 * lstMediaSearch.add(valueNowVolume);
					 * 
					 * if(valueNowVolume == null || valueNowVolume.longValue() == 0){ continue; }
					 */

					// Obtencion de PE ratio by company
					FundamentalHistoryCompany fc = admEnt.getLastFundamentalRecord(cmp);
					if (null == fc) {
						System.out.print("," + (null == cmp ? "" : cmp.getId()));
						continue;
					}
					Double PERatio = null;
					try {
						PERatio = Double.valueOf(fc.getpERatio() != null ? fc.getpERatio() : "-1");
					} catch (NumberFormatException n) {
						PERatio = 1001d;
						_logger.info("PERatio mayor a mil:" + fc.getpERatio() + "(" + cmp.getId() + ")");
					}
					if (PERatio < 0) {
						continue;
					}

					// Obtencion precio mas bajo
					Double price = null;
					try {
						price = Double.valueOf(qhcNow.getPrice() != null ? qhcNow.getPrice() : "0");
					} catch (NumberFormatException n) {
						_logger.info("price:" + qhcNow.getPrice() + "(" + cmp.getId() + ")");
					}

					if (price == null || price.doubleValue() == 0) {
						continue;
					}

					// Double supportLevel =
					// Double.valueOf(qhcNow.getYear_low()!=null?qhcNow.getYear_low():"0");

					// _logger.info("Company{"+cmp.getName()+"} PERatio{"+PERatio+"}");

					/*
					 * TODO Calcular cuanto porcentaje subio y dar un ponderado i. Si encuentra una
					 * noticia que contenga palabras positivas, dar una nota apreciativa al ponderad
					 * de 0,05% //Guaardar la informacion que se itera en Collections y que realize
					 * ordenamiento, para que imprima en linea el resultado y no tener que
					 * almacenarlo en ls BD para despues leerlo o calcularlo. Realixar el calculo de
					 * las mejoras coma�ias depues de la iteraci�n por cada uno de las compa�ias que
					 * estan cumplienod con el // * calculo/estrategia definida en el algoritmo!
					 * adicionar la variable /indice P/e usando http://jsoup.org/ 2015Dec24--> Tener
					 * en cuenta el laboratorio de analisis fundamental realizado
					 * {https://drive.google.com/drive/u/0/folders/
					 * 0BwJXnohKnxjbfmNJV2NsYm4zT1Zqb0VlUC1zaUlfcjRaM2VIX1E2WmZ6cU1MN1J2WWJhTGs}
					 */
					/*
					 * TODO: Filtrar YTD positivo:
					 * CAGR=(endingvalue/beginingvalue)elevado[^](1/#deyears) - 1 REF:
					 * http://www.investopedia.com/terms/a/annual-return.asp
					 */

					if ((PERatio > 0 && PERatio <= 17)
					// && (price > supportLevel)
					) {

						double ytd = 0;
						try {
							ytd = this.getYearToDateReturn(cmp.getId());
						} catch (IllegalStateException ie) {
							// _logger.info("ie.getMessage(this.getYearToDateReturn): " + ie.getMessage());
						} catch (Exception e) {
							// _logger.info("e.getMessage(this.getYearToDateReturn): " + e.getMessage());
						}

						// Evalua si la companie tiene rendimientos positivos
						if (ytd > 0) {

							CompanyRanking addAR = null;

							/*
							 * Variable valor --> A Variable volumen --> B A B 0 0 ==0 Evalua.ZERO 0 1 ==1
							 * Evalua.ONE 1 0 ==2 Evalua.TWO 1 1 ==3 Evalua.THREE
							 */
							// Evalua ev = Evalua.THREE;
							switch (ev) {
							case THREE:
								addAR = evalua03(qhcBefore, qhcNow, cmp);
								break;
							case ONE:
								addAR = evalua01(qhcBefore, qhcNow, cmp);
								break;
							default:
								break;

							}

							if (null != addAR) {
								addAR.setPeRatio(PERatio);
								addAR.setCapitalization(fc.getMarketCapitalization());
								addAR.setYTD(ytd);
								cr.add(addAR);
							}

						} // End --> YTD index
					} // END --> PERatio validation

				}
				
				if (contadorLiberaRecursos++ % 50 == 0) {
					admEnt.free();
				}

			} // END --> for companies

			// Imprime la media
			// UtilGeneral.imprimirMedia(lstMediaSearch);

			// Imprime Arreglo ordenado
			Collections.sort(cr);
			// TODO persistir la informacion del resultado, con la fecha de la
			// ejecui_n del proceso
			String toexecute = "java -jar indicesToCFD.jar 10 (RunMe con el Close del dia que paso, No correr runME.) ";
			int i = 0;
			Long identificadorUnicoIteracion = UtilGeneral.obtenerIdIteracion();
			for (CompanyRanking companyRanking : cr) {
				if (null != companyRanking && companyRanking
						.getNotaPonderada() > (cortePorcentajePonderado == null ? 25 : cortePorcentajePonderado)) {
					// TODO --> PErsisistir la informaci_n para saber cada
					// cuanto aparece una compa_ia en la impresion del lsitado
					// en un determinado tiempo.
					_logger.info((++i) + " " + companyRanking.toString());
					// Persiste informacion en DataMining
					DataMiningCompany dmcToPersistir = UtilGeneral.construirObjetoDataMiningCompany(companyRanking,
							identificadorUnicoIteracion);
					admEnt.persistirDataMiningCompany(dmcToPersistir);
					toexecute += companyRanking.getSymbol() + ";";
				}
			}
			_logger.info("Identificador iteracion:" + identificadorUnicoIteracion);
			_logger.info(toexecute + " " + identificadorUnicoIteracion);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @throws Exception
	 */
	private void printOBVGoShort(Integer numIteracionAntes, Integer cortePorcentajePonderado, Evalua ev) {
		List<CompanyRanking> cr = new LinkedList<CompanyRanking>();
		List<Double> lstMediaSearch = new ArrayList<Double>();
		try {
			/*
			 * 0 --> Accion disminuye, volumen disminuye 1 --> Accion disminuye, volumen
			 * aumenta 2 --> Accion aumenta, volumen disminuye 3 --> Accion aumenta, volumen
			 * aumenta
			 */
			if (cmpGlobal == null) {
				cmpGlobal = admEnt.getCompanies();
			}
			for (Company cmp : cmpGlobal) {
				List<Object> listIdxCompany = admEnt.getCompIdxQuote(cmp);
				Object tmp[] = listIdxCompany.toArray();
				if (null != tmp && tmp.length > 1) {
					// TODO --> Realizar comparaci_n con el primer regitro del dia
					QuoteHistoryCompany qhcBefore = (QuoteHistoryCompany) tmp[numIteracionAntes == null ? 1
							: numIteracionAntes];
					QuoteHistoryCompany qhcNow = (QuoteHistoryCompany) tmp[0];

					Double valueNowVolume = Double.valueOf(qhcNow.getVolume());
					lstMediaSearch.add(valueNowVolume);

					// Obtencion de PE ratio by company
					FundamentalHistoryCompany fc = admEnt.getLastFundamentalRecord(cmp);
					if (null == fc) {
						System.out.print("," + (null == cmp ? "" : cmp.getId()));
						continue;
					}

					Double PERatio = Double.valueOf(fc.getpERatio() != null ? fc.getpERatio() : "-1");

					// _logger.info("Company{"+cmp.getName()+"} PERatio{"+PERatio+"}");

					/*
					 * TODO Calcular cuanto porcentaje subio y dar un ponderado i. Si encuentra una
					 * noticia que contenga palabras positivas, dar una nota apreciativa al ponderad
					 * de 0,05% //Guaardar la informacion que se itera en Collections y que realize
					 * ordenamiento, para que imprima en linea el resultado y no tener que
					 * almacenarlo en ls BD para despues leerlo o calcularlo. Realixar el calculo de
					 * las mejoras coma�ias depues de la iteraci�n por cada uno de las compa�ias que
					 * estan cumplienod con el // * calculo/estrategia definida en el algoritmo!
					 * adicionar la variable /indice P/e usando http://jsoup.org/ 2015Dec24--> Tener
					 * en cuenta el laboratorio de analisis fundamental realizado
					 * {https://drive.google.com/drive/u/0/folders/
					 * 0BwJXnohKnxjbfmNJV2NsYm4zT1Zqb0VlUC1zaUlfcjRaM2VIX1E2WmZ6cU1MN1J2WWJhTGs}
					 */
					/*
					 * TODO: Filtrar YTD negativo:
					 * CAGR=(endingvalue/beginingvalue)elevado[^](1/#deyears) - 1 REF:
					 * http://www.investopedia.com/terms/a/annual-return.asp
					 */
					if (PERatio > 16) {

						double ytd = 0;
						try {
							ytd = this.getYearToDateReturn(cmp.getId());
						} catch (IllegalStateException ie) {
							// _logger.info("ie.getMessage(this.getYearToDateReturn): " + ie.getMessage());
						} catch (Exception e) {
							// _logger.info("e.getMessage(this.getYearToDateReturn): " + e.getMessage());
						}

						// Evalua si la companie tiene rendimientos negativos
						if (ytd < 0) {

							CompanyRanking addAR = null;

							/*
							 * Variable valor --> A Variable volumen --> B A B 0 0 ==0 Evalua.ZERO 0 1 ==1
							 * Evalua.ONE 1 0 ==2 Evalua.TWO 1 1 ==3 Evalua.THREE
							 */
							// Evalua ev = Evalua.THREE;
							switch (ev) {
							case THREE:
								addAR = evalua03(qhcBefore, qhcNow, cmp);
								break;
							case ONE:
								addAR = evalua01(qhcBefore, qhcNow, cmp);
								break;
							default:
								break;

							}

							if (null != addAR) {
								addAR.setPeRatio(PERatio);
								addAR.setCapitalization(fc.getMarketCapitalization());
								addAR.setYTD(ytd);
								cr.add(addAR);
							}

						} // End --> YTD index
					} // END --> PERatio validation

				}

			} // END --> for companies

			// Imprime la media
			UtilGeneral.imprimirMedia(lstMediaSearch);

			// Imprime Arreglo ordenado
			Collections.sort(cr);
			// TODO persistir la informacion del resultado, con la fecha de la
			// ejecucion del proceso
			int i = 0;
			for (CompanyRanking companyRanking : cr) {
				if (null != companyRanking && companyRanking
						.getNotaPonderada() > (cortePorcentajePonderado == null ? 25 : cortePorcentajePonderado)) {
					// TODO --> PErsisistir la informaci�n para saber cada
					// cuanto aparece una compa�ia en la impresion del lsitado
					// en un determinado tiempo.
					_logger.info((++i) + " " + companyRanking.toString());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @throws BusinessException
	 * @throws IOException
	 */
	private void persistirCompaniesQuotes(ReturnYahooFinanceQuoteObject ri, Company cmp)
			throws BusinessException, IOException {

		admEnt.persistirCompaniesQuotes(ri, cmp);

	}

	/**
	 * @throws BusinessException
	 * @throws IOException
	 */
	private void persistirCompaniesFundamental(ReturnYahooFinanceQuoteObject rf, Company cmp)
			throws BusinessException, IOException {

		admEnt.persistirCompaniesFundamental(rf, cmp);

	}

	/**
	 * Imprime si el mercado esta en Bull o Bear
	 * 
	 * @throws Exception
	 * @throws BusinessException
	 */
	private void printIndicadorMercado() {
		try {
			Company cmp = new Company();
			cmp.setId(43l);
			QuoteHistoryCompany qFirstHC = admEnt.getFirstRecordDay(cmp);
			if (null != qFirstHC) {
				_logger.info(qFirstHC.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param idCompany
	 *            imprime resultado cada instante de tiempo 1 minuto
	 *            [nombreCompania], [%inc],
	 *            estaSubiendo[true/false(numeroVeces)*Volver a iteracion uno si
	 *            cambia la bandera]
	 */
	private void printChartCompany(Integer idCompany) {
		if (null != idCompany) {
			try {
				Company cmp = new Company();
				cmp.setId(idCompany.longValue());
				cmp = admEnt.getCompanyById(cmp);
				// Raaliza iteracion infinita
				int iteracionUpDown = 0;
				boolean banderaUpDown = false;
				CompanyRanking crIteracion1 = this.evaluaRanking(cmp);
				if (null != crIteracion1) {
					while (true) {
						iteracionUpDown++;
						// 1. Persiste info compania
						ReturnYahooFinanceQuoteObject ri = this.executeYahooIndexQuoteHTML(cmp.getUrlQuote());
						// _logger.info(cmp.getUrlQuote());
						// Persiste en loa BD
						this.persistirCompaniesFundamental(ri, cmp);
						this.persistirCompaniesQuotes(ri, cmp);
						// 2. espera un instante de tiempo 3segundos
						Thread.sleep(10000);
						// 3. Consulta si la compania subio o bajo
						CompanyRanking cr = this.evaluaRanking(cmp);
						if (null != cr) {
							// 4. imprime
							_logger.info("[" + iteracionUpDown + "] " + cr.printToChart());
							_logger.info("[" + iteracionUpDown + "] " + cr.toString());
							if (cr.getPrecioEvaluado() > crIteracion1.getPrecioEvaluado()) {
								banderaUpDown = !banderaUpDown;
							}
							if (banderaUpDown) {
								iteracionUpDown = 0;
							}
						}
						crIteracion1 = cr;
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param cmp
	 *            Evalua el rankig para ser impreso en el chart
	 * @throws Exception
	 */
	private CompanyRanking evaluaRanking(Company cmp) throws Exception {

		CompanyRanking addAR = null;
		List<Object> listIdxCompany = admEnt.getCompIdxQuote(cmp);
		Object tmp[] = listIdxCompany.toArray();
		if (null != tmp && tmp.length > 1) {

			QuoteHistoryCompany qhcBefore = (QuoteHistoryCompany) tmp[1];
			QuoteHistoryCompany qhcNow = (QuoteHistoryCompany) tmp[0];
			Double valueBeforePrice = Double.valueOf(qhcBefore.getPrice());
			Double valueNowPrice = Double.valueOf(qhcNow.getPrice());

			addAR = new CompanyRanking();
			addAR.setCompanyName(cmp.getName());
			addAR.setIdCompany(cmp.getId());

			addAR.setPricePercentageincrement((((valueNowPrice * 100) / valueBeforePrice) - 100));
			addAR.setDayLow(Double.valueOf(qhcNow.getDay_low()));
			addAR.setPrecioEvaluado(valueNowPrice);
			addAR.setVolumenEvaluado(valueNowPrice);
			addAR.setDayHigh(Double.valueOf(qhcNow.getDay_high()));
			addAR.setFechaIteracion1(qhcBefore.getFechaCreacion());
			addAR.setFechaIteracion2(qhcNow.getFechaCreacion());
			addAR.setSymbol(cmp.getGoogleSymbol());

			try {
				// Obtencion de PE ratio by company
				FundamentalHistoryCompany fc = admEnt.getLastFundamentalRecord(cmp);
				Double PERatio = Double.valueOf(fc.getpERatio() != null ? fc.getpERatio() : "-1");
				addAR.setPeRatio(PERatio);
			} catch (Exception e) {
				_logger.info("No obtine PERATIO");
			}

		}

		return addAR;
	}

	/**
	 * Persiste la informaci�n varias veces
	 */
	private void persisteVariasIteraciones() {
		try {
			while (true) {
				this.printPERatio();
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @throws BusinessException
	 * @throws IOException
	 */
	private void printPERatio() throws BusinessException, IOException {
		ri = null;
		
		if (cmpGlobal == null) {
			cmpGlobal = admEnt.getCompanies();
		}
		int contadorLiberaRecursos = 0;
		for (Company cmp : cmpGlobal) {
			if (null != cmp && null != cmp.getUrlIndex() && cmp.getUrlIndex().length() > 3) {
				//_logger.info("cmp" + cmp.getId());
				ri = this.executeYahooIndexQuoteHTML(cmp.getUrlQuote());
				//_logger.info("Execute yahoo" );
				this.persistirCompaniesFundamental(ri, cmp);
				//_logger.info("Persiste Fundamental" );
				this.persistirCompaniesQuotes(ri, cmp);
				//_logger.info("Persiste Quotes" );
				
				if (contadorLiberaRecursos++ % 50 == 0) {
					admEnt.free();
				}
			}
		}
	}

	// TODO: --> REalizar algoritmo cuando el precio este bajando , recuerde el
	// laboratiorio q realizo con internet y q tiene q
	// comprar barato para vender caro. En el ponderado evaluar cuanto a disminuido
	// el precio.

	/**
	 * @param qhcBefore
	 * @param qhcNow
	 * @param cmp
	 * @return Evalua e = Evalua.ONE; _logger.info("Precio accion menor = & %
	 *         volumen mayor a cero!");
	 */
	private CompanyRanking evalua01(QuoteHistoryCompany qhcBefore, QuoteHistoryCompany qhcNow, Company cmp) {

		CompanyRanking addAR = null;

		try {

			Double valueBeforePrice = Double.valueOf(qhcBefore.getPrice());
			Double valueNowPrice = Double.valueOf(qhcNow.getPrice());
			Double valueBeforeVolume = Double.valueOf(qhcBefore.getVolume());
			Double valueNowVolume = Double.valueOf(qhcNow.getVolume());

			if (valueNowPrice <= valueBeforePrice) {
				// Obtine las compa�ias q tienen un volumen superior!
				// TODO obtner la media y dar mas calificaci�n a los valores que esten
				// dentro mas cerca a la media
				// Realizar pruebas con la informaci�n que tiene en la BD
				// Idea01: 27Dec2015 --> De la BD sacar la media
				if ((((valueNowVolume * 100) / valueBeforeVolume) - 100) > 0) {

					addAR = new CompanyRanking();
					addAR.setCompanyName(cmp.getName());
					addAR.setIdCompany(cmp.getId());
					addAR.setOBV((valueBeforeVolume + valueNowVolume));
					addAR.setVolumePercentageIncrement((((valueNowVolume * 100) / valueBeforeVolume) - 100));
					addAR.setPricePercentageincrement((((valueNowPrice * 100) / valueBeforePrice) - 100));
					addAR.setDayLow(Double.valueOf(qhcNow.getDay_low()));
					addAR.setPrecioEvaluado(valueNowPrice);
					addAR.setVolumenEvaluado(valueNowVolume);
					addAR.setDayHigh(Double.valueOf(qhcNow.getDay_high()));
					addAR.setFechaIteracion1(qhcBefore.getFechaCreacion());
					addAR.setFechaIteracion2(qhcNow.getFechaCreacion());
					addAR.setSymbol(cmp.getGoogleSymbol());
					// 01Aug2016
					// Adicionar funcionalidad que consulta los indicadores YTD & 1YR return from
					// Bloomberg.
					addAR.setYearReturn(this.obtenerReturnIndex(cmp));
				}

			}
		} catch (Exception e) {
			// _logger.info("Error en : evalua01 --> " + e.getMessage());
		}

		return addAR;

	}

	/**
	 * @param qhcBefore
	 * @param qhcNow
	 * @param cmp
	 * @return Evalua e = Evalua.THREE; _logger.info("Precio accion mayor & %
	 *         volumen mayor a cero!");
	 */
	private CompanyRanking evalua03(QuoteHistoryCompany qhcBefore, QuoteHistoryCompany qhcNow, Company cmp) {

		CompanyRanking addAR = null;

		try {

			Double valueBeforePrice = Double.valueOf(qhcBefore.getPrice());
			Double valueNowPrice = Double.valueOf(qhcNow.getPrice());
			Double valueBeforeVolume = Double.valueOf(qhcBefore.getVolume());
			Double valueNowVolume = Double.valueOf(qhcNow.getVolume());

			if (valueNowPrice > valueBeforePrice) {
				// Obtine las compa_ias q tienen un volumen superior!
				// TODO obtner la media y dar mas calificaci_n a los valores que esten
				// dentro mas cerca a la media
				// Realizar pruebas con la informaci_n que tiene en la BD
				// Idea01: 27Dec2015 --> De la BD sacar la media
				if ((((valueNowVolume * 100) / valueBeforeVolume) - 100) > 0) {

					addAR = new CompanyRanking();
					addAR.setCompanyName(cmp.getName());
					addAR.setIdCompany(cmp.getId());
					addAR.setOBV((valueBeforeVolume + valueNowVolume));
					addAR.setVolumePercentageIncrement((((valueNowVolume * 100) / valueBeforeVolume) - 100));
					addAR.setPricePercentageincrement((((valueNowPrice * 100) / valueBeforePrice) - 100));
					addAR.setDayLow(Double.valueOf(qhcNow.getDay_low()));
					addAR.setPrecioEvaluado(valueNowPrice);
					addAR.setVolumenEvaluado(valueNowVolume);
					addAR.setDayHigh(Double.valueOf(qhcNow.getDay_high()));
					addAR.setFechaIteracion1(qhcBefore.getFechaCreacion());
					addAR.setFechaIteracion2(qhcNow.getFechaCreacion());
					addAR.setSymbol(cmp.getGoogleSymbol());
					// 01Aug2016
					// Adicionar funcionalidad que consulta los indicadores YTD & 1YR return from
					// Bloomberg.
					addAR.setYearReturn(this.obtenerReturnIndex(cmp));
				}

			}

		} catch (Exception e) {
			// _logger.info("Error en: evalua03 --> " + e.getMessage());
		}

		return addAR;

	}



	/**
	 * Obtiene el valor de YTD & 1YR from Bloomberg
	 * 
	 * @param idCmp
	 */
	private String obtenerReturnIndex(Company cmp) {
		// Consulta la URL, para obtener el indicador
		String ridx = null;
		// Modificar para que traiga de :
		// https://markets.ft.com/data/equities/tearsheet/forecasts?s=MGNS:LSE

		/*
		 * try{ BloombergIndex bi = admEnt.getBloombergIndex(cmp);
		 * //_logger.info("bi.getUrlBloomberg() " + bi.getUrlBloomberg()); ridx =
		 * UtilGeneral.getYearReturn(bi.getUrlBloomberg());
		 * 
		 * } catch(Exception e){
		 * _logger.info("Error al obtener la informacion de Bloomberg:" +
		 * e.getMessage()); //e.printStackTrace(); }
		 */

		return ridx;

	}

	/*
	 * Obtine el indicador, para saber que tan costosa o overbuy esta la accion. El
	 * input lo toma de un servicio de un servicio de yahoo nDays debe ser negativo
	 * para que sirva el algoritmo
	 */
	private void relativeStrengthIndexFromGoogle(String companySymbol, boolean print, String iteracion) {
		// Convertir company Symbol en id de compañua
		Company cmp = null;
		cmp = new Company();
		cmp.setGoogleSymbol(companySymbol);
		// Variables to evaluate REACTION MODE vs TREND MODE
		double lastHigh = 0, lastLow = 0, lastClose = 0;

		try {
			cmp = this.admEnt.getCompanyBySymbol(cmp);
		} catch (Exception e1) {
			_logger.error("Error al traer info de la compania", e1);
			return;
		}

		Long idCompanyDB = cmp.getId();

		// obtener el historico de 14 dias o iteraciones!
		if (print) {
			_logger.info("obtener el historico de 14 dias o iteraciones!");
		}

		List<RelativeStrengthIndexData> lstRSI = null;

		// lstRSI = UtilGeneral.getListaRSIGoogle(companySymbol, fechaHoy, mesatras,
		// print);
		lstRSI = UtilGeneral.getListaRSIGoogleDB(idCompanyDB, print);

		// ordena descendente ID, porque el formamo de la data esta de mayor a menor
		// y las fechas deben ordenarse de menor a Mayor
		Collections.sort(lstRSI);

		// Obtener el valor maximo y minimo en el cierre de la accion al dia
		double max = 0;
		double min = 0;

		// obtener el promedio de H&L
		double avgHigh = 0;
		double avgLow = 0;
		// Iteracion 2 change = close today - close yesterday
		for (int i = 0; i < lstRSI.size(); i++) {
			if (i == 0) {
				RelativeStrengthIndexData relativeStrengthIndexMM = lstRSI.get(i);
				if (null != relativeStrengthIndexMM) {
					max = relativeStrengthIndexMM.getClose();
					min = relativeStrengthIndexMM.getClose();
					avgHigh += relativeStrengthIndexMM.getHigh();
					avgLow += relativeStrengthIndexMM.getLow();
				}

			}
			if (i > 0) {
				RelativeStrengthIndexData relativeStrengthIndexDataA = lstRSI.get(i - 1);
				RelativeStrengthIndexData relativeStrengthIndexDataB = lstRSI.get(i);
				// _logger.info(relativeStrengthIndexDataA.getClose());
				// _logger.info(relativeStrengthIndexDataB.getClose());
				relativeStrengthIndexDataB
						.setChange(-relativeStrengthIndexDataA.getClose() + relativeStrengthIndexDataB.getClose());
				// _logger.info(relativeStrengthIndexDataB.toString());
				lstRSI.set(i, relativeStrengthIndexDataB);

				// Valida valor Mayor y menor
				if (relativeStrengthIndexDataB.getClose() > max) {
					max = relativeStrengthIndexDataB.getClose();
				}
				if (relativeStrengthIndexDataB.getClose() < min) {
					min = relativeStrengthIndexDataB.getClose();
				}

				// sumar el average
				avgHigh += relativeStrengthIndexDataB.getHigh();
				avgLow += relativeStrengthIndexDataB.getLow();

				// Variables to evaluate REACTION MODE vs TREND MODE
				lastClose = relativeStrengthIndexDataB.getClose();
				lastHigh = relativeStrengthIndexDataB.getHigh();
				lastLow = relativeStrengthIndexDataB.getLow();
			}

		}

		avgHigh = avgHigh / lstRSI.size();
		avgLow = avgLow / lstRSI.size();

		// print Resultado
		// for (RelativeStrengthIndexData relativeStrengthIndexData : lstRSI) {
		// _logger.info(relativeStrengthIndexData.toString());
		// }

		int win = 0;
		int lst = 0;
		// Iteracion 3 suma gain and lost
		BigDecimal gain = new BigDecimal(0);
		gain.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		BigDecimal lost = new BigDecimal(0);
		lost.setScale(10, BigDecimal.ROUND_UNNECESSARY);
		if (null != lstRSI && lstRSI.size() > 2) {
			for (int i = 0; i < 14; i++) {
				try {
					double change = lstRSI.get(i).getChange();
					if (change > 0) {
						win++;
						// _logger.info("change (+) >" + change);
						gain = gain.add(new BigDecimal(change));
						// _logger.info("gain >" + gain);
					} else if (change < 0) {
						lst++;
						// _logger.info("change (-) >" + change);
						lost = lost.add(new BigDecimal(Math.abs(change)));
						// _logger.info("lost >" + lost);
					}
				} catch (IndexOutOfBoundsException iobE) {
					System.out.print("Error en obtenerReturnIndex" + iobE.getMessage());
				}
			}
		}

		// _logger.info(gain + "<-- g");
		// _logger.info(lost + "<-- l");

		double rs = (gain.doubleValue() / 14) / (lost.doubleValue() / 14);
		double rsi = 100 - (100 / (1 + rs));

		int diff = (int) (max - min);
		int porcentajeIncremento = (int) (((100 * avgHigh) / avgLow) - 100);

		// if (diff > 49 || porcentajeIncremento>=3 ){

		System.out.print("symbol:[" + companySymbol + "]");
		System.out.print("|RSI14:[" + String.format("%.4f", rsi) + "]");
		System.out.print("|max:[" + max + "]");
		System.out.print("|min:[" + min + "]");
		System.out.print("|diff:[" + String.format("%.4f", (max - min))
				+ (diff > 49 ? "Diferencia mayor a 49 DataMining*" : "") + "]");
		System.out.print("|%IncrementoHigh:[" + String.format("%.4f", (((100 * avgHigh) / avgLow) - 100))
				+ (porcentajeIncremento >= 3 ? "%IncMayorIgual3 DataMining *" : "") + "]");
		System.out.print("|media:[" + String.format("%.2f", (max + min) / 2) + "] ");
		_logger.info("|win:[" + win + "]|lost:[" + lst + "]|diff(win-lost):[" + (win - lst) + "] ");
		// }
		// Almacenar informacion de Data Mining si el numero
		// de la iteracion contine informacion
		try {
			if (null != iteracion && Long.valueOf(iteracion) > 0) {
				// Consultar identificador de la compania
				DataMiningCompany dmCmp = new DataMiningCompany();
				dmCmp.setCompany(cmp);
				dmCmp.setIdIteracion(Long.valueOf(iteracion));
				dmCmp = admEnt.getDMCompanyByCmpAndIteracion(dmCmp);
				dmCmp.setRelativeStrengthIndex(String.format("%.4f", rsi));
				dmCmp.setDiffMaxMin(String.format("%.4f", (max - min)));
				dmCmp.setPercentageIncrement(String.format("%.4f", (((100 * avgHigh) / avgLow) - 100)));
				boolean isStockPriceMayorMedia = (Double
						.parseDouble(dmCmp.getStockPrice().replace(',', '.').trim()) > (max + min) / 2);
				dmCmp.setIsTockPriceMayorMedia(isStockPriceMayorMedia);
				// Obtener tendencia (0) - alza (1) - baja (2) Alza (3) Baja
				diasIntentos = -1;
				dmCmp.setTendencia(getTendenciaGoogle(cmp.getId(), -1));
				dmCmp.setMomentumFactor(
						this.transformMomentumFactorToInteger(this.getMomentumFactor(cmp.getId(), false)));

				// Actualiza el campo: dmc_stock_price_close del registro anterior con el
				// actual.
				DataMiningCompany dmCmpAnterior = new DataMiningCompany();
				dmCmpAnterior.setCompany(cmp);
				dmCmpAnterior = admEnt.getPenultimateCompanyByCmp(dmCmp);
				if (dmCmpAnterior != null) {
					dmCmpAnterior.setStockPriceClose(dmCmp.getStockPrice());
				}

				// Aceleracion: delta Precio / delta Tiempo.
				// Almacena el valor de la aceleraci_n
				dmCmp.setAcceleration(String.valueOf(this.getAcceleration(dmCmpAnterior, dmCmp)));
				/*
				 * 0 BuyPoint 1 SellPoint 2 HBOP 3 LBOP 4 REACTION MODE (1) | TREND MODE (0)
				 */

				ReactionTrendSystem rts = UtilGeneral.isReactionMode(lastHigh, lastLow, lastClose,
						Double.parseDouble(dmCmp.getStockPrice().replace(",", ".")), true);
				dmCmp.setBuyPoint(rts.getActualPriceBetweenBuy());
				dmCmp.setSellPoint(rts.getActualPriceBetweenSell());
				dmCmp.setHbop(rts.getActualPriceUpHBOP());
				dmCmp.setLbop(rts.getActualPriceDownLBOP());
				dmCmp.setReactionMode(rts.getActualPriceBetweenHBOPLBOP());
				
				//Registra el indicador de Awesome indicator para ser almacenado en "dmc_data_mining_company"
				Double awesomeOscillator = this.getAwesomeOscillator(companySymbol);
				if (awesomeOscillator != null) {
					dmCmp.setAwesomeOscillator(String.valueOf(awesomeOscillator));
				}
				
				

				if (dmCmpAnterior != null) {
					admEnt.updateDataMiningCompany(dmCmpAnterior);
				}
				admEnt.updateDataMiningCompany(dmCmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			_logger.info("Error al persistir el DataMining" + e.getMessage());

		} finally {
			variableGlobalIntentos = 0;
		}

	}

	/**
	 * @param companySymbol
	 * @param nDays
	 *            Obtener la tendencia segun 4 estados /* (0) - alza (1) - baja (2)
	 *            Alza (3) Baja enum TENDENCIA {minusalza, minusbaja, ALZA, BAJA;}
	 */
	private Integer getTendenciaGoogle(Long idCompany, int nDays) {

		switch (this.getTendenciaGoogle(idCompany)) {
		case minusalza:
			return 0; // "-alza";
		case minusbaja:
			return 1; // "-baja";
		case ALZA:
			return 2; // "ALZA";
		case BAJA:
			return 3; // "BAJA";
		case NO_EVALUADA:
			_logger.info("Se llama de forma recursiva a getTendencia " + (diasIntentos--));
			this.variableGlobalIntentos++;
			if (variableGlobalIntentos > 3) {
				return null;
			}
			return getTendenciaGoogle(idCompany, nDays + (diasIntentos));
		default:
			break;

		}

		return null;

	}

	/**
	 * @param companySymbols
	 * @param diasAtras
	 *            Obtener el RSI de varias companias dado una fecha hacia atras
	 */
	private void relativeStrengthIndexArray(String companySymbols, String idIteracion) {

		String cmpSymbol[] = companySymbols.split(";");

		for (String string : cmpSymbol) {
			this.relativeStrengthIndexFromGoogle(string.trim(), false, idIteracion);
		}

	}

	/**
	 * Imprime el BOS por cada compania, indicando si el dia de ejecución esta en
	 * Buy, O, Sell La idea es que el último registro este en Sell, que indica que
	 * el día de hoy se aplican las regas de Buy
	 * 
	 * @param companySymbols
	 * @param idIteracion
	 */
	private void printBOSForEachCompany(String companySymbols) {

		String cmpSymbol[] = companySymbols.split(";");

		for (String companySymbol : cmpSymbol) {
			// Convertir company Symbol en id de compañua
			Company cmp = null;
			cmp = new Company();
			cmp.setGoogleSymbol(companySymbol);

			try {
				cmp = this.admEnt.getCompanyBySymbol(cmp);
				_logger.info(companySymbol + ":[" + cmp.getId() + "] ---------- " + new Date());
				printBOS(Integer.valueOf(cmp.getId().intValue()), false);

			} catch (Exception e1) {
				_logger.error("Error al traer info de la compania", e1);
				return;
			}

		}

	}
	
	
	/*
	 * The Awesome oscillator(AO) is an indicator used to measured market momentum. AO calculates the difference between a 34 period &
	 * 5 period Simple Moving Average. The SMA that are used are not calculated using closing price but rqather each bars midpoints. AO
	 * is generally used to affirm trends or to anticipate possible reversals.
	 		iii.	Awsome Oscillator indicator (AO) Bill Williams. New Trading Dimensions
					lengthAO1=input(5, minval=1) //5 periods
					lengthAO2=input(34, minval=1) //34 periods
					AO = sma((high+low)/2, lengthAO1) - sma((high+low)/2, lengthAO2)
					
					**sma: Simple Movile Average
					
					https://www.tradingview.com/wiki/Awesome_Oscillator_(AO)
	 * 
	 */
	private double getAwesomeOscillator(String companySymbols) {
		String cmpSymbol[] = companySymbols.split(";");
		Double awesomeOscillator = 0d;

		for (String companySymbol : cmpSymbol) {
			// Convertir company Symbol en id de compañua
			Company cmp = null;
			cmp = new Company();
			cmp.setGoogleSymbol(companySymbol);

			try {
				cmp = this.admEnt.getCompanyBySymbol(cmp);
				_logger.info(companySymbol + ":[" + cmp.getId() + "] ---------- " + new Date());
				//calculate Awesome Oscillator and persist in "dmc_data_mining_company"
				List<Object> listIdxCompany = admEnt.getCompIdxAOQuote(cmp);
				
				Double SMA05Period = 0d;
				Double SMA34Period = 0d;
				
				
				
				for (Object object : listIdxCompany) {
					QuoteHistoryCompany data = (QuoteHistoryCompany) object;
					SMA34Period += data.getMedia();
				}
				
				for (int i = 0; i < 5; i++) {
					QuoteHistoryCompany data = (QuoteHistoryCompany) listIdxCompany.get(i);
					SMA05Period += data.getMedia();
				}
				SMA34Period = SMA34Period / 34;
				SMA05Period = SMA05Period / 5;
				
				awesomeOscillator = SMA05Period - SMA34Period;
				
				_logger.info("awesomeOscillator:" + awesomeOscillator);
				
				
				
				

			} catch (Exception e1) {
				_logger.error("Error al traer info de la compania para obtener el indicador: Awesome OScillator", e1);
			}

		}
		
		return awesomeOscillator;
		
		
	}

	/**
	 * @param symbol
	 * @param dateEnd
	 * @param dateBegin
	 * @param print
	 * @return Obtiene la tendencia de la compania
	 */
	private TENDENCIA getTendenciaGoogle(Long idCompany) {

		Double valorHoy = null;
		Double valorTresMesesAtras = null;

		try {
			HistoricalDataCompany hdc = null;
			hdc = new HistoricalDataCompany();
			hdc.setCompany(idCompany);
			hdc = this.admEnt.getFirstHistoricalDataCompanyByCompany(hdc);
			valorHoy = Double.parseDouble(hdc.getStockPriceClose());
		} catch (Exception e) {
			_logger.error("Error getTendenciaGoogle.valorHoy (" + idCompany + ")");
		}

		try {
			HistoricalDataCompany hdc = null;
			hdc = new HistoricalDataCompany();
			hdc.setCompany(idCompany);
			hdc = this.admEnt.getLastHistoricalDataCompanyByCompany(hdc);
			valorTresMesesAtras = Double.parseDouble(hdc.getStockPriceClose());
		} catch (Exception e) {
			_logger.error("Error valorTresMesesAtras.valorBeginYear (" + idCompany + ")");
		}

		if (null == valorHoy | null == valorTresMesesAtras) {
			return TENDENCIA.NO_EVALUADA;
		}

		if (valorHoy > valorTresMesesAtras) {

			double res = valorHoy - valorTresMesesAtras;
			if (res < 1.2d) {
				return TENDENCIA.minusalza;
			}
			return TENDENCIA.ALZA;
		} else {
			double res = valorTresMesesAtras - valorHoy;
			if (res < 1.2d) {
				return TENDENCIA.minusbaja;
			}
			return TENDENCIA.BAJA;
		}

	}


	// TODO: Filtrar YTD positivo:
	// * CAGR=(endingvalue/beginingvalue)elevado[^](1/#deyears) - 1
	// * REF: http://www.investopedia.com/terms/a/annual-return.asp
	// Compara hoy y hace un año
	// private Double getAnnualReaturn(String companySymbol){

	// }

	/*
	 * Ecuacion de regla de tres Compara hoy con respecto al primero de enero.
	 * 
	 * FIXME: ##Si estoy a principio de anio (Enero 01 a 30 de abril) comparar con
	 * Junio!
	 */
	private Double getYearToDateReturn(Long companySymbol) throws IllegalStateException, Exception {

		Double returnPorcentajeYTD = null;

		Double valorActual = 0d;
		Double valorBeginYear = 0d;

		try {
			HistoricalDataCompany hdc = null;
			hdc = new HistoricalDataCompany();
			hdc.setCompany(companySymbol);
			hdc = this.admEnt.getFirstHistoricalDataCompanyByCompany(hdc);
			valorActual = Double.parseDouble(hdc.getStockPriceClose());
		} catch (Exception e) {
			_logger.error("Error getYearToDateReturn.valorActual (" + companySymbol + ")");
			return 0d;
			// _logger.error("Error getYearToDateReturn.valorActual", e);
		}

		try {
			HistoricalDataCompany hdc = null;
			hdc = new HistoricalDataCompany();
			hdc.setCompany(companySymbol);
			hdc = this.admEnt.getLastHistoricalDataCompanyByCompany(hdc);
			valorBeginYear = Double.parseDouble(hdc.getStockPriceClose());
		} catch (Exception e) {
			_logger.error("Error getYearToDateReturn.valorBeginYear (" + companySymbol + ")");
			return 0d;
			// _logger.error("Error getYearToDateReturn.valorBeginYear", e);
		}

		// Calculo de regla de tres para consultar el porcentaje de YTD
		returnPorcentajeYTD = ((100 * valorActual) / valorBeginYear) - 100;

		return returnPorcentajeYTD;

	}

	/*
	 * Algoritmo que: 1. Consula en la BD por Numero de Iteracion 2. Da un peso a
	 * cada variable del data mining 3. Genera un respuesta idicando si el peso de
	 * ganancia es mayor al de perdida
	 * 
	 * REF: Book: Data Mining, Practical machine learning, tools and techniques
	 * Chapter: 4.2
	 *
	 * FIXME: Ajustar los pesos con la variable YTD de la plataforma.
	 */
	/*
	 * Tener en cuenta esta consulta>
	 * 
	 * SELECT CASE WHEN d.DMC_TENDENCIA = 0 THEN '(0) - alza' WHEN d.DMC_TENDENCIA =
	 * 1 THEN '(1) - baja' WHEN d.DMC_TENDENCIA = 2 THEN '(2) Alza' WHEN
	 * d.DMC_TENDENCIA = 3 THEN '(3) Baja' END as tendencia, c.id, c.name,
	 * c.urlIndex, d.DMC_STOCK_PRICE, d.DMC_FECHA_CREACION,
	 * d.DMC_RELATIVE_STRENGTH_INDEX FROM indexyahoocfd.company c inner join
	 * indexyahoocfd.dmc_data_mining_company d on d.SCN_CODIGO = c.id --WHERE name
	 * like '%Vonovia SE%' WHERE d.DMC_CODIGO_GRP_ITERACION = 1611040744 ORDER by
	 * d.DMC_FECHA_CREACION desc
	 * 
	 * SIEMPRE: 1. VERIFICAR QUE EL rsi ESTE POR DEBAJO DE 60 2.
	 * https://online.capitalcube.com/#!/stock/gb/london/pdl, verificar CapitalCube4
	 * 3. Sector: seguros y finanzas muy riesgoso
	 * 
	 */
	// --> Volver a evaluar este algoritmo. Repasar libro:
	// C:\francisco\readme\BI\Data Mining - Practical Machine Learning Tools and
	// Techniques.pdf
	private void getStatisticalModeling(Long numeroIteracion) throws Exception {

		_logger.info("Evalua numero de iteracion: " + numeroIteracion);

		try {

			DataMiningCompany dmCmp = new DataMiningCompany();
			dmCmp.setIdIteracion(numeroIteracion);

			List<DataMiningCompany> lstDM = admEnt.getDMCompanyByIteracion(dmCmp);

			for (DataMiningCompany dataMiningCompany : lstDM) {

				try {
					Double probabilidadWinTotal = null;
					Double probabilidadLostTotal = null;
					// _logger.info(dataMiningCompany.toString());
					PriceBetweenHighLow probabilidadVariable01 = new PriceBetweenHighLow();
					double probabilidadWin01 = probabilidadVariable01
							.getWinStatistics(dataMiningCompany.getIsPriceBetweenHighLow());
					double probabilidadLost01 = probabilidadVariable01
							.getLostStatistics(dataMiningCompany.getIsPriceBetweenHighLow());
					// System.out.print("W1" + probabilidadWin01);
					// System.out.print("L1" + probabilidadLost01);

					TendenciaTresMeses probabilidadVariable02 = new TendenciaTresMeses();
					double probabilidadWin02 = probabilidadVariable02
							.getWinStatistics(dataMiningCompany.getTendencia());
					double probabilidadLost02 = probabilidadVariable02
							.getLostStatistics(dataMiningCompany.getTendencia());
					// System.out.print("W2" + probabilidadWin02);
					// System.out.print("L2" + probabilidadLost02);

					PricePercentageIncrement probabilidadVariable03 = new PricePercentageIncrement();
					double probabilidadWin03 = probabilidadVariable03.getWinStatistics(
							Double.parseDouble(dataMiningCompany.getPercentageIncrement().replace(',', '.').trim()));
					double probabilidadLost03 = probabilidadVariable03.getLostStatistics(
							Double.parseDouble(dataMiningCompany.getPercentageIncrement().replace(',', '.').trim()));
					// System.out.print("W3" + probabilidadWin03);
					// System.out.print("L3" + probabilidadLost03);

					NotaPonderada probabilidadVariable04 = new NotaPonderada();
					double probabilidadWin04 = probabilidadVariable04.getWinStatistics(
							Double.parseDouble(dataMiningCompany.getNotaPonderada().replace(',', '.').trim()));
					double probabilidadLost04 = probabilidadVariable04.getLostStatistics(
							Double.parseDouble(dataMiningCompany.getNotaPonderada().replace(',', '.').trim()));
					// System.out.print("W4" + probabilidadWin04);
					// System.out.print("L4" + probabilidadLost04);

					PriceEarningRatio probabilidadVariable05 = new PriceEarningRatio();
					double probabilidadWin05 = probabilidadVariable05.getWinStatistics(
							Double.parseDouble(dataMiningCompany.getPriceEarningRatio().replace(',', '.').trim()));
					double probabilidadLost05 = probabilidadVariable05.getLostStatistics(
							Double.parseDouble(dataMiningCompany.getPriceEarningRatio().replace(',', '.').trim()));
					// System.out.print("W5" + probabilidadWin05);
					// System.out.print("L5" + probabilidadLost05);

					RelativeStrengthIndex probabilidadVariable06 = new RelativeStrengthIndex();
					double probabilidadWin06 = probabilidadVariable06.getWinStatistics(
							Double.parseDouble(dataMiningCompany.getRelativeStrengthIndex().replace(',', '.').trim()));
					double probabilidadLost06 = probabilidadVariable06.getLostStatistics(
							Double.parseDouble(dataMiningCompany.getRelativeStrengthIndex().replace(',', '.').trim()));
					// System.out.print("W6" + probabilidadWin06);
					// System.out.print("L6" + probabilidadLost06);

					PrecioAccion probabilidadVariable07 = new PrecioAccion();
					double probabilidadWin07 = probabilidadVariable07.getWinStatistics(
							Double.parseDouble(dataMiningCompany.getStockPrice().replace(',', '.').trim()));
					double probabilidadLost07 = probabilidadVariable07.getLostStatistics(
							Double.parseDouble(dataMiningCompany.getStockPrice().replace(',', '.').trim()));
					// System.out.print("W7" + probabilidadWin07);
					// System.out.print("L7" + probabilidadLost07);

					LastDigit probabilidadVariable08 = new LastDigit();
					double probabilidadWin08 = probabilidadVariable08
							.getWinStatistics(dataMiningCompany.getLastDigitStockPrice());
					double probabilidadLost08 = probabilidadVariable08
							.getLostStatistics(dataMiningCompany.getLastDigitStockPrice());
					// System.out.print("W8" + probabilidadWin08);
					// System.out.print("L8" + probabilidadLost08);

					DiffMaxMin probabilidadVariable09 = new DiffMaxMin();
					double probabilidadWin09 = probabilidadVariable09.getWinStatistics(
							Double.parseDouble(dataMiningCompany.getDiffMaxMin().replace(',', '.').trim()));
					double probabilidadLost09 = probabilidadVariable09.getLostStatistics(
							Double.parseDouble(dataMiningCompany.getDiffMaxMin().replace(',', '.').trim()));
					// System.out.print("W9" + probabilidadWin09);
					// System.out.print("L9" + probabilidadLost09);

					PercentageIncremento probabilidadVariable10 = new PercentageIncremento();
					double probabilidadWin10 = probabilidadVariable10.getWinStatistics(
							Double.parseDouble(dataMiningCompany.getPercentageIncrement().replace(',', '.').trim()));
					double probabilidadLost10 = probabilidadVariable10.getLostStatistics(
							Double.parseDouble(dataMiningCompany.getPercentageIncrement().replace(',', '.').trim()));
					// System.out.print("W10" + probabilidadWin10);
					// System.out.print("L10" + probabilidadLost10);

					StockMayorMedia probabilidadVariable11 = new StockMayorMedia();
					double probabilidadWin11 = probabilidadVariable11
							.getWinStatistics(dataMiningCompany.getIsTockPriceMayorMedia());
					double probabilidadLost11 = probabilidadVariable11
							.getLostStatistics(dataMiningCompany.getIsTockPriceMayorMedia());
					// System.out.print("W11" + probabilidadWin11);
					// System.out.print("L11" + probabilidadLost11);

					// Indicador/peso de la ganancia o perdida de la accion al final del dia
					double probabilidadWin12 = IStatistics.ganoVariacionPrecio;
					double probabilidadLost12 = IStatistics.perdioVariacionPrecio;
					// System.out.print("W12" + probabilidadWin12);
					// System.out.print("L12" + probabilidadLost12);

					probabilidadWinTotal = probabilidadWin01 * probabilidadWin02 * probabilidadWin03 * probabilidadWin04
							* probabilidadWin05 * probabilidadWin06 * probabilidadWin07 * probabilidadWin08
							* probabilidadWin09 * probabilidadWin10 * probabilidadWin11 * probabilidadWin12;
					probabilidadLostTotal = probabilidadLost01 * probabilidadLost02 * probabilidadLost03
							* probabilidadLost04 * probabilidadLost05 * probabilidadLost06 * probabilidadLost07
							* probabilidadLost08 * probabilidadLost09 * probabilidadLost10 * probabilidadLost11
							* probabilidadLost12;

					_logger.info("*******************(ini) [" + dataMiningCompany.getCompany().getName()
							+ "]*************************");
					/*
					 * Si no almacena la informacion en el Datamining para el stage de Machine
					 * learning, dejar valores en cero(0)
					 */
					if (probabilidadWinTotal > probabilidadLostTotal && Double
							.parseDouble(dataMiningCompany.getRelativeStrengthIndex().replace(',', '.').trim()) < 60) {
						// _logger.info("Tiene probabilidad de ganancia al final del dia ");
						System.out.println("[" + dataMiningCompany.getCompany().getId() + "-"
								+ dataMiningCompany.getCompany().getName() + "]probabilidadWin - Lost *10000-->"
								+ ((probabilidadWinTotal - probabilidadLostTotal) * 10000));
						// _logger.info(dataMiningCompany.toString());
						// Almacena info en Datamining probabilidadWin
						dataMiningCompany.setProbabilidadWin(
								Double.toString(((probabilidadWinTotal - probabilidadLostTotal) * 10000)));
						admEnt.updateDataMiningCompany(dataMiningCompany);

					} else {
						System.out.println("[" + dataMiningCompany.getCompany().getId() + "-"
								+ dataMiningCompany.getCompany().getName() + "]probabilidadLost - Lost *10000-->"
								+ ((probabilidadWinTotal - probabilidadLostTotal) * 10000));
						// Almacena info en Datamining probabilidadWin
						dataMiningCompany.setProbabilidadLost(
								Double.toString(((probabilidadWinTotal - probabilidadLostTotal) * 10000)));
						admEnt.updateDataMiningCompany(dataMiningCompany);
					}

					Double ytd = Double.parseDouble(dataMiningCompany.getYTDPlataforma().replace(',', '.').trim());

					if (Double.parseDouble(dataMiningCompany.getDiffMaxMin().replace(',', '.').trim()) > 49
							| Double.parseDouble(
									dataMiningCompany.getPercentageIncrement().replace(',', '.').trim()) >= 3
							| (ytd > 0 && ytd < 20)) {
						_logger.info(
								"Diferencia mayor a 49 DataMining* OR %IncMayorIgual3 DataMining OR YTD between 0 - 20");
						System.out.println("dataMiningCompany.getDiffMaxMin()" + dataMiningCompany.getDiffMaxMin());
						System.out.println("dataMiningCompany.getPercentageIncrement()"
								+ dataMiningCompany.getPercentageIncrement());
						System.out
								.println("dataMiningCompany.getYTDPlataforma()" + dataMiningCompany.getYTDPlataforma());
						_logger.info(dataMiningCompany.toString());
						// Almacenar info en Datamining bandera 1
						dataMiningCompany.setBanderaIncremento(true);
						admEnt.updateDataMiningCompany(dataMiningCompany);
					} else {
						// Almacenar info en Datamining bandera 0
						dataMiningCompany.setBanderaIncremento(false);
						admEnt.updateDataMiningCompany(dataMiningCompany);
					}
					_logger.info("*******************(fin) [" + dataMiningCompany.getCompany().getName()
							+ "]*************************");

				} catch (NumberFormatException e) {
					_logger.info("NumberFormatException");
				} catch (NullPointerException e) {
					_logger.info("NullPointerException");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param omi
	 * @param argumento2
	 * @param cortePorcentajePonderado
	 * @throws BusinessException
	 * @throws IOException
	 */
	private static void runManytimes(ObtenerMarketIndex omi, Integer argumento2, Integer cortePorcentajePonderado)
			throws BusinessException, IOException {

		omi.persistirHistoricoToRSI();
		
		for (;;) {
			omi.freeAdminEntity();
			cr = null;
			_logger.info("Persiste info de las companias, consultando de yahoo  [Go long]");
			omi.printPERatio();
			omi.freeAdminEntity();
			_logger.info("Precio accion menor = & % volumen mayor a cero!" + " Time:" + new Date());
			omi.printOBV(argumento2, cortePorcentajePonderado, Evalua.ONE);
			omi.freeAdminEntity();
			_logger.info("Precio accion mayor & % volumen mayor a cero!" + "Time:" + new Date());
			omi.printOBV(argumento2, cortePorcentajePonderado, Evalua.THREE);
			_logger.info("FIN, esperar 3 minutos.." + new Date());
			try {
				// Cuando este en idle deberia liberar los objetos
				System.gc();
				Thread.sleep(180000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // 10 minutos
		}

	}
	
	
	/**
	 * Metodo encargado de persistir en la base de datos la informacion historica de
	 * DATE,CLOSE,HIGH,LOW,OPEN,VOLUME, para luego calcular el RSI por compa�ia.
	 * 
	 * @throws IOException
	 */
	private void persistirHistoricoToRSI() throws IOException {

		_logger.info(
				"Persiste la informacion de: https://finance.services.appex.bing.com/Market.svc/ChartDataV5?symbols=200.1.LUK.FRA&chartType=1y&isEOD=False&lang=en-US&isCS=true&isVol=true&prime=true");

		try {

			// Elimina la informacion historia de la tabla para tener el ultimo stock
			admEnt.deleteDataHistorica();

			if (cmpGlobal == null) {
				cmpGlobal = admEnt.getCompanies();
			}
		} catch (Exception e) {
			_logger.error("Error al borrar info historica", e);
		}

		for (Company cmp : cmpGlobal) {
			try {
				if (cmp.getUrlQuote() != null && cmp.getUrlQuote().indexOf("fi-") > 0) {
					
					String qx = cmp.getUrlQuote().substring(cmp.getUrlQuote().indexOf("fi-") + 3,
							cmp.getUrlQuote().length());
					 
					 
					 String call = "https://finance.services.appex.bing.com/Market.svc/ChartDataV5?symbols="
							 + qx.replace("%7CSLA%7C", "%2F")
							 + "&chartType=1y&isEOD=False&lang=en-US&isCS=true&isVol=true&prime=true";

					 //_logger.info("call: " + call);

					List<HistoricalDataCompany> lstHistoricalDataCompany = null;
					lstHistoricalDataCompany = new ArrayList<HistoricalDataCompany>();

					
					JsonElement result = executeJ(call);
					if (result.isJsonObject()) {
						JsonElement error = result.getAsJsonObject().get("error");
						if (error != null) {
							JsonElement code = result.getAsJsonObject().get("code");
							System.out.println("[Error] code:" + code);
						}
					}

					StockMarketData[] quote = gson.fromJson(result, StockMarketData[].class);
					if (quote == null || quote.length <= 0) {
						continue;
					}
					StockMarketData d = quote[0];

					Calendar dayBase = Calendar.getInstance();
					dayBase.setTimeInMillis(Long.parseLong(d.getUtcFullRunTime().substring(6, 19)));

					Calendar firstDay = (Calendar) dayBase.clone();

					int size = d.getSeries().size() - 1;
					int count = 0;
					for (int z = size; z >= 0; z--) {
						
						if (count++ == 50) {
							break;
						}
						
						SeriesObj s = d.getSeries().get(z);

						if (z == size) {
							firstDay.add(Calendar.DAY_OF_YEAR, (((s.getT() / 1440) + 1) * -1));
						}

						Calendar dayEval = (Calendar) firstDay.clone();
						dayEval.add(Calendar.DAY_OF_YEAR, (s.getT() / 1440));

						if (z == 0) {
							dayEval = (Calendar) firstDay.clone();
						}

						//_logger.info(s.getT() + " --> " + new Date(dayEval.getTimeInMillis()));

						HistoricalDataCompany hdc = new HistoricalDataCompany();
						hdc.setCompany(cmp.getId());
						hdc.setFechaCreacion(Calendar.getInstance());
						hdc.setFechaDataHistorica(dayEval);
						hdc.setStockPriceClose(Double.toString(s.getP()));
						hdc.setStockPriceHigh(Double.toString(s.getHp()));
						hdc.setStockPriceLow(Double.toString(s.getLp()));
						hdc.setStockPriceOpen(Double.toString(s.getOp()));
						hdc.setStockVolume(Double.toString(s.getV()));

						lstHistoricalDataCompany.add(hdc);

					}
					admEnt.persistirDataHistoricaByCompany(lstHistoricalDataCompany);
				}
			} catch (Exception e) {
				_logger.error("Error al persistir la informacion de " + cmp.getName(), e);
			}

		}

		// Periste la informacion de la lista

	}



	private void printMomentumFactor() {
		try {
			if (cmpGlobal == null) {
				cmpGlobal = admEnt.getCompanies();
			}
			for (Company cmp : cmpGlobal) {
				// _logger.info(":" + cmp.getId());
				getMomentumFactor(cmp.getId(), true);
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Print the momentu factor for each company
	 */
	private MOMENTUM_FACTOR getMomentumFactor(Long cmpId, boolean persistirMomentumFactorByCompany) {

		List<HistoricalDataCompany> listTopFiveHdc = null;
		Double[] aClosePrice = new Double[5];
		MOMENTUM_FACTOR saveDataMining = null;

		try {

			HistoricalDataCompany hdc = null;
			hdc = new HistoricalDataCompany();
			hdc.setCompany(cmpId);
			// Take de 5 last info from hst_historical_data_company_to_rsi
			listTopFiveHdc = this.admEnt.getTopFiveToMomentumFactor(hdc);

			int count = 0;
			for (HistoricalDataCompany h : listTopFiveHdc) {
				aClosePrice[count++] = Double.parseDouble(h.getStockPriceClose());
			}
			/*
			 * for (Double double1 : aClosePrice) { _logger.info(" " + double1); }
			 */

			/*
			 * _logger.info("MF1:" + (aClosePrice[4]-aClosePrice[2])*-1 );
			 * _logger.info("MF2:" + (aClosePrice[3]-aClosePrice[1])*-1 );
			 * _logger.info("MF3:" + (aClosePrice[2]-aClosePrice[0])*-1 );
			 */

			Double MF1 = (aClosePrice[4] - aClosePrice[2]) * -1;
			Double MF2 = (aClosePrice[3] - aClosePrice[1]) * -1;
			Double MFToday = (aClosePrice[2] - aClosePrice[0]) * -1;

			/*
			 * Long --> When MF today is [higher] number for [either] of the previous two(2)
			 * days. 
			 * Short --> When MF today is [lower] number for [both] of the previous
			 * two(2) days.
			 */

			if (MF1 != 0 && MF2 != 0 && MFToday != 0) {
				if (MF1 > 0 && MF2 > 0 && MFToday > 0) {
					if (MFToday < MF1 && MFToday < MF2) {
						// Almacena momentum factor SHORT
						saveDataMining = MOMENTUM_FACTOR.SHORT;
						_logger.info("Posible Short [" + cmpId + "] MF1[" + MF1 + "] MF2[" + MF2 + "] MFToday["
								+ MFToday + "]");
					} else {
						saveDataMining = MOMENTUM_FACTOR.MF1;
					}
				} else if (MF1 < 0 && MF2 < 0 && MFToday < 0) {
					if (MFToday > MF1 || MFToday > MF2) {
						// Almacena momentum factor LONG
						saveDataMining = MOMENTUM_FACTOR.LONG;
						_logger.info("Posible Long [" + cmpId + "] MF1[" + MF1 + "] MF2[" + MF2 + "] MFToday["
								+ MFToday + "]");
					} else {
						saveDataMining = MOMENTUM_FACTOR.MF2;
					}

				} else {
					saveDataMining = MOMENTUM_FACTOR.MF3;
				}
			} else {
				saveDataMining = MOMENTUM_FACTOR.CEROS;
			}

			// Persiste en Data mining el valor de SaveDataMining.
			if (persistirMomentumFactorByCompany) {
				this.persistirMomentumFactor(saveDataMining, cmpId);
			}

		} catch (Exception e) {
			_logger.error("Error al leer el top 5 para obtener el Momentum factor y persistirlo :" + e.getMessage());
		}

		return saveDataMining;

	}

	/**
	 * Metodo encargado de persistir en BD la variable Momentum factor
	 * 
	 * @param mf
	 * @param cmpId
	 * @throws Exception
	 */
	private void persistirMomentumFactor(MOMENTUM_FACTOR mf, Long cmpId) throws Exception {

		admEnt.updateMomentumFactorByCompany(cmpId, this.transformMomentumFactorToInteger(mf));

	}

	/**
	 * Transforma el momentum factor en un entero para poderlo persistir en la BD.
	 * 
	 * @param mf
	 * @return
	 */
	private Integer transformMomentumFactorToInteger(MOMENTUM_FACTOR mf) {
		Integer retornoMF = null;

		switch (mf) {
		case CEROS:
			retornoMF = 0;
			break;
		case MF1:
			retornoMF = 1;
			break;
		case MF2:
			retornoMF = 2;
			break;
		case MF3:
			retornoMF = 3;
			break;
		case SHORT:
			retornoMF = 4;
			break;
		case LONG:
			retornoMF = 5;
			break;
		default:
			break;

		}

		return retornoMF;
	}

	/**
	 * Obtener la aceleracion de la compania: delta Precio / delta Tiempo
	 * 
	 * @param dmCmpAnterior
	 * @param dmCmpNow
	 * @return
	 */
	private Double getAcceleration(DataMiningCompany dmCmpAnterior, DataMiningCompany dmCmpNow) {
		Double aceleracion = 0d;

		try {
			double precioAnterior = 0, precioNow = 0, deltaPrecio = 0;
			long tiempoAnterior = 0, tiempoNow = 0, deltaTiempo = 0;

			try {
				precioAnterior = Double.parseDouble(dmCmpAnterior.getStockPrice().replace(",", "."));
				precioNow = Double.parseDouble(dmCmpNow.getStockPrice().replace(",", "."));
				tiempoAnterior = dmCmpAnterior.getIdIteracion();
				tiempoNow = dmCmpNow.getIdIteracion();
			} catch (Exception e) {
				return aceleracion;
			}

			/*
			 * _logger.info("precioAnterior: " + precioAnterior); _logger.info("precioNow: "
			 * + precioNow); _logger.info("tiempoAnterior: " + tiempoAnterior);
			 * _logger.info("tiempoNow: " + tiempoNow);
			 */

			deltaPrecio = (precioNow - precioAnterior);
			deltaTiempo = (tiempoNow - tiempoAnterior);

			aceleracion = deltaPrecio / deltaTiempo;
		} catch (Exception e) {
			aceleracion = 0d;
		}

		return aceleracion;

	}

	/**
	 * @param idCompany
	 *            Imprime la relacion Buy, No position & Sell
	 */
	private void printBOS(Integer scnCodigo, boolean print) {
		try {

			Calendar iniTime = Calendar.getInstance();
			iniTime.add(Calendar.DAY_OF_YEAR, -21);
			iniTime.set(Calendar.MILLISECOND, 0);
			iniTime.set(Calendar.SECOND, 0);
			iniTime.set(Calendar.MINUTE, 0);
			iniTime.set(Calendar.HOUR_OF_DAY, 0);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			// _logger.info("Date: " + simpleDateFormat.format(new
			// Date(iniTime.getTimeInMillis())));

			HistoricalDataCompany hdc = new HistoricalDataCompany();
			hdc.setCompany(scnCodigo.longValue());
			hdc.setFechaDataHistorica(iniTime);

			List<HistoricalDataCompany> lstHdc = admEnt.getHistoricalDataCompanyByCompanyDateBegin(hdc);
			Collections.sort(lstHdc);

			HistoricalDataCompany[] y = lstHdc.toArray(new HistoricalDataCompany[0]);

			int idx = UtilGeneral.getLowest(y);
			boolean banderaisHigh = false;
			if (idx > 7) {
				banderaisHigh = true;
				idx = UtilGeneral.getHighest(y);
			}
			if (print) {
				_logger.info("high[" + banderaisHigh + "]" + idx + "-->" + y[idx]);
			}

			String bos[] = { "B", "0", "S", "B", "0", "S", "B", "0", "S", "B", "0", "S", "B", "0", "S", "B", "0", "S",
					"B", "0", "S", "B", "0", "S", "B", "0", "S", "B", "0", "S", "B", "0", "S", "B", "0", "S", "B", "0",
					"S", "B", "0", "S", "B", "0", "S", "B", "0", "S", "B", "0", "S", };
			int idxB = 0;
			if (banderaisHigh) {
				idxB = 2;
			}
			HistoricalDataCompany historicalDataCompanyBefore = null;
			for (int i = idx; i < y.length; i++) {
				HistoricalDataCompany historicalDataCompanyNow = y[i];
				ReactionTrendSystem rtsNow = null;
				ReactionTrendSystem rtsB = null;
				if (i > 0) {
					historicalDataCompanyBefore = y[i - 1];
					double h, l, c, a;
					h = Double.parseDouble(historicalDataCompanyBefore.getStockPriceHigh());
					l = Double.parseDouble(historicalDataCompanyBefore.getStockPriceLow());
					c = Double.parseDouble(historicalDataCompanyBefore.getStockPriceClose());
					a = (h + l + c) / 3;
					rtsB = UtilGeneral.isReactionMode(h, l, c, a, false);
				}

				/*
				 * Valida si el precio anterior esta entre HBOP y LBOP, si no iniciar el bos con
				 * B o S, segun sea el caso.
				 */
				if (historicalDataCompanyBefore != null && rtsB != null && i > idx) {
					double h, l, c, a;
					h = Double.parseDouble(historicalDataCompanyNow.getStockPriceHigh());
					l = Double.parseDouble(historicalDataCompanyNow.getStockPriceLow());
					c = Double.parseDouble(historicalDataCompanyNow.getStockPriceClose());
					a = (h + l + c) / 3;
					rtsNow = UtilGeneral.isReactionMode(h, l, c, a, false);
					if (rtsNow.getxPrima() > rtsB.getHbop()) {
						idxB = 2;
						if (print) {
							_logger.info("[" + i + "](CorrigeSell) StockPriceHigh Before: "
									+ historicalDataCompanyBefore.getStockPriceHigh() + bos[idxB]);
						}

					} else if (rtsNow.getxPrima() < rtsB.getLbop()) {
						idxB = 0;
						if (print) {
							_logger.info("[" + i + "](CorrigeBuy) StockPriceLow Before"
									+ historicalDataCompanyBefore.getStockPriceLow() + bos[idxB]);
						}

					}
				}
				// Importante incrementa el valor
				idxB++;

				if (print) {
					_logger.info("[" + i + "]" + historicalDataCompanyNow.getStockPriceLow() + ": " + bos[idxB]);
				}
				if (i == (y.length - 1) && bos[idxB].endsWith("S")) {
					_logger.info("[" + i + "]" + historicalDataCompanyNow.getStockPriceLow() + ": " + bos[idxB]
							+ "Sell, next buy");
				}

			}

			// for (HistoricalDataCompany historicalDataCompany : y) {
			// _logger.info("-->" + simpleDateFormat.format(new Date(
			// historicalDataCompany.getFechaDataHistorica().getTimeInMillis())));
			// }

		} catch (Exception e) {
			_logger.error("Error al imprimir 'BOS' sequence ", e);
		}

	}

	/**
	 * @param idCompany
	 *            Imprime la relacion Buy, No position & Sell
	 */
	private void printLastReactionModeByCompany(Long scnCodigo, Double actualPrice) {
		try {

			Map<Long, HistoricalDataCompany> hdc = admEnt.getAllLastHistoricalData();
			HistoricalDataCompany historicalDataCompany = hdc.get(scnCodigo);

			double h, l, c, a;
			h = Double.parseDouble(historicalDataCompany.getStockPriceHigh());
			l = Double.parseDouble(historicalDataCompany.getStockPriceLow());
			c = Double.parseDouble(historicalDataCompany.getStockPriceClose());
			a = actualPrice;
			ReactionTrendSystem rtsB = UtilGeneral.isReactionMode(h, l, c, a, true);

		} catch (Exception e) {
			_logger.error("Error al imprimir printLastReactionModeByCompany ", e);
		}

	}
	
	/**
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private JsonElement executeJ(String url) throws IOException, InterruptedException {
		return new JsonParser().parse(execute(url));
	    }
	
	
	/**
     * @param url
     * @param request
     * @return
     * @throws IOException
     */
	private String execute(String url) throws IOException, InterruptedException {
		String response = null;
		try {
			
			URL resultadoURL = new URL(url);
			URLConnection con = resultadoURL.openConnection();
			con.setConnectTimeout(WAIT_TIME);
			con.setReadTimeout(WAIT_TIME);
			BufferedReader in = null;
			
			//intento 1
			try{
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
			    //System.out.println("1st try to get indicator:" + e.getMessage());
			}
			
			//intento 2
			if (null == in){
			try{
			    Thread.sleep(500);
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
				//System.out.println("2nd try to get indicator:" + e.getMessage());
			}
			}
			//intento 3
			if (null == in){
			try{
			    Thread.sleep(700);
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
				//System.out.println("3th try to get indicator:" + e.getMessage());
			}
			}
			//intento 4
			if (null == in){
			try{
			    Thread.sleep(700);
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
				//System.out.println("4th try to get indicator:" + e.getMessage());
			}
			}
			
			//intento 5
			if (null == in){
			try{
			    Thread.sleep(900);
			    in = new BufferedReader(new InputStreamReader(resultadoURL.openStream()));
			}catch(IOException e){
				//System.out.println("5th try to get indicator:" + e.getMessage());
			}
			}

			StringBuilder yahooSM = new StringBuilder();
			String inputLine;
			if (null != in){
				while ((inputLine = in.readLine()) != null) {
				    yahooSM.append(inputLine);
				}
				in.close();	
			}
			
			//response = http.execute(get, new BasicResponseHandler());
			response = yahooSM.toString();
			// _logger.info("Response: " + response);
		} catch (IOException io) {
			System.out.println("url No responde:" + url);
			io.printStackTrace();
			throw io; 
		}catch (InterruptedException io) {
			System.out.println("url No responde:" + url);
			io.printStackTrace();
			throw io; 
		}
		return response;
	}
	

    /**
     * Creates a new {@link Gson} object.
     */
    private Gson createGson() {
	GsonBuilder builder = new GsonBuilder();
	return builder.create();
    }
    

}
