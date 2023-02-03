package com.inmaytide.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class StreamExecuteTest {

    @Test
    public void test() {
        List<Operate> list = new ArrayList<>();
        list.add(new Operate(false));
        list.add(new Operate(false));
        list.add(new Operate(true));
        list.add(new Operate(true));
        list.add(new Operate(false));

        Operate value = list.stream().map(Operate::identity).filter(Operate::getValue).findFirst().orElse(null);
        Assertions.assertNotNull(value);
        Assertions.assertEquals(list.get(0).getCount(), 1);
        Assertions.assertEquals(list.get(1).getCount(), 1);
        Assertions.assertEquals(list.get(2).getCount(), 1);
        Assertions.assertEquals(list.get(3).getCount(), 0);
        Assertions.assertEquals(list.get(4).getCount(), 0);
    }

    static class Operate {

        private final boolean value;

        private int count = 0;

        public Operate(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return this.value;
        }

        public Operate identity() {
            count++;
            return this;
        }

        public int getCount() {
            return count;
        }
    }

}
