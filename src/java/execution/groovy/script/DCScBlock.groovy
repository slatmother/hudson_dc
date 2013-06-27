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
class DCScBlock {
  String query
//  def minVal
//  def maxVal

  def affected

  def init(Closure closure) {
    closure.delegate = this
    closure.call()
  }

  def sql(String query) {
    this.query = query
  }

  def dql(String query) {
    this.query = query
  }

//  def condition(Integer minVal, Integer maxVal) {
//    this.minVal = minVal
//    this.maxVal = maxVal
//  }

  def affected(Integer value) {
    affected = value
  }

  def validate() {
    def result = true
    result &= ((query == null) || (query.equals('')) && affected >= 0)
    return result;
  }
}
