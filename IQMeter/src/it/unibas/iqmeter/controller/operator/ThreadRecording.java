package it.unibas.iqmeter.controller.operator;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import de.ksquared.system.keyboard.GlobalKeyListener;
import de.ksquared.system.keyboard.KeyAdapter;
import de.ksquared.system.keyboard.KeyEvent;
import de.ksquared.system.mouse.GlobalMouseListener;
import de.ksquared.system.mouse.MouseEvent;
import de.ksquared.system.mouse.MouseListener;
import it.unibas.ping.contrib.PingThreadWorker;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThreadRecording extends PingThreadWorker {

    private static Log logger = LogFactory.getLog(ThreadRecording.class);
    private static ThreadRecording singleton;
    public int PROCESS_VM_READ = 0x0010;
    public int PROCESS_QUERY_INFORMATION = 0x0400;
    public User32 USER32 = User32.INSTANCE;
//    public User32 USER32 = null;
    public ThreadRecording.Psapi PSAPI = ThreadRecording.Psapi.INSTANCE;
    private int MAX_TITLE_LEGHT = 1024;

    private boolean record = false;
    private boolean started = false;
    private String processToRecord = null;

    private Map<String, Integer> clickMap = new HashMap<String, Integer>();
    private Map<String, Integer> keyboardInputsMap = new HashMap<String, Integer>();
    public static final String SPICY = "spicy.exe";
    public static final String CLOVER_ETL = "javaw.exe";
    public static final String MAP_FORCE = "MapForce.exe";

    public static ThreadRecording getInstance() {
        if (singleton != null) {
            try{
                singleton = new ThreadRecording();
            }catch(Exception e){}
        }
        return singleton;
    }

    private ThreadRecording() {
        this.clickMap.put(SPICY, 0);
        this.clickMap.put(CLOVER_ETL, 0);
        this.clickMap.put(MAP_FORCE, 0);
        this.keyboardInputsMap.put(SPICY, 0);
        this.keyboardInputsMap.put(CLOVER_ETL, 0);
        this.keyboardInputsMap.put(MAP_FORCE, 0);
    }

    public void resetForTool(String tool) {
        this.clickMap.put(tool, 0);
        this.keyboardInputsMap.put(tool, 0);
    }
    public int getClickCounter(String tool) {
        return this.clickMap.get(tool);
    }

    public void setClickCounter(String tool, int value) {
        this.clickMap.put(tool, value);
    }

    public int getKeyboardCounter(String tool) {
        return this.keyboardInputsMap.get(tool);
    }

    public void setKeyboardCounter(String tool, int value) {
        this.keyboardInputsMap.put(tool, value);
    }

    public void setRecord(boolean record) {
        this.record = record;
    }

    public boolean isStarted() {
        return started;
    }

    public void startThread() {
        this.started = true;
        this.start();
    }

    public void setProcessToRecord(String process) {
        this.processToRecord = process;
    }

    public String getProcessFromPanel(String panelName) {
        if (panelName.equalsIgnoreCase("spicy")) {
            return SPICY;
        }
        if (panelName.equalsIgnoreCase("cloveretl")) {
            return CLOVER_ETL;
        }
        if (panelName.equalsIgnoreCase("altova_mapforce")) {
            return MAP_FORCE;
        }
        return null;
    }

    @Override
    public Object avvia() {
        new GlobalMouseListener().addMouseListener(new MyMouse());
        new GlobalKeyListener().addKeyListener(new MyKeyboard());
        while (record) {
        }
        return null;
    }

    @Override
    public void concludi() {
        record = false;
        processToRecord = null;
    }

    private void updateData(String process) {
        int click = clickMap.get(process);
        click++;
        clickMap.put(process, click);
        if (logger.isDebugEnabled()) {
            logger.debug("Adding one click for: " + process);
        }
    }

    private void updateDataKeyboard(String process) {
        int click = keyboardInputsMap.get(process);
        click++;
        keyboardInputsMap.put(process, click);
        if (logger.isDebugEnabled()) {
            logger.debug("Adding one keyboard input for: " + process);
        }
    }

    private class MyMouse implements MouseListener {

        @Override
        public void mouseMoved(MouseEvent me) {
        }

        @Override
        public void mousePressed(MouseEvent me) {
            int button = me.getButton();
            if (record && processToRecord != null && (button == MouseEvent.BUTTON_LEFT || button == MouseEvent.BUTTON_RIGHT)) {
                if (Platform.isWindows()) {
                    char[] buffer = new char[MAX_TITLE_LEGHT * 2];
                    WinDef.HWND windowHandle = USER32.GetForegroundWindow();
                    USER32.GetWindowText(windowHandle, buffer, MAX_TITLE_LEGHT);
//                    System.out.println("Active Window title: " + Native.toString(buffer));
                    PointerByReference pointer = new PointerByReference();
                    User32DLL.GetWindowThreadProcessId(User32DLL.GetForegroundWindow(), pointer);
                    Pointer process = Kernel32.OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, false, pointer.getValue());
                    char[] bufferByte = new char[MAX_TITLE_LEGHT * 2];
                    PSAPI.GetModuleBaseNameW(process, null, bufferByte, MAX_TITLE_LEGHT);
                    String processName = Native.toString(bufferByte);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Process: " + processName);
                    }
                    if (processName.equalsIgnoreCase(processToRecord)) {
                        updateData(processName);
                    }
                } else {
                    throw new UnsupportedOperationException("This does not work in a non Windows System");
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }
    }

    private class MyKeyboard extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {
            if (record && processToRecord != null) {
                if (Platform.isWindows()) {
                    char[] buffer = new char[MAX_TITLE_LEGHT * 2];
                    WinDef.HWND windowHandle = USER32.GetForegroundWindow();
                    USER32.GetWindowText(windowHandle, buffer, MAX_TITLE_LEGHT);
//                    System.out.println("Active Window title: " + Native.toString(buffer));
                    PointerByReference pointer = new PointerByReference();
                    User32DLL.GetWindowThreadProcessId(User32DLL.GetForegroundWindow(), pointer);
                    Pointer process = Kernel32.OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, false, pointer.getValue());
                    char[] bufferByte = new char[MAX_TITLE_LEGHT * 2];
                    PSAPI.GetModuleBaseNameW(process, null, bufferByte, MAX_TITLE_LEGHT);
                    String processName = Native.toString(bufferByte);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Process: " + processName);
                    }
                    if (processName.equalsIgnoreCase(processToRecord)) {
                        if (!event.isAltPressed() && !event.isCtrlPressed() && !event.isShiftPressed()) {
                            updateDataKeyboard(processName);
                        }
                    }
                } else {
                    throw new UnsupportedOperationException("This does not work in a non Windows System");
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent event) {
            boolean isComposite = false;
            if (event.isAltPressed() || event.isCtrlPressed() || event.isShiftPressed()) {
                isComposite = true;
            }
            if (record && isComposite && processToRecord != null) {
                if (Platform.isWindows()) {
                    char[] buffer = new char[MAX_TITLE_LEGHT * 2];
                    WinDef.HWND windowHandle = USER32.GetForegroundWindow();
                    USER32.GetWindowText(windowHandle, buffer, MAX_TITLE_LEGHT);
//                    System.out.println("Active Window title: " + Native.toString(buffer));
                    PointerByReference pointer = new PointerByReference();
                    User32DLL.GetWindowThreadProcessId(User32DLL.GetForegroundWindow(), pointer);
                    Pointer process = Kernel32.OpenProcess(PROCESS_QUERY_INFORMATION | PROCESS_VM_READ, false, pointer.getValue());
                    char[] bufferByte = new char[MAX_TITLE_LEGHT * 2];
                    PSAPI.GetModuleBaseNameW(process, null, bufferByte, MAX_TITLE_LEGHT);
                    String processName = Native.toString(bufferByte);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Process: " + processName);
                    }
                    if (processName.equalsIgnoreCase(processToRecord)) {
                        updateDataKeyboard(processName);
                    }
                } else {
                    throw new UnsupportedOperationException("This does not work in a non Windows System");
                }
            }
        }

    }

    public interface Psapi extends StdCallLibrary {

        Psapi INSTANCE = (Psapi) Native.loadLibrary("Psapi", Psapi.class);

        WinDef.DWORD GetModuleBaseNameW(Pointer hProcess, Pointer hModule, char[] lpBaseName, int nSize);
    }

    static class Kernel32 {

        static {
            Native.register("kernel32");
        }

        public static native int GetLastError();

        public static native Pointer OpenProcess(int dwDesiredAccess, boolean bInheritHandle, Pointer pointer);
    }

    static class User32DLL {

        static {
            Native.register("user32");
        }

        public static native int GetWindowThreadProcessId(WinDef.HWND hwnd, PointerByReference pbr);

        public static native WinDef.HWND GetForegroundWindow();

        public static native int GetWindowTextW(WinDef.HWND hwnd, char[] lpString, int nMaxCount);
    }

}
