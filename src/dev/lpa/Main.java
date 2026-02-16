package dev.lpa;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class Main {
    static void main() {
        Consumer<ByteBuffer> printBuffer = buffer -> {
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            System.out.printf("\"%s\"", new String(data, StandardCharsets.UTF_8));
        };
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        doOperation("Print", buffer, (b) -> System.out.println(b + " "));
        doOperation("Write", buffer, (b) -> b.put("This is a test".getBytes()));
        doOperation("Flip (from Write to Read)", buffer, ByteBuffer::flip);
        doOperation("Read and Print Values", buffer, printBuffer);
        doOperation("Flip (from Read to Write)", buffer, ByteBuffer::flip);
        doOperation("1. Move position to the end", buffer, (b) -> b.position(b.limit()));
        doOperation("2. Change limit to capacity", buffer, (b) -> b.limit(b.capacity()));
        doOperation("Append: ", buffer, (b) -> b.put(" This is a test".getBytes()));
//        doOperation("Flip (from Write to Read)", buffer, ByteBuffer::flip);
        doOperation("Read and Print Values", buffer.slice(0, buffer.position()), printBuffer);
        doOperation("Append: ", buffer, (b) -> b.put(" ******".getBytes()));
        doOperation("Read and Print Values", buffer.slice(0, buffer.position()), printBuffer);


    }

    private static void doOperation(String op, ByteBuffer buffer, Consumer<ByteBuffer> c) {
        System.out.println("%-30s".formatted(op));
        c.accept(buffer);
        System.out.printf("Capacity = %d, Limit = %d, Position = %d, Remaining = %d%n",
                buffer.capacity(), buffer.limit(), buffer.position(), buffer.remaining());

    }
}
