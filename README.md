# Game save's store
My diplom project. Here I wrote a server app, my colleague and friend Marsel wrote client part. This is a project where people can add, update, delete their 
game saves. User can register, login, change password, add game save files of chosen game. In a client this game state divides on parameters, which used for making a graphics.
This app supposed to be serving by admins, whose tasks are to fill the existing games, form game state parameters for them and mark common parameters which will help in graphic building.
User can be banned and unbanned by admin. Users can share by their game saves with another users. User can make his game states public in order to every user could see, download and use his game save files.


Server part consists of: 
- controllers (here i define endpoints and recieve and response data using REST API)
- repositories (communicating with database)
- services (responsible for business logic. Here occurs pagination, logic of saving, updating, deleting data in database)
- DTO's (files which help client and server share only required data)
- models (classes of entities, where defined fields related with database tables)
- validators (classes which were used in controller to validate entities before processing them (for example checking if entity unique))

Also here you can find pagination for every required entity, which will be depicted on a client side, search processing, also work with mail. It required to change a password for unauthorized person.

## Postman 
I use postman to check if my code works right. I have every endpoint as a request in there.
![image](https://github.com/Ki-Really/GameSavesStore/assets/133647432/6c26cea0-f5f6-4fca-8276-ca57ba64026e)

## Minio
I use minio S3 for saving images of games and user's saving archives on a server. Data saves into special folders (buckets). They can be extracted and returned to a user.
![image](https://github.com/Ki-Really/GameSavesStore/assets/133647432/603a0f1a-9d1a-4212-8a78-53c2191f24d6)

## Database structure
![image](https://github.com/Ki-Really/GameSavesStore/assets/133647432/6c0900a6-7c15-4856-89a7-bcb4bc9585bd)



## Docker
Here I can run a whole project, but in a fact only Minio store placed in here using docker-compose and Dockerfile files.
![image](https://github.com/Ki-Really/GameSavesStore/assets/133647432/ec37db8c-ab0a-4b1b-915b-c3864531f37f)


