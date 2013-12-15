Goalmeister API
===============

This is the web API for Goalmeister.

Benchmarks
==========

Baseline benchmark taken on the Ping service on Dec 15:

```
apib -w 5 -c 250 -d 20 http://localhost:8080/goalmeister/ping

Warming up: (5 / 5) 2133.699 0% cpu
(5 / 20) 2154.670 0% cpu
(10 / 20) 2176.255 0% cpu
(15 / 20) 2134.635 0% cpu
(20 / 20) 2049.800 0% cpu
Duration:             20.008 seconds
Attempted requests:   42593
Successful requests:  42593
Non-200 results:      0
Connections opened:   0
Socket errors:        0

Throughput:           2128.836 requests/second
Average latency:      117.339 milliseconds
Minimum latency:      14.155 milliseconds
Maximum latency:      518.690 milliseconds
Latency std. dev:     49.175 milliseconds
50% latency:          90.587 milliseconds
90% latency:          173.935 milliseconds
98% latency:          193.756 milliseconds
99% latency:          206.876 milliseconds

Client CPU average:    0%
Client CPU max:        0%
Client memory usage:    0%

Total bytes sent:      3.98 megabytes
Total bytes received:  5.38 megabytes
Send bandwidth:        1.59 megabits / second
Receive bandwidth:     2.15 megabits / second
```
