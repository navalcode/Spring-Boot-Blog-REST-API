#!/bin/sh

while ! nc -z blogapi-db 3306; do
    echo "Waiting for the Mysql Server"
    sleep 3
done

#java -jar hello-app.jar
#ENTRYPOINT ["java","-cp","app:app/lib/*","com.sopromadze.blogapi.BlogApiApplication"]
java -Dspring.profiles.active=prod -cp app:app/lib/* com.sopromadze.blogapi.BlogApiApplication 