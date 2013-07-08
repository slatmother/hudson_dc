package execution.groovy.dsl.script

import execution.java.runner.DCScriptRunner
import parser.IStatement
import parser.sql.SQLStatement

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
  IStatement statement
  boolean hasExecuted
  boolean needManualRollback
  def min
  def max

  def init(Closure closure) {
    closure.delegate = this
    closure.call()
  }

  def sql(String query) {
    if (!statement) {
      statement = SQLStatement.resolve(query)
      needManualRollback = ((SQLStatement) statement).isDDLQuery()
    }
  }

  def dql(String query) {
    if (!statement) {
      statement = DQLStatement.resolve(query)
      needManualRollback = false
    }
  }

  def condition(Map<Integer, Integer> map) {
    this.min = map.min
    this.max = map.max
  }

  def validate() {
    return (!statement?.queryType) && (!min)
  }


  def execute(session) {
    if (needManualRollback) {

    }
    hasExecuted = DCScriptRunner.runScript(session,
            query,
            min,
            max)
    return hasExecuted
  }
}
