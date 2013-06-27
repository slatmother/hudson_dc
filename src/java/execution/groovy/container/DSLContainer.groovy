package execution.groovy.container

import database.DatabaseHelperFactory
import execution.DCScriptRunner
import execution.groovy.script.DCScBlock
import execution.groovy.script.PostScBlock
import execution.groovy.script.PrecScBlock
import util.SQLUtils

/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
class DSLContainer {
  boolean isAutoCommitDC = false
  def rollbackQuery

  PrecScBlock precondition = new PrecScBlock()
  DCScBlock dc = new DCScBlock()
  PostScBlock postcondition = new PostScBlock()

  def precondition(Closure closure) {
    precondition.init(closure)
  }

  def dc(Closure closure) {
    dc.init(closure)
  }

  def postcondition(Closure closure) {
    postcondition.init(closure)
  }

  def validate() {
    return precondition.validate() && dc.validate() && postcondition.validate()
  }

  def preexecute(session) {
    String dcQueryType = SQLUtils.checkQueryType(dc.query)

    if ("view".equals(dcQueryType)) {
      needManualRollback = true
      rollbackQuery = SQLUtils.constructRollbackQuery(session, dc.query, dcQueryType)
    }
  }

  def execute(session) {
    boolean result = true

    if (validate()) {
      preexecute(session)

      result &= DCScriptRunner.runScript(session, precondition.query, precondition.rows)

      result &= result ? DCScriptRunner.runScript(
              isAutoCommitDC ? DatabaseHelperFactory.getCustomProjectHelper() : session,
              dc.query, dc.condition) : false
      result &= result ? DCScriptRunner.runScript(session, postcondition.query, postcondition.rows) : false
    }

    return result
  }

  def rollback() {
    return DCScriptRunner.runScript(DatabaseHelperFactory.getCustomProjectHelper(), rollbackQuery, "")
  }
}
