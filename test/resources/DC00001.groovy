precondition {
  dql "select count(*) as c from ddt_birt_report where dss_type='RENAMED_ACC_FOLDERS_REPORT'"

//  row "c": 0
}

dc {
  dql "CREATE ddt_birt_report object\n" +
          "SET object_name='Отчет об изменении наименований пакетов'\n" +
          "SET dss_type='RENAMED_ACC_FOLDERS_REPORT'\n" +
          "SET dss_url='http://172.21.103.182:8080/birt/run?__report=renamed_acc_folder_report.rptdesign&__format=xls'\n" +
          "LINK '/CustomReports'"

  affected 1
}

precondition {
  dql "select count(*) as c from ddt_birt_report where dss_type='RENAMED_ACC_FOLDERS_REPORT'"

  row "c": 1
}