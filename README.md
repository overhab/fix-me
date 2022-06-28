# fix-me
Simulation of stock exchanges, with networking and socket implementations.
## Router
Central component. All other components connect 
to it in order to send messages to other components.

Router listens on two ports: `5000` (Broker) and `5001` (Market).

Main functionality of router:
- Validate the message based on the checkshum.
- Identify the destination in the routing table.
- Forward the message.

## Broker
The Broker sends two types of messages: to buy or to sell.

## Market
A market has a list of instruments that can be traded. When orders are received from
brokers the market tries to execute it. 
