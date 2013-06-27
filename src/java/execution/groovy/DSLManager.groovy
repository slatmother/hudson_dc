package execution.groovy

import execution.groovy.container.DSLContainer
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

    return metaClass.container
  }

  def executeDC(session, DSLContainer container) {
    return container.validate() ? container.execute(session) : false
  }

  def validate(DSLContainer container) {
    return container.validate()
  }

  def rollback(DSLContainer container) {
    if (container.isAutoCommitDC) {
      container.rollback()
    }
  }

  def runScript(File dc, metaClass) {
    def groovyshell = new GroovyShell()

    def script = groovyshell.parse(dc);
    script.metaClass = metaClass;

    script.run();
  }
}
