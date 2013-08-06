package execution.groovy.dsl.container

import execution.groovy.dsl.script.DCScBlock
import execution.groovy.dsl.script.PostScBlock
import execution.groovy.dsl.script.PrecScBlock
import execution.java.runner.DCScriptRunner
import org.apache.log4j.Logger

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
  DCScBlock dc = new DCScBlock()
  def preconditions = []
  def postconditions = []
  String name
  String dcQueryType

  /**
   *
   * @param closure
   * @return
   */
  def precondition(Closure closure) {
    PrecScBlock prec = new PrecScBlock()
    prec.init(closure)
    preconditions << prec
  }

  /**
   *
   * @param closure
   * @return
   */
  def dc(Closure closure) {
    dc.init(closure)
    dcQueryType = dc.queryType
  }

  /**
   *
   * @param closure
   * @return
   */
  def postcondition(Closure closure) {
    PostScBlock post = new PostScBlock()
    post.init(closure)
    postconditions << post
  }

  /**
   *
   * @return
   */
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
              precondition.validation_result ? "" : " precondition " +
              dc.validation_result ? "" : " dc " +
              postcondition.validation_result ? "" : " postcondition " +
              "block"
      )
    }
    return result
  }

  /**
   *
   * @param session
   * @return
   */
  def execute(session) {
    boolean result = true
    logger.info("Start executing container with name ${name}")

    for (precondition in preconditions) {
      result &= precondition.execute(session)
      logger.info("Precondition result is ${result}")
    }

    if (result) {
      result &= dc.execute(session)
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
}
