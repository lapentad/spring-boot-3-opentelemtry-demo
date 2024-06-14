bash start.sh

curl http://localhost:8091/web/notrace
Will use a WebClient webflux to call the other service "NoTrace" on an endpoint,
and that call is traced

Similar it can be done for MVC RestTemplate
