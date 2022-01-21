Aby uruchomić aplikację należy wykonać następujące polecenie:

``docker-compose build``

A następnie

``docker-compose up -d``

Aplikacja uruchomi się pod adresem:

``localhost:8080``

Aplikacja wystawia następujący endpoint:

``localhost:8080/customers/getById?customerId={list|ALL}``