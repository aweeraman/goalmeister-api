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


---


Performance of simple fetch of 2 records without JSON serialization:

```
apib -w 5 -c 250 -d 20 http://localhost:8080/api/goals

Warming up: (4 / 5) 1503.814 0% cpu
(4 / 20) 1523.231 0% cpu
(10 / 20) 1322.285 0% cpu
(15 / 20) 1585.761 0% cpu
(20 / 20) 1537.802 0% cpu
Duration:             20.001 seconds
Attempted requests:   29847
Successful requests:  29847
Non-200 results:      0
Connections opened:   0
Socket errors:        0

Throughput:           1492.304 requests/second
Average latency:      167.455 milliseconds
Minimum latency:      90.297 milliseconds
Maximum latency:      503.780 milliseconds
Latency std. dev:     59.115 milliseconds
50% latency:          129.635 milliseconds
90% latency:          232.733 milliseconds
98% latency:          295.129 milliseconds
99% latency:          309.390 milliseconds

Client CPU average:    0%
Client CPU max:        0%
Client memory usage:    0%

Total bytes sent:      2.55 megabytes
Total bytes received:  10.87 megabytes
Send bandwidth:        1.02 megabits / second
Receive bandwidth:     4.35 megabits / second
```
