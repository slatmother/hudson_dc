package groovy.script.block

import groovy.script.core.AbstractBlock
import transaction.script.ProjectTrScript

/**
 * $Id
 * <p>Title: Блок ДС</p>
 * <p>Description:
 * </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 13.05.13</p>
 *
 * @version 1.0
 */
class DC extends AbstractBlock {
  final String name = "DC"
  def min
  def max
  String queryType

  /**
   *
   * @param query
   * @return
   */
  def sql(String query) {
    super.sql(query)

    queryType = "sql"
  }

  /**
   *
   * @param query
   * @return
   */
  def dql(String query) {
    super.sql(query)

    queryType = "dql"
  }

  /**
   *
   * @param map
   * @return
   */
  def condition(Map<Integer, Integer> map) {
    this.min = map.min
    this.max = map.max
  }

  /**
   *
   * @return
   */
  def validate() {
    super.validate() && min != null
  }

  /**
   *
   * @param session
   * @return
   */
  def execute(session) {
    ProjectTrScript.query(session,
            query,
            min,
            max)
  }

  @Override
  def info() {
    name + ":\n" +
            "Query: " + query +
            "Min: " + min +
            "Max: " + max
  }
}
