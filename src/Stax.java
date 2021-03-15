import java.util.*;
public class Stax<E>
{
    private LinkedList<E> stk;
    private int size;

    public Stax ( )
    {
        size = 0;
        stk = new LinkedList<E>();

    }

    public void push( E data )
    {
        stk.add(data);
        size++;
    }

    public E peek( )
    {
        return stk.get(size-1);
    }

    public E pop ( )
    {
        E temp = stk.remove(size-1);
        size--;
        return temp;
    }

    public boolean isEmpty ( )
    {
        return stk.isEmpty();
    }

    public int length( )
    {
        return size;
    }

    public String toString()
    {
        String str = "";
        for(int x = size-1; x >= 0; x--)
        {
            str += (stk.get(x) + "\n");

        }
        return str;
    }
}
