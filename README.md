# openshift

##setup

POST /setup/create_user


Parameters  
 
```
 {
    "user_name": "new_user",  
    "pet_name": "Tido",  
    "pass_word": "hiimemerson"  
  }  
```


POST /setup/add_plaid  
Parameters  

```
 {  
  "user_name": "rwr21",
  "bank_name": "Chase",  
  "bank_user_name": "mybankusername",     
  "bank_pass_word": "hiimemerson"  
  }  
```


POST /setup/add_plaid  #not finished waiting on emc67/ig11




##Friends

POST /friends/add

Parameters  


```
  {  
    "user_name": "rwr21",  
    "friend_user_name": "emc67" 
  }  
```

POST /friends/find

Parameters  


```
 {  
    "user_name": "rwr21"  
 }  
```

Example Response

```
   [{"user_name": "emc67"}, 
    {"user_name": "ajs262"}]
``` 












