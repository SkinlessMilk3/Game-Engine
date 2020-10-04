package API.EventListeners;

import java.util.ArrayList;
import java.util.List;

public class WindowResizeDispatcher {

    private static List<WindowResizeListener> listeners = new ArrayList<>();

    public static void WindowReizeCallback(long wnd, int width, int height){


        for(WindowResizeListener tmp : listeners){
            tmp.onWindowResizeEvent(width, height);
        }
    }

    public static void addListener(WindowResizeListener ls){
        listeners.add(ls);
    }
}
