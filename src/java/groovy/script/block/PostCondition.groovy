package groovy.script.block

import groovy.script.core.AbstractBlock
import transaction.script.ProjectTrScript

/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
class PostCondition extends AbstractBlock {
  final String name = "Postcondition"
  def rows = []

  /**
   *
   * @param rowMap
   * @return
   */
  def row(Map<String, Object> rowMap) {
    rows << rowMap
  }

  /**
   *
   * @param session
   * @return
   */
  def execute(session) {
    ProjectTrScript.query(session, query, rows)
  }

  @Override
  def info() {
    name + ":\n" +
            "Query: " + query +
            "Rows: " + rows
  }
}
