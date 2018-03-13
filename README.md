Work in progress: Spring `@HystrixCommand` combined with RxJava `Single`.

This little project has three endpoints: one to capitalize a name, one to create a greeting text
 based on the time of day, and one to create a greeting based on the output of the other two.

Since the capitalize and greet text endpoints can be slow and unreliable, the main hello endpoint
 is calling them using services which wrap the REST calls in `@HystrixCommand` annotations 
 to provide fallback values.

This project demonstrates how to have the Hystrix calls return an RxJva `Single`, and 
how to combine these in the main endpoint so that the services are called in parallel. 

There is extensive logging to help visualize what is happening in which thread, and how timeouts 
are handled.
