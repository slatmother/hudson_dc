Автоматическое выполнение DC.
Целью проекта является создание исполняемого модуля(jar) для автоматизации исполния DC скриптов, dql и sql в Hudson


На момент создания данного описания, предполагаемая структура DC такая:

precondition {
    sql "select test_id, test_name from test_table where test_id = 1"
    <-- empty row-->
}

dc {
    sql ''' insert into test_table values(1, 'test') '''

    condition min: 1, max: 2
}

postcondition {
    sql "select test_id, test_name from test_table where test_id = 1"

    row "TEST_ID": 1, "TEST_NAME": "test"
}


