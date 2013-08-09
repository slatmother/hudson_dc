package execution.groovy.dsl.script

import execution.java.runner.DCScriptRunner

/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
class DCScBlock {
  String query
  def min
  def max
  def validation_result
  String queryType

  /**
   *
   * @param closure
   * @return
   */
  def init(Closure closure) {
    closure.delegate = this
    closure.call()
  }

  /**
   *
   * @param query
   * @return
   */
  def sql(String query) {
    if (!this.query) {
      this.query = query
    }

    queryType = "sql"
  }

  /**
   *
   * @param query
   * @return
   */
  def dql(String query) {
    if (!this.query) {
      this.query = query
    }

    queryType = "dql"
  }

  /**
   *
   * @param map
   * @return
   */
  def condition(Map<Integer, Integer> map) {
    this.min = map.min
    this.max = map.max
  }

  /**
   *
   * @return
   */
  def validate() {
    validation_result = (query != null && !query.isAllWhitespace()) && (min != null)
    return validation_result
  }

  /**
   *
   * @param session
   * @return
   */
  def execute(session) {
    DCScriptRunner.runScript(session,
            query,
            min,
            max)
  }
}
