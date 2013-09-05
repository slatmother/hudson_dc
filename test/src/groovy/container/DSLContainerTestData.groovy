package groovy.container

import org.testng.annotations.DataProvider

/*
* $Id
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
class DSLContainerTestData {
  @DataProvider(name = "sqlTestData")
  public Object[][] sqlTestData() {
    [
            [
                    '''
                          precondition {
                              sql "select test_id, test_name from test_table where test_id = 1"
                          }

                          dc {
                              sql \''' insert into test_table values(1, 'test') \'''

                              condition min: 1, max: 2
                          }

                          postcondition {
                              sql "select test_id, test_name from test_table where test_id = 1"

                              row "TEST_ID": 1, "TEST_NAME": "test"
                          }
                      ''',
                    3,
                    'sql',
                    true
            ],
            [
                    '''
                       precondition {
                         dql "select count(*) as c from ddt_choice where dss_dmtype = 'dm_sysobject' and dss_attr = 'test'"
                         row "c": 0
                       }

                       dc {
                         dql \'''create ddt_choice object
                         set dss_dmtype = 'dm_sysobject',
                         set dss_attr = 'test',
                         set dss_label = 'Test',

                         set drs_attr_value[0] = 'Test_val_1',
                         set drs_attr_label[0] = '1',
                         set drb_default[0] = false

                         set drs_attr_value[1] = 'Test_val_2',
                         set drs_attr_label[1] = '2',
                         set drb_default[1] = false\'''

                         condition min: 0
                       }

                       postcondition {
                         dql "select drs_attr_value, drs_attr_label from ddt_choice where dss_dmtype = 'dm_sysobject' and dss_attr = 'test' and dss_label = 'Test' order by drs_attr_value"

                         row "drs_attr_value": ["Test_val_1"], "drs_attr_label": ["1"]
                         row "drs_attr_value": ["Test_val_2"], "drs_attr_label": ["2"]
                       }
                      ''',
                    3,
                    'dql',
                    true
            ]
    ]
  }

  @DataProvider(name = "dqlTestData")
  public Object[][] dqlTestData() {
    [
            [
                    '''
                         precondition {
                           dql "test_query"
                           row "c": 0
                         }

                         dc {
                           dql \''' test_dql_query\'''

                           condition min: 0
                         }

                         postcondition {
                           dql "test_query"

                           row "drs_attr_value": ["Test_val_1"], "drs_attr_label": ["1"]
                         }
                        ''',
                    3,
                    'dql',
                    true
            ]
    ]
  }
}
