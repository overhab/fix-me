import sys
import os
import subprocess


def test(count, brokers):
    for i in range(brokers):
        subprocess.Popen(["java", "-jar", "broker-client.jar", str(count)],
                         stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
        print("Broker " + str(i) + " started")


if __name__ == "__main__":
    count = int(sys.argv[1])
    brokers = int(sys.argv[2])
    test(count, brokers)
