package com.wireless.soft.indices.cfd.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;


public class UtilSession {

    // ////////////////////////////////////////////////////////////////////////
    // Logger de la clase
    // ////////////////////////////////////////////////////////////////////////
    /** */
    private static final Logger _logger = Logger.getLogger(UtilSession.class);

    // ////////////////////////////////////////////////////////////////////////
    // Metodos utilitarios
    // ////////////////////////////////////////////////////////////////////////

    /**
     * Metodo que ejecuta un update definido dentro del namedquery
     * @param entityManager El entity manager a utilizar
     * @param namedQuery El nombre del query nombrado
     * @param params Los parametros del query
     * @return El resultado de la consulta
     * @throws Exception Si existe un problema al ejecutar la consulta
     */
    public static int executeUpdateByNamedQuery(EntityManager entityManager, String namedQuery,
	    Map<String, Object> params) throws Exception {
	try {
	    // Crea un objeto query
	    Query query = entityManager.createNamedQuery(namedQuery);
	    // Setea parametros
	    setParameters(query, params);
	    // Ejecuta update
	    return query.executeUpdate();
	} catch (Exception ex) {
	    _logger.error(ex.getMessage(), ex);
	    throw new Exception(ex);
	}
    }

    /**
     * Metodo que ejecuta un update utilizando sentencia SQL
     * @param entityManager El entity manager a utilizar
     * @param sql Sentencia sql a ejecutar
     * @param params Los parametros del query
     * @return El resultado de la consulta
     * @throws Exception Si existe un problema al ejecutar la consulta
     */
    public static int executeUpdateByNativeQuery(EntityManager entityManager, String sql, Map<String, Object> params)
	    throws Exception {
	try {
	    // Crea un objeto query
	    Query query = entityManager.createNativeQuery(sql);
	    // Setea parametros
	    setParameters(query, params);
	    // Ejecuta update
	    return query.executeUpdate();
	} catch (Exception ex) {
	    _logger.error(ex.getMessage(), ex);
	    throw new Exception(ex);
	}
    }

    /**
     * Metodo que ejecuta un update utilizando sentencia HQL
     * @param entityManager El entity manager a utilizar
     * @param hql Sentencia HQL a ejecutar
     * @param params Los parametros del query
     * @return El resultado de la consulta
     * @throws Exception Si existe un problema al ejecutar la consulta
     */
    public static int executeUpdateByQuery(EntityManager entityManager, String hql, Map<String, Object> params)
	    throws Exception {
	try {
	    // Crea un objeto query
	    Query query = entityManager.createQuery(hql);
	    // Setea parametros
	    setParameters(query, params);
	    // Ejecuta update
	    return query.executeUpdate();
	} catch (Exception ex) {
	    _logger.error(ex.getMessage(), ex);
	    throw new Exception(ex);
	}
    }

    /**
     * Metodo que ejecuta un select utilizando la consulta definido dentro de un namedquery
     * @param entityManager El entity manager a utilizar
     * @param namedQuery Nombre del named query a utilizar
     * @param params Los parametros del query
     * @return El resultado de la consulta
     * @throws Exception Si existe un problema al ejecutar la consulta
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getObjectsByNamedQuery(EntityManager entityManager, String namedQuery,
	    Map<String, Object> params) throws Exception {
	try {
	    // Crea un objeto query
	    Query query = entityManager.createNamedQuery(namedQuery);
	    // Setea parametros
	    setParameters(query, params);
	    // Ejecuta la consulta
	    return query.getResultList();
	} catch (Exception ex) {
	    _logger.error(ex.getMessage(), ex);
	    throw new Exception(ex);
	}
    }

    /**
     * Metodo que ejecuta un select utilizando la consulta en formato SQL
     * @param entityManager El entity manager a utilizar
     * @param nativeQuery Consulta SQL a invocar
     * @param clazz Clase que debe retornar el metodo en el resultado
     * @param params Los parametros del query
     * @return El resultado de la consulta
     * @throws Exception Si existe un problema al ejecutar la consulta
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getObjectsByNativeQuery(EntityManager entityManager, String nativeQuery, Class<?> clazz,
	    Map<String, Object> params) throws Exception {
	try {
	    // Crea un objeto query
	    Query query = null;
	    query = entityManager.createNativeQuery(nativeQuery, clazz);
	    // Setea parametros
	    setParameters(query, params);
	    // Ejecuta la consulta
	    return query.getResultList();
	} catch (Exception ex) {
	    _logger.error(ex.getMessage(), ex);
	    throw new Exception(ex);
	}
    }

    /**
     * Metodo que ejecuta un select utilizando la consulta en formato SQL
     * @param entityManager El entity manager a utilizar
     * @param nativeQuery Consulta SQL a invocar
     * @param params Los parametros del query
     * @return El resultado de la consulta
     * @throws Exception Si existe un problema al ejecutar la consulta
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getObjectsByNativeQuery(EntityManager entityManager, String nativeQuery,
	    Map<String, Object> params) throws Exception {
	try {
	    // Crea un objeto query
	    Query query = null;
	    query = entityManager.createNativeQuery(nativeQuery);
	    // Setea parametros
	    setParameters(query, params);
	    // Ejecuta la consulta
	    return query.getResultList();
	} catch (Exception ex) {
	    _logger.error(ex.getMessage(), ex);
	    throw new Exception(ex);
	}
    }

    /**
     * Metodo que ejecuta un select utilizando la consulta en formato HQL
     * @param entityManager El entity manager a utilizar
     * @param sql Consulta HQL a invocar
     * @param params Los parametros del query
     * @return El resultado de la consulta
     * @throws Exception Si existe un problema al ejecutar la consulta
     */
    @SuppressWarnings("unchecked")
    public static List<Object> getObjectsByQuery(EntityManager entityManager, String sql, Map<String, Object> params)
	    throws Exception {
	try {
	    // Crea un objeto query
	    Query query = entityManager.createQuery(sql);
	    // Setea parametros
	    setParameters(query, params);
	    // Ejecuta la consulta
	    return query.getResultList();
	} catch (Exception ex) {
	    _logger.error(ex.getMessage(), ex);
	    throw new Exception(ex);
	}
    }

    /**
     * Metodo que setea los parametros de una consulta
     * @param query El query a setear los parametros
     * @param params Los parametros a setear dentro del query
     */
    private static void setParameters(Query query, Map<String, Object> params) {
	// Verifica que los parametros sean distinto a vacio
	if (params != null) {
	    // Conjunto de llaves con parametros
	    Set<String> keys = params.keySet();
	    for (String key : keys) {
		query.setParameter(key, params.get(key));
	    }
	}
    }
}
