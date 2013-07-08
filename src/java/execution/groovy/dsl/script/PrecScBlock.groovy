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

class PrecScBlock {
  String query
  def rows = []

  def init(Closure closure) {
    closure.delegate = this
    closure.call()

    if (!rows) {
      rows << Collections.<String, Object> emptyMap()
    }

    validate();
  }

  def sql(String query) {
    this.query = query
  }

  def dql(String query) {
    this.query = query
  }

  def row(Map<String, Object> rowMap) {
    rows << rowMap
  }

  def validate() {
    return (!query)
  }

  def execute(session) {
    return DCScriptRunner.runScript(session, query, rows)
  }
}
