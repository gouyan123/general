package com.bjsxt.集合.linkedlist;

public class SxtLinkedList {
    private Node first;
    private Node last;
    private int size;

    public void add(Object o){
        Node n = new Node();
        /*如果列表中第一个节点为空，则将新建的节点作为该链表第一个节点*/
        if(this.first == null){
            n.setPrevious(null);
            n.setObj(o);
            n.setNext(null);
            this.first = n;
            this.last = n;
        }else{
            /*如果第一个节点不为空，则将新建的节点放在最后一个节点后面*/
            n.setPrevious(this.last);
            n.setObj(o);
            n.setNext(null);
            this.last.setNext(n);
            this.last = n;
        }
        this.size++;
    }
    /*获取链表中的第 index 个节点node*/
    public Node node(int index){
        Node temp = null;
        if(this.first != null){
            temp = this.first;
            for (int i=0;i<index;i++){
                temp = temp.getNext();
            }
        }
        return temp;
    }

    public Object get(int index){
        rangecheck(index);
        Node temp = this.node(index);
        if (temp != null){
            return temp.getObj();
        }
        return null;
    }
    public void remove(int index){
        Node temp = this.node(index);
        if (temp != null){
            Node up = temp.getPrevious();
            Node down = temp.getNext();
            /*链表中只有一个节点，删除该节点*/
            if (this.first == temp && this.last == temp){
                this.first = null;
                this.last = null;
                this.size--;
                /*要删除的节点是头节点*/
            }else if (this.first == temp && this.last != temp){
                /*取出头节点的下一个节点，来当新的头节点*/
                Node next = this.first.getNext();
                next.setPrevious(null);
                this.first = next;
                this.size--;
            }
            /*要删除的节点是尾节点*/
            else if (this.first != temp && this.last == temp){
                /*取出尾节点的上一个节点，来当新的尾节点*/
                Node previous = this.last.getPrevious();
                previous.setNext(null);
                this.last = previous;
                this.size--;
                /*要删除的节点不是头尾节点*/
            }else if (this.first != temp && this.last != temp){
                Node previous = temp.getPrevious();
                Node next = temp.getNext();
                previous.setNext(next);
                next.setPrevious(previous);
                this.size--;
            }

        }
    }
    public int size(){
        return this.size;
    }
    public void rangecheck(int index){
        if(index < 0 || index >= size){
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        SxtLinkedList list = new SxtLinkedList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.remove(3);
        //System.out.println(list.get(0));
        for (int i=0;i<list.size();i++){
            System.out.println(list.get(i));
        }
    }
}
