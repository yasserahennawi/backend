# backend

**Api Documentation**

GET localhost:4567/ 
return Hello World!
---------------------------------------------------------------------------------------------------------
GET localhost:4567/api/users/session 
No Authorization Header
return null
---------------------------------------------------------------------------------------------------------
GET localhost:4567/api/users/session 
With JWTToken in Authorization Header
return userData Json
{
  "displayName": "Omar",
  "pictureUrl": "https:picture",
  "appUserID": "11224354654"
}
---------------------------------------------------------------------------------------------------------
GET localhost:4567/api/users/login
redirect http://127.0.0.1:8080?token=JWTToken
---------------------------------------------------------------------------------------------------------
GET localhost:4567/api/users/:appUserID/crushes/count
With JWTToken in Authorization Header
return int number of crushes/count
---------------------------------------------------------------------------------------------------------
GET localhost:4567/api/users/:appUserID/crushes-on-me-count
With JWTToken in Authorization Header
return int number of crushes on that user
---------------------------------------------------------------------------------------------------------
GET localhost:4567/api/users/:appUserID/crushes
With JWTToken in Authorization Header
retutn json array of crushes
[
  {
    "appUserID": "1254564",
    "fbCrushID": "10546464606502"
  },
{
    "appUserID": "1254sda564",
    "fbCrushID": "10546454464606502"
  },
{
    "appUserID": "1254325564",
    "fbCrushID": "105464gfgsda64606502"
  }
]
---------------------------------------------------------------------------------------------------------
POST localhost:4567/api/users/:appUserID/crushes
With JWTToken in Authorization Header
and body url > for example https://www.facebook.com/yasser.amrii/about?lst=1029643632%3A100004191306502%3A1490888352
return json crush
{
    "appUserID": "10234323",
    "fbCrushID": "105654654502"
}
and if the url is invalid > return null
----------------------------------------------------------------------------------------------------------
DELETE localhost:4567/api/users/:appUserID/crushes/:fbCrushID
With JWTToken in Authorization Header
return Json of the deleted crush
-----------------------------------------------------------------------------------------------------------
Any API that demand authorization header and JWTtoken wasn't sent will return "401 Unauthorized"
Also :appUserID should match with JWTToken

