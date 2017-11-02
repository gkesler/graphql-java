/*
 * Copyright 2016 Intuit Inc. All rights reserved. Unauthorized reproduction
 * is a violation of applicable law. This material contains certain
 * confidential or proprietary information and trade secrets of Intuit Inc.
 * 
 * graphql.language.LanguageUtil.java
 * 
 * Created: Nov 1, 2017 10:45:45 AM
 * Author: gkesler
 */
package graphql.language;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Queue;

/**
 *
 * @author gkesler
 */
public class LanguageUtil {
    // disable instantiation
    private LanguageUtil () {
    }
    
    public enum Markers {
        END_LIST,   // signals to the traverse mechanizm that the list of the children had been processed
        STOP,       // signals to traverse mechanizm to abort traversal of the current element
        QUIT        // signals to the traverse mechanizm to immediately stop the traversal
    }
    
    public static <U> U depthFirst (Collection<? extends Node> roots, U initialValue, NodeVisitor<U> visitor) {
        return traverse(roots, initialValue, visitor, new StackDecorator() {            
            @Override
            public Object peek() {
                return delegate.peek();
            }

            @Override
            public Object pop() {
                return delegate.pop();
            }

            @Override
            public void push(Node o) {
                delegate.push(o);
            }

            @Override
            public void push(Node o, List<? extends Node> children) {
                // need to push the element first to allow to signal "leave" on visitor
                // so it'll be extracted after the marker
                delegate.push(o);
                delegate.push(Markers.END_LIST);
                
                // and now push children to the stack
                // in the reverse order, so they'll be
                // extracted from the stack in the correct one
                if (children == null) {
                    children = Collections.emptyList();
                }
                
                for (ListIterator<? extends Node> it = children.listIterator(children.size()); it.hasPrevious(); delegate.push(it.previous()));
            }

            @Override
            public boolean addAll(Collection<? extends Node> list) {
                return delegate.addAll(list);
            }

            @Override
            public boolean isEmpty() {
                return delegate.isEmpty();
            }
            
            final Deque<Object> delegate = new ArrayDeque<>();
        });
    }
    
    public static <U> U breadthFirst (Collection<? extends Node> roots, U initialValue, NodeVisitor<U> visitor) {
        return traverse(roots, initialValue, visitor, new StackDecorator() {
            @Override
            public Object peek() {
                return delegate.peek();
            }

            @Override
            public Object pop() {
                return delegate.remove();
            }

            @Override
            public void push(Node o) {
                delegate.offer(o);
            }

            @Override
            public void push(Node o, List<? extends Node> children) {
                // we'll put children first into this FIFO structure
                if (children == null) {
                    children = Collections.emptyList();
                }
                
                for (ListIterator<? extends Node> it = children.listIterator(); it.hasNext(); delegate.offer(it.next()));
                
                // followed by the END_LIST marker
                // and the parent element in order to trigger
                // "leave" action on the visitor
                delegate.offer(Markers.END_LIST);
                delegate.offer(o);
            }

            @Override
            public boolean addAll(Collection<? extends Node> list) {
                return delegate.addAll(list);
            }

            @Override
            public boolean isEmpty() {
                return delegate.isEmpty();
            }
            
            final Queue<Object> delegate = new ArrayDeque<>();
        });
    }
    
    /**
     * Decorates a Stack or Queue to decouple them from the push-down traversal<br>
     * - If underlying structure is LIFO - the most recently discovered elements will be visited first, that 
     * corresponds to depth-first traversal
     * - If underlying structure is FIFO -  the most recently discovered elements will be visited after the elements
     * already in the queue and that corresponds to breadth-first traversal
     * 
     * @param <T> type boundary of the elements in the stack
     */
    private interface StackDecorator {
        Object peek ();
        Object pop ();
        void push (Node o);
        void push (Node o, List<? extends Node> children);
        boolean addAll (Collection<? extends Node> list);
        boolean isEmpty ();
    }
    
    private static <U> U traverse (Collection<? extends Node> roots, U initialValue, NodeVisitor<U> visitor, StackDecorator stack) {
        Objects.requireNonNull(roots);
        Objects.requireNonNull(visitor);
        Objects.requireNonNull(stack);
        
        Object data = initialValue;
        // seed the stack with the provided roots
        stack.addAll(roots);
        
        // the main loop until we've visited (extracted) all roots
        // from the stack or explicitly aborted by the client
        while (!(stack.isEmpty() || (data = traverseOne(stack, visitor, data)) == Markers.QUIT));
        
        // and return the accumulated data
        return !(data == Markers.QUIT || data == Markers.STOP)
            ? (U)data
            : null;
    }
    
    private static <U> Object traverseOne (StackDecorator stack, NodeVisitor<U> visitor, Object data) {
        Object top = stack.pop();
        if (top == Markers.END_LIST) {
            // end-of-list state is signalled, take the next element
            // from the stack and call visitor.leave to sgnal to the client
            // that we are done processing the element
            data = visitor.leave((Node)stack.pop(), (U)data);
        } else {
            // the current element to visit
            // pass visitor to its accept method, it'll be double dispatched back
            // to the right visitor's one
            data = ((Node)top).accept(visitor, (U)data);
            // and push children to the stack
            stack.push((Node)top, 
                data != Markers.STOP
                    ? ((Node)top).getChildren() 
                    : Collections.emptyList());
        }
        
        return data;
    }
}
