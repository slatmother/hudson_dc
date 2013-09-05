package groovy.container

import groovy.script.block.DC
import groovy.script.block.PostCondition
import groovy.script.block.PreCondition
import groovy.script.core.AbstractBlock
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
class DSLContainer extends DelegatingMetaClass {
  private final def logger = Logger.rootLogger

  def blocs = []

  String name
  String dcQueryType
  private boolean containsDCBlock = false


  DSLContainer(Class delegate) {
    super(delegate)
  }

  @Override
  Object invokeMethod(Object object, String methodName, Object arguments) {
    this.invokeMethod(methodName, arguments)
  }

  /**
   *
   * @param closure
   * @return
   */
  def precondition(Closure closure) {
    AbstractBlock pre = new PreCondition()
    pre.init(closure)

    if (logger.isDebugEnabled()) {
      logger.info("Precondition initialization block has been completed.\n" + pre.info())
    }

    blocs << pre
  }

  /**
   *
   * @param closure
   * @return
   */
  def dc(Closure closure) {
    AbstractBlock dc = new DC()
    dc.init(closure)

    dcQueryType = dc.queryType

    blocs << dc
    containsDCBlock = true

    if (logger.isDebugEnabled()) {
      logger.info("DC initialization block has been completed.\n" + dc.info())
    }
  }

  /**
   *
   * @param closure
   * @return
   */
  def postcondition(Closure closure) {
    AbstractBlock post = new PostCondition()
    post.init(closure)

    if (logger.isDebugEnabled()) {
      logger.info("Postcondition initialization block has been completed.\n" + post.info())
    }

    blocs << post
  }

  /**
   *
   * @return
   */
  def validate() {
    boolean result = true;

    for (block in blocs) {
      result &= block.validate()

      if (!result && logger.isDebugEnabled()) {
        logger.info("Validation failed! ${name}\n Check syntax of block with properties ${block.info()}")
      }
    }

    result
  }

  /**
   *
   * @param session
   * @return
   */
  def execute(session) {
    boolean result = true
    logger.info("Start executing container with name ${name}")

    for (block in blocs) {
      if (result) {
        result &= block.execute(session)

        if (logger.isDebugEnabled()) {
          logger.info("Block ${block.name} execution result is ${result}.")
        }
      } else {
        break
      }
    }

    result
  }

  boolean equals(o) {
    if (this.is(o)) return true
    if (!(o instanceof DSLContainer)) return false

    DSLContainer container = (DSLContainer) o

    if (name != container.name) return false

    return true
  }

  int hashCode() {
    name.hashCode()
  }
}
