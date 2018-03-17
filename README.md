# HAS-Client-Side
The client side of the HAS (Hospital Alert System) project for the hospital center of Valenciennes

## Installation

To deploy the client application, just build it with Maven. To do so, use the following command in a terminal executed at the root of the HAS-Client-Side sources :  

`mvn install -Drabbitmq_uri=YOUR_RABBIT_MQ_URI`  

Just replace the parameter value of `rabbitmq_uri` by the RabbitMQ url you used when configuring HAS-Server-Side. 