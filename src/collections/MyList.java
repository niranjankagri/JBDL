package collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyList {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();     //[][][]
        list.add(1);
        list.remove(0);
        list.size();

        List<Integer> linkedList = new LinkedList<>();      //[]->[]->[
        linkedList.add(1);
        linkedList.remove(0);
        linkedList.size();
    }
}
