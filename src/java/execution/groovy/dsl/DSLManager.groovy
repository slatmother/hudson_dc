package execution.groovy.dsl

import execution.groovy.dsl.container.DSLContainer
import execution.groovy.metaclass.DSLDelegatingMetaClass

/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
class DSLManager {

  def getDCMappingInst(File dc) {
    DSLDelegatingMetaClass metaClass = new DSLDelegatingMetaClass(Script.class)
    runScript(dc, metaClass)
    metaClass.container.name = dc.name
    return metaClass.container
  }

  def executeDC(session, DSLContainer container) {
    boolean result = false
    if (validate(container)) {
      result = container.execute(session)
    }

    return result
  }

  def validate(DSLContainer container) {
    return container.validate()
  }

  def rollback(DSLContainer container) {
    container.rollback()
  }

  def runScript(File dc, metaClass) {
    def groovyshell = new GroovyShell()

    def script = groovyshell.parse(dc);
    script.metaClass = metaClass;

    script.run();
  }
}
