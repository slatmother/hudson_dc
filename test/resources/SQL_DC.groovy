precondition {
  sql "select test_id from test_table where test_id = '17'"
}

dc {
  sql "insert into test_table values(17, 'test')"
  affected 1
}

precondition {
  sql "select test_id from test_table where test_id = '17'"

  row "TEST_ID": 17
}

dc {
  sql "delete from test_table where test_id in ('17')"
  affected 1
}

precondition {
  sql "select test_id from test_table"

  row "TEST_ID": 100
  row "TEST_ID": 15
  row "TEST_ID": 16
}

