package nlpstack.annotations.Parser;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Simple linked list implementation. The reason we are using a custom linked list and not the java implementation
 * it to be able to append 2 lists in O(1) (the java LinkedList does it in O(n))
 * @param <T> The type of the elements contained in the linked list
 */
public class LinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> last;
    private int length;

    public LinkedList(T head) {
        this.head = new Node<>(head, null);
        this.last = this.head;
        length = 1;
    }

    public void appendAtTail(T el) {
        this.last.next = new Node<>(el, null);
        this.last = this.last.next;
        length += 1;
    }

    public void appendAtTail(LinkedList<T> list) {
        this.last.next = list.head;
        this.last = list.last;
        length += list.length;
    }

    public ArrayList<T> toArrayList() {
        ArrayList<T> out = new ArrayList<>(length);
        for(T el : this) {
            out.add(el);
        }
        return out;
    }

    public int size() {
        return length;
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator<>(head);
    }
}

class Node<T> {
    T element;
    Node<T> next;

    Node(T element, Node<T> next) {
        this.element = element;
        this.next = next;
    }
}

class ListIterator<T> implements Iterator<T> {
    private Node<T> iteratorPointer;

    ListIterator(Node<T> head) {
        iteratorPointer = head;
    }

    @Override
    public boolean hasNext() {
        return iteratorPointer != null;
    }

    @Override
    public T next() {
        T out = iteratorPointer.element;
        iteratorPointer = iteratorPointer.next;
        return out;
    }
}