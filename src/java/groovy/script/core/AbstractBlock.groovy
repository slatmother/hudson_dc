package groovy.script.core

/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
abstract class AbstractBlock implements IBlock {
  public String query

  @Override
  public def init(Closure closure) {
    closure.delegate = this
    closure.call()
    validate()
  }

  public def sql(String query) {
    this.query = query
  }

  public def dql(String query) {
    this.query = query
  }

  @Override
  def validate() {
    query != null && !query.isAllWhitespace()
  }
}
