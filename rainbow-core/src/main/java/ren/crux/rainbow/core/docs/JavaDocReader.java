package ren.crux.rainbow.core.docs;

import com.sun.javadoc.RootDoc;
import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.core.model.Document;

public class JavaDocReader {

    private static RootDoc mRoot;

    private static Document read(String path, String[] packageNames, CallBack callBack) {
        if (callBack != null) {
            return callBack.call(path, packageNames, mRoot);
        }
        return null;
    }

    public static Document readDoc(String path, String[] packageNames, String[] executeParams, CallBack callBack) {
        try {
            com.sun.tools.javadoc.Main.execute(executeParams);
            return read(path, packageNames, callBack);
        } catch (Exception e) {
            if (callBack != null) {
                callBack.onError(e);
            }
        }
        return null;
    }

    public static Document readDoc(String path, String[] packageNames, CallBack callBack) {
        return readDoc(path, packageNames, ArrayUtils.addAll(new String[]{
                "-private", "-doclet", Doclet.class.getName(),
                "-encoding", "utf-8",
                "-sourcepath",
                path,
                "-subpackages"}, packageNames), callBack);
    }

    public interface CallBack {

        Document call(String path, String[] packageNames, RootDoc rootDoc);

        void onError(Exception e);

    }

    public static class Doclet {
        public static boolean start(final RootDoc root) {
            mRoot = root;
            return true;
        }
    }

}
