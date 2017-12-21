package annotations;

import nlpstack.annotations.Parser.LinkedList;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class LinkedListTests {
    @Test
    public void simpleLinkedListTest() {
        LinkedList<Integer> list = new LinkedList<>(0);
        list.appendAtTail(1);
        list.appendAtTail(2);
        LinkedList<Integer> listToAppend = new LinkedList<>(3);
        listToAppend.appendAtTail(4);
        listToAppend.appendAtTail(5);
        list.appendAtTail(listToAppend);

        Integer i = 0;
        for (Integer listInteger : list) {
            assertEquals(i, listInteger);
            i += 1;
        }

        assertEquals(6, list.size());

        ArrayList<Integer> arrayList = list.toArrayList();
        assertEquals(6, arrayList.size());
        i = 0;
        for (Integer listInteger : arrayList) {
            assertEquals(i, listInteger);
            i += 1;
        }
    }
}
