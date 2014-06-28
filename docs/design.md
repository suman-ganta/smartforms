Data Definition
===============
Field - id, label, dataType, optionality, readOnly/readWrite, Visible/Invisible, listOfValues [ACL]

EntityDef - id, name, List<Field>

User - List<Entity>


Data instances
==============
{ 
 entityDef : '',
 fields : {'f1' : 'myval', 'f2' : 'myval2'}
}

REST interfaces

POST data instance
==================
POST /forms/<entityDefId>
{ 
 entity : '',
 fields : {'f1' : 'myval', 'f2' : 'myval2'}
}

Response Body - EntityInstanceId

Read-Only Views
================
View1
<search box>
<table of results> - accordian with revealing summary

Bindings - Contain binding expressions dot notations of data instances. And each of these forms are associated to one entityInstance

View2
Details - a table of labels and values

Form views
==========
Palette - Show all relevant data fields associated to selected entityDef.



References
==========
Data Sets (DSPL) - https://developers.google.com/public-data/docs/tutorial