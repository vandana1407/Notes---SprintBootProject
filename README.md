# NotesProject - used Spring boot
Various Notes aded, deleted, searched and updated can be seen in H2 database - 
In memory h2 db - http://localhost:8082/h2-console
username-sa , password - empty

Functionality covered:
 You should be able to return all notes.
 You should be able to add one or more notes.
 You should be able to edit a note.
 You should be able to delete one or more notes.
 You should be able to search notes.
 The notes should be persisted and retrieved via the service.

Tested apis in Postman

Various api calls can be made as below:

Get - http://localhost:8082/api/notes/load
loads 5 records in notes table

get all notes - get - http://localhost:8082/api/notes
get by user  - http://localhost:8082/api/notes/byuser/user1
get by title - http://localhost:8082/api/notes/bytitle/title3
search by keyword - http://localhost:8082/api/notes/search/spring  - searches if word in text or title of notes
http://localhost:8082/api/notes/search/Spring


Add note - Post - http://localhost:8082/api/notes
{
        "title": "title6",
        "createdBy": "user6",
        "text": "Notes on Spring Data JPA"
    }
multipel notes add - post - http://localhost:8082/api/notes/addnotes
[
    {
        "title": "title7",
        "createdBy": "user7",
        "text": "Notes on hibernate"
    },
    {
        "title": "title8",
        "createdBy": "user8",
        "text": "Notes on JPA"
    }

edit note -PUT 
http://localhost:8082/api/notes/1 - 
   {
        "id":1,
        "title": "title7updated",
        "createdBy": "user7",
        "text": "Notes on hibernate"
    }

delete - DELETE - http://localhost:8082/api/notes/1 - delteby id
delete by user - http://localhost:8082/api/notes/byuser/user3
delete by title - http://localhost:8082/api/notes/bytitle/title4


Also Included NotesControllerIntegrationTests and 100% tests pass.
