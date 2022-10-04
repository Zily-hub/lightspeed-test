package org.example;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class IpCounterApp {

    private static final Path PATH_1 = new File(ClassLoader.getSystemResource("test.txt").getFile()).toPath();
//    private static final Path PATH_2 = Paths.get("C:\\ip_addresses\\ip_addresses");

    public static void main(String[] args) {
        var startTime = System.currentTimeMillis();
        var positiveIntegerIps = new BitSet();
        var negativeIntegerIps = new BitSet();
        final var count = new AtomicLong();
        final var total = new AtomicLong();
        try (var lines = Files.lines(PATH_1)) {
            lines.forEach(line ->  {
                total.incrementAndGet();
                var ipValue = calculateIpAddress(Arrays.stream(line.split("\\."))
                        .map(Short::valueOf).toList());
                if (ipValue >= 0) {
                    if (!positiveIntegerIps.get(ipValue)) {
                        positiveIntegerIps.set(ipValue);
                        count.incrementAndGet();
                    }
                } else {
                    var value = negativeToPositiveInt(ipValue);
                    if (!negativeIntegerIps.get(value)) {
                        negativeIntegerIps.set(value);
                        count.incrementAndGet();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Total ip addresses: " + total.get());
        System.out.println("Count of unique ip addresses is: " + count.get());
        System.out.println("Calculated for " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
    }

    private static int calculateIpAddress(List<Short> ipValues) {
        return ipValues.get(0)*256*256*256 + ipValues.get(1)*256*256 + ipValues.get(2)*256 + ipValues.get(3);
    }

    private static int negativeToPositiveInt(int value) {
        return -value - 1;
    }
}
