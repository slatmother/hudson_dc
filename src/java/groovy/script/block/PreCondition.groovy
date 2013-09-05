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

class PreCondition extends AbstractBlock {
  final String name = "Precondition"

  def rows = []

  def row(Map<String, Object> rowMap) {
    rows << rowMap
  }

  def execute(session) {
    return ProjectTrScript.query(session, query, rows)
  }

  @Override
  def info() {
    name + ":\n" +
            "Query: " + query +
            "Rows: " + rows
  }
}
