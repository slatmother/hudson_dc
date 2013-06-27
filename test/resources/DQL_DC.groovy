precondition {
  dql "select count(*) as c from dm_user where user_name like '%test%'"

  row "c": 2
}

dc {
  dql "update dm_user object " +
          "set user_os_name = 'testOS' " +
          "where user_name like '%test%'"

  affected 2
}

precondition {
  dql "select user_os_name, user_name from dm_user where user_name like '%test%'"

  row "user_os_name": "testOS", "user_name": "test_account2"
  row "user_os_name": "testOS", "user_name": "test_account"
}

dc {
  dql "update dm_user object " +
          "set user_os_name = '' " +
          "where user_name like '%test%'"

  affected 2
}

precondition {
  dql "select user_os_name, user_name from dm_user where user_name like '%test%'"

  row "user_os_name": "", "user_name": "test_account2"
  row "user_os_name": "", "user_name": "test_account"
}