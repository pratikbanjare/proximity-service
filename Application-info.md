# Overview

A spring web app, Proximity service to identify nearby business in map. 


# Requirements 

## Database 
- Business Table 
- Geohash Table 

### Business Table 

| business_id | business_name | lattitute | longitute |
|-------------|---------------|-----------|-----------|
|1            | Iyengers Bakery | 45677 | 234234324|


### Geohash table 

|geohash_id|business_id|
|----------|-----------|
|1         |1          |


## Endpoints 

1. /business/{id}
    - GET : get details of business ID from database 
    - POST : Add a new business to Database 
    - PUT : Update existing or add new entry of busines to database 
    - DELETE : Delete an existing business from database 

2. GET : /nearby/search/{lat}/{long} - location from where to look for nearby business 



# Technical Specifications 

Language - Java 

Use in memory database for initial development runs and testing.

Framework - Spring boot, Spring web

