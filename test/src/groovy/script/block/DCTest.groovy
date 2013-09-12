package groovy.script.block

import groovy.script.data.DCTestData
import org.codehaus.groovy.control.CompilerConfiguration
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
class DCTest {
  @Test(dataProvider = 'correct', dataProviderClass = DCTestData.class)
  public void testCorrectParams(scriptBody, queryType, minVal, maxVal) throws Exception {
    def cc = new CompilerConfiguration()
    cc.scriptBaseClass = DelegatingScript.class.name

    // parse script with GroovyShell
    // and the configuration
    def sh = new GroovyShell(cc)
    def script = sh.parse(scriptBody)

    // set the delegate and run the script
    def dc = new DC()
    script.setDelegate(dc)
    script.run()

    assert dc.queryType == queryType
    assert dc.min == minVal
    assert dc.max == maxVal
    assert dc.validate()
  }

  @Test(dataProvider = 'wrong', dataProviderClass = DCTestData.class)
  public void testWrongParams(scriptBody) throws Exception {
    def cc = new CompilerConfiguration()
    cc.scriptBaseClass = DelegatingScript.class.name

    // parse script with GroovyShell
    // and the configuration
    def sh = new GroovyShell(cc)
    def script = sh.parse(scriptBody)

    // set the delegate and run the script
    def dc = new DC()
    script.setDelegate(dc)
    script.run()

    assert !dc.validate()
  }

}
