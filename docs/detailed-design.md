Account (1) -> DataSet (n)
DataSet (1) -> ViewDef (n)

ViewDef - JSON representing the form rendering

DataSet -> id, title, description, and provider metadata

a separate table for each dataSet data, each row has a id.

Account -> userid, password and other user details.

Ex: Exam marks
==============

Account -> ssc/ssc1

DataSet -> ds1, "10th marks", "10th marks of 2014 batch"

ViewDef -> id:ds1-view1, title:"individual marks view", viewDef: json structure with list of field:properties
           id:ds1-view2, title:"data entry form", viewDef: json structure with list of field:properties
           id:ds1-view3, title:"search view", viewDef: json structure with list of field:properties


DataSet-Vallist map - ds1 - valRecord1, valrecord2,...
varRecord1 - hash map of key value pairs


APIs
====
1. All Datasets of an account
RHASHes with names ACCT.AcctID
2. All viewDefs of a dataset
3. all dataRecords of a dataset
4. View rendering - input - ViewDefId, dataRecord Id