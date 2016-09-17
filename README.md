# Openshift API

##Setup

POST **/setup/create_user**  

Create a new user


Parameters  
 
```
 {
    "user_name": "new_user",  
    "pet_name": "Tido",  
    "pass_word": "hiimemerson"  
  }  
```


POST **/setup/add_plaid**  

Setup plaid for the first time  

Parameters  

```
 {  
  "user_name": "rwr21",
  "bank_name": "Chase",  
  "bank_user_name": "mybankusername",     
  "bank_pass_word": "hiimemerson"  
  }  
```


POST **/setup/add_plaid**  #not finished waiting on emc67/ig11




##Friends

POST **/friends/add**

Add a friend

Parameters  


```
  {  
    "user_name": "rwr21",  
    "friend_user_name": "emc67" 
  }  
```

POST **/friends/find**   

Call this endpoint with a username, recieve all of that users friends as a list of key-values.

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

##Events












