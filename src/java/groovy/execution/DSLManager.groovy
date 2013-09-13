package groovy.execution
import groovy.container.DSLContainer

/**
 * $Id
 * <p>Title: Менеджер для контейнеров</p>
 * <p>Description:
 * </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 13.05.13</p>
 *
 * @version 1.0
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
