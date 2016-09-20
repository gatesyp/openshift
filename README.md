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

POST **/setup/login**  

Login (only needs done if user did not just make an account) 


Parameters  
 
```
 {
    "user_name": "hi_im_steven",  
    "pass_word": "emerson_lol_jk"  
  }  
 ```
 
Example Response

If password is correct   
```
True
```

If password is incorrect   
```
False
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
{
  "friends": [
    {
      "user_name": "emc67"
    },
    {
      "user_name": "ajs262"
    }
  ]
}
``` 


POST **/friends/find_advanced**  
Get all friend data

Parameters 
```
 {  
    "user_name": "rwr21"  
 }  
```


Example Response


```
[
  {
    "data": {
      "username": "emc67",
      "new_events": [
        null
      ],
      "percent_to_lvl": 0,
      "last_week_data": {
        "current_pet_state": "happy",
        "future_pet_state": "happy",
        "last_week_xp": {
          "nutrition": 0,
          "social": 0,
          "sleep": 0,
          "fitness": 0
        }
      },
      "pet_level": 1
    }
  },
  {
    "data": {
      "username": "ajs262",
      "new_events": [
        null
      ],
      "percent_to_lvl": 0,
      "last_week_data": {
        "current_pet_state": "happy",
        "future_pet_state": "happy",
        "last_week_xp": {
          "nutrition": 0,
          "social": 0,
          "sleep": 0,
          "fitness": 0
        }
      },
      "pet_level": 1
    }
  },
  {
    "data": {
      "username": "test",
      "new_events": [
        null
      ],
      "percent_to_lvl": 0,
      "last_week_data": {
        "current_pet_state": "happy",
        "future_pet_state": "happy",
        "last_week_xp": {
          "nutrition": 0,
          "social": 0,
          "sleep": 0,
          "fitness": 0
        }
      },
      "pet_level": 1
    }
  }
]
```


##Events

POST **/events/plaid**   

Check for new events with plaid ###Don't use this

Parameters  

```
 {  
    "user_name": "rwr21"  
 }  
```

POST **/events/get_new**   

Call this when opening plaid, it checks if any new plaid events happened since last refresh

Parameters  

```
 {  
    "user_name": "rwr21"  
 }  
```

Example Response  if new event is found

```
{
  "event_list": [
    {
      "message": "fluffy got plenty of exercise yesterday and as a result he is very happy!"
    },
    {
      "message": "fluffy feels great today! It must be from getting enough sleep. +250 xp"
    }
  ],
  "level": 1954
}
```


Example Response if a new event is NOT found 
```
{
  "event_list": null,
  "level": 1954
}
```

POST **/events/get_last_few**

Get last events for a user. Specify username and the number events that you want as parameters. 

Parameters 
```
  {  
    "user_name": "rwr21",  
    "num": "2" 
  }  
  ```


Example Response if you request two events

```
{
  "get_last_events": [
    {
      "category": "fitness",
      "current_xp": "5806",
      "user_name": "rwr21",
      "pet_status": "fit",
      "xp_change": "303"
    },
    {
      "category": "fitness",
      "current_xp": "5236",
      "user_name": "rwr21",
      "pet_status": "fit",
      "xp_change": "423"
    }
  ]
}
```









