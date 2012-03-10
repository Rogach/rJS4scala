 #!/bin/bash

while true
do
  (echo $2; cat) | nc localhost $1
  sleep 1 # so it would not bang the downed server at 1000/req per second
done
