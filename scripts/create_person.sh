#!/bin/sh
create_index(){
	curl -X PUT "192.168.65.11:9200/t_person?pretty" -H 'Content-Type: application/json' -d'
	{
	  "mappings": {
		"properties": {
			"name": {
				"type": "keyword"
			},
			"birthday": {
				"type": "date",
				"format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
			},
			"address": {
				"type": "text"
			},
			"createdTime": {
				"type": "date",
				"format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
			},
			"modifiedTime": {
				"type": "date",
				"format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
			}
		}
	  }
	}
	'
}

add_alias(){
	curl -X POST "192.168.65.11:9200/_aliases?pretty" -H 'Content-Type: application/json' -d'
	{
		"actions" : [
			{ "add" : { "index" : "t_person", "alias" : "a_person" } }
		]
	}
'
}


create_index
echo -e "\ncreate_index ok"
sleep 1

add_alias
echo -e "\nadd_alias ok"






