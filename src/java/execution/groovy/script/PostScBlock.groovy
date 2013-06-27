package execution.groovy.script

/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
class PostScBlock {
  def query
  def rows = []

  def init(Closure closure) {
    closure.delegate = this
    closure.call()

    if (rows.size() == 0) {
      rows << Collections.<String, Object> emptyMap()
    }
    return validate()
  }

  def sql(String query) {
    this.query = query
  }

  def dql(String query) {
    this.query = query
  }

  def row(Map<String, Object> rowMap) {
    rows << rowMap
  }

  def validate() {
    def result = true
    result &= ((query == null) || (query.equals('')))

    return result;
  }
}
