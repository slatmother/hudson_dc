package groovy.script.data

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
class DCTestData {
  @DataProvider
  public static Object[][] correct() {
    [
            [
                    '''
                      dql \'''dql_query \'''
                      condition min: 0, max: 1
                    ''',
                    'dql',
                    0,
                    1
            ],
            [
                    '''
                      dql \'''dql_query \'''
                      condition min: 1, max: 1
                    ''',
                    'dql',
                    1,
                    1
            ],
            [
                    '''
                       dql \'''dql_query \'''
                       condition min: 1
                    ''',
                    'dql',
                    1,
                    null
            ],
            [
                    '''
                       dql \'''dql_query \'''
                       condition min: 100
                    ''',
                    'dql',
                    100,
                    null
            ],
            [
                    '''
                                           sql \'''sql_query \'''
                                           condition min: 100
                    ''',
                    'sql',
                    100,
                    null
            ]
    ]
  }

  @DataProvider
  public static Object[][] wrong() {
    [
            [
                    '''
                       condition min: 0, max: 1
                    '''
            ],
            [
                    '''
                        dql \''' \'''
                        condition min: 0, max: 1
                    '''
            ],
            [
                    '''
                         dql \'''dql_query \'''
                         condition max: 1
                    '''
            ]
    ]
  }
}
