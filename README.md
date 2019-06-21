# Chapter 3.b: The EventBus

As frontends grow, there sooner or later will always be a need for sending messages between separated parts of the application. To prevent class dependency hell, it is better to decouple those parts since they do not need know each other, but make them exchange messages anonymously via event bus.

Cotton offers a build-in event bus, where subscribing beans to that bus are automatically hooked on and of with their life cycle.

## Sending events

