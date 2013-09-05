package groovy.container
import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.testng.annotations.Test
/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
class DSLContainerTest {

  @Test(dataProvider = "testData", dataProviderClass = DSLContainerTestData.class)
  public void testContainerAsMetaClass(scriptBody, blockSize, dcType, validationResult) throws Exception {
    Logger.getRootLogger().setLevel(Level.DEBUG)

    assert Logger.getRootLogger().isDebugEnabled()

    // configure the base script class
//    def cc = new CompilerConfiguration()
//    cc.scriptBaseClass = DelegatingScript.class.name

    // parse script with GroovyShell
    // and the configuration
//    def sh = new GroovyShell(cc)
    def sh = new GroovyShell()

    def script = sh.parse(scriptBody)
    def container = new DSLContainer(Script.class)
    container.name = "test"

    script.metaClass = container

    // set the delegate and run the script
//    def p = new DSLContainer(Script.class)
//    script.setDelegate(p)
    script.run()

    // the name is set correctly
    // and the output will display "Hi Guillaume"
    assert container.blocs.size() == blockSize

    assert container.dcQueryType == dcType


    assert container.validate() == validationResult
  }
}
