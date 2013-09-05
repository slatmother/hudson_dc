package groovy.execution
import groovy.container.DSLContainer
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
  /**
   *
   * @param dc
   * @return
   */
  def static getDCMappingInst(File dc) {
    DSLContainer container = new DSLContainer(Script.class);
    container.name = dc.name

    runScript(dc, container)

    container
  }

  /**
   *
   * @param container
   * @param session
   * @return
   */
  def static executeDC(DSLContainer container, session) {
    boolean result = false

    if (validate(container)) {
      result = container.execute(session)
    }

    result
  }

  /**
   *
   * @param container
   * @return
   */
  def static boolean validate(DSLContainer container) {
    container.validate()
  }

  /**
   *
   * @param dc
   * @param metaClass
   * @return
   */
  private def static runScript(File dc, metaClass) {
    def groovyshell = new GroovyShell()

    def script = groovyshell.parse(dc);
    script.metaClass = metaClass;

    script.run();
  }
}
