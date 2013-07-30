package execution.groovy.dsl.container

import database.DBHelper
import database.DBHelperFactory
import execution.groovy.dsl.script.DCScBlock
import execution.groovy.dsl.script.PostScBlock
import execution.groovy.dsl.script.PrecScBlock
import execution.java.runner.DCScriptRunner
import org.apache.log4j.Logger
import transaction.SQLTx

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
  def logger = Logger.rootLogger
  def rollbackQuery
  def name

  def preconditions = []
  DCScBlock dc = new DCScBlock()
  def postconditions = []

  boolean isAutoCommitDC = false
  boolean needManualRollback = false

  def precondition(Closure closure) {
    PrecScBlock prec = new PrecScBlock()
    prec.init(closure)
    preconditions << prec
  }

  def dc(Closure closure) {
    dc.init(closure)
  }

  def postcondition(Closure closure) {
    PostScBlock post = new PostScBlock()
    post.init(closure)
    postconditions << post
  }

  def validate() {
    boolean result = true;

    for (precondition in preconditions) {
      result &= precondition.validate()
    }

    result &= dc.validate()

    for (postcondition in preconditions) {
      result &= postcondition.validate()
    }

    if (!result) {
      logger.info("Validation failed! Check syntax of" +
              precondition.validate() ? "" : " precondition " +
              dc.validate() ? "" : " dc " +
              postcondition.validate() ? "" : " postcondition " +
              "block"
      )
    }
    return result
  }

  def execute(session) {
    boolean result = true
    logger.info("Start executing container with name ${name}")

    for (precondition in preconditions) {
      result &= precondition.execute(session)
      logger.info("Precondition result is ${result}")
    }

    if (result) {
      if (dc.needManualRollback) {
        DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper()
        SQLTx sqlTxHelper = SQLTx.beginTransaction(dbHelper.getConnection())

        result &= dc.execute(dbHelper)

        sqlTxHelper.okToCommit()
        sqlTxHelper.commitOrAbort()
      } else {
        result &= dc.execute(session)
      }

      logger.info("DC result is ${result}")
    }

    if (result) {
      for (postcondition in postconditions) {
        result &= result ? DCScriptRunner.runScript(session, postcondition.query, postcondition.rows) : false
        logger.info("Postcondition result is " + result)
      }
    }

    return result
  }

//  def rollback() {
////    if (dc.hasExecuted && dc.needManualRollback && !dc.statement.rollbackQuery) {
//      if (dc.hasExecuted && dc.needManualRollback) {
//      logger.info("Mapping with name " + name + " contains auto-commit dc. Rollback is running.")
//      DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper()
//      SQLTx sqlTxHelper = SQLTx.beginTransaction(dbHelper.getConnection())
//
//      DCScriptRunner.runScript(dbHelper, dc.statement.rollbackQuery, 0, null)
//
//      sqlTxHelper.okToCommit()
//      sqlTxHelper.commitOrAbort()
//    }
//  }
}
