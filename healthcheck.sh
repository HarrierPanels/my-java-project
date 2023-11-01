#!/bin/bash

retry() {
    function="$@"
    count=0
    max=5
    sleep=5

    while :; do
        $function && break
        if [ $count -lt $max ]; then
            ((count++))
            echo "Command failed, retrying after $sleep seconds... $count/$max"
            sleep $sleep
            continue
        fi
        echo "Command failed, out of retries." && exit 1
    done
}

healthcheck() {
    curl -Ls "$1" | grep -E "$2" >/dev/null 2>&1
    [ $? -ne 0 ] && echo "Host unreachable" && return 1
    echo "Host OK"
}

# Health Check
retry healthcheck "$@"
