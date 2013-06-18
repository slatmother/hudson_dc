package execution.groovy

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
class ExecutorDelegatingMetaClass extends DelegatingMetaClass {
  def logger = Logger.rootLogger
  boolean result = true
  def session

  ExecutorDelegatingMetaClass(Class theClass) {
    super(theClass)
  }

  @Override
  Object invokeMethod(Object object, String methodName, Object arguments) {
    if (result) {
      GroovyDSLExecutor executor = new GroovyDSLExecutor(session);
      result &= executor.invokeMethod(methodName, arguments)
      logger.warn("Execution result of method with name ${methodName} is ${result}")
    }

    return result;
  }
}
