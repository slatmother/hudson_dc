package execution.groovy
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

  static def executeDCScript(session, File dc) {
    ExecutorDelegatingMetaClass scriptMetaClass = new ExecutorDelegatingMetaClass(Script.class);
    scriptMetaClass.session = session;

    Binding binding = new Binding()
    def groovyshell = new GroovyShell(binding)

    def script = groovyshell.parse(dc);
    script.metaClass = scriptMetaClass;
    script.run();

    return scriptMetaClass.result
  }
}
