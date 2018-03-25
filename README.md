# HAS-Client-Side
The client side of the HAS (Hospital Alert System) project for the hospital center of Valenciennes

## Installation

To deploy the client application, just build it with Maven. To do so, use the following command in a terminal executed at the root of the HAS-Client-Side sources :  

`mvn clean install package -Drabbitmq_uri=YOUR_RABBIT_MQ_URI`  

Just replace the parameter value of `rabbitmq_uri` by the RabbitMQ url you used when configuring HAS-Server-Side.
 
 Once the project is successfully built, just go to the `target` directory of the project, and use the jar file ending by `jar-with-dependencies.jar` to launch the app.  
 
 Then, feel free to copy and launch this `jar` file to every computer you want to connect to your HAS server :-)