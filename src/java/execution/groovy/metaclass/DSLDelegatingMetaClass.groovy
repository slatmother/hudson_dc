package execution.groovy.metaclass

import execution.groovy.container.DSLContainer
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
class DSLDelegatingMetaClass extends DelegatingMetaClass {
  def logger = Logger.rootLogger
  boolean result = true
  DSLContainer container = new DSLContainer();

  DSLDelegatingMetaClass(Class theClass) {
    super(theClass)
  }

  @Override
  Object invokeMethod(Object object, String methodName, Object arguments) {
    container.invokeMethod(methodName, arguments)
    return result;
  }
}
