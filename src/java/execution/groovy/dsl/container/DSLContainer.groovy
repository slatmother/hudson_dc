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

    logger.info("Precondition initialization block has run.")
    logger.info("Precondition properties: " +
            "\nQuery: " + prec.query +
            "\nRows: " + prec.rows
    )
    preconditions << prec
  }

  /**
   *
   * @param closure
   * @return
   */
  def dc(Closure closure) {
    dc.init(closure)

    logger.info("DC initialization block has run.")
    logger.info("DC properties: " +
            "\nQuery: " + dc.query +
            "\nMin val: " + dc.min +
            "\nMax val: " + dc.max
    )
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

    logger.info("Postcondition initialization block has run.")
    logger.info("Postcondition properties: " +
            "\nQuery: " + post.query +
            "\nRows: " + post.rows
    )

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

              preconditions.each {
                it.validation_result
              } ? "" : " precondition " +

              dc.validation_result ? "" : " dc " +

              postconditions.each {
                it.validation_result
              } ? "" : " postcondition " +
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
