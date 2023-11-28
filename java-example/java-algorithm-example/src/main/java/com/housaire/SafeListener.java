package com.housaire;

/**
 * Created by kay on 2019/12/2.
 */
public class SafeListener {
    private final EventListener listener;

    private SafeListener() {
        listener = new EventListener() {
            @Override
            public void onEvent(Event e) {
                doSomething(e);
        }
        };
    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safe = new SafeListener();
        source.registerListener(safe.listener);
        return safe;
    }

    void doSomething(Event e) {
        System.out.println("hi event " + e);
    }

    interface EventSource {
        void registerListener(EventListener e);
    }

    interface EventListener {
        void onEvent(Event e);
    }

    interface Event {
    }

    public static void main(String[] args) {
        SafeListener listener = SafeListener.newInstance(new EventSource() {
            @Override
            public void registerListener(EventListener e) {
                    e.onEvent(new Event() {
                });
            }
        });
    }

}
