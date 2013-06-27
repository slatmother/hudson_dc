package execution.groovy

import execution.DCScriptRunner

/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
@Deprecated
class GroovyDSLContainer {
  def session;
  String sql
  String dql
  String dc
  int affected = 0
  def rows = []

  GroovyDSLContainer(session) {
    this.session = session
  }

  def precondition(Closure closure) {
    closure.delegate = this
    closure.call()

    if (rows.size() == 0) {
      rows << Collections.<String, Object> emptyMap()
    }

    (sql == null || sql.equals('')) ? executeDQL(dql, rows) : executeSQL(sql, rows)
  }

  def dc(Closure closure) {
    closure.delegate = this
    closure.call()
    (sql == null || sql.equals('')) ? executeDQL(dql, affected) : executeSQL(sql, affected)
  }

  def sql(String sqlQuery) {
    this.sql = sqlQuery.replaceAll('[\n\t\r]', ' ')
  }

  def dql(String dqlQuery) {
    this.dql = dqlQuery
  }

  def affected(int rowNumAffected) {
    this.affected = rowNumAffected
  }

  def row(Map<String, Object> rowMap) {
    rows << rowMap
  }

  def executeDQL(query, condition) {
    DCScriptRunner.runScript(
            session,
            query,
            condition
    )
  }

  def executeSQL(query, condition) {
    DCScriptRunner.runScript(
            session,
            query,
            condition
    )
  }
}
