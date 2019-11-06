package ren.crux.rainbow.core.docs;

import com.sun.javadoc.RootDoc;

public class JavaDocReader {

    private static RootDoc mRoot;

    private static String read(String path, String packageName, CallBack callBack) {
        if (callBack != null) {
            return callBack.call(path, packageName, mRoot);
        }
        return null;
    }

    public static String readDoc(String path, String packageName, String[] executeParams, CallBack callBack) {
        try {
            com.sun.tools.javadoc.Main.execute(executeParams);
            return read(path, packageName, callBack);
        } catch (Exception e) {
            if (callBack != null) {
                callBack.onError(e);
            }
        }
        return null;
    }

    public static String readDoc(String path, String packageName, CallBack callBack) {
        return readDoc(path, packageName, new String[]{
                "-private", "-doclet", Doclet.class.getName(),
                "-encoding", "utf-8",
                "-sourcepath",
                path,
                "-subpackages",
                packageName}, callBack);
    }

    public interface CallBack {

        String call(String path, String packageName, RootDoc rootDoc);

        void onError(Exception e);

    }

    public static class Doclet {
        public static boolean start(final RootDoc root) {
            mRoot = root;
            return true;
        }
    }

}
