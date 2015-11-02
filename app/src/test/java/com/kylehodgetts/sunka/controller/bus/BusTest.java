package com.kylehodgetts.sunka.controller.bus;

import android.app.Activity;

import com.kylehodgetts.sunka.util.Tuple2;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Test Class for the the event bus
 */
public class BusTest{

    private EventBus<TestState> bus;
    private EventHandler<TestState> handler;

    private int renderTry;
    private int eventsReceived;

    /**
     * Set's up a new board object before each test is run ensuring it is at its default
     * state before the test.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        bus = new EventBus<>(new TestState(),null);
        handler = new EventHandler<TestState>("TestHandler") {
            @Override
            public Tuple2<TestState, Boolean> handleEvent(Event event, TestState state, EventBus<TestState> bus) {
                TestState newState = state.incrementEventCall();
                eventsReceived = newState.getEventCalls();
                return new Tuple2<>(newState,newState.getEventCalls()%2==0);
            }

            @Override
            public void updateView(TestState state, Activity activity) {
                renderTry++;
            }
        };

        renderTry = 0;
        eventsReceived = 0;
    }

    /**
     * Tests that the events are fed one by one to the registered handlers
     * @throws Exception
     */
    @Test
    public void eventFeed() throws Exception {
        bus.registerHandler(handler);
        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});

        assertEquals(2,eventsReceived);
    }

    /**
     * Tests that the handlers when requested are added and removed to/from the bus
     * @throws Exception
     */
    @Test
    public void registering() throws Exception {
        bus.registerHandler(handler);

        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});

        bus.removeHandler(handler.getId());

        bus.feedEvent(new Event() {});
        assertEquals(3,eventsReceived);
    }

    /**
     * Thats that the bus will not allow to add two handlers with the same id
     * @throws Exception
     */
    @Test
    public void testSameId() throws Exception {
        bus.registerHandler(handler);
        boolean res = bus.registerHandler(handler);
        assertEquals(res,false);
    }

    /**
     * Tests that when more handlers registered they each of them receives correct new state from previous handler
     * @throws Exception
     */
    @Test
    public void testMultipleHandlers() throws Exception {
        bus.registerHandler(handler);
        bus.registerHandler(new EventHandler<TestState>("TestHandler2") {
            @Override
            public Tuple2<TestState, Boolean> handleEvent(Event event, TestState state, EventBus<TestState> bus) {
                TestState newState = state.incrementEventCall();
                eventsReceived = newState.getEventCalls();
                return new Tuple2<>(newState,newState.getEventCalls()%2==0);
            }

            @Override
            public void updateView(TestState state, Activity activity) {
            }
        });

        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});

        bus.removeHandler(handler.getId());

        bus.feedEvent(new Event() {});
        assertEquals(7,eventsReceived);
    }


    /**
     * Tests that the render call is made correctly
     * @throws Exception
     */
    @Test
    public void testRenderCall() throws Exception {
        bus.registerHandler(handler);

        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});
        bus.feedEvent(new Event() {});

        assertEquals(2,renderTry);
    }

    /**
     * Tests that for one cascading event there is only one possible render call
     * @throws Exception
     */
    @Test
    public void testRenderCallNested() throws Exception {
        bus.registerHandler(new EventHandler<TestState>("NestedHandler") {
            @Override
            public Tuple2<TestState, Boolean> handleEvent(Event event, TestState state, EventBus<TestState> bus) {
                TestState newState = state.incrementEventCall();
                eventsReceived = newState.getEventCalls();
                if (eventsReceived < 6)bus.feedEvent(new Event() {});
                return new Tuple2<>(newState, newState.getEventCalls() % 2 == 0);
            }

            @Override
            public void updateView(TestState state, Activity activity) {
                renderTry++;
            }
        });

        bus.feedEvent(new Event() {});

        assertEquals(1,renderTry);
        assertEquals(6,eventsReceived);
    }
}