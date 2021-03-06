package com.wireless.soft.indices.cfd.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wireless.soft.indices.cfd.business.adm.AdminEntity;
import com.wireless.soft.indices.cfd.business.entities.Company;
import com.wireless.soft.indices.cfd.business.entities.DataMiningCompany;
import com.wireless.soft.indices.cfd.business.entities.HistoricalDataCompany;
import com.wireless.soft.indices.cfd.business.entities.QuoteHistoryCompany;
import com.wireless.soft.indices.cfd.collections.CompanyRanking;
import com.wireless.soft.indices.cfd.collections.ReactionTrendSystem;
import com.wireless.soft.indices.cfd.collections.RelativeStrengthIndexData;

/**
 * @author Francisco Clase encargada de calculos matematicos para validar la
 *         compra o no de las acciones e impresi_n de formatos de fecha, entre
 *         otros. General
 *
 */
public class UtilGeneral {

	// ////////////////////////////////////////////////////////////////////////
	// Logger de la clase
	// ////////////////////////////////////////////////////////////////////////
	private static Logger _logger = Logger.getLogger(UtilGeneral.class);

	/**
	 * @param precioBuy
	 * @param precioHigh
	 * @param PrecioLow
	 * @return Sret
	 */
	public static Boolean isPriceBetweenHighLow(String precioBuy, String precioHigh, String PrecioLow) {
		Boolean retorno = false;
		try {
			double pB = Double.valueOf(precioBuy);
			double ph = Double.valueOf(precioHigh);
			double pl = Double.valueOf(PrecioLow);
			// Valida que el precio se almenos igual al valor mas bajo
			// y que este dentro del valor -2 dolares al mas alto,
			// Recuerde la estrategia de comprar barato para vender alto
			// Buy when price reaches its low, sell when prices reaches high
			if (pB >= pl && pB < (ph - 2)) {
				retorno = true;
			} else {
				retorno = false;
			}

		} catch (Exception e) {
			retorno = false;
		}

		return retorno;
	}

	/**
	 * @param precioBuy
	 * @param precioHigh
	 * @param PrecioLow
	 * @return Sret
	 */
	public static Boolean isPriceBetweenHighLow(double precioBuy, double precioHigh, double PrecioLow) {
		Boolean retorno = false;
		try {
			double pB = Double.valueOf(precioBuy);
			double ph = Double.valueOf(precioHigh);
			double pl = Double.valueOf(PrecioLow);
			// Valida que el precio se almenos igual al valor mas bajo
			// y que este dentro del valor -2 dolares al mas alto,
			// Recuerde la estrategia de comprar barato para vender alto
			// Buy when price reaches its low, sell when prices reaches high
			if (pB >= pl && pB < (ph - 2)) {
				retorno = true;
			} else {
				retorno = false;
			}

		} catch (Exception e) {
			retorno = false;
		}

		return retorno;
	}

	/**
	 * @param calendar
	 * @param format
	 * @return Retorna el string de fecha dado un formato
	 */
	public static String printFormat(Calendar calendar, String format) {

		String retornoFF = null;

		if (null != calendar) {

			SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.ENGLISH); // "yyyy-MM-dd HH:mm:ss.SSS0"
			retornoFF = format1.format(calendar.getTime());
		} else {
			retornoFF = "Fecha Null";
		}

		return retornoFF;

	}

	public static String printNumberFormat(double valNum, String format) {
		String retNF = null;

		// customFormat("###,###.###", 123456.789);
		// customFormat("###.##", 123456.789);
		// customFormat("000000.000", 123.78);
		// customFormat("$###,###.###", 12345.67);
		DecimalFormat myFormatter = new DecimalFormat(format);
		retNF = myFormatter.format(valNum);

		return retNF;
	}

	/**
	 * Calcula la media
	 * 
	 * @param lstMediaSearch
	 */
	public static double imprimirMedia(List<Double> lstMediaSearch) {
		Double med[] = new Double[lstMediaSearch.size()];
		med = lstMediaSearch.toArray(med);
		Arrays.sort(med);
		int middle = med.length / 2;
		if (med.length % 2 == 0) {
			double left = med[middle - 1];
			double right = med[middle];
			_logger.info("middle [LR]:" + ((left + right) / 2));
			return ((left + right) / 2);
		} else {
			_logger.info("middle:" + (med[middle]));
			return (med[middle]);
		}
	}

	/**
	 * @return
	 */
	public static List<RelativeStrengthIndexData> getListaRSIGoogle() {
		List<RelativeStrengthIndexData> lstRSI = null;
		lstRSI = new ArrayList<RelativeStrengthIndexData>();
		// try(BufferedReader br = new BufferedReader(new
		// FileReader("/nbr/relativeStrengthIndex/table_888.L.csv"))) {
		try (InputStream input = new URL(
				"https://finance.google.ca/finance/historical?q=ETR%3ASKB&startdate=Nov%201,%202011&enddate=Nov%2030,%202011&output=csv")
						.openStream()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			int ctd = 0;
			while (line != null) {

				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
				if (null != line) {
					// System.out.println(line);
					RelativeStrengthIndexData rsid = new RelativeStrengthIndexData();
					String[] torsid = line.split(",");
					rsid.setId(++ctd);
					DateFormat formatter1;
					formatter1 = new SimpleDateFormat("yyyy-MMM-DD", Locale.ENGLISH);
					rsid.setFecha(formatter1.parse(torsid[0]));
					rsid.setClose(Double.parseDouble(torsid[4]));
					rsid.setHigh(Double.parseDouble(torsid[2]));
					rsid.setLow(Double.parseDouble(torsid[3]));
					lstRSI.add(rsid);
				}

			}
		} catch (FileNotFoundException e) {
			_logger.error("UtilGeneral public static List<RelativeStrengthIndexData> getListaRSIGoogle() [FileNotFoundException]", e);
		} catch (IOException e) {
			_logger.error("UtilGeneral public static List<RelativeStrengthIndexData> getListaRSIGoogle() [IOException]", e);
		} catch (ParseException e) {
			_logger.error("UtilGeneral public static List<RelativeStrengthIndexData> getListaRSIGoogle() [ParseException]", e);
		}

		return lstRSI;
	}

	/**
	 * @param companySymbol
	 * @param nDays
	 * @param print
	 * @param iteracion
	 * @return
	 */
	/*
	 * @Deprecated public static List<RelativeStrengthIndexData>
	 * getListaRSIGoogle(String symbol, String dateEnd, String dateBegin, boolean
	 * print) { List<RelativeStrengthIndexData> lstRSI = null; lstRSI = new
	 * ArrayList<RelativeStrengthIndexData>(); // String urlHistdata = //
	 * "https://www.google.ca/finance/historical?q="+symbol.replace(":", //
	 * "%3A")+"&startdate="+dateBegin.replace(" ", //
	 * "%20")+"&enddate="+dateEnd.replace(" ", "%20")+"&output=csv"; String
	 * urlHistdata = "https://finance.google.ca/finance/historical?q=" +
	 * symbol.replace(":", "%3A") + "&startdate=" + dateBegin.replace(" ", "%20") +
	 * "&enddate=" + dateEnd.replace(" ", "%20") + "&output=csv";
	 * 
	 * int ctd = 0; String[] torsid = null; try (InputStream input = new
	 * URL(urlHistdata).openStream()) {
	 * 
	 * if (print) { System.out.println("urlHistdata: [" + urlHistdata + "]");
	 * System.out.println("Date,Open,High,Low,Close"); }
	 * 
	 * BufferedReader br = new BufferedReader(new InputStreamReader(input,
	 * "UTF-8")); StringBuilder sb = new StringBuilder(); String line =
	 * br.readLine();
	 * 
	 * ctd = 0; while (line != null) {
	 * 
	 * sb.append(line); sb.append(System.lineSeparator()); line = br.readLine(); if
	 * (null != line) { // System.out.println(line); RelativeStrengthIndexData rsid
	 * = new RelativeStrengthIndexData(); torsid = line.split(",");
	 * 
	 * rsid.setId(++ctd); DateFormat formatter1; formatter1 = new
	 * SimpleDateFormat("d-MMM-yy", Locale.ENGLISH); try {
	 * rsid.setFecha(formatter1.parse(torsid[0])); } catch (ParseException e) {
	 * formatter1 = new SimpleDateFormat("d-MMM.-yy", Locale.ENGLISH);
	 * rsid.setFecha(formatter1.parse(torsid[0])); } try {
	 * rsid.setClose(Double.parseDouble(torsid[4])); } catch (NumberFormatException
	 * n) { rsid.setClose(0); } try { rsid.setHigh(Double.parseDouble(torsid[2])); }
	 * catch (NumberFormatException n) { rsid.setHigh(0); } try {
	 * rsid.setLow(Double.parseDouble(torsid[3])); } catch (NumberFormatException n)
	 * { rsid.setLow(0); } lstRSI.add(rsid);
	 * 
	 * if (print) { try { System.out.println(torsid[0] + "," +
	 * Double.parseDouble(torsid[1]) + "," + Double.parseDouble(torsid[2]) + "," +
	 * Double.parseDouble(torsid[3]) + "," + Double.parseDouble(torsid[4])); } catch
	 * (NumberFormatException n) { System.out.println( torsid[0] + "," + torsid[1] +
	 * "," + torsid[2] + "," + torsid[3] + "," + torsid[4]); } }
	 * 
	 * if (ctd > 13) { break; } }
	 * 
	 * } } catch (FileNotFoundException e) {
	 * 
	 * lstRSI = getListaRSIGoogleByHTML(symbol, dateEnd, dateBegin, print);
	 * 
	 * if (lstRSI == null || (lstRSI != null && lstRSI.size() < 3)) {
	 * System.out.println("Error al leer [" + symbol + "](FileNotFoundException)");
	 * } } catch (IOException e) { System.out.println("Error al leer [" + symbol +
	 * "](IOException)"); } catch (ParseException e) {
	 * System.out.println("Error al leer [" + symbol + "](ParseException)"); } catch
	 * (NumberFormatException nf) { System.out.println("Error al leer [" + symbol +
	 * "](NumberFormatException) [" + ctd + "][" + torsid + "]"); }
	 * 
	 * return lstRSI; }
	 */

	/**
	 * @param companySymbol
	 * @param nDays
	 * @param print
	 * @param iteracion
	 * @return
	 */
	public static List<RelativeStrengthIndexData> getListaRSIGoogleDB(Long scnCodigo, boolean print) {
		List<RelativeStrengthIndexData> lstRSI = null;
		lstRSI = new ArrayList<RelativeStrengthIndexData>();

		AdminEntity admEnt = AdminEntity.getInstance();

		try {

			HistoricalDataCompany hdc = new HistoricalDataCompany();
			hdc.setCompany(scnCodigo);
			List<HistoricalDataCompany> lstHdc = admEnt.getHistoricalDataCompanyByCompany(hdc);

			int ctd = 0;

			if (print) {
				_logger.info("urlHistdata [scnCodigo]: [" + scnCodigo + "]");
				_logger.info("Date,Open,High,Low,Close");
			}

			ctd = 0;
			for (HistoricalDataCompany historicalDataCompany : lstHdc) {

				RelativeStrengthIndexData rsid = new RelativeStrengthIndexData();

				rsid.setId(++ctd);
				rsid.setFecha(historicalDataCompany.getFechaDataHistorica().getTime());
				try {
					rsid.setClose(Double.parseDouble(historicalDataCompany.getStockPriceClose()));
				} catch (NumberFormatException n) {
					rsid.setClose(0);
				}
				try {
					rsid.setHigh(Double.parseDouble(historicalDataCompany.getStockPriceHigh()));
				} catch (NumberFormatException n) {
					rsid.setHigh(0);
				}
				try {
					rsid.setLow(Double.parseDouble(historicalDataCompany.getStockPriceLow()));
				} catch (NumberFormatException n) {
					rsid.setLow(0);
				}
				lstRSI.add(rsid);

				if (print) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
					try {
						_logger.info(sdf.format(historicalDataCompany.getFechaDataHistorica().getTime()) + ","
								+ Double.parseDouble(historicalDataCompany.getStockPriceOpen()) + ","
								+ Double.parseDouble(historicalDataCompany.getStockPriceHigh()) + ","
								+ Double.parseDouble(historicalDataCompany.getStockPriceLow()) + ","
								+ Double.parseDouble(historicalDataCompany.getStockPriceClose()));
					} catch (NumberFormatException n) {
						_logger.info(sdf.format(historicalDataCompany.getFechaDataHistorica().getTime()) + ","
								+ historicalDataCompany.getStockPriceOpen() + ","
								+ historicalDataCompany.getStockPriceHigh() + ","
								+ historicalDataCompany.getStockPriceLow() + ","
								+ historicalDataCompany.getStockPriceClose());
					}
				}

				if (ctd > 13) {
					break;
				}

			}

		} catch (ParseException e) {
			_logger.error("Error al leer scnCodigo: [" + scnCodigo + "](ParseException)", e);
		} catch (Exception e1) {
			_logger.error("Error al leer scnCodigo: [" + scnCodigo + "](Exception)", e1);

		}

		return lstRSI;
	}

	/**
	 * @param companySymbol
	 * @param nDays
	 * @param print
	 * @param iteracion
	 * @return
	 */
	public static List<RelativeStrengthIndexData> getFirstLastRSIDataGoogleDB(Long scnCodigo, boolean print) {
		List<RelativeStrengthIndexData> lstRSI = null;
		lstRSI = new ArrayList<RelativeStrengthIndexData>();

		AdminEntity admEnt = AdminEntity.getInstance();

		try {

			HistoricalDataCompany hdc = new HistoricalDataCompany();
			hdc.setCompany(scnCodigo);
			List<HistoricalDataCompany> lstHdc = admEnt.getHistoricalDataCompanyByCompany(hdc);

			int ctd = 0;

			if (print) {
				_logger.info("urlHistdata [scnCodigo]: [" + scnCodigo + "]");
				_logger.info("Date,Open,High,Low,Close");
			}

			ctd = 0;
			for (HistoricalDataCompany historicalDataCompany : lstHdc) {

				RelativeStrengthIndexData rsid = new RelativeStrengthIndexData();

				rsid.setId(++ctd);
				rsid.setFecha(historicalDataCompany.getFechaDataHistorica().getTime());
				try {
					rsid.setClose(Double.parseDouble(historicalDataCompany.getStockPriceClose()));
				} catch (NumberFormatException n) {
					rsid.setClose(0);
				}
				try {
					rsid.setHigh(Double.parseDouble(historicalDataCompany.getStockPriceHigh()));
				} catch (NumberFormatException n) {
					rsid.setHigh(0);
				}
				try {
					rsid.setLow(Double.parseDouble(historicalDataCompany.getStockPriceLow()));
				} catch (NumberFormatException n) {
					rsid.setLow(0);
				}
				lstRSI.add(rsid);

				if (print) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
					try {
						_logger.info(sdf.format(historicalDataCompany.getFechaDataHistorica().getTime()) + ","
								+ Double.parseDouble(historicalDataCompany.getStockPriceOpen()) + ","
								+ Double.parseDouble(historicalDataCompany.getStockPriceHigh()) + ","
								+ Double.parseDouble(historicalDataCompany.getStockPriceLow()) + ","
								+ Double.parseDouble(historicalDataCompany.getStockPriceClose()));
					} catch (NumberFormatException n) {
						_logger.info(sdf.format(historicalDataCompany.getFechaDataHistorica().getTime()) + ","
								+ historicalDataCompany.getStockPriceOpen() + ","
								+ historicalDataCompany.getStockPriceHigh() + ","
								+ historicalDataCompany.getStockPriceLow() + ","
								+ historicalDataCompany.getStockPriceClose());
					}
				}

				if (ctd > 13) {
					break;
				}

			}

		} catch (ParseException e) {
			_logger.error("Error al leer scnCodigo: [" + scnCodigo + "](ParseException)", e);
		} catch (Exception e1) {
			_logger.error("Error al leer scnCodigo: [" + scnCodigo + "](Exception)", e1);
		}

		return lstRSI;
	}

	/**
	 * Toma info de bloomberg para indicar si hay retornos positivos o negativos
	 * 
	 * @return
	 */
	public static String getYearReturn(String urlBloomberg) {
		String retornoYTD = "";

		try {
			Document doc;
			doc = Jsoup.connect(urlBloomberg).timeout(3000).get();
			Elements newsHeadlines = doc.select("div.cell__value_up");
			// int itera = 0;

			for (Element element : newsHeadlines) {
				// System.out.print((++itera) + ". ");
				// retornoYTD += (++itera) + ". ";
				// System.out.print("[" + element.text() + "]");
				retornoYTD += "[" + element.text() + "]";
			}

		} catch (IOException e) {
			_logger.error("Error al obtener indicador de Bloomberg: " + e.getMessage(), e);
			_logger.error("{" + urlBloomberg + "}");
		}

		if (retornoYTD != null && retornoYTD.length() > 2) {
			retornoYTD = urlBloomberg + "\t " + retornoYTD;
		}

		return retornoYTD;
	}

	/**
	 * Obtiene la fecha de hoy en formato "MMM dd, yyyy"
	 * 
	 * @return
	 */
	public static String obtenerToday() {

		String fh = null;
		DateFormat formatter1;
		formatter1 = new SimpleDateFormat("MMM+d,+yyyy", Locale.ENGLISH);
		fh = formatter1.format(new Date());

		return fh;

	}

	/**
	 * Obtiene identificadorunicoiteracion
	 * 
	 * @return
	 */
	public static Long obtenerIdIteracion() {

		String fh = null;
		DateFormat formatter1;
		formatter1 = new SimpleDateFormat("yyMMddHHmm", Locale.ENGLISH);
		fh = formatter1.format(new Date());

		return Long.parseLong(fh);

	}

	/**
	 * Obtiene la fecha de hoy en formato "yyyy-mm-DD"
	 * 
	 * @return
	 */
	public static String obtenerTodayMinusNDays(int ndays) {

		String dateOneMothAgo = null;

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, ndays); // I just want date before 90 days. you can give that you want.

		// SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd"); // you can specify
		// your format here...
		// Log.d("DATE","Date before 90 Days: " + s.format(new
		// Date(cal.getTimeInMillis())));
		//
		//
		// Date today = new Date();
		DateFormat formatter1;
		// formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		formatter1 = new SimpleDateFormat("MMM+d,+yyyy", Locale.ENGLISH);
		dateOneMothAgo = formatter1.format(new Date(cal.getTimeInMillis()));

		return dateOneMothAgo;

	}

	/**
	 * Obtiene la fecha de hoy hace un mes en formato "MMM dd, yyyy"
	 * 
	 * @return
	 */
	public static String obtenerTodayMinusMonth() {

		String dateOneMothAgo = null;

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -27); // I just want date before 90 days. you can give that you want.

		// SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd"); // you can specify
		// your format here...
		// Log.d("DATE","Date before 90 Days: " + s.format(new
		// Date(cal.getTimeInMillis())));
		//
		//
		// Date today = new Date();
		DateFormat formatter1;
		formatter1 = new SimpleDateFormat("MMM+d,+yyyy", Locale.ENGLISH);
		dateOneMothAgo = formatter1.format(new Date(cal.getTimeInMillis()));

		return dateOneMothAgo;

	}

	/**
	 * @return
	 */
	public static String obtenerTodayMinusThree() {

		String dateThreeDaysAgo = null;

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -5); // I just want date before 90 days. you can give that you want.
		DateFormat formatter1;
		formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		dateThreeDaysAgo = formatter1.format(new Date(cal.getTimeInMillis()));

		return dateThreeDaysAgo;

	}

	/**
	 * @return
	 */
	public static String obtenerFirstDateOftheYearMinusOne() {

		String firstDayYearMinusOne = null;
		String year = null;

		DateFormat formatterYear;
		formatterYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
		year = formatterYear.format(new Date());

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.DAY_OF_YEAR, 1);
		cal.add(Calendar.DATE, -3); // --29 de Diciembre
		// cal.add(Calendar.DATE, -362); --Evaluar si al restar estos dias es un dia
		// habil, tener en cuenta una fecha fija seteando el valor del primer dia del
		// anio
		cal.add(Calendar.DATE, -138);

		DateFormat formatter1;
		formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		firstDayYearMinusOne = formatter1.format(new Date(cal.getTimeInMillis()));

		return firstDayYearMinusOne;

	}

	/**
	 * @return
	 */
	public static DataMiningCompany construirObjetoDataMiningCompany(CompanyRanking cpnRnk,
			Long identificadorUnicoIteracion) {
		DataMiningCompany retornoDMC = new DataMiningCompany();
		retornoDMC.setIdIteracion(identificadorUnicoIteracion);
		retornoDMC.setFechaCreacion(Calendar.getInstance());
		Company cmp = new Company();
		cmp.setId(cpnRnk.getIdCompany());
		retornoDMC.setCompany(cmp);
		retornoDMC.setNotaPonderada(printNumberFormat(cpnRnk.getNotaPonderada(), "#########.#########"));
		retornoDMC.setStockPrice(printNumberFormat(cpnRnk.getPrecioEvaluado(), "#########.#########"));
		retornoDMC.setPriceEarningRatio(printNumberFormat(cpnRnk.getPeRatio(), "#########.#########"));
		retornoDMC.setIsPriceBetweenHighLow(cpnRnk.isPriceBetweenHighLow());
		retornoDMC.setPricePercentageIncrement(printNumberFormat(cpnRnk.getPricePercentageincrement(), "###.###"));
		int price = (int) cpnRnk.getPrecioEvaluado();
		retornoDMC.setLastDigitStockPrice(price % 10);
		retornoDMC.setYTDPlataforma(printNumberFormat(cpnRnk.getYTD(), "#########.#########"));

		return retornoDMC;
	}

	/**
	 * retorna el Item con el valor mas bajo.
	 * 
	 * @param hdc
	 * @return
	 */
	public static int getLowest(HistoricalDataCompany[] hdc) {

		double lowest = 0;
		int idxLowest = 0;
		for (int i = 0; i < hdc.length; i++) {
			HistoricalDataCompany historicalDataCompany = hdc[i];
			if (i == 0) {
				lowest = Double.parseDouble(historicalDataCompany.getStockPriceLow());
				idxLowest = i;
			}
			if (Double.parseDouble(historicalDataCompany.getStockPriceLow()) < lowest) {
				idxLowest = i;
				lowest = Double.parseDouble(historicalDataCompany.getStockPriceLow());
			}

		}

		return idxLowest;

	}

	/**
	 * retorna el Item con el valor mas bajo.
	 * 
	 * @param hdc
	 * @return
	 */
	public static int getHighest(HistoricalDataCompany[] hdc) {

		double highest = 0;
		int idxHighest = 0;
		for (int i = 0; i < hdc.length; i++) {
			HistoricalDataCompany historicalDataCompany = hdc[i];
			if (i == 0) {
				highest = Double.parseDouble(historicalDataCompany.getStockPriceHigh());
				idxHighest = i;
			}
			if (Double.parseDouble(historicalDataCompany.getStockPriceHigh()) > highest) {
				idxHighest = i;
				highest = Double.parseDouble(historicalDataCompany.getStockPriceHigh());
			}

		}

		return idxHighest;

	}

	/*
	 * Valida el precio actual de las compañias con el presio de reaction Mode si
	 * esta dentro de la opción de buy o sell
	 * 
	 * Toma el ultimo precio de la tabla indexyahoocfd.iyc_quote_company_history, lo
	 * compara con el ultimo precio de
	 * indexyahoocfd.hst_historical_data_company_to_rsi y realiza el calculo para
	 * saber si esta en Buyo SELL
	 */
	public static void printPosibleBuyOrSell(String[] idCompaniesToLookUpArray) {
		

		AdminEntity admEnt = AdminEntity.getInstance();
		Map<Long, HistoricalDataCompany> h = null;
		List<QuoteHistoryCompany> q = null;

		try {
			h = admEnt.getAllLastHistoricalData();
			q = admEnt.getAllLastPriceHistory();

			if (q != null && q.size() > 0) {
				for (QuoteHistoryCompany quoteHistoryCompany : q) {
					try {
						if (idCompaniesToLookUpArray == null || isCompanyInTheArray(quoteHistoryCompany.getCompany(), idCompaniesToLookUpArray )) {
						double lastHigh, lastLow, lastClose, actualPrice;
						HistoricalDataCompany historicalDataCompany = h.get(quoteHistoryCompany.getCompany());
						lastHigh = Double.parseDouble(historicalDataCompany.getStockPriceHigh());
						lastLow = Double.parseDouble(historicalDataCompany.getStockPriceLow());
						lastClose = Double.parseDouble(historicalDataCompany.getStockPriceClose());
						actualPrice = Double.parseDouble(quoteHistoryCompany.getPrice());

						ReactionTrendSystem reactionTrendSystem = isReactionMode(lastHigh, lastLow, lastClose,
								actualPrice, false);
						if (actualPrice <= reactionTrendSystem.getB_1()) {
							_logger.info("The companyID [" + historicalDataCompany.getCompany()
									+ "], has the price below B1");
						}
						if (actualPrice >= reactionTrendSystem.getS_1()) {
							_logger.info("The companyID [" + historicalDataCompany.getCompany()
									+ "], has the price above S1");
						}
						}
					} catch (Exception e) {
						// Salta al siguiente Registro
					}

				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Evalua si el precio actual esta en ReactionMode o TrendMode
	 * 
	 * @param lastHigh
	 * @param lastLow
	 * @param lastClose
	 * @return
	 * 
	 * 		Array --> 0 BuyPoint 1 SellPoint 2 HBOP 3 LBOP 4 REACTION MODE (1) |
	 *         TREND MODE (0)
	 * 
	 */
	public static ReactionTrendSystem isReactionMode(double lastHigh, double lastLow, double lastClose,
			double actualPrice, boolean print) {
		
		if (lastHigh == lastLow && lastLow == lastClose && lastClose ==  actualPrice) {
			return null;
		}

		ReactionTrendSystem rts = new ReactionTrendSystem();
		/*
		 * xPrima:= media entre HLC b_1:= Buy Point s_1:= Sell Point
		 * hbop:=HighBreakOutPoint lbop:=LowBreakOutPoint
		 */
		double xPrima, b_1, s_1, hbop, lbop, b_1_up, s_1_down;
		if (print) {
			_logger.info("actualPrice: " + actualPrice);
		}
		xPrima = (lastHigh + lastLow + lastClose) / 3;
		b_1 = (2 * xPrima) - lastHigh;
		b_1_up = (xPrima - (b_1 - xPrima));
		s_1 = (2 * xPrima) - lastLow;
		s_1_down = xPrima - (s_1 - xPrima);
		
		hbop = (2 * xPrima) - (2 * lastLow) + lastHigh;
		lbop = (2 * xPrima) - (2 * lastHigh) + lastLow;
		if (print) {
			_logger.info("xPrima" + xPrima);
			_logger.info("b_1: (" + b_1 + "|" + b_1_up + ")s_1: (" + s_1 + "|" + (xPrima + (xPrima - s_1)) + ")");
			_logger.info("hbop: " + hbop + "lbop:" + lbop );
		}
		 

		boolean betweenBuy = (actualPrice > b_1) && (actualPrice < b_1_up);
		rts.setActualPriceBetweenBuy(betweenBuy);
		rts.setActualPriceBetweenSell((actualPrice > s_1_down) && (actualPrice < s_1) && !betweenBuy);
		rts.setActualPriceUpHBOP((actualPrice > hbop));
		rts.setActualPriceDownLBOP((actualPrice < lbop));
		rts.setActualPriceBetweenHBOPLBOP((actualPrice < hbop) && (actualPrice > lbop));

		rts.setxPrima(xPrima);
		rts.setB_1(b_1);
		rts.setS_1(s_1);
		rts.setHbop(hbop);
		rts.setLbop(lbop);
		rts.setB_1_up(b_1_up);
		rts.setS_1_down(s_1_down);

		return rts;

	}
	
	/**
	 * @param idCmp
	 * @param array
	 * @return
	 */
	private static boolean isCompanyInTheArray(Long idCmp, String[] array) {
		
		boolean isCompanyInTheArray = false;
		
		for (String c : array) {
			if (Long.parseLong( c ) == idCmp) {
				isCompanyInTheArray = true;
				break;
			}
			
		}
		
		
		return isCompanyInTheArray;
		
	}

}
