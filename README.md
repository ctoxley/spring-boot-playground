# Spring Boot Playground

### Run application

    $ gradle bootRun
    
### API

##### Health

Request:

    GET /health

Response:

    {
        "status": "up"
    }
    
##### Email

Request:

    POST /email
    Content-type: application/json
    
    { "to": "email@tosend.com", 
      "subject": "subject", 
      "body": "body", 
      "attachments": [
         { "name": "file",
           "extension": ".txt",
           "content": "content"
         }
      ]
    }

Response:

    CREATED
    
#### MailHog

    $ docker run --name mailhog-instance --rm -p 1025:1025 -p 8025:8025 mailhog/mailhog
    
MailHog UI: http://localhost:8025