# Spring Security Example

This spring boot application resumes the tutorial of "javainuse" \
See the original tutorial at this link : https://www.javainuse.com/spring/boot-jwt

## How does it work ? 
In our case, we have two endpoints : 
> /authenticate - not secured\
> /hello - secured\

In order to call `/hello`, you have to provide a valid token. Otherwise, we obtain an Authorized exception\
To generate this, you have to call `/authenticate` with your login and password

Then, when you get a token, you can call `/hello` with an `Authorization` header which it will start by Bearer and then THE valid token.

## What is a token ?
A token is an encrypted key with :
- a _header_, in this, we have a type : JWT and a hashing algorithm : **HS512**
- a _payload_, in our case, we have a **subject** : username, a **issuer date** : date you log in and an **expiration date**
- a _signature_, this is a hash of the header and the payload using the hashing algorithm

:warning: The payload is visible by every one, so you don't put a sensible data.

## Use case
At the launch of application, you enable and configure Spring Web Security with this class : `WebSecurityConfig`\
For each HTTP Call, we trigger an Request Filter : `JwtRequestFilter`., it verify that you provide a valid token. 
If the token are valid, then you set the Spring Security Context.
 
© Grégory TAILLY - 2020

