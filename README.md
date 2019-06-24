# drools-spring-boot
demo of drools rules engine in a spring boot application

HTTP POST request:

{
"name" : "twesh",
"cardType" : "HDFC",
"price" : "10001"
}

HTTP response:
(200)

{
    "name": "twesh",
    "cardType": "HDFC",
    "discount": 10, <---- discount is applied after rule file is loaded from classpath
    "price": 10001
}
